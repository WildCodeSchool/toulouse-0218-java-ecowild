package fr.wildcodeschool.ecowild;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.submitbutton.SubmitButton;

import static fr.wildcodeschool.ecowild.R.string.remplissez_tout_les_champs;

public class ConnectionActivity extends AppCompatActivity {

    public static final int PASSWORD_HIDDEN = 1;
    public static final int PASSWORD_VISIBLE = 2;
    int mPasswordVisibility = PASSWORD_HIDDEN;
    public static final String CACHE_USERNAME = "username";
    public static final String CACHE_PASSWORD = "password";
    public static boolean CONNECTED = false;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        final EditText editTextProfil = findViewById(R.id.edit_text_profil_connection);
        final EditText editTextPassword = findViewById(R.id.edit_text_password_connection);
        final ImageView imageViewPassword = findViewById(R.id.image_view_password_connection);
        final TextView textViewForgottenPassword = findViewById(R.id.text_view_forgotten_password);
        final Button buttonToLogIn = findViewById(R.id.button_log_in);
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

        buttonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CONNECTED = true;

                String editProfil = editTextProfil.getText().toString();
                String editPassword = editTextPassword.getText().toString();

                if (editProfil.isEmpty() || editPassword.isEmpty()) {
                    Toast.makeText(ConnectionActivity.this, getString(remplissez_tout_les_champs), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentMap = new Intent(ConnectionActivity.this, MapsActivity.class);
                    intentMap.putExtra("username", editProfil);
                    ConnectionActivity.this.startActivity(intentMap);
                }

                if (checkBoxToLogIn.isChecked()) {
                    SharedPreferences.Editor editorProfil = sharedPrefProfil.edit();
                    editorProfil.putString(CACHE_USERNAME, editProfil);
                    editorProfil.putString(CACHE_PASSWORD, editPassword);
                    editorProfil.commit();
                }
            }
        });

        /**Proposition changement bouton se connecter */

        final SubmitButton sbConnection = findViewById(R.id.sb_connexion);
        sbConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CONNECTED = true;

                final String editProfil = editTextProfil.getText().toString();
                String editPassword = editTextPassword.getText().toString();

                if (editProfil.isEmpty() || editPassword.isEmpty()) {

                    Toast.makeText(ConnectionActivity.this, getString(remplissez_tout_les_champs), Toast.LENGTH_SHORT).show();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentMap = new Intent(ConnectionActivity.this, MapsActivity.class);
                            intentMap.putExtra("username", editProfil);
                            ConnectionActivity.this.startActivity(intentMap);
                        }
                    }, SPLASH_TIME_OUT);


                }

                if (checkBoxToLogIn.isChecked()) {
                    SharedPreferences.Editor editorProfil = sharedPrefProfil.edit();
                    editorProfil.putString(CACHE_USERNAME, editProfil);
                    editorProfil.putString(CACHE_PASSWORD, editPassword);
                    editorProfil.commit();
                }


            }
        });
    }
}
