package fr.wildcodeschool.ecowild;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Crock on 09/04/2018.
 */

public class ElemntModel implements Parcelable {

    String adress;
    String type;
    String id;

    public ElemntModel(String adress, String type, String id) {
        this.adress = adress;
        this.type = type;
        this.id = id;
    }

    protected ElemntModel(Parcel in) {
        adress = in.readString();
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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
        dest.writeString(adress);
        dest.writeString(type);
        dest.writeString(id);
    }
}
