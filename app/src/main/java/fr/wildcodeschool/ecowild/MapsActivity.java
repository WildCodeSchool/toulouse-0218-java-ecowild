package fr.wildcodeschool.ecowild;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.annotation.RequiresApi;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;

import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 6786;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    DrawerLayout mDrawerLayout;
    final ArrayList<ElementModel> mGps = new ArrayList<>();
    boolean mGlassFilter = true;
    boolean mPaperfilter = true;
    boolean mIsWaitingForGoogleMap = false;
    Location mLastLocation = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final ProgressBar pbTest = findViewById(R.id.pb_xp);
        final Button buttonTest = findViewById(R.id.button_test);
        final experienceModel experienceModelModel = new experienceModel(0, 1);

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                experienceModelModel.setExperience(experienceModelModel.getExperience() + experienceModelModel.getTriExperience());
                pbTest.setProgress(10);
                pbTest.setProgress(experienceModelModel.getExperience());

                Toast.makeText(MapsActivity.this, R.string.pointXP, Toast.LENGTH_SHORT).show();
            }
        });

        mDrawerLayout = findViewById(R.id.drawerLayout);

        /** Partie GPS **/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        askLocationPermission();

        //Volet gauche
        TextView pseudo = findViewById(R.id.tv_pseudo);
        TextView rank = findViewById(R.id.tv_rank);
        ImageView toLogIn = findViewById(R.id.iv_to_log_in);
        ImageView createAccount = findViewById(R.id.iv_create_account);
        final Button btnToLogIn = findViewById(R.id.button_connection);
        final Button btnCreateAccount = findViewById(R.id.button_create_account);

        if (ConnectionActivity.CONNECTED) {

            String username = getIntent().getStringExtra("username");
            pseudo.setText(username);
            pseudo.setVisibility(View.VISIBLE);
            rank.setVisibility(View.VISIBLE);
            toLogIn.setVisibility(View.GONE);
            createAccount.setVisibility(View.GONE);
            btnToLogIn.setVisibility(View.GONE);
            btnCreateAccount.setVisibility(View.GONE);
        }


        btnToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCo = new Intent(MapsActivity.this, ConnectionActivity.class);
                MapsActivity.this.startActivity(intentCo);


            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMap = new Intent(MapsActivity.this, MemberActivity.class);
                MapsActivity.this.startActivity(intentMap);
            }
        });

        ImageView glassFilter = findViewById(R.id.iv_glass_filter);
        ImageView plasticFilter = findViewById(R.id.iv_plastic_filter);

        if (!ConnectionActivity.CONNECTED) {
            Snackbar snackbar = Snackbar.make(this.findViewById(R.id.map), R.string.snack, Snackbar.LENGTH_INDEFINITE).setDuration(9000).setAction("Connexion", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapsActivity.this, ConnectionActivity.class);
                    startActivity(intent);


                }
            });


            View snackBarView = snackbar.getView();
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(3);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.setDuration(3500);
            snackbar.show();
        }


        //Toast réutilisable plus tard
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

        final Button buttonLeft = findViewById(R.id.button_left);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        final Button buttonRight = findViewById(R.id.button_right);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        glassFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MapsActivity.this.mGlassFilter) {
                    buttonRight.setBackgroundResource(R.drawable.papier);
                    MapsActivity.this.mGlassFilter = false;
                    mMap.clear();
                    onMapReady(mMap);

                } else {
                    MapsActivity.this.mGlassFilter = true;
                    mMap.clear();
                    onMapReady(mMap);

                }

                Toast.makeText(MapsActivity.this, R.string.Verre, Toast.LENGTH_SHORT).show();
            }
        });

        plasticFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPaperfilter) {
                    buttonRight.setBackgroundResource(R.drawable.verre);

                    mPaperfilter = false;
                    mMap.clear();
                    onMapReady(mMap);

                } else {
                    mPaperfilter = true;
                    mMap.clear();
                    onMapReady(mMap);

                }
                Toast.makeText(MapsActivity.this, R.string.Papier, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // personne a déjà refusé

            } else {

                // on ne lui a pas encore posé la question
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // demande les droits à l'utilisateur
            }
        } else {
            // on a déjà le droit !
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // il a accepté, charger la position de la personne
                    getLocation();

                } else {

                    Toast.makeText(this, "Vous avez refusé la géolocalisation.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void moveCameraOnUser(Location location) {

        if (mMap == null) {
            mIsWaitingForGoogleMap = true;
            mLastLocation = location;
        } else {

            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(userLocation, 17);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
            mMap.moveCamera(yourLocation);

        }
    }

    private void getLocation() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // mettre à jour la position de l'utilisateur
                moveCameraOnUser(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Si l'utilisateur à permis l'utilisation de la localisation
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            moveCameraOnUser(location);
                        }
                    });

            // Si l'utilisateur n'a pas désactivé la localisation du téléphone
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                // demande la position de l'utilisateur
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            } else {
                Toast.makeText(this, "Géolocalisation désactivée", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mIsWaitingForGoogleMap) {
            moveCameraOnUser(mLastLocation);
        }

        WindowsInfoAdapter customInfoWindow = new WindowsInfoAdapter(MapsActivity.this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        // filtre afin de n'afficher que certaines données, en lien avec le json dans raw (créé).
        MapStyleOptions mapFilter = MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.map_style);
        googleMap.setMapStyle(mapFilter);

        /** Partie Json Verre**/

        final TextView testPosition = findViewById(R.id.test_position);

        // Crée une file d'attente pour les requêtes vers l'API
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-verre&refine.commune=TOULOUSE";

        // Création de la requête vers l'API, ajout des écouteurs pour les réponses et erreurs possibles
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray records = response.getJSONArray("records");

                            for (int c = 0; c < records.length(); c++) {
                                JSONObject recordslist = records.getJSONObject(c);
                                JSONObject geometry = recordslist.getJSONObject("geometry");
                                JSONObject location = recordslist.getJSONObject("fields");
                                String address = location.getString("adresse");
                                JSONArray coordinate = geometry.getJSONArray("coordinates");
                                String abs = coordinate.getString(0);
                                String ordo = coordinate.getString(1);
                                double valueAbs = Double.parseDouble(abs);
                                double valueOrdo = Double.parseDouble(ordo);
                                String type = "Verre";
                                String id = "v" + c;

                                mGps.add(new ElementModel(address, type, id));

                                // testPosition.append(valueAbs + " " + valueOrdo + address+ " \n ");
                                Marker verre = mMap.addMarker(new MarkerOptions().position(new LatLng(valueOrdo, valueAbs)).title(address)
                                        .snippet(type).visible(mGlassFilter).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Afficher l'erreur
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );

        // On ajoute la requête à la file d'attente
        requestQueue.add(jsonObjectRequest);

        /** Partie Json Papier/plastique **/
        // Crée une file d'attente pour les requêtes vers l'API
        RequestQueue requestQueueTwo = Volley.newRequestQueue(this);

        String urlTwo = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-emballage&refine.commune=TOULOUSE";

        // Création de la requête vers l'API, ajout des écouteurs pour les réponses et erreurs possibles
        JsonObjectRequest jsonObjectRequestTwo = new JsonObjectRequest(
                Request.Method.GET, urlTwo, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray records = response.getJSONArray("records");

                            for (int c = 0; c < records.length(); c++) {
                                JSONObject recordslist = records.getJSONObject(c);
                                JSONObject geometry = recordslist.getJSONObject("geometry");
                                JSONObject location = recordslist.getJSONObject("fields");
                                String address = location.getString("adresse");
                                JSONArray coordinate = geometry.getJSONArray("coordinates");
                                String abs = coordinate.getString(0);
                                String ordo = coordinate.getString(1);
                                double valueAbs = Double.parseDouble(abs);
                                double valueOrdo = Double.parseDouble(ordo);
                                String type = "Papier/Plastique";
                                String id = "p" + c;

                                mGps.add(new ElementModel(address, type, id));

                                // testPosition.append(valueAbs + " " + valueOrdo + address + " \n ");
                                mMap.addMarker(new MarkerOptions().position(new LatLng(valueOrdo, valueAbs)).title(address)
                                        .snippet(type).visible(mPaperfilter).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Afficher l'erreur
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );

        // On ajoute la requête à la file d'attente
        requestQueueTwo.add(jsonObjectRequestTwo);

        Switch goList = findViewById(R.id.go_list);
        goList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent goList = new Intent(MapsActivity.this, ListLocationActivity.class);
                goList.putExtra("GPS_POSITIONS", mGps);
                startActivity(goList);

            }
        });
    }

    private void initUI(View v) {
        Button button1 = (Button) v.findViewById(R.id.button_connection);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //tu fais ce que tu veux dans le onClick
                Intent intentCo = new Intent(MapsActivity.this, ConnectionActivity.class);
                MapsActivity.this.startActivity(intentCo);
            }
        });
    }

}

