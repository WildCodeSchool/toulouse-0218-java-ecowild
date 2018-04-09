package fr.wildcodeschool.ecowild;

/**
 * Created by wilder on 09/04/18.
 */

public class GpsConstructor {

    double ValueAbs;
    double ValueOrdo;
    String adress;

    public GpsConstructor(double valueAbs, double valueOrdo, String adress) {
        ValueAbs = valueAbs;
        ValueOrdo = valueOrdo;
        this.adress = adress;
    }

    public double getValueAbs() {
        return ValueAbs;
    }

    public void setValueAbs(double valueAbs) {
        ValueAbs = valueAbs;
    }

    public double getValueOrdo() {
        return ValueOrdo;
    }

    public void setValueOrdo(double valueOrdo) {
        ValueOrdo = valueOrdo;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
