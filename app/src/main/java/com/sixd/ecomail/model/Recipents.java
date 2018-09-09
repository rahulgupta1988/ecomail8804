package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 02-05-2018.
 */

@Entity
public class Recipents {

    @Id
    long ID;
    String createDate="";

    public Recipents(){}
    public Recipents(String createDate, String image, String consumerId, String networkLoginId, String recipientName, String recipientType) {
        this.createDate = createDate;
        this.image = image;
        this.consumerId = consumerId;
        this.networkLoginId = networkLoginId;
        this.recipientName = recipientName;
        this.recipientType = recipientType;
    }

    String image="";

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getNetworkLoginId() {
        return networkLoginId;
    }

    public void setNetworkLoginId(String networkLoginId) {
        this.networkLoginId = networkLoginId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    String consumerId="";
    String networkLoginId="";
    String recipientName="";
    String recipientType="";
    boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
