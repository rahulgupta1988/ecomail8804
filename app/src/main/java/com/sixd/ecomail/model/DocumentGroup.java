package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

/**
 * Created by Rahul Gupta on 02-05-2018.
 */

@Entity
public class DocumentGroup {
    @Id
    long id;

    boolean isSelected=false;
    String documentGroupId="";
    String documentGroupName="";
    String gracePeriod="";

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    String relationship="";
    String documentGroupLogoLocation="";
    String paperlessStatus="";

    String documentGroupCreater="";
    String invitationSentId="";

    public ToMany<Subscription> subscriptionToMany;
    public ToOne<Producer> producerToOne;


    public DocumentGroup() {
    }

    public DocumentGroup(String documentGroupId, String documentGroupName, String gracePeriod, String relationship, String documentGroupLogoLocation, String paperlessStatus, String documentGroupCreater, String invitationSentId) {
        this.documentGroupId = documentGroupId;
        this.documentGroupName = documentGroupName;
        this.gracePeriod = gracePeriod;
        this.relationship = relationship;
        this.documentGroupLogoLocation = documentGroupLogoLocation;
        this.paperlessStatus = paperlessStatus;
        this.documentGroupCreater = documentGroupCreater;
        this.invitationSentId = invitationSentId;
    }


    public DocumentGroup(String documentGroupId, String documentGroupName, String gracePeriod, String relationship, String documentGroupLogoLocation, String paperlessStatus) {
        this.documentGroupId = documentGroupId;
        this.documentGroupName = documentGroupName;
        this.gracePeriod = gracePeriod;
        this.relationship = relationship;
        this.documentGroupLogoLocation = documentGroupLogoLocation;
        this.paperlessStatus = paperlessStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(String gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getDocumentGroupLogoLocation() {
        return documentGroupLogoLocation;
    }

    public void setDocumentGroupLogoLocation(String documentGroupLogoLocation) {
        this.documentGroupLogoLocation = documentGroupLogoLocation;
    }

    public String getPaperlessStatus() {
        return paperlessStatus;
    }

    public void setPaperlessStatus(String paperlessStatus) {
        this.paperlessStatus = paperlessStatus;
    }

    public String getDocumentGroupCreater() {
        return documentGroupCreater;
    }

    public void setDocumentGroupCreater(String documentGroupCreater) {
        this.documentGroupCreater = documentGroupCreater;
    }

    public String getInvitationSentId() {
        return invitationSentId;
    }

    public void setInvitationSentId(String invitationSentId) {
        this.invitationSentId = invitationSentId;
    }
}
