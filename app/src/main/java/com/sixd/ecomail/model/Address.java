package com.sixd.ecomail.model;

/**
 * Created by Rahul Gupta on 16-04-2018.
 */

public class Address {

    String isPrimary="";
    String adressStr="";

    public Address(String isPrimary, String adressStr, boolean isChecked) {
        this.isPrimary = isPrimary;
        this.adressStr = adressStr;
        this.isChecked = isChecked;
    }

    public String getIsPrimary() {
        return isPrimary;

    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getAdressStr() {
        return adressStr;
    }

    public void setAdressStr(String adressStr) {
        this.adressStr = adressStr;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked=false;
}
