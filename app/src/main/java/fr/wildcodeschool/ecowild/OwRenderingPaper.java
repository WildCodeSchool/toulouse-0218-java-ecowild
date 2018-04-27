package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by wilder on 19/04/18.
 **/

public class OwRenderingPaper extends DefaultClusterRenderer<ClusterModel> {

    Context mContext;

    /**
     * Avec la precieuse aide de Bastien
     **/

    public OwRenderingPaper(Context context, GoogleMap map, ClusterManager<ClusterModel> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    protected void onBeforeClusterItemRendered(ClusterModel item, MarkerOptions markerOptions) {

        Bitmap bitmap2;

        // Filtre où le marqueur est créé si du bon type, sinon rien.
        if (item.getType().equals("Verre")) {
            markerOptions.snippet(item.getType());
            markerOptions.title(item.getAddress());

            //Bitmap retaille
            Resources ressource = mContext.getResources();
            int valuePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, ressource.getDisplayMetrics());

            //on recup le bitmap
            BitmapDrawable bitmapDrawableGlass = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.pointeur_verre);
            Bitmap glass = bitmapDrawableGlass.getBitmap();
            bitmap2 = Bitmap.createScaledBitmap(glass, valuePx, valuePx, false);

            //Mise en place du marqueur.
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap2));
        }

        if (item.getType().equals("Papier/Plastique")) {
            markerOptions.visible(false);
        }

        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}



