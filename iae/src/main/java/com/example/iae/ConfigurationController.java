package com.example.iae;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

    private String[] supportedLanguages = { "Java", "Python", "C", "C++", "Dart" };

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

        if (configurationStatus.equals("create new configuration"))
            stackPane.getChildren().get(0).setVisible(true);
        else if (configurationStatus.equals("edit selected configuration"))
            editScreen();
        else if (configurationStatus.equals("edit configurations"))
            stackPane.getChildren().get(1).setVisible(true);
    }

    @FXML
    private void save() {
        if (name.getText().isBlank())
            alertErrorWindow("Error","You should enter a name for your configuration.");
        else if (path.getText().isBlank())
            alertErrorWindow("Error","You should enter a compiler/interpreter path for your configuration.");
        else {
            configurationStatus = "";
            Config tempConfig;
            boolean configExists = false;
            for (Config config : configurationsList) {
                if (config.getName().equals(name.getText())
                        || toEdit != null && config.getName().equals(toEdit.getName())) {
                    System.out.println("Configuration already exists \n");
                    tempConfig = config;
                    tempConfig.setName(name.getText());
                    tempConfig.setCompilerPath(path.getText());
                    tempConfig.setLibraries(libraries.getText());
                    tempConfig.setSource(source.getValue());
                    System.out.println("Configuration edited:" + "\n + new name: " +
                            name.getText() + "\n + new path: " + path.getText() + "\n + new libraries: "
                            + libraries.getText() + "\n + new source: " + source.getValue() + "\n");
                    configExists = true;
                    break;
                }
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

    }

    @FXML
    private void deleteConfiguration() {

        if (configurationsList.isEmpty()) {
            System.out.println("Nothing to delete. Configurations list is empty.");
            return;
        }
        Config configToRemove = configurations.getValue();
        if (configToRemove == null)
            alertErrorWindow("Error","You must select a configuration from the choiceBox");
        else {
            configurations.getItems().remove(configToRemove);
            configurationsList.remove(configToRemove);
            System.out.println("Configuration deleted:" + "\n + name: " +
                    configToRemove.getName() + "\n + path: " + configToRemove.getCompilerPath() + "\n + libraries: "
                    + configToRemove.getLibraries() + "\n + source: " + configToRemove.getSource() + "\n");
            saveConfigurationsToFile(configurationsList);
        }
    }

    @FXML
    private void editScreen() {
        Config temp = configurations.getValue();
        if (temp == null)
            alertErrorWindow("Error","You must select a configuration from the choiceBox");
        else {
            stackPane.getChildren().get(1).setVisible(false);
            name.setText(temp.getName());
            source.setValue(temp.getSource());
            libraries.setText(temp.getLibraries());
            path.setText(temp.getCompilerPath());
            stackPane.getChildren().get(0).setVisible(true);
        }
    }

    private void saveConfigurationsToFile(ArrayList<Config> configurationsList) {

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

}
