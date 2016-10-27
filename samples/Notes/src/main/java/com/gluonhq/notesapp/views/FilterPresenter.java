package com.gluonhq.notesapp.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.notesapp.NotesApp;
import com.gluonhq.notesapp.model.Note;
import java.util.function.Predicate;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class FilterPresenter {

    @FXML private TextField searchField;
    
    public void initialize() {
        HBox.setHgrow(searchField, Priority.ALWAYS);
    }
    
    @FXML
    private void search() {
        MobileApplication.getInstance().hideLayer(NotesApp.POPUP_FILTER_NOTES);
    }
    
    @FXML
    private void close() {
        searchField.clear();
        MobileApplication.getInstance().hideLayer(NotesApp.POPUP_FILTER_NOTES);
    }

    public Predicate<? super Note> getPredicate() {
        return n -> n.getTitle().contains(searchField.getText()) || 
                    n.getText().contains(searchField.getText());
    }
    
}
