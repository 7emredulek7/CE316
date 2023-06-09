package com.example.iae;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("IAE");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(600);
        stage.setMaxWidth(1400);
        stage.setMaxHeight(950);
        stage.setFullScreen(false);
        stage.setOnCloseRequest(windowEvent->System.exit(0));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}