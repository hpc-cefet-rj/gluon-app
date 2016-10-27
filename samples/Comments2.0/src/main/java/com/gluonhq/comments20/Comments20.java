package com.gluonhq.comments20;

import com.gluonhq.comments20.views.CommentsView;
import com.gluonhq.comments20.views.EditionView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Comments20 extends MobileApplication {

    public static final String COMMENTS_VIEW = HOME_VIEW;
    public static final String EDITION_VIEW = "Edition View";
    public static final String MENU_LAYER = "Side Menu";
    

    @Override
    public void init() {
        addViewFactory(COMMENTS_VIEW, () -> (View) new CommentsView().getView());
        addViewFactory(EDITION_VIEW, () -> (View) new EditionView().getView());
        
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(new DrawerManager().getDrawer()));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);
        
        scene.getStylesheets().add(Comments20.class.getResource("style.css").toExternalForm());
        
        ((Stage) scene.getWindow()).getIcons().add(new Image(Comments20.class.getResourceAsStream("/icon.png")));
    }
    
}
