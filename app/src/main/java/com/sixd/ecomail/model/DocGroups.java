package com.sixd.ecomail.model;

/**
 * Created by Rahul Gupta on 23-04-2018.
 */

public class DocGroups {
    String docGP="";
    boolean selected=false;

    public DocGroups(String docGP, boolean selected) {
        this.docGP = docGP;
        this.selected = selected;
    }

    public String getDocGP() {
        return docGP;

    }

    public void setDocGP(String docGP) {
        this.docGP = docGP;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
