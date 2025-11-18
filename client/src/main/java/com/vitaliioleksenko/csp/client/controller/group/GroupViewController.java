package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.model.Group;
import com.vitaliioleksenko.csp.client.util.UserSession;

// import com.example.csp.service.GroupService;
import com.vitaliioleksenko.csp.client.service.GroupService;
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

public class GroupViewController {

    @FXML private TextField searchFilterField;
    @FXML private ListView<Group> groupListView;

    private final GroupService groupService = new GroupService();
    private final UserSession session = UserSession.getInstance();

    private Consumer<Integer> navigationCallback;

    private final ObservableList<Group> allGroups = FXCollections.observableArrayList();
    private FilteredList<Group> filteredGroups;

    public void setNavigationCallback(Consumer<Integer> navigationCallback) {
        this.navigationCallback = navigationCallback;
    }

    @FXML
    public void initialize() {
        setupListViewFactory();
        setupNavigation();
        setupSearchFilter();
        loadDataBasedOnRole();
    }

    private void loadDataBasedOnRole() {
        boolean isAdmin = session.getCurrentUserRole().equals("ROLE_ADMIN");
        List<Group> groups;
        try {
            if (isAdmin) {
                groups = groupService.getGroups(null);
            } else {
                groups = groupService.getGroups(session.getCurrentUserId());
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        allGroups.setAll(groups);
        filteredGroups = new FilteredList<>(allGroups, p -> true);
        groupListView.setItems(filteredGroups);
    }

    private void setupSearchFilter() {
        searchFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (filteredGroups != null) {
                filteredGroups.setPredicate(group -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return group.getName().toLowerCase().contains(lowerCaseFilter);
                });
            }
        });
    }

    private void setupNavigation() {
        groupListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && navigationCallback != null) {
                Group selectedGroup = groupListView.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    navigationCallback.accept(selectedGroup.getGroupId());
                }
            }
        });
    }

    private void setupListViewFactory() {
        groupListView.setCellFactory(lv -> new GroupListCell());
    }

    static class GroupListCell extends ListCell<Group> {
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
        protected void updateItem(Group group, boolean empty) {
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
