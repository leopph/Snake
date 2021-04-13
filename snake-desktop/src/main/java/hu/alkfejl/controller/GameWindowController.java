package hu.alkfejl.controller;

import hu.alkfejl.model.GameManager;
import hu.alkfejl.model.Map;
import hu.alkfejl.model.Snake;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;


public abstract class GameWindowController implements Initializable
{
    @FXML protected GridPane m_Grid;

    protected GameManager m_GameManager;
    protected ObjectProperty<Snake> m_Snake;
    protected ObjectProperty<Map> m_Map;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        m_Snake = new SimpleObjectProperty<>();
        m_Map = new SimpleObjectProperty<>();
    }


    public ObjectProperty<Snake> getSnakeProperty()
    {
        return m_Snake;
    }
    public ObjectProperty<Map> getMapProperty()
    {
        return m_Map;
    }

    public Snake getSnake() { return m_Snake.get(); }
    public Map getMap() { return m_Map.get(); }


    @FXML
    private void keyCallback(KeyEvent event)
    {
        System.out.println(event.getText());
        switch (event.getCode())
        {
            case W:
                m_GameManager.getSnake().changeDirection(Snake.Direction.UP);
                return;
            case S:
                m_GameManager.getSnake().changeDirection(Snake.Direction.DOWN);
                return;
            case A:
                m_GameManager.getSnake().changeDirection(Snake.Direction.LEFT);
                return;
            case D:
                m_GameManager.getSnake().changeDirection(Snake.Direction.RIGHT);
        }
    }
}
