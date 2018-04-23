package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<CusterModel> {

    public ListAdapter(Context context, ArrayList<CusterModel> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CusterModel gpsMarker = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_model, parent, false);
        }

        TextView address = convertView.findViewById(R.id.address);
        LinearLayout list = convertView.findViewById(R.id.item_list);

        address.setText(gpsMarker.getAddress());

        ImageView ivType = convertView.findViewById(R.id.iv_type);
        if (gpsMarker.getType().equals("Verre")) {
            ivType.setBackgroundResource(R.drawable.verre);
            list.setBackgroundColor(Color.parseColor("#bce7ca"));
        } else {
            ivType.setBackgroundResource(R.drawable.papier);
            list.setBackgroundColor(Color.parseColor("#c7e0f6"));
        }

        final ImageView itinerary = convertView.findViewById(R.id.iv_itineraire);
        itinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itinerary.setBackgroundResource(R.drawable.papier);
                itinerary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itinerary.setBackgroundResource(R.drawable.itineraire);
                    }
                });

            }
        });

        return convertView;
    }
}
