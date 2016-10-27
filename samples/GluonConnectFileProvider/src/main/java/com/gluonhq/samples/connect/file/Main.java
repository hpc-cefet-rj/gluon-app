package com.gluonhq.samples.connect.file;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.StorageService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends MobileApplication {

    public static final File ROOT_DIR; 
    
    static {
        ROOT_DIR = Services.get(StorageService.class)
                    .flatMap(StorageService::getPrivateStorage)
                    .orElseThrow(() -> new RuntimeException("Error retrieving private storage"));
    }
    
    public static final String FILELIST_VIEW = HOME_VIEW;
    public static final String FILEOBJECT_VIEW = "FileObjectView";
    public static final String MENU_LAYER = "SideMenu";

    public Main() {
        try {
            File languagesFile = new File(ROOT_DIR, "languages.json");
            if (!languagesFile.exists()) {
                try (FileWriter writer = new FileWriter(languagesFile)) {
                    writer.write("[\n" +
                            "  {\"name\":\"Java\",\"ratings\":20.956},\n" +
                            "  {\"name\":\"C\",\"ratings\":13.223},\n" +
                            "  {\"name\":\"C++\",\"ratings\":6.698},\n" +
                            "  {\"name\":\"C#\",\"ratings\":4.481},\n" +
                            "  {\"name\":\"Python\",\"ratings\":3.789}\n" +
                            "]");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File userFile = new File(ROOT_DIR, "user.json");
            if (!userFile.exists()) {
                try (FileWriter writer = new FileWriter(userFile)) {
                    writer.write("{\"name\":\"Duke\",\"subscribed\":true}");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void init() {
        addViewFactory(FILELIST_VIEW, () -> {
            try {
                return new FileListView(FILELIST_VIEW);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        addViewFactory(FILEOBJECT_VIEW, () -> {
            try {
                return new FileObjectView(FILEOBJECT_VIEW);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });

        NavigationDrawer navigationDrawer = new NavigationDrawer();
        NavigationDrawer.Item listItem = new NavigationDrawer.Item("List Viewer", MaterialDesignIcon.VIEW_LIST.graphic());
        NavigationDrawer.Item objectItem = new NavigationDrawer.Item("Object Viewer", MaterialDesignIcon.INSERT_DRIVE_FILE.graphic());
        navigationDrawer.getItems().addAll(listItem, objectItem);
        navigationDrawer.selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            hideLayer(MENU_LAYER);
            if (newItem.equals(listItem)) {
                switchView(FILELIST_VIEW);
            } else if (newItem.equals(objectItem)) {
                switchView(FILEOBJECT_VIEW);
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
