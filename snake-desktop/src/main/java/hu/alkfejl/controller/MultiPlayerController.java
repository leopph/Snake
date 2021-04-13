package hu.alkfejl.controller;

import hu.alkfejl.model.Snake;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.URL;
import java.util.ResourceBundle;


public class MultiPlayerController extends GameWindowController
{
    private ObjectProperty<Snake> m_P2Snake;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        super.initialize(url, resourceBundle);
        m_P2Snake = new SimpleObjectProperty<>();
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
