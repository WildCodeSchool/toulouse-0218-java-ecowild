package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ConnectionActivity extends AppCompatActivity {
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        final EditText editTextProfil = findViewById(R.id.editText_profil_connection);
        final EditText editTextPassword = findViewById(R.id.editText_password_connection);
        final ImageView imageViewPassword = findViewById(R.id.imageView_password_connection);
        final TextView textViewForgottenPassword = findViewById(R.id.textView_forgotten_password);
        final Button buttonToLogIn = findViewById(R.id.button_log_in);
        final CheckBox checkBoxToLogIn = findViewById(R.id.checkBox_connection);

        final SharedPreferences sharedPrefProfil = this.getPreferences(Context.MODE_PRIVATE);
        String usernameCache = sharedPrefProfil.getString("username", "");
        editTextProfil.setText(usernameCache);

        final SharedPreferences sharedPreferencesPassword = this.getPreferences(Context.MODE_PRIVATE);
        String passwordCache = sharedPreferencesPassword.getString("password", "");
        editTextPassword.setText(passwordCache);

        imageViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 1) {
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    i = 2;
                }

                else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    i = 1;
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

                String editProfil = editTextProfil.getText().toString();
                String editPassword = editTextPassword.getText().toString();

                if (editProfil.isEmpty() || editPassword.isEmpty()) {
                    Toast.makeText(ConnectionActivity.this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
                }

                else {
                    Intent intentMap = new Intent(ConnectionActivity.this, MapsActivity.class);
                    ConnectionActivity.this.startActivity(intentMap);
                }

                if (checkBoxToLogIn.isChecked()) {
                    SharedPreferences.Editor editorProfil = sharedPrefProfil.edit();
                    editorProfil.putString("username", editProfil);
                    editorProfil.commit();

                    SharedPreferences.Editor editorPassword = sharedPrefProfil.edit();
                    editorPassword.putString("password", editPassword);
                    editorPassword.commit();
                }
            }
        });
    }
}
