package com.example.iae;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private ChoiceBox configurations;
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
    private void sourceCodeChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Node node = (Node) event.getSource();

        // should not be filtered by txt it can be folder itself
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = chooser.showOpenDialog(node.getScene().getWindow());
        chooser.setTitle("Choose Source Codes");

        if (file != null)
            sourceCodes = file;
    }

    @FXML
    private void save() {

        // create project object

        Stage stage = (Stage) name.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}