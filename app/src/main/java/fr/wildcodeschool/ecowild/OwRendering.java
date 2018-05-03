package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by wilder on 19/04/18.
 **/

public class OwRendering extends DefaultClusterRenderer<ClusterModel> {

    Context mContext;

    /**
     * Avec la precieuse aide de Bastien
     **/

    public OwRendering(Context context, GoogleMap map, ClusterManager<ClusterModel> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    protected void onBeforeClusterItemRendered(ClusterModel item, MarkerOptions markerOptions) {
        markerOptions.snippet(item.getType());
        markerOptions.title(item.getAddress());

        BitmapDescriptor bitmapDescriptor;
        UserSingleton singleton = UserSingleton.getInstance();
        // create bitpma ici au lieu de le generer à chaque fois.
        if (item.getType().equals("Verre")) {
            bitmapDescriptor = singleton.getBitmapFromDrawable(mContext, R.drawable.pointeur_verre, 38);
        } else {
            bitmapDescriptor = singleton.getBitmapFromDrawable(mContext, R.drawable.pointeur_papier, 38);
        }

        markerOptions.icon(bitmapDescriptor);


        super.onBeforeClusterItemRendered(item, markerOptions);

    }


}



