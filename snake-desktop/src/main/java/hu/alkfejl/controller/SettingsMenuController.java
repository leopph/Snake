package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.Food;
import hu.alkfejl.model.GameManager;
import hu.alkfejl.model.Map;
import hu.alkfejl.model.MultiPlayerGameManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
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
    @FXML private ColorPicker m_P1HeadColor;
    @FXML private ColorPicker m_P1BodyColor;
    @FXML private ColorPicker m_P2HeadColor;
    @FXML private ColorPicker m_P2BodyColor;
    @FXML private ColorPicker m_AppleColor;
    @FXML private ColorPicker m_CucumberColor;
    @FXML private ColorPicker m_PeachColor;
    @FXML private ColorPicker m_BananaColor;
    @FXML private TextField m_P1NameInput;
    @FXML private TextField m_P2NameInput;

    private final ObjectProperty<GameManager> m_SinglePlayerGameManager;
    private final ObjectProperty<GameManager> m_MultiPlayerGameManager;


    /* CONSTRUCTORS */
    public SettingsMenuController()
    {
        m_SinglePlayerGameManager = new SimpleObjectProperty<>();
        m_MultiPlayerGameManager = new SimpleObjectProperty<>();
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

        m_MultiPlayerGameManager.get().tickRateProperty().bind(m_SinglePlayerGameManager.get().tickRateProperty());

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

        m_P1HeadColor.valueProperty().bindBidirectional(m_SinglePlayerGameManager.get().getSnake().headColorProperty());
        m_P1BodyColor.valueProperty().bindBidirectional(m_SinglePlayerGameManager.get().getSnake().bodyColorProperty());
        m_P2HeadColor.valueProperty().bindBidirectional(((MultiPlayerGameManager) m_MultiPlayerGameManager.get()).getSnake2().headColorProperty());
        m_P2BodyColor.valueProperty().bindBidirectional(((MultiPlayerGameManager) m_MultiPlayerGameManager.get()).getSnake2().bodyColorProperty());

        m_AppleColor.valueProperty().bindBidirectional(Food.appleColorProperty());
        m_CucumberColor.valueProperty().bindBidirectional(Food.cucumberColorProperty());
        m_PeachColor.valueProperty().bindBidirectional(Food.peachColorProperty());
        m_BananaColor.valueProperty().bindBidirectional(Food.bananaColorProperty());

        m_P1NameInput.textProperty().bindBidirectional(m_SinglePlayerGameManager.get().playerNameProperty());
        m_MultiPlayerGameManager.get().playerNameProperty().bind(m_P1NameInput.textProperty());

        m_P2NameInput.textProperty().bindBidirectional(((MultiPlayerGameManager) m_MultiPlayerGameManager.get()).player2NameProperty());
    }


    /* PROPERTY GETTERS */
    public ObjectProperty<GameManager> singlePlayerGameManagerProperty() { return m_SinglePlayerGameManager; }
    public ObjectProperty<GameManager> multiPlayerGameManagerProperty() { return m_MultiPlayerGameManager; }


    @FXML
    private void onBack()
    {
        App.getWindowManager().showScene("MainMenu");
        /*var loader = App.loadWindow("main_menu.fxml");
        var controller = loader.<MainMenuController>getController();
        controller.singlePlayerGameManagerProperty().bind(m_SinglePlayerGameManager);*/
    }
}
