package fr.wildcodeschool.ecowild;

/**
 * Created by wilder on 25/04/18.
 */

public class UserSingleton {
    public static UserSingleton sInstance = null;
    public String mNameSaved ="";
    public String mAvatarSaved ="";
    public String mPasswordSaved ="";
    public int mXp=0;
    public int mLevel=1;

    public UserSingleton(){}

    public static UserSingleton getInstance(){
        if(sInstance == null){
            sInstance = new UserSingleton();
        }
        return  sInstance;

    }

    public void setTextName(String textName ){
        mNameSaved= textName;
    }
    public void setTextAvatar (String textAvatar){
        mAvatarSaved = textAvatar;
    }

    public void setTextPassword(String textPassword){
        mPasswordSaved = textPassword;
    }
    public void setIntXp(int intXp){
        mXp = intXp;
    }

    public void setIntLevel(int intLevel){
        mLevel =intLevel;
    }

    public String  getTextName(){
        return  mNameSaved;
    }

    public String getTextAvatar(){
        return mAvatarSaved;
    }
    public String getTextPassword(){
        return mPasswordSaved;
    }

    public int getIntLevel (){
        return mLevel;
    }

    public int getIntXp(){
        return mXp;
    }
}
