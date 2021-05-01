package hu.alkfejl.model;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MultiPlayerGameManager extends GameManager
{
    /* REPRESENTS SNAKE SPEED */
    public enum Boost
    {
        SLOW(4), FAST(1), NONE(2);
        private final int m_Value;
        Boost(int value) { m_Value = value; }
        public int getValue() { return m_Value; }
    }


    /* PROPERTIES */
    private final ObjectProperty<Snake> m_Snake2;
    private final StringProperty m_PlayerName2;
    private final IntegerProperty m_Points2;
    private final ObjectProperty<Skill> m_HungerSkill2;
    private final ObjectProperty<Skill> m_CannibalSkill1;
    private final ObjectProperty<Skill> m_CannibalSkill2;
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
        m_CannibalSkill1 = new SimpleObjectProperty<>(new Skill());
        m_CannibalSkill2 = new SimpleObjectProperty<>(new Skill());
        m_Boost1 = new SimpleObjectProperty<>(Boost.NONE);
        m_Boost2 = new SimpleObjectProperty<>(Boost.NONE);
        m_Ticks = new SimpleLongProperty(0L);
        m_Snake1Alive = new SimpleBooleanProperty(true);
        m_Snake2Alive = new SimpleBooleanProperty(true);

        m_HungerSkill2.get().setCooldown(m_HungerSkill.get().getCooldown());
        m_HungerSkill2.get().setDuration(m_HungerSkill.get().getDuration());

        m_CannibalSkill1.get().setCooldown(java.time.Duration.ofSeconds(120));
        m_CannibalSkill1.get().setDuration(java.time.Duration.ofSeconds(2));

        m_CannibalSkill2.get().setCooldown(m_CannibalSkill1.get().getCooldown());
        m_CannibalSkill2.get().setDuration(m_CannibalSkill1.get().getDuration());

        /* TICK RATE IS HALF OF THE ACTUAL GAME SPEED TO HANDLE BOOSTING */
        m_Loop.periodProperty().unbind();
        m_Loop.periodProperty().bind(Bindings.createObjectBinding(() -> new Duration((1.0 / m_TickRate.get()) * 500), m_TickRate));

        m_HungerSkill.get().ticksProperty().bind(m_Ticks);
        m_HungerSkill2.get().ticksProperty().bind(m_Ticks);
        m_CannibalSkill1.get().ticksProperty().bind(m_Ticks);
        m_CannibalSkill2.get().ticksProperty().bind(m_Ticks);
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ObjectProperty<Snake> snake2Property() { return m_Snake2; }
    public StringProperty player2NameProperty() { return m_PlayerName2; }
    public IntegerProperty player2PointsProperty() { return m_Points2; }
    public ObjectProperty<Skill> hungerSkill2Property() { return m_HungerSkill2; }
    public ObjectProperty<Boost> snake1BoostProperty() { return m_Boost1; }
    public ObjectProperty<Boost> snake2BoostProperty() { return m_Boost2; }
    public LongProperty ticksProperty() { return m_Ticks; }
    public BooleanProperty snake1AliveProperty() { return m_Snake1Alive; }
    public BooleanProperty snake2AliveProperty() { return m_Snake2Alive; }
    public ObjectProperty<Skill> cannibalSkill1Property() { return m_CannibalSkill1; }
    public ObjectProperty<Skill> cannibalSkill2Property() { return m_CannibalSkill2; }

    public Snake getSnake2() { return m_Snake2.get(); }
    public String getPlayer2Name() { return m_PlayerName2.get(); }
    public Integer getPlayer2Points() { return m_Points2.get(); }
    public Skill getHungerSkill2() { return m_HungerSkill2.get(); }
    public Boost getSnake1Boost() { return m_Boost1.get(); }
    public Boost getSnake2Boost() { return m_Boost2.get(); }
    public Long getTicks() { return m_Ticks.get(); }
    public Boolean isSnake1Alive() { return m_Snake1Alive.get(); }
    public Boolean isSnake2Alive() { return m_Snake2Alive.get(); }
    public Skill getCannibalSkill1() { return m_CannibalSkill1.get(); }
    public Skill getCannibalSkill2() { return m_CannibalSkill2.get(); }

    public void setSnake2(Snake value) { m_Snake2.setValue(value); }
    public void setPlayer2Name(String value) { m_PlayerName2.setValue(value); }
    public void setPlayer2Points(Integer value) { m_Points2.setValue(value); }
    public void setHungerSkill2(Skill value) { m_HungerSkill2.setValue(value); }
    public void setSnake1Boost(Boost value) { m_Boost1.setValue(value); }
    public void setSnake2Boost(Boost value) { m_Boost2.setValue(value); }
    public void setTicks(Long value) { m_Ticks.setValue(value); }
    public void setSnake1Alive(Boolean value) { m_Snake1Alive.set(value); }
    public void setSnake2Alive(Boolean value) { m_Snake2Alive.set(value); }
    public void setCannibalSkill1(Skill value) { m_CannibalSkill1.setValue(value); }
    public void setCannibalSkill2(Skill value) { m_CannibalSkill2.setValue(value); }


    @Override
    public void startGame()
    {
        if (m_Loop.isRunning())
            m_Loop.cancel();

        m_Loop.reset();

        m_Ticks.set(0L);

        m_Snake.get().reset();
        m_Snake2.get().reset();

        m_Snake1Alive.setValue(true);
        m_Snake2Alive.setValue(true);

        m_Points.setValue(0);
        m_Points2.setValue(0);

        m_HungerSkill.get().setLastUsed(Instant.MIN);
        m_HungerSkill2.get().setLastUsed(Instant.MIN);

        m_CannibalSkill1.get().setLastUsed(Instant.MIN);
        m_CannibalSkill2.get().setLastUsed(Instant.MIN);

        m_Map.get().setFood(null);
        placeFood(Food.Random(), List.of(), m_Map.get());
        m_State.setValue(GameState.IN_PROGRESS);


        /* SET SNAKE BEGINNING POSITIONS */
        m_Snake.get().rotate90Deg(Snake.Rotation.CLOCKWISE);
        m_Snake.get().translate(new Vector2(-2, 2));
        m_Snake.get().setCurrentDirection(Snake.Direction.DOWN);
        m_Snake.get().setNextDirection(Snake.Direction.DOWN);

        m_Snake2.get().rotate90Deg(Snake.Rotation.CLOCKWISE);
        m_Snake2.get().translate(new Vector2(m_Map.get().getSizeX()-3, 2));
        m_Snake2.get().setCurrentDirection(Snake.Direction.DOWN);
        m_Snake2.get().setNextDirection(Snake.Direction.DOWN);

        m_Loop.start();
    }


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

                            var foodWasEaten = false;

                            /* IF SNAKE1 SHOULD ACT */
                            if (m_Snake1Alive.get() && Math.floorMod(m_Ticks.get(), m_Boost1.get().getValue()) == 0)
                            {
                                if (hitWall(m_Snake.get()))
                                    m_Snake1Alive.setValue(false);

                                /* MOVE OR GROW SNAKE */
                                else
                                {
                                    var nextPos = m_Snake.get().nextHeadPosition(m_Map.get().getSizeX(), m_Map.get().getSizeY());
                                    var willEat = m_Map.get().getFood() != null && m_Map.get().getFood().getKey().equals(nextPos);
                                    foodWasEaten = willEat;
                                    m_Snake.get().move(willEat, m_Map.get().getSizeX(), m_Map.get().getSizeY());

                                    if (willEat)
                                    {
                                        Platform.runLater(() -> m_Points.setValue(m_Points.get() + m_Map.get().getFood().getValue().getPoint()));

                                        /* IF SNAKE ATE MAGIC FOOD, RESET CANNIBAL COOLDOWN */
                                        if (m_Map.get().getFood().getValue().getName().equals(Food.getMagicFoodName()))
                                            m_CannibalSkill1.get().setLastUsed(Instant.MIN);
                                    }

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

                                /* MOVE OR GROW SNAKE */
                                else
                                {
                                    var nextPos = m_Snake2.get().nextHeadPosition(m_Map.get().getSizeX(), m_Map.get().getSizeY());
                                    var willEat = m_Map.get().getFood() != null && m_Map.get().getFood().getKey().equals(nextPos);
                                    foodWasEaten = foodWasEaten || willEat;
                                    m_Snake2.get().move(willEat, m_Map.get().getSizeX(), m_Map.get().getSizeY());

                                    if (willEat)
                                    {
                                        Platform.runLater(() -> m_Points2.setValue(m_Points2.get() + m_Map.get().getFood().getValue().getPoint()));

                                        /* IF SNAKE ATE MAGIC FOOD, RESET CANNIBAL COOLDOWN */
                                        if (m_Map.get().getFood().getValue().getName().equals(Food.getMagicFoodName()))
                                            m_CannibalSkill2.get().setLastUsed(Instant.MIN);
                                    }

                                    if (m_Snake2.get().isSelfEating())
                                    {
                                        if (m_HungerSkill.get().isInUse())
                                            m_Snake2.get().getBodyCoords().remove(m_Snake2.get().getBodyCoords().lastIndexOf(m_Snake2.get().getBodyCoords().get(0)), m_Snake2.get().getBodyCoords().size());
                                        else
                                            m_Snake2Alive.setValue(false);
                                    }
                                }
                            }

                            /* IF NEW FOOD HAS TO BE SPAWNED, SPAWN IT */
                            if (foodWasEaten)
                                placeFood(Food.Random(), Stream.concat(m_Snake.get().getBodyCoords().stream(), m_Snake2.get().getBodyCoords().stream()).collect(Collectors.toList()), m_Map.get());


                            /* IF BOTH ALIVE THEY MIGHT BE EATING EACH OTHER */
                            if (m_Snake1Alive.get() && m_Snake2Alive.get())
                            {
                                if (m_Snake2.get().getBodyCoords().contains(m_Snake.get().getBodyCoords().get(0)))
                                    m_Snake1Alive.set(false);
                                if (m_Snake.get().getBodyCoords().contains(m_Snake2.get().getBodyCoords().get(0)))
                                    m_Snake2Alive.set(false);
                            }

                            // IF BOTH DEAD GAME IS OVER */
                            if (!m_Snake1Alive.get() && !m_Snake2Alive.get())
                            {
                                Platform.runLater(() -> m_State.setValue(GameState.ALL_DEAD));
                                Platform.runLater(m_Loop::cancel);
                                return null;
                            }

                            /* IF ONLY SNAKE 1 IS ALIVE IT MIGHT HAVE WON */
                            if (m_Snake1Alive.get() && !m_Snake2Alive.get())
                            {
                                /* IF SNAKE 2 WAS CANNIBAL, IT DIDNT ACTUALLY DIE */
                                if (m_CannibalSkill2.get().isInUse())
                                {
                                    m_Snake2Alive.setValue(true);
                                    m_Snake.get().getBodyCoords().remove(m_Snake.get().getBodyCoords().lastIndexOf(m_Snake2.get().getBodyCoords().get(0)), m_Snake.get().getBodyCoords().size());
                                }
                                else
                                {
                                    Platform.runLater(() -> m_State.setValue(GameState.P1_WON));
                                    Platform.runLater(m_Loop::cancel);
                                    return null;
                                }
                            }

                            /* IF ONLY SNAKE 2 IS ALIVE IT MIGHT HAVE WON */
                            if (!m_Snake1Alive.get() && m_Snake2Alive.get())
                            {
                                /* IF SNAKE 1 WAS CANNIBAL, IT DIDNT ACTUALLY DIE */
                                if (m_CannibalSkill1.get().isInUse())
                                {
                                    m_Snake1Alive.setValue(true);
                                    m_Snake2.get().getBodyCoords().remove(m_Snake2.get().getBodyCoords().lastIndexOf(m_Snake.get().getBodyCoords().get(0)), m_Snake2.get().getBodyCoords().size());
                                }
                                else
                                {
                                    Platform.runLater(() -> m_State.setValue(GameState.P2_WON));
                                    Platform.runLater(m_Loop::cancel);
                                    return null;
                                }
                            }
                        }
                        catch (Exception e) { e.printStackTrace(); }
                        return null;
                    }
                };
            }
        };
    }


    /* HELPER TO CALCULATE WALL COLLISION */
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
