package fr.wildcodeschool.ecowild;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class MyItem implements ClusterItem, Parcelable {

    /**
     * Bitmap et position/LatLng INTERDIT
     *
     * Il faut sortir les elements et envoyer des doubles
     *
     **/

    String adress;
    String type;
    boolean filtre;
    double lat;
    double lng;
  
    public MyItem(double lat, double lng, String adress, String type, boolean filtre) {
        this.lng = lng;
        this.lat= lat;
        this.adress = adress;
        this.type = type;
        this.filtre = filtre;
    }

    protected MyItem(Parcel in) {
        adress = in.readString();
        type = in.readString();
        filtre = in.readByte() != 0;
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public void setFiltre(boolean filtre) {
        this.filtre = filtre;
    }

    public static final Creator<MyItem> CREATOR = new Creator<MyItem>() {
        @Override
        public MyItem createFromParcel(Parcel in) {
            return new MyItem(in);
        }

        @Override
        public MyItem[] newArray(int size) {
            return new MyItem[size];
        }
    };

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    public String getType() {
        return type;
    }

    public Boolean getFiltre() {
        return filtre;
    }

    public String getAdress() {
        return adress;
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
        parcel.writeString(adress);
        parcel.writeString(type);
        parcel.writeByte((byte) (filtre ? 1 : 0));
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);

    }
}