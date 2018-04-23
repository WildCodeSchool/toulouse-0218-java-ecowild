package fr.wildcodeschool.ecowild;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class CusterModel implements ClusterItem, Parcelable {

    public static final Creator<CusterModel> CREATOR = new Creator<CusterModel>() {
        @Override
        public CusterModel createFromParcel(Parcel in) {
            return new CusterModel(in);
        }

        @Override
        public CusterModel[] newArray(int size) {
            return new CusterModel[size];
        }
    };
    /**
     * Bitmap et position/LatLng INTERDIT
     * <p>
     * Il faut sortir les elements et envoyer des doubles
     **/

    String address;
    String type;
    boolean filter;
    double lat;
    double lng;

    public CusterModel(double lat, double lng, String address, String type, boolean filter) {
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.type = type;
        this.filter = filter;
    }

    protected CusterModel(Parcel in) {
        address = in.readString();
        type = in.readString();
        filter = in.readByte() != 0;
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

    public Boolean getFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
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
        parcel.writeByte((byte) (filter ? 1 : 0));
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);

    }
}