package com.gluonhq.notesapp.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.notesapp.NotesApp;
import com.gluonhq.notesapp.model.Model;
import com.gluonhq.notesapp.model.Note;
import com.gluonhq.notesapp.service.Service;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.inject.Inject;

public class EditionPresenter {

    @Inject private Service service;
    
    @Inject private Model model;
    
    @FXML private View edition;

    @FXML private Button submit;
    @FXML private Button cancel;
    @FXML private TextField title;
    @FXML private TextArea comment;
    
    private boolean editMode;

    public void initialize() {
        edition.setShowTransitionFactory(BounceInRightTransition::new);
        
        edition.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                submit.disableProperty().unbind();
                
                Note activeNote = model.activeNote().get();
                if (activeNote != null) {
                    submit.setText("APPLY");
                    title.setText(activeNote.getTitle());
                    comment.setText(activeNote.getText());
                    submit.disableProperty().bind(Bindings.createBooleanBinding(()->{
                        if (title == null || comment == null) {
                            return true;
                        }
                        return title.textProperty()
                                .isEqualTo(activeNote.getTitle())
                                .and(comment.textProperty()
                                        .isEqualTo(activeNote.getText())).get();
                        }, title.textProperty(),comment.textProperty()));
                    editMode = true;
                } else {
                    submit.setText("SUBMIT");
                    submit.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                            return title.textProperty()
                                    .isEmpty()
                                    .or(comment.textProperty()
                                            .isEmpty()).get();
                        }, title.textProperty(), comment.textProperty()));
                    editMode = false;
                }
                 
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(NotesApp.MENU_LAYER)));
                appBar.setTitleText(editMode ? "Edit Note" : "Add Note");
            } else {
                title.clear();
                comment.clear();
            }
        });
        
        submit.setOnAction(e -> {
            Note note = editMode ? model.activeNote().get() : new Note();
            note.setTitle(title.getText());
            note.setText(comment.getText());
            
            if (!editMode) {
                service.addNote(note);
            }
            close();
        });
        cancel.setOnAction(e -> close());
    }
    
    private void close() {
        title.clear();
        comment.clear();
        model.activeNote().set(null);
        MobileApplication.getInstance().goHome();
    }
    
}
