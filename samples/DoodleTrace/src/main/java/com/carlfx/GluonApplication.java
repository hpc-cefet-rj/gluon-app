package com.carlfx;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GluonApplication extends MobileApplication {

    public static final String DOODLE_TRACE_VIEW = HOME_VIEW;

    @Override
    public void init() {

        View view = new DoodleTrace(DOODLE_TRACE_VIEW);
        addViewFactory(DOODLE_TRACE_VIEW, () -> view);
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.getRandom().assignTo(scene);

        ((Stage) scene.getWindow()).getIcons()
                .add(new Image(GluonApplication.class
                        .getResourceAsStream("/icon.png")));
    }
}
