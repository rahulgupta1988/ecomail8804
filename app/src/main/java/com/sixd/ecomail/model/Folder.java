package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;

/**
 * Created by Rahul Gupta on 19-04-2018.
 */


@Entity
public class Folder {
    @Id private long id;
    @Index
    String folderId = "";
    String folderName = "";
    int sequenceNo;
    boolean isSelected = false;

    public Folder() {}

    public ToMany<Document> documentToMany;



    public Folder(String folderId, String folderName, int sequenceNo, boolean isSelected) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.sequenceNo = sequenceNo;
        this.isSelected = isSelected;

    }


    public Folder(String folderId, String folderName, int sequenceNo) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.sequenceNo = sequenceNo;
        this.isSelected = isSelected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


}
