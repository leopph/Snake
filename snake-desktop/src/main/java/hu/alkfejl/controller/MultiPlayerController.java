package hu.alkfejl.controller;

import hu.alkfejl.model.Snake;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.GridPane;


public class MultiPlayerController extends GameWindowController
{
    private ObjectProperty<Snake> m_P2Snake;


    public MultiPlayerController(GridPane root)
    {
        super(root);
        m_P2Snake = new SimpleObjectProperty<>();
    }


    @Override
    public void start()
    {

    }


    public ObjectProperty<Snake> getP2SnakeProperty()
    {
        return m_P2Snake;
    }
    public Snake getSnake()
    {
        return m_Snake.get();
    }
}
