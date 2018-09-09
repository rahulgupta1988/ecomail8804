package com.sixd.ecomail.model;

import java.util.ArrayList;

/**
 * Created by Rahul Gupta on 17-04-2018.
 */

public class CardSectionModel {

    private String sectionLabel;
    private ArrayList<Card> cardList;

    public CardSectionModel(String sectionLabel, ArrayList<Card> cardList) {
        this.sectionLabel = sectionLabel;
        this.cardList = cardList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    public void setCardList(ArrayList<Card> cardList) {
        this.cardList = cardList;
    }
}
