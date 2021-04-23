package hu.alkfejl.controller;

import hu.alkfejl.model.MultiPlayerGameManager;
import hu.alkfejl.model.Snake;
import hu.alkfejl.model.Vector2;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.Instant;


public class MultiPlayerController extends GameWindowController
{
    @Override
    public void start()
    {
        m_Grid.getScene().setOnKeyPressed(this::keyCallback);

        ((MultiPlayerGameManager) m_GameManager.get()).ticksProperty().addListener(event ->
        {
            var manager = ((MultiPlayerGameManager) m_GameManager.get());

            if (manager.getSnake().getBodyCoords().size() == 0 || manager.getSnake2().getBodyCoords().size() == 0)
                return;

            for (var child : m_Grid.getChildren())
            {
                if (GridPane.getColumnIndex(child) == null)
                    return;

                var pos = new Vector2(GridPane.getColumnIndex(child), GridPane.getRowIndex(child));

                if (manager.getSnake().getBodyCoords().get(0).equals(pos))
                    ((Rectangle) child).setFill(manager.getSnake().getHeadColor());
                else if (manager.getSnake2().getBodyCoords().get(0).equals(pos))
                    ((Rectangle) child).setFill(manager.getSnake2().getHeadColor());
                else if (manager.getSnake().getBodyCoords().contains(pos))
                    ((Rectangle) child).setFill(manager.getSnake().getBodyColor());
                else if (manager.getSnake2().getBodyCoords().contains(pos))
                    ((Rectangle) child).setFill(manager.getSnake2().getBodyColor());
                else if (manager.getMap().getFood() != null && manager.getMap().getFood().getKey().equals(pos))
                    ((Rectangle) child).setFill(manager.getMap().getFood().getValue().getColor());
                else
                    ((Rectangle) child).setFill(Color.BLACK);
            }
        });

        reset();
    }


    @Override
    protected void keyCallback(KeyEvent event)
    {
        super.keyCallback(event);

        var manager = (MultiPlayerGameManager) m_GameManager.get();

        switch (event.getCode())
        {
            case UP:
                if (manager.getSnake2().getCurrentDirection() != Snake.Direction.DOWN)
                    manager.getSnake2().setNextDirection(Snake.Direction.UP);
                return;

            case DOWN:
                if (manager.getSnake2().getCurrentDirection() != Snake.Direction.UP)
                    manager.getSnake2().setNextDirection(Snake.Direction.DOWN);
                return;

            case LEFT:
                if (manager.getSnake2().getCurrentDirection() != Snake.Direction.RIGHT)
                    manager.getSnake2().setNextDirection(Snake.Direction.LEFT);
                return;

            case RIGHT:
                if (manager.getSnake2().getCurrentDirection() != Snake.Direction.LEFT)
                    manager.getSnake2().setNextDirection(Snake.Direction.RIGHT);
                return;

            case M:
                if (!manager.getHungerSkill2().isOnCooldown())
                    manager.getHungerSkill2().setLastUsed(Instant.now());
        }
    }
}
