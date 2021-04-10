package hu.alkfejl.model;

import javafx.concurrent.ScheduledService;
import javafx.util.Duration;


public abstract class GameManager
{
    public enum GameState
    {
        READY, INPROGRESS, WON, LOST
    }

    private ScheduledService<Void> m_Loop;
    private GameState m_State;


    GameManager()
    {
        m_Loop = createLoop();
        m_State = GameState.READY;
    }


    protected abstract ScheduledService<Void> createLoop();

    public void startGame()
    {
        m_State = GameState.INPROGRESS;
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
}
