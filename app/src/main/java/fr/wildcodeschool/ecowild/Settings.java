package fr.wildcodeschool.ecowild;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_HIDDEN;
import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_VISIBLE;
import static fr.wildcodeschool.ecowild.ConnectionActivity.REQUEST_TAKE_PHOTO;


public class Settings extends AppCompatActivity {
    int mPasswordVisibility = 1;
    String mCurrentPhotoPath;
    private Uri mPhotoUri = null;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText etPassword = findViewById(R.id.edit_text_password);
        final EditText etNewPassword = findViewById(R.id.edit_text_new_password);
        final EditText etNewPassword2 = findViewById(R.id.edit_text_new_password_confirm);
        final EditText etNewProfil = findViewById(R.id.edit_text_new_profil);

        final TextView name = findViewById(R.id.tv_name);

        final Button buttonMdp = findViewById(R.id.button_mdp);
        final Button buttonMnc = findViewById(R.id.button_mnc);
        final Button buttonValidate = findViewById(R.id.button_validate);
        Button buttonBack = findViewById(R.id.button_back);

        final ImageView ivPassword = findViewById(R.id.image_view_past_password);
        final ImageView ivNewPassword = findViewById(R.id.image_view_new_password);
        final ImageView ivNewPassword2 = findViewById(R.id.image_view_validated_password2);
        final ImageView ivkey = findViewById(R.id.image_view_key);
        final ImageView ivAvatar = findViewById(R.id.image_view_avatar);
        final ImageView ivProfil = findViewById(R.id.iv_profil);

        final RadioButton rbAvatar = findViewById(R.id.radio_button_avatar);
        final RadioButton rbName = findViewById(R.id.radio_button_name);
        final RadioButton rbPassword = findViewById(R.id.radio_button_password);
        RadioGroup rbOptionGroup = findViewById(R.id.radio_group);

        final Intent intent = new Intent(this, MapsActivity.class);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });


        final UserSingleton userSingleton = UserSingleton.getInstance();
        if (userSingleton.getTextAvatar() != null) {
            Glide.with(Settings.this).load(userSingleton.getTextAvatar()).apply(RequestOptions.circleCropTransform()).into(ivProfil);
            name.setText(userSingleton.getTextName());
            ivProfil.setBackground(null);
        }


        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        ImageView ivDelete = findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popup = new AlertDialog.Builder(Settings.this);
                popup.setTitle(R.string.alerte);
                popup.setMessage(R.string.delete_compte);
                popup.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference user = database.getReference("utilisateurs");

                        user.orderByChild("name").equalTo(userSingleton.getTextName()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userdataSnapshot : dataSnapshot.getChildren()) {

                                    String key = userdataSnapshot.getKey().toString();
                                    user.child(key).removeValue();
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toast,
                                            (ViewGroup) findViewById(R.id.custom_toast_container));
                                    TextView textToast = (TextView) layout.findViewById(R.id.text);
                                    textToast.setText(R.string.delete);
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setView(layout);
                                    toast.show();

                                    UserSingleton.getInstance().removeInstance();

                                    Intent intent = new Intent(Settings.this, ConnectionActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });




                    }
                });
                popup.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                popup.show();
            }
        });


        ivPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        ivNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    etNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        ivNewPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    etNewPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    etNewPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        rbOptionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                etPassword.setVisibility(View.GONE);
                etNewPassword.setVisibility(View.GONE);
                etNewPassword2.setVisibility(View.GONE);
                etNewProfil.setVisibility(View.GONE);
                buttonMdp.setVisibility(View.GONE);
                buttonMnc.setVisibility(View.GONE);
                buttonValidate.setVisibility(View.GONE);
                ivPassword.setVisibility(View.GONE);
                ivNewPassword.setVisibility(View.GONE);
                ivNewPassword2.setVisibility(View.GONE);
                ivkey.setVisibility(View.GONE);
                ivAvatar.setVisibility(View.GONE);


                if (rbPassword.isChecked()) {
                    visibleEt(etPassword, etNewPassword, etNewPassword2);
                    buttonMdp.setVisibility(View.VISIBLE);
                    ivPassword.setVisibility(View.VISIBLE);
                    ivNewPassword.setVisibility(View.VISIBLE);
                    ivNewPassword2.setVisibility(View.VISIBLE);
                    ivkey.setVisibility(View.VISIBLE);

                    buttonMdp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Cryptage nouveau mot de passe
                            final String password = etPassword.getText().toString();
                            final HashCode hashCode = Hashing.sha256().hashString(password, Charset.defaultCharset());
                            String newPassword = etNewPassword.getText().toString();
                            final HashCode hashCodeNP = Hashing.sha256().hashString(newPassword, Charset.defaultCharset());
                            String newPassword2 = etNewPassword2.getText().toString();
                            userSingleton.setTextPassword(hashCodeNP.toString());

                            if (password.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()) {
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,
                                        (ViewGroup) findViewById(R.id.custom_toast_container));
                                TextView textToast = (TextView) layout.findViewById(R.id.text);
                                textToast.setText(R.string.remplissez_tout_les_champs);
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();
                            }
                            if (newPassword.equals(newPassword2)) {
                                /**Partie recuperation ancien mot de passe**/
                                DatabaseReference myRef = database.getReference("utilisateurs");
                                myRef.orderByChild("name").equalTo(userSingleton.getTextName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            UserModel userModel = snapshot.getValue(UserModel.class);
                                            String passwordRecup = userModel.getPassword();

                                            if (passwordRecup.equals(hashCode.toString())) {
                                                //changement mot de passe
                                                final DatabaseReference user = database.getReference("utilisateurs");
                                                user.orderByChild("name").equalTo(userSingleton.getTextName()).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot userdataSnapshot : dataSnapshot.getChildren()) {
                                                            userSingleton.setTextPassword(hashCodeNP.toString());
                                                            String key = userdataSnapshot.getKey().toString();
                                                            user.child(key).child("password").setValue(userSingleton.getTextPassword());

                                                            LayoutInflater inflater = getLayoutInflater();
                                                            View layout = inflater.inflate(R.layout.toast,
                                                                    (ViewGroup) findViewById(R.id.custom_toast_container));
                                                            TextView textToast = (TextView) layout.findViewById(R.id.text);
                                                            textToast.setText(R.string.move_password);
                                                            Toast toast = new Toast(getApplicationContext());
                                                            toast.setDuration(Toast.LENGTH_SHORT);
                                                            toast.setView(layout);
                                                            toast.show();

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }

                                                });

                                            } else if (!password.isEmpty()) {
                                                LayoutInflater inflater = getLayoutInflater();
                                                View layout = inflater.inflate(R.layout.toast,
                                                        (ViewGroup) findViewById(R.id.custom_toast_container));
                                                TextView textToast = (TextView) layout.findViewById(R.id.text);
                                                textToast.setText(R.string.false_password);
                                                Toast toast = new Toast(getApplicationContext());
                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                toast.setView(layout);
                                                toast.show();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            } else {
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,
                                        (ViewGroup) findViewById(R.id.custom_toast_container));
                                TextView textToast = (TextView) layout.findViewById(R.id.text);
                                textToast.setText(R.string.new_password_false);
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();
                            }
                        }
                    });
                }

                if (rbName.isChecked()) {
                    buttonMnc.setVisibility(View.VISIBLE);
                    etNewProfil.setVisibility(View.VISIBLE);

                    buttonMnc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final String newProfil = etNewProfil.getText().toString();

                            if (newProfil.isEmpty()) {
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,
                                        (ViewGroup) findViewById(R.id.custom_toast_container));
                                TextView textToast = (TextView) layout.findViewById(R.id.text);
                                textToast.setText(R.string.remplissez_tout_les_champs);
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();
                            }
                            //verification si le nom utilisateur existe ou non
                            else {
                                DatabaseReference myRef = database.getReference("utilisateurs");
                                myRef.orderByChild("name").equalTo(newProfil).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() == 0) {
                                            //changement nom utilisateur
                                            final DatabaseReference user = database.getReference("utilisateurs");
                                            user.orderByChild("name").equalTo(userSingleton.getTextName()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot userdataSnapshot : dataSnapshot.getChildren()) {
                                                        userSingleton.setTextName(newProfil);
                                                        String key = userdataSnapshot.getKey().toString();
                                                        user.child(key).child("name").setValue(userSingleton.getTextName());
                                                        LayoutInflater inflater = getLayoutInflater();
                                                        View layout = inflater.inflate(R.layout.toast,
                                                                (ViewGroup) findViewById(R.id.custom_toast_container));
                                                        TextView textToast = (TextView) layout.findViewById(R.id.text);
                                                        textToast.setText(R.string.name_move);
                                                        Toast toast = new Toast(getApplicationContext());
                                                        toast.setDuration(Toast.LENGTH_SHORT);
                                                        toast.setView(layout);
                                                        toast.show();
                                                        name.setText(userSingleton.getTextName());

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }

                                            });

                                        } else {
                                            LayoutInflater inflater = getLayoutInflater();
                                            View layout = inflater.inflate(R.layout.toast,
                                                    (ViewGroup) findViewById(R.id.custom_toast_container));
                                            TextView textToast = (TextView) layout.findViewById(R.id.text);
                                            textToast.setText(R.string.repeat);
                                            Toast toast = new Toast(getApplicationContext());
                                            toast.setDuration(Toast.LENGTH_SHORT);
                                            toast.setView(layout);
                                            toast.show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        }
                    });

                }

                if (rbAvatar.isChecked()) {
                    ivAvatar.setVisibility(View.VISIBLE);
                    buttonValidate.setVisibility(View.VISIBLE);
                    ivAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //qd click sur avatar lance toutes les methodes
                            dispatchTakePictureIntent();
                        }
                    });
                    buttonValidate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Glide.with(Settings.this).load(R.drawable.loading_photo).into(ivProfil);

                            final DatabaseReference user = database.getReference("utilisateurs");
                            user.orderByChild("name").limitToFirst(1).equalTo(userSingleton.getTextName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    //transformation Uri
                                    mStorageRef = FirebaseStorage.getInstance().getReference();

                                    StorageReference photoRef = mStorageRef.child("images/" + mPhotoUri.getLastPathSegment());
                                    UploadTask uploadTask = photoRef.putFile(mPhotoUri);

                                    // Register observers to listen for when the download is done or if it fails
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(Settings.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                            for (DataSnapshot userdataSnapshot : dataSnapshot.getChildren()) {
                                                userSingleton.setTextAvatar(downloadUrl.toString());
                                                String key = userdataSnapshot.getKey().toString();
                                                user.child(key).child("avatar").setValue(userSingleton.getTextAvatar());


                                            }
                                            Glide.with(Settings.this).load(userSingleton.getTextAvatar()).apply(RequestOptions.circleCropTransform()).into(ivProfil);
                                            Glide.with(Settings.this).load(R.drawable.appareil_photo).into(ivAvatar);
                                        }


                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            }
        });

    }

    public void visibleEt(EditText et1, EditText et2, EditText et3) {
        et1.setVisibility(View.VISIBLE);
        et2.setVisibility(View.VISIBLE);
        et3.setVisibility(View.VISIBLE);
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
        ImageView mImageView = findViewById(R.id.image_view_avatar);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Glide.with(Settings.this).load(mPhotoUri).apply(RequestOptions.circleCropTransform()).into(mImageView);
        }
        if(resultCode!=RESULT_OK){
           return;
        }

    }


}
