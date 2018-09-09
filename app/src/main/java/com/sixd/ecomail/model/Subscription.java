package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Rahul Gupta on 03-05-2018.
 */
@Entity
public class Subscription {
    @Id
    long id;

    String block="";
    String documentGroupId="";
    String documentId="";
    String documentName="";
    String isActionAllowed="";
    String isMandatory="";
    String ownerConsumerId="";
    String subscriptionId="";
    String subscriptionStartDate="";
    String subscriptionType="";


    public ToOne<DocumentGroup> documentGroupToOne;

    public Subscription() {
    }


    public Subscription(String block, String documentGroupId, String documentId, String documentName, String isMandatory, String ownerConsumerId, String subscriptionId, String subscriptionStartDate, String subscriptionType) {
        this.block = block;
        this.documentGroupId = documentGroupId;
        this.documentId = documentId;
        this.documentName = documentName;
        this.isMandatory = isMandatory;
        this.ownerConsumerId = ownerConsumerId;
        this.subscriptionId = subscriptionId;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionType = subscriptionType;
    }

    public Subscription(String block, String documentGroupId, String documentId, String documentName, String isActionAllowed, String isMandatory, String ownerConsumerId, String subscriptionId, String subscriptionStartDate, String subscriptionType) {
        this.block = block;
        this.documentGroupId = documentGroupId;
        this.documentId = documentId;
        this.documentName = documentName;
        this.isActionAllowed = isActionAllowed;
        this.isMandatory = isMandatory;
        this.ownerConsumerId = ownerConsumerId;
        this.subscriptionId = subscriptionId;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionType = subscriptionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getDocumentGroupId() {
        return documentGroupId;
    }

    public void setDocumentGroupId(String documentGroupId) {
        this.documentGroupId = documentGroupId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getIsActionAllowed() {
        return isActionAllowed;
    }

    public void setIsActionAllowed(String isActionAllowed) {
        this.isActionAllowed = isActionAllowed;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getOwnerConsumerId() {
        return ownerConsumerId;
    }

    public void setOwnerConsumerId(String ownerConsumerId) {
        this.ownerConsumerId = ownerConsumerId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(String subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }


}
