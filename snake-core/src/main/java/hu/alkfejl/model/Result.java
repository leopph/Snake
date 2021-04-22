package hu.alkfejl.model;

import javafx.beans.property.*;

import java.io.Serializable;


public class Result implements Serializable
{
    /* GAME MODE TYPES IN DATABASE */
    public enum GameMode
    {
        SINGLE("SINGLE"), MULTI("MULTI");

        private final String m_Value;
        GameMode(String str) { m_Value = str; }
        public String getValue() { return m_Value; }
    }


    /* PROPERTIES */
    private final StringProperty m_PlayerName;
    private final IntegerProperty m_Score;
    private final ObjectProperty<GameMode> m_GameMode;


    /* CONSTRUCTOR */
    public Result()
    {
        m_PlayerName = new SimpleStringProperty();
        m_Score = new SimpleIntegerProperty();
        m_GameMode = new SimpleObjectProperty<>();
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public StringProperty playerNameProperty() { return m_PlayerName; }
    public IntegerProperty scoreProperty() { return m_Score; }
    public ObjectProperty<GameMode> gameModeProperty() { return m_GameMode; }

    public String getPlayerName() { return m_PlayerName.get(); }
    public Integer getScore() { return m_Score.get(); }
    public GameMode getGameMode() { return m_GameMode.get(); }

    public void setPlayerName(String value) { m_PlayerName.setValue(value); }
    public void setScore(Integer value) { m_Score.setValue(value); }
    public void setGameMode(GameMode value) { m_GameMode.setValue(value);}
}
