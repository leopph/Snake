package hu.alkfejl.controller;

import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.model.Result;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;


public class LeaderboardController implements Initializable
{
    @FXML private TableView<Result> m_Table;
    @FXML private Button m_ModeButton;
    @FXML private TableColumn<Result, String> m_NameColumn;
    @FXML private TableColumn<Result, Integer> m_ScoreColumn;
    @FXML private TableColumn<Result, Instant> m_DateColumn;

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
        m_DateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
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
