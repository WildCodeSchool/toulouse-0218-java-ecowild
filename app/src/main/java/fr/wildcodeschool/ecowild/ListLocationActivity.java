package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class ListLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listlocationactivity);

        Switch goMap = findViewById(R.id.goMap);

        goMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent goToMap = new Intent(ListLocationActivity.this, MapsActivity.class);
                startActivity(goToMap);
            }
        });
    }
}
