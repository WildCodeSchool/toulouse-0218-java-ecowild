package fr.wildcodeschool.ecowild;


public class GpsConstructor {

    double valueAbs;
    double valueOrdo;
    String address;

    public GpsConstructor(double valueAbs, double valueOrdo, String address) {
        this.valueAbs = valueAbs;
        this.valueOrdo = valueOrdo;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
