package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

public class ListLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listlocationactivity);

        ListView locationList = findViewById(R.id.location_list);

        Switch goMap = findViewById(R.id.go_Map);

        ArrayList listData = ListLocationActivity.this.getIntent().getExtras().getParcelableArrayList("GPS_POSITIONS");

        final ListAdapter adapter = new ListAdapter(ListLocationActivity.this, listData);
        locationList.setAdapter(adapter);

        goMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent goToMap = new Intent(ListLocationActivity.this, MapsActivity.class);
                startActivity(goToMap);
            }
        });

        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CusterModel location = adapter.getItem(i);
                String address = location.getAddress();

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + address + "&mode=b"));
                startActivity(intent);
            }
        });
    }
}

