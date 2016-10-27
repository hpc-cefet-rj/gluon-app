package com.gluonhq.samples.connect.file;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.converter.InputStreamInputConverter;
import com.gluonhq.connect.converter.JsonInputConverter;
import com.gluonhq.connect.converter.JsonOutputConverter;
import com.gluonhq.connect.converter.OutputStreamOutputConverter;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.FileClient;
import static com.gluonhq.samples.connect.file.Main.ROOT_DIR;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.lang.*;

public class FileObjectView extends View {

    public FileObjectView(String name) throws IOException {
        super(name);

        Label lbName = new Label();
        CheckBox cbSubscribed = new CheckBox("Subscribed?");

        GridPane gridPane = new GridPane();
        gridPane.addRow(0, new Label("Name:"), lbName);
        gridPane.addRow(1, cbSubscribed);

        setCenter(gridPane);

        // create a FileClient to the specified file
        FileClient fileClient = FileClient.create(new File(ROOT_DIR, "user.json"));

        // create a JSON converter that converts a JSON object into a user object
        InputStreamInputConverter<User> converter = new JsonInputConverter<>(User.class);

        // retrieve an object from an ObjectDataReader created from the FileClient
        GluonObservableObject<User> user = DataProvider.retrieveObject(fileClient.createObjectDataReader(converter));

        // when the object is initialized, bind its properties to the JavaFX UI controls
        user.initializedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                lbName.textProperty().bind(user.get().nameProperty());
                cbSubscribed.selectedProperty().bindBidirectional(user.get().subscribedProperty());
            }
        });

        // write user to file when selected property of the subscribed checkbox is changed
        cbSubscribed.selectedProperty().addListener((obs, ov, nv) -> {
            user.get().setSubscribed(nv);

            // create a JSON converter that converts the user object into a JSON object
            OutputStreamOutputConverter<User> outputConverter = new JsonOutputConverter<>(User.class);

            // store an object with an ObjectDataWriter created from the FileClient
            DataProvider.storeObject(user.get(), fileClient.createObjectDataWriter(outputConverter));
        });
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("File Object Viewer");
    }

}
