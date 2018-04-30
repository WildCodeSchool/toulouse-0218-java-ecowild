package fr.wildcodeschool.ecowild;

import android.os.Parcel;
import android.os.Parcelable;


public class ElementModel implements Parcelable {

    public static final Creator<ElementModel> CREATOR = new Creator<ElementModel>() {
        @Override
        public ElementModel createFromParcel(Parcel in) {
            return new ElementModel(in);
        }

        @Override
        public ElementModel[] newArray(int size) {
            return new ElementModel[size];
        }
    };
    String address;
    String type;
    String id;

    public ElementModel(String address, String type, String id) {
        this.address = address;
        this.type = type;
        this.id = id;
    }

    protected ElementModel(Parcel in) {
        address = in.readString();
        type = in.readString();
        id = in.readString();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(type);
        dest.writeString(id);
    }
}
