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

public class MemberActivity extends AppCompatActivity {

    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        final EditText editTextCompte = findViewById(R.id.editText_Compte);
        final EditText editTextPassword = findViewById(R.id.editText_password);
        final EditText editTextPassword2 = findViewById(R.id.editText_password2);
        final ImageView imageViewPassword = findViewById(R.id.imageView_password);
        final ImageView imageViewPassword2 = findViewById(R.id.imageView_password2);

        Button buttonMember = findViewById(R.id.button_become_member);
        buttonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editCompte = editTextCompte.getText().toString();
                String editPassword = editTextPassword.getText().toString();
                String editPassword2 = editTextPassword2.getText().toString();

                if (editCompte.isEmpty() ||(editPassword.isEmpty() || (editPassword2.isEmpty()))) {
                    Toast.makeText(MemberActivity.this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
                }

                else {
                    Intent intentConnect = new Intent(MemberActivity.this, ConnectionActivity.class);
                    startActivity(intentConnect);
                }
            }
        });

        imageViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 1){
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    i = 2;
                }

                else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    i = 1;
                }
            }
        });

        imageViewPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 1) {
                    editTextPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    i = 2;
                }

                else {
                    editTextPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    i = 1;
                }
            }
        });
    }
}
