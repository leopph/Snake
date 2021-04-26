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
    private SinglePlayerController m_SinglePlayerController;

    private ObjectProperty<GameManager> m_MultiPlayerGameManager;
    private MultiPlayerController m_MultiPlayerController;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // SHARE MAP AND SNAKES ACROSS GAME TYPES
        Snake p1Snake = new Snake();
        Snake p2Snake = new Snake();
        Map map = new Map();

        // GAMES MANAGERS STORE THESE
        m_SinglePlayerGameManager = new SimpleObjectProperty<>(new SinglePlayerGameManager());
        m_SinglePlayerGameManager.get().setSnake(p1Snake);
        m_SinglePlayerGameManager.get().setMap(map);

        m_MultiPlayerGameManager = new SimpleObjectProperty<>(new MultiPlayerGameManager());
        m_MultiPlayerGameManager.get().setSnake(p1Snake);
        ((MultiPlayerGameManager) m_MultiPlayerGameManager.get()).setSnake2(p2Snake);
        m_MultiPlayerGameManager.get().setMap(map);
    }


    public ObjectProperty<GameManager> singlePlayerGameManagerProperty() { return m_SinglePlayerGameManager; }
    public ObjectProperty<GameManager> multiPlayerGameManagerProperty() { return m_MultiPlayerGameManager; }


    @FXML
    private void startSinglePlayer()
    {
        if (!App.getWindowManager().showScene("SinglePlayer"))
        {
            var loader = App.getWindowManager().createScene("SinglePlayer", "single_player.fxml");
            m_SinglePlayerController = loader.getController();
            m_SinglePlayerController.gameManagerProperty().bindBidirectional(m_SinglePlayerGameManager);
            m_SinglePlayerController.start();
            App.getWindowManager().showScene("SinglePlayer");
        }
        else
            m_SinglePlayerController.reset();
    }


    @FXML
    private void startMultiPlayer()
    {
        if (!App.getWindowManager().showScene("MultiPlayer"))
        {
            var loader = App.getWindowManager().createScene("MultiPlayer", "multi_player.fxml");
            m_MultiPlayerController = loader.getController();
            m_MultiPlayerController.gameManagerProperty().bindBidirectional(m_MultiPlayerGameManager);
            m_MultiPlayerController.start();
            App.getWindowManager().showScene("MultiPlayer");
        }
        else
            m_MultiPlayerController.reset();
    }


    @FXML
    private void openSettings()
    {
        if (!App.getWindowManager().showScene("SettingsMenu"))
        {
            var loader = App.getWindowManager().createScene("SettingsMenu", "settings_menu.fxml");
            var controller = loader.<SettingsMenuController>getController();
            controller.singlePlayerGameManagerProperty().bindBidirectional(m_SinglePlayerGameManager);
            controller.multiPlayerGameManagerProperty().bindBidirectional(m_MultiPlayerGameManager);
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
