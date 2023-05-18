package com.example.iae;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class HelpController {

    @FXML
    private StackPane stackPane;
    @FXML
    private Button button1, button2, button3, button4;

    private Button[] buttons = new Button[4];
    private int index = 0;
    private Button prevButton;

    @FXML
    public void initialize() {
        stackPane.getChildren().get(0).setVisible(true);
        stackPane.getChildren().get(1).setVisible(false);
        stackPane.getChildren().get(2).setVisible(false);
        stackPane.getChildren().get(3).setVisible(false);

        button1.setOnAction(event -> swap(button1, 0));
        button2.setOnAction(event -> swap(button2, 1));
        button3.setOnAction(event -> swap(button3, 2));
        button4.setOnAction(event -> swap(button4, 3));

        prevButton = button1;
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
    }

    @FXML
    private void nextPage() {
        if (index < 6) {
            stackPane.getChildren().get(index++).setVisible(false);
            stackPane.getChildren().get(index).setVisible(true);
            buttonStyleSwap(prevButton, buttons[index]);
            prevButton = buttons[index];
        }
    }

    @FXML
    private void prevPage() {
        if (index > 0) {
            stackPane.getChildren().get(index--).setVisible(false);
            stackPane.getChildren().get(index).setVisible(true);
            buttonStyleSwap(prevButton, buttons[index]);
            prevButton = buttons[index];
        }
    }

    private void swap(Button pressedButton, int newIndex) {
        buttonStyleSwap(prevButton, pressedButton);
        stackPane.getChildren().get(index).setVisible(false);
        index = newIndex;
        prevButton = buttons[index];
        stackPane.getChildren().get(index).setVisible(true);
    }

    private void buttonStyleSwap(Button prevButton, Button nextButton) {
        prevButton.setStyle(
                "-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 20;");

        nextButton.setStyle("-fx-background-color: black; -fx-background-radius: 20;");
    }
}
