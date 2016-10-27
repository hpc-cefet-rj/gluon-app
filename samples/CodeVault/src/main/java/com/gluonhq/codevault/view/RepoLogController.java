package com.gluonhq.codevault.view;

import com.gluonhq.codevault.git.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RepoLogController {

    @FXML
    private TreeView<GitRef> info;

    @FXML
    private TableView<GitCommit> table;

    @FXML
    private TableColumn<GitCommit, String> tcCommit;

    @FXML
    private TableColumn<GitCommit, GitCommit> tcDescription;

    @FXML
    private TableColumn<GitCommit, String> tcAuthor;

    @FXML
    private TableColumn<GitCommit, Date> tcDate;

    // repositoryProperty
    private final ObjectProperty<GitRepository> repository = new SimpleObjectProperty<>(this, "repository");

    public final ObjectProperty<GitRepository> repositoryProperty() {
        return repository;
    }

    public final GitRepository getRepository() {
        return repository.get();
    }

    public final void setRepository(GitRepository value) throws GitRepoException {
        repository.set(value);
    }

    public void initialize() {
        SplitPane.setResizableWithParent(info, false);

        info.setCellFactory(tree -> new InfoTreeCell());

        tcDate.setCellValueFactory( new PropertyValueFactory<>("time"));
        tcAuthor.setCellValueFactory( new PropertyValueFactory<>("author"));
        tcCommit.setCellValueFactory( new PropertyValueFactory<>("hash"));

        tcDescription.setCellValueFactory(param -> new ReadOnlyObjectWrapper<GitCommit>(param.getValue()));
        tcDescription.setCellFactory(column -> new CommitDescriptionTableCell());

        repositoryProperty().addListener((obs, ov, repo) -> {
            if (repo != null) {
                table.getItems().setAll(repo.getLog());
                info.setRoot(createRepoInfoModel());
            } else {
                table.getItems().clear();
                info.setRoot(null);
            }
        });

        info.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv.isLeaf()) {
                table.getItems().stream()
                        .filter(commit -> commit.getRefs().size() > 0)
                        .filter(commit -> commit.getRefs().get(0).getShortName().equals(nv.getValue().getShortName()))
                        .findFirst().ifPresent(commit -> {
                            table.getSelectionModel().select(commit);
                            table.scrollTo(commit);
                        });
            }
        });
    }

    private TreeItem<GitRef> createRepoInfoModel() {

        GitRepository repo = getRepository();

        TreeItem<GitRef> branches = createTopicFromElements("Branches", repo.getBranches(), true);

        List<TreeItem<GitRef>> topics = Arrays.asList(
                branches,
                createTopicFromElements("Tags", repo.getTags(), false));

        return createTopicFromTreeItems(repo.getName(), topics, true);
    }

    private static TreeItem<GitRef> createTopicFromElements(String name,
                                                            Collection<? extends GitRef> children,
                                                            boolean expand) {
        return createTopicFromTreeItems(name,
                children.stream()
                        .map(c -> new TreeItem<>(c))
                        .sorted((o1, o2) -> o2.getValue().getShortName().compareTo(o1.getValue().getShortName()))
                        .collect(Collectors.toList()),
                expand);
    }

    private static TreeItem<GitRef> createTopicFromTreeItems(String name,
                                                             Collection<TreeItem<GitRef>> children,
                                                             Boolean expand) {
        TreeItem<GitRef> result = new TreeItem<>(new Topic(name));
        result.setExpanded(expand);
        result.getChildren().addAll(children);
        return result;
    }
}
