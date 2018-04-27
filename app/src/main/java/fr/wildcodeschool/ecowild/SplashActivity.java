package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static fr.wildcodeschool.ecowild.ConnectionActivity.CACHE_USERNAME;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView loadingGif = findViewById(R.id.loading);

        Glide.with(SplashActivity.this).load("https://thumbs.gfycat.com/ImpoliteSnappyBumblebee-size_restricted.gif").into(loadingGif);

        final SharedPreferences sharedPrefProfil = this.getSharedPreferences("ECOWILD", Context.MODE_PRIVATE);
        final String username = sharedPrefProfil.getString(CACHE_USERNAME, "");

        if (!username.isEmpty()) {
            // chargement de l'utilisateur s'il est connecté
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("utilisateurs");
            myRef.orderByChild("name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String passwordRecup = userModel.getPassword();
                        String avatar = userModel.getAvatar();
                        String name = userModel.getName();
                        int xp = userModel.getXp();
                        int level = userModel.getLevel();

                        UserSingleton userSingleton = UserSingleton.getInstance();
                        userSingleton.setTextName(name);
                        userSingleton.setTextPassword(passwordRecup);
                        userSingleton.setTextAvatar(avatar);
                        userSingleton.setIntXp(xp);
                        userSingleton.setIntLevel(level);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

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
