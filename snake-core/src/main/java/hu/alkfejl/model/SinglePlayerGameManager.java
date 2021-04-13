package hu.alkfejl.model;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;


public class SinglePlayerGameManager extends GameManager
{
    public SinglePlayerGameManager()
    {
    }

    public SinglePlayerGameManager(Snake snake, Map map)
    {
        super(snake, map);
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
                            if ((nextPos.getX() >= m_Map.getSize().getX() && (Map.Wall.RIGHT.get() & m_Map.getWalls()) == Map.Wall.RIGHT.get()) ||
                                    (nextPos.getX() < 0 && (Map.Wall.LEFT.get() & m_Map.getWalls()) == Map.Wall.LEFT.get()) ||
                                    (nextPos.getY() >= m_Map.getSize().getY() && (Map.Wall.UP.get() & m_Map.getWalls()) == Map.Wall.UP.get()) ||
                                    (nextPos.getY() < 0 && (Map.Wall.DOWN.get() & m_Map.getWalls()) == Map.Wall.DOWN.get()))
                            {
                                m_State.setValue(GameState.ENDED);
                                Platform.runLater(m_Loop::cancel);
                                return null;
                                // TODO
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

                            var willEatFood = m_Map.getFoodOnPoint(m_Snake.nextHeadLocation()) != null;

                        if (!m_Snake.move(willEatFood))
                        {
                            System.out.println("Megette magát xd");
                            m_State.setValue(GameState.ENDED);
                            Platform.runLater(m_Loop::cancel);
                            // TODO
                        }

                        if (willEatFood)
                            placeFood(Food.Random(), m_Snake.getBodyCoords(), m_Map);

                        if (m_Snake.getBodyCoords().size() >= m_Map.getSize().getX() * m_Map.getSize().getY())
                        {
                            System.out.println("Nyertél kek");
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
