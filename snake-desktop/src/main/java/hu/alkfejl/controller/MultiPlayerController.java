package hu.alkfejl.controller;

import hu.alkfejl.model.MultiPlayerGameManager;
import hu.alkfejl.model.Skill;
import hu.alkfejl.model.Snake;
import hu.alkfejl.model.Vector2;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.Duration;
import java.time.Instant;


public class MultiPlayerController extends GameWindowController
{
    @FXML private Label m_PlayerNameLabel1;
    @FXML private Label m_PlayerNameLabel2;
    @FXML private Label m_ScoreLabel2;
    @FXML private Label m_SkillLabel2;


    @Override
    public void start()
    {
        // SET INPUT HANDLER
        m_Grid.getScene().setOnKeyPressed(this::keyCallback);
        m_Grid.getScene().setOnKeyReleased(this::keyCallback);


        // CONFIGURE PLAYER NAME DISPLAY
        m_PlayerNameLabel1.textProperty().bind(Bindings.createStringBinding(() ->
        {
            var manager = (MultiPlayerGameManager) m_GameManager.get();
            if (manager.getPlayerName() != null && !manager.getPlayerName().isEmpty())
                return manager.getPlayerName();
            return "Player 1";
        }, m_GameManager.get().playerNameProperty()));

        m_PlayerNameLabel2.textProperty().bind(Bindings.createStringBinding(() ->
        {
            var manager = (MultiPlayerGameManager) m_GameManager.get();
            if (manager.getPlayer2Name() != null && !manager.getPlayer2Name().isEmpty())
                return manager.getPlayer2Name();
            return "Player 1";
        }, ((MultiPlayerGameManager) m_GameManager.get()).player2NameProperty()));


        // CONFIGURE SCORE DISPLAY
        m_ScoreLabel.textProperty().bind(Bindings.createStringBinding(() -> "Score: " + m_GameManager.get().getPoints(), m_GameManager.get().pointsProperty()));
        m_ScoreLabel2.textProperty().bind(Bindings.createStringBinding(() -> "Score: " + ((MultiPlayerGameManager) m_GameManager.get()).getPlayer2Points(), ((MultiPlayerGameManager) m_GameManager.get()).player2PointsProperty()));


        // CONFIGURE HUNGER SKILL DISPLAYS
        m_GameManager.get().hungerSkillProperty().get().ticksProperty().addListener((event, oldValue, newValue) ->
                Platform.runLater(() -> setSkillLabel(m_GameManager.get().getHungerSkill(), m_SkillLabel, "Hunger")));
        ((MultiPlayerGameManager) m_GameManager.get()).hungerSkill2Property().get().ticksProperty().addListener((event, oldValue, newValue) ->
                Platform.runLater(() -> setSkillLabel(((MultiPlayerGameManager) m_GameManager.get()).getHungerSkill2(), m_SkillLabel2, "Hunger")));


        // CONFIGURE FOOD PICKUP DISPLAY
        m_GameManager.get().getMap().foodProperty().addListener((event, oldValue, newValue) -> Platform.runLater(() ->
        {
            if (m_FoodPickUpAnimation.getStatus() == Animation.Status.RUNNING)
                m_FoodPickUpAnimation.stop();

            if (oldValue == null || newValue == null)
            {
                m_FoodPickUpLabel.setText("");
                return;
            }

            m_FoodPickUpLabel.setText("Picked up " + oldValue.getValue().getName() + " for " + oldValue.getValue().getPoint() + " points.");
            m_FoodPickUpAnimation.play();
        }));


        // RENDER "CYCLE"
        ((MultiPlayerGameManager) m_GameManager.get()).ticksProperty().addListener(event ->
        {
            var manager = ((MultiPlayerGameManager) m_GameManager.get());

            if (manager.getSnake().getBodyCoords().size() == 0 || manager.getSnake2().getBodyCoords().size() == 0)
                return;

            for (var child : m_Grid.getChildren())
            {
                if (GridPane.getColumnIndex(child) == null)
                    return;

                var pos = new Vector2(GridPane.getColumnIndex(child), GridPane.getRowIndex(child));

                if (manager.getMap().getFood() != null && manager.getMap().getFood().getKey().equals(pos))
                    ((Rectangle) child).setFill(manager.getMap().getFood().getValue().getColor());
                else
                    ((Rectangle) child).setFill(Color.BLACK);

                if (manager.isSnake1Alive())
                {
                    if (manager.getSnake().getBodyCoords().get(0).equals(pos))
                        ((Rectangle) child).setFill(manager.getSnake().getHeadColor());
                    else if (manager.getSnake().getBodyCoords().contains(pos))
                        ((Rectangle) child).setFill(manager.getSnake().getBodyColor());
                }

                if (manager.isSnake2Alive())
                {
                    if (manager.getSnake2().getBodyCoords().get(0).equals(pos))
                    ((Rectangle) child).setFill(manager.getSnake2().getHeadColor());
                    else if (manager.getSnake2().getBodyCoords().contains(pos))
                        ((Rectangle) child).setFill(manager.getSnake2().getBodyColor());
                }
            }
        });


        // END GAME NOTIFICATION
        m_GameManager.get().gameStateProperty().addListener((event, oldValue, newValue) ->
        {
            switch (newValue)
            {
                case P1_WON:
                    System.out.println("p1 won");
                    returnToMain();
                    return;

                case P2_WON:
                    System.out.println("p2 won");
                    returnToMain();
                    return;

                case ALL_DEAD:
                    System.out.println("nobody won");
                    returnToMain();
            }
        });


        // CREATE LAYOUT
        reset();
    }


    /* CHANGE SKILL LABELS */
    private void setSkillLabel(Skill skill, Label label, String name)
    {
        if (skill.isInUse())
        {
            var durationLeft = Duration.between(Instant.now(), skill.getLastUsed().plus(skill.getDuration()));
            label.setText(name + " is active! " + durationLeft.toSeconds() + " seconds left.");
        }
        else if (skill.isOnCooldown())
        {
            var cooldown = Duration.between(Instant.now(), skill.getLastUsed().plus(skill.getCooldown()));
            label.setText(name + " is on cooldown! " + cooldown.toSeconds() + " seconds left.");
        }
        else
            label.setText("");
    }


    @Override
    protected void keyCallback(KeyEvent event)
    {
        var manager = (MultiPlayerGameManager) m_GameManager.get();

        if (event.getEventType() == KeyEvent.KEY_PRESSED)
            switch (event.getCode())
            {
                case W:
                    if (manager.getSnake().getCurrentDirection() == Snake.Direction.UP)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake().getCurrentDirection() == Snake.Direction.DOWN)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake().setNextDirection(Snake.Direction.UP);
                    return;

                case UP:
                    if (manager.getSnake2().getCurrentDirection() == Snake.Direction.UP)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake2().getCurrentDirection() == Snake.Direction.DOWN)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake2().setNextDirection(Snake.Direction.UP);
                    return;

                case S:
                    if (manager.getSnake().getCurrentDirection() == Snake.Direction.DOWN)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake().getCurrentDirection() == Snake.Direction.UP)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake().setNextDirection(Snake.Direction.DOWN);
                    return;

                case DOWN:
                    if (manager.getSnake2().getCurrentDirection() == Snake.Direction.DOWN)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake2().getCurrentDirection() == Snake.Direction.UP)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake2().setNextDirection(Snake.Direction.DOWN);
                    return;

                case A:
                    if (manager.getSnake().getCurrentDirection() == Snake.Direction.LEFT)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake().getCurrentDirection() == Snake.Direction.RIGHT)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake().setNextDirection(Snake.Direction.LEFT);
                    return;

                case LEFT:
                    if (manager.getSnake2().getCurrentDirection() == Snake.Direction.LEFT)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake2().getCurrentDirection() == Snake.Direction.RIGHT)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake2().setNextDirection(Snake.Direction.LEFT);
                    return;

                case D:
                    if (manager.getSnake().getCurrentDirection() == Snake.Direction.RIGHT)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake().getCurrentDirection() == Snake.Direction.LEFT)
                        manager.setSnake1Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake().setNextDirection(Snake.Direction.RIGHT);
                    return;

                case RIGHT:
                    if (manager.getSnake2().getCurrentDirection() == Snake.Direction.RIGHT)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.FAST);
                    else if (manager.getSnake2().getCurrentDirection() == Snake.Direction.LEFT)
                        manager.setSnake2Boost(MultiPlayerGameManager.Boost.SLOW);
                    else
                        manager.getSnake2().setNextDirection(Snake.Direction.RIGHT);
                    return;

                case E:
                    if (!manager.getHungerSkill().isOnCooldown())
                        manager.getHungerSkill().setLastUsed(Instant.now());
                    return;

                case M:
                    if (!manager.getHungerSkill2().isOnCooldown())
                        manager.getHungerSkill2().setLastUsed(Instant.now());
            }
        else
        {
            switch (event.getCode())
            {
                case W:
                case A:
                case S:
                case D:
                    manager.setSnake1Boost(MultiPlayerGameManager.Boost.NONE);
                    return;

                case UP:
                case LEFT:
                case DOWN:
                case RIGHT:
                    manager.setSnake2Boost(MultiPlayerGameManager.Boost.NONE);
            }
        }
    }
}
