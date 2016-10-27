package com.gluonhq.notesapp.views;

import com.gluonhq.charm.glisten.animation.BounceInLeftTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.notesapp.NotesApp;
import com.gluonhq.notesapp.model.Model;
import com.gluonhq.notesapp.model.Note;
import com.gluonhq.notesapp.model.Settings;
import com.gluonhq.notesapp.service.Service;
import java.time.LocalDate;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javax.inject.Inject;

public class NotesPresenter {

    @Inject private Service service;
    
    @FXML private View notes;

    @Inject private Model model;
    
    @FXML private CharmListView<Note, LocalDate> lstNotes;
    
    private FilteredList<Note> filteredList;
    
    public void initialize() {
        notes.setShowTransitionFactory(BounceInLeftTransition::new);
        notes.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(NotesApp.MENU_LAYER)));
                appBar.setTitleText("Notes");
                appBar.getActionItems().add(MaterialDesignIcon.FILTER_LIST.button(e -> {
                    MobileApplication.getInstance().showLayer(NotesApp.POPUP_FILTER_NOTES);
                }));
            }
        });
        
        lstNotes.setCellFactory(p -> new NoteCell(service, this::edit, this::remove));
        lstNotes.setHeadersFunction(t -> t.getCreationDate().toLocalDate());
        lstNotes.setHeaderCellFactory(p -> new HeaderCell());
        lstNotes.setPlaceholder(new Label("There are no notes"));
        
        service.notesProperty().addListener((ListChangeListener.Change<? extends Note> c) -> {
            filteredList = new FilteredList(c.getList());
            lstNotes.setItems(filteredList);
            lstNotes.setComparator((n1, n2) -> n1.getCreationDate().compareTo(n2.getCreationDate()));
        });
        
        final FloatingActionButton floatingActionButton = new FloatingActionButton();
        floatingActionButton.setOnAction(e -> edit(null));
        notes.getLayers().add(floatingActionButton.getLayer());
        
        MobileApplication.getInstance().addLayerFactory(NotesApp.POPUP_FILTER_NOTES, () -> { 
            FilterView filterView = new FilterView();
            FilterPresenter filterPresenter = (FilterPresenter) filterView.getPresenter();
            
            SidePopupView sidePopupView = new SidePopupView(filterView.getView(), Side.TOP, true);
            sidePopupView.showingProperty().addListener((obs, ov, nv) -> {
                if (ov && !nv) {
                    filteredList.setPredicate(filterPresenter.getPredicate());
                }
            });
            
            return sidePopupView; 
        });
        
        service.retrieveNotes();
        
        service.settingsProperty().addListener((obs, ov, nv) -> updateSettings());
        
        updateSettings();
    }
    
    private void edit(Note note) {
        model.activeNote().set(note);
        MobileApplication.getInstance().switchView(NotesApp.EDITION_VIEW);
    }
    
    private void remove(Note note) {
        service.removeNote(note);
    }

    private void updateSettings() {
        Settings settings = service.settingsProperty().get();
        if (settings.isAscending()) {
            lstNotes.setHeaderComparator((h1, h2) -> h1.compareTo(h2));
        } else {
            lstNotes.setHeaderComparator((h1, h2) -> h2.compareTo(h1));
        }
        
        switch (settings.getSorting()) {
            case DATE:  
                if (settings.isAscending()) {
                    lstNotes.setComparator((n1, n2) -> n1.getCreationDate().compareTo(n2.getCreationDate()));
                } else {
                    lstNotes.setComparator((n1, n2) -> n2.getCreationDate().compareTo(n1.getCreationDate()));
                }
                break;
            case TITLE: 
                if (settings.isAscending()) {
                    lstNotes.setComparator((n1, n2) -> n1.getTitle().compareTo(n2.getTitle()));
                } else {
                    lstNotes.setComparator((n1, n2) -> n2.getTitle().compareTo(n1.getTitle()));
                }
                break;
            case CONTENT: 
                if (settings.isAscending()) {
                    lstNotes.setComparator((n1, n2) -> n1.getText().compareTo(n2.getText()));
                } else {
                    lstNotes.setComparator((n1, n2) -> n2.getText().compareTo(n1.getText()));
                }
                break;
        }
    }
}
