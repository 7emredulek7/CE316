package com.example.iae;

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
    private TextField path;
    @FXML
    private TextField libraries;
    @FXML
    private ChoiceBox configurations;

    public void initialize() {
        stackPane.getChildren().get(0).setVisible(false);
        stackPane.getChildren().get(1).setVisible(false);

        if (configurationStatus.equals("create new configuration"))
            stackPane.getChildren().get(0).setVisible(true);
        else if (configurationStatus.equals("edit selected configuration")) {
            stackPane.getChildren().get(0).setVisible(true);
            /*
            * name = configurastion.name
            * ...
            * */
        }
        else if(configurationStatus.equals("edit configurations"))
            stackPane.getChildren().get(1).setVisible(true);
        /**
         * if create new configuration
         *  stackPane.getChildren().get(0).setVisible(true)
         * else if edit new configuratiom
         *  stackPane.get(0).setVisible(true)
         *  name = confiration.name
         *  .
         *  .
         *  .
         *
         *  else
         *  stackPane.getChildren().get(1).setVisible(true)
         *
         */
    }

    @FXML
    private void save() {
        configurationStatus = "";
        // create configuration object and/or file
        // if configuration already exists, then edit that configuration
        Stage stage = (Stage) name.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }
    @FXML
    private void deleteProject(){
        //configurations.remove(selectedConfiguration)
        // delete it from project as well
    }
    @FXML
    private void editScreen(){
        stackPane.getChildren().get(1).setVisible(false);
        //name = selectedCOnfiguraton.name
        //...
        stackPane.getChildren().get(0).setVisible(true);
    }
}
