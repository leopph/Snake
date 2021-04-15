package hu.alkfejl.model;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;


public class SinglePlayerGameManager extends GameManager
{
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
                            var nextPos = m_Snake.get().nextHeadPosition();
                            boolean willEat;
                            if (nextPos.getX() >= m_Map.get().getSizeX() || nextPos.getX() < 0 || nextPos.getY() >= m_Map.get().getSizeY() || nextPos.getY() < 0)
                            {
                                /* IF SNAKE HIT A WALL, DIE */
                                if ((nextPos.getX() >= m_Map.get().getSizeX() && m_Map.get().hasRightWall()) ||
                                        (nextPos.getX() < 0 && m_Map.get().hasLeftWall()) ||
                                        (nextPos.getY() >= m_Map.get().getSizeY() && m_Map.get().hasDownWall()) ||
                                        (nextPos.getY() < 0 && m_Map.get().hasUpWall()))
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
                                    int modX = m_Map.get().getSizeX();
                                    int modY = m_Map.get().getSizeY();
                                    /*int mod;

                                    if (nextPos.getX() >= m_Map.get().getSizeX() || nextPos.getX() < 0)
                                        mod = m_Map.get().getSizeX();
                                    else
                                        mod = m_Map.get().getSizeY();*/

                                    System.out.println("SNAKE SHOULD RELOCATE, NEW POSITION IS " + m_Snake.get().nextHeadPosition(modX, modY) + "!"); // DEBUG
                                    willEat = m_Map.get().getFood() != null && m_Map.get().getFood().getKey().equals(m_Snake.get().nextHeadPosition(modX, modY));
                                    m_Snake.get().move(willEat, modX, modY);
                                }
                            }
                            /* NORMAL GROWTH AND MOVEMENT */
                            else
                            {
                                willEat = m_Map.get().getFood() != null && m_Map.get().getFood().getKey().equals(nextPos);
                                m_Snake.get().move(willEat);
                            }

                            /* CHECK IF SNAKE IS EATING ITSELF AT THE NEW LOCATION */
                            if (m_Snake.get().isSelfEating()) // TODO CHECKS +1 COORDINATES NEEDS FIX
                            {
                                System.out.println("SNAKE ATE ITSELF"); // DEBUG
                                m_State.setValue(GameState.ENDED);
                                Platform.runLater(m_Loop::cancel);
                                return null;
                                // TODO

                            }

                            /* IF ITS ALIVE AND ATE FOOD SPAWN NEW ONE */
                            if (willEat)
                                placeFood(Food.Random(), m_Snake.get().getBodyCoords(), m_Map.get());

                            /* SNAKE HAS FILLED THE MAP, GG */
                            if (m_Snake.get().getBodyCoords().size() >= m_Map.get().getSizeX() * m_Map.get().getSizeY())
                            {
                                System.out.println("YOU WON"); // DEBUG
                                m_State.setValue(GameState.ENDED);
                                Platform.runLater(m_Loop::cancel);
                                // TODO
                            }

                            System.out.println("Snake is now at: " + m_Snake.get().getBodyCoords().get(0) + "."); // DEBUG

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
