package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * Created by Rahul Gupta on 30-05-2018.
 */

@Entity
public class SmartFolder {
    @Id
    long id;

    int order;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    String createDate;
    String smartFolderId;
    String smartFolderName;
    String updateDate;
    boolean isSelected=false;


    public SmartFolder(int order, String createDate, String smartFolderId, String smartFolderName, String updateDate) {
        this.order = order;
        this.createDate = createDate;
        this.smartFolderId = smartFolderId;
        this.smartFolderName = smartFolderName;
        this.updateDate = updateDate;
    }


    public ToMany<Folder> folderToMany;
    public ToMany<DocumentGroup> documentGroupToMany;
    public ToMany<DocTypes> docTypesToMany;
    public ToMany<Recipents> recipentsToMany;

    public SmartFolder() {
    }


    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSmartFolderId() {
        return smartFolderId;
    }

    public void setSmartFolderId(String smartFolderId) {
        this.smartFolderId = smartFolderId;
    }

    public String getSmartFolderName() {
        return smartFolderName;
    }

    public void setSmartFolderName(String smartFolderName) {
        this.smartFolderName = smartFolderName;
    }

    public String getUpdateDate() {
        return updateDate;
    }


    public void setFolderToMany(ToMany<Folder> folderToMany) {
        this.folderToMany = folderToMany;
    }
}
