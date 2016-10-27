package com.gluonhq.fiftystates;

import com.gluonhq.charm.glisten.application.MobileApplication;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FiftyStates extends MobileApplication {

    public static final String BASIC_VIEW = HOME_VIEW;

    @Override
    public void init() {
        addViewFactory(BASIC_VIEW, () -> new BasicView(BASIC_VIEW));
    }

    @Override
    public void postInit(Scene scene) {

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        ((Stage) scene.getWindow()).getIcons().add(new Image(FiftyStates.class.getResourceAsStream("/icon.png")));
    }
}
