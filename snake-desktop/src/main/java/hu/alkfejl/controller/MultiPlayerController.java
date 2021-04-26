package hu.alkfejl.controller;

import hu.alkfejl.model.MultiPlayerGameManager;
import hu.alkfejl.model.Snake;
import hu.alkfejl.model.Vector2;
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
        // SET INPUT HANDLER
        m_Grid.getScene().setOnKeyPressed(this::keyCallback);
        m_Grid.getScene().setOnKeyReleased(this::keyCallback);

        // RENDER "CYCLE"
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



                if (manager.getMap().getFood() != null && manager.getMap().getFood().getKey().equals(pos))
                    ((Rectangle) child).setFill(manager.getMap().getFood().getValue().getColor());
                else
                    ((Rectangle) child).setFill(Color.BLACK);

                if (manager.isSnake1Alive())
                {
                    if (manager.getSnake().getBodyCoords().get(0).equals(pos))
                        ((Rectangle) child).setFill(manager.getSnake().getHeadColor());
                    else if (manager.getSnake().getBodyCoords().contains(pos))
                        ((Rectangle) child).setFill(manager.getSnake().getBodyColor());
                }

                if (manager.isSnake2Alive())
                {
                    if (manager.getSnake2().getBodyCoords().get(0).equals(pos))
                    ((Rectangle) child).setFill(manager.getSnake2().getHeadColor());
                    else if (manager.getSnake2().getBodyCoords().contains(pos))
                        ((Rectangle) child).setFill(manager.getSnake2().getBodyColor());
                }
            }
        });

        // END GAME NOTIFICATION
        m_GameManager.get().gameStateProperty().addListener((event, oldValue, newValue) ->
        {
            switch (newValue)
            {
                case P1_WON:
                    System.out.println("p1 won");
                    return;

                case P2_WON:
                    System.out.println("p2 won");
                    return;

                case ALL_DEAD:
                    System.out.println("nobody won");
            }
        });

        reset();
    }


    @Override
    protected void keyCallback(KeyEvent event)
    {
        var manager = (MultiPlayerGameManager) m_GameManager.get();

        if (event.getEventType() == KeyEvent.KEY_PRESSED)
            switch (event.getCode())
            {
                case W:
                    if (manager.getSnake().getCurrentDirection() == Snake.Direction.UP)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake().getCurrentDirection() == Snake.Direction.DOWN)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake().setNextDirection(Snake.Direction.UP);
                    return;

                case UP:
                    if (manager.getSnake2().getCurrentDirection() == Snake.Direction.UP)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake2().getCurrentDirection() == Snake.Direction.DOWN)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake2().setNextDirection(Snake.Direction.UP);
                    return;

                case S:
                    if (manager.getSnake().getCurrentDirection() == Snake.Direction.DOWN)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake().getCurrentDirection() == Snake.Direction.UP)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake().setNextDirection(Snake.Direction.DOWN);
                    return;

                case DOWN:
                    if (manager.getSnake2().getCurrentDirection() == Snake.Direction.DOWN)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake2().getCurrentDirection() == Snake.Direction.UP)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake2().setNextDirection(Snake.Direction.DOWN);
                    return;

                case A:
                    if (manager.getSnake().getCurrentDirection() == Snake.Direction.LEFT)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake().getCurrentDirection() == Snake.Direction.RIGHT)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake().setNextDirection(Snake.Direction.LEFT);
                    return;

                case LEFT:
                    if (manager.getSnake2().getCurrentDirection() == Snake.Direction.LEFT)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake2().getCurrentDirection() == Snake.Direction.RIGHT)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake2().setNextDirection(Snake.Direction.LEFT);
                    return;

                case D:
                    if (manager.getSnake().getCurrentDirection() == Snake.Direction.RIGHT)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake().getCurrentDirection() == Snake.Direction.LEFT)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake().setNextDirection(Snake.Direction.RIGHT);
                    return;

                case RIGHT:
                    if (manager.getSnake2().getCurrentDirection() == Snake.Direction.RIGHT)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake2().getCurrentDirection() == Snake.Direction.LEFT)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake2().setNextDirection(Snake.Direction.RIGHT);
                    return;

                case E:
                    if (!manager.getHungerSkill().isOnCooldown())
                        manager.getHungerSkill().setLastUsed(Instant.now());
                    return;

                case M:
                    if (!manager.getHungerSkill2().isOnCooldown())
                        manager.getHungerSkill2().setLastUsed(Instant.now());
            }
        else
        {
            switch (event.getCode())
            {
                case W:
                case A:
                case S:
                case D:
                    manager.setSnake1Boost(MultiPlayerGameManager.Boost.NONE);
                    return;

                case UP:
                case LEFT:
                case DOWN:
                case RIGHT:
                    manager.setSnake2Boost(MultiPlayerGameManager.Boost.NONE);
            }
        }
    }
}
