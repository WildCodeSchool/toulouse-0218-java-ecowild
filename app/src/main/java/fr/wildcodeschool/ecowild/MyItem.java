package fr.wildcodeschool.ecowild;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private boolean visible;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}

