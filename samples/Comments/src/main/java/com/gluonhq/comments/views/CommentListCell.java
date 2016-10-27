package com.gluonhq.comments.views;

import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.comments.model.Comment;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

/**
 * Custom format for ListCells on the ListView control
 * It uses ListTile control
 */
public class CommentListCell extends ListCell<Comment> {

    private final ListTile tile = new ListTile();
    private Comment comment;
    
    {
        final Button button = MaterialDesignIcon.DELETE.button(e -> showDialog(comment));
        // Second graphic area, on the right
        tile.setSecondaryGraphic(new VBox(button));
    }
    
    /**
     * Add a ListTile control to not empty cells
     * @param item the comment on the cell
     * @param empty 
     */
    @Override
    protected void updateItem(Comment item, boolean empty) {
        super.updateItem(item, empty);
        comment = item;
        if (!empty && item != null) {
            
            // Text
            tile.textProperty().setAll(item.getAuthor(), item.getContent());
        
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
    
    /**
     * Create a Dialog for getting deletion confirmation
     * @param item 
     */
    private void showDialog(Comment item) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
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
        alert.getButtons().setAll(yes, no);
        
        Optional result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.YES)) {
            /*
            With confirmation, delete the item from the ListView. This will be
            propagated to the Cloud, and from there to the rest of the clients
            */
            listViewProperty().get().getItems().remove(item);
        }
    }

}
