package com.example.iae;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.ArrayList;

public class ConfigurationController extends MainController {
    @FXML
    private StackPane stackPane;
    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<String> source;
    @FXML
    private TextField libraries;
    @FXML
    private TextField path;
    @FXML
    private ChoiceBox<Config> configurations;

    private final String[] supportedLanguages = { "Java", "Python", "C", "C++", "Dart" };

    Config toEdit;

    public void initialize() {
        for (String s : supportedLanguages) {
            source.getItems().add(s);
        }
        source.getSelectionModel().selectFirst();
        for (Config c : configurationsList)
            configurations.getItems().add(c);

        stackPane.getChildren().get(0).setVisible(false);
        stackPane.getChildren().get(1).setVisible(false);

        switch (configurationStatus) {
            case "create new configuration" -> stackPane.getChildren().get(0).setVisible(true);
            case "edit selected configuration" -> editScreen();
            case "edit configurations" -> stackPane.getChildren().get(1).setVisible(true);
        }
    }

    @FXML
    private void save() {
        if (name.getText().isBlank()) {
            alertErrorWindow("Error", "You should enter a name for your configuration.");
            return;
        }
        if (path.getText().isBlank()) {
            alertErrorWindow("Error", "You should enter a compiler/interpreter path for your configuration.");
            return;
        }

        configurationStatus = "";
        Config tempConfig = null;
        boolean configExists = false;
        for (Config config : configurationsList) {
            if ((toEdit != null && config != toEdit && config.getName().equals(name.getText())) ||
                    (toEdit == null && config.getName().equals(name.getText()))) {
                alertErrorWindow("Error", "A configuration with the same name already exists.");
                return;
            }
            if (toEdit != null && (config == toEdit || config.getName().equals(toEdit.getName()))) {
                tempConfig = config;
                break;
            }
        }

        if (toEdit != null && tempConfig != null) {
            tempConfig.setName(name.getText());
            tempConfig.setCompilerPath(path.getText());
            tempConfig.setLibraries(libraries.getText());
            tempConfig.setSource(source.getValue());
            System.out.println("Configuration edited:" + "\n + new name: " +
                    name.getText() + "\n + new path: " + path.getText() + "\n + new libraries: "
                    + libraries.getText() + "\n + new source: " + source.getValue() + "\n");
            configExists = true;
        }

        if (!configExists) {
            tempConfig = new Config(name.getText(), source.getValue(), libraries.getText(), path.getText());
            configurationsList.add(tempConfig);
            System.out.println("Configuration created:" + "\n + name: " +
                    name.getText() + "\n + path: " + path.getText() + "\n + libraries: "
                    + libraries.getText() + "\n + source: " + source.getValue() + "\n");
        }

        saveConfigurationsToFile(configurationsList);

        Stage stage = (Stage) name.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }



    @FXML
    private void deleteConfiguration() {

        if (configurationsList.isEmpty()) {
            System.out.println("Nothing to delete. Configurations list is empty.");
            return;
        }
        Config configToRemove = configurations.getValue();
        if (configToRemove == null)
            alertErrorWindow("Error", "You must select a configuration from the choiceBox");
        else {
            configurations.getItems().remove(configToRemove);
            configurationsList.remove(configToRemove);
            System.out.println("Configuration deleted:" + "\n + name: " +
                    configToRemove.getName() + "\n + path: " + configToRemove.getCompilerPath() + "\n + libraries: "
                    + configToRemove.getLibraries() + "\n + source: " + configToRemove.getSource() + "\n");
            saveConfigurationsToFile(configurationsList);
            configurations.getSelectionModel().clearSelection();
        }

    }

    private static void saveConfigurationsToFile(ArrayList<Config> configurationsList) {

        try {
            File file = new File("configuration.cfg");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(configurationsList);
            out.close();
            fileOut.close();
            System.out.println("Configurations saved to configuration.cfg");
        } catch (IOException e) {
            e.getCause().printStackTrace();
        }
    }


    @FXML
    private void editScreen() {
        Config temp = configurations.getValue();
        if (temp == null) {
            alertErrorWindow("Error", "You must select a configuration from the choiceBox");
            return;
        }

        toEdit = temp; // Update the toEdit variable

        stackPane.getChildren().get(1).setVisible(false);
        name.setText(temp.getName());
        source.setValue(temp.getSource());
        libraries.setText(temp.getLibraries());
        path.setText(temp.getCompilerPath());
        stackPane.getChildren().get(0).setVisible(true);
    }


    @FXML
    private void exportConfiguration() {
        if (configurationsList.isEmpty()) {
            System.out.println("Nothing to export. Configurations list is empty.");
            return;
        }
        Config configToExport = configurations.getValue();
        if (configToExport == null) {
            alertErrorWindow("Error", "You must select a configuration from the choiceBox");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export CFG File");
        fileChooser.setInitialFileName(configToExport.getName() + ".cfg");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CFG Files", "*.cfg"));

        Stage stage = new Stage();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try {
                FileOutputStream fileOut = new FileOutputStream(selectedFile);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(configToExport);
                out.close();
                fileOut.close();
                System.out.println(configToExport.getName() + ".cfg has been exported to " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }


    public static void importConfiguration() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import CFG File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CFG Files", "*.cfg"));

        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Read the configuration from the selected file
                FileInputStream fileIn = new FileInputStream(selectedFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Config importedConfig = (Config) in.readObject();
                in.close();
                fileIn.close();

                // Check if the configuration already exists
                boolean exists = configurationsList.stream()
                        .anyMatch(config -> config.getName().equals(importedConfig.getName()));
                if (exists) {
                    System.out.println("Configuration already exists: " + importedConfig.getName());
                    alertErrorWindow("Error", "A configuration with the same name already exists.");
                    return;
                }

                // Add the imported configuration to the configurationsList
                configurationsList.add(importedConfig);
                saveConfigurationsToFile(configurationsList);
                System.out.println("Configuration imported: " + importedConfig.getName());

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }
    }


}
