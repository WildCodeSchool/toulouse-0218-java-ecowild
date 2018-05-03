package fr.wildcodeschool.ecowild;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
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
import android.support.v4.widget.DrawerLayout;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import org.w3c.dom.Text;

import java.util.ArrayList;
import br.com.bloder.magic.view.MagicButton;

import static android.view.MotionEvent.ACTION_UP;
import static fr.wildcodeschool.ecowild.ConnectionActivity.CACHE_USERNAME;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 6786;
    private static int SPLASH_TIME_OUT = 100;
    public boolean NOT_MOVE = true;
    DrawerLayout mDrawerLayout;
    boolean mGlassFilter = true;
    boolean mPaperfilter = true;
    boolean mIsWaitingForGoogleMap = false;
    Location mLastLocation = null;
    float dX;
    float dY;
    int mButtonPositionX;
    int mButtonPositionY;
    int mStratAngle = 180;
    int mEndAngle = 270;
    FloatingActionMenu mActionMenu;
    int mScreenSizeX;
    int mScreenSizeY;
    int lastAction;
    private ClusterManager<ClusterModel> mClusterManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        final ImageView buttonLeft = findViewById(R.id.iv_left);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenSizeX = size.x;
        mScreenSizeY = size.y;

        final SharedPreferences sharedPrefProfil = this.getSharedPreferences("ECOWILD", Context.MODE_PRIVATE);
        final String username = sharedPrefProfil.getString(CACHE_USERNAME, "");


        /**initi singleton*/
        final UserSingleton userSingleton = UserSingleton.getInstance();
        final ImageView accountImgCreation = findViewById(R.id.img_profil);


        /** Partie menu Circle**/
        //Image bouton Menu
        ImageView iconMenu = new ImageView(this);
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
        final SubActionButton sabPaper = listeBuilder.setContentView(paperFilterGlass).build();

        Resources ressource = getResources();
        int valuePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, ressource.getDisplayMetrics());

        DrawerLayout.LayoutParams layoutParam = new DrawerLayout.LayoutParams(valuePx, valuePx);
        sabPaper.setLayoutParams(layoutParam);
        sabGlass.setLayoutParams(layoutParam);


        //Creation bouton sous menu
        mActionMenu = new FloatingActionMenu.Builder(MapsActivity.this)
                .addSubActionView(sabGlass)
                .addSubActionView(sabPaper)
                .setStartAngle(mStratAngle)
                .setEndAngle(mEndAngle)
                .attachTo(actionButton)
                .build();

        /** Partie XP */
        final ProgressBar pbXpImg = findViewById(R.id.pb_xp);
        final TextView rank = findViewById(R.id.tv_rank);
        final TextView level = findViewById(R.id.tv_level);
        final TextView xp = findViewById(R.id.tv_xp);
        rank.setText(userSingleton.getTextRank());
        final MagicButton mbXp = findViewById(R.id.magic_button);
        final int intGainExperience = 1;
        final ImageView imgRang = findViewById(R.id.img_rang);

        mbXp.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSingleton.setIntXp(userSingleton.getIntXp() + intGainExperience);
                int currentXp = userSingleton.getIntXp() % 10;

                pbXpImg.setProgress(currentXp);

                if (userSingleton.getIntXp() % 10 == 0) {
                    pbXpImg.setProgress(0);
                    userSingleton.setIntLevel(userSingleton.getIntLevel() + 1);
                    level.setText(String.format(getString(R.string.lvl), userSingleton.getIntLevel()));
                    xp.setText(R.string.xppp);
                }

                if (userSingleton.getIntXp() % 10 != 0) {
                    xp.setText(String.format(getString(R.string.xp_progress), userSingleton.getIntXp() % 10));
                }

                if (userSingleton.getIntLevel() >= 10) {
                    rank.setText(R.string.rang5);
                    userSingleton.setTextRank("EcoGod");
                    Glide.with(MapsActivity.this).load(R.drawable.ecogod_cercle).into(imgRang);
                } else if (userSingleton.getIntLevel() >= 7) {
                    rank.setText(R.string.rang4);
                    userSingleton.setTextRank("EcoWild");
                    Glide.with(MapsActivity.this).load(R.drawable.ecowild_cercle).into(imgRang);
                } else if (userSingleton.getIntLevel() >= 5) {
                    rank.setText(R.string.rang3);
                    userSingleton.setTextRank("EcoFan");
                    Glide.with(MapsActivity.this).load(R.drawable.ecofan_cercle).into(imgRang);
                } else if (userSingleton.getIntLevel() >= 3) {
                    rank.setText(R.string.rang2);
                    userSingleton.setTextRank("EcoCool");
                    Glide.with(MapsActivity.this).load(R.drawable.ecocool_cercle).into(imgRang);
                } else if (userSingleton.getIntLevel() >= 1) {
                    rank.setText(R.string.rang1);
                    userSingleton.setTextRank("EcoNoob");
                    Glide.with(MapsActivity.this).load(R.drawable.econoob_cercle).into(imgRang);
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

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference user = database.getReference("utilisateurs");
                user.orderByChild("name").equalTo(userSingleton.getTextName()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userdataSnapshot : dataSnapshot.getChildren()) {

                            String key = userdataSnapshot.getKey().toString();
                            user.child(key).child("xp").setValue(userSingleton.getIntXp());
                            user.child(key).child("level").setValue(userSingleton.getIntLevel());
                            user.child(key).child("rank").setValue(userSingleton.getTextRank());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
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
                    final View dragView = findViewById(R.id.iv_left);
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
                    final View dragView = findViewById(R.id.iv_left);
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
                    final View dragView = findViewById(R.id.iv_left);
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
                                    mButtonPositionX = Math.round(view.getX());
                                    mButtonPositionY = Math.round(view.getY());

                                    //cas de position et ouverture
                                    if ((mButtonPositionX < (mScreenSizeX/2)) && (mButtonPositionY < (mScreenSizeY/2))) {
                                        mStratAngle = 0;
                                        mEndAngle = 90;
                                    }

                                    if ((mButtonPositionX < (mScreenSizeX/2)) && (mButtonPositionY > (mScreenSizeY/2))) {
                                        mStratAngle = 90;
                                        mEndAngle = 180;
                                    }

                                    if ((mButtonPositionX > (mScreenSizeX/2)) && (mButtonPositionY < (mScreenSizeY/2))) {
                                        mStratAngle = 0;
                                        mEndAngle = 90;
                                    }

                                    if ((mButtonPositionX > (mScreenSizeX/2)) && (mButtonPositionY > (mScreenSizeY/2))) {
                                        mStratAngle = 180;
                                        mEndAngle = 270;
                                    }

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
                    final View dragView = findViewById(R.id.iv_left);
                    dragView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });

                }
            }
        });

        TextView tvJeu = findViewById(R.id.tv_jeu);
        ImageView ivJeu = findViewById(R.id.iv_jeu);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        TextView textToast = (TextView) layout.findViewById(R.id.text);
        textToast.setText(R.string.coonected);
        final Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        final Intent intentParameter = new Intent(MapsActivity.this, Settings.class);
        final Intent intentUsefulInformation = new Intent(MapsActivity.this, UsefulInformationActivity.class);
        final Intent intentAccount = new Intent(MapsActivity.this, ConnectionActivity.class);
        final Intent intentJeu = new Intent(MapsActivity.this, GamingActivity.class);
        tvParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toast.show();
            }
        });

        ivParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.show();
            }
        });

        tvJeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toast.show();
            }
        });

        ivJeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.show();
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
                startActivity(intentAccount);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAccount);
            }
        });



        if (ConnectionActivity.CONNECTED || !username.isEmpty()) {
            pseudo.setText(userSingleton.getTextName());
            level.setText(getString(R.string.xp_connection) + Integer.valueOf(userSingleton.getIntLevel()).toString());
            xp.setText(Integer.valueOf(userSingleton.getIntXp() % 10).toString() + getString(R.string.xp_ooo));
            pseudo.setVisibility(View.VISIBLE);
            rank.setVisibility(View.VISIBLE);
            pbXpImg.setProgress(userSingleton.getIntXp() % 10);
            rank.setText(userSingleton.getTextRank());
            level.setVisibility(View.VISIBLE);
            xp.setVisibility(View.VISIBLE);
            btnCreateAccount.setVisibility(View.GONE);
            Glide.with(MapsActivity.this).load(userSingleton.getTextAvatar()).apply(RequestOptions.circleCropTransform()).into(accountImgCreation);
            Glide.with(MapsActivity.this).load(userSingleton.getTextAvatar()).apply(RequestOptions.circleCropTransform()).into(buttonLeft);
            accountImgCreation.setBackground(null);

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

            tvJeu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(intentJeu);
                }
            });

            ivJeu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intentJeu);
                }
            });
            if (userSingleton.getTextAvatar() == null) {
                accountImgCreation.setBackgroundResource(R.drawable.icon_avatar);

            }
        }

        /**Map**/
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /** Drawer **/

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        /** on ecoute le drawer (sil ouvert ou pas)
         * A chaque changement, on affiche ou non le bouton
         *
         * On anime l'apparition ou non du bouton, et ce avec une animation
         *
         * On met l'animation sur ecoute pour que le changement d'etat se fasse de maniere smooth
         *
         */

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(final View drawerView) {

                Animation fadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_animation);
                buttonLeft.startAnimation(fadeOutAnimation);
                actionButton.startAnimation(fadeOutAnimation);
                sabPaper.startAnimation(fadeOutAnimation);
                sabGlass.startAnimation(fadeOutAnimation);
                fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        actionButton.setVisibility(View.VISIBLE);
                        buttonLeft.setVisibility(View.VISIBLE);
                        sabGlass.setVisibility(View.VISIBLE);
                        sabPaper.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        actionButton.setVisibility(View.GONE);
                        buttonLeft.setVisibility(View.GONE);
                        sabGlass.setVisibility(View.GONE);
                        sabPaper.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                actionButton.setClickable(false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
                buttonLeft.startAnimation(fadeInAnimation);
                actionButton.startAnimation(fadeInAnimation);
                sabGlass.startAnimation(fadeInAnimation);
                sabPaper.startAnimation(fadeInAnimation);
                fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        actionButton.setVisibility(View.VISIBLE);
                        buttonLeft.setVisibility(View.VISIBLE);
                        sabPaper.setVisibility(View.VISIBLE);
                        sabGlass.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                actionButton.setClickable(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        /**Filtres verre et papier menu multiples droit**/
        sabGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGlassFilter) {
                    mGlassFilter = false;
                    if ((!mPaperfilter) & (!mGlassFilter)) {
                        glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verresansfond));
                        mClusterManager.setRenderer(new OwRenderingGlass(getApplicationContext(), mMap, mClusterManager));
                        paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papier));
                        mPaperfilter = true;
                    } else {
                        mClusterManager.setRenderer(new OwRenderingGlass(getApplicationContext(), mMap, mClusterManager));
                        glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verresansfond));
                    }
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
                    if ((!mPaperfilter) & (!mGlassFilter)) {

                        mClusterManager.setRenderer(new OwRenderingPaper(getApplicationContext(), mMap, mClusterManager));
                        paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papiersansfond));
                        glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verre));

                        mGlassFilter = true;
                    } else {
                        mClusterManager.setRenderer(new OwRenderingPaper(getApplicationContext(), mMap, mClusterManager));
                        paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papiersansfond));
                    }

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                    snackbar.setDuration(6000);
                    snackbar.setActionTextColor(ContextCompat.getColor(MapsActivity.this, R.color.colorEcoWild2));
                    snackBarView.setBackgroundColor(ContextCompat.getColor(MapsActivity.this, R.color.colorEcoWild));
                    snackbar.show();

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

        /**rajout list aux cluster **/
        mClusterManager = new ClusterManager<ClusterModel>(this, mMap);
        mClusterManager.setRenderer(new OwRendering(getApplicationContext(), mMap, mClusterManager));
        LoadAPISingleton loadAPISingleton = LoadAPISingleton.getInstance();
        mClusterManager.addItems(loadAPISingleton.getClusterList());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String address = marker.getTitle();

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + address + "&mode=b"));
                startActivity(intent);
            }
        });

        Switch goList = findViewById(R.id.go_list);
        goList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent goList = new Intent(MapsActivity.this, ListLocationActivity.class);
                startActivity(goList);
            }
        });
    }
}
