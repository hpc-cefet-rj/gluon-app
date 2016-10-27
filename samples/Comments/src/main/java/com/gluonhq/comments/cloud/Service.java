package com.gluonhq.comments.cloud;

import com.gluonhq.comments.model.Comment;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.ConnectState;
import com.gluonhq.connect.gluoncloud.GluonClient;
import com.gluonhq.connect.provider.DataProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javax.annotation.PostConstruct;

/** Service to access the application on Gluon Cloud, retrieve a list with comments
 * and send new comments to the list.
 * 
 */
public class Service {

    private GluonClient gluonClient;
    
    /*
    Every list stored under the same application on the Cloud has a unique id:
    */
    private static final String CLOUD_LIST_ID = "comments";
    
    /*
    An observable wrapper of the retrieved list, used to expose it and bind the 
    ListView items to the list.
    */
    private final ListProperty<Comment> commentsList = 
            new SimpleListProperty<>(FXCollections.<Comment>observableArrayList()); 
    
    /**
     * See Afterburner.fx
     */
    @PostConstruct
    public void postConstruct() {
        gluonClient = GluonClientProvider.getGluonClient();
    }
    
    /**
     * Once there's a valid gluonClient, the contents of the list can be retrieved. This will return a 
     * GluonObservableList, using the default flags:
     * - LIST_WRITE_THROUGH: Changes in the local list are reflected to the remote copy of that list on Gluon Cloud.
     * - LIST_READ_THROUGH: Changes in the remote list on Gluon Cloud are reflected to the local copy of that list
     *
     * This means that any change done in any client app will be reflected in the cloud, and inmediatelly broadcasted
     * to all the listening applications.
     */
    public void retrieveComments() {
        GluonObservableList<Comment> retrieveList = DataProvider.<Comment>retrieveList(
                gluonClient.createListDataReader(CLOUD_LIST_ID, Comment.class));
        
        retrieveList.stateProperty().addListener((obs, ov, nv) -> {
            if (ConnectState.SUCCEEDED.equals(nv)) {
                commentsList.set(retrieveList);
            }
        });
        
    }
    
    /**  
     * Add a new comment to the list
     * Note comments can be deleted directly on the ListView, since its bounded to the list
     * @param comment
     */
    public void addComment(Comment comment) {
        commentsList.get().add(comment);
    }
    
    /**
     *
     * @return a ListProperty, the wrapper of the remote list of comments.
     */
    public ListProperty<Comment> commentsProperty() {
        return commentsList;
    }
    
}
