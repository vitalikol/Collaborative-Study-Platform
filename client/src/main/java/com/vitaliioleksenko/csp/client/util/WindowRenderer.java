package com.vitaliioleksenko.csp.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowRenderer {

    public static void openWindow(String fxmlPath, String title){
        FXMLLoader loader = new FXMLLoader(WindowRenderer.class.getResource(fxmlPath));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Can't open" + fxmlPath);
        }
    }

    public static void switchScene(Stage stage, String fxmlPath)  {
        FXMLLoader loader = new FXMLLoader(WindowRenderer.class.getResource(fxmlPath));
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Can't open " + fxmlPath + ", because " + e.getMessage() + e.getLocalizedMessage());
        }
    }
}
