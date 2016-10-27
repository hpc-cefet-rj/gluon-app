package com.gluonhq.sqlite;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GluonSQLite extends MobileApplication {

    public static final String BASIC_VIEW = HOME_VIEW;

    @Override
    public void init() {
        addViewFactory(BASIC_VIEW, () -> new BasicView(BASIC_VIEW));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        ((Stage) scene.getWindow()).getIcons().add(new Image(GluonSQLite.class.getResourceAsStream("/icon.png")));

        if (System.getProperty("os.arch").toUpperCase().contains("ARM") &&
                !Platform.isIOS()  && !Platform.isAndroid()) {
            ((Stage) scene.getWindow()).setFullScreen(true);
            ((Stage) scene.getWindow()).setFullScreenExitHint("");
        }
    }
}
