package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_HIDDEN;
import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_VISIBLE;

public class MemberActivity extends AppCompatActivity {

    int mPasswordVisibility = PASSWORD_HIDDEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        final EditText editTextCompte = findViewById(R.id.edit_text_compte);
        final EditText editTextPassword = findViewById(R.id.edit_text_password);
        final EditText editTextPassword2 = findViewById(R.id.edit_text_password2);
        final ImageView imageViewPassword = findViewById(R.id.image_view_password);
        final ImageView imageViewPassword2 = findViewById(R.id.image_view_password2);

        Button buttonMember = findViewById(R.id.button_become_member);
        buttonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editCompte = editTextCompte.getText().toString();
                String editPassword = editTextPassword.getText().toString();
                String editPassword2 = editTextPassword2.getText().toString();

                if (editCompte.isEmpty() || (editPassword.isEmpty() || (editPassword2.isEmpty()))) {
                    Toast.makeText(MemberActivity.this, fr.wildcodeschool.ecowild.R.string.remplissez_tout_les_champs, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentConnect = new Intent(MemberActivity.this, ConnectionActivity.class);
                    startActivity(intentConnect);
                }
            }
        });

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
    }
}
