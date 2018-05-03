package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.HashMap;

public class UserSingleton {
    public static UserSingleton sInstance = null;
    public String mNameSaved = "";
    public String mAvatarSaved = "";
    public String mPasswordSaved = "";
    public String mRankSaved = "";
    public int mXp = 0;
    public int mLevel = 1;
    private HashMap<Integer, BitmapDescriptor> bitmaps = new HashMap<>();

    public UserSingleton() {
    }

    public void removeInstance() {
        sInstance = null;
    }

    public static UserSingleton getInstance() {
        if (sInstance == null) {
            sInstance = new UserSingleton();
        }
        return sInstance;

    }

    public BitmapDescriptor getBitmapFromDrawable(Context context, int drawable, int widthPx, int heightPx) {

        if (!bitmaps.containsKey(drawable)) {
            Resources ressource = context.getResources();
            int widthDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthPx, ressource.getDisplayMetrics());
            int heightDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightPx, ressource.getDisplayMetrics());

            BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(drawable);
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), widthDp, heightDp, false);

            bitmaps.put(drawable, BitmapDescriptorFactory.fromBitmap(bitmap));
        }
        return bitmaps.get(drawable);
    }

    public String getTextName() {
        return mNameSaved;
    }

    public void setTextName(String textName) {
        mNameSaved = textName;
    }

    public String getTextAvatar() {
        return mAvatarSaved;
    }

    public void setTextAvatar(String textAvatar) {
        mAvatarSaved = textAvatar;
    }

    public String getTextRank() {
        return mRankSaved;
    }

    public void setTextRank(String textRank) {
        mRankSaved = textRank;
    }

    public String getTextPassword() {
        return mPasswordSaved;
    }

    public void setTextPassword(String textPassword) {
        mPasswordSaved = textPassword;
    }

    public int getIntLevel() {
        return mLevel;
    }

    public void setIntLevel(int intLevel) {
        mLevel = intLevel;
    }

    public int getIntXp() {
        return mXp;
    }

    public void setIntXp(int intXp) {
        mXp = intXp;
    }
}
