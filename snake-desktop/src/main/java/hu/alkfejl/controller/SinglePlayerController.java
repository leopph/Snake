package hu.alkfejl.controller;

import hu.alkfejl.model.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class SinglePlayerController extends GameWindowController
{
    private ObjectProperty<GameManager.GameState> gameStateObjectProperty;


    public SinglePlayerController()
    {
        gameStateObjectProperty = new SimpleObjectProperty<>();
    }


    @Override
    public void start()
    {
        m_GameManager = new SinglePlayerGameManager(m_Snake.get(), m_Map.get());

        for (int i = 0; i < m_Map.get().getSize().getX(); i++)
            for (int j = 0; j < m_Map.get().getSize().getY(); j++)
            {
                var pane = new Pane();
                m_Grid.add(pane, j, i);
                pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            }

        gameStateObjectProperty.bind(m_GameManager.getStateProperty());
        gameStateObjectProperty.addListener((event, oldValue, newValue) ->
        {
            if (newValue == GameManager.GameState.ENDED)
                System.out.println("well its over bois");
        });

        m_GameManager.setSpeed(1);
        m_GameManager.startGame();
    }
}
