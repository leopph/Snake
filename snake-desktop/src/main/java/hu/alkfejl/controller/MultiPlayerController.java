package hu.alkfejl.controller;

import hu.alkfejl.model.MultiPlayerGameManager;
import hu.alkfejl.model.Vector2;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MultiPlayerController extends GameWindowController
{
    @Override
    public void start()
    {
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
}
