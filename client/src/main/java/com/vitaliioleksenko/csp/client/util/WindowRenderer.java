package com.vitaliioleksenko.csp.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowRenderer {
    public static void openWindow(String fxmlPath, String title) {
        try {
            Parent root = loadViewInternal(fxmlPath);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Can't open window: " + fxmlPath, e);
        }
    }

    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            Parent root = loadViewInternal(fxmlPath);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Can't switch scene to: " + fxmlPath, e);
        }
    }

    public static Parent loadViewInternal(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowRenderer.class.getResource(fxmlPath));
        Parent root = loader.load();
        if (root == null) {
            throw new IOException("FXML file not found or is empty: " + fxmlPath);
        }
        return root;
    }
}