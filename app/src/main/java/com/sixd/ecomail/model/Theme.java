package com.sixd.ecomail.model;

/**
 * Created by Rahul Gupta on 27-Mar-18.
 */

public class Theme {

    String themeName="";
    String itemBGColor="";

    public String getItemBGColor() {
        return itemBGColor;
    }

    public void setItemBGColor(String itemBGColor) {
        this.itemBGColor = itemBGColor;
    }

    public Theme(String themeName,String itemBGColor){

        this.themeName = themeName;
        this.itemBGColor=itemBGColor;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    @Override
    public String toString() {

        return themeName;
    }
}
