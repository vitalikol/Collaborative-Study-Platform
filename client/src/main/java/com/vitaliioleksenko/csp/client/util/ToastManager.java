package com.vitaliioleksenko.csp.client.util;

import com.vitaliioleksenko.csp.client.model.NotificationMessage;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ToastManager {

    private final StackPane notificationLayer;

    public ToastManager(StackPane notificationLayer) {
        this.notificationLayer = notificationLayer;
    }

    public void show(NotificationMessage msg) {

        VBox box = new VBox();
        box.setStyle("""
            -fx-background-color: #333333;
            -fx-background-radius: 8;
            -fx-padding: 12;
            -fx-spacing: 5;
        """);

        box.setMaxWidth(300);
        box.setMaxHeight(Region.USE_PREF_SIZE);

        Label title = new Label(msg.getTitle());
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label text = new Label(msg.getMessage());
        text.setStyle("-fx-text-fill: white;");

        box.getChildren().addAll(title, text);

        notificationLayer.getChildren().add(box);

        StackPane.setAlignment(box, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(box, new Insets(20));

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(200), box);
        slideIn.setFromY(50);
        slideIn.setToY(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), box);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition showAnim = new ParallelTransition(slideIn, fadeIn);

        PauseTransition stay = new PauseTransition(Duration.seconds(3));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), box);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> notificationLayer.getChildren().remove(box));

        new SequentialTransition(showAnim, stay, fadeOut).play();
    }
}

