package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.Map;
import hu.alkfejl.model.Snake;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;


public class SettingsMenuController implements Initializable
{
    /* PROPERTIES */
    @FXML private CheckBox m_UpWallCheck;
    @FXML private CheckBox m_DownWallCheck;
    @FXML private CheckBox m_LeftWallCheck;
    @FXML private CheckBox m_RightWallCheck;
    @FXML private Button m_CancelButton;
    @FXML private Button m_SaveButton;

    private final ObjectProperty<Map> m_Map;
    private final ObjectProperty<Snake> m_P1Snake;
    private final ObjectProperty<Snake> m_P2Snake;


    /* CONSTRUCTORS */
    public SettingsMenuController()
    {
        m_Map = new SimpleObjectProperty<>();
        m_P1Snake = new SimpleObjectProperty<>();
        m_P2Snake = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // TODO
    }

    public void start()
    {
        if ((m_Map.get().getWalls() & Map.Wall.UP.get()) == Map.Wall.UP.get())
            m_UpWallCheck.setSelected(true);
        if ((m_Map.get().getWalls() & Map.Wall.DOWN.get()) == Map.Wall.DOWN.get())
            m_DownWallCheck.setSelected(true);
        if ((m_Map.get().getWalls() & Map.Wall.LEFT.get()) == Map.Wall.LEFT.get())
            m_LeftWallCheck.setSelected(true);
        if ((m_Map.get().getWalls() & Map.Wall.RIGHT.get()) == Map.Wall.RIGHT.get())
            m_RightWallCheck.setSelected(true);
    }


    /* PROPERTY GETTERS */
    public ObjectProperty<Map> mapProperty() { return m_Map; }
    public ObjectProperty<Snake> p1SnakeProperty() { return m_P1Snake; }
    public ObjectProperty<Snake> p2SnakeProperty() { return m_P2Snake; }


    @FXML
    private void onCancel()
    {
        var loader = App.loadWindow("main_menu.fxml");
        var controller = loader.<MainMenuController>getController();
        controller.mapProperty().bindBidirectional(m_Map);
        controller.p1SnakeProperty().bindBidirectional(m_P1Snake);
        controller.p2SnakeProperty().bindBidirectional(m_P2Snake);
    }


    @FXML
    private void onSave()
    {
        // TODO
    }
}
