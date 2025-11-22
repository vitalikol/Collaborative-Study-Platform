package com.vitaliioleksenko.csp.client.controller.task;

import com.vitaliioleksenko.csp.client.model.group.GroupDetailed;
import com.vitaliioleksenko.csp.client.model.group.GroupPartial;
import com.vitaliioleksenko.csp.client.model.task.TaskPartial;
import com.vitaliioleksenko.csp.client.model.user.UserPartial;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.TaskService;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.service.UserService;
import com.vitaliioleksenko.csp.client.util.enums.GroupRole;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class TaskViewController {
    public enum TaskViewMode {
        ACTIVE,
        ARCHIVE
    }

    @FXML private ComboBox<GroupPartial> teamFilterCombo;
    @FXML private Label userFilterLabel;
    @FXML private ComboBox<UserPartial> userFilterCombo;
    @FXML private Button applyFilterButton;
    @FXML private Button createTaskButton;
    @FXML private Button clearFilterButton;
    @FXML private ListView<TaskPartial> taskListView;
    @Setter private Consumer<Void> createTaskCallback;
    @Setter private Consumer<TaskPartial> navigationCallback;

    private ObservableList<TaskPartial> taskData = FXCollections.observableArrayList();

    private final TaskService taskService;
    private final GroupService groupService;
    private final UserService userService;
    private final UserSession session = UserSession.getInstance();
    private final boolean isAdmin;
    private final int currentUserId;

    private TaskViewMode currentMode = TaskViewMode.ACTIVE;

    public TaskViewController() {
        taskService = new TaskService();
        groupService = new GroupService();
        userService = new UserService();
        this.isAdmin = session.getCurrentUserRole() == Role.ROLE_ADMIN;
        this.currentUserId = session.getCurrentUserId();
    }

    public void setViewMode(TaskViewMode mode) {
        this.currentMode = mode;
        if(currentMode == TaskViewMode.ARCHIVE){
            createTaskButton.setVisible(false);
            createTaskButton.setManaged(false);
        }
        handleFilterAction();
    }

    public GroupPartial getSelectedGroup(){
        return teamFilterCombo.getValue();
    }

    @FXML public void initialize() {
        setupComboBoxes();
        setupFilters();
        loadTasks(null, null);

        taskListView.setItems(taskData);
        taskListView.setCellFactory(listView -> new TaskCell());

        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && navigationCallback != null) {
                TaskPartial selectedTask = taskListView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    navigationCallback.accept(selectedTask);
                }
            }
        });
    }

    @FXML private void handleFilterAction() {
        GroupPartial selectedGroup = teamFilterCombo.getValue();
        Integer teamId = (selectedGroup != null) ? selectedGroup.getGroupId() : null;

        if (selectedGroup != null && currentMode == TaskViewMode.ACTIVE && isTeamLeadInGroup(selectedGroup.getGroupId())){
            createTaskButton.setVisible(true);
            createTaskButton.setManaged(true);
            createTaskButton.setDisable(false);
        }

        Integer userId = null;
        if (isAdmin) {
            UserPartial selectedUser = userFilterCombo.getValue();
            userId = (selectedUser != null) ? selectedUser.getUserId() : null;
        } else {
            userId = this.currentUserId;
        }

        loadTasks(teamId, userId);
    }

    @FXML private void handleClearFilterAction() {
        createTaskButton.setDisable(true);
        teamFilterCombo.setValue(null);
        if (isAdmin) {
            userFilterCombo.setValue(null);
        }
        loadTasks(null, null);
    }

    @FXML private void onCreateTaskClicked() {
        if (createTaskCallback != null) {
            createTaskCallback.accept(null);
        }
    }

    private void setupComboBoxes() {teamFilterCombo.setCellFactory(lv -> new ListCell<GroupPartial>() {
            @Override
            protected void updateItem(GroupPartial group, boolean empty) {
                super.updateItem(group, empty);
                setText(empty || group == null ? "All Groups" : group.getName());
            }
        });
        teamFilterCombo.setButtonCell(teamFilterCombo.getCellFactory().call(null));

        userFilterCombo.setCellFactory(lv -> new ListCell<UserPartial>() {
            @Override
            protected void updateItem(UserPartial user, boolean empty) {
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
                List<GroupPartial> groups = groupService.getGroups(null, null, null, null).getContent();
                teamFilterCombo.setItems(FXCollections.observableArrayList(groups));
                List<UserPartial> users = userService.getUsers(null, null, null).getContent();
                userFilterCombo.setItems(FXCollections.observableArrayList(users));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                List<GroupPartial> groups = groupService.getGroups(null, this.currentUserId, null, null).getContent();
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
        try {
            List<TaskPartial> tasks;
            if (currentMode == TaskViewMode.ARCHIVE) {
                tasks = taskService.getDoneTasks(userId, teamId, null, null).getContent();
            } else {
                tasks = taskService.getActiveTasks(userId, teamId, null, null).getContent();
            }
            taskListView.setItems(FXCollections.observableArrayList(tasks));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isTeamLeadInGroup(int groupId) {
        try {
            GroupDetailed detailed = groupService.getGroupById(groupId);
            return detailed.getMembers().stream()
                    .anyMatch(member ->
                            member.getUser().getUserId() == currentUserId &&
                                    member.getRole() == GroupRole.TEAM_LEAD
                    );
        } catch (IOException e) {
            return false;
        }
    }

    static class TaskCell extends ListCell<TaskPartial> {
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
        protected void updateItem(TaskPartial task, boolean empty) {
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