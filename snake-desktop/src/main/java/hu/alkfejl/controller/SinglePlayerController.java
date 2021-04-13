package hu.alkfejl.controller;

import hu.alkfejl.model.*;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
                var pane = new StackPane();
                pane.setAlignment(Pos.CENTER);
                pane.getChildren().add(new Label("                   "));
                m_Grid.add(pane, j, i);
                pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        m_Snake.get().getBodyCoords().addListener((ListChangeListener<Vector2>) listener ->
        {
            listener.next();
            for (var child : m_Grid.getChildren())
            {
                if (GridPane.getColumnIndex(child) == null)
                {
                    Platform.runLater(() -> m_Grid.getChildren().remove(child));
                    return;
                }

                var pos = new Vector2(GridPane.getColumnIndex(child), GridPane.getRowIndex(child));
                if (m_Snake.get().getBodyCoords().contains(pos))
                    ((Pane) child).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                else if (m_Map.get().getFoodOnPoint(pos) != null)
                    ((Pane) child).setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                else
                    ((Pane) child).setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });

        System.out.println(m_Grid.getChildren().size());

        gameStateObjectProperty.bind(m_GameManager.getStateProperty());
        gameStateObjectProperty.addListener((event, oldValue, newValue) ->
        {
            if (newValue == GameManager.GameState.ENDED)
                System.out.println("well its over bois");
        });

        m_GameManager.setSpeed(2);
        m_Grid.gridLinesVisibleProperty().setValue(true);
        m_GameManager.startGame();
    }
}
