package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.*;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class SinglePlayerController extends GameWindowController
{
    public SinglePlayerController(GridPane root)
    {
        super(root);
    }


    @Override
    public void start()
    {
        m_Grid.setAlignment(Pos.CENTER);
        m_Grid.getScene().setOnKeyPressed(this::keyCallback);

        var sizeBinding = Bindings.min(m_Grid.widthProperty().divide(m_GameManager.get().getMap().getSizeX()),
                                                    m_Grid.heightProperty().divide(m_GameManager.get().getMap().getSizeY()));

        /* CREATE INITIAL GRID */
        for (int i = 0; i < m_GameManager.get().getMap().getSizeX(); i++)
            for (int j = 0; j < m_GameManager.get().getMap().getSizeY(); j++)
            {
                var rect = new Rectangle();
                rect.widthProperty().bind(sizeBinding);
                rect.heightProperty().bind(sizeBinding);
                rect.setFill(Color.BLACK);
                m_Grid.add(rect, i, j);
            }


        /* RENDER CYCLE FOR SNAKE */
        ListChangeListener<Vector2> listener = event ->
        {
            /* DEBUG */
            System.out.println("---SNAKE BODY COORDS---");
            for (var pos : m_GameManager.get().getSnake().getBodyCoords())
                System.out.println(pos);
            System.out.println("---END SNAKE COORDS---");
            /* NDEBUG */


            for (var child : m_Grid.getChildren())
            {
                if (GridPane.getColumnIndex(child) == null)
                    return;

                var pos = new Vector2(GridPane.getColumnIndex(child), GridPane.getRowIndex(child));
                if (m_GameManager.get().getSnake().getBodyCoords().get(0).equals(pos))
                    ((Rectangle) child).setFill(m_GameManager.get().getSnake().getHeadColor());
                else if (m_GameManager.get().getSnake().getBodyCoords().contains(pos))
                    ((Rectangle) child).setFill(m_GameManager.get().getSnake().getBodyColor());
                else if (m_GameManager.get().getMap().getFood() != null && m_GameManager.get().getMap().getFood().getKey().equals(pos))
                    ((Rectangle) child).setFill(m_GameManager.get().getMap().getFood().getValue().getColor());
                else
                    ((Rectangle) child).setFill(Color.BLACK);
            }
        };

        m_GameManager.get().getSnake().getBodyCoords().addListener(listener);
        m_GameManager.get().snakeProperty().addListener(event -> m_GameManager.get().getSnake().getBodyCoords().addListener(listener));


        /* RENDER CYCLE FOR FOODS */
        m_GameManager.get().getMap().foodProperty().addListener((observable, oldValue, newValue) ->
        {
            for (var child : m_Grid.getChildren())
            {
                var x = GridPane.getColumnIndex(child);
                var y = GridPane.getRowIndex(child);

                if (x != null && y != null && newValue.getKey().getX() == x && newValue.getKey().getY() == y)
                    ((Rectangle) child).setFill(m_GameManager.get().getMap().getFood().getValue().getColor());
            }
        });


        /* GET NOTIFIED WHEN GAME ENDS */
        m_GameManager.get().gameStateProperty().addListener((event, oldValue, newValue) ->
        {
            switch (newValue)
            {
                case P1_WON:
                    System.out.println("Good, good. You won.");
                    App.loadWindow("main_menu.fxml").<MainMenuController>getController().singlePlayerGameManagerProperty().bind(m_GameManager);
                    break;
                case WALL_HIT:
                    System.out.println("Well, you hit a wall.");
                    App.loadWindow("main_menu.fxml").<MainMenuController>getController().singlePlayerGameManagerProperty().bind(m_GameManager);
                    break;
                case SELF_ATE:
                    System.out.println("Well, you ate yourself. Congrats.");
                    App.loadWindow("main_menu.fxml").<MainMenuController>getController().singlePlayerGameManagerProperty().bind(m_GameManager);
            }
        });

        m_Grid.setGridLinesVisible(true); // DEBUG

        m_GameManager.get().startGame();
    }
}
