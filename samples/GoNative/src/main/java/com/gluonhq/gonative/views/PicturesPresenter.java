package com.gluonhq.gonative.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CardPane;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.gonative.GoNative;
import com.gluonhq.gonative.GoNativePlatformFactory;
import com.gluonhq.gonative.NativeService;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class PicturesPresenter {

    @FXML
    private View pictures;
    
    @FXML
    private CardPane cardPane;
    
    private ImageView imageView;

    public void initialize() {
        final NativeService nativeService = GoNativePlatformFactory.getPlatform().getNativeService();

        pictures.setShowTransitionFactory(BounceInRightTransition::new);
        
        pictures.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(GoNative.MENU_LAYER)));
                appBar.setTitleText("Picture Service");
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.CAMERA.button(e -> nativeService.takePicture()),
                        MaterialDesignIcon.COLLECTIONS.button(e -> nativeService.retrievePicture()));
            }
        });
        
        imageView = new ImageView();
        imageView.setImage(nativeService.imageProperty().get());
        imageView.fitWidthProperty().bind(pictures.widthProperty().subtract(60));
        imageView.fitHeightProperty().bind(pictures.heightProperty().subtract(60));
        imageView.setPreserveRatio(true);
        
        cardPane.getCards().add(imageView);
        
        nativeService.imageProperty().addListener((obs, ov, nv) -> imageView.setImage(nv));
    }
    
}
