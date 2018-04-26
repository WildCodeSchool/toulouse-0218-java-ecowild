package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LoadAPISingleton loadAPISingleton = LoadAPISingleton.getInstance();
        loadAPISingleton.loadAPIs(this, new LoadAPISingleton.LoadAPIListener() {
            @Override
            public void onBothAPILoaded() {
                Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
