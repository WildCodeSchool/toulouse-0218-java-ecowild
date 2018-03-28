package fr.wildcodeschool.ecowild;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ForgottenPasswordActivity extends AppCompatActivity {

    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        final EditText editTextForgottenPassword = findViewById(R.id.editText_forgotten_password);
        final EditText editTextValidatedPassword = findViewById(R.id.editText_validated_password);
        final ImageView imageViewForgottenPassword = findViewById(R.id.imageView_forgotten_password);
        final ImageView imageViewValidatedPassword = findViewById(R.id.imageView_validated_password);

        setTitle("Modifier mot de passe");

        imageViewForgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 1) {
                    editTextForgottenPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    i = 2;
                }

                else {
                    editTextForgottenPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    i = 1;
                }
            }
        });

        imageViewValidatedPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 1) {
                    editTextValidatedPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    i = 2;
                }

                else {
                    editTextValidatedPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    i = 1;
                }
            }
        });
    }
}

