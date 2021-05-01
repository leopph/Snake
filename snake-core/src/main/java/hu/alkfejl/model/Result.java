package hu.alkfejl.model;

import javafx.beans.property.*;

import java.io.Serializable;
import java.time.Instant;


public class Result implements Serializable
{
    /* ENUM TO REPRESENT GAME MODE */
    public enum GameMode
    {
        SINGLE("SINGLE"), MULTI("MULTI");

        public final String name;
        GameMode(String name) { this.name = name; }
    }


    /* PROPERTIES */
    private final StringProperty m_PlayerName;
    private final IntegerProperty m_Score;
    private final ObjectProperty<Instant> m_Date;
    private final ObjectProperty<GameMode> m_GameMode;
    private final LongProperty m_ID;


    /* CONSTRUCTOR */
    public Result()
    {
        m_PlayerName = new SimpleStringProperty();
        m_Score = new SimpleIntegerProperty();
        m_Date = new SimpleObjectProperty<>();
        m_GameMode = new SimpleObjectProperty<>();
        m_ID = new SimpleLongProperty();
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public StringProperty playerNameProperty() { return m_PlayerName; }
    public IntegerProperty scoreProperty() { return m_Score; }
    public ObjectProperty<Instant> dateProperty() { return m_Date; }
    public ObjectProperty<GameMode> gameModeProperty() { return m_GameMode; }
    public LongProperty idProperty() { return m_ID; }

    public String getPlayerName() { return m_PlayerName.get(); }
    public Integer getScore() { return m_Score.get(); }
    public Instant getDate() { return m_Date.get(); }
    public GameMode getGameMode() { return m_GameMode.get(); }
    public Long getID() { return m_ID.get(); }

    public void setPlayerName(String value) { m_PlayerName.setValue(value); }
    public void setScore(Integer value) { m_Score.setValue(value); }
    public void setDate(Instant value) { m_Date.setValue(value); }
    public void setGameMode(GameMode value) { m_GameMode.setValue(value); }
    public void setID(Long value) { m_ID.setValue(value); }
}
