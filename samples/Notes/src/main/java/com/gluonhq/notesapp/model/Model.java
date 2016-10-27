package com.gluonhq.notesapp.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Model {
    
    private final ObjectProperty<Note> activeNote = new SimpleObjectProperty<>();
    
    public ObjectProperty<Note> activeNote() {
        return activeNote;
    }
    
}
