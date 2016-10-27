package com.gluonhq.samples.connect.rest;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.converter.InputStreamInputConverter;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class RestObjectView extends View {

    public RestObjectView(String name) {
        super(name);

        Label lbQuestionId = new Label();
        Label lbTitle = new Label();
        Label lbLink = new Label();

        GridPane gridPane = new GridPane();
        gridPane.addRow(0, new Label("Question ID:"), lbQuestionId);
        gridPane.addRow(1, new Label("Title:"), lbTitle);
        gridPane.addRow(2, new Label("Link:"), lbLink);
        gridPane.getColumnConstraints().add(new ColumnConstraints(75));

        setCenter(gridPane);

        // create a RestClient to the specific URL
        RestClient restClient = RestClient.create()
                .method("GET")
                .host("https://api.stackexchange.com")
                .path("/2.2/questions/36243147")
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .queryParam("site", "stackoverflow");

        // create a custom Converter that is able to parse the response into a single object
        InputStreamInputConverter<Question> converter = new SingleItemInputConverter<>(Question.class);

        // retrieve an object from the DataProvider
        GluonObservableObject<Question> question = DataProvider.retrieveObject(restClient.createObjectDataReader(converter));

        // when the object is initialized, bind its properties to the JavaFX UI controls
        question.initializedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                lbQuestionId.textProperty().bind(question.get().questionIdProperty().asString());
                lbTitle.textProperty().bind(question.get().titleProperty());
                lbLink.textProperty().bind(question.get().linkProperty());
            }
        });
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("Rest Object Viewer");
    }

}
