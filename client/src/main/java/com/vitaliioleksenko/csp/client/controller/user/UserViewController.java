package com.vitaliioleksenko.csp.client.controller.user;


import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.user.UserPartial;
import com.vitaliioleksenko.csp.client.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
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

    private int currentPage = 0;
    private final int pageSize = 10; // Кількість елементів на сторінці
    private int totalPages = 0;

    public UserViewController() {
        this.userService = new UserService();
    }

    @FXML
    public void initialize() {
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
                if (selectedUser != null) {
                    navigationCallback.accept(selectedUser.getUserId());
                }
            }
        });
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