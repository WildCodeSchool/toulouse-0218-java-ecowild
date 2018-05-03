package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class PageQuatreInformation extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.activity_page_quatre_information, container, false);




    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //TODO changer anim
        ImageView ivJedi =getView().findViewById(R.id.iv_jedi);
        ImageView ivBulle = getView().findViewById(R.id.iv_bulle);
        TextView tvGame =getView().findViewById(R.id.tv_go);
        Button btnHere =getView().findViewById(R.id.btn_here);

        Animation fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_animation);
        ivBulle.startAnimation(fadeOutAnimation);
        tvGame.startAnimation(fadeOutAnimation);
        ivJedi.startAnimation(fadeOutAnimation);
        btnHere.startAnimation(fadeOutAnimation);

        btnHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),GamingActivity.class);
                startActivity(intent);
            }
        });


    }
}
