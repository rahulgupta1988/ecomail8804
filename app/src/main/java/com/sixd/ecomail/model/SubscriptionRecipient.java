package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 03-05-2018.
 */

@Entity
public class SubscriptionRecipient {
    @Id
    long id;

    String actionAllowed="";
    String actionId="";
    String recipientId="";
    String recipientType="";
    String subscriptionId="";

    public SubscriptionRecipient() {
    }

    public SubscriptionRecipient(String actionAllowed, String actionId, String recipientId, String recipientType, String subscriptionId) {
        this.actionAllowed = actionAllowed;
        this.actionId = actionId;
        this.recipientId = recipientId;
        this.recipientType = recipientType;
        this.subscriptionId = subscriptionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getActionAllowed() {
        return actionAllowed;
    }

    public void setActionAllowed(String actionAllowed) {
        this.actionAllowed = actionAllowed;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
