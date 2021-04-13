package hu.alkfejl;

import hu.alkfejl.model.GameManager;
import hu.alkfejl.model.Map;
import hu.alkfejl.model.Snake;
import hu.alkfejl.model.SinglePlayerGameManager;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;


public class App extends Application
{
    ObjectProperty<GameManager.GameState> gameStateObjectProperty = new SimpleObjectProperty<>();

    @Override
    public void start(Stage stage)
    {
        var game = new SinglePlayerGameManager(new Snake(), new Map(10, 10));
        game.getMap().addWalls(Map.Wall.RIGHT);
        gameStateObjectProperty.bind(game.getStateProperty());
        gameStateObjectProperty.addListener((event, oldValue, newValue) ->
        {
            if (newValue == GameManager.GameState.ENDED)
                System.out.println("well its over bois");
        });

        game.setSpeed(1);
        game.startGame();
    }
}