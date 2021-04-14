package hu.alkfejl.model;

import javafx.beans.property.ObjectProperty;
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
        READY, INPROGRESS, ENDED
    }

    protected static Random s_Random = new Random();

    protected ScheduledService<Void> m_Loop;
    protected ObjectProperty<GameState> m_State;
    protected Snake m_Snake;
    protected Map m_Map;


    protected GameManager()
    {
        m_Loop = createLoop();
        m_State = new SimpleObjectProperty<>();
        m_State.setValue(GameState.READY);
    }
    protected GameManager(Snake snake, Map map)
    {
        this();
        m_Snake = snake;
        m_Map = map;
    }

    public Snake getSnake()
    {
        return m_Snake;
    }
    public Map getMap()
    {
        return m_Map;
    }

    public void setSnake(Snake snake)
    {
        m_Snake = snake;
    }
    public void setMap(Map map)
    {
        m_Map = map;
    }


    protected abstract ScheduledService<Void> createLoop();


    public void startGame()
    {
        if (m_Loop.isRunning())
            m_Loop.cancel();

        placeFood(Food.Random(), List.of(), m_Map);
        m_State.setValue(GameState.INPROGRESS);
        m_Loop.start();
    }


    public void setSpeed(float ticksPerSec)
    {
        m_Loop.setPeriod(new Duration((1.0 / ticksPerSec) * 1000));
    }
    public float getSpeed()
    {
        return 1.0f / (float) m_Loop.getPeriod().toSeconds();
    }

    public ObjectProperty<GameState> stateProperty()
    {
        return m_State;
    }
    public GameState getState()
    {
        return m_State.get();
    }


    protected void placeFood(Food f, Collection<Vector2> excludedPoints, Map map)
    {
        while (true)
        {
            var p = new Vector2(s_Random.nextInt(map.getSize().getX()), s_Random.nextInt(map.getSize().getY()));
            if (!excludedPoints.contains(p))
            {
                map.setFood(new Pair<>(p, f));
                return;
            }
        }
    }
}
