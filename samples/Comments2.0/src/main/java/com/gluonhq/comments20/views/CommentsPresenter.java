package com.gluonhq.comments20.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.LifecycleEvent;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.comments20.Comments20;
import com.gluonhq.comments20.cloud.Service;
import com.gluonhq.comments20.model.Comment;
import com.gluonhq.connect.gluoncloud.User;
import java.util.Optional;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.ScrollEvent;
import javax.inject.Inject;
import static com.gluonhq.comments20.Comments20.EDITION_VIEW;

public class CommentsPresenter {

    @Inject 
    private Service service;
    
    @FXML
    private View comments;

    @FXML
    private ListView<Comment> commentsList;
    
    private final BooleanProperty sliding = new SimpleBooleanProperty();
    
    private static final String SIGN_IN_MESSAGE = "Please, sign in first\n"
                + "to gain online access\n"
                + "to Comments v2.0";
    
    private static final String NO_COMMENTS_MESSAGE = "There are no comments\n"
            + "Click on the Action button\n"
            + "to create one";
    
    public void initialize() {
        comments.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(Comments20.MENU_LAYER)));
                appBar.setTitleText("Comments");
            }
        });
        
        FloatingActionButton floatingActionButton = new FloatingActionButton();
        floatingActionButton.textProperty().bind(new When(userProperty().isNotNull())
            .then(MaterialDesignIcon.ADD.text)
            .otherwise(MaterialDesignIcon.CLOUD_DOWNLOAD.text));
        floatingActionButton.setOnAction(e -> {
            if (service.getUser() == null) { 
                service.retrieveComments();
            } else {
                MobileApplication.getInstance().switchView(EDITION_VIEW);
            }
        });
        
        comments.getLayers().add(floatingActionButton.getLayer());
        
        commentsList.setCellFactory(cell -> { 
            final CommentListCell commentListCell = new CommentListCell(
                    service,
                    // left button: delete comment, only author's comment can delete it
                    c -> {
                        if (service.getUser().getNetworkId().equals(c.getNetworkId())) {
                           showDialog(c);
                        }
                    },
                    // right button: edit comment, everybody can view it, only author can edit it
                    c -> {
                       service.activeCommentProperty().set(c);
                       MobileApplication.getInstance().switchView(Comments20.EDITION_VIEW);
                    });
            
            // notify view that cell is sliding
            sliding.bind(commentListCell.slidingProperty());
        
            return commentListCell; 
        });
        
        final Label label = new Label(SIGN_IN_MESSAGE);
        label.textProperty().bind(new When(userProperty().isNotNull())
                .then(NO_COMMENTS_MESSAGE)
                .otherwise(SIGN_IN_MESSAGE));
        commentsList.setPlaceholder(label);
        commentsList.disableProperty().bind(service.userProperty().isNull());
        commentsList.setItems(service.commentsProperty());
        
        comments.addEventHandler(LifecycleEvent.SHOWN, new EventHandler<LifecycleEvent>() {
            @Override
            public void handle(LifecycleEvent event) {
                comments.removeEventHandler(LifecycleEvent.SHOWN, this);
                service.retrieveComments();
            }
        });
        
        // block scrolling when sliding
        comments.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });
    }
    
    /**
     * Create a Dialog for getting deletion confirmation
     * @param item 
     */
    private void showDialog(Comment item) {
        Alert alert = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitleText("Confirm deletion");
        alert.setContentText("This comment will be deleted permanently.\nDo you want to continue?");
        
        Button yes = new Button("Yes, delete permanently");
        yes.setOnAction(e -> {
            alert.setResult(ButtonType.YES); 
            alert.hide();
        });
        yes.setDefaultButton(true);
        
        Button no = new Button("No");
        no.setCancelButton(true);
        no.setOnAction(e -> {
            alert.setResult(ButtonType.NO); 
            alert.hide();
        });
        alert.getButtons().setAll(yes,no);
        
        Optional result = alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.YES)){
            /*
            With confirmation, delete the item from the ListView. This will be
            propagated to the Cloud, and from here to the rest of the clients
            */
            commentsList.getItems().remove(item);
        }    
    }
    
    private ObjectProperty<User> userProperty() {
        return service.userProperty();
    }
}
