package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;


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

        /* CREATE INITIAL GRID */
        for (int i = 0; i < m_Map.get().getSize().getX(); i++)
            for (int j = 0; j < m_Map.get().getSize().getY(); j++)
            {
                var rect = new Rectangle();
                rect.setWidth(App.getStage().getWidth() > App.getStage().getHeight() ? App.getStage().getHeight() / m_Map.get().getSize().getY() : App.getStage().getWidth() / m_Map.get().getSize().getX());
                rect.setHeight(rect.getWidth());
                rect.setFill(Color.BLACK);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(0);
                m_Grid.add(rect, j, i);
            }


        /* RENDER CYCLE */
        m_Snake.get().getBodyCoords().addListener((ListChangeListener<Vector2>) listener ->
        {
            /* DEBUG */
            System.out.println("---SNAKE BODY COORDS---");
            for (var pos : m_Snake.get().getBodyCoords())
                System.out.println(pos);
            System.out.println("---END SNAKE COORDS---");
            /* NDEBUG */


            for (var child : m_Grid.getChildren())
            {
                if (GridPane.getColumnIndex(child) == null)
                    return;

                var pos = new Vector2(GridPane.getColumnIndex(child), GridPane.getRowIndex(child));
                if (m_Snake.get().getBodyCoords().contains(pos))
                    ((Rectangle) child).setFill(Color.WHITE);
                else if (m_Map.get().getFoodOnPoint(pos) != null)
                    ((Rectangle) child).setFill(Color.RED);
                else
                    ((Rectangle) child).setFill(Color.BLACK);
            }
        });


        m_Map.get().getFoodProperty().addListener((observable, oldValue, newValue) ->
        {
            for (var child : m_Grid.getChildren())
            {
                var x = GridPane.getColumnIndex(child);
                var y = GridPane.getRowIndex(child);

                if (x != null && y != null && newValue.getKey().getX() == x && newValue.getKey().getY() == y)
                    ((Rectangle) child).setFill(Color.RED);
            }
        });


        /* GET NOTIFIED WHEN GAME ENDS */
        gameStateObjectProperty.bind(m_GameManager.getStateProperty());
        gameStateObjectProperty.addListener((event, oldValue, newValue) ->
        {
            if (newValue == GameManager.GameState.ENDED)
                System.out.println("well its over bois");
        });

        m_Grid.setGridLinesVisible(true); // DEBUG

        m_GameManager.setSpeed(2);
        m_GameManager.startGame();
    }
}
