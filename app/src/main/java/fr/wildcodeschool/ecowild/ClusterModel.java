package fr.wildcodeschool.ecowild;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;


public class ClusterModel implements ClusterItem, Parcelable {

    public static final Creator<ClusterModel> CREATOR = new Creator<ClusterModel>() {
        @Override
        public ClusterModel createFromParcel(Parcel in) {
            return new ClusterModel(in);
        }

        @Override
        public ClusterModel[] newArray(int size) {
            return new ClusterModel[size];
        }
    };
    /**
     * Bitmap et position/LatLng INTERDIT
     * <p>
     * Il faut sortir les elements et envoyer des doubles
     **/

    String address;
    String type;
    double lat;
    double lng;

    public ClusterModel(double lat, double lng, String address, String type) {
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.type = type;
    }

    protected ClusterModel(Parcel in) {
        address = in.readString();
        type = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(type);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);

    }

    public ClusterModel clone() {
        ClusterModel clone = new ClusterModel(lat, lng, address, type);
        return clone;
    }
}