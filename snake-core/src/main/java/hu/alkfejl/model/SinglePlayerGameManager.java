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
                            /* INITIAL ASSUMED NEW VALUES */
                            var nextPos = m_Snake.nextHeadPosition();
                            boolean willEat;
                            if (nextPos.getX() >= m_Map.getSize().getX() || nextPos.getX() < 0 || nextPos.getY() >= m_Map.getSize().getY() || nextPos.getY() < 0)
                            {
                                /* IF SNAKE HIT A WALL, DIE */
                                if ((nextPos.getX() >= m_Map.getSize().getX() && (Map.Wall.RIGHT.get() & m_Map.getWalls()) == Map.Wall.RIGHT.get()) ||
                                        (nextPos.getX() < 0 && (Map.Wall.LEFT.get() & m_Map.getWalls()) == Map.Wall.LEFT.get()) ||
                                        (nextPos.getY() >= m_Map.getSize().getY() && (Map.Wall.DOWN.get() & m_Map.getWalls()) == Map.Wall.DOWN.get()) ||
                                        (nextPos.getY() < 0 && (Map.Wall.UP.get() & m_Map.getWalls()) == Map.Wall.UP.get()))
                                {
                                    System.out.println("SNAKE HIT A WALL"); // DEBUG
                                    m_State.setValue(GameState.ENDED);
                                    Platform.runLater(m_Loop::cancel);
                                    return null;
                                    // TODO
                                }
                                /* IF SNAKE NEEDS TO RELOCATE, CALCULATE GROWTH AND NEW POSITIONS ACCORDINGLY */
                                else
                                {
                                    int mod;

                                    if (nextPos.getX() > m_Map.getSize().getX() || nextPos.getX() < 0)
                                        mod = m_Map.getSize().getX();
                                    else
                                        mod = m_Map.getSize().getY();

                                    System.out.println("SNAKE SHOULD RELOCATE, NEW POSITION IS " + m_Snake.nextHeadPosition(mod) + "!"); // DEBUG
                                    willEat = m_Map.getFood() != null && m_Map.getFood().getKey().equals(m_Snake.nextHeadPosition(mod));
                                    m_Snake.move(willEat, mod);
                                }
                            }
                            /* NORMAL GROWTH AND MOVEMENT */
                            else
                            {
                                willEat = m_Map.getFood() != null && m_Map.getFood().getKey().equals(nextPos);
                                m_Snake.move(willEat);
                            }

                            /* CHECK IF SNAKE IS EATING ITSELF AT THE NEW LOCATION */
                            if (m_Snake.isSelfEating())
                            {
                                System.out.println("SNAKE ATE ITSELF"); // DEBUG
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
                                System.out.println("YOU WON"); // DEBUG
                                m_State.setValue(GameState.ENDED);
                                Platform.runLater(m_Loop::cancel);
                                // TODO
                            }

                            System.out.println("Snake is now at: " + m_Snake.getBodyCoords().get(0) + "."); // DEBUG

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
