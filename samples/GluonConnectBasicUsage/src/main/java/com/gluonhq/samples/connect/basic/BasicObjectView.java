package com.gluonhq.samples.connect.basic;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.converter.InputStreamInputConverter;
import com.gluonhq.connect.converter.JsonInputConverter;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.InputStreamObjectDataReader;
import com.gluonhq.connect.provider.ObjectDataReader;
import com.gluonhq.connect.source.BasicInputDataSource;
import com.gluonhq.connect.source.InputDataSource;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class BasicObjectView extends View {

    public BasicObjectView(String name) {
        super(name);

        Label lbName = new Label();
        CheckBox cbSubscribed = new CheckBox("Subscribed?");

        GridPane gridPane = new GridPane();
        gridPane.addRow(0, new Label("Name:"), lbName);
        gridPane.addRow(1, cbSubscribed);

        setCenter(gridPane);

        // create a DataSource that loads data from a classpath resource
        InputDataSource dataSource = new BasicInputDataSource(Main.class.getResourceAsStream("/user.json"));

        // create a Converter that converts a json object into a java object
        InputStreamInputConverter<User> converter = new JsonInputConverter<>(User.class);

        // create an ObjectDataReader that will read the data from the DataSource and converts
        // it from json into an object
        ObjectDataReader<User> objectDataReader = new InputStreamObjectDataReader<>(dataSource, converter);

        // retrieve an object from the DataProvider
        GluonObservableObject<User> user = DataProvider.retrieveObject(objectDataReader);

        // when the object is initialized, bind its properties to the JavaFX UI controls
        user.initializedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                lbName.textProperty().bind(user.get().nameProperty());
                cbSubscribed.selectedProperty().bindBidirectional(user.get().subscribedProperty());
            }
        });
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("Object Viewer");
    }

}
