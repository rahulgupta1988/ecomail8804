package com.sixd.ecomail.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Gupta on 30-05-2018.
 */
public class Custom_FolderModel {

    String folderId = "";
    String folderName = "";
    int sequenceNo;
    boolean isSelected = false;
    boolean isSmartFolder = false;
    int docCount = 0;


    List<Document> smartDocs = new ArrayList<>();

    public Custom_FolderModel(String folderId, String folderName, int sequenceNo, boolean isSelected, boolean isSmartFolder, int docCount) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.sequenceNo = sequenceNo;
        this.isSelected = isSelected;
        this.isSmartFolder = isSmartFolder;
        this.docCount = docCount;
    }

    public List<Document> getSmartDocs() {
        return smartDocs;
    }

    public void setSmartDocs(List<Document> smartDocs) {
        this.smartDocs = smartDocs;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSmartFolder() {
        return isSmartFolder;
    }

    public void setSmartFolder(boolean smartFolder) {
        isSmartFolder = smartFolder;
    }

    public int getDocCount() {
        return docCount;
    }

    public void setDocCount(int docCount) {
        this.docCount = docCount;
    }
}
