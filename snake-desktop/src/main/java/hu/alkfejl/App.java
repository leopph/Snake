package hu.alkfejl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class App extends Application
{
    @Override
    public void start(Stage stage)
    {
        try
        {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/single_player_map.fxml")))));
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}