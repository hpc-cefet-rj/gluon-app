package com.gluonhq.comments.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.comments.Comments;
import com.gluonhq.comments.cloud.Service;
import com.gluonhq.comments.model.Comment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javax.inject.Inject;
import static com.gluonhq.comments.Comments.EDITION_VIEW;

public class CommentsPresenter {

    @Inject 
    private Service service;
    
    @FXML
    private View comments;

    @FXML
    private ListView<Comment> commentsList;

    public void initialize() {
        comments.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(Comments.MENU_LAYER)));
                appBar.setTitleText("Comments");
            }
        });
        
        comments.getLayers().add(new FloatingActionButton(MaterialDesignIcon.ADD.text, 
            e -> MobileApplication.getInstance().switchView(EDITION_VIEW)).getLayer());
        
        commentsList.setCellFactory(p -> new CommentListCell());
        commentsList.setPlaceholder(new Label("There are no comments"));
        commentsList.setItems(service.commentsProperty());
        
        service.retrieveComments();
    }
    
}
