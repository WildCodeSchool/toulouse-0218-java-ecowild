package fr.wildcodeschool.ecowild;


public class UserSingleton {
    public static UserSingleton sInstance = null;
    public String mNameSaved = "";
    public String mAvatarSaved = "";
    public String mPasswordSaved = "";
    public String mRankSaved = "";
    public int mXp = 0;
    public int mLevel = 1;

    public UserSingleton() {
    }

    public static UserSingleton getInstance() {
        if (sInstance == null) {
            sInstance = new UserSingleton();
        }
        return sInstance;

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
