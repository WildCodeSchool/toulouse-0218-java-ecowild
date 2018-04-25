package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ClusterModel> {

    private ArrayList<ClusterModel> originalData = null;
    private ArrayList<ClusterModel> filteredData = null;

    public ListAdapter(Context context, ArrayList<ClusterModel> items) {
        super(context, 0, items);

        originalData = items;

        filteredData = new ArrayList<ClusterModel>(originalData.size());
        for (ClusterModel item : originalData) filteredData.add(item.clone());
    }

    public void filterList(String filter) {

        filteredData.clear();

        if (filter == null) {
            filteredData = new ArrayList<ClusterModel>(originalData.size());
            for (ClusterModel item : originalData) filteredData.add(item.clone());
        } else {
            for (int i = 0; i < originalData.size(); i++) {
                ClusterModel filteredCluster = originalData.get(i);
                if (filteredCluster.getType().equals(filter)) {
                    filteredData.add(filteredCluster.clone());
                }
            }
        }

        notifyDataSetChanged();
    }

    public int getCount() {
        return filteredData.size();
    }

    public ClusterModel getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClusterModel gpsMarker = getItem(position);

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

        return convertView;
    }
}
