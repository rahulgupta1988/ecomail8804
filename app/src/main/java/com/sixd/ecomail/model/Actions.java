package com.sixd.ecomail.model;

import io.objectbox.annotation.BaseEntity;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Rahul Gupta on 02-05-2018.
 */

@Entity
public class Actions {

    @Id
    long id;
    String actionId = "";

    String actionDescription = "";

    public Actions() {
    }

    public Actions(String actionId, String actionDescription) {
        this.actionId = actionId;
        this.actionDescription = actionDescription;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }
}
