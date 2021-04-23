package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.model.GameManager;
import hu.alkfejl.model.Snake;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;


public abstract class GameWindowController implements Initializable
{
    @FXML protected GridPane m_Grid;
    @FXML protected Label m_ScoreLabel;
    @FXML protected Label m_FoodPickUpLabel;
    @FXML protected Label m_SkillLabel;
    protected ObjectProperty<GameManager> m_GameManager;
    protected Timeline m_FoodPickUpAnimation;


    public GameWindowController()
    {
        m_GameManager = new SimpleObjectProperty<>();
    }


    public void initialize(URL url, ResourceBundle bundle)
    {
        m_FoodPickUpAnimation = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(
                                m_FoodPickUpLabel.opacityProperty(),
                                1,
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(
                                m_FoodPickUpLabel.opacityProperty(),
                                1,
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(3),
                        new KeyValue(
                                m_FoodPickUpLabel.opacityProperty(),
                                0,
                                Interpolator.DISCRETE
                        )
                )
        );
    }


    public ObjectProperty<GameManager> gameManagerProperty() { return m_GameManager; }
    public GameManager getGameManager() { return m_GameManager.get(); }
    public void setGameManager(GameManager newValue) { m_GameManager.setValue(newValue); }


    public abstract void start();


    public void reset()
    {
        /* CLEAR THE GRID OUT */
        m_Grid.getChildren().clear();
        m_Grid.getRowConstraints().clear();
        m_Grid.getColumnConstraints().clear();


        /* SET THE GRID UP ACCORDING TO MAP */
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
    
    
    protected void keyCallback(KeyEvent event)
    {
        switch (event.getCode())
        {
            case W:
                if (m_GameManager.get().getSnake().getCurrentDirection() != Snake.Direction.DOWN && m_GameManager.get().getSnake().getNextDirection() != Snake.Direction.DOWN)
                    m_GameManager.get().getSnake().setNextDirection(Snake.Direction.UP);
                return;
            case S:
                if (m_GameManager.get().getSnake().getCurrentDirection() != Snake.Direction.UP && m_GameManager.get().getSnake().getNextDirection() != Snake.Direction.UP)
                    m_GameManager.get().getSnake().setNextDirection(Snake.Direction.DOWN);
                return;
            case A:
                if (m_GameManager.get().getSnake().getCurrentDirection() != Snake.Direction.RIGHT && m_GameManager.get().getSnake().getNextDirection() != Snake.Direction.RIGHT)
                    m_GameManager.get().getSnake().setNextDirection(Snake.Direction.LEFT);
                return;
            case D:
                if (m_GameManager.get().getSnake().getCurrentDirection() != Snake.Direction.LEFT && m_GameManager.get().getSnake().getNextDirection() != Snake.Direction.LEFT)
                    m_GameManager.get().getSnake().setNextDirection(Snake.Direction.RIGHT);
                return;
            case E:
                if (!m_GameManager.get().getHungerSkill().isOnCooldown())
                    m_GameManager.get().getHungerSkill().setLastUsed(Instant.now());
        }
    }


    @FXML
    protected void returnToMain()
    {
        m_GameManager.get().stopGame();
        App.getWindowManager().showScene("MainMenu");
    }
}
