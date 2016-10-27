package com.gluonhq.gonative.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.gonative.GoNative;
import com.gluonhq.gonative.GoNativePlatformFactory;
import com.gluonhq.gonative.NativeService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class VibrationPresenter {
    
    @FXML
    private View vibration;

    @FXML
    private Button vibrateButton;
    
    public void initialize() {
        final NativeService nativeService = GoNativePlatformFactory.getPlatform().getNativeService();

        vibration.setShowTransitionFactory(BounceInRightTransition::new);
        
        vibration.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(GoNative.MENU_LAYER)));
                appBar.setTitleText("Vibration");
            }
        });
        
        vibrateButton.setOnAction(e -> nativeService.vibrate());
    }    
    
}
