package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.model.Result;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;


public class LeaderboardController implements Initializable
{
    @FXML private TableView<Result> m_Table;
    @FXML private Button m_ModeButton;
    @FXML private TableColumn<Result, String> m_NameColumn;
    @FXML private TableColumn<Result, Integer> m_ScoreColumn;
    @FXML private TableColumn<Result, String> m_DateColumn;
    @FXML private TableColumn<Result, Void> m_ActionColumn;

    private final ScoreDAO m_DAO;
    private Result.GameMode m_CurrentMode;


    public LeaderboardController()
    {
        m_DAO = ScoreDAO.getInstance();
        m_CurrentMode = Result.GameMode.SINGLE;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        reset();


        m_NameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        m_ScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        m_DateColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault())
                .format(features.getValue().getDate())));

        m_NameColumn.prefWidthProperty().bind(m_Table.widthProperty().divide(4));
        m_ScoreColumn.prefWidthProperty().bind(m_Table.widthProperty().divide(4));
        m_DateColumn.prefWidthProperty().bind(m_Table.widthProperty().divide(4));
        m_ActionColumn.prefWidthProperty().bind(m_Table.widthProperty().divide(4));


        m_ActionColumn.setCellFactory(param -> new TableCell<>()
        {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event ->
                {
                    var result = getTableRow().getItem();

                    if (result == null)
                        return;

                    var dialog = new Dialog<Boolean>()
                    {
                        {
                            initStyle(StageStyle.UNDECORATED);

                            var loader = new FXMLLoader(App.class.getResource("/fxml/edit_score.fxml"));

                            try
                            {
                                var root = loader.<Parent>load();
                                getDialogPane().setContent(root);
                                getDialogPane().getContent().setStyle("-fx-padding: 0 ;"); // I SUFFERED HOURS WITH THIS
                                loader.<ScoreEditController>getController().start(result, this);
                            }
                            catch (IOException exception) { exception.printStackTrace(); }
                        }
                    };

                    dialog.showAndWait();

                    if (dialog.getResult())
                    {
                        var task = new Task<Void>()
                        {
                            @Override
                            protected Void call() throws Exception
                            {
                                m_DAO.update(result);
                                return null;
                            }
                        };

                        task.setOnSucceeded(param -> reset());
                        new Thread(task).start();
                    }


                    reset();
                });

                deleteButton.setOnAction(event ->
                {
                    var result = getTableRow().getItem();

                    if (result == null)
                        return;

                    var task = new Task<Void>()
                    {
                        @Override
                        protected Void call() throws Exception
                        {
                            m_DAO.delete(result);
                            return null;
                        }
                    };

                    task.setOnSucceeded(param -> reset());
                    new Thread(task).start();
                });
            }


            @FXML
            protected void updateItem(Void item, boolean empty)
            {
                super.updateItem(item, empty);

                if (empty)
                    setGraphic(null);
                else
                {
                    var hbox = new HBox();
                    hbox.getChildren().addAll(editButton, deleteButton);
                    setGraphic(hbox);
                }
            }
        });
    }


    public void reset()
    {
        var task = new Task<Void>()
        {
            @Override
            protected Void call()
            {
                m_Table.getItems().setAll(m_DAO.findAllByGameMode(m_CurrentMode));
                return null;
            }
        };

        new Thread(task).start();
    }


    @FXML
    private void onModeChange()
    {
        if (m_CurrentMode.equals(Result.GameMode.SINGLE))
        {
            m_CurrentMode = Result.GameMode.MULTI;
            m_ModeButton.setText("Singleplayer");
        }
        else if (m_CurrentMode.equals(Result.GameMode.MULTI))
        {
            m_CurrentMode = Result.GameMode.SINGLE;
            m_ModeButton.setText("Multiplayer");
        }

        reset();
    }


    @FXML
    private void onBack()
    {
        App.getWindowManager().showScene("MainMenu");
    }

    @FXML
    private void onDeleteCategory()
    {
        var task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {
                m_DAO.deleteByCategory(m_CurrentMode);
                return null;
            }
        };

        task.setOnSucceeded(event -> reset());
        new Thread(task).start();
    }

    @FXML
    private void onDeleteAll()
    {
        var task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {
                m_DAO.deleteAll();
                return null;
            }
        };

        task.setOnSucceeded(event -> reset());
        new Thread(task).start();
    }
}
