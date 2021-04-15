package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.*;
import hu.alkfejl.view.SinglePlayerView;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class MainMenuController implements Initializable
{
    private ObjectProperty<GameManager> m_SinglePlayerGameManager;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // SHARE MAP AND SNAKES ACROSS GAME TYPES
        Snake p1Snake = new Snake();
        Map map = new Map();

        // GAMES MANAGERS STORE THESE
        m_SinglePlayerGameManager = new SimpleObjectProperty<>(new SinglePlayerGameManager());
        m_SinglePlayerGameManager.get().setSnake(p1Snake);
        m_SinglePlayerGameManager.get().setMap(map);
    }


    public ObjectProperty<GameManager> singlePlayerGameManagerProperty() { return m_SinglePlayerGameManager; }


    @FXML
    private void startSinglePlayer()
    {
        var view = new SinglePlayerView(App.getStage());
        var controller = view.getController();
        controller.gameManagerProperty().bind(m_SinglePlayerGameManager);
        view.createBindings();
        controller.start();
    }


    @FXML
    private void startMultiPlayer()
    {
        // TODO
    }


    @FXML
    private void openSettings()
    {
        var loader = App.loadWindow("settings_menu.fxml");
        var controller = loader.<SettingsMenuController>getController();
        controller.singlePlayerControllerProperty().bindBidirectional(m_SinglePlayerGameManager);
        controller.start();
    }


    @FXML
    private void exitApp()
    {
        Platform.exit();
    }
}
