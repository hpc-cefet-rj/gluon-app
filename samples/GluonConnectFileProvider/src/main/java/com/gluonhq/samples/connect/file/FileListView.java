package com.gluonhq.samples.connect.file;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.converter.InputStreamIterableInputConverter;
import com.gluonhq.connect.converter.JsonIterableInputConverter;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.FileClient;
import static com.gluonhq.samples.connect.file.Main.ROOT_DIR;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;

public class FileListView extends View {

    public FileListView(String name) throws IOException {
        super(name);

        File languagesFile = new File(ROOT_DIR, "languages.json");

        // create a FileClient to the specified file
        FileClient fileClient = FileClient.create(languagesFile);

        // create a JSON converter that converts the nodes from a JSON array into language objects
        InputStreamIterableInputConverter<Language> converter = new JsonIterableInputConverter<>(Language.class);

        // retrieve a list from a ListDataReader created from the FileClient
        GluonObservableList<Language> languages = DataProvider.retrieveList(fileClient.createListDataReader(converter));

        // create a JavaFX ListView and populate it with the retrieved list
        ListView<Language> lvLanguages = new ListView<>(languages);
        lvLanguages.setCellFactory(lv -> new ListCell<Language>() {
            @Override
            protected void updateItem(Language item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    setText(item.getName() + " - " + item.getRatings());
                } else {
                    setText(null);
                }
            }
        });

        setCenter(lvLanguages);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("File List Viewer");
    }

}
