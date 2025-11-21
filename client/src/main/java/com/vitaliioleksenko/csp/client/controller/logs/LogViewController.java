package com.vitaliioleksenko.csp.client.controller.logs;

import com.vitaliioleksenko.csp.client.controller.group.GroupCardController;
import com.vitaliioleksenko.csp.client.controller.group.GroupViewController;
import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.activity.ActivityLogDetailed;
import com.vitaliioleksenko.csp.client.model.group.GroupPartial;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.service.LogService;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

public class LogViewController {
    @FXML private ListView<ActivityLogDetailed> logListView;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    private final ObservableList<ActivityLogDetailed> allGroups = FXCollections.observableArrayList();

    private final LogService logService;
    private final UserSession session;
    private final boolean isAdmin;

    private int currentPage = 0;
    private final int pageSize = 10;
    private int totalPages = 0;

    public LogViewController() {
        this.logService = new LogService();
        this.session = UserSession.getInstance();
        this.isAdmin = session.getCurrentUserRole() == Role.ROLE_ADMIN;
    }

    @FXML public void initialize() {
        setupListViewFactory();
        loadLogBasedOnRole();
    }

    @FXML private void onPrevPage() {
        if (currentPage > 0) {
            currentPage--;
            loadLogBasedOnRole();
        }
    }

    @FXML private void onNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadLogBasedOnRole();
        }
    }

    private void loadLogBasedOnRole() {
        PageResponse<ActivityLogDetailed> logsPage;
        try {
            if (isAdmin) {
                logsPage = logService.getLogs(null, currentPage, pageSize);
            } else {
                logsPage = logService.getLogs(session.getCurrentUserId(), currentPage, pageSize);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        totalPages = logsPage.getTotalPages();
        allGroups.setAll(logsPage.getContent());
        logListView.setItems(allGroups);

        updatePaginationControls();
    }

    private void updatePaginationControls() {
        pageLabel.setText(String.format("Page %d of %d", currentPage + 1, Math.max(1, totalPages)));

        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage >= totalPages - 1);
    }

    private void setupListViewFactory() {
        logListView.setCellFactory(lv -> new LogListCell());
    }

    static class LogListCell extends ListCell<ActivityLogDetailed> {
        private Node cardNode;
        private LogCardController cardController;

        public LogListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/logs/log-card.fxml"));
                cardNode = loader.load();
                cardController = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void updateItem(ActivityLogDetailed log, boolean empty) {
            super.updateItem(log, empty);
            if (empty || log == null) {
                setGraphic(null);
            } else {
                cardController.setData(log);
                setGraphic(cardNode);
            }
        }
    }
}
