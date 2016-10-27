package com.gluonhq.codevault.view;

import com.gluonhq.codevault.git.GitRef;
import com.gluonhq.codevault.git.Topic;
import com.gluonhq.codevault.util.UITools;
import javafx.scene.control.TreeCell;

public class InfoTreeCell extends TreeCell<GitRef> {

    private final String lastStyle = "topic";

    {
        getStyleClass().add("repoViewCell");
    }

    @Override
    protected void updateItem(GitRef ref, boolean empty) {
        super.updateItem(ref, empty);
        if (ref == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (ref instanceof Topic) {
                if (!getStyleClass().contains(lastStyle)) {
                    getStyleClass().add(lastStyle);
                }
            } else {
                if (getStyleClass().contains(lastStyle)) {
                    getStyleClass().remove(lastStyle);
                }
            }

            setText(ref.getShortName());
            setGraphic(UITools.getRefIcon(ref));
        }
    }
}
