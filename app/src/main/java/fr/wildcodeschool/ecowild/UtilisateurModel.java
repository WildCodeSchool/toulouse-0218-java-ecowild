package fr.wildcodeschool.ecowild;

import android.graphics.Bitmap;

/**
 * Created by wilder on 23/04/18.
 */

public class UtilisateurModel {
    String name;
    String password;
    Bitmap avatar;
    int xp;
    String favoris;


    public UtilisateurModel(){}

    public UtilisateurModel(String name, String password, int xp, String favoris) {
        this.name = name;
        this.password = password;
        this.avatar = avatar;
        this.xp = xp;
        this.favoris = favoris;
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

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getFavoris() {
        return favoris;
    }

    public void setFavoris(String favoris) {
        this.favoris = favoris;
    }
}


