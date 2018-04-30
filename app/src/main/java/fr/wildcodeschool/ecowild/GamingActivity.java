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

public class GamingActivity extends AppCompatActivity {

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

        final ConstraintLayout llBac = findViewById(R.id.linear_layout_bac);
        LinearLayout linearLayout = findViewById(R.id.linear_layout_normal);
        LinearLayout linearLayoutPaper = findViewById(R.id.linear_layout_paper);
        LinearLayout linearLayoutGlass = findViewById(R.id.linear_layout_glass);
        final ConstraintLayout cWaste = findViewById(R.id.constraintlayout_waste);

        final TextView tvInfosGame = findViewById(R.id.tv_infos);
        final Button btnYes = findViewById(R.id.button_yes);
        final Button btnNo = findViewById(R.id.button_no);
        Button btnBack= findViewById(R.id.button_back);


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
            //TODO: interaction avec les differentes poubelles
            return false;
        }
    };
}
