package com.gluonhq.samples.connect.rest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlElement;

public class Question {
    private final IntegerProperty questionId = new SimpleIntegerProperty();
    private final BooleanProperty answered = new SimpleBooleanProperty();
    private final IntegerProperty viewCount = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty link = new SimpleStringProperty();

    @XmlElement(name = "question_id")
    public int getQuestionId() {
        return questionId.get();
    }

    public IntegerProperty questionIdProperty() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId.set(questionId);
    }

    @XmlElement(name = "is_answered")
    public boolean getAnswered() {
        return answered.get();
    }

    public BooleanProperty answeredProperty() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered.set(answered);
    }

    @XmlElement(name = "view_count")
    public int getViewCount() {
        return viewCount.get();
    }

    public IntegerProperty viewCountProperty() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount.set(viewCount);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getLink() {
        return link.get();
    }

    public StringProperty linkProperty() {
        return link;
    }

    public void setLink(String link) {
        this.link.set(link);
    }
}
