package com.example.iae;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private VBox projectScene;
    @FXML
    private ListView<HBox> projectList;
    @FXML
    private Label projectName;
    @FXML
    private Label compilerName;
    @FXML
    private Label libraryNames;
    @FXML
    private ListView<Student> projectResults;

    protected static String configurationStatus = "";

    public static ArrayList<Config> configurationsList = new ArrayList<>();
    protected static ArrayList<Project> projects = new ArrayList<>();
    private Project selectedProject;
    public void initialize() {
        configurationsList = readConfigurationsFromFile();

        /* test data
        Project p1 = new Project("1", "","",new Config("","","",""));
        Project p2 = new Project("2", "","",new Config("","","",""));
        Project p3 = new Project("3", "","",new Config("","","",""));
        Project p4 = new Project("4", "","",new Config("","","",""));

        projects.add(p1);   projects.add(p2);
        projects.add(p3);   projects.add(p4);
        */
        //projects = projects from database
        for (Project p: projects) {
            HBox hbox = new HBox();
            HBox trash = new HBox();
            trash.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(trash,Priority.ALWAYS);
            HBox.setHgrow(hbox,Priority.ALWAYS);
            hbox.setSpacing(10);
            hbox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(hbox, new Insets(0, 0, 10, 0));
            Button deleteButton = new Button();
            ImageView deleteImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/trash.png"))));
            deleteButton.setGraphic(deleteImageView);
            deleteImageView.setFitWidth(20);
            deleteImageView.setFitHeight(20);
            deleteButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            Label label = new Label(p.getName());
            trash.getChildren().add(deleteButton);
            hbox.getChildren().addAll(label, trash);
            hbox.setOnMouseClicked(event -> {
                displayProject(p);
            });
            deleteButton.setOnAction(event -> {
                projectList.getItems().remove(hbox);
                deleteProject(p);
            });
            projectList.getItems().add(hbox);
        }
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

    private void displayProject(Project selectedProject) {
        projectScene.setVisible(!projectScene.isVisible()); // I will delete this later
        if(selectedProject != null){
            projectName.setText(selectedProject.getName());
            compilerName.setText(selectedProject.getConfiguration().getSource());
            libraryNames.setText(selectedProject.getConfiguration().getLibraries());
            projectResults.getItems().addAll(selectedProject.getStudents());
            projectScene.setVisible(true);
        }
        //else
            //projectScene.setVisible(false);
    }
    private void deleteProject(Project selectedProject){
        projects.remove(selectedProject);
        // remove it from the database as well
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