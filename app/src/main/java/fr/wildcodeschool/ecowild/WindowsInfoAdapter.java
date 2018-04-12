package fr.wildcodeschool.ecowild;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class WindowsInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public WindowsInfoAdapter(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {


        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.windows_info_layout, null);

        TextView adress = view.findViewById(R.id.pointAddress);
        TextView deco = view.findViewById(R.id.decorationText);

        adress.setText(marker.getTitle());
        deco.setText(marker.getSnippet());

        return view;
    }
}