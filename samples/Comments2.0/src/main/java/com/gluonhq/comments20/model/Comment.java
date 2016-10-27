package com.gluonhq.comments20.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Simple POJO 
 */
public class Comment {

    // name of user
    private final StringProperty author = new SimpleStringProperty();
    // content of comment
    private final StringProperty content = new SimpleStringProperty();
    // url to user image
    private final StringProperty imageUrl = new SimpleStringProperty();
    // user network id, to identify author of comment
    private final StringProperty networkId = new SimpleStringProperty();

    public Comment() {
    }

    public Comment(String author, String content, String imageUrl, String networkId) {
        this.author.set(author);
        this.content.set(content);
        this.imageUrl.set(imageUrl);
        this.networkId.set(networkId);
    }

    public StringProperty authorProperty() {
        return author;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty contentProperty() {
        return content;
    }

    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);

    }
    
    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    public String getImageUrl() {
        return imageUrl.get();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }
    
    public StringProperty networkIdProperty() {
        return networkId;
    }

    public String getNetworkId() {
        return networkId.get();
    }

    public void setNetworkId(String networkId) {
        this.networkId.set(networkId);
    }
    
    @Override
    public String toString() {
        return author.get()+": "+content.get();
    }
    
}
