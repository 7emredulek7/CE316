package com.example.iae;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private ListView<HBox> projectResults;

    protected static String configurationStatus = "";

    public static ArrayList<Config> configurationsList = new ArrayList<>();
    protected static ArrayList<Project> projects = new ArrayList<>();
    private Project selectedProject;

    public void initialize() {
        configurationsList = readConfigurationsFromFile();
        /*
         * Config c1 = new Config("a", "x", "x", "x");
         * Config c2 = new Config("b", "y", "y", "y");
         * Config c3 = new Config("c", "z", "z", "z");
         * Config c4 = new Config("d", "t", "t", "t");
         * 
         * Project p1 = new Project("1", "ab", "ab", c1);
         * Project p2 = new Project("2", "c", "c", c2);
         * Project p3 = new Project("3", "d", "d", c3);
         * Project p4 = new Project("4", "e", "e", c4);
         * 
         * configurationsList.add(c1);
         * configurationsList.add(c2);
         * configurationsList.add(c3);
         * configurationsList.add(c4);
         * 
         * p1.addStudent(new Student("20200", true));
         * p1.addStudent(new Student("20300", false));
         * p1.addStudent(new Student("20400", true));
         * p1.addStudent(new Student("20500", false));
         * 
         * DBConnection.getInstance().addProject(p1);
         * DBConnection.getInstance().addProject(p2);
         * DBConnection.getInstance().addProject(p3);
         * DBConnection.getInstance().addProject(p4);
         */
        projects = DBConnection.getInstance().getAllProjects();

        projects = DBConnection.getInstance().getAllProjects();
        addProjectsToList();
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
        stage.setOnCloseRequest(windowEvent -> {
            projectList.getItems().clear();
            addProjectsToList();
        });
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
        if (selectedProject != null) {
            projectName.setText(selectedProject.getName());
            compilerName.setText(selectedProject.getConfiguration().getSource());
            libraryNames.setText(selectedProject.getConfiguration().getLibraries().stream().map(Object::toString)
                    .collect(Collectors.joining(",")));
            addStudentsToList(selectedProject);
            projectScene.setVisible(true);
        } else
            projectScene.setVisible(false);
    }

    private void deleteProject(Project selectedProject, HBox hBox) {
        if (alertYesNoWindow("Are you sure?", "Are you sure you want delete " + selectedProject.getName())) {
            projects.remove(selectedProject);
            DBConnection.getInstance().removeProject(selectedProject.getName());
            projectList.getItems().remove(hBox);
            projectScene.setVisible(false);

        }
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

    public static void alertErrorWindow(String title, String contentText) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setResizable(false);
        a.setTitle(title);
        a.setContentText(contentText);
        a.showAndWait();
    }

    public boolean alertYesNoWindow(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(contentText);
        AtomicBoolean b = new AtomicBoolean(false);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.showAndWait().ifPresent(choice -> b.set(choice != noButton));
        return b.get();
    }

    private void addProjectsToList() {
        for (Project p : projects) {
            HBox hbox = new HBox();
            HBox trash = new HBox();
            trash.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(trash, Priority.ALWAYS);
            HBox.setHgrow(hbox, Priority.ALWAYS);
            hbox.setSpacing(10);
            hbox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(hbox, new Insets(0, 0, 10, 0));
            Button deleteButton = new Button();
            ImageView deleteImageView = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/trash.png"))));
            deleteButton.setGraphic(deleteImageView);
            deleteImageView.setFitWidth(20);
            deleteImageView.setFitHeight(20);
            deleteButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            Label label = new Label(p.getName());
            label.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            trash.getChildren().add(deleteButton);
            hbox.getChildren().addAll(label, trash);
            hbox.setOnMouseClicked(event -> displayProject(p));
            deleteButton.setOnAction(event -> deleteProject(p, hbox));
            projectList.getItems().add(hbox);
        }
    }

    private void addStudentsToList(Project selectedProject) {
        projectResults.getItems().clear();
        for (Student s : selectedProject.getStudents()) {
            HBox hbox = new HBox();
            HBox passed = new HBox();
            passed.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(passed, Priority.ALWAYS);
            HBox.setHgrow(hbox, Priority.ALWAYS);
            hbox.setSpacing(10);
            hbox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(hbox, new Insets(0, 0, 10, 0));
            ImageView mark;
            if (s.isPassed())
                mark = new ImageView(
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/checkmark.png"))));
            else
                mark = new ImageView(
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/redmark.png"))));
            mark.setFitWidth(20);
            mark.setFitHeight(20);
            mark.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            Label label = new Label(s.getId());
            label.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            passed.getChildren().add(mark);
            hbox.getChildren().addAll(label, passed);
            projectResults.getItems().add(hbox);
        }

    }
}