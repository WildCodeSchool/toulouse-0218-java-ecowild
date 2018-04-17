package fr.wildcodeschool.ecowild;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_HIDDEN;
import static fr.wildcodeschool.ecowild.ConnectionActivity.PASSWORD_VISIBLE;

public class Settings extends AppCompatActivity {
    int mPasswordVisibility = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText etPassword = findViewById(R.id.edit_text_password);
        final EditText etNewPassword = findViewById(R.id.edit_text_new_password);
        final EditText etNewPassword2 = findViewById(R.id.edit_text_new_assword_confirm);
        final EditText etProfil = findViewById(R.id.edit_text_profil);
        final EditText etNewProfil = findViewById(R.id.edit_text_new_profil);
        final EditText etNewProfil2 = findViewById(R.id.edit_text_new_profil_confirm);
        final Button buttonMdp = findViewById(R.id.button_mdp);
        final Button buttonMnc = findViewById(R.id.button_mnc);
        final ImageView ivPassword = findViewById(R.id.image_view_past_password);
        final ImageView ivNewPassword = findViewById(R.id.image_view_new_password);
        final ImageView ivNewPassword2 = findViewById(R.id.image_view_validated_password2);
        final ImageView ivkey = findViewById(R.id.image_view_key);

        //test image ronde
        final ImageView ivAvatar =findViewById(R.id.imageView_avatar);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.jeter);
        RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        ivAvatar.setImageDrawable(roundedBitmapDrawable);

        final RadioButton rbAvatar = findViewById(R.id.radioButton_avatar);
        final RadioButton rbName = findViewById(R.id.radioButton_name);
        final RadioButton rbPassword =findViewById(R.id.radioButton_password);
        RadioGroup rG =findViewById(R.id.radio_group);

        ivPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        ivNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    etNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        ivNewPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPasswordVisibility == PASSWORD_HIDDEN) {
                    etNewPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswordVisibility = PASSWORD_VISIBLE;
                } else {
                    etNewPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordVisibility = PASSWORD_HIDDEN;
                }
            }
        });

        rG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                etPassword.setVisibility(View.GONE);
                etNewPassword.setVisibility(View.GONE);
                etNewPassword2.setVisibility(View.GONE);
                etProfil.setVisibility(View.GONE);
                etNewProfil.setVisibility(View.GONE);
                etNewProfil2.setVisibility(View.GONE);
                buttonMdp.setVisibility(View.GONE);
                buttonMnc.setVisibility(View.GONE);
                ivPassword.setVisibility(View.GONE);
                ivNewPassword.setVisibility(View.GONE);
                ivNewPassword2.setVisibility(View.GONE);
                ivkey.setVisibility(View.GONE);
                ivAvatar.setVisibility(View.GONE);

                if (rbPassword.isChecked()){
                    visibleEt(etPassword,etNewPassword,etNewPassword2);
                    buttonMdp.setVisibility(View.VISIBLE);
                    ivPassword.setVisibility(View.VISIBLE);
                    ivNewPassword.setVisibility(View.VISIBLE);
                    ivNewPassword2.setVisibility(View.VISIBLE);
                    ivkey.setVisibility(View.VISIBLE);

                }

                if(rbName.isChecked()){
                   buttonMnc.setVisibility(View.VISIBLE);
                    visibleEt(etProfil, etNewProfil,etNewProfil2);

                }

                if(rbAvatar.isChecked()){
                    ivAvatar.setVisibility(View.VISIBLE);
                }


            }
        });






    }
    public void visibleEt(EditText et1, EditText et2, EditText et3){
        et1.setVisibility(View.VISIBLE);
        et2.setVisibility(View.VISIBLE);
        et3.setVisibility(View.VISIBLE);

    }

}
