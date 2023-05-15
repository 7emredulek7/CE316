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
import javafx.stage.DirectoryChooser;
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
            configurations.setValue(configurationsList.get(configurationsList.size() - 1));
        });
        stage.showAndWait();
    }

    @FXML
    private void sourceCodeChooser(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        Node node = (Node) event.getSource();

        File directory = chooser.showDialog(node.getScene().getWindow());
        chooser.setTitle("Choose Source Code Directory");

        if (directory != null)
            sourceCodes = directory;
    }

    @FXML
    private void save() {
        if (name.getText().isBlank())
            alertErrorWindow("Error", "You should enter a name for your project");
        else if (inputFile == null)
            alertErrorWindow("Error", "You should select a input file for your project");
        else if (outputFile == null)
            alertErrorWindow("Error", "You should select a output file for your project");
        else if (configurations.getValue() == null)
            alertErrorWindow("Error",
                    "You should select a configuration for your project. If you haven't created any, you can use the create button.");
        else if (sourceCodes == null)
            alertErrorWindow("Error", "You should select source codes for your project");
        else {
            Project project = new Project(name.getText(), inputFile.getPath(), outputFile.getPath(),
                    configurations.getValue());

            String fileName = sourceCodes.getName().replaceFirst("[.][^.]+$", "");
            Driver driver = new Driver(project, sourceCodes.getPath());
            driver.evaluateAllStudents();

            if (driver.getCompileError() == false) {
                projects.add(project);
            } else {
                MainController.alertErrorWindow("Compiler Error", "Compiler path is not recognized by the program");
            }

            Stage stage = (Stage) name.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }
}