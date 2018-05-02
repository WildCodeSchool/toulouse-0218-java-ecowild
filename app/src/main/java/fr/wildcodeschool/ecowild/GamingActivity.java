package fr.wildcodeschool.ecowild;

import android.content.ClipData;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
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


public class GamingActivity extends AppCompatActivity {
    public static int mEndGame = 0;
    public static int mXp = 0;
    private static int SPLASH_TIME_OUT = 100;
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

            ImageView ivGlassTable = findViewById(R.id.iv_game_burger);
            ImageView ivFalseGlass = findViewById(R.id.iv_game_jam);
            ImageView ivTacos = findViewById(R.id.iv_game_cardboard);

            ImageView ivPaper = findViewById(R.id.iv_game_bottle);
            ImageView ivCardboard = findViewById(R.id.iv_game_bag);

            ImageView ivJar = findViewById(R.id.iv_game_coca);
            ImageView ivGlass = findViewById(R.id.iv_game_glass);

            ImageView ivJedi = findViewById(R.id.iv_jedi);
            ImageView ivJedi2 = findViewById(R.id.iv_jedi2);
            ImageView ivBublle = findViewById(R.id.iv_bulle);
            ImageView ivBublle2 = findViewById(R.id.iv_bulle2);


            ImageView gifArrow = findViewById(R.id.iv_arrow);
            TextView tvInfosGame = findViewById(R.id.tv_infos);
            TextView tvInfosGame2 = findViewById(R.id.tv_infos2);
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
                    if ((view.getId() == R.id.iv_game_burger || view.getId() == R.id.iv_game_jam || view.getId() == R.id.iv_game_cardboard) && v.getId() == R.id.linear_layout_normal) {
                        Toast.makeText(GamingActivity.this, R.string.positive_xp, Toast.LENGTH_SHORT).show();
                        tvInfosGame.setText(" ");
                        if (view.getId() == R.id.iv_game_burger) {
                            ivGlassTable.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive1);
                            mEndGame += 1;
                            mXp += 1;
                        } else if (view.getId() == R.id.iv_game_jam) {
                            ivFalseGlass.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive3);
                            mEndGame += 1;
                            mXp += 1;
                        } else {
                            ivTacos.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positve4);
                            mEndGame += 1;
                            mXp += 1;
                        }
                        tvScore.setText(getString(R.string.score) + Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_game_burger || view.getId() == R.id.iv_game_cardboard || view.getId() == R.id.iv_game_jam) && (v.getId() == R.id.linear_layout_paper || v.getId() == R.id.linear_layout_glass)) {
                        Toast.makeText(GamingActivity.this, R.string.negative_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_game_burger) {
                            ivGlassTable.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative1);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_jam) {
                            ivFalseGlass.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative3);
                            mEndGame += 1;
                            mXp -= 1;
                        } else {
                            ivTacos.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative4);
                            mEndGame += 1;
                            mXp -= 1;

                        }

                        tvScore.setText(getString(R.string.score) + Integer.valueOf(mXp).toString());
                    }

                    //dechets verre
                    if ((view.getId() == R.id.iv_game_glass || view.getId() == R.id.iv_game_coca) && v.getId() == R.id.linear_layout_glass) {
                        Toast.makeText(GamingActivity.this, R.string.positive_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_game_glass) {
                            ivGlass.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive3);
                            mEndGame += 1;
                            mXp += 1;
                        } else {
                            ivJar.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive2);
                            mEndGame += 1;
                            mXp += 1;
                        }

                        tvScore.setText(getString(R.string.score) + Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_game_glass || view.getId() == R.id.iv_game_coca) && (v.getId() == R.id.linear_layout_paper || v.getId() == R.id.linear_layout_normal)) {
                        Toast.makeText(GamingActivity.this, R.string.negative_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_game_glass) {
                            ivGlass.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative5);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_coca) {
                            ivJar.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative6);
                            mEndGame += 1;
                            mXp -= 1;
                        }
                        tvScore.setText(getString(R.string.score) + Integer.valueOf(mXp).toString());

                    }

                    //dechet papier
                    if ((view.getId() == R.id.iv_game_bag || view.getId() == R.id.iv_game_bottle) && v.getId() == R.id.linear_layout_paper) {
                        Toast.makeText(GamingActivity.this, R.string.positive_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_game_bag) {
                            ivCardboard.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive1);
                            mEndGame += 1;
                            mXp += 1;
                        } else {
                            ivPaper.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive3);
                            mEndGame += 1;
                            mXp += 1;
                        }

                        tvScore.setText(getString(R.string.score) + Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_game_bag || view.getId() == R.id.iv_game_bottle) && (v.getId() == R.id.linear_layout_glass || v.getId() == R.id.linear_layout_normal)) {
                        Toast.makeText(GamingActivity.this, R.string.negative_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_game_bag) {
                            ivCardboard.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative7);
                            mEndGame += 1;
                            mXp -= 1;
                        } else if (view.getId() == R.id.iv_game_bottle) {
                            ivPaper.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative8);
                            mEndGame += 1;
                            mXp -= 1;
                        }
                        tvScore.setText(getString(R.string.score) + Integer.valueOf(mXp).toString());

                    }
                    if (mEndGame == 8) {
                        if (mXp > 0) {
                            tvInfosGame2.setText(getString(R.string.gain_game) + Integer.valueOf(mXp).toString() + getString(R.string.gain2));

                        } else {
                            tvInfosGame2.setText(getString(R.string.lose) + Integer.valueOf(mXp).toString() + getString(R.string.lose2));

                        }

                        llBac.setVisibility(View.INVISIBLE);
                        cWaste.setVisibility(View.INVISIBLE);
                        gifArrow.setVisibility(View.INVISIBLE);
                        tvInfosGame.setVisibility(View.INVISIBLE);
                        ivJedi.setVisibility(View.INVISIBLE);
                        ivBublle.setVisibility(View.INVISIBLE);
                        ivJedi2.setVisibility(View.VISIBLE);
                        tvInfosGame2.setVisibility(View.VISIBLE);
                        ivBublle2.setVisibility(View.VISIBLE);
                        tvScore.setVisibility(View.INVISIBLE);

                        userSingleton.setIntXp(userSingleton.getIntLevel() + mXp);
                        user.orderByChild("name").equalTo(userSingleton.getTextName()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userdataSnapshot : dataSnapshot.getChildren()) {

                                    String key = userdataSnapshot.getKey().toString();
                                    user.child(key).child("xp").setValue(userSingleton.getIntXp());
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


        ImageView ivVerre = findViewById(R.id.iv_game_burger);
        ImageView ivFalseGlass = findViewById(R.id.iv_game_jam);
        ImageView ivTacos = findViewById(R.id.iv_game_cardboard);

        ImageView ivPaper = findViewById(R.id.iv_game_bottle);
        ImageView ivCardboard = findViewById(R.id.iv_game_bag);

        ImageView ivJar = findViewById(R.id.iv_game_coca);
        ImageView ivGlass = findViewById(R.id.iv_game_glass);
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


        ivVerre.setOnTouchListener(onTouchListener);
        ivFalseGlass.setOnTouchListener(onTouchListener);
        ivPaper.setOnTouchListener(onTouchListener);
        ivCardboard.setOnTouchListener(onTouchListener);
        ivJar.setOnTouchListener(onTouchListener);
        ivGlass.setOnTouchListener(onTouchListener);
        ivTacos.setOnTouchListener(onTouchListener);


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
