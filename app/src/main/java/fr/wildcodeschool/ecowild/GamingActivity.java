package fr.wildcodeschool.ecowild;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GamingActivity extends AppCompatActivity {
    public static int mEndGame = 0;
    public static int mXp = 0;
    private static int SPLASH_TIME_OUT = 2000;
    final int SPLASH_DISPLAY_LENGTH = 4000;

    //pour laisser une ombre en deplacant l'objet
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder, v, 0);
            return true;
        }
    };

    //interaction lorsqu'on lache les objets
    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final UserSingleton userSingleton = UserSingleton.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference user = database.getReference("utilisateurs");

            ImageView ivGlassGame = findViewById(R.id.iv_game_glass);
            ImageView ivPlasticBottleGame = findViewById(R.id.iv_game_plastic_bottle);
            ImageView ivNewspaperGame = findViewById(R.id.iv_game_newspaper);
            ImageView ivTacosGame = findViewById(R.id.iv_game_tacos);
            ImageView ivVaseGame = findViewById(R.id.iv_game_vase);
            ImageView ivCocaGame = findViewById(R.id.iv_game_coca);
            ImageView ivBagGame = findViewById(R.id.iv_game_bag);
            ImageView ivBurgerGame = findViewById(R.id.iv_game_burger);
            ImageView ivBottleGame = findViewById(R.id.iv_game_bottle);

            ImageView ivJedi = findViewById(R.id.iv_jedi);
            final ImageView ivJedi2 = findViewById(R.id.iv_jedi2);
            ImageView ivBublle = findViewById(R.id.iv_bulle);
            final ImageView ivBublle2 = findViewById(R.id.iv_bulle2);


            ImageView gifArrow = findViewById(R.id.iv_arrow);
            TextView tvInfosGame = findViewById(R.id.tv_infos);
            final TextView tvInfosGame2 = findViewById(R.id.tv_infos2);
            TextView tvScore = findViewById(R.id.tv_score);

            Animation fadeOutAnimation = AnimationUtils.loadAnimation(GamingActivity.this, R.anim.fade_out_animation);
            tvInfosGame.startAnimation(fadeOutAnimation);
            Glide.with(GamingActivity.this).load(R.drawable.loading_screen).into(gifArrow);

            final ConstraintLayout llBac = findViewById(R.id.linear_layout_bac);
            final ConstraintLayout cWaste = findViewById(R.id.constraintlayout_waste);
            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:

                    break;
                case DragEvent.ACTION_DROP:

                    //Dechets classiques
                    if ((view.getId() == R.id.iv_game_burger || view.getId() == R.id.iv_game_tacos || view.getId() == R.id.iv_game_glass || view.getId() == R.id.iv_game_vase) && v.getId() == R.id.linear_layout_normal) {

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView textToast = (TextView) layout.findViewById(R.id.text);
                        textToast.setText(R.string.positive_xp);

                        Toast toast = new Toast(GamingActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        if (view.getId() == R.id.iv_game_burger) {
                            ivBurgerGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive1);
                            mEndGame += 1;
                            mXp += 1;
                        } else if (view.getId() == R.id.iv_game_tacos) {
                            ivTacosGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive2);
                            mEndGame += 1;
                            mXp += 1;
                        } else if (view.getId() == R.id.iv_game_glass) {
                            ivGlassGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive3);
                            mEndGame += 1;
                            mXp += 1;
                        } else if (view.getId() == R.id.iv_game_vase) {
                            ivVaseGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive4);
                            mEndGame += 1;
                            mXp += 1;
                        }

                        tvScore.setText(getString(R.string.score) + " " + Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_game_burger || view.getId() == R.id.iv_game_tacos || view.getId() == R.id.iv_game_glass || view.getId() == R.id.iv_game_vase) && (v.getId() == R.id.linear_layout_paper || v.getId() == R.id.linear_layout_glass)) {

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView textToast = (TextView) layout.findViewById(R.id.text);
                        textToast.setText(R.string.negative_xp);

                        Toast toast = new Toast(GamingActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        if (view.getId() == R.id.iv_game_burger) {
                            ivBurgerGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative1);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_tacos) {
                            ivTacosGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative2);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_glass) {
                            ivGlassGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative3);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_vase) {
                            ivVaseGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative4);
                            mEndGame += 1;
                            mXp -= 1;
                        }

                        tvScore.setText(getString(R.string.score) + " " + Integer.valueOf(mXp).toString());

                    }

                    //dechets verre
                    if ((view.getId() == R.id.iv_game_bottle) && v.getId() == R.id.linear_layout_glass) {

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView textToast = (TextView) layout.findViewById(R.id.text);
                        textToast.setText(R.string.positive_xp);

                        Toast toast = new Toast(GamingActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        if (view.getId() == R.id.iv_game_bottle) {
                            ivBottleGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive5);
                            mEndGame += 1;
                            mXp += 1;
                        }

                        tvScore.setText(getString(R.string.score) + " " + Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_game_bottle) && (v.getId() == R.id.linear_layout_paper || v.getId() == R.id.linear_layout_normal)) {

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView textToast = (TextView) layout.findViewById(R.id.text);
                        textToast.setText(R.string.negative_xp);
                        Toast toast = new Toast(GamingActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        if (view.getId() == R.id.iv_game_bottle) {
                            ivBottleGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative5);
                            mEndGame += 1;
                            mXp -= 1;
                        }

                        tvScore.setText(getString(R.string.score) + " " + Integer.valueOf(mXp).toString());

                    }

                    //dechet papier
                    if ((view.getId() == R.id.iv_game_newspaper || view.getId() == R.id.iv_game_bag || view.getId() == R.id.iv_game_coca || view.getId() == R.id.iv_game_plastic_bottle) && v.getId() == R.id.linear_layout_paper) {

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView textToast = (TextView) layout.findViewById(R.id.text);
                        textToast.setText(R.string.positive_xp);

                        Toast toast = new Toast(GamingActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        if (view.getId() == R.id.iv_game_newspaper) {
                            ivNewspaperGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive1);
                            mEndGame += 1;
                            mXp += 1;
                        } else if (view.getId() == R.id.iv_game_bag) {
                            ivBagGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive2);
                            mEndGame += 1;
                            mXp += 1;
                        } else if (view.getId() == R.id.iv_game_coca) {
                            ivCocaGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive4);
                            mEndGame += 1;
                            mXp += 1;
                        } else if (view.getId() == R.id.iv_game_plastic_bottle) {
                            ivPlasticBottleGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive6);
                            mEndGame += 1;
                            mXp += 1;
                        }

                        tvScore.setText(getString(R.string.score) + " " + Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_game_newspaper || view.getId() == R.id.iv_game_bag || view.getId() == R.id.iv_game_coca || view.getId() == R.id.iv_game_plastic_bottle) && (v.getId() == R.id.linear_layout_glass || v.getId() == R.id.linear_layout_normal)) {

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView textToast = (TextView) layout.findViewById(R.id.text);
                        textToast.setText(R.string.negative_xp);

                        Toast toast = new Toast(GamingActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        if (view.getId() == R.id.iv_game_newspaper) {
                            ivNewspaperGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative1);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_bag) {
                            ivBagGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative2);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_coca) {
                            ivCocaGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative4);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_plastic_bottle) {
                            ivPlasticBottleGame.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative6);
                            mEndGame += 1;
                            mXp -= 1;
                        }

                        tvScore.setText(getString(R.string.score) + " " + Integer.valueOf(mXp).toString());

                    }

                    if (mEndGame == 9) {
                        mEndGame = 0;

                        Animation zoomAnimation = AnimationUtils.loadAnimation(GamingActivity.this, R.anim.zoom);
                        ivJedi2.startAnimation(zoomAnimation);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivBublle2.setVisibility(View.VISIBLE);
                                tvInfosGame2.setVisibility(View.VISIBLE);

                            }
                        }, SPLASH_TIME_OUT);

                        if (mXp > 0) {
                            tvInfosGame2.setText(getString(R.string.gain_game) + " " + Integer.valueOf(mXp).toString() + " " + getString(R.string.gain2));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(GamingActivity.this, MapsActivity.class);
                                    startActivity(intent);
                                }
                            }, SPLASH_DISPLAY_LENGTH);

                        } else {
                            tvInfosGame2.setText(getString(R.string.lose) + " " + Integer.valueOf(mXp).toString() + " " + getString(R.string.lose2));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(GamingActivity.this, MapsActivity.class);
                                    startActivity(intent);
                                }
                            }, SPLASH_DISPLAY_LENGTH);
                        }


                        ivJedi2.setVisibility(View.VISIBLE);
                        llBac.setVisibility(View.INVISIBLE);
                        cWaste.setVisibility(View.INVISIBLE);
                        gifArrow.setVisibility(View.INVISIBLE);
                        tvInfosGame.setVisibility(View.INVISIBLE);
                        ivJedi.setVisibility(View.INVISIBLE);
                        ivBublle.setVisibility(View.INVISIBLE);
                        tvScore.setVisibility(View.INVISIBLE);


                        user.orderByChild("name").equalTo(userSingleton.getTextName()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userdataSnapshot : dataSnapshot.getChildren()) {

                                    if ((userSingleton.getIntXp() % 10) +mXp >= 10) {
                                        userSingleton.setIntXp(userSingleton.getIntXp() + mXp);
                                        userSingleton.setIntLevel(userSingleton.getIntLevel() + 1);
                                        String key = userdataSnapshot.getKey().toString();
                                        user.child(key).child("xp").setValue(userSingleton.getIntXp());
                                        user.child(key).child("level").setValue(userSingleton.getIntLevel());


                                    } else {
                                        String key = userdataSnapshot.getKey().toString();
                                        userSingleton.setIntXp(userSingleton.getIntXp() + mXp);
                                        user.child(key).child("xp").setValue(userSingleton.getIntXp());

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                    }

                    break;


            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        ImageView ivGlassGame = findViewById(R.id.iv_game_glass);
        ImageView ivPlasticBottleGame = findViewById(R.id.iv_game_plastic_bottle);
        ImageView ivNewspaperGame = findViewById(R.id.iv_game_newspaper);
        ImageView ivTacosGame = findViewById(R.id.iv_game_tacos);
        ImageView ivVaseGame = findViewById(R.id.iv_game_vase);
        ImageView ivCocaGame = findViewById(R.id.iv_game_coca);
        ImageView ivBagGame = findViewById(R.id.iv_game_bag);
        ImageView ivBurgerGame = findViewById(R.id.iv_game_burger);
        ImageView ivBottleGame = findViewById(R.id.iv_game_bottle);
        final ImageView gifArrow = findViewById(R.id.iv_arrow);


        final ConstraintLayout llBac = findViewById(R.id.linear_layout_bac);
        LinearLayout linearLayout = findViewById(R.id.linear_layout_normal);
        LinearLayout linearLayoutPaper = findViewById(R.id.linear_layout_paper);
        LinearLayout linearLayoutGlass = findViewById(R.id.linear_layout_glass);
        final ConstraintLayout cWaste = findViewById(R.id.constraintlayout_waste);

        final TextView tvInfosGame = findViewById(R.id.tv_infos);
        final TextView tvScore = findViewById(R.id.tv_score);
        final Button btnYes = findViewById(R.id.button_yes);
        final Button btnNo = findViewById(R.id.button_no);
        Button btnBack = findViewById(R.id.button_back);

        Date dateDay = new Date();
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat month = new SimpleDateFormat("MM");

        SharedPreferences dateSharedPreferences = getSharedPreferences("Date", Activity.MODE_PRIVATE);
        int dayInit = dateSharedPreferences.getInt("Day", 0);
        int monthInit = dateSharedPreferences.getInt("Month", 0);

        String stringDay1 = day.format(dateDay);
        String stringMonth1 = month.format(dateDay);
        int month1 = Integer.parseInt(stringDay1);
        int day1 = Integer.parseInt(stringMonth1);

        if (monthInit == month1 && dayInit == day1) {
            tvInfosGame.setText(R.string.dejajoue);
            btnNo.setVisibility(View.GONE);
            btnYes.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(GamingActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
            }, SPLASH_DISPLAY_LENGTH);

        } else {
            ivGlassGame.setOnTouchListener(onTouchListener);
            ivPlasticBottleGame.setOnTouchListener(onTouchListener);
            ivNewspaperGame.setOnTouchListener(onTouchListener);
            ivTacosGame.setOnTouchListener(onTouchListener);
            ivVaseGame.setOnTouchListener(onTouchListener);
            ivCocaGame.setOnTouchListener(onTouchListener);
            ivBagGame.setOnTouchListener(onTouchListener);
            ivBurgerGame.setOnTouchListener(onTouchListener);
            ivBottleGame.setOnTouchListener(onTouchListener);

            SharedPreferences.Editor editorPreferences = dateSharedPreferences.edit();
            editorPreferences.putInt("Day", day1);
            editorPreferences.putInt("Month", month1);
            editorPreferences.commit();
        }

        linearLayout.setOnDragListener(dragListener);
        linearLayoutPaper.setOnDragListener(dragListener);
        linearLayoutGlass.setOnDragListener(dragListener);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvInfosGame.setText(R.string.apprenti);
                tvInfosGame.setVisibility(View.VISIBLE);
                btnNo.setVisibility(View.GONE);
                btnYes.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(GamingActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }
                }, SPLASH_TIME_OUT);
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cWaste.setVisibility(View.VISIBLE);
                llBac.setVisibility(View.VISIBLE);
                btnNo.setVisibility(View.GONE);
                btnYes.setVisibility(View.GONE);
                tvInfosGame.setText(R.string.go_game);
                tvScore.setVisibility(View.VISIBLE);
                gifArrow.setVisibility(View.VISIBLE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamingActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


    }
}
