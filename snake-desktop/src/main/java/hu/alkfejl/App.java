package hu.alkfejl;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;


public class App extends Application
{
    private BooleanProperty readThis = new SimpleBooleanProperty(false);
    private BooleanProperty writeThis = new SimpleBooleanProperty(false);


    @Override
    public void start(Stage stage)
    {
        readThis.bindBidirectional(writeThis);

        var scene = new Scene(new Label());
        scene.setOnKeyPressed(event -> writeThis.setValue(true));

        stage.setScene(scene);
        stage.show();

        var service = new ScheduledService<Void>()
        {
            @Override
            protected Task<Void> createTask()
            {
                return new Task<>()
                {
                    @Override
                    protected Void call()
                    {
                        if (readThis.getValue())
                        {
                            readThis.setValue(false);
                            System.out.println("eef");
                        }
                        System.out.println(System.currentTimeMillis());
                        return null;
                    }
                };
            }
        };

        service.setPeriod(new Duration(1000));
        service.start();
    }
}