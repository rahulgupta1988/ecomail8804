package com.sixd.ecomail.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 04-05-2018.
 */
@Entity
public class Document_Actions_Payment implements Parcelable{
    @Id(assignable = true)
    long id;

    String actionDescription="";
    String minimumAmountDue="";
    String actionPriority="";
    String autoPaid="";
    String actionDueDate="";
    String amountDue="";
    String actionId="";
    String message="";
    String link="";
    String expirationDate="";

    public Document_Actions_Payment() {
    }

    public Document_Actions_Payment(String actionDescription, String minimumAmountDue, String actionPriority, String autoPaid, String actionDueDate, String amountDue, String actionId, String message, String link, String expirationDate) {
        this.actionDescription = actionDescription;
        this.minimumAmountDue = minimumAmountDue;
        this.actionPriority = actionPriority;
        this.autoPaid = autoPaid;
        this.actionDueDate = actionDueDate;
        this.amountDue = amountDue;
        this.actionId = actionId;
        this.message = message;
        this.link = link;
        this.expirationDate = expirationDate;
    }

    protected Document_Actions_Payment(Parcel in) {
        id = in.readLong();
        actionDescription = in.readString();
        minimumAmountDue = in.readString();
        actionPriority = in.readString();
        autoPaid = in.readString();
        actionDueDate = in.readString();
        amountDue = in.readString();
        actionId = in.readString();
        message = in.readString();
        link = in.readString();
        expirationDate = in.readString();
    }

    public static final Creator<Document_Actions_Payment> CREATOR = new Creator<Document_Actions_Payment>() {
        @Override
        public Document_Actions_Payment createFromParcel(Parcel in) {
            return new Document_Actions_Payment(in);
        }

        @Override
        public Document_Actions_Payment[] newArray(int size) {
            return new Document_Actions_Payment[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getMinimumAmountDue() {
        return minimumAmountDue;
    }

    public void setMinimumAmountDue(String minimumAmountDue) {
        this.minimumAmountDue = minimumAmountDue;
    }

    public String getActionPriority() {
        return actionPriority;
    }

    public void setActionPriority(String actionPriority) {
        this.actionPriority = actionPriority;
    }

    public String getAutoPaid() {
        return autoPaid;
    }

    public void setAutoPaid(String autoPaid) {
        this.autoPaid = autoPaid;
    }

    public String getActionDueDate() {
        return actionDueDate;
    }

    public void setActionDueDate(String actionDueDate) {
        this.actionDueDate = actionDueDate;
    }

    public String getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
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
        parcel.writeString(minimumAmountDue);
        parcel.writeString(actionPriority);
        parcel.writeString(autoPaid);
        parcel.writeString(actionDueDate);
        parcel.writeString(amountDue);
        parcel.writeString(actionId);
        parcel.writeString(message);
        parcel.writeString(link);
        parcel.writeString(expirationDate);
    }
}
