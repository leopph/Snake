package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.GameManager;
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
                            return Double.parseDouble(string);
                        }
                        catch (Exception e)
                        {
                            return 1;
                        }
                    }
                });
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
