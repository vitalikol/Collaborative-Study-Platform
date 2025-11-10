package com.vitaliioleksenko.csp.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Circle circle = new Circle(50);
        Pane pane = new Pane();
        pane.getChildren().add(circle);
        Scene scene = new Scene(pane, 400, 400);
        stage.setScene(scene);
        stage.show();
    }
}