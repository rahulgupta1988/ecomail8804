package com.sixd.ecomail.model;

/**
 * Created by Rahul Gupta on 16-04-2018.
 */

public class State {

    String name="";
    String abbreviation="";
    String isFirst="";

    public State(String name, String abbreviation, String isFirst, String alph) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.isFirst = isFirst;
        this.alph = alph;
    }



    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAlph() {
        return alph;
    }

    public void setAlph(String alph) {
        this.alph = alph;
    }

    String alph="";
}
