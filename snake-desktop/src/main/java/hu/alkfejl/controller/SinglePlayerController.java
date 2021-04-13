package hu.alkfejl.controller;

import hu.alkfejl.model.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;


public class SinglePlayerController implements Initializable
{
    @FXML private GridPane m_Grid;

    private GameManager m_GameManager;
    private ObjectProperty<GameManager.GameState> gameStateObjectProperty;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        m_GameManager = new SinglePlayerGameManager(new Snake(), new Map());
        gameStateObjectProperty = new SimpleObjectProperty<>();

        gameStateObjectProperty.bind(m_GameManager.getStateProperty());
        gameStateObjectProperty.addListener((event, oldValue, newValue) ->
        {
            if (newValue == GameManager.GameState.ENDED)
                System.out.println("well its over bois");
        });

        m_GameManager.setSpeed(1);
        m_GameManager.getMap().setSize(new Vector2(15, 15));
        m_GameManager.startGame();
    }


    @FXML
    private void keyCallback(KeyEvent event)
    {
        System.out.println(event.getText());
        switch (event.getCode())
        {
            case W:
                m_GameManager.getSnake().changeDirection(Snake.Direction.UP);
                return;
            case S:
                m_GameManager.getSnake().changeDirection(Snake.Direction.DOWN);
                return;
            case A:
                m_GameManager.getSnake().changeDirection(Snake.Direction.LEFT);
                return;
            case D:
                m_GameManager.getSnake().changeDirection(Snake.Direction.RIGHT);
        }
    }
}
