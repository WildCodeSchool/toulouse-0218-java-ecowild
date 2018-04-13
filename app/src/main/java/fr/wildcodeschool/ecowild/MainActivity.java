package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final int PERMS_CALL_ID = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonToLogIn = findViewById(R.id.button_to_log_in);
        buttonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToLogIn = new Intent(MainActivity.this, ConnectionActivity.class);
                startActivity(intentToLogIn);
            }
        });

        final Button buttonBecomeMember = findViewById(R.id.button_become_member);
        buttonBecomeMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBecomeMember = new Intent(MainActivity.this, MemberActivity.class);
                MainActivity.this.startActivity(intentBecomeMember);
            }
        });

        final Button shortcut = findViewById(R.id.cheat);
        shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentShortcut = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(intentShortcut);
            }
        });
    }
}
