package com.sixd.ecomail.model;

/**
 * Created by Rahul Gupta on 10-04-2018.
 */

public class InboxSort {

    String sortName="";
    int sortID;
    boolean isChecked=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public InboxSort(String sortName, int sortID, boolean isChecked) {
        this.sortName = sortName;
        this.sortID = sortID;

        this.isChecked = isChecked;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public int getSortID() {
        return sortID;
    }

    public void setSortID(int sortID) {
        this.sortID = sortID;
    }
}
