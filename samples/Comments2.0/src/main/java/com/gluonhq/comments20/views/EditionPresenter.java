package com.gluonhq.comments20.views;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.DisplayService;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import static com.gluonhq.charm.glisten.application.MobileApplication.HOME_VIEW;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.comments20.Comments20;
import com.gluonhq.comments20.cloud.Service;
import com.gluonhq.comments20.model.Comment;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
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
    private Avatar avatar;
    
    @FXML
    private TextField authorText;

    @FXML
    private TextArea commentsText;

    @FXML
    private Button submit;

    private boolean editMode;

    public void initialize() {
        edition.setShowTransitionFactory(BounceInRightTransition::new);
        PseudoClass pseudoClassDisable = PseudoClass.getPseudoClass("disabled");
                
        edition.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                
                submit.disableProperty().unbind();
                    
                Comment activeComment = service.activeCommentProperty().get();
                if (activeComment != null) {
                    // disable avatar in case comment is not editable
                    // content is enabled if user is its author
                    commentsText.setDisable(!activeComment.getNetworkId().equals(service.getUser().getNetworkId()));
                    avatar.pseudoClassStateChanged(pseudoClassDisable, commentsText.isDisable());
                    avatar.setImage(Service.getUserImage(activeComment.getImageUrl()));
                
                    authorText.setText(activeComment.getAuthor());
                    commentsText.setText(activeComment.getContent());
                    
                    submit.setText("Apply");
                    submit.disableProperty().bind(Bindings.createBooleanBinding(()->{
                        return authorText.textProperty()
                                .isEqualTo(activeComment.getAuthor())
                                .and(commentsText.textProperty()
                                        .isEqualTo(activeComment.getContent())).get();
                        }, authorText.textProperty(),commentsText.textProperty()));
                    editMode = true;
                } else {
                    commentsText.setDisable(false);
                    avatar.pseudoClassStateChanged(pseudoClassDisable, false);
                    avatar.setImage(Service.getUserImage(service.getUser().getPicture()));
                    authorText.setText(service.getUser().getName());
                    
                    submit.setText("Submit");
                    submit.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                            return authorText.textProperty()
                                    .isEmpty()
                                    .or(commentsText.textProperty()
                                            .isEmpty()).get();
                        }, authorText.textProperty(), commentsText.textProperty()));
                    editMode = false;
                }
                
                authorText.setDisable(!authorText.getText().isEmpty());
                
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(Comments20.MENU_LAYER)));
                appBar.setTitleText("Edition");
            } else {
                authorText.clear();
                commentsText.clear();
            }
        });
        
        Services.get(DisplayService.class)
                .ifPresent(d -> {
                    if (d.isTablet()) {
                        avatar.getStyleClass().add("tablet");
                    }
                });
        avatar.setImage(Service.getUserImage(service.getUser().getPicture()));
    }
    
    @FXML
    void onCancel(ActionEvent event) {
        authorText.clear();
        commentsText.clear();
        service.activeCommentProperty().set(null);
        MobileApplication.getInstance().switchView(HOME_VIEW);
    }

    @FXML
    void onSubmit(ActionEvent event) {
        Comment comment = editMode ? 
                service.activeCommentProperty().get() : 
                new Comment(authorText.getText(), commentsText.getText(), 
                            service.getUser().getPicture(), service.getUser().getNetworkId());
        comment.setContent(commentsText.getText());

        if (!editMode) {
            service.addComment(comment);
        }
        
        onCancel(event);
    }

}
