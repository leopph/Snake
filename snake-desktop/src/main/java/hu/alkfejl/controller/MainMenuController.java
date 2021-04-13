package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.Map;
import hu.alkfejl.model.Snake;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MainMenuController implements Initializable
{
    private ObjectProperty<Snake> m_P1Snake;
    private ObjectProperty<Snake> m_P2Snake;
    private ObjectProperty<Map> m_Map;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        m_P1Snake = new SimpleObjectProperty<>();
        m_P2Snake = new SimpleObjectProperty<>();
        m_Map = new SimpleObjectProperty<>();
    }


    @FXML
    private void startSinglePlayer()
    {
        App.loadWindow("single_player_map.fxml");
    }
}
