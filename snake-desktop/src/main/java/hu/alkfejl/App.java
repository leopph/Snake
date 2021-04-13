package hu.alkfejl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class App extends Application
{
    private static Stage m_Stage;


    @Override
    public void start(Stage stage)
    {
        m_Stage = stage;
        loadWindow("main_menu.fxml");
    }


    public static void loadWindow(String fileName)
    {
        try
        {
            var root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("/fxml/" + fileName)));
            var scene = new Scene((Parent) root);
            m_Stage.setScene(scene);
            m_Stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Platform.exit();
        }
    }
}