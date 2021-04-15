package hu.alkfejl.view;

import hu.alkfejl.controller.SinglePlayerController;
import hu.alkfejl.model.Map;
import hu.alkfejl.model.Snake;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class SinglePlayerView
{
    /* CALLBACK CONTROLLER */
    private SinglePlayerController m_Controller;

    /* SCENE ROOT */
    private GridPane m_Root;

    /* MODEL PROPERTIES */
    private ObjectProperty<Snake> m_Snake;
    private ObjectProperty<Map> m_Map;


    public SinglePlayerView(Stage stage)
    {
        createLayout(stage);
        createController();
        createProperties();
    }


    /* GET CONTROLLER INSTANCE */
    public SinglePlayerController getController()
    {
        return m_Controller;
    }


    /* SET UP CONTROLLER */
    private void createController()
    {
        m_Controller = new SinglePlayerController(m_Root);
    }


    /* SET UP PROPERTIES */
    private void createProperties()
    {
        m_Snake = new SimpleObjectProperty<>();
        m_Map = new SimpleObjectProperty<>();
    }


    /* SET UP GUI ELEMENTS */
    private void createLayout(Stage stage)
    {
        m_Root = new GridPane();

        if (stage.getScene() == null)
            stage.setScene(new Scene(m_Root));
        else
            stage.getScene().setRoot(m_Root);
    }


    /* BIND CONTROLLER AND VIEW PROPERTIES */
    public void createBindings()
    {
        m_Snake.bind(m_Controller.getGameManager().snakeProperty());
        m_Map.bind(m_Controller.getGameManager().mapProperty());
    }
}
