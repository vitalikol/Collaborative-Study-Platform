package com.vitaliioleksenko.csp.client.controller.task;

import com.vitaliioleksenko.csp.client.model.Group;
import com.vitaliioleksenko.csp.client.model.Task;
import com.vitaliioleksenko.csp.client.model.User;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.TaskService;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer; // НОВИЙ ІМПОРТ

public class TaskViewController {
    @FXML private ComboBox<Group> teamFilterCombo;
    @FXML private Label userFilterLabel;
    @FXML private ComboBox<User> userFilterCombo;
    @FXML private Button applyFilterButton;
    @FXML private Button clearFilterButton;
    @FXML private ListView<Task> taskListView;

    @Setter
    private Consumer<Task> navigationCallback;

    private ObservableList<Task> taskData = FXCollections.observableArrayList();

    private final TaskService taskService;
    private final GroupService groupService;
    private final UserService userService;

    private UserSession session = UserSession.getInstance();
    private boolean isAdmin;
    private int currentUserId;

    public TaskViewController() {
        taskService = new TaskService();
        groupService = new GroupService();
        userService = new UserService();
    }

    @FXML
    public void initialize() {
        setupComboBoxes();
        if (!session.isLoggedIn()) {
            return;
        }
        this.isAdmin = session.getCurrentUserRole().equals("ROLE_ADMIN");
        this.currentUserId = session.getCurrentUserId();

        setupFilters();

        loadTasks(null, null);
        taskListView.setItems(taskData);
        taskListView.setCellFactory(listView -> new TaskCell());

        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && navigationCallback != null) {
                Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    navigationCallback.accept(selectedTask);
                }
            }
        });
    }

    @FXML
    private void handleFilterAction() {
        Group selectedGroup = teamFilterCombo.getValue();
        Integer teamId = (selectedGroup != null) ? selectedGroup.getGroupId() : null;

        Integer userId = null;
        if (isAdmin) {
            User selectedUser = userFilterCombo.getValue();
            userId = (selectedUser != null) ? selectedUser.getUserId() : null;
        } else {
            userId = this.currentUserId;
        }

        loadTasks(teamId, userId);
    }

    @FXML
    private void handleClearFilterAction() {
        teamFilterCombo.setValue(null);
        if (isAdmin) {
            userFilterCombo.setValue(null);
        }
        loadTasks(null, null);
    }

    private void setupComboBoxes() {
        teamFilterCombo.setCellFactory(lv -> new ListCell<Group>() {
            @Override
            protected void updateItem(Group group, boolean empty) {
                super.updateItem(group, empty);
                setText(empty || group == null ? "All Groups" : group.getName());
            }
        });
        teamFilterCombo.setButtonCell(teamFilterCombo.getCellFactory().call(null));

        userFilterCombo.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? "All users" : user.getName() + " (" + user.getRole() + ")");
            }
        });
        userFilterCombo.setButtonCell(userFilterCombo.getCellFactory().call(null));
    }

    private void setupFilters() {
        if (isAdmin) {
            userFilterLabel.setVisible(true);
            userFilterLabel.setManaged(true);
            userFilterCombo.setVisible(true);
            userFilterCombo.setManaged(true);

            try {
                List<Group> groups = groupService.getGroups(null);
                teamFilterCombo.setItems(FXCollections.observableArrayList(groups));
                List<User> users = userService.getUsers();
                userFilterCombo.setItems(FXCollections.observableArrayList(users));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                List<Group> groups = groupService.getGroups(this.currentUserId);
                teamFilterCombo.setItems(FXCollections.observableArrayList(groups));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadTasks(Integer teamId, Integer userId) {
        if (!isAdmin) {
            userId = this.currentUserId;
        }
        try{
            List<Task> tasks = taskService.getTasks(userId, teamId);
            taskListView.setItems(FXCollections.observableArrayList(tasks));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class TaskCell extends ListCell<Task> {
        private Node cardNode;
        private TaskCardController cardController;

        public TaskCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task/task-card.fxml"));
                cardNode = loader.load();
                cardController = loader.getController();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                cardController.setData(task);
                setGraphic(cardNode);
            }
        }
    }
}