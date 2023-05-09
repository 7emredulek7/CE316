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

    private void compileProject(String language, String projectName, String path) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        String command = "default";
        String[] commandArgs = command.split("\\s+");
        switch (language) {
            case "C":
                command = "cmd.exe /c gcc -o " + projectName + ".exe " + projectName + ".c & " + projectName;
                break;
            case "C++":
                command = "cmd.exe /c g++ -o " + projectName + ".exe " + projectName + ".cpp & " + projectName;
                break;
            case "Python":
                command = "cmd.exe /c py " + projectName + ".py";
                break;
            case "Java":
                command = "cmd.exe /c javac " + projectName + ".java & java " + projectName;
                break;
            case "Dart":
                command = "cmd.exe /c dart run " + projectName + ".dart";
                break;
            default:
                break;
        }
        try {
            commandArgs = command.split("\\s+");
            ProcessBuilder builder = new ProcessBuilder(commandArgs);
            builder.directory(new File(path));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}