package hu.alkfejl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class WindowManager
{
    /* PROPERTIES */
    private final Stage m_Stage;
    private final Map<String, Scene> m_Scenes;


    /* CONSTRUCTORS */
    public WindowManager(Stage stage)
    {
        m_Stage = stage;
        m_Scenes = new HashMap<>();
    }


    /* SCENE CREATION AND LOADING */
    public boolean showScene(String name)
    {
        var scene = m_Scenes.get(name);
        if (scene == null)
            return false;

        var width = m_Stage.getWidth();
        var height = m_Stage.getHeight();
        m_Stage.setScene(scene);
        m_Stage.setWidth(width);
        m_Stage.setHeight(height);

        m_Stage.show();
        return true;
    }


    public void createScene(String name, Parent parent)
    {
        m_Scenes.put(name, new Scene(parent));
    }


    public FXMLLoader createScene(String name, String fileName)
    {
        var loader = new FXMLLoader(App.class.getResource("/fxml/" + fileName));

        try
        {
            var root = (Parent) loader.load();
            m_Scenes.put(name, new Scene(root));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return loader;
    }
}
