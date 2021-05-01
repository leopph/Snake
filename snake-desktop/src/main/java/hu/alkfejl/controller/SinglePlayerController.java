package hu.alkfejl.controller;

import hu.alkfejl.model.*;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;

import java.time.Duration;
import java.time.Instant;


public class SinglePlayerController extends GameWindowController
{
    @Override
    public void start()
    {
        m_Grid.setAlignment(Pos.CENTER);
        m_Grid.getScene().setOnKeyPressed(this::keyCallback);

        /* DISPLAY CURRENT SCORE */
        m_ScoreLabel.textProperty().bind(Bindings.createStringBinding(() -> "Score: " + m_GameManager.get().getPoints(), m_GameManager.get().pointsProperty()));

        /* DISPLAY PICKED UP FOOD FOR A SHORT PERIOD */
        m_GameManager.get().getMap().foodProperty().addListener((event, oldValue, newValue) -> Platform.runLater(() ->
        {
            if (m_FoodPickUpAnimation.getStatus() == Animation.Status.RUNNING)
                m_FoodPickUpAnimation.stop();

            if (oldValue == null)
                return;

            m_FoodPickUpLabel.setText("Picked up " + oldValue.getValue().getName() + " for " + oldValue.getValue().getPoint() + " points.");
            m_FoodPickUpAnimation.play();
        }));

        /* DISPLAY CURRENT USED SKILL AND COOLDOWN */
        m_GameManager.get().hungerSkillProperty().get().ticksProperty().addListener((event, oldValue, newValue) -> Platform.runLater(() ->
        {
            var skill = m_GameManager.get().getHungerSkill();

            if (skill.isInUse())
            {
                var cooldown = Duration.between(Instant.now(), skill.getLastUsed().plus(skill.getCooldown()));
                var durationLeft = Duration.between(Instant.now(), skill.getLastUsed().plus(skill.getDuration()));
                m_SkillLabel.setText("Hunger is active!\nTime left: " + durationLeft.toSeconds() + "\nCooldown: " + cooldown.toSeconds() + ".");
            }
            else if (skill.isOnCooldown())
            {
                var cooldown = Duration.between(Instant.now(), skill.getLastUsed().plus(skill.getCooldown()));
                m_SkillLabel.setText("Hunger is on cooldown. " + cooldown.toSeconds() + " seconds left.");
            }
            else
                m_SkillLabel.setText("");
        }));


        /* RENDER CYCLE FOR SNAKE */
        ListChangeListener<Vector2> listener = event ->
        {
            if (event.getList().size() == 0)
                return;

            for (var child : m_Grid.getChildren())
            {
                if (GridPane.getColumnIndex(child) == null)
                    return;

                var pos = new Vector2(GridPane.getColumnIndex(child), GridPane.getRowIndex(child));
                if (m_GameManager.get().getSnake().getBodyCoords().get(0).equals(pos))
                    ((Rectangle) child).setFill(m_GameManager.get().getSnake().getHeadColor());
                else if (m_GameManager.get().getSnake().getBodyCoords().contains(pos))
                    ((Rectangle) child).setFill(m_GameManager.get().getSnake().getBodyColor());
                else if (m_GameManager.get().getMap().getFood() != null && m_GameManager.get().getMap().getFood().getKey().equals(pos))
                    ((Rectangle) child).setFill(m_GameManager.get().getMap().getFood().getValue().getColor());
                else
                    ((Rectangle) child).setFill(Color.BLACK);
            }
        };

        /* IF ITS STUPID BUT IT WORKS ITS NOT STUPID */
        m_GameManager.get().getSnake().getBodyCoords().addListener(listener);
        m_GameManager.get().snakeProperty().addListener(event -> m_GameManager.get().getSnake().getBodyCoords().addListener(listener));


        /* RENDER CYCLE FOR FOODS */
        m_GameManager.get().getMap().foodProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == null)
                return;

            for (var child : m_Grid.getChildren())
            {
                var x = GridPane.getColumnIndex(child);
                var y = GridPane.getRowIndex(child);

                if (x != null && y != null && newValue.getKey().getX() == x && newValue.getKey().getY() == y)
                    ((Rectangle) child).setFill(m_GameManager.get().getMap().getFood().getValue().getColor());
            }
        });


        /* GET NOTIFIED WHEN GAME ENDS */
        m_GameManager.get().gameStateProperty().addListener((event, oldValue, newValue) ->
        {
            /* PASS IF ITS NOT THE END */
            if (newValue != GameManager.GameState.P1_WON && newValue != GameManager.GameState.WALL_HIT && newValue != GameManager.GameState.SELF_ATE)
                return;

            /* CREATE A NICE ALERT ABOUT THE RESULT */
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setGraphic(null);

            switch (newValue)
            {
                case P1_WON:
                    alert.setTitle("You won");
                    alert.setHeaderText("You achieved victory.");
                    alert.setContentText("Nobody has ever seen a snake of such magnitude...");
                    break;

                case WALL_HIT:
                    alert.setTitle("You died");
                    alert.setHeaderText("You hit a wall.");
                    alert.setContentText("You might wanna work on those wall-dodging skills...");
                    break;

                case SELF_ATE:
                    alert.setTitle("You died");
                    alert.setHeaderText("You ate yourself.");
                    alert.setContentText("Snake's gotta eat, am I right?");
            }

            alert.setContentText(alert.getContentText() + "\nYour score was " + m_GameManager.get().getPoints() + ".");
            alert.showAndWait();

            /* IF PLAYER FORGOT TO NAME THEMSELVES, HELP'EM */
            if (m_GameManager.get().getPlayerName() == null || m_GameManager.get().getPlayerName().isEmpty())
            {
                var nameDialog = new TextInputDialog();
                nameDialog.setGraphic(null);
                nameDialog.setTitle("Missing name");
                nameDialog.setHeaderText("Please enter your name to display on the leaderboard!");

                /* shh, dont let them get away with nothing */
                while (true)
                {
                    nameDialog.showAndWait();
                    var result = nameDialog.getResult();
                    if (result != null && !result.isEmpty())
                    {
                        m_GameManager.get().setPlayerName(result);
                        break;
                    }
                }
            }

            /* STORE RESULT IN DB */
            var result = new Result();
            result.setPlayerName(m_GameManager.get().getPlayerName());
            result.setScore(m_GameManager.get().getPoints());
            result.setDate(Instant.now());
            result.setGameMode(Result.GameMode.SINGLE);
            m_DAO.insert(result);

            /* GO BACK */
            returnToMain();
        });


        /* DO THE LAYOUT SETUP */
        reset();
    }
}
