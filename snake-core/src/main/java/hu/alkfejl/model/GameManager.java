package hu.alkfejl.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.ScheduledService;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Random;


public abstract class GameManager
{
    public enum GameState
    {
        READY, IN_PROGRESS, SELF_ATE, WALL_HIT, P1_WON, P2_WON
    }


    /* PROPERTIES */
    public static final int DEFAULT_TICK_RATE = 1;
    protected static Random s_Random = new Random();

    protected GameState m_InternalFinalStage;

    protected ScheduledService<Void> m_Loop;
    protected ObjectProperty<GameState> m_State;
    protected DoubleProperty m_TickRate;
    protected ObjectProperty<Snake> m_Snake;
    protected ObjectProperty<Map> m_Map;


    /* CONSTRUCTORS */
    protected GameManager()
    {
        m_Loop = createLoop();

        m_InternalFinalStage = null;

        m_State = new SimpleObjectProperty<>();
        m_State.setValue(GameState.READY);

        m_TickRate = new SimpleDoubleProperty(DEFAULT_TICK_RATE);

        m_Snake = new SimpleObjectProperty<>(new Snake());
        m_Map = new SimpleObjectProperty<>(new Map());

        m_Loop.periodProperty().bind(Bindings.createObjectBinding(() -> new Duration((1.0 / m_TickRate.get()) * 1000), m_TickRate));

        m_Loop.setOnCancelled(event -> m_State.setValue(m_InternalFinalStage));
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ObjectProperty<GameState> gameStateProperty() { return m_State; }
    public DoubleProperty tickRateProperty() { return m_TickRate; }
    public ObjectProperty<Snake> snakeProperty() { return m_Snake; }
    public ObjectProperty<Map> mapProperty() { return m_Map; }

    public GameState getGameState() { return m_State.get(); }
    public Double getTickRate() { return m_TickRate.get(); }
    public Snake getSnake()
    {
        return m_Snake.get();
    }
    public Map getMap()
    {
        return m_Map.get();
    }

    public void setGameState(GameState newValue) { m_State.setValue(newValue); }
    public void setTickRate(Double newValue) { m_TickRate.setValue(newValue); }
    public void setSnake(Snake snake)
    {
        m_Snake.setValue(snake);
    }
    public void setMap(Map map)
    {
        m_Map.setValue(map);
    }


    protected abstract ScheduledService<Void> createLoop();


    public void startGame()
    {
        if (m_Loop.isRunning())
            m_Loop.cancel();

        m_Loop.reset();
        m_Snake.set(new Snake()); // DEBUG

        placeFood(Food.Random(), List.of(), m_Map.get());
        m_State.setValue(GameState.IN_PROGRESS);
        m_Loop.start();
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
}
