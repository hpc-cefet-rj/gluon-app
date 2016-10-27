package com.gluonhq.samples.cloudlink.connector.rest.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The JPA entity definition for storing the Settings object.
 */
@Entity
public class Settings {
    @Id
    private String id;

    private boolean showDate;
    private boolean ascending;
    private int sortingId;
    private int fontSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public int getSortingId() {
        return sortingId;
    }

    public void setSortingId(int sortingId) {
        this.sortingId = sortingId;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
