package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 11-05-2018.
 */
@Entity
public class InboxSortOrder {
    @Id
    long id;

    String associatedEntity="";
    String caption="";
    String category="";
    String dbColumn="";
    int defaultOrder;
    boolean isSelected;
    String sortOrder="";


    public InboxSortOrder() {
    }

    public InboxSortOrder(String associatedEntity, String caption, String category, String dbColumn, int defaultOrder, boolean isSelected, String sortOrder) {
        this.associatedEntity = associatedEntity;
        this.caption = caption;
        this.category = category;
        this.dbColumn = dbColumn;
        this.defaultOrder = defaultOrder;
        this.isSelected = isSelected;
        this.sortOrder = sortOrder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAssociatedEntity() {
        return associatedEntity;
    }

    public void setAssociatedEntity(String associatedEntity) {
        this.associatedEntity = associatedEntity;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public void setDbColumn(String dbColumn) {
        this.dbColumn = dbColumn;
    }

    public int getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(int defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
