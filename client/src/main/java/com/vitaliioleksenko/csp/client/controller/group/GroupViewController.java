package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.group.GroupCreate;
import com.vitaliioleksenko.csp.client.model.group.GroupPartial;
import com.vitaliioleksenko.csp.client.util.UserSession;

// import com.example.csp.service.GroupService;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GroupViewController {

    @FXML private TextField searchFilterField;
    @FXML private ListView<GroupPartial> groupListView;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;
    @FXML private Button createGroupButton;
    @Setter private Consumer<Void> createGroupCallback;

    @Setter private Consumer<Integer> navigationCallback;

    private final ObservableList<GroupPartial> allGroups = FXCollections.observableArrayList();

    private final GroupService groupService;
    private final UserSession session;
    private final boolean isAdmin;

    private int currentPage = 0;
    private final int pageSize = 10;
    private int totalPages = 0;

    public GroupViewController() {
        this.groupService = new GroupService();
        this.session = UserSession.getInstance();
        this.isAdmin = session.getCurrentUserRole() == Role.ROLE_ADMIN;
    }

    @FXML public void initialize() {
        setupListViewFactory();
        setupNavigation();
        setupSearchFilter();
        loadGroupsBasedOnRole(null);
    }

    @FXML private void onPrevPage() {
        if (currentPage > 0) {
            currentPage--;
            loadGroupsBasedOnRole(searchFilterField.getText());
        }
    }

    @FXML private void onNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadGroupsBasedOnRole(searchFilterField.getText());
        }
    }

    @FXML private void onCreateGroupClicked() {
        if (createGroupCallback != null) {
            createGroupCallback.accept(null);
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadGroupsBasedOnRole(String search) {
        PageResponse<GroupPartial> groupsPage;
        try {
            String finalSearch = (search != null && search.trim().isEmpty()) ? null : search;
            if (isAdmin) {
                groupsPage = groupService.getGroups(finalSearch, null, currentPage, pageSize);
            } else {
                groupsPage = groupService.getGroups(finalSearch, session.getCurrentUserId(), currentPage, pageSize);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        totalPages = groupsPage.getTotalPages();
        allGroups.setAll(groupsPage.getContent());
        groupListView.setItems(allGroups);

        updatePaginationControls();
    }

    private void setupSearchFilter() {
        searchFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            currentPage = 0;
            loadGroupsBasedOnRole(newValue);
        });
    }

    private void updatePaginationControls() {
        pageLabel.setText(String.format("Page %d of %d", currentPage + 1, Math.max(1, totalPages)));

        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage >= totalPages - 1);
    }

    private void setupNavigation() {
        groupListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && navigationCallback != null) {
                GroupPartial selectedGroup = groupListView.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    navigationCallback.accept(selectedGroup.getGroupId());
                }
            }
        });
    }

    private void setupListViewFactory() {
        groupListView.setCellFactory(lv -> new GroupListCell());
    }

    static class GroupListCell extends ListCell<GroupPartial> {
        private Node cardNode;
        private GroupCardController cardController;

        public GroupListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/group/group-card.fxml"));
                cardNode = loader.load();
                cardController = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void updateItem(GroupPartial group, boolean empty) {
            super.updateItem(group, empty);
            if (empty || group == null) {
                setGraphic(null);
            } else {
                cardController.setData(group);
                setGraphic(cardNode);
            }
        }
    }
}
