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
                        try
                        {
                            var nextPos = m_Snake.nextHeadPosition();
                            boolean willEat;
                            if (nextPos.getX() >= m_Map.getSize().getX() || nextPos.getX() < 0 || nextPos.getY() >= m_Map.getSize().getY() || nextPos.getY() < 0)
                            {
                                /* IF ITS HIT A WALL, DIE */
                                if ((nextPos.getX() >= m_Map.getSize().getX() && (Map.Wall.RIGHT.get() & m_Map.getWalls()) == Map.Wall.RIGHT.get()) ||
                                        (nextPos.getX() < 0 && (Map.Wall.LEFT.get() & m_Map.getWalls()) == Map.Wall.LEFT.get()) ||
                                        (nextPos.getY() >= m_Map.getSize().getY() && (Map.Wall.UP.get() & m_Map.getWalls()) == Map.Wall.UP.get()) ||
                                        (nextPos.getY() < 0 && (Map.Wall.DOWN.get() & m_Map.getWalls()) == Map.Wall.DOWN.get()))
                                {
                                    System.out.println("HIT WALL"); // DEBUG
                                    m_State.setValue(GameState.ENDED);
                                    Platform.runLater(m_Loop::cancel);
                                    return null;
                                    // TODO
                                }
                                else
                                {
                                    if (nextPos.getX() > m_Map.getSize().getX() || nextPos.getX() < 0)
                                    {
                                        willEat = m_Map.getFoodOnPoint(m_Snake.nextHeadPosition(m_Map.getSize().getX())) != null;
                                        System.out.println("SNAKE SHOULD RELOCATE, NEW POSITION IS " + m_Snake.nextHeadPosition(m_Map.getSize().getX()) + "!");
                                        m_Snake.move(willEat, m_Map.getSize().getX());
                                    }
                                    else
                                    {
                                        willEat = m_Map.getFoodOnPoint(m_Snake.nextHeadPosition(m_Map.getSize().getY())) != null;
                                        System.out.println("SNAKE SHOULD RELOCATE, NEW POSITION IS " + m_Snake.nextHeadPosition(m_Map.getSize().getY()) + "!");
                                        m_Snake.move(willEat, m_Map.getSize().getY());
                                    }
                                }
                            }
                            else
                            {
                                willEat = m_Map.getFoodOnPoint(nextPos) != null;
                                m_Snake.move(willEat);
                            }

                            /* CHECK IF SNAKE IS EATING ITSELF AT THE NEW LOCATION */
                            if (m_Snake.isSelfEating())
                            {
                                System.out.println("Megette magát xd");
                                m_State.setValue(GameState.ENDED);
                                Platform.runLater(m_Loop::cancel);
                                return null;
                                // TODO

                            }

                            /* IF ITS ALIVE AND ATE FOOD SPAWN NEW ONE */
                            if (willEat)
                                placeFood(Food.Random(), m_Snake.getBodyCoords(), m_Map);

                            /* SNAKE HAS FILLED THE MAP, GG */
                            if (m_Snake.getBodyCoords().size() >= m_Map.getSize().getX() * m_Map.getSize().getY())
                            {
                                System.out.println("Nyertél kek");
                                m_State.setValue(GameState.ENDED);
                                Platform.runLater(m_Loop::cancel);
                                // TODO
                            }

                            System.out.println("Snake is now at: " + m_Snake.getBodyCoords().get(0) + ".");

                            return null;
                        }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                    }
                };
            }
        };
    }
}
