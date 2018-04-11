package fr.wildcodeschool.ecowild;

import android.os.Parcel;
import android.os.Parcelable;

public class ElemntModel implements Parcelable {

    String address;
    String type;
    String id;

    public ElemntModel(String address, String type, String id) {
        this.address = address;
        this.type = type;
        this.id = id;
    }

    protected ElemntModel(Parcel in) {
        address = in.readString();
        type = in.readString();
        id = in.readString();
    }

    public static final Creator<ElemntModel> CREATOR = new Creator<ElemntModel>() {
        @Override
        public ElemntModel createFromParcel(Parcel in) {
            return new ElemntModel(in);
        }

        @Override
        public ElemntModel[] newArray(int size) {
            return new ElemntModel[size];
        }
    };

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
