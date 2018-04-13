package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ElementModel> {

    public ListAdapter(Context context, ArrayList<ElementModel> enemyGrid) {
        super(context, 0, enemyGrid);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ElementModel gpsLocations = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_model, parent, false);
        }

        TextView address = convertView.findViewById(R.id.address);
        //TextView type = convertView.findViewById(R.id.type);
       // TextView id = convertView.findViewById(R.id.smallid);
        LinearLayout list = convertView.findViewById(R.id.item_list);

        address.setText(gpsLocations.getAddress());
       // type.setText(gpsLocations.getType());
       // id.setText(gpsLocations.getId());

        return convertView;
    }
}
