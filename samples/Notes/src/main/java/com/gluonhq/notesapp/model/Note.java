package com.gluonhq.notesapp.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Note {

    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty text = new SimpleStringProperty();
    private long creationDate;
    
    public Note() {
        this.creationDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public Note(String title, String text) {
        this.title.set(title);
        this.text.set(text);
    }

    public final String getTitle() { return title.get(); }
    public final void setTitle(String title) { this.title.set(title); }
    public final StringProperty titleProperty() { return this.title; }
    
    public final String getText() { return text.get(); }
    public final void setText(String text) { this.text.set(text); }
    public final StringProperty textProperty() { return this.text; }

    public final LocalDateTime getCreationDate() {
        return LocalDateTime.ofEpochSecond(creationDate, 0, ZoneOffset.UTC);
    }

    @Override
    public String toString() {
        return super.toString() + " with title " + title.get();
    }
}