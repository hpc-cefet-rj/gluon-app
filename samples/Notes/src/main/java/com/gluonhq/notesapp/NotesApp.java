package com.gluonhq.notesapp;

import com.gluonhq.notesapp.views.NotesView;
import com.gluonhq.notesapp.views.EditionView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.gluonhq.notesapp.views.SettingsView;

public class NotesApp extends MobileApplication {

    public static final String NOTES_VIEW = HOME_VIEW;
    public static final String EDITION_VIEW = "Edition View";
    public static final String SETTINGS_VIEW = "Settings View";
    public static final String MENU_LAYER = "Side Menu";
    public static final String POPUP_FILTER_NOTES = "Filter Notes";
    
    @Override
    public void init() {
        
        addViewFactory(NOTES_VIEW, () -> (View) new NotesView().getView());
        addViewFactory(EDITION_VIEW, () -> (View) new EditionView().getView());
        addViewFactory(SETTINGS_VIEW, () -> (View) new SettingsView().getView());
        
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(new DrawerManager().getDrawer()));
        
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.LIGHT_GREEN.assignTo(scene);

        scene.getStylesheets().add(NotesApp.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(NotesApp.class.getResourceAsStream("/icon.png")));
    }
}
