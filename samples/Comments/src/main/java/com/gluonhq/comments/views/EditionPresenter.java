package com.gluonhq.comments.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import static com.gluonhq.charm.glisten.application.MobileApplication.HOME_VIEW;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.comments.Comments;
import com.gluonhq.comments.cloud.Service;
import com.gluonhq.comments.model.Comment;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.inject.Inject;

public class EditionPresenter {

    @Inject 
    private Service service;
    
    @FXML
    private View edition;

    @FXML
    private TextField authorText;

    @FXML
    private TextArea commentsText;

    @FXML
    private Button submit;

    public void initialize() {
        edition.setShowTransitionFactory(BounceInRightTransition::new);
        
        edition.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(Comments.MENU_LAYER)));
                appBar.setTitleText("Edition");
            }
        });
        
        submit.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                return authorText.textProperty().isEmpty()
                        .or(commentsText.textProperty().isEmpty()).get();
            }, authorText.textProperty(), commentsText.textProperty()));
    }
    
    @FXML
    void onCancel(ActionEvent event) {
        authorText.setText("");
        commentsText.setText("");
        MobileApplication.getInstance().switchView(HOME_VIEW);
    }

    @FXML
    void onSubmit(ActionEvent event) {
        service.addComment(new Comment(authorText.getText(), commentsText.getText()));
        authorText.setText("");
        commentsText.setText("");
        MobileApplication.getInstance().switchView(HOME_VIEW);
    }

}
