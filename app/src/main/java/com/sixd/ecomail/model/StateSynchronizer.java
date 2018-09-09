package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 07-05-2018.
 */
@Entity
public class StateSynchronizer {
    @Id
    long id;

    String transmissionId="";
    String stateTypeId="";
    String updateDate="";

    public StateSynchronizer() {
    }

    public StateSynchronizer(String transmissionId, String stateTypeId, String updateDate) {
        this.transmissionId = transmissionId;
        this.stateTypeId = stateTypeId;
        this.updateDate = updateDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransmissionId() {
        return transmissionId;
    }

    public void setTransmissionId(String transmissionId) {
        this.transmissionId = transmissionId;
    }

    public String getStateTypeId() {
        return stateTypeId;
    }

    public void setStateTypeId(String stateTypeId) {
        this.stateTypeId = stateTypeId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
