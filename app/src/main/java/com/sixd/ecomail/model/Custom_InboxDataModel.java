package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;

/**
 * Created by Rahul Gupta on 14-05-2018.
 */


public class Custom_InboxDataModel {

    String documentName="";
    String documentGroupName="";
    String primaryConsumerName="";

    public String getPreiodStart() {
        return preiodStart;
    }

    public void setPreiodStart(String preiodStart) {
        this.preiodStart = preiodStart;
    }

    public String getPreiodEnd() {
        return preiodEnd;
    }

    public void setPreiodEnd(String preiodEnd) {
        this.preiodEnd = preiodEnd;
    }

    String contentCreationDate="";
    String logoUrl="";
    String preiodStart="";
    String preiodEnd="";

    public String getDisplayLocation() {
        return displayLocation;
    }

    public void setDisplayLocation(String displayLocation) {
        this.displayLocation = displayLocation;
    }

    String documentWebLocation="";
    String documentCategroyId="";
    String amountDue="";
    String displayLocation="";

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    String minimumAmountDue="";
    String documentDate="";

    public String getTransmissionId() {
        return transmissionId;
    }

    public void setTransmissionId(String transmissionId) {
        this.transmissionId = transmissionId;
    }

    String actionDueDate="";
    String link="";
    String message;
    String transmissionId="";

    public String getPaymentStateId() {
        return paymentStateId;
    }

    public void setPaymentStateId(String paymentStateId) {
        this.paymentStateId = paymentStateId;
    }

    String actionID="";
    String DocumentGroupId="";
    String contentLocation="";
    String producerName="";

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    String recipientName="";
    String paymentStateId="";
    String viewType="";
    String documentID="";


    public Custom_InboxDataModel(String documentName, String documentGroupName, String primaryConsumerName, String contentCreationDate,
            String logoUrl, String documentWebLocation, String documentCategroyId, String amountDue, String minimumAmountDue, String actionDueDate,
                                 String link, String message, String actionID,String DocumentGroupId,  String contentLocation,
                                 String producerName,String recipientName,String paymentStateId,String viewType) {
        this.documentName = documentName;
        this.documentGroupName = documentGroupName;
        this.primaryConsumerName = primaryConsumerName;
        this.contentCreationDate = contentCreationDate;
        this.logoUrl = logoUrl;
        this.documentWebLocation = documentWebLocation;
        this.documentCategroyId = documentCategroyId;
        this.amountDue = amountDue;
        this.minimumAmountDue = minimumAmountDue;
        this.actionDueDate = actionDueDate;
        this.link = link;
        this.message = message;
        this.actionID = actionID;
        this.DocumentGroupId=DocumentGroupId;
        this.contentLocation=contentLocation;
        this.producerName=producerName;
        this.recipientName=recipientName;
        this.paymentStateId=paymentStateId;
        this.viewType = viewType;
    }



    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getDocumentGroupId() {
        return DocumentGroupId;
    }

    public void setDocumentGroupId(String documentGroupId) {
        DocumentGroupId = documentGroupId;
    }

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentGroupName() {
        return documentGroupName;
    }

    public void setDocumentGroupName(String documentGroupName) {
        this.documentGroupName = documentGroupName;
    }

    public String getPrimaryConsumerName() {
        return primaryConsumerName;
    }

    public void setPrimaryConsumerName(String primaryConsumerName) {
        this.primaryConsumerName = primaryConsumerName;
    }

    public String getContentCreationDate() {
        return contentCreationDate;
    }

    public void setContentCreationDate(String contentCreationDate) {
        this.contentCreationDate = contentCreationDate;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDocumentWebLocation() {
        return documentWebLocation;
    }

    public void setDocumentWebLocation(String documentWebLocation) {
        this.documentWebLocation = documentWebLocation;
    }

    public String getDocumentCategroyId() {
        return documentCategroyId;
    }

    public void setDocumentCategroyId(String documentCategroyId) {
        this.documentCategroyId = documentCategroyId;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
    }

    public String getMinimumAmountDue() {
        return minimumAmountDue;
    }

    public void setMinimumAmountDue(String minimumAmountDue) {
        this.minimumAmountDue = minimumAmountDue;
    }

    public String getActionDueDate() {
        return actionDueDate;
    }

    public void setActionDueDate(String actionDueDate) {
        this.actionDueDate = actionDueDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }
}
