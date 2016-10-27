package com.gluonhq.notesapp.views;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.DisplayService;
import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.notesapp.model.Note;
import com.gluonhq.notesapp.model.Settings;
import com.gluonhq.notesapp.service.Service;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class NoteCell extends CharmListCell<Note> {

    private final ListTile tile;
    private Note currentItem;
    private final DateTimeFormatter dateFormat;
    
    private final ChangeListener<String> noteChangeListener;
    
    private Settings settings;
    private int fontSize = -1;

    public NoteCell(Service service, Consumer<Note> edit, Consumer<Note> remove) {
        
        tile = new ListTile();
        tile.setPrimaryGraphic(MaterialDesignIcon.DESCRIPTION.graphic());
        
        Button btnEdit = MaterialDesignIcon.EDIT.button(e -> edit.accept(currentItem));
        Button btnRemove = MaterialDesignIcon.DELETE.button(e -> remove.accept(currentItem));
        HBox buttonBar = new HBox(0, btnEdit, btnRemove);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        tile.setSecondaryGraphic(buttonBar);
        
        dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd yyyy - HH:mm", Locale.ENGLISH);
        
        noteChangeListener = (obs, ov, nv) -> update();
        
        service.settingsProperty().addListener((obs, ov, nv) -> {
            settings = nv;
            update();
        });
        settings = service.settingsProperty().get();
        update();
    }

    @Override
    public void updateItem(Note item, boolean empty) {
        super.updateItem(item, empty);
        updateCurrentItem(item);
        if (!empty && item != null) {
            update();
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
    
    private void updateCurrentItem(Note note) {
        if (currentItem == null || !currentItem.equals(note)) {
            if (currentItem != null) {
                currentItem.titleProperty().removeListener(noteChangeListener);
                currentItem.textProperty().removeListener(noteChangeListener);
            }

            currentItem = note;

            if (currentItem != null) {
                currentItem.titleProperty().addListener(noteChangeListener);
                currentItem.textProperty().addListener(noteChangeListener);
            }
        }
    }

    private void update() {
        if (currentItem != null) {
            tile.textProperty().setAll(currentItem.getTitle(), 
                                       getSecondLine(currentItem.getText()));
            if (settings.isShowDate()) {
                tile.textProperty().add(dateFormat.format(currentItem.getCreationDate()));
            }
            
            if (fontSize != settings.getFontSize()) { 
                fontSize = settings.getFontSize();
                tile.getCenter().setStyle("-fx-font-size: " + 
                        (Services.get(DisplayService.class)
                                .map(d -> d.isTablet() ? 1.4 : 1.0)
                                .orElse(1.0) * fontSize) + "pt;");
            }
        } else {
            tile.textProperty().clear();
        }
    }
    
    private String getSecondLine(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        int eol = text.indexOf("\n", 0);
        return eol < 1 ? text : text.substring(0, eol) + "...";
    }
    
}
