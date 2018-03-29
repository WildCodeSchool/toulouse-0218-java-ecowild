package fr.wildcodeschool.ecowild;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_HIDDEN;
import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_VISIBLE;

public class ForgottenPasswordActivity extends AppCompatActivity {


    int mPasswordVisibility = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        final EditText editTextForgottenPassword = findViewById(R.id.edit_text_forgotten_password);
        final EditText editTextValidatedPassword = findViewById(R.id.edit_text_validated_password);
        final ImageView imageViewForgottenPassword = findViewById(R.id.image_view_forgotten_password);
        final ImageView imageViewValidatedPassword = findViewById(R.id.image_view_validated_password);

        setTitle("Modifier mot de passe");

        imageViewForgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( mPasswordVisibility == PASSWORD_HIDDEN) {
                    editTextForgottenPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                }

                else {
                    editTextForgottenPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        imageViewValidatedPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    editTextValidatedPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                }

                else {
                    editTextValidatedPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });
    }
}

