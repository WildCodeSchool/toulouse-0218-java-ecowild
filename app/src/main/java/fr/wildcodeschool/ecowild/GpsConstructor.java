package fr.wildcodeschool.ecowild;

public class GpsConstructor {

    double valueAbs;
    double valueOrdo;
    String adress;

    public GpsConstructor(double valueAbs, double valueOrdo, String adress) {
        this.valueAbs = valueAbs;
        this.valueOrdo = valueOrdo;
        this.adress = adress;
    }

    public double getValueAbs() {
        return valueAbs;
    }

    public void setValueAbs(double valueAbs) {
        this.valueAbs = valueAbs;
    }

    public double getValueOrdo() {
        return valueOrdo;
    }

    public void setValueOrdo(double valueOrdo) {
        this.valueOrdo = valueOrdo;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
