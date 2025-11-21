package com.vitaliioleksenko.csp.client.controller.task;

import com.vitaliioleksenko.csp.client.model.resource.ResourcePartial;
import com.vitaliioleksenko.csp.client.model.task.TaskDetailed;
import com.vitaliioleksenko.csp.client.model.task.TaskPartial;
import com.vitaliioleksenko.csp.client.service.TaskService;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.fxml.FXML;

import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class TaskProfileController {

    @FXML private Label taskTitleLabel;
    @FXML private Label groupNameLabel;
    @FXML private Label deadlineLabel;
    @FXML private HBox adminActionsBox;
    @FXML private VBox submissionBox;
    @FXML private TextArea descriptionArea;
    @FXML private ListView<ResourcePartial> materialsListView;
    @FXML private ListView<ResourcePartial> submissionsListView;

    private final TaskService taskService;
    private final UserSession session;
    private final DateTimeFormatter formatter;
    private final boolean isAdmin;

    public TaskProfileController() {
        this.taskService = new TaskService();
        this.session = UserSession.getInstance();
        this.formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        this.isAdmin = session.getCurrentUserRole() == Role.ROLE_ADMIN;
    }

    public void initData(TaskPartial basicTask) {
        taskTitleLabel.setText(basicTask.getTitle());
        deadlineLabel.setText("DeadLine: " + basicTask.getDeadline().format(formatter));

        setupResourceListViews();

        if (isAdmin) {
            adminActionsBox.setVisible(true);
            adminActionsBox.setManaged(true);
        } else {
            submissionBox.setVisible(true);
            submissionBox.setManaged(true);
        }

        loadTaskDetails(basicTask.getTaskId());
    }

    private void loadTaskDetails(int taskId) {
        try {
            TaskDetailed details = taskService.getTaskById(taskId);

            descriptionArea.setText(details.getDescription());
            groupNameLabel.setText("Group: " + details.getGroup().getName());

//            List<Resource> materials = details.getResources().stream()
//                    .filter(r -> r.getType() == ResourceType.MATERIAL)
//                    .collect(Collectors.toList());
//
//            List<Resource> submissions = details.getResources().stream()
//                    .filter(r -> r.getType() == ResourceType.SUBMISSION)
//                    .collect(Collectors.toList());
//
//            materialsListView.setItems(FXCollections.observableArrayList(materials));
//            submissionsListView.setItems(FXCollections.observableArrayList(submissions));

        } catch (IOException e) {
            descriptionArea.setText("Failed load task: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private void setupResourceListViews() {
//        javafx.util.Callback<ListView<Resource>, ListCell<Resource>> factory = lv -> new ListCell<Resource>() {
//            @Override
//            protected void updateItem(Resource resource, boolean empty) {
//                super.updateItem(resource, empty);
//                if (empty || resource == null) {
//                    setText(null);
//                } else {
//                    // TODO: Використати ResourceCard.fxml для гарнішого вигляду
//                    setText(resource.getTitle() + " (by " + resource.getUploadedBy().getName() + ")");
//                }
//            }
//        };
//
//        materialsListView.setCellFactory(factory);
//        submissionsListView.setCellFactory(factory);
    }
}