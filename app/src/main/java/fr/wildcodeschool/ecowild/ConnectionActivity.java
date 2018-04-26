package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionActivity extends AppCompatActivity {

    public static final int PASSWORD_HIDDEN = 1;
    public static final int PASSWORD_VISIBLE = 2;
    public static final String CACHE_USERNAME = "username";
    public static final String CACHE_PASSWORD = "password";
    static final int REQUEST_TAKE_PHOTO = 800;
    public static boolean CONNECTED = false;
    static Bitmap mPhotography;
    int mPasswordVisibility = PASSWORD_HIDDEN;
    String mCurrentPhotoPath;
    private Uri mPhotoUri = null;
    private StorageReference mStorageRef;


    //TODO mot de passe en crypter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("utilisateurs");

        final EditText editTextProfil = findViewById(R.id.edit_text_profil_connection);
        final EditText editTextPassword = findViewById(R.id.edit_text_password_connection);
        final EditText editTextPassword2 = findViewById(R.id.edit_text_password2);
        final ImageView imageViewPassword = findViewById(R.id.image_view_password_connection);
        final ImageView imageViewPassword2 = findViewById(R.id.image_view_password2);
        final ImageView ivLigne = findViewById(R.id.image_view_ligne);
        final ImageView ivLigneBis = findViewById(R.id.image_view_ligneBis);
        final TextView tvWhere = findViewById(R.id.text_view_where);
        final TextView textViewForgottenPassword = findViewById(R.id.text_view_forgotten_password);
        final ImageView ivPhoto = findViewById(R.id.iv_photo);
        final ImageView ivAppareilPhoto = findViewById(R.id.iv_appareil_photo);


        final Button buttonToLogIn = findViewById(R.id.button_log_in);
        final Button buttonMember = findViewById(R.id.button_create);
        final CheckBox checkBoxToLogIn = findViewById(R.id.check_box_connection);


        final SharedPreferences sharedPrefProfil = this.getSharedPreferences("ECOWILD", Context.MODE_PRIVATE);
        final String username = sharedPrefProfil.getString(CACHE_USERNAME, "");
        editTextProfil.setText(username);

        final SharedPreferences sharedPreferencesPassword = this.getPreferences(Context.MODE_PRIVATE);
        final String password = sharedPreferencesPassword.getString(CACHE_PASSWORD, "");
        editTextPassword.setText(password);

        /**cryptage mot de passe**/
        imageViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        imageViewPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    editTextPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    editTextPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });
        /**Mot de passe oublier*/
        textViewForgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForgottenPassword = new Intent(ConnectionActivity.this, ForgottenPasswordActivity.class);
                ConnectionActivity.this.startActivity(intentForgottenPassword);
            }
        });

        /**Partie se Connecter*/
        buttonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CONNECTED = true;
                final String editProfil = editTextProfil.getText().toString();
                final String editPassword = editTextPassword.getText().toString();
                final HashCode hashCode = Hashing.sha256().hashString(editPassword, Charset.defaultCharset());
                final String editPassword2 = editTextPassword2.getText().toString();


                if (editProfil.isEmpty() || editPassword.isEmpty()) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));
                    TextView textToast = (TextView) layout.findViewById(R.id.text);
                    textToast.setText(R.string.remplissez_tout_les_champs);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    /**Partie recuperation firebase si deja inscrit pour verification du mot de passe**/
                    DatabaseReference myRef = database.getReference("utilisateurs");
                    myRef.orderByChild("name").equalTo(editProfil).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                String passwordRecup = userModel.getPassword();
                                String avatar = userModel.getAvatar();
                                String name = userModel.getName();
                                int xp = userModel.getXp();
                                int level = userModel.getLevel();

                                ImageView mImageView = findViewById(R.id.iv_photo);
                                Glide.with(ConnectionActivity.this).load(avatar).apply(RequestOptions.circleCropTransform()).into(mImageView);


                                if (passwordRecup.equals(hashCode.toString())) {
                                    Intent intentMap = new Intent(ConnectionActivity.this, MapsActivity.class);
                                    /** partie Singleton*/
                                    userModelSingleton(name, passwordRecup, avatar, xp, level);
                                    ConnectionActivity.this.startActivity(intentMap);
                                } else {
                                    Toast.makeText(ConnectionActivity.this, R.string.error_identification, Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


                }

                if (checkBoxToLogIn.isChecked()) {
                    SharedPreferences.Editor editorProfil = sharedPrefProfil.edit();
                    editorProfil.putString(CACHE_USERNAME, editProfil);
                    editorProfil.putString(CACHE_PASSWORD, editPassword);
                    editorProfil.commit();
                }
            }
        });

        /**Partie devenir membre*/
        buttonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String editPassword = editTextPassword.getText().toString();
                final HashCode hashCode = Hashing.sha256().hashString(editPassword, Charset.defaultCharset());

                final String editPassword2 = editTextPassword2.getText().toString();
                final String editProfil = editTextProfil.getText().toString();
                ivAppareilPhoto.setVisibility(View.VISIBLE);
                editTextPassword2.setVisibility(View.VISIBLE);
                imageViewPassword2.setVisibility(View.VISIBLE);
                buttonToLogIn.setVisibility(View.GONE);
                ivLigne.setVisibility(View.GONE);
                ivLigneBis.setVisibility(View.GONE);
                tvWhere.setVisibility(View.GONE);
                textViewForgottenPassword.setVisibility(View.GONE);

                if (editPassword2.isEmpty() || editPassword.isEmpty()) {
                } else if (editPassword2.isEmpty()) {


                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView textToast = (TextView) layout.findViewById(R.id.text);
                    textToast.setText(R.string.confirmation);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                } else if (!editPassword.equals(editPassword2)) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView textToast = (TextView) layout.findViewById(R.id.text);
                    textToast.setText(R.string.error);

                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                } else {
                    CONNECTED = true;
                    final Intent intentMap = new Intent(ConnectionActivity.this, MapsActivity.class);

                    /**Partie recuperation firebase pour verif si name n'est pas pris**/
                    DatabaseReference myRef = database.getReference("utilisateurs");
                    myRef.orderByChild("name").equalTo(editProfil).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /**si il ne trouve pas de correspondance il envoit les infos*/
                            if (dataSnapshot.getChildrenCount() == 0) {

                                if (mPhotoUri == null) {
                                    /**Partie Firease envoit si il ne prend pas de photo*/
                                    UserModel userModel = new UserModel(editProfil, hashCode.toString(), null, 0, 1);
                                    String userKey = userRef.push().getKey();
                                    userRef.child(userKey).setValue(userModel);

                                    /**Partie Singleton*/
                                    userModelSingleton(editProfil, hashCode.toString(), null, 0, 1);

                                    ConnectionActivity.this.startActivity(intentMap);
                                } else {
                                    /**Partie Firebase envoit avec photo*/

                                    //sockage dans firebase avec Uri
                                    mStorageRef = FirebaseStorage.getInstance().getReference();

                                    StorageReference photoRef = mStorageRef.child("images/" + mPhotoUri.getLastPathSegment());
                                    UploadTask uploadTask = photoRef.putFile(mPhotoUri);

                                    // Register observers to listen for when the download is done or if it fails
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(ConnectionActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                            UserModel userModel = new UserModel(editProfil, hashCode.toString(), downloadUrl.toString(), 0, 1);
                                            String userKey = userRef.push().getKey();
                                            userRef.child(userKey).setValue(userModel);

                                            /**Partie Singleton*/
                                            userModelSingleton(editProfil, hashCode.toString(), downloadUrl.toString(), 0, 1);


                                            ConnectionActivity.this.startActivity(intentMap);
                                        }
                                    });


                                }
                                return;
                            }
                            /** si il y a une correspondance**/
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                String name = userModel.getName();

                                if (name.equals(editProfil)) {
                                    Toast.makeText(ConnectionActivity.this, R.string.repeat, Toast.LENGTH_LONG).show();
                                }

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    if (checkBoxToLogIn.isChecked()) {
                        SharedPreferences.Editor editorProfil = sharedPrefProfil.edit();
                        editorProfil.putString(CACHE_USERNAME, editProfil);
                        editorProfil.putString(CACHE_PASSWORD, editPassword);
                        editorProfil.commit();
                    }

                }


            }
        });


        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //qd click sur avatar lance toutes les methodes
                dispatchTakePictureIntent();
            }
        });


    }

    // apl le app photo intent photo
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mPhotoUri = FileProvider.getUriForFile(this,
                        "fr.wildcodeschool.ecowild.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    // creation de limage dans le stockage local
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //recuperer le resultat de la photo stocker en local
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView mImageView = findViewById(R.id.iv_photo);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Glide.with(ConnectionActivity.this).load(mPhotoUri).apply(RequestOptions.circleCropTransform()).into(mImageView);


        }
    }

    public void userModelSingleton(String textName, String textPassword, String textAvatar, int intXp, int intLevel) {
        UserSingleton userSingleton = UserSingleton.getInstance();
        userSingleton.setTextName(textName);
        userSingleton.setTextPassword(textPassword);
        userSingleton.setTextAvatar(textAvatar);
        userSingleton.setIntXp(intXp);
        userSingleton.setIntLevel(intLevel);
    }

}
