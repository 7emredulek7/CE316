package com.example.iae;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
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
    private ChoiceBox<String> configurations;

    private ArrayList<Config> configurationsList = new ArrayList<>();

    private String[] supportedLanguages = { "Java", "Python", "C", "C++", "Dart" };

    Config toEdit;

    public void initialize() {
        for (String s : supportedLanguages) {
            source.getItems().add(s);
        }
        source.getSelectionModel().selectFirst();
        configurationsList = readConfigurationsFromFile();

        for (Config c : configurationsList)
            configurations.getItems().add(c.getName());

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
        configurationStatus = "";

        Config tempConfig;
        boolean configExists = false;
        for (int i = 0; i < configurationsList.size(); i++) {
            if (configurationsList.get(i).getName().equals(name.getText())
                    || toEdit != null && configurationsList.get(i).getName().equals(toEdit.getName())) {
                System.out.println("Configuration already exists \n");
                tempConfig = configurationsList.get(i);
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

    @FXML
    private void deleteConfiguration() {

        if (configurationsList.isEmpty()) {
            System.out.println("Nothing to delete. Configurations list is empty.");
            return;
        }

        Config configToRemove = null;
        String configNameToRemove = configurations.getValue();

        for (Config c : configurationsList) {
            if (c.getName().equals(configNameToRemove)) {
                configToRemove = c;
                break;
            }
        }

        if (configToRemove != null) {
            configurations.getItems().remove(configNameToRemove);
            configurationsList.remove(configToRemove);
            System.out.println("Configuration deleted:" + "\n + name: " +
                    configNameToRemove + "\n + path: " + configToRemove.getCompilerPath() + "\n + libraries: "
                    + configToRemove.getLibraries() + "\n + source: " + configToRemove.getSource() + "\n");
            saveConfigurationsToFile(configurationsList);
        } else {
            System.out.println("Configuration not found: " + configNameToRemove);
        }
        configurations.getSelectionModel().selectFirst();
    }

    @FXML
    private void editScreen() {
        stackPane.getChildren().get(1).setVisible(false);
        configurations.getSelectionModel().selectFirst();

        String selectedConfigName = configurations.getValue();

        for (Config c : configurationsList) {
            if (c.getName().equals(selectedConfigName)) {
                toEdit = c;
                name.setText(c.getName());
                source.setValue(c.getSource());
                libraries.setText(c.getLibraries());
                path.setText(c.getCompilerPath());

            }
        }
        stackPane.getChildren().get(0).setVisible(true);
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

    private ArrayList<Config> readConfigurationsFromFile() {
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
