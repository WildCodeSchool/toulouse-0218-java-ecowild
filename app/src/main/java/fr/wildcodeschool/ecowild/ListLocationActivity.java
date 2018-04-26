package fr.wildcodeschool.ecowild;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

public class ListLocationActivity extends AppCompatActivity {

    boolean mGlassFilter = true;
    boolean mPaperfilter = true;
    ArrayList<ClusterModel> mlistData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listlocationactivity);

        ListView locationList = findViewById(R.id.location_list);

        Switch goMap = findViewById(R.id.go_Map);

        mlistData = ListLocationActivity.this.getIntent().getExtras().getParcelableArrayList("GPS_POSITIONS");

        final ListAdapter adapter = new ListAdapter(ListLocationActivity.this, mlistData);
        locationList.setAdapter(adapter);

        goMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent goToMap = new Intent(ListLocationActivity.this, MapsActivity.class);
                startActivity(goToMap);
            }
        });

        /** Partie menu Circle**/

        //Image bouton Menu
        ImageView iconMenu = new ImageView(this); // Create an icon
        iconMenu.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.entonnoir));

        //creation bouton Menu
        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(iconMenu)
                .build();

        SubActionButton.Builder listeBuilder = new SubActionButton.Builder(this);

        //Creation image sous menu
        final ImageView glassFilterImg = new ImageView(this); // Create an icon
        glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verre));
        //envoit sous bouton au menu
        final SubActionButton sabGlass = listeBuilder.setContentView(glassFilterImg).build();

        final ImageView paperFilterGlass = new ImageView(this); // Create an icon
        paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papier));
        SubActionButton sabPaper = listeBuilder.setContentView(paperFilterGlass).build();

        DrawerLayout.LayoutParams layoutParam = new DrawerLayout.LayoutParams(200, 200);
        sabPaper.setLayoutParams(layoutParam);
        sabGlass.setLayoutParams(layoutParam);

        //Creation bouton sous menu
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(ListLocationActivity.this)
                .addSubActionView(sabGlass)
                .addSubActionView(sabPaper)
                .attachTo(actionButton)
                .build();


        sabPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPaperfilter) {
                    mPaperfilter = false;
                    if ((!mPaperfilter) & (!mGlassFilter)) {
                        glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verre));
                        paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papiersansfond));
                        adapter.filterList("Verre");
                        mGlassFilter= true;
                    } else {
                        paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papiersansfond));
                        adapter.filterList("Verre");
                    }
                } else {
                    mPaperfilter = true;
                    paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papier));
                    adapter.filterList(null);
                }
            }
        });
        sabGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGlassFilter) {
                    mGlassFilter = false;
                    if ((!mPaperfilter) & (!mGlassFilter)) {
                        paperFilterGlass.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.papier));
                        adapter.filterList("Papier/Plastique");
                        glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verresansfond));
                        mPaperfilter = true;
                    } else {
                        glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verresansfond));
                        adapter.filterList("Papier/Plastique");
                    }
                } else {
                    mGlassFilter = true;
                    adapter.filterList(null);
                    glassFilterImg.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.verre));
                }
            }
        });
    }
}

