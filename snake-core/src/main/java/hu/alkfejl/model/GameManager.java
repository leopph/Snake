package hu.alkfejl.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.ScheduledService;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public abstract class GameManager implements Serializable
{
    public enum GameState implements Serializable
    {
        READY, IN_PROGRESS, SELF_ATE, WALL_HIT, P1_WON, P2_WON, CANCELLED, ALL_DEAD
    }


    /* PROPERTIES */
    public static final int DEFAULT_TICK_RATE = 1;
    protected static Random s_Random = new Random();

    protected ScheduledService<Void> m_Loop;
    protected ObjectProperty<GameState> m_State;
    protected DoubleProperty m_TickRate;
    protected ObjectProperty<Snake> m_Snake;
    protected ObjectProperty<Map> m_Map;
    protected ObjectProperty<Skill> m_HungerSkill;
    protected StringProperty m_PlayerName;
    protected IntegerProperty m_Points;


    /* CONSTRUCTORS */
    protected GameManager()
    {
        m_Loop = createLoop();

        m_State = new SimpleObjectProperty<>();
        m_State.setValue(GameState.READY);

        m_TickRate = new SimpleDoubleProperty(DEFAULT_TICK_RATE);

        m_Snake = new SimpleObjectProperty<>(new Snake());
        m_Map = new SimpleObjectProperty<>(new Map());
        m_PlayerName = new SimpleStringProperty();
        m_Points = new SimpleIntegerProperty(0);

        m_HungerSkill = new SimpleObjectProperty<>(new Skill());
        m_HungerSkill.get().setCooldown(java.time.Duration.ofSeconds(10));
        m_HungerSkill.get().setDuration(java.time.Duration.ofSeconds(3));

        m_Loop.periodProperty().bind(Bindings.createObjectBinding(() -> new Duration((1.0 / m_TickRate.get()) * 1000), m_TickRate));
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ObjectProperty<GameState> gameStateProperty() { return m_State; }
    public DoubleProperty tickRateProperty() { return m_TickRate; }
    public ObjectProperty<Snake> snakeProperty() { return m_Snake; }
    public ObjectProperty<Map> mapProperty() { return m_Map; }
    public ObjectProperty<Skill> hungerSkillProperty() { return m_HungerSkill; }
    public StringProperty playerNameProperty() { return m_PlayerName; }
    public IntegerProperty pointsProperty() { return m_Points; }

    public GameState getGameState() { return m_State.get(); }
    public Double getTickRate() { return m_TickRate.get(); }
    public Snake getSnake() { return m_Snake.get(); }
    public Map getMap() { return m_Map.get(); }
    public Skill getHungerSkill() { return m_HungerSkill.get(); }
    public String getPlayerName() { return m_PlayerName.get(); }
    public Integer getPoints() { return m_Points.get(); }

    public void setGameState(GameState newValue) { m_State.setValue(newValue); }
    public void setTickRate(Double newValue) { m_TickRate.setValue(newValue); }
    public void setSnake(Snake snake) { m_Snake.setValue(snake); }
    public void setMap(Map map) { m_Map.setValue(map); }
    public void setHungerSkill(Skill newValue) { m_HungerSkill.set(newValue); }
    public void setPlayerName(String newValue) { m_PlayerName.set(newValue); }
    public void setPoints(Integer newValue) { m_Points.setValue(newValue); }


    public void startGame()
    {
        if (m_Loop.isRunning())
            m_Loop.cancel();

        m_Loop.reset();

        m_Snake.get().reset();

        m_Points.setValue(0);
        m_HungerSkill.get().setLastUsed(Instant.MIN);

        m_Map.get().setFood(null);

        placeFood(Food.Random(), List.of(), m_Map.get());
        m_State.setValue(GameState.IN_PROGRESS);
        m_Loop.start();
    }


    public void stopGame()
    {
        m_State.setValue(GameState.CANCELLED);
        m_Loop.cancel();
    }


    protected void placeFood(Food f, Collection<Vector2> excludedPoints, Map map)
    {
        while (true)
        {
            var p = new Vector2(s_Random.nextInt(map.getSizeX()), s_Random.nextInt(map.getSizeY()));
            if (!excludedPoints.contains(p))
            {
                map.setFood(new Pair<>(p, f));
                return;
            }
        }
    }


    /* SUBCLASSES IMPLEMENT THIS TO DEFINE THE GAME LOOP */
    protected abstract ScheduledService<Void> createLoop();
}
