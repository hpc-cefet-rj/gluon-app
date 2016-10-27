package com.gluonhq.gonative.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.gonative.GoNative;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainPresenter {

    @FXML
    private View main;

    @FXML
    private Label label;
    
    @FXML
    private Button picturesButton;

    @FXML
    private Button accelButton;

    @FXML
    private Button vibrationButton;

    public void initialize() {
        main.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(GoNative.MENU_LAYER)));
                appBar.setTitleText("Native Services");
            }
        });
        
        picturesButton.setOnAction(e -> MobileApplication.getInstance().switchView(GoNative.PICTURES_VIEW));
        accelButton.setOnAction(e -> MobileApplication.getInstance().switchView(GoNative.ACCEL_VIEW));
        vibrationButton.setOnAction(e -> MobileApplication.getInstance().switchView(GoNative.VIBRATION_VIEW));
    }
    
}
