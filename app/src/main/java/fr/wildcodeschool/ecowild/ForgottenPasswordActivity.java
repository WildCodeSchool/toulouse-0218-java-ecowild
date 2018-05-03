package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;

import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_HIDDEN;
import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_VISIBLE;


public class ForgottenPasswordActivity extends AppCompatActivity {


    int mPasswordVisibility = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        final EditText editTextCompte = findViewById(R.id.edit_text_password);
        final EditText editTextValidatedPassword = findViewById(R.id.edit_text_new_password);
        final ImageView imageViewValidatedPassword = findViewById(R.id.image_view_validated_password);
        final Button buttonPassword = findViewById(R.id.button_mdp);

        setTitle(getString(R.string.password));

        imageViewValidatedPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    editTextValidatedPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    editTextValidatedPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String accountString = editTextCompte.getText().toString();
                final String passwordString = editTextValidatedPassword.getText().toString();
                final HashCode hashCode = Hashing.sha256().hashString(passwordString, Charset.defaultCharset());

                if (accountString.isEmpty() || passwordString.isEmpty()) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView textToast = (TextView) layout.findViewById(R.id.text);
                    textToast.setText(R.string.remplissez_tout_les_champs);

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                } else {


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference user = database.getReference("utilisateurs");
                    user.orderByChild("name").equalTo(accountString).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() == 0) {
                                Toast.makeText(ForgottenPasswordActivity.this, R.string.password_false, Toast.LENGTH_SHORT).show();
                            }
                            for (DataSnapshot userdataSnapshot : dataSnapshot.getChildren()) {
                                String key = userdataSnapshot.getKey().toString();
                                user.child(key).child("password").setValue(hashCode.toString());
                                Toast.makeText(ForgottenPasswordActivity.this, R.string.move_password, Toast.LENGTH_SHORT).show();
                                Intent intentPassword = new Intent(ForgottenPasswordActivity.this, ConnectionActivity.class);
                                startActivity(intentPassword);
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
}

