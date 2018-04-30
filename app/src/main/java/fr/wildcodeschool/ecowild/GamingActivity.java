package fr.wildcodeschool.ecowild;

import android.content.ClipData;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class GamingActivity extends AppCompatActivity {
    public static int mENDGAME =0;
    public static int mXp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);


        ImageView ivVerre = findViewById(R.id.iv_glass_table);
        ImageView ivVase = findViewById(R.id.iv_vase);
        ImageView ivFalseGlass = findViewById(R.id.iv_false_glass);
        ImageView ivTacos = findViewById(R.id.iv_tacos);

        ImageView ivPaper = findViewById(R.id.iv_paper);
        ImageView ivCardboard = findViewById(R.id.iv_cardboard);

        ImageView ivJar = findViewById(R.id.iv_jar);
        ImageView ivGlass = findViewById(R.id.iv_glass);
        final ImageView gifArrow =findViewById(R.id.iv_arrow);


        final ConstraintLayout llBac = findViewById(R.id.linear_layout_bac);
        LinearLayout linearLayout = findViewById(R.id.linear_layout_normal);
        LinearLayout linearLayoutPaper = findViewById(R.id.linear_layout_paper);
        LinearLayout linearLayoutGlass = findViewById(R.id.linear_layout_glass);
        final ConstraintLayout cWaste = findViewById(R.id.constraintlayout_waste);

        final TextView tvInfosGame = findViewById(R.id.tv_infos);
        final TextView tvScore=findViewById(R.id.tv_score);
        final Button btnYes = findViewById(R.id.button_yes);
        final Button btnNo = findViewById(R.id.button_no);
        Button btnBack= findViewById(R.id.button_back);

        //TODO: mettre en place gif de la fleche
        //Glide.with(GamingActivity.this).load(R.drawable.arrow_animated).into(gifArrow);

        ivVerre.setOnTouchListener(onTouchListener);
        ivVase.setOnTouchListener(onTouchListener);
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
                Intent intent =new Intent(GamingActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //TODO: mettre l'experience dans Singleton et Firebase

    }

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
            ImageView ivGlassTable = findViewById(R.id.iv_glass_table);
            ImageView ivVase = findViewById(R.id.iv_vase);
            ImageView ivFalseGlass = findViewById(R.id.iv_false_glass);
            ImageView ivTacos = findViewById(R.id.iv_tacos);

            ImageView ivPaper = findViewById(R.id.iv_paper);
            ImageView ivCardboard = findViewById(R.id.iv_cardboard);

            ImageView ivJar = findViewById(R.id.iv_jar);
            ImageView ivGlass = findViewById(R.id.iv_glass);

            ImageView gifArrow =findViewById(R.id.iv_arrow);
            TextView tvInfosGame = findViewById(R.id.tv_infos);
            TextView tvScore =findViewById(R.id.tv_score);

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
                    if ((view.getId() == R.id.iv_glass_table || view.getId() == R.id.iv_vase || view.getId() == R.id.iv_false_glass || view.getId() == R.id.iv_tacos) && v.getId() == R.id.linear_layout_normal) {
                        Toast.makeText(GamingActivity.this, R.string.positive_xp, Toast.LENGTH_SHORT).show();
                        tvInfosGame.setText(" ");
                        if (view.getId() == R.id.iv_glass_table) {
                            ivGlassTable.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive1);
                            mENDGAME += 1;
                            mXp +=1;
                        } else if (view.getId() == R.id.iv_vase) {
                            ivVase.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive2);
                            mENDGAME += 1;
                            mXp +=1;
                        } else if (view.getId() == R.id.iv_false_glass) {
                            ivFalseGlass.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive3);
                            mENDGAME += 1;
                            mXp +=1;
                        } else {
                            ivTacos.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positve4);
                            mENDGAME += 1;
                            mXp +=1;
                        }
                        tvScore.setText(getString(R.string.score)+Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_glass_table || view.getId() == R.id.iv_vase || view.getId() == R.id.iv_tacos || view.getId() == R.id.iv_false_glass) && (v.getId() == R.id.linear_layout_paper || v.getId() == R.id.linear_layout_glass)) {
                        Toast.makeText(GamingActivity.this, R.string.negative_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_glass_table) {
                            ivGlassTable.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative1);
                            mENDGAME += 1;
                            mXp -=1;
                        } else if (view.getId() == R.id.iv_vase) {
                            ivVase.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negatuve2);
                            mENDGAME += 1;
                            mXp -=1;
                        } else if (view.getId() == R.id.iv_false_glass) {
                            ivFalseGlass.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative3);
                            mENDGAME += 1;
                            mXp -=1;
                        }
                        else {
                            ivTacos.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative4);
                            mENDGAME += 1;
                            mXp -=1;

                        }

                        tvScore.setText(getString(R.string.score)+Integer.valueOf(mXp).toString());
                    }

                    //dechets verre
                    if ((view.getId() == R.id.iv_glass || view.getId() == R.id.iv_jar) && v.getId() == R.id.linear_layout_glass) {
                        Toast.makeText(GamingActivity.this, R.string.positive_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_glass) {
                            ivGlass.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive3);
                            mENDGAME += 1;
                            mXp +=1;
                        } else {
                            ivJar.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive2);
                            mENDGAME += 1;
                            mXp +=1;
                        }

                        tvScore.setText(getString(R.string.score)+Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_glass || view.getId() == R.id.iv_jar) && (v.getId() == R.id.linear_layout_paper || v.getId() == R.id.linear_layout_normal)) {
                        Toast.makeText(GamingActivity.this, R.string.negative_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_glass) {
                            ivGlass.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative5);
                            mENDGAME += 1;
                            mXp -=1;
                        } else if (view.getId() == R.id.iv_jar) {
                            ivJar.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative6);
                            mENDGAME += 1;
                            mXp -=1;
                        }
                        tvScore.setText(getString(R.string.score)+Integer.valueOf(mXp).toString());

                    }

                    //dechet papier
                    if ((view.getId() == R.id.iv_cardboard || view.getId() == R.id.iv_paper) && v.getId() == R.id.linear_layout_paper) {
                        Toast.makeText(GamingActivity.this, R.string.positive_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_cardboard) {
                            ivCardboard.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive1);
                            mENDGAME += 1;
                            mXp +=1;
                        } else {
                            ivPaper.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.positive3);
                            mENDGAME += 1;
                            mXp +=1;
                        }

                        tvScore.setText(getString(R.string.score)+Integer.valueOf(mXp).toString());

                    } else if ((view.getId() == R.id.iv_cardboard || view.getId() == R.id.iv_paper) && (v.getId() == R.id.linear_layout_glass || v.getId() == R.id.linear_layout_normal)) {
                        Toast.makeText(GamingActivity.this, R.string.negative_xp, Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.iv_cardboard) {
                            ivCardboard.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative7);
                            mENDGAME += 1;
                            mXp -=1;
                        } else if (view.getId() == R.id.iv_paper) {
                            ivPaper.setVisibility(View.INVISIBLE);
                            tvInfosGame.setText(R.string.negative8);
                            mENDGAME += 1;
                            mXp -=1;
                        }
                        tvScore.setText(getString(R.string.score)+Integer.valueOf(mXp).toString());

                    }
                    if (mENDGAME ==8) {
                        if (mXp > 0) {
                            Toast.makeText(GamingActivity.this, "Tu as obtenu " + Integer.valueOf(mXp).toString() + " XP en plus", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(GamingActivity.this, "Tu viens de perdre " + Integer.valueOf(mXp).toString() + " XP", Toast.LENGTH_SHORT).show();

                        }

                    llBac.setVisibility(View.INVISIBLE);
                        cWaste.setVisibility(View.INVISIBLE);
                        gifArrow.setVisibility(View.INVISIBLE);
                    }

                    break;


            }

            return true;
        }
    };
}
