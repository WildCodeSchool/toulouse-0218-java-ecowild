package fr.wildcodeschool.ecowild;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final int PERMS_CALL_ID = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

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

    private void checkPermission() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)

        {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);

            return;
        }
    }
}
