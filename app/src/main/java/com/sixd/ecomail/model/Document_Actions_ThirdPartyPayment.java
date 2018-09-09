package com.sixd.ecomail.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 04-05-2018.
 */
@Entity
public class Document_Actions_ThirdPartyPayment implements Parcelable{
     @Id(assignable = true)
    long id;

    String actionDescription="";
    String beneficiaryAccount="";
    String beneficiaryName="";
    String minimumAmountDue="";
    String actionPriority="";
    String routingNo="";
    String actionDueDate="";
    String actionId="";
    String amountDue="";
    String referenceInfo="";
    String message="";
    String link="";
    String expirationDate="";


    public Document_Actions_ThirdPartyPayment() {
    }

    public Document_Actions_ThirdPartyPayment(String actionDescription, String beneficiaryAccount, String beneficiaryName, String minimumAmountDue, String actionPriority, String routingNo, String actionDueDate, String actionId, String amountDue, String referenceInfo, String message, String link, String expirationDate) {
        this.actionDescription = actionDescription;
        this.beneficiaryAccount = beneficiaryAccount;
        this.beneficiaryName = beneficiaryName;
        this.minimumAmountDue = minimumAmountDue;
        this.actionPriority = actionPriority;
        this.routingNo = routingNo;
        this.actionDueDate = actionDueDate;
        this.actionId = actionId;
        this.amountDue = amountDue;
        this.referenceInfo = referenceInfo;
        this.message = message;
        this.link = link;
        this.expirationDate = expirationDate;
    }

    protected Document_Actions_ThirdPartyPayment(Parcel in) {
        id = in.readLong();
        actionDescription = in.readString();
        beneficiaryAccount = in.readString();
        beneficiaryName = in.readString();
        minimumAmountDue = in.readString();
        actionPriority = in.readString();
        routingNo = in.readString();
        actionDueDate = in.readString();
        actionId = in.readString();
        amountDue = in.readString();
        referenceInfo = in.readString();
        message = in.readString();
        link = in.readString();
        expirationDate = in.readString();
    }

    public static final Creator<Document_Actions_ThirdPartyPayment> CREATOR = new Creator<Document_Actions_ThirdPartyPayment>() {
        @Override
        public Document_Actions_ThirdPartyPayment createFromParcel(Parcel in) {
            return new Document_Actions_ThirdPartyPayment(in);
        }

        @Override
        public Document_Actions_ThirdPartyPayment[] newArray(int size) {
            return new Document_Actions_ThirdPartyPayment[size];
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

    public String getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public void setBeneficiaryAccount(String beneficiaryAccount) {
        this.beneficiaryAccount = beneficiaryAccount;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
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

    public String getRoutingNo() {
        return routingNo;
    }

    public void setRoutingNo(String routingNo) {
        this.routingNo = routingNo;
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

    public String getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
    }

    public String getReferenceInfo() {
        return referenceInfo;
    }

    public void setReferenceInfo(String referenceInfo) {
        this.referenceInfo = referenceInfo;
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
        parcel.writeString(beneficiaryAccount);
        parcel.writeString(beneficiaryName);
        parcel.writeString(minimumAmountDue);
        parcel.writeString(actionPriority);
        parcel.writeString(routingNo);
        parcel.writeString(actionDueDate);
        parcel.writeString(actionId);
        parcel.writeString(amountDue);
        parcel.writeString(referenceInfo);
        parcel.writeString(message);
        parcel.writeString(link);
        parcel.writeString(expirationDate);
    }
}
