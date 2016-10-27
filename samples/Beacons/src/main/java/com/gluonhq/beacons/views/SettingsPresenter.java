package com.gluonhq.beacons.views;

import javax.inject.Inject;

import com.gluonhq.beacons.Beacons;
import com.gluonhq.beacons.settings.Settings;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.SettingsPane;
import com.gluonhq.charm.glisten.control.settings.DefaultOption;
import com.gluonhq.charm.glisten.control.settings.Option;
import com.gluonhq.charm.glisten.control.settings.OptionBase;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;

public class SettingsPresenter {

    @FXML
    private View settings;

    @Inject
    private Settings config;
    
    @FXML 
    private SettingsPane settingsPane;
    
    public void initialize() {
        settings.setShowTransitionFactory(BounceInRightTransition::new);
        
        final Option<StringProperty> uuidOption = new DefaultOption<>(MaterialDesignIcon.BLUETOOTH_SEARCHING.graphic(), 
        		"Set the UUID", "Set the UUID to be scanned", "Beacon Settings", config.uuidProperty(), true);
        
        ((OptionBase<StringProperty>) uuidOption).setLayout(Orientation.VERTICAL);
        
        settingsPane.getOptions().add(uuidOption);
        settingsPane.setSearchBoxVisible(false);
        
        settings.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(Beacons.MENU_LAYER)));
                appBar.setTitleText("Settings");
                appBar.getActionItems().add(MaterialDesignIcon.SYNC.button(e -> config.setUuid(Settings.UUID)));
            }
        });
    }
}
