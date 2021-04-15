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
                            /* POSITION WITHOUT WALLS TAKEN INTO ACCOUNT */
                            var nextPos = m_Snake.get().nextHeadPosition();

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

                            /* CALCULATE NEW POSITION, NOW TAKING RELOCATION INTO ACCOUNT */
                            nextPos = m_Snake.get().nextHeadPosition(m_Map.get().getSizeX(), m_Map.get().getSizeY());

                            /* CHECK IF SNAKE WILL EAT ON ITS NEXT TILE */
                            boolean willEat = m_Map.get().getFood() != null && m_Map.get().getFood().getKey().equals(nextPos);

                            /* MOVE THE SNAKE WITH A POSSIBLE RELOCATION */
                            m_Snake.get().move(willEat, m_Map.get().getSizeX(), m_Map.get().getSizeY());

                            /* IF SNAKE IS NOW EATING ITSELF, DIE */
                            if (m_Snake.get().isSelfEating())
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
