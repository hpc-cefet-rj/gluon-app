package com.gluonhq.samples.connect.file;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private StringProperty name = new SimpleStringProperty();
    private BooleanProperty subscribed = new SimpleBooleanProperty();

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean getSubscribed() {
        return subscribed.get();
    }

    public BooleanProperty subscribedProperty() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed.set(subscribed);
    }
}
