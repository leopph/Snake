package hu.alkfejl.model;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
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
                            /* RECALCULATE HUNGER USAGE AND COOLDOWN VALUES */
                            m_HungerSkill.get().setTicks(m_HungerSkill.get().getTicks() + 1);

                            /* POSITION WITHOUT WALLS TAKEN INTO ACCOUNT */
                            var nextPos = m_Snake.get().nextHeadPosition();

                            /* IF SNAKE HIT A WALL, DIE */
                            if ((nextPos.getX() >= m_Map.get().getSizeX() && m_Map.get().hasRightWall()) ||
                                    (nextPos.getX() < 0 && m_Map.get().hasLeftWall()) ||
                                    (nextPos.getY() >= m_Map.get().getSizeY() && m_Map.get().hasDownWall()) ||
                                    (nextPos.getY() < 0 && m_Map.get().hasUpWall()))
                            {
                                System.out.println("SNAKE HIT A WALL"); // DEBUG
                                m_InternalFinalStage = GameState.WALL_HIT;
                                Platform.runLater(m_Loop::cancel);
                                return null;
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
                                if (m_HungerSkill.get().isInUse())
                                    m_Snake.get().getBodyCoords().remove(m_Snake.get().getBodyCoords().lastIndexOf(m_Snake.get().getBodyCoords().get(0)), m_Snake.get().getBodyCoords().size());
                                else
                                {
                                    System.out.println("SNAKE ATE ITSELF"); // DEBUG
                                    m_InternalFinalStage = GameState.SELF_ATE;
                                    Platform.runLater(m_Loop::cancel);
                                    return null;
                                }
                            }

                            /* IF ITS ALIVE AND ATE FOOD SPAWN NEW ONE */
                            if (willEat)
                                placeFood(Food.Random(), m_Snake.get().getBodyCoords(), m_Map.get());

                            /* SNAKE HAS FILLED THE MAP, GG */
                            if (m_Snake.get().getBodyCoords().size() >= m_Map.get().getSizeX() * m_Map.get().getSizeY())
                            {
                                System.out.println("YOU WON"); // DEBUG
                                m_InternalFinalStage = GameState.P1_WON;
                                Platform.runLater(m_Loop::cancel);
                            }

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
