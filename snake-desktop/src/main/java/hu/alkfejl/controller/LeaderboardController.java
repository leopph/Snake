package hu.alkfejl.controller;

import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.model.Result;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

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


        m_ActionColumn.setCellFactory(param -> new TableCell<>()
        {
            private final Button m_DeleteButton = new Button("Delete");

            {
                m_DeleteButton.setOnAction(event ->
                {
                    var result = getTableRow().getItem();

                    if (result == null)
                        return;

                    m_DAO.delete(result).setOnSucceeded(param -> reset());
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
                    hbox.getChildren().add(m_DeleteButton);
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
}
