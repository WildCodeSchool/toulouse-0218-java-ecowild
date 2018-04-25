package fr.wildcodeschool.ecowild;

import android.graphics.Bitmap;

/**
 * Created by wilder on 23/04/18.
 */

public class UserModel {
    String name;
    String password;
    String avatar;
    int xp;
    int level;




    public UserModel(){}

    public UserModel(String name, String password, String avatar, int xp, int level) {
        this.name = name;
        this.password = password;
        this.avatar = avatar;
        this.xp = xp;
        this.level =level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel(){return level;}

    public void setLevel(){this.level=level;}

}


