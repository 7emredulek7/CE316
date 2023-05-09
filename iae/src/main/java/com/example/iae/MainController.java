package com.example.iae;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private VBox projectScene;
    @FXML
    private ListView projectList;
    @FXML
    private Label projectName;
    @FXML
    private Label compilerName;
    @FXML
    private Label libraryNames;
    @FXML
    private ListView projectResults;

    protected static String configurationStatus = "";

    protected static ArrayList<Config> configurationsList = new ArrayList<>();
    public void initialize() {
        configurationsList = readConfigurationsFromFile();
    }

    @FXML
    private void createProject() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProjectScreen.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("New Project");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.showAndWait();
    }

    @FXML
    private void createConfiguration() throws IOException {
        configurationStatus = "create new configuration";
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConfigurationScreen.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("New Configuration");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
    @FXML
    private void editConfigurations() throws IOException {
        configurationStatus = "edit configurations";
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConfigurationScreen.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Edit Configurations");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    private void displayProject() {
        projectScene.setVisible(!projectScene.isVisible());
        // selectedProject = (Project) projectList.getSelectionModel().getSelectedItem();
        // if selected project is not null

        // information for the selected project
        // projectName = ...
        //projectScene.setVisible(true);

        // if selected project is null
        //projectScene.setVisible(false);
    }
    @FXML
    private void deleteProject(){
        //projects.remove(selectedProject)
        projectScene.setVisible(false);
    }

    protected ArrayList<Config> readConfigurationsFromFile() {
        ArrayList<Config> configurationsList = new ArrayList<>();
        try {
            File file = new File("configuration.cfg");
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                configurationsList = (ArrayList<Config>) in.readObject();
                in.close();
                fileIn.close();
            } else {
                System.out.println("Configuration file does not exist.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return configurationsList;
    }
}