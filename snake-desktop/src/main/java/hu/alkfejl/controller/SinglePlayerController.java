package hu.alkfejl.controller;

import hu.alkfejl.model.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.URL;
import java.util.ResourceBundle;


public class SinglePlayerController extends GameWindowController
{
    private ObjectProperty<GameManager.GameState> gameStateObjectProperty;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        super.initialize(url, resourceBundle);

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
}
