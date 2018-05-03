package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;


public class PageQuatreInformation extends Fragment {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.activity_page_quatre_information, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //TODO changer anim
        ImageView ivJedi = getView().findViewById(R.id.iv_jedi);
        final ImageView ivBulle = getView().findViewById(R.id.iv_bulle);
        final TextView tvGame = getView().findViewById(R.id.tv_go);
        final Button btnHere = getView().findViewById(R.id.btn_here);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivBulle.setVisibility(View.VISIBLE);
                tvGame.setVisibility(View.VISIBLE);
                btnHere.setVisibility(View.VISIBLE);

            }
        }, SPLASH_TIME_OUT);
        Animation zoomAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);
        ivJedi.startAnimation(zoomAnimation);

        btnHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectionActivity.CONNECTED) {
                    Intent intent = new Intent(getContext(), GamingActivity.class);
                    startActivity(intent);
                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) getView().findViewById(R.id.custom_toast_container));
                    TextView textToast = (TextView) layout.findViewById(R.id.text);
                    textToast.setText(R.string.coonected);
                    final Toast toast = new Toast(getContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), ConnectionActivity.class);
                            startActivity(intent);
                        }
                    }, SPLASH_TIME_OUT);

                }
            }
        });


    }
}
