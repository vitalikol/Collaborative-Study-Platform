package com.vitaliioleksenko.csp.client.controller.task;

import com.vitaliioleksenko.csp.client.model.Resource;
import com.vitaliioleksenko.csp.client.model.Task;
import com.vitaliioleksenko.csp.client.model.TaskDetails;
import com.vitaliioleksenko.csp.client.service.TaskService;
import com.vitaliioleksenko.csp.client.util.ResourceType;
import com.vitaliioleksenko.csp.client.util.UserSession;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.fxml.FXML;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class TaskProfileController {

    @FXML private Label taskTitleLabel;
    @FXML private Label groupNameLabel;
    @FXML private Label deadlineLabel;
    @FXML private HBox adminActionsBox;
    @FXML private VBox submissionBox;
    @FXML private TextArea descriptionArea;
    @FXML private ListView<Resource> materialsListView;
    @FXML private ListView<Resource> submissionsListView;

    private final TaskService taskService = new TaskService();
    private final UserSession session = UserSession.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");

    public void initData(Task basicTask) {
        taskTitleLabel.setText(basicTask.getTitle());
        deadlineLabel.setText("Дедлайн: " + basicTask.getDeadline().format(formatter));

        setupResourceListViews();

        if (session.getCurrentUserRole().equals("ROLE_ADMIN")) {
            adminActionsBox.setVisible(true);
            adminActionsBox.setManaged(true);
        } else {
            submissionBox.setVisible(true);
            submissionBox.setManaged(true);
        }

        loadTaskDetails(basicTask.getTaskId());
    }

    /**
     * Завантажує повні деталі (включно з ресурсами) з API
     */
    private void loadTaskDetails(int taskId) {
        try {
            TaskDetails details = taskService.getTaskById(taskId);

            descriptionArea.setText(details.getDescription());
            groupNameLabel.setText("Group: "); // + details.getGroup().getName()

            List<Resource> materials = details.getResources().stream()
                    .filter(r -> r.getType() == ResourceType.MATERIAL)
                    .collect(Collectors.toList());

            List<Resource> submissions = details.getResources().stream()
                    .filter(r -> r.getType() == ResourceType.SUBMISSION)
                    .collect(Collectors.toList());

            materialsListView.setItems(FXCollections.observableArrayList(materials));
            submissionsListView.setItems(FXCollections.observableArrayList(submissions));

        } catch (IOException e) {
            descriptionArea.setText("Не вдалося завантажити деталі завдання: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private void setupResourceListViews() {
        javafx.util.Callback<ListView<Resource>, ListCell<Resource>> factory = lv -> new ListCell<Resource>() {
            @Override
            protected void updateItem(Resource resource, boolean empty) {
                super.updateItem(resource, empty);
                if (empty || resource == null) {
                    setText(null);
                } else {
                    // TODO: Використати ResourceCard.fxml для гарнішого вигляду
                    setText(resource.getTitle() + " (by " + resource.getUploadedBy().getName() + ")");
                }
            }
        };

        materialsListView.setCellFactory(factory);
        submissionsListView.setCellFactory(factory);
    }
}