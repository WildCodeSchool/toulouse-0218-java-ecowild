package fr.wildcodeschool.ecowild;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
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
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.bloder.magic.view.MagicButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 6786;

    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    DrawerLayout mDrawerLayout;
    final ArrayList<ElementModel> mGps = new ArrayList<>();
    boolean mGlassFilter = true;
    boolean mPaperfilter = true;
    // variable pour presentation en enlever apres
    int i = 0;
    boolean mIsWaitingForGoogleMap = false;
    Location mLastLocation = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /** Partie menu Circle**/
        //Image bouton Menu
        ImageView iconMenu = new ImageView(this); // Create an icon
        iconMenu.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.entonnoir));

        //creation bouton Menu
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(iconMenu)
                .build();


        SubActionButton.Builder listeBuilder = new SubActionButton.Builder(this);

        //Creation image sous menu
        final ImageView filtreVerre = new ImageView(this); // Create an icon
        filtreVerre.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verre));
        //envoit sous bouton au menu
        final SubActionButton sabVerre = listeBuilder.setContentView(filtreVerre).build();

        final ImageView filtrePapier = new ImageView(this); // Create an icon
        filtrePapier.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papier));
        SubActionButton sabPapier = listeBuilder.setContentView(filtrePapier).build();


        final ImageView filtreFavoris = new ImageView(MapsActivity.this); // Create an icon
        filtreFavoris.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.etoile));
        SubActionButton sabFavoris = listeBuilder.setContentView(filtreFavoris).build();

        final ImageView filtreKm = new ImageView(MapsActivity.this);
        filtreKm.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.borne));
        SubActionButton sabKm = listeBuilder.setContentView(filtreKm).build();

        SeekBar seekBarKM = new SeekBar(MapsActivity.this);
        SubActionButton sabSeekBar = listeBuilder.setContentView(seekBarKM).build();


        DrawerLayout.LayoutParams layoutParam = new DrawerLayout.LayoutParams(200, 200);
        sabFavoris.setLayoutParams(layoutParam);
        sabKm.setLayoutParams(layoutParam);
        sabPapier.setLayoutParams(layoutParam);
        sabVerre.setLayoutParams(layoutParam);

        //Creation bouton sous menu
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(MapsActivity.this)
                .addSubActionView(sabVerre)
                .addSubActionView(sabPapier)
                .addSubActionView(sabKm)
                .addSubActionView(sabFavoris)
                .attachTo(actionButton)
                .build();

        DrawerLayout.LayoutParams layoutParamHori = new DrawerLayout.LayoutParams(500, 150);
        sabSeekBar.setLayoutParams(layoutParamHori);


        /*FloatingActionMenu actionkm = new FloatingActionMenu.Builder(MapsActivity.this)
                .addSubActionView(sabSeekBar)
                .attachTo(sabKm)
                .build();*/


      /** Partie XP */
        final ProgressBar pbTest = findViewById(R.id.pb_xp);
        final ExperienceModel experienceModelModel = new ExperienceModel(0, 1);


        MagicButton mbXp = findViewById(R.id.magic_button);

        mbXp.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experienceModelModel.setExperience(experienceModelModel.getExperience() + experienceModelModel.getTriExperience());
                pbTest.setProgress(10);
                pbTest.setProgress(experienceModelModel.getExperience());

                LayoutInflater inflater = getLayoutInflater();
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
            }
        });


        mDrawerLayout = findViewById(R.id.drawer_layout);

        /** Partie GPS **/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        askLocationPermission();

        /**Partie Slide**/
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Volet gauche
        TextView pseudo = findViewById(R.id.tv_pseudo);
        TextView rank = findViewById(R.id.tv_rank);
        final Button btnCreateAccount = findViewById(R.id.button_create_account);
        final SwipeButton swipeButton = findViewById(R.id.swipe_btn);

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {

                Intent test = new Intent(MapsActivity.this, ConnectionActivity.class);
                startActivity(test);
            }
        });

        if (ConnectionActivity.CONNECTED) {

            String username = getIntent().getStringExtra("username");
            pseudo.setText(username);
            pseudo.setVisibility(View.VISIBLE);
            rank.setVisibility(View.VISIBLE);
            btnCreateAccount.setVisibility(View.GONE);
        }

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

/*
            View snackBarView = snackbar.getView();
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(3);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.setDuration(3500);
            snackbar.show();*/

        }

        /**Toast réutilisable plus tard**/
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

        /**Map**/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Bouton slide ouverture droite et gauche

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


        /**Filtres verre et papier slide droit**/
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

        /**Filtres verre et papier menu multiples droit**/
        sabVerre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MapsActivity.this.mGlassFilter) {
                    buttonRight.setBackgroundResource(R.drawable.papier);
                    MapsActivity.this.mGlassFilter = false;
                    mMap.clear();
                    onMapReady(mMap);
                    filtreVerre.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verresansfond));


                } else {
                    MapsActivity.this.mGlassFilter = true;
                    mMap.clear();
                    onMapReady(mMap);
                    filtreVerre.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verre));

                }


            }
        });

        sabPapier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mPaperfilter) {
                    buttonRight.setBackgroundResource(R.drawable.verre);
                    filtrePapier.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papiersansfond));
                    mPaperfilter = false;
                    mMap.clear();
                    onMapReady(mMap);

                } else {
                    mPaperfilter = true;
                    mMap.clear();
                    onMapReady(mMap);
                    filtrePapier.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papier));

                }

            }
        });

        /*
        sabKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // i pour presentation a retirer apres av les filtre
                if (i == 0) {
                    filtreKm.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.bornesansfond));
                    i = 1;
                } else if (i == 1) {
                    filtreKm.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.borne));
                    i = 0;
                }
            }
        }); */

        sabFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // i pour presentation a retirer apres av les filtre
                if (i == 0) {
                    filtreFavoris.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.etoilesansfond));
                    i = 1;
                } else if (i == 1) {
                    filtreFavoris.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.etoile));
                    i = 0;
                }
            }
        });

    }

    /**
     * Permissions
     **/
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

        String url = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-verre&refine.commune=TOULOUSE&rows=100";

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

                                int height = 150;
                                int width = 150;
                                BitmapDrawable bitmapDrawableGlass = (BitmapDrawable) getResources().getDrawable(R.drawable.pointeur_verre);
                                Bitmap glass = bitmapDrawableGlass.getBitmap();
                                Bitmap finalGlass = Bitmap.createScaledBitmap(glass, width, height, false);

                                mGps.add(new ElementModel(address, type, id));

                                // testPosition.append(valueAbs + " " + valueOrdo + address+ " \n ");
                                Marker verre = mMap.addMarker(new MarkerOptions().position(new LatLng(valueOrdo, valueAbs)).title(address)
                                        .snippet(type).visible(mGlassFilter).icon(BitmapDescriptorFactory.fromBitmap(finalGlass)));
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

        String urlTwo = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-emballage&refine.commune=TOULOUSE&rows=100";

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

                                int height = 150;
                                int width = 150;
                                BitmapDrawable bitmapDrawablePlastic = (BitmapDrawable) getResources().getDrawable(R.drawable.pointeur_papier);
                                Bitmap plastic = bitmapDrawablePlastic.getBitmap();
                                Bitmap finalPlastic = Bitmap.createScaledBitmap(plastic, width, height, false);

                                mGps.add(new ElementModel(address, type, id));

                                // testPosition.append(valueAbs + " " + valueOrdo + address + " \n ");
                                mMap.addMarker(new MarkerOptions().position(new LatLng(valueOrdo, valueAbs)).title(address)
                                        .snippet(type).visible(mPaperfilter).icon(BitmapDescriptorFactory.fromBitmap(finalPlastic)));
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
    /** Boutton mystere
     private void initUI(View v) {
     Button button1 = (Button) v.findViewById(R.id.button_connection);
     button1.setOnClickListener(new View.OnClickListener() {

    @Override public void onClick(View v) {
    //tu fais ce que tu veux dans le onClick
    Intent intentCo = new Intent(MapsActivity.this, ConnectionActivity.class);
    MapsActivity.this.startActivity(intentCo);
    }
    });
     } */

}

