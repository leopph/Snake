package hu.alkfejl.controller;

import hu.alkfejl.model.*;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;


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

            m_FoodPickUpLabel.setText("Picked up " + oldValue.getValue().getName() + " for " + oldValue.getValue().getPoint() + "points.");
            m_FoodPickUpAnimation.play();
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
            if (newValue != GameManager.GameState.P1_WON && newValue != GameManager.GameState.WALL_HIT && newValue != GameManager.GameState.SELF_ATE)
                return;

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

            alert.setContentText(alert.getContentText() + "\nYour score was " + m_GameManager.get().getPoints() + "."); // DEBUG
            alert.showAndWait();
            returnToMain();
        });


        /* DO THE LAYOUT SETUP */
        reset();
    }


    @Override
    public void reset()
    {
        /* CLEAR THE GRID OUT */
        m_Grid.getChildren().clear();
        m_Grid.getRowConstraints().clear();
        m_Grid.getColumnConstraints().clear();


        /* SET THHE GRID UP ACCORDING TO MAP */
        var sizeBinding = Bindings.min(m_Grid.widthProperty().divide(m_GameManager.get().getMap().getSizeX()),
                m_Grid.heightProperty().divide(m_GameManager.get().getMap().getSizeY()));

        for (int i = 0; i < m_GameManager.get().getMap().getSizeX(); i++)
            for (int j = 0; j < m_GameManager.get().getMap().getSizeY(); j++)
            {
                var rect = new Rectangle();
                rect.widthProperty().bind(sizeBinding);
                rect.heightProperty().bind(sizeBinding);
                rect.setFill(Color.BLACK);
                m_Grid.add(rect, i, j);
            }

        /* START THE GAME THREAD */
        m_GameManager.get().startGame();
    }
}
