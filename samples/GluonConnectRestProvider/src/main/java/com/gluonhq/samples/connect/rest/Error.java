package com.gluonhq.samples.connect.rest;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlElement;

public class Error {
    private final IntegerProperty errorId = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty errorName = new SimpleStringProperty();

    @XmlElement(name = "error_id")
    public int getErrorId() {
        return errorId.get();
    }

    public IntegerProperty errorIdProperty() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId.set(errorId);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @XmlElement(name = "error_name")
    public String getErrorName() {
        return errorName.get();
    }

    public StringProperty errorNameProperty() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName.set(errorName);
    }
}
