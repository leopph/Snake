package hu.alkfejl;

import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application
{
    private static WindowManager s_WindowManager;


    @Override
    public void start(Stage stage)
    {
        stage.setTitle("The Snake Strikes Back");

        s_WindowManager = new WindowManager(stage);
        s_WindowManager.createScene("MainMenu", "main_menu.fxml");
        s_WindowManager.showScene("MainMenu");
    }


    public static WindowManager getWindowManager() { return s_WindowManager; }
}