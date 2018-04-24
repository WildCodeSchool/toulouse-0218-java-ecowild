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
    String favorites;



    public UserModel(){}

    public UserModel(String name, String password, String avatar, int xp, String favorites) {
        this.name = name;
        this.password = password;
        this.avatar = avatar;
        this.xp = xp;
        this.favorites = favorites;
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

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }
}


