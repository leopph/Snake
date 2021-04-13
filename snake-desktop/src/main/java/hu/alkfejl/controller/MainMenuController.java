package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.Map;
import hu.alkfejl.model.Snake;
import hu.alkfejl.model.Vector2;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class MainMenuController implements Initializable
{
    private ObjectProperty<Snake> m_P1Snake;
    private ObjectProperty<Snake> m_P2Snake;
    private ObjectProperty<Map> m_Map;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        m_P1Snake = new SimpleObjectProperty<>(new Snake());
        m_P2Snake = new SimpleObjectProperty<>(new Snake());
        m_Map = new SimpleObjectProperty<>(new Map());
    }


    @FXML
    private void startSinglePlayer()
    {
        var loader = App.loadWindow("single_player_map.fxml");
        var controller = loader.<SinglePlayerController>getController();
        controller.getSnakeProperty().bindBidirectional(m_P1Snake);
        controller.getMapProperty().bindBidirectional(m_Map);
        m_Map.get().setSize(new Vector2(15, 15)); // DEBUG
        controller.start();
    }


    @FXML
    private void startMultiPlayer()
    {
        var loader = App.loadWindow("multi_player_map.fxml");
        var controller = loader.<MultiPlayerController>getController();
        controller.getMapProperty().bindBidirectional(m_Map);
        controller.getSnakeProperty().bindBidirectional(m_P1Snake);
        controller.getP2SnakeProperty().bindBidirectional(m_P2Snake);
        controller.start();
    }


    @FXML
    private void openSettings()
    {
        // TODO
    }


    @FXML
    private void exitApp()
    {
        Platform.exit();
    }
}
