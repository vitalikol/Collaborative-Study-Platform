package com.vitaliioleksenko.csp.client.controller.user;

// import com.example.csp.service.UserService;
import com.vitaliioleksenko.csp.client.model.User;
import com.vitaliioleksenko.csp.client.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class UserViewController {

    @FXML private TextField searchFilterField;
    @FXML private ListView<User> userListView;

    private final UserService userService = new UserService();
    private Consumer<Integer> navigationCallback;

    private final ObservableList<User> allUsers = FXCollections.observableArrayList();
    private FilteredList<User> filteredUsers;

    public void setNavigationCallback(Consumer<Integer> navigationCallback) {
        this.navigationCallback = navigationCallback;
    }

    @FXML
    public void initialize() {
        setupListViewFactory();
        setupNavigation();
        setupSearchFilter();
        loadAllUsers();
    }

    private void loadAllUsers() {
        try {
            List<User> users = userService.getUsers();
            allUsers.setAll(users);
            filteredUsers = new FilteredList<>(allUsers, p -> true);
            userListView.setItems(filteredUsers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupSearchFilter() {
        searchFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (filteredUsers != null) {
                filteredUsers.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // Показувати все, якщо фільтр порожній
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return user.getName().toLowerCase().contains(lowerCaseFilter)
                            || user.getEmail().toLowerCase().contains(lowerCaseFilter);
                });
            }
        });
    }

    private void setupNavigation() {
        userListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && navigationCallback != null) {
                User selectedUser = userListView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    navigationCallback.accept(selectedUser.getUserId());
                }
            }
        });
    }

    private void setupListViewFactory() {
        userListView.setCellFactory(lv -> new UserListCell());
    }

    static class UserListCell extends ListCell<User> {
        private Node cardNode;
        private UserCardController cardController;

        public UserListCell() {
            try {
                // Шлях до FXML картки
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-card.fxml"));
                cardNode = loader.load();
                cardController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void updateItem(User user, boolean empty) {
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