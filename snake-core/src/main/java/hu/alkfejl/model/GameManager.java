package hu.alkfejl.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.ScheduledService;
import javafx.util.Duration;


public abstract class GameManager
{
    public enum GameState
    {
        READY, INPROGRESS, ENDED
    }


    protected ScheduledService<Void> m_Loop;
    protected ObjectProperty<GameState> m_State;


    protected GameManager()
    {
        m_Loop = createLoop();
        m_State = new SimpleObjectProperty<>();
        m_State.setValue(GameState.READY);
    }

    protected abstract ScheduledService<Void> createLoop();

    public void startGame()
    {
        if (m_Loop.isRunning())
            m_Loop.cancel();

        m_State.setValue(GameState.INPROGRESS);
        m_Loop.start();
    }

    public void setSpeed(int ticksPerSec)
    {
        m_Loop.setPeriod(new Duration((1.0 / ticksPerSec) * 1000));
    }
    public int getSpeed()
    {
        return (int) (1.0 / m_Loop.getPeriod().toSeconds());
    }

    public ObjectProperty<GameState> getStateProperty()
    {
        return m_State;
    }
    public GameState getState()
    {
        return m_State.get();
    }
}
