package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 23-04-2018.
 */

@Entity
public class DocTypes {

    @Id
    long id;
    String documentType="";
    String documentTypeName="";
    boolean isSelected=false;
    boolean isSelectedForSmartFolder=false;

    public DocTypes() {
    }

    public DocTypes(String documentType, String documentTypeName, boolean isSelected, boolean isSelectedForSmartFolder) {
        this.documentType = documentType;
        this.documentTypeName = documentTypeName;
        this.isSelected = isSelected;
        this.isSelectedForSmartFolder = isSelectedForSmartFolder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
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
}
