package fr.wildcodeschool.ecowild;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    public static boolean INVISIBLE = true;
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
        if(INVISIBLE) {
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
        }
        if(!INVISIBLE){

        }

        RadioButton rbAvatar = findViewById(R.id.radioButton_avatar);
        RadioButton rbName = findViewById(R.id.radioButton_name);
        final RadioButton rbPassword =findViewById(R.id.radioButton_password);

        RadioGroup rg =findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbPassword.isChecked()){
                    visibleEt(etPassword);
                    visibleEt(etNewPassword);
                    visibleEt(etNewPassword2);
                    visibleBte(buttonMdp);
                    visibleIv(ivPassword);
                    visibleIv(ivNewPassword);
                    visibleIv(ivNewPassword2);
                    visibleIv(ivkey);
                }
            }
        });




        rbName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        CheckBox cbName = findViewById(R.id.checkBox_name);
        cbName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                visibleBte(buttonMnc);
                visibleEt(etProfil);
                visibleEt(etNewProfil);
                visibleEt(etNewProfil2);
                INVISIBLE= false;
            }
        });




    }
    public void visibleEt(EditText et){
        et.setVisibility(View.VISIBLE);

    }
    public void visibleBte (Button bte){
        bte.setVisibility(View.VISIBLE);
    }
    public void visibleIv (ImageView iv){
        iv.setVisibility(View.VISIBLE);
    }

}
