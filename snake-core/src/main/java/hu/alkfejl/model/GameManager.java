package hu.alkfejl.model;

import javafx.concurrent.ScheduledService;
import javafx.util.Duration;


public abstract class GameManager
{
    private ScheduledService<Void> m_Loop;


    GameManager()
    {
        m_Loop = createLoop();
    }


    protected abstract ScheduledService<Void> createLoop();

    public void startGame()
    {
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
