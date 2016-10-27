package com.gluonhq.codevault.view;

import com.gluonhq.codevault.git.GitBranch;
import com.gluonhq.codevault.git.GitCommit;
import com.gluonhq.codevault.git.GitRef;
import com.gluonhq.codevault.git.GitTag;
import com.gluonhq.codevault.util.UITools;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

import java.util.stream.Collectors;

public class CommitDescriptionTableCell extends TableCell<GitCommit, GitCommit> {

    private HBox labels = new HBox(1);

    @Override
    protected void updateItem(GitCommit commit, boolean empty) {
        super.updateItem(commit, empty);
        if (commit == null || empty ) {
            setText(null);
            setGraphic(null);
            setTooltip(null);
        } else {
            setText(commit.getShortMessage());
            setTooltip(new Tooltip(commit.getFullMessage()));
            labels.getChildren().setAll(
                    commit.getRefs().stream()
                            .map(this::makeRefLabel)
                            .collect(Collectors.toList())
            );
            setGraphic( labels.getChildren().isEmpty()? null: labels);
        }
    }

    private Label makeRefLabel(GitRef ref) {
        Label refLabel = new Label(ref.getShortName());
        refLabel.setGraphic(UITools.getRefIcon(ref));
        if (ref instanceof GitTag) {
            refLabel.getStyleClass().add("tag-ref");
        } else if (ref instanceof GitBranch) {
            refLabel.getStyleClass().add("branch-ref");
        } else {
            refLabel.getStyleClass().add("unknown-ref");
        }
        return refLabel;
    }
}
