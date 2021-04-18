package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.*;
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
        if (!App.getWindowManager().showScene("SinglePlayer"))
        {
            var loader = App.getWindowManager().createScene("SinglePlayer", "single_player.fxml");
            var controller = loader.<SinglePlayerController>getController();
            System.out.println(controller);
            controller.gameManagerProperty().bindBidirectional(m_SinglePlayerGameManager);
            controller.start();
            App.getWindowManager().showScene("SinglePlayer");
        }
        /*var view = new SinglePlayerView(App.getStage());
        var controller = view.getController();
        controller.gameManagerProperty().bind(m_SinglePlayerGameManager);
        view.createBindings();
        controller.start();*/
    }


    @FXML
    private void startMultiPlayer()
    {
        // TODO
    }


    @FXML
    private void openSettings()
    {
        if (!App.getWindowManager().showScene("SettingsMenu"))
        {
            var loader = App.getWindowManager().createScene("SettingsMenu", "settings_menu.fxml");
            var controller = loader.<SettingsMenuController>getController();
            controller.singlePlayerControllerProperty().bindBidirectional(m_SinglePlayerGameManager);
            controller.start();
            App.getWindowManager().showScene("SettingsMenu");
        }

    }


    @FXML
    private void exitApp()
    {
        Platform.exit();
    }
}
