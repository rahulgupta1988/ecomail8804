package com.sixd.ecomail.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Rahul Gupta on 02-05-2018.
 */

@Entity
public class Document {

    @Id
    long id;

    String documentId = "";
    String documentName = "";
    String subscriptionId = "";
    String subscriptionType = "";
    String ownerConsumerId = "";
    String subscriptionCreateDate = "";
    String block = "";
    String mandatory = "";
    String subscriptionStartDate = "";


    String cetegoryDescription = "";
    String contentCreationDate = "";
    String contentLocation = "";
    String contentUpdateDat = "";
    String customerIdentifier = "";
    String customerIdentifierType = "";
    String displayLocation = "";
    String documentCategroyId = "";
    String documentDate = "";
    String documentGroupId = "";
    String documentGroupName = "";
    String documentLogoLocation = "";
    String documentWebLocation = "";
    String envelopeImage = "";
    String paperlessStatus = "";
    String preiodDescription = "";
    String preiodEnd = "";
    String preiodStart = "";
    String primaryAction = "";
    String primaryConsumerId = "";
    String primaryConsumerName = "";
    String producerId = "";
    String producerMessage = "";
    String producerName = "";
    String recipientCosumeId = "";
    String recipientName = "";
    String recipientShortName = "";
    String stateTypeId = "";



    String transmissionId = "";




    public ToOne<Document_Actions> document_actionsToOne;
    public ToOne<Folder>  folderToOne;


    public Document() {
    }

    public Document(String documentId, String documentName, String subscriptionId, String subscriptionType, String ownerConsumerId, String subscriptionCreateDate, String block,
                    String mandatory, String subscriptionStartDate, String cetegoryDescription, String contentCreationDate, String contentLocation,
                    String contentUpdateDat, String customerIdentifier, String customerIdentifierType, String displayLocation,
                    String documentCategroyId, String documentDate, String documentGroupId, String documentGroupName, String documentLogoLocation,
                    String documentWebLocation, String envelopeImage, String paperlessStatus, String preiodDescription, String preiodEnd,
                    String preiodStart, String primaryAction, String primaryConsumerId, String primaryConsumerName, String producerId,
                    String producerMessage, String producerName, String recipientCosumeId, String recipientName, String recipientShortName,
                    String stateTypeId, String transmissionId) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.subscriptionId = subscriptionId;
        this.subscriptionType = subscriptionType;
        this.ownerConsumerId = ownerConsumerId;
        this.subscriptionCreateDate = subscriptionCreateDate;
        this.block = block;
        this.mandatory = mandatory;
        this.subscriptionStartDate = subscriptionStartDate;
        this.cetegoryDescription = cetegoryDescription;
        this.contentCreationDate = contentCreationDate;
        this.contentLocation = contentLocation;
        this.contentUpdateDat = contentUpdateDat;
        this.customerIdentifier = customerIdentifier;
        this.customerIdentifierType = customerIdentifierType;
        this.displayLocation = displayLocation;
        this.documentCategroyId = documentCategroyId;
        this.documentDate = documentDate;
        this.documentGroupId = documentGroupId;
        this.documentGroupName = documentGroupName;
        this.documentLogoLocation = documentLogoLocation;
        this.documentWebLocation = documentWebLocation;
        this.envelopeImage = envelopeImage;
        this.paperlessStatus = paperlessStatus;
        this.preiodDescription = preiodDescription;
        this.preiodEnd = preiodEnd;
        this.preiodStart = preiodStart;
        this.primaryAction = primaryAction;
        this.primaryConsumerId = primaryConsumerId;
        this.primaryConsumerName = primaryConsumerName;
        this.producerId = producerId;
        this.producerMessage = producerMessage;
        this.producerName = producerName;
        this.recipientCosumeId = recipientCosumeId;
        this.recipientName = recipientName;
        this.recipientShortName = recipientShortName;
        this.stateTypeId = stateTypeId;
        this.transmissionId = transmissionId;

    }

    public Document(String documentId, String documentName, String subscriptionId, String subscriptionType, String ownerConsumerId,
                    String subscriptionCreateDate, String block, String mandatory, String subscriptionStartDate) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.subscriptionId = subscriptionId;
        this.subscriptionType = subscriptionType;
        this.ownerConsumerId = ownerConsumerId;
        this.subscriptionCreateDate = subscriptionCreateDate;
        this.block = block;
        this.mandatory = mandatory;
        this.subscriptionStartDate = subscriptionStartDate;


    }


    protected Document(Parcel in) {
        id = in.readLong();
        documentId = in.readString();
        documentName = in.readString();
        subscriptionId = in.readString();
        subscriptionType = in.readString();
        ownerConsumerId = in.readString();
        subscriptionCreateDate = in.readString();
        block = in.readString();
        mandatory = in.readString();
        subscriptionStartDate = in.readString();
        cetegoryDescription = in.readString();
        contentCreationDate = in.readString();
        contentLocation = in.readString();
        contentUpdateDat = in.readString();
        customerIdentifier = in.readString();
        customerIdentifierType = in.readString();
        displayLocation = in.readString();
        documentCategroyId = in.readString();
        documentDate = in.readString();
        documentGroupId = in.readString();
        documentGroupName = in.readString();
        documentLogoLocation = in.readString();
        documentWebLocation = in.readString();
        envelopeImage = in.readString();
        paperlessStatus = in.readString();
        preiodDescription = in.readString();
        preiodEnd = in.readString();
        preiodStart = in.readString();
        primaryAction = in.readString();
        primaryConsumerId = in.readString();
        primaryConsumerName = in.readString();
        producerId = in.readString();
        producerMessage = in.readString();
        producerName = in.readString();
        recipientCosumeId = in.readString();
        recipientName = in.readString();
        recipientShortName = in.readString();
        stateTypeId = in.readString();
        transmissionId = in.readString();
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getOwnerConsumerId() {
        return ownerConsumerId;
    }

    public void setOwnerConsumerId(String ownerConsumerId) {
        this.ownerConsumerId = ownerConsumerId;
    }

    public String getSubscriptionCreateDate() {
        return subscriptionCreateDate;
    }

    public void setSubscriptionCreateDate(String subscriptionCreateDate) {
        this.subscriptionCreateDate = subscriptionCreateDate;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(String subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }


    public String getCetegoryDescription() {
        return cetegoryDescription;
    }

    public void setCetegoryDescription(String cetegoryDescription) {
        this.cetegoryDescription = cetegoryDescription;
    }

    public String getContentCreationDate() {
        return contentCreationDate;
    }

    public void setContentCreationDate(String contentCreationDate) {
        this.contentCreationDate = contentCreationDate;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public String getContentUpdateDat() {
        return contentUpdateDat;
    }

    public void setContentUpdateDat(String contentUpdateDat) {
        this.contentUpdateDat = contentUpdateDat;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public String getCustomerIdentifierType() {
        return customerIdentifierType;
    }

    public void setCustomerIdentifierType(String customerIdentifierType) {
        this.customerIdentifierType = customerIdentifierType;
    }

    public String getDisplayLocation() {
        return displayLocation;
    }

    public void setDisplayLocation(String displayLocation) {
        this.displayLocation = displayLocation;
    }

    public String getDocumentCategroyId() {
        return documentCategroyId;
    }

    public void setDocumentCategroyId(String documentCategroyId) {
        this.documentCategroyId = documentCategroyId;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentGroupId() {
        return documentGroupId;
    }

    public void setDocumentGroupId(String documentGroupId) {
        this.documentGroupId = documentGroupId;
    }

    public String getDocumentGroupName() {
        return documentGroupName;
    }

    public void setDocumentGroupName(String documentGroupName) {
        this.documentGroupName = documentGroupName;
    }

    public String getDocumentLogoLocation() {
        return documentLogoLocation;
    }

    public void setDocumentLogoLocation(String documentLogoLocation) {
        this.documentLogoLocation = documentLogoLocation;
    }

    public String getDocumentWebLocation() {
        return documentWebLocation;
    }

    public void setDocumentWebLocation(String documentWebLocation) {
        this.documentWebLocation = documentWebLocation;
    }

    public String getEnvelopeImage() {
        return envelopeImage;
    }

    public void setEnvelopeImage(String envelopeImage) {
        this.envelopeImage = envelopeImage;
    }

    public String getPaperlessStatus() {
        return paperlessStatus;
    }

    public void setPaperlessStatus(String paperlessStatus) {
        this.paperlessStatus = paperlessStatus;
    }

    public String getPreiodDescription() {
        return preiodDescription;
    }

    public void setPreiodDescription(String preiodDescription) {
        this.preiodDescription = preiodDescription;
    }

    public String getPreiodEnd() {
        return preiodEnd;
    }

    public void setPreiodEnd(String preiodEnd) {
        this.preiodEnd = preiodEnd;
    }

    public String getPreiodStart() {
        return preiodStart;
    }

    public void setPreiodStart(String preiodStart) {
        this.preiodStart = preiodStart;
    }

    public String getPrimaryAction() {
        return primaryAction;
    }

    public void setPrimaryAction(String primaryAction) {
        this.primaryAction = primaryAction;
    }

    public String getPrimaryConsumerId() {
        return primaryConsumerId;
    }

    public void setPrimaryConsumerId(String primaryConsumerId) {
        this.primaryConsumerId = primaryConsumerId;
    }

    public String getPrimaryConsumerName() {
        return primaryConsumerName;
    }

    public void setPrimaryConsumerName(String primaryConsumerName) {
        this.primaryConsumerName = primaryConsumerName;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getProducerMessage() {
        return producerMessage;
    }

    public void setProducerMessage(String producerMessage) {
        this.producerMessage = producerMessage;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getRecipientCosumeId() {
        return recipientCosumeId;
    }

    public void setRecipientCosumeId(String recipientCosumeId) {
        this.recipientCosumeId = recipientCosumeId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientShortName() {
        return recipientShortName;
    }

    public void setRecipientShortName(String recipientShortName) {
        this.recipientShortName = recipientShortName;
    }

    public String getStateTypeId() {
        return stateTypeId;
    }

    public void setStateTypeId(String stateTypeId) {
        this.stateTypeId = stateTypeId;
    }

    public String getTransmissionId() {
        return transmissionId;
    }

    public void setTransmissionId(String transmissionId) {
        this.transmissionId = transmissionId;
    }



}
