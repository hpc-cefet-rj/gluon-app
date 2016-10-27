package com.gluonhq.samples.connect.basic;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.converter.InputStreamIterableInputConverter;
import com.gluonhq.connect.converter.JsonIterableInputConverter;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.InputStreamListDataReader;
import com.gluonhq.connect.provider.ListDataReader;
import com.gluonhq.connect.source.BasicInputDataSource;
import com.gluonhq.connect.source.InputDataSource;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class BasicListView extends View {

    public BasicListView(String name) {
        super(name);

        // create a DataSource that loads data from a classpath resource
        InputDataSource dataSource = new BasicInputDataSource(Main.class.getResourceAsStream("/languages.json"));

        // create a Converter that converts a json array into a list
        InputStreamIterableInputConverter<Language> converter = new JsonIterableInputConverter<>(Language.class);

        // create a ListDataReader that will read the data from the DataSource and converts
        // it from json into a list of objects
        ListDataReader<Language> listDataReader = new InputStreamListDataReader<>(dataSource, converter);

        // retrieve a list from the DataProvider
        GluonObservableList<Language> programmingLanguages = DataProvider.retrieveList(listDataReader);

        // create a JavaFX ListView and populate it with the retrieved list
        ListView<Language> lvProgrammingLanguages = new ListView<>(programmingLanguages);
        lvProgrammingLanguages.setCellFactory(lv -> new ListCell<Language>() {
            @Override
            protected void updateItem(Language item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    setText(item.getName() + ": " + item.getRatings() + "%");
                } else {
                    setText(null);
                }
            }
        });

        setCenter(lvProgrammingLanguages);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("List Viewer");
    }
    
}
