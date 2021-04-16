package hu.alkfejl.controller;

import hu.alkfejl.model.GameManager;
import hu.alkfejl.model.Snake;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.time.Instant;


public abstract class GameWindowController
{
    protected GridPane m_Grid;
    protected ObjectProperty<GameManager> m_GameManager;


    public GameWindowController(GridPane root)
    {
        m_Grid = root;
        m_GameManager = new SimpleObjectProperty<>();
    }


    public ObjectProperty<GameManager> gameManagerProperty() { return m_GameManager; }
    public GameManager getGameManager() { return m_GameManager.get(); }
    public void setGameManager(GameManager newValue) { m_GameManager.setValue(newValue); }


    public abstract void start();
    
    
    protected void keyCallback(KeyEvent event)
    {
        switch (event.getCode())
        {
            case W:
                if (m_GameManager.get().getSnake().getCurrentDirection() != Snake.Direction.DOWN && m_GameManager.get().getSnake().getNextDirection() != Snake.Direction.DOWN)
                    m_GameManager.get().getSnake().setNextDirection(Snake.Direction.UP);
                return;
            case S:
                if (m_GameManager.get().getSnake().getCurrentDirection() != Snake.Direction.UP && m_GameManager.get().getSnake().getNextDirection() != Snake.Direction.UP)
                    m_GameManager.get().getSnake().setNextDirection(Snake.Direction.DOWN);
                return;
            case A:
                if (m_GameManager.get().getSnake().getCurrentDirection() != Snake.Direction.RIGHT && m_GameManager.get().getSnake().getNextDirection() != Snake.Direction.RIGHT)
                    m_GameManager.get().getSnake().setNextDirection(Snake.Direction.LEFT);
                return;
            case D:
                if (m_GameManager.get().getSnake().getCurrentDirection() != Snake.Direction.LEFT && m_GameManager.get().getSnake().getNextDirection() != Snake.Direction.LEFT)
                    m_GameManager.get().getSnake().setNextDirection(Snake.Direction.RIGHT);
                return;
            case E:
                if (!m_GameManager.get().getHungerSkill().isOnCooldown())
                    m_GameManager.get().getHungerSkill().setLastUsed(Instant.now());
        }
    }
}
