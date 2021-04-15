package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.GameManager;
import hu.alkfejl.model.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;


public class SettingsMenuController
{
    /* PROPERTIES */
    @FXML private CheckBox m_UpWallCheck;
    @FXML private CheckBox m_DownWallCheck;
    @FXML private CheckBox m_LeftWallCheck;
    @FXML private CheckBox m_RightWallCheck;
    @FXML private TextField m_GameSpeedInput;
    @FXML private TextField m_MapRowInput;
    @FXML private TextField m_MapColumnInput;
    
    private final ObjectProperty<GameManager> m_SinglePlayerGameManager;


    /* CONSTRUCTORS */
    public SettingsMenuController()
    {
        m_SinglePlayerGameManager = new SimpleObjectProperty<>();
    }


    public void start()
    {
        m_UpWallCheck.selectedProperty().bindBidirectional(m_SinglePlayerGameManager.get().getMap().upWallProperty());
        m_DownWallCheck.selectedProperty().bindBidirectional(m_SinglePlayerGameManager.get().getMap().downWallProperty());
        m_LeftWallCheck.selectedProperty().bindBidirectional(m_SinglePlayerGameManager.get().getMap().leftWallProperty());
        m_RightWallCheck.selectedProperty().bindBidirectional(m_SinglePlayerGameManager.get().getMap().rightWallProperty());

        Bindings.bindBidirectional(m_GameSpeedInput.textProperty(),
                m_SinglePlayerGameManager.get().tickRateProperty(),
                new StringConverter<>()
                {
                    @Override
                    public String toString(Number object)
                    {
                        return object.toString();
                    }

                    @Override
                    public Number fromString(String string)
                    {
                        try
                        {
                            var ret = Double.parseDouble(string);
                            if (ret <= 0)
                                return GameManager.DEFAULT_TICK_RATE;
                            return ret;
                        }
                        catch (Exception e) { return GameManager.DEFAULT_TICK_RATE; }
                    }
                });

        var mapSizeConverter = new StringConverter<Number>()
        {
            @Override
            public String toString(Number object) { return object.toString(); }

            @Override
            public Number fromString(String string)
            {
                try { return Math.max(Integer.parseInt(string), Map.MIN_SIZE); }
                catch (Exception e) { return Map.MIN_SIZE; }
            }
        };

        Bindings.bindBidirectional(m_MapRowInput.textProperty(),
                m_SinglePlayerGameManager.get().mapProperty().get().sizeYProperty(),
                mapSizeConverter);


        Bindings.bindBidirectional(m_MapColumnInput.textProperty(),
                m_SinglePlayerGameManager.get().mapProperty().get().sizeXProperty(),
                mapSizeConverter);
    }


    /* PROPERTY GETTERS */
    public ObjectProperty<GameManager> singlePlayerControllerProperty() { return m_SinglePlayerGameManager; }


    @FXML
    private void onBack()
    {
        var loader = App.loadWindow("main_menu.fxml");
        var controller = loader.<MainMenuController>getController();
        controller.singlePlayerGameManagerProperty().bind(m_SinglePlayerGameManager);
    }
}
