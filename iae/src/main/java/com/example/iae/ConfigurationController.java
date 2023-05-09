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

public class ConfigurationController extends MainController{
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

    private String[] supportedLanguages = {"Java", "Python", "C", "C++", "Dart"};

    public void initialize() {
        for(String s : supportedLanguages){
            source.getItems().add(s);
        }
        source.getSelectionModel().selectFirst();
        configurationsList = readConfigurationsFromFile();

        for (Config c: configurationsList)
            configurations.getItems().add(c.getName());

        stackPane.getChildren().get(0).setVisible(false);
        stackPane.getChildren().get(1).setVisible(false);

        if (configurationStatus.equals("create new configuration"))
            stackPane.getChildren().get(0).setVisible(true);
        else if (configurationStatus.equals("edit selected configuration"))
            editScreen();
        else if(configurationStatus.equals("edit configurations"))
            stackPane.getChildren().get(1).setVisible(true);
    }

    @FXML
private void save() {
    configurationStatus = "";

    Config tempConfig;
    boolean configExists = false;
    for (int i = 0; i < configurationsList.size(); i++) {
        if (configurationsList.get(i).getName().equals(name.getText())) {
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

    // create configuration object and/or file
    saveConfigurationsToFile(configurationsList);

    // if configuration already exists, then edit that configuration
    Stage stage = (Stage) name.getScene().getWindow();
    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    
}



@FXML
private void deleteConfiguration(){
    // TODO: THERE EXIST A BUG WHILE USING THE SAVECONFIGURTIONSTOFILE METHOD
    if (configurationsList.isEmpty()) {
        return; // Exit the method early if the list is empty
    }

    for(Config c : configurationsList) {
        if(c.getName().equals(configurations.getValue())) {
            Config temp = c;
            String tempName = temp.getName();
            configurations.getItems().remove(tempName);
            configurationsList.remove(temp);
            System.out.println("Configuration deleted:" + "\n + name: " +
                    tempName + "\n + path: " + temp.getCompilerPath() + "\n + libraries: "
                    + temp.getLibraries() + "\n + source: " + temp.getSource() + "\n");
            File file = new File("configuration.cfg");
            file.delete();
        }
    }
    saveConfigurationsToFile(configurationsList);
}

    @FXML
    private void editScreen(){
        stackPane.getChildren().get(1).setVisible(false);

        for(Config c : configurationsList)
            if(c.getName().equals(configurations.getValue())){
                Config temp = c;
                name.setText(temp.getName());
                source.setValue(temp.getSource());
                libraries.setText(temp.getLibraries());
                path.setText(temp.getCompilerPath());
            }
       
        stackPane.getChildren().get(0).setVisible(true);
    }

    
    
    private void saveConfigurationsToFile(ArrayList<Config> configurationsList) {
        // TODO: FIX THE UNENDING NIGHTMARE OF EXCEPTIONS
        if (configurationsList.isEmpty()) {
            System.out.println("Nothing to save. Configurations list is empty.");
            return; 
        }
    
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
            e.printStackTrace();
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
