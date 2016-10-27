package com.gluonhq.codevault.actions;

import com.gluonhq.codevault.view.RepoManagerView;
import com.gluonhq.particle.annotation.ParticleActions;
import com.gluonhq.particle.application.ParticleApplication;
import com.gluonhq.particle.view.View;
import com.gluonhq.particle.view.ViewManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.inject.Inject;

import javafx.stage.DirectoryChooser;
import org.controlsfx.control.action.ActionProxy;

import java.util.Optional;

@ParticleActions
public class MenuActions {

    @Inject
    ParticleApplication app;

    @Inject private ViewManager viewManager;

    @ActionProxy(text="Exit",
                 graphic="font>github-octicons|SIGN_OUT",
                 accelerator="alt+F4")
    private void exit() {
        app.exit();
    }
    
    @ActionProxy(text="About",
            graphic="font>github-octicons|MARK_GITHUB",
            accelerator="ctrl+A")
    private void about() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("CodeVault");
        alert.setHeaderText("About CodeVault");
        alert.setGraphic(new ImageView(new Image(MenuActions.class.getResource("/icon.png").toExternalForm(), 48, 48, true, true)));
        alert.setContentText("This is a Gluon Desktop Application that creates a simple Git Repository");
        alert.showAndWait();
    }

    @ActionProxy(
            text="Open Repository",
            graphic="font>FontAwesome|FOLDER_OPEN",
            accelerator="ctrl+O")
    private void openRepo() {
        View currentView = viewManager.getCurrentView();

        if (currentView instanceof RepoManagerView) {
            RepoManagerView view = (RepoManagerView) currentView;
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Open Git Repository");
            Optional.ofNullable(dirChooser.showDialog(app.getPrimaryStage())).ifPresent(view::openRepo);
        }
    }
    
}