package fr.wildcodeschool.ecowild;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
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
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.bloder.magic.view.MagicButton;

import static android.view.MotionEvent.ACTION_UP;
import static fr.wildcodeschool.ecowild.ConnectionActivity.mPhotography;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 6786;

    private static int SPLASH_TIME_OUT = 100;
    final ArrayList<ClusterModel> mClusterMarker = new ArrayList<>();
    public boolean NOT_MOVE = true;
    DrawerLayout mDrawerLayout;
    boolean mGlassFilter = true;
    boolean mPaperfilter = true;
    boolean mIsWaitingForGoogleMap = false;
    Location mLastLocation = null;
    float dX;
    float dY;
    int lastAction;
    private ClusterManager<ClusterModel> mClusterManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /**initi singleton*/
        UserSingleton userSingleton = UserSingleton.getInstance();

        final ImageView accountImgCreation = findViewById(R.id.img_profil);

        /** Partie menu Circle**/
        //Image bouton Menu
        ImageView iconMenu = new ImageView(this); // Create an icon
        iconMenu.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.entonnoir));

        //creation bouton Menu
        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(iconMenu)
                .build();

        SubActionButton.Builder listeBuilder = new SubActionButton.Builder(this);

        //Creation image sous menu
        final ImageView glassFilterImg = new ImageView(this); // Create an icon
        glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verre));
        //envoit sous bouton au menu
        final SubActionButton sabGlass = listeBuilder.setContentView(glassFilterImg).build();

        final ImageView paperFilterGlass = new ImageView(this); // Create an icon
        paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papier));
        SubActionButton sabPaper = listeBuilder.setContentView(paperFilterGlass).build();

        final ImageView favFilter = new ImageView(MapsActivity.this); // Create an icon
        favFilter.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.etoile));
        SubActionButton sabFavorite = listeBuilder.setContentView(favFilter).build();

        DrawerLayout.LayoutParams layoutParam = new DrawerLayout.LayoutParams(200, 200);
        sabFavorite.setLayoutParams(layoutParam);
        sabPaper.setLayoutParams(layoutParam);
        sabGlass.setLayoutParams(layoutParam);

        //Creation bouton sous menu
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(MapsActivity.this)
                .addSubActionView(sabGlass)
                .addSubActionView(sabPaper)
                .addSubActionView(sabFavorite)
                .attachTo(actionButton)
                .build();

        /** Partie XP */
        final ProgressBar pbXpImg = findViewById(R.id.pb_xp);
        final ExperienceModel experienceModel = new ExperienceModel(0, 1, 1);
        final TextView rank = findViewById(R.id.tv_rank);
        final TextView level = findViewById(R.id.tv_level);
        final MagicButton mbXp = findViewById(R.id.magic_button);

        mbXp.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                experienceModel.setExperience(experienceModel.getExperience() + experienceModel.getExperienceGain());

                int currentXp = experienceModel.getExperience() % 10;

                pbXpImg.setProgress(currentXp);

                if (experienceModel.getExperience() % 10 == 0) {
                    pbXpImg.setProgress(0);
                    experienceModel.setLevel(experienceModel.getLevel() + 1);
                    level.setText(String.format(getString(R.string.lvl), experienceModel.getLevel()));
                }

                if (experienceModel.getLevel() >= 10) {
                    rank.setText(R.string.rang5);
                }

                else if (experienceModel.getLevel() >= 7) {
                    rank.setText(R.string.rang4);
                }

                else if (experienceModel.getLevel() >= 5) {
                    rank.setText(R.string.rang3);
                }

                else if (experienceModel.getLevel() >= 3) {
                    rank.setText(R.string.rang2);
                }

                else if (experienceModel.getLevel() >= 1) {
                    rank.setText(R.string.rang1);
                }

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView textToast = (TextView) layout.findViewById(R.id.text);
                textToast.setText(R.string.Toast);

                Toast toast = new Toast(MapsActivity.this);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });

        /** Partie Popup**/
        Button popup = findViewById(R.id.button_popup);
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popup = new AlertDialog.Builder(MapsActivity.this);
                popup.setTitle(R.string.alerte);
                popup.setMessage(R.string.alert_message);
                popup.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mbXp.setVisibility(View.VISIBLE);
                    }
                });
                popup.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mbXp.setVisibility(View.GONE);
                    }
                });
                popup.show();
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
        final Button btnCreateAccount = findViewById(R.id.button_create_account);
        final SwipeButton swipeButton = findViewById(R.id.swipe_btn);
        final TextView tvParameter = findViewById(R.id.tv_parameter);
        final TextView tvUsefulInformation = findViewById(R.id.tv_useful_information);
        final TextView tvMove = findViewById(R.id.tv_move);
        final ImageView ivParameter = findViewById(R.id.imageButton);
        final ImageView ivUsefulInformation = findViewById(R.id.iv_information);
        final ImageView ivMove = findViewById(R.id.iv_move);

        tvMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NOT_MOVE) {
                    ivMove.setAlpha((float) 0.2);
                    tvMove.setText(R.string.lock_buttons);
                    ObjectAnimator.ofFloat(actionButton, "translationX", 0, 30).setDuration(400).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ObjectAnimator.ofFloat(actionButton, "translationX", 30, 0).setDuration(400).start();
                        }
                    }, SPLASH_TIME_OUT);
                    ObjectAnimator.ofFloat(actionButton, "translationY", 0, 30).setDuration(400).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ObjectAnimator.ofFloat(actionButton, "translationY", 30, 0).setDuration(400).start();
                        }
                    }, SPLASH_TIME_OUT);


                    /** faire bouger boutton*/
                    //Dire ou on veut qu'on puisse faire une action
                    final View dragView = findViewById(R.id.button_left);
                    //l'ecouter et en fct de mouvement faire tel ou tel chose
                    dragView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {


                            switch (event.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                    dX = view.getX() - event.getRawX();
                                    dY = view.getY() - event.getRawY();
                                    lastAction = MotionEvent.ACTION_DOWN;
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    view.setY(event.getRawY() + dY);
                                    view.setX(event.getRawX() + dX);
                                    lastAction = MotionEvent.ACTION_MOVE;
                                    break;

                                case ACTION_UP:

                                    break;


                                default:
                                    return true;
                            }


                            return false;
                        }
                    });

                    final View dragViewActionButton = actionButton;
                    //l'ecouter et en fct de mouvement faire tel ou tel chose
                    dragViewActionButton.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {


                            switch (event.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                    dX = view.getX() - event.getRawX();
                                    dY = view.getY() - event.getRawY();
                                    lastAction = MotionEvent.ACTION_DOWN;
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    view.setY(event.getRawY() + dY);
                                    view.setX(event.getRawX() + dX);
                                    lastAction = MotionEvent.ACTION_MOVE;
                                    break;

                                case ACTION_UP:

                                    break;


                                default:
                                    return true;
                            }


                            return false;
                        }
                    });


                    NOT_MOVE = false;
                } else {
                    ivMove.setAlpha((float) 1.0);
                    tvMove.setText(R.string.unlock_button);
                    NOT_MOVE = true;

                    final View dragViewActionButton = actionButton;
                    dragViewActionButton.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    final View dragView = findViewById(R.id.button_left);
                    dragView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });

                }


            }
        });

        ivMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NOT_MOVE) {
                    ivMove.setAlpha((float) 0.2);
                    tvMove.setText(R.string.lock_buttons);
                    ObjectAnimator.ofFloat(actionButton, "translationX", 0, 30).setDuration(400).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ObjectAnimator.ofFloat(actionButton, "translationX", 30, 0).setDuration(400).start();
                        }
                    }, SPLASH_TIME_OUT);
                    ObjectAnimator.ofFloat(actionButton, "translationY", 0, 30).setDuration(400).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ObjectAnimator.ofFloat(actionButton, "translationY", 30, 0).setDuration(400).start();
                        }
                    }, SPLASH_TIME_OUT);


                    /** faire bouger boutton*/
                    //Dire ou on veut qu'on puisse faire une action
                    final View dragView = findViewById(R.id.button_left);
                    //l'ecouter et en fct de mouvement faire tel ou tel chose
                    dragView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {


                            switch (event.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                    dX = view.getX() - event.getRawX();
                                    dY = view.getY() - event.getRawY();
                                    lastAction = MotionEvent.ACTION_DOWN;
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    view.setY(event.getRawY() + dY);
                                    view.setX(event.getRawX() + dX);
                                    lastAction = MotionEvent.ACTION_MOVE;
                                    break;

                                case ACTION_UP:

                                    break;


                                default:
                                    return true;
                            }


                            return false;
                        }
                    });

                    final View dragViewActionButton = actionButton;
                    //l'ecouter et en fct de mouvement faire tel ou tel chose
                    dragViewActionButton.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {


                            switch (event.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                    dX = view.getX() - event.getRawX();
                                    dY = view.getY() - event.getRawY();
                                    lastAction = MotionEvent.ACTION_DOWN;
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    view.setY(event.getRawY() + dY);
                                    view.setX(event.getRawX() + dX);
                                    lastAction = MotionEvent.ACTION_MOVE;
                                    break;

                                case ACTION_UP:

                                    break;


                                default:
                                    return true;
                            }


                            return false;
                        }
                    });


                    NOT_MOVE = false;
                } else {
                    ivMove.setAlpha((float) 1.0);
                    tvMove.setText(R.string.unlock_button);
                    NOT_MOVE = true;

                    final View dragViewActionButton = actionButton;
                    dragViewActionButton.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    final View dragView = findViewById(R.id.button_left);
                    dragView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });

                }

            }
        });
        final Intent intentParameter = new Intent(MapsActivity.this, Settings.class);
        final Intent intentUsefulInformation = new Intent(MapsActivity.this, UsefulInformationActivity.class);
        final Intent intentFavorite = new Intent(MapsActivity.this, UsefulInformationActivity.class);
        tvParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intentParameter);
            }
        });

        ivParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentParameter);
            }
        });

        tvUsefulInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intentUsefulInformation);
            }
        });

        ivUsefulInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentUsefulInformation);
            }
        });

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {

                Intent swipeButton = new Intent(MapsActivity.this, ConnectionActivity.class);
                startActivity(swipeButton);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createAccount = new Intent(MapsActivity.this, ConnectionActivity.class);
                startActivity(createAccount);
            }
        });

        final Button buttonRight = findViewById(R.id.button_right);
        final Button buttonLeft = findViewById(R.id.button_left);

        if (ConnectionActivity.CONNECTED) {

            pseudo.setText(userSingleton.getTextName());
            pseudo.setVisibility(View.VISIBLE);
            rank.setVisibility(View.VISIBLE);
            level.setVisibility(View.VISIBLE);
            btnCreateAccount.setVisibility(View.GONE);
            Glide.with(MapsActivity.this).load(userSingleton.getTextAvatar()).apply(RequestOptions.circleCropTransform()).into(accountImgCreation);

            accountImgCreation.setBackground(null);


        }

        if (!ConnectionActivity.CONNECTED) {
            Snackbar snackbar = Snackbar.make(this.findViewById(R.id.map), R.string.snack, Snackbar.LENGTH_INDEFINITE).setDuration(9000).setAction("Connexion", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapsActivity.this, ConnectionActivity.class);
                    startActivity(intent);

                }
            });

            View snackBarView = snackbar.getView();
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(MapsActivity.this, R.color.colorEcoWild2));
            textView.setMaxLines(3);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.setDuration(3500);
            snackbar.setActionTextColor(ContextCompat.getColor(MapsActivity.this, R.color.colorEcoWild2));
            snackBarView.setBackgroundColor(ContextCompat.getColor(MapsActivity.this, R.color.colorEcoWild));
            snackbar.show();

        }

        /**Map**/
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });


        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        /**Filtres verre et papier menu multiples droit**/
        sabGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGlassFilter) {
                    mClusterManager.setRenderer(new OwRenderingGlass(getApplicationContext(), mMap, mClusterManager));
                    mGlassFilter = false;
                    glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verresansfond));
                } else {
                    mClusterManager.setRenderer(new OwRendering(getApplicationContext(), mMap, mClusterManager));
                    mGlassFilter = true;
                    glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verre));
                }
            }
        });

        sabPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPaperfilter) {
                    mPaperfilter = false;
                    mClusterManager.setRenderer(new OwRenderingPaper(getApplicationContext(), mMap, mClusterManager));
                    paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papiersansfond));
                } else {
                    mClusterManager.setRenderer(new OwRendering(getApplicationContext(), mMap, mClusterManager));
                    mPaperfilter = true;
                    paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papier));
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

                    Toast.makeText(this, R.string.geoloc_refused, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void moveCameraOnUser(Location location) {

        if (mMap == null) {
            mIsWaitingForGoogleMap = true;
            mLastLocation = location;
        } else if (location != null) {

            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(userLocation, 17);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
            mMap.animateCamera(yourLocation);
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.6014536, 1.4421452000000272), 10));
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 5, locationListener);
            } else {
                Toast.makeText(this, R.string.geoloc_off, Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        if (mIsWaitingForGoogleMap) {
            moveCameraOnUser(mLastLocation);
        }

        // filter afin de n'afficher que certaines données, en lien avec le json dans raw (créé).
        MapStyleOptions mapFilter = MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.map_style);
        googleMap.setMapStyle(mapFilter);


        /** Partie Json Verre**/

        final TextView testPosition = findViewById(R.id.test_position);

        // Crée une file d'attente pour les requêtes vers l'API
        RequestQueue requestGlassQueue = Volley.newRequestQueue(this);

        String urlGlass = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-verre&refine.commune=TOULOUSE&rows=50";

        // Création de la requête vers l'API, ajout des écouteurs pour les réponses et erreurs possibles
        JsonObjectRequest jsonObjectRequestGlass = new JsonObjectRequest(
                Request.Method.GET, urlGlass, null,
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

                                //Cluster et Liste de celui ci pour aller aussi en ArrayList
                                mClusterMarker.add(new ClusterModel(valueOrdo, valueAbs, address, type, mGlassFilter));
                                mClusterManager.setRenderer(new OwRendering(getApplicationContext(), mMap, mClusterManager));
                                mClusterManager.addItems(mClusterMarker);

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
        requestGlassQueue.add(jsonObjectRequestGlass);

        /** Partie Json Papier/plastique **/
        // Crée une file d'attente pour les requêtes vers l'API
        RequestQueue requestPaperQueue = Volley.newRequestQueue(this);

        String urlPaper = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-emballage&refine.commune=TOULOUSE&rows=50";

        // Création de la requête vers l'API, ajout des écouteurs pour les réponses et erreurs possibles
        JsonObjectRequest jsonObjectRequestPaper = new JsonObjectRequest(
                Request.Method.GET, urlPaper, null,
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


                                //Cluster et Liste de celui ci pour aller aussi en liste
                                mClusterMarker.add(new ClusterModel(valueOrdo, valueAbs, address, type, mPaperfilter));
                                mClusterManager.setRenderer(new OwRendering(getApplicationContext(), mMap, mClusterManager));
                                mClusterManager.addItems(mClusterMarker);
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
        requestPaperQueue.add(jsonObjectRequestPaper);

        /**rajout list aux cluster*/
        mClusterManager = new ClusterManager<ClusterModel>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.addItems(mClusterMarker);

        Switch goList = findViewById(R.id.go_list);
        goList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent goList = new Intent(MapsActivity.this, ListLocationActivity.class);
                goList.putExtra("GPS_POSITIONS", mClusterMarker);
                startActivity(goList);
            }
        });
    }

    /** Bouton mystere (comme la tarte Tin tin !)
     private void initUI(View v) {
     Button button1 = (Button) v.findViewById(R.id.rob_the_bank);
     button1.setOnClickListener(new View.OnClickListener() {

    @Override public void onClick(View v) {
    //tu fais ce que tu veux dans le onClick
    Intent intentCo = new Intent(MapsActivity.this, LaCasaBonita.Restaurant);
    MapsActivity.this.startActivity(intentCo);
    }
    });
     } */
}
