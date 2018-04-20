package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by wilder on 19/04/18.
 **/

public class OwRenderingEnd extends DefaultClusterRenderer<MyItem> {

    Context mContext;

    /** Avec la precieuse aide de Bastien **/

    public OwRenderingEnd(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {

            markerOptions.visible(false);

        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}



