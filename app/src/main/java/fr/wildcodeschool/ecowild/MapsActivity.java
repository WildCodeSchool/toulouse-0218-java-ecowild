package fr.wildcodeschool.ecowild;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int PERMS_CALL_ID = 1234;
    private LocationManager mLocation;

    //Pour recup la carte utiliser ses 2 attributs
    //1er name ds layout=instance
    //2eme: objet a linterieur pour pouvoir modif les données
    private GoogleMap mMap;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Snack
       Snackbar snackbar= Snackbar.make(this.findViewById(R.id.map), "N'oublier pas de vous connecter pour plus de fonctionnalités", Snackbar.LENGTH_INDEFINITE).setDuration(9000).setAction("Connexion", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,ConnectionActivity.class);
                startActivity(intent);

            }
        });

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(3);
        snackbar.show();


        //Toast
        /**LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView textToast = (TextView) layout.findViewById(R.id.text);
        textToast.setText(R.string.Toast);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        **/
        //Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // slide droite et gauche
        drawerLayout = findViewById(R.id.drawerLayout);
        Button buttonLeft = findViewById(R.id.button_left);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        Button buttonRigth = findViewById(R.id.buttonright);
        buttonRigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

    }


    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng depart = new LatLng(43.6043896, 1.4433718000000226);

        googleMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(depart, 10));

        // filtre afin de n'afficher que certaines données, en lien avec le json dans raw (créé).
        MapStyleOptions mapFilter = MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.map_style);
        googleMap.setMapStyle(mapFilter);

        // Add a marker in Sydney and move the camera
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

    }
}

