package hu.alkfejl.controller;

import hu.alkfejl.model.GameManager;
import hu.alkfejl.model.Map;
import hu.alkfejl.model.Snake;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;


public abstract class GameWindowController
{
    protected GridPane m_Grid;

    protected GameManager m_GameManager;
    protected ObjectProperty<Snake> m_Snake;
    protected ObjectProperty<Map> m_Map;


    public GameWindowController(GridPane root)
    {
        m_Grid = root;
        m_Snake = new SimpleObjectProperty<>();
        m_Map = new SimpleObjectProperty<>();
    }


    public abstract void start();


    public ObjectProperty<Snake> snakeProperty()
    {
        return m_Snake;
    }
    public ObjectProperty<Map> mapProperty()
    {
        return m_Map;
    }

    public Snake getSnake() { return m_Snake.get(); }
    public Map getMap() { return m_Map.get(); }


    @FXML
    private void keyCallback(KeyEvent event)
    {
        System.out.println("Key pressed: " + event.getText() + "."); // DEBUG
        switch (event.getCode())
        {
            case W:
                if (m_Snake.get().getCurrentDirection() != Snake.Direction.DOWN && m_Snake.get().getNextDirection() != Snake.Direction.DOWN)
                    m_GameManager.getSnake().setNextDirection(Snake.Direction.UP);
                return;
            case S:
                if (m_Snake.get().getCurrentDirection() != Snake.Direction.UP && m_Snake.get().getNextDirection() != Snake.Direction.UP)
                    m_GameManager.getSnake().setNextDirection(Snake.Direction.DOWN);
                return;
            case A:
                if (m_Snake.get().getCurrentDirection() != Snake.Direction.RIGHT && m_Snake.get().getNextDirection() != Snake.Direction.RIGHT)
                    m_GameManager.getSnake().setNextDirection(Snake.Direction.LEFT);
                return;
            case D:
                if (m_Snake.get().getCurrentDirection() != Snake.Direction.LEFT && m_Snake.get().getNextDirection() != Snake.Direction.LEFT)
                    m_GameManager.getSnake().setNextDirection(Snake.Direction.RIGHT);
        }
    }
}
