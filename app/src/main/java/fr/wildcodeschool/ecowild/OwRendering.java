package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.GoogleMap;
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

        Bitmap bitmap;

        Resources ressource = mContext.getResources();
        int valuePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, ressource.getDisplayMetrics());

        // create bitpma ici au lieu de le generer à chaque fois.
        if (item.getType().equals("Verre")) {
            //Bitmap config pour la taille du marqueur
            BitmapDrawable bitmapDrawableGlass = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.pointeur_verre);
            Bitmap glass = bitmapDrawableGlass.getBitmap();
            bitmap = Bitmap.createScaledBitmap(glass, (valuePx - 15) , valuePx, false);
        } else {
            //Bitmap config pour la taille du marqueur
            BitmapDrawable bitmapDrawablePlastic = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.pointeur_papier);
            Bitmap plastic = bitmapDrawablePlastic.getBitmap();
            bitmap = Bitmap.createScaledBitmap(plastic, (valuePx - 15), valuePx, false);
        }


        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

        super.onBeforeClusterItemRendered(item, markerOptions);

    }

}



