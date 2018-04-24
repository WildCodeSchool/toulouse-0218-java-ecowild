package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionActivity extends AppCompatActivity {

    public static final int PASSWORD_HIDDEN = 1;
    public static final int PASSWORD_VISIBLE = 2;
    public static final String CACHE_USERNAME = "username";
    public static final String CACHE_PASSWORD = "password";
    public static boolean CONNECTED = false;
    static Bitmap mPhotography;
    int mPasswordVisibility = PASSWORD_HIDDEN;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference utilisateursRef = database.getReference("utilisateurs");

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

        final String editProfil = editTextProfil.getText().toString();
        final String editPassword = editTextPassword.getText().toString();
        final String editPassword2 = editTextPassword2.getText().toString();
        final String passwordU;


        final Button buttonToLogIn = findViewById(R.id.button_log_in);
        Button buttonMember = findViewById(R.id.button_create);
        final CheckBox checkBoxToLogIn = findViewById(R.id.check_box_connection);

        final SharedPreferences sharedPrefProfil = this.getPreferences(Context.MODE_PRIVATE);
        final String username = sharedPrefProfil.getString(CACHE_USERNAME, "");
        editTextProfil.setText(username);

        final SharedPreferences sharedPreferencesPassword = this.getPreferences(Context.MODE_PRIVATE);
        final String password = sharedPreferencesPassword.getString(CACHE_PASSWORD, "");
        editTextPassword.setText(password);

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

        textViewForgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForgottenPassword = new Intent(ConnectionActivity.this, ForgottenPasswordActivity.class);
                ConnectionActivity.this.startActivity(intentForgottenPassword);
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

        buttonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CONNECTED = true;
                final String editProfil = editTextProfil.getText().toString();
                final String editPassword = editTextPassword.getText().toString();
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

                    /**Partie recuperation firebase**/
                    DatabaseReference myRef = database.getReference("utilisateurs");

                    myRef.orderByChild("name").equalTo(editProfil).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UtilisateurModel utilisateurRecup = snapshot.getValue(UtilisateurModel.class);
                                String passwordRecup = utilisateurRecup.getPassword();

                                if (passwordRecup.equals(editPassword)) {
                                    Intent intentMap = new Intent(ConnectionActivity.this, MapsActivity.class);
                                    intentMap.putExtra("username", editProfil);
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

        buttonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String editPassword = editTextPassword.getText().toString();
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


                if (editPassword.equals(editPassword2) && !editPassword.isEmpty() && !editPassword2.isEmpty()) {
                    CONNECTED = true;
                    Intent intentMap = new Intent(ConnectionActivity.this, MapsActivity.class);
                    intentMap.putExtra("username", editProfil);

                    /**Partie Firease envoit**/
                    UtilisateurModel utilisateurs = new UtilisateurModel(editProfil, editPassword,mCurrentPhotoPath, 0, null);
                    String utilisateurKey = utilisateursRef.push().getKey();
                    utilisateursRef.child(utilisateurKey).setValue(utilisateurs);

                    ConnectionActivity.this.startActivity(intentMap);
                }
                if (!editPassword.equals(editPassword2) && !editPassword.isEmpty() && !editPassword2.isEmpty()) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView textToast = (TextView) layout.findViewById(R.id.text);
                    textToast.setText(R.string.error);

                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }

                if (editPassword2.isEmpty() && !editPassword.isEmpty()) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView textToast = (TextView) layout.findViewById(R.id.text);
                    textToast.setText(R.string.confirmation);

                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });

        /** PHOTO PART I (prise photo et sauvegarde image) **/
        /** A noter faire une variable m pour le bouton et l'image (si pas firebase) **/

        /** ancien code
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }

            }
        }); **/

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                    //transformation de la photo en String
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = null;
                    try {
                        image = File.createTempFile(
                                imageFileName,  /* prefix */
                                ".jpg",         /* suffix */
                                storageDir      /* directory */
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Save a file: path for use with ACTION_VIEW intents
                    mCurrentPhotoPath = image.getAbsolutePath();
                }
            }
        });

        Button test = findViewById(R.id.button2);
        /**Partie recuperation firebase**/
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = database.getReference("utilisateurs");

                myRef.orderByChild("name").equalTo(editProfil).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UtilisateurModel utilisateurRecup = snapshot.getValue(UtilisateurModel.class);
                            String passwordRecup = utilisateurRecup.getPassword();

                            String avatar = utilisateurRecup.getAvatar();
                            ImageView mImageView = findViewById(R.id.iv_photo);
                            Glide.with(ConnectionActivity.this).load(avatar).into(mImageView);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView mImageView = findViewById(R.id.iv_photo);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);

        }
    }




    /**ancien code
     * PHOTO PARTII (récuperation image et on set)

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView photo = findViewById(R.id.iv_photo);
        mPhotography = (Bitmap) data.getExtras().get("data");

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                ConnectionActivity.this.getResources(), mPhotography);

        /** on donne une forme ronde et on set l'image où on veut
        roundedBitmapDrawable.setCircular(true);
        photo.setImageDrawable(roundedBitmapDrawable);
    } **/
}
