package com.sixd.ecomail.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 04-05-2018.
 */
@Entity
public class Document_Actions_Dispute implements Parcelable{
    @Id(assignable = true)
    long id;

    String actionDescription="";
    String actionPriority="";
    String actionDueDate="";
    String actionId="";
    String message="";
    String link="";
    String expirationDate="";

    public Document_Actions_Dispute() {
    }

    public Document_Actions_Dispute(String actionDescription, String actionPriority, String actionDueDate, String actionId, String message, String link, String expirationDate) {
        this.actionDescription = actionDescription;
        this.actionPriority = actionPriority;
        this.actionDueDate = actionDueDate;
        this.actionId = actionId;
        this.message = message;
        this.link = link;
        this.expirationDate = expirationDate;
    }

    protected Document_Actions_Dispute(Parcel in) {
        id = in.readLong();
        actionDescription = in.readString();
        actionPriority = in.readString();
        actionDueDate = in.readString();
        actionId = in.readString();
        message = in.readString();
        link = in.readString();
        expirationDate = in.readString();
    }

    public static final Creator<Document_Actions_Dispute> CREATOR = new Creator<Document_Actions_Dispute>() {
        @Override
        public Document_Actions_Dispute createFromParcel(Parcel in) {
            return new Document_Actions_Dispute(in);
        }

        @Override
        public Document_Actions_Dispute[] newArray(int size) {
            return new Document_Actions_Dispute[size];
        }
    };

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getActionPriority() {
        return actionPriority;
    }

    public void setActionPriority(String actionPriority) {
        this.actionPriority = actionPriority;
    }

    public String getActionDueDate() {
        return actionDueDate;
    }

    public void setActionDueDate(String actionDueDate) {
        this.actionDueDate = actionDueDate;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(actionDescription);
        parcel.writeString(actionPriority);
        parcel.writeString(actionDueDate);
        parcel.writeString(actionId);
        parcel.writeString(message);
        parcel.writeString(link);
        parcel.writeString(expirationDate);
    }
}
