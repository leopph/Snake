package hu.alkfejl.model;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;


public class SinglePlayerGameManager extends GameManager
{
    private Snake m_Snake;
    private Map m_Map;
    private boolean m_WallsMatter;


    public SinglePlayerGameManager()
    {
    }

    public SinglePlayerGameManager(Snake snake, Map map, boolean dieIfWallIsHit)
    {
        m_Snake = snake;
        m_Map = map;
        m_WallsMatter = dieIfWallIsHit;
    }


    public Snake getSnake()
    {
        return m_Snake;
    }

    public Map getMap()
    {
        return m_Map;
    }

    public boolean doWallsMatter()
    {
        return m_WallsMatter;
    }

    public void setSnake(Snake snake)
    {
        m_Snake = snake;
    }

    public void setMap(Map map)
    {
        m_Map = map;
    }

    public void setWallsMatter(boolean newValue)
    {
        m_WallsMatter = newValue;
    }


    @Override
    protected ScheduledService<Void> createLoop()
    {
        return new ScheduledService<>()
        {
            @Override
            protected Task<Void> createTask()
            {
                return new Task<>()
                {
                    @Override
                    protected Void call()
                    {
                        var nextPos = m_Snake.nextHeadLocation();

                        if (nextPos.getX() >= m_Map.getSize().getX() || nextPos.getX() < 0 || nextPos.getY() >= m_Map.getSize().getY() || nextPos.getY() < 0)
                            if (m_WallsMatter)
                            {
                                m_State.setValue(GameState.ENDED);
                                Platform.runLater(m_Loop::cancel);
                                // TODO
                                return null;
                            }
                            else
                            {
                                if (nextPos.getX() >= m_Map.getSize().getX())
                                    m_Snake.relocate(Snake.Direction.LEFT, -1);
                                else if (nextPos.getX() < 0)
                                    m_Snake.relocate(Snake.Direction.LEFT, m_Map.getSize().getX());
                                else if (nextPos.getY() >= m_Map.getSize().getY())
                                    m_Snake.relocate(Snake.Direction.UP, -1);
                                else if (nextPos.getY() < 0)
                                    m_Snake.relocate(Snake.Direction.UP, m_Map.getSize().getY());
                            }

                        if (!m_Snake.move(m_Map.getFoodOnPoint(nextPos) != null))
                        {
                            System.out.println("Megette magát xd");
                            m_State.setValue(GameState.ENDED);
                            Platform.runLater(m_Loop::cancel);
                            // TODO
                        }

                        System.out.println(nextPos);
                        return null;
                    }
                };
            }
        };
    }
}
