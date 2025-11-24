package com.vitaliioleksenko.csp.client.controller.task;

import com.vitaliioleksenko.csp.client.model.resource.ResourceCreate;
import com.vitaliioleksenko.csp.client.model.resource.ResourcePartial;
import com.vitaliioleksenko.csp.client.model.resource.ResourceShort;
import com.vitaliioleksenko.csp.client.model.task.TaskDetailed;
import com.vitaliioleksenko.csp.client.model.task.TaskPartial;
import com.vitaliioleksenko.csp.client.service.ResourceService;
import com.vitaliioleksenko.csp.client.service.TaskService;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.util.enums.ResourceFormat;
import com.vitaliioleksenko.csp.client.util.enums.ResourceType;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TaskProfileController {

    @FXML private Label taskTitleLabel;
    @FXML private Label groupNameLabel;
    @FXML private Label deadlineLabel;
    @FXML private HBox adminActionsBox;
    @FXML private VBox submissionBox;
    @FXML private TextArea descriptionArea;
    @FXML private ListView<ResourcePartial> materialsListView;
    @FXML private ListView<ResourcePartial> submissionsListView;
    @Setter private Consumer<Void> taskEditCallback;
    @Setter private Runnable backNavigationCallback;

    private final TaskService taskService;
    private final ResourceService resourceService;
    private final UserSession session;
    private final DateTimeFormatter formatter;
    private final boolean isAdmin;

    private TaskDetailed details;
    private int taskId;

    public TaskProfileController() {
        this.taskService = new TaskService();
        this.resourceService = new ResourceService();
        this.session = UserSession.getInstance();
        this.formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        this.isAdmin = true; //todo
    }

    // ------------------------------ INIT ------------------------------

    public void initData(int taskId) {
        this.taskId = taskId;
        loadTaskDetails();
        taskTitleLabel.setText(details.getTitle());
        deadlineLabel.setText("Deadline: " + details.getDeadline().format(formatter));

        setupListViews();

        if (isAdmin) {
            adminActionsBox.setVisible(true);
            adminActionsBox.setManaged(true);
        } else {
            submissionBox.setVisible(true);
            submissionBox.setManaged(true);
        }

        loadTaskDetails();
    }

    private void loadTaskDetails() {
        try {
            details = taskService.getTaskById(taskId);
            List<ResourcePartial> resources = resourceService.getResources(taskId, null, null).getContent();

            descriptionArea.setText(details.getDescription());
            groupNameLabel.setText("Group: " + details.getGroup().getName());

            materialsListView.getItems().setAll(
                    resources.stream()
                            .filter(r -> r.getType() == ResourceType.MATERIAL)
                            .toList()
            );


            submissionsListView.getItems().setAll(
                    resources.stream()
                            .filter(r -> r.getType() == ResourceType.SUBMISSION)
                            .toList()
            );

        } catch (IOException e) {
            descriptionArea.setText("Failed to load : " + e.getMessage());
        }
    }

    // ------------------------------ LIST VIEWS ------------------------------

    private void setupListViews() {
        materialsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ResourcePartial item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle() + " (" + item.getFormat() + ")");
            }
        });

        submissionsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ResourcePartial item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle() + " — submitted");
            }
        });
    }

    // ------------------------------ ADMIN ACTIONS ------------------------------

    @FXML private void onEditTask() {
        if (taskEditCallback != null) {
            taskEditCallback.accept(null);
        }
    }

    @FXML private void onAddMaterial() {
        showResourceCreateDialog(false);
    }

    @FXML private void onDeleteTask() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete task");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Task will be permanently deleted.");
        Stage stage = (Stage) taskTitleLabel.getScene().getWindow();
        alert.initOwner(stage);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                taskService.deleteTask(taskId);
                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setHeaderText("Deleted successfully");
                done.initOwner(stage);
                done.showAndWait();
                if (backNavigationCallback != null) {
                    backNavigationCallback.run();
                }
            } catch (Exception e) {
                showError("Failed to delete: " + e.getMessage());
            }
        }
    }

    // ------------------------------ SUBMISSION (USER) ------------------------------

    @FXML private void onSubmitUrl() {
        showResourceCreateDialog(true);
    }

    @FXML private void onSubmitFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Upload submission");
        File file = chooser.showOpenDialog(getStage());

        if (file == null) return;
//todo
//        try {
//            resourceService.uploadSubmission(taskId, session.getUserId(), file);
//            loadTaskDetails();
//        } catch (IOException e) {
//            showError("Upload failed: " + e.getMessage());
//        }
    }

    // ------------------------------ RESOURCE CREATE DIALOG ------------------------------

    private void showResourceCreateDialog(boolean submissionMode) {
        Dialog<ResourceCreate> dialog = new Dialog<>();
        dialog.setWidth(500);
        dialog.setTitle(submissionMode ? "Submit work" : "Add material");
        dialog.initOwner(taskTitleLabel.getScene().getWindow());

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label typeLabel = new Label("Type:");
        ComboBox<ResourceType> typeBox = new ComboBox<>();

        Label formatLabel = new Label("Format:");
        ComboBox<ResourceFormat> formatBox = new ComboBox<>();

        Label urlLabel = new Label("URL:");
        TextField urlField = new TextField();

        // File chooser UI elements
        Label fileLabel = new Label("File:");
        Button fileButton = new Button("Choose file...");
        Label chosenFileLabel = new Label();
        File[] selectedFile = new File[1];

        fileButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose file");
            File f = fileChooser.showOpenDialog(dialog.getOwner());
            if (f != null) {
                selectedFile[0] = f;
                chosenFileLabel.setText(f.getAbsolutePath());
            }
        });

        typeBox.getItems().setAll(ResourceType.values());
        formatBox.getItems().setAll(ResourceFormat.values());

        if (submissionMode) {
            typeBox.setValue(ResourceType.SUBMISSION);
            typeBox.setDisable(true);
        }

        VBox box = new VBox(10,
                titleLabel, titleField,
                typeLabel, typeBox,
                formatLabel, formatBox,
                urlLabel, urlField,
                fileLabel, fileButton, chosenFileLabel
        );

        fileLabel.setVisible(false);
        fileButton.setVisible(false);
        chosenFileLabel.setVisible(false);

        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        formatBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!submissionMode && newVal == ResourceFormat.FILE) {
                // FILE → show file input, hide URL
                urlLabel.setVisible(false);
                urlField.setVisible(false);
                urlLabel.setManaged(false);
                urlField.setManaged(false);


                fileLabel.setVisible(true);
                fileButton.setVisible(true);
                chosenFileLabel.setVisible(true);
            } else {
                // URL or submission mode → show URL, hide file
                urlLabel.setVisible(true);
                urlField.setVisible(true);
                urlLabel.setManaged(true);
                urlField.setManaged(true);

                fileLabel.setVisible(false);
                fileButton.setVisible(false);
                chosenFileLabel.setVisible(false);
            }
        });

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                return ResourceCreate.builder()
                        .taskId(taskId)
                        .userId(session.getCurrentUserId())
                        .title(titleField.getText())
                        .type(typeBox.getValue())
                        .format(formatBox.getValue())
                        .pathOrUrl(
                                formatBox.getValue() == ResourceFormat.FILE
                                        ? (selectedFile[0] != null ? selectedFile[0].getAbsolutePath() : null)
                                        : urlField.getText()
                        )
                        .build();
            }
            return null;
        });

        Optional<ResourceCreate> result = dialog.showAndWait();
        result.ifPresent(res -> {
            try {
                ResourceShort resourceShort = resourceService.createResource(res);
                if (formatBox.getValue() == ResourceFormat.FILE){
                    resourceService.uploadResource(selectedFile[0] , resourceShort.getResourceId());
                }
                loadTaskDetails();
            } catch (IOException e) {
                showError("Failed to create resource: " + e.getMessage());
            }
        });
    }

    // ------------------------------ UTIL ------------------------------

    private Stage getStage() {
        return (Stage) taskTitleLabel.getScene().getWindow();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error");
        a.setContentText(msg);
        a.showAndWait();
    }
}
