package com.example.iae;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ProjectController extends MainController {
    @FXML
    private TextField name;
    @FXML
    private File inputFile;
    @FXML
    private File outputFile;
    @FXML
    private ChoiceBox<Config> configurations;
    @FXML
    private File sourceCodes;

    public void initialize() {
        configurations.getItems().addAll(configurationsList);
    }

    @FXML
    private void inputFileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Node node = (Node) event.getSource();

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = chooser.showOpenDialog(node.getScene().getWindow());
        chooser.setTitle("Choose Input File");

        if (file != null)
            inputFile = file;
    }

    @FXML
    private void outputFileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Node node = (Node) event.getSource();

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = chooser.showOpenDialog(node.getScene().getWindow());
        chooser.setTitle("Choose Output File");

        if (file != null)
            outputFile = file;
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
        stage.setOnCloseRequest(windowEvent -> {
            configurations.getItems().clear();
            configurations.getItems().addAll(configurationsList);
        });
        stage.showAndWait();
    }

    @FXML
    private void sourceCodeChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Node node = (Node) event.getSource();

        chooser.getExtensionFilters().addAll();

        File file = chooser.showOpenDialog(node.getScene().getWindow());
        chooser.setTitle("Choose Source Codes");

        if (file != null)
            sourceCodes = file;
    }

    @FXML
    private void save() {

        Project project = new Project(name.getText(), inputFile.getPath(), outputFile.getPath(),
                configurations.getValue());

        DBConnection.getInstance().addProject(project);
        projects.add(project);

        Stage stage = (Stage) name.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

        String fileName = sourceCodes.getName().replaceFirst("[.][^.]+$", "");
        Driver driver = new Driver(project, sourceCodes.getParentFile().getPath());
        driver.evaluateStudent();
    }
}