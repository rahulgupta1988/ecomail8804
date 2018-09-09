package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 02-05-2018.
 */

@Entity
public class Producer {

    @Id
    long id;

    boolean isSelected=false;
    boolean isSelectedForSmartFolder=false;
    String producerId="";
    String producerName="";
    String producerLogoLocation="";

    String startChar="";
    String isFirst="";

    public String getStartChar() {
        return startChar;
    }

    public void setStartChar(String startChar) {
        this.startChar = startChar;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public Producer() {}


    public Producer(boolean isSelected, String producerId, String producerName, String producerLogoLocation, String isFirst) {
        this.isSelected = isSelected;
        this.producerId = producerId;
        this.producerName = producerName;
        this.producerLogoLocation = producerLogoLocation;
        this.isFirst=isFirst;

    }

    public Producer(boolean isSelected, boolean isSelectedForSmartFolder, String producerId, String producerName, String producerLogoLocation) {
        this.isSelected = isSelected;
        this.isSelectedForSmartFolder = isSelectedForSmartFolder;
        this.producerId = producerId;
        this.producerName = producerName;
        this.producerLogoLocation = producerLogoLocation;
    }


    public Producer(String producerId, String producerName, String producerLogoLocation) {
        this.producerId = producerId;
        this.producerName = producerName;
        this.producerLogoLocation = producerLogoLocation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelectedForSmartFolder() {
        return isSelectedForSmartFolder;
    }

    public void setSelectedForSmartFolder(boolean selectedForSmartFolder) {
        isSelectedForSmartFolder = selectedForSmartFolder;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getProducerLogoLocation() {
        return producerLogoLocation;
    }

    public void setProducerLogoLocation(String producerLogoLocation) {
        this.producerLogoLocation = producerLogoLocation;
    }
}
