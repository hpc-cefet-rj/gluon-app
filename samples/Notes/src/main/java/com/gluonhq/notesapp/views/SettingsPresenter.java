package com.gluonhq.notesapp.views;

import com.gluonhq.charm.glisten.animation.BounceInUpTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.SettingsPane;
import com.gluonhq.charm.glisten.control.settings.DefaultOption;
import com.gluonhq.charm.glisten.control.settings.Option;
import com.gluonhq.charm.glisten.control.settings.OptionBase;
import com.gluonhq.charm.glisten.control.settings.OptionEditor;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.notesapp.NotesApp;
import com.gluonhq.notesapp.model.Settings;
import com.gluonhq.notesapp.model.Settings.SORTING;
import com.gluonhq.notesapp.service.Service;
import java.util.Optional;
import java.util.function.Function;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javax.inject.Inject;

public class SettingsPresenter {

    @Inject private Service service;
    private Settings config;
    
    @FXML private View settings;

    @FXML private SettingsPane settingsPane;

    public void initialize() {
        settings.setShowTransitionFactory(BounceInUpTransition::new);
        settings.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(NotesApp.MENU_LAYER)));
                appBar.setTitleText("Settings");
                appBar.getActionItems().add(MaterialDesignIcon.CLOSE.button(e -> MobileApplication.getInstance().goHome()));
            }
        });
        
        config = new Settings();
        updateSettings(service.settingsProperty().get());
        service.settingsProperty().addListener((obs, ov, nv) -> updateSettings(nv));
        
        config.showDateProperty().addListener((obs, ov, nv) -> updateService());
        config.sortingProperty().addListener((obs, ov, nv) -> updateService());
        config.ascendingProperty().addListener((obs, ov, nv) -> updateService());
        config.fontSizeProperty().addListener((obs, ov, nv) -> updateService());
        
        final Option<BooleanProperty> dateOption = new DefaultOption<>(MaterialDesignIcon.DATE_RANGE.graphic(),
                "Show Date", "Show the note's date", null, config.showDateProperty(), true);
        
        final DefaultOption<ObjectProperty<SORTING>> sortOption = new DefaultOption<>(MaterialDesignIcon.SORT_BY_ALPHA.graphic(),
                "Sort Notes", "Sort the notes by", null, config.sortingProperty(), true);
        
        final DefaultOption<BooleanProperty> ascendingOption = new DefaultOption<>(MaterialDesignIcon.SORT.graphic(),
                "Asc./Des.", "Sort in ascending or descending order", null, config.ascendingProperty(), true);
        
        final Option<Number> fontOption = new SliderOption(MaterialDesignIcon.NETWORK_CELL.graphic(), 
                "Size of Text", "Set the text size", null, config.fontSizeProperty(), true, 8, 12);
        
        settingsPane.getOptions().addAll(dateOption, sortOption, ascendingOption, fontOption);
        settingsPane.setSearchBoxVisible(false);
    }   
    
    private void updateSettings(Settings settings) {
        this.config.setShowDate(settings.isShowDate());
        this.config.setSorting(settings.getSorting());
        this.config.setAscending(settings.isAscending());
        this.config.setFontSize(settings.getFontSize());
    }
    
    private void updateService() {
        Settings newConfig = new Settings();
        newConfig.setShowDate(this.config.isShowDate());
        newConfig.setFontSize(this.config.getFontSize());
        newConfig.setSorting(this.config.getSorting());
        newConfig.setAscending(this.config.isAscending());
        
        service.settingsProperty().set(newConfig);        
        service.storeSettings();
    }
    
    private class SliderOption extends OptionBase<Number> {

        private final int min;
        private final int max;
        
        public SliderOption(Node graphic, String caption, String description, String category, IntegerProperty value, 
                boolean isEditable, int min, int max) {
            super(graphic, caption, description, category, (Property<Number>) value, isEditable);
            this.min = min;
            this.max = max;
        }
        
        @Override
        public Property<Number> valueProperty() {
            return value;
        }

        @Override
        public Optional<Function<Option<Number>, OptionEditor<Number>>> editorFactoryProperty() {
            return Optional.of(option -> new SliderEditor(option, min, max));
        }
        
    }
    
    private class SliderEditor implements OptionEditor<Number> {
        
        private final Slider slider;

        public SliderEditor(Option<Number> option, int min, int max) {
            slider = new Slider(min, max, option.valueProperty().getValue().doubleValue());
            slider.setSnapToTicks(true);
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);
            valueProperty().bindBidirectional(option.valueProperty());
        }

        @Override
        public Node getEditor() {
            return slider; 
        }

        @Override
        public Number getValue() {
            return slider.getValue();
        }

        @Override
        public void setValue(Number value) {
            slider.setValue(value.doubleValue());
        }

        @Override
        public final Property<Number> valueProperty() {
            return (Property<Number>) slider.valueProperty();
        }
    }
    
}
