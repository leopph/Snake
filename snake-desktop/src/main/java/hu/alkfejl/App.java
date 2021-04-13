package hu.alkfejl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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


    public static Stage getStage()
    {
        return m_Stage;
    }



    public static FXMLLoader loadWindow(String fileName)
    {
        try
        {
            var loader = new FXMLLoader(Objects.requireNonNull(App.class.getResource("/fxml/" + fileName)));
            var root = loader.load();
            var scene = new Scene((Parent) root);

            m_Stage.setScene(scene);
            m_Stage.show();

            return loader;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Platform.exit();
            return null;
        }
    }
}