package com.vitaliioleksenko.csp.client.controller.user;


import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.membership.MembershipCreate;
import com.vitaliioleksenko.csp.client.model.user.UserPartial;
import com.vitaliioleksenko.csp.client.service.MembershipService;
import com.vitaliioleksenko.csp.client.service.UserService;
import com.vitaliioleksenko.csp.client.util.enums.GroupRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class UserViewController {
    @FXML private TextField searchFilterField;
    @FXML private ListView<UserPartial> userListView;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;
    @Setter private Consumer<Integer> navigationCallback;

    private final ObservableList<UserPartial> usersData = FXCollections.observableArrayList();
    private final UserService userService;
    private final MembershipService membershipService;

    private boolean selectionMode = false;
    private int currentPage = 0;
    private final int pageSize = 10;
    private int totalPages = 0;
    private int targetGroupId;

    public UserViewController() {
        this.userService = new UserService();
        this.membershipService = new MembershipService();
    }

    @FXML public void initialize() {
        setupListViewFactory();
        setupNavigation();
        setupSearchFilter();
        loadUsers();
    }

    @FXML private void onPrevPage() {
        if (currentPage > 0) {
            currentPage--;
            loadUsers();
        }
    }

    @FXML private void onNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadUsers();
        }
    }

    public void enableSelectionMode(int groupId) {
        this.selectionMode = true;
        this.targetGroupId = groupId;
    }

    private void loadUsers() {
        try {
            String searchText = searchFilterField.getText();
            if (searchText != null && searchText.trim().isEmpty()) {
                searchText = null;
            }

            PageResponse<UserPartial> pageResponse = userService.getUsers(searchText, currentPage, pageSize);

            usersData.setAll(pageResponse.getContent());
            userListView.setItems(usersData);

            this.totalPages = pageResponse.getTotalPages();
            updatePaginationControls();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load users", e);
        }
    }

    private void updatePaginationControls() {
        pageLabel.setText(String.format("Page %d of %d", currentPage + 1, Math.max(1, totalPages)));

        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage >= totalPages - 1);
    }

    private void setupSearchFilter() {
        searchFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            currentPage = 0;
            loadUsers();
        });
    }

    private void setupNavigation() {
        userListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && navigationCallback != null) {
                UserPartial selectedUser = userListView.getSelectionModel().getSelectedItem();
                if (!selectionMode) {
                    navigationCallback.accept(selectedUser.getUserId());
                } else {
                    showAddToGroupConfirmation(userListView.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    private void showAddToGroupConfirmation(UserPartial user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add member");
        alert.setHeaderText("Add " + user.getName() + " to this group?");

        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(addBtn, cancelBtn);

        Stage stage = (Stage) searchFilterField.getScene().getWindow();
        alert.initOwner(stage);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == addBtn) {

            MembershipCreate membershipCreate = MembershipCreate.builder()
                    .groupId(targetGroupId)
                    .userId(user.getUserId())
                    .role(GroupRole.MEMBER)
                    .build();
            try {
                membershipService.creatMembership(membershipCreate);
            } catch (IOException e) {
                Alert alertEr = new Alert(Alert.AlertType.ERROR);
                alertEr.setTitle("Error");
                alertEr.setHeaderText(null);
                alertEr.setContentText(e.getMessage());

                alertEr.initOwner(stage);

                alertEr.showAndWait();
                return;
            }
            navigationCallback.accept(targetGroupId);
        }
    }

    private void setupListViewFactory() {
        userListView.setCellFactory(lv -> new UserListCell());
    }

    static class UserListCell extends ListCell<UserPartial> {
        private Node cardNode;
        private UserCardController cardController;

        public UserListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-card.fxml"));
                cardNode = loader.load();
                cardController = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void updateItem(UserPartial user, boolean empty) {
            super.updateItem(user, empty);
            if (empty || user == null) {
                setGraphic(null);
            } else {
                cardController.setData(user);
                setGraphic(cardNode);
            }
        }
    }
}