package hu.alkfejl.model;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;


public class MultiPlayerGameManager extends GameManager
{
    /* REPRESENTS SNAKE SPEED */
    public enum Boost
    {
        SLOW(4), FAST(2), NONE(1);
        private final int m_Value;
        Boost(int value) { m_Value = value; }
        public int getValue() { return m_Value; }
    }


    /* PROPERTIES */
    private final ObjectProperty<Snake> m_Snake2;
    private final StringProperty m_PlayerName2;
    private final IntegerProperty m_Points2;
    private final ObjectProperty<Skill> m_HungerSkill2;
    private final ObjectProperty<Boost> m_Boost1;
    private final ObjectProperty<Boost> m_Boost2;
    private final LongProperty m_Ticks;
    private final BooleanProperty m_Snake1Alive;
    private final BooleanProperty m_Snake2Alive;


    /* CONSTRUCTOR */
    public MultiPlayerGameManager()
    {
        m_Snake2 = new SimpleObjectProperty<>(new Snake());
        m_PlayerName2 = new SimpleStringProperty();
        m_Points2 = new SimpleIntegerProperty(0);
        m_HungerSkill2 = new SimpleObjectProperty<>(new Skill());
        m_Boost1 = new SimpleObjectProperty<>(Boost.NONE);
        m_Boost2 = new SimpleObjectProperty<>(Boost.NONE);
        m_Ticks = new SimpleLongProperty(0L);
        m_Snake1Alive = new SimpleBooleanProperty(true);
        m_Snake2Alive = new SimpleBooleanProperty(true);

        m_HungerSkill2.get().setCooldown(m_HungerSkill.get().getCooldown());
        m_HungerSkill2.get().setDuration(m_HungerSkill.get().getDuration());

        /* TICK RATE IS HALF OF THE ACTUAL GAME SPEED TO HANDLE BOOSTING */
        m_Loop.periodProperty().bind(Bindings.createObjectBinding(() -> new Duration((1.0 / m_TickRate.get()) * 500), m_TickRate));


        /* SET SNAKE BEGINNING POSITIONS */
        m_Snake.get().flip();
        m_Snake.get().rotate90Deg(Snake.Rotation.COUNTERCLOCKWISE);
        m_Snake.get().translate(new Vector2(0, 2));
        m_Snake.get().setCurrentDirection(Snake.Direction.DOWN);
        m_Snake.get().setNextDirection(Snake.Direction.DOWN);

        m_Snake2.get().translate(new Vector2(m_Map.get().getSizeX() - 3, 0));
        m_Snake2.get().rotate90Deg(Snake.Rotation.CLOCKWISE);
        m_Snake2.get().translate(new Vector2(0, 2));
        m_Snake.get().setCurrentDirection(Snake.Direction.DOWN);
        m_Snake.get().setNextDirection(Snake.Direction.DOWN);
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ObjectProperty<Snake> snake2Property() { return m_Snake2; }
    public StringProperty player2NameProperty() { return m_PlayerName2; }
    public IntegerProperty player2PointsProperty() { return m_Points2; }
    public ObjectProperty<Skill> hungerSkill2Property() { return m_HungerSkill2; }
    public ObjectProperty<Boost> snake1BoostProperty() { return m_Boost1; }
    public ObjectProperty<Boost> snake2BoostProperty() { return m_Boost2; }
    public LongProperty ticksProperty() { return m_Ticks; }

    public Snake getSnake2() { return m_Snake2.get(); }
    public String getPlayer2Name() { return m_PlayerName2.get(); }
    public Integer getPlayer2Points() { return m_Points2.get(); }
    public Skill getHungerSkill2() { return m_HungerSkill2.get(); }
    public Boost getSnake1Boost() { return m_Boost1.get(); }
    public Boost getSnake2Boost() { return m_Boost2.get(); }
    public Long getTicks() { return m_Ticks.get(); }

    public void setSnake2(Snake value) { m_Snake2.setValue(value); }
    public void setPlayer2Name(String value) { m_PlayerName2.setValue(value); }
    public void setPlayer2Points(Integer value) { m_Points2.setValue(value); }
    public void setHungerSkill2(Skill value) { m_HungerSkill2.setValue(value); }
    public void setSnake1Boost(Boost value) { m_Boost1.setValue(value); }
    public void setSnake2Boost(Boost value) { m_Boost2.setValue(value); }
    public void setTicks(Long value) { m_Ticks.setValue(value); }


    /* GAME RELATED FUNCTIONS */
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
                            m_Ticks.setValue(m_Ticks.get() + 1);
                            m_HungerSkill.get().setTicks(m_Ticks.getValue());
                            m_HungerSkill2.get().setTicks(m_Ticks.getValue());

                            var foodWasEaten = false;

                            /* IF SNAKE1 SHOULD ACT */
                            if (m_Snake1Alive.get() && Math.floorMod(m_Ticks.get(), m_Boost1.get().getValue()) == 0)
                            {
                                if (hitWall(m_Snake.get()))
                                    m_Snake1Alive.setValue(false);

                                else
                                {
                                    var nextPos = m_Snake.get().nextHeadPosition(m_Map.get().getSizeX(), m_Map.get().getSizeY());
                                    var willEat = m_Map.get().getFood() != null && m_Map.get().getFood().getKey().equals(nextPos);
                                    foodWasEaten = willEat;
                                    m_Snake.get().move(willEat, m_Map.get().getSizeX(), m_Map.get().getSizeY());

                                    if (m_Snake.get().isSelfEating())
                                    {
                                        if (m_HungerSkill.get().isInUse())
                                            m_Snake.get().getBodyCoords().remove(m_Snake.get().getBodyCoords().lastIndexOf(m_Snake.get().getBodyCoords().get(0)), m_Snake.get().getBodyCoords().size());
                                        else
                                            m_Snake1Alive.setValue(false);
                                    }
                                }
                            }

                            /* IF SNAKE 2 SHOULD ACT */
                            if (m_Snake2Alive.get() && Math.floorMod(m_Ticks.get(), m_Boost2.get().getValue()) == 0)
                            {
                                if (hitWall(m_Snake2.get()))
                                    m_Snake2Alive.setValue(false);

                                var nextPos = m_Snake2.get().nextHeadPosition(m_Map.get().getSizeX(), m_Map.get().getSizeY());
                                var willEat = m_Map.get().getFood() != null && m_Map.get().getFood().getKey().equals(nextPos);
                                foodWasEaten = foodWasEaten || willEat;
                                m_Snake2.get().move(willEat, m_Map.get().getSizeX(), m_Map.get().getSizeY());

                                if (m_Snake2.get().isSelfEating())
                                {
                                    if (m_HungerSkill.get().isInUse())
                                        m_Snake2.get().getBodyCoords().remove(m_Snake2.get().getBodyCoords().lastIndexOf(m_Snake2.get().getBodyCoords().get(0)), m_Snake2.get().getBodyCoords().size());
                                    else
                                        m_Snake2Alive.setValue(false);
                                }
                            }

                            if (!m_Snake1Alive.get() && !m_Snake2Alive.get())
                            {
                                Platform.runLater(() -> m_State.setValue(GameState.ALL_DEAD));
                                Platform.runLater(m_Loop::cancel);
                                return null;
                            }

                            if (m_Snake1Alive.get())
                            {

                            }
                        }
                        catch (Exception e) { e.printStackTrace(); }
                        return null;
                    }
                };
            }
        };
    }


    private boolean hitWall(Snake snake)
    {
        /* POSITION WITHOUT WALLS TAKEN INTO ACCOUNT */
        var nextPos = snake.nextHeadPosition();

        /* WALL CHECKING */
        return (nextPos.getX() >= m_Map.get().getSizeX() && m_Map.get().hasRightWall()) ||
                (nextPos.getX() < 0 && m_Map.get().hasLeftWall()) ||
                (nextPos.getY() >= m_Map.get().getSizeY() && m_Map.get().hasDownWall()) ||
                (nextPos.getY() < 0 && m_Map.get().hasUpWall());
    }
}
