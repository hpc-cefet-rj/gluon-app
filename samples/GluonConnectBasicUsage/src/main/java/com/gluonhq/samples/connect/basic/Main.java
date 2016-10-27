package com.gluonhq.samples.connect.basic;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends MobileApplication {

    public static final String LIST_VIEW = HOME_VIEW;
    public static final String OBJECT_VIEW = "ObjectView";
    public static final String MENU_LAYER = "SideMenu";

    @Override
    public void init() {
        addViewFactory(LIST_VIEW, () -> new BasicListView(LIST_VIEW));
        addViewFactory(OBJECT_VIEW, () -> new BasicObjectView(OBJECT_VIEW));

        NavigationDrawer navigationDrawer = new NavigationDrawer();
        NavigationDrawer.Item listItem = new NavigationDrawer.Item("List Viewer", MaterialDesignIcon.VIEW_LIST.graphic());
        NavigationDrawer.Item objectItem = new NavigationDrawer.Item("Object Viewer", MaterialDesignIcon.INSERT_DRIVE_FILE.graphic());
        navigationDrawer.getItems().addAll(listItem, objectItem);
        navigationDrawer.selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            hideLayer(MENU_LAYER);
            if (newItem.equals(listItem)) {
                switchView(LIST_VIEW);
            } else if (newItem.equals(objectItem)) {
                switchView(OBJECT_VIEW);
            }
        });
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(navigationDrawer));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        ((Stage) scene.getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
    }
}
