package hu.alkfejl.controller;

import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.model.Result;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDateTime;
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
    }


    public void start()
    {

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
}
