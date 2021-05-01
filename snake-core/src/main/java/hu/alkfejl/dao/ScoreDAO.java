package hu.alkfejl.dao;

import hu.alkfejl.config.Configuration;
import hu.alkfejl.model.Result;
import javafx.concurrent.Task;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO
{
    /* SQL STATEMENT TEMPLATES */
    private static final String INSERT_STATEMENT;
    private static final String SELECT_GAMEMODE_STATEMENT;
    private static final String DELETE_SINGLE_STATEMENT;
    private static final String DELETE_GAMEMODE_STATEMENT;
    private static final String DELETE_ALL_STATEMENT;

    static
    {
        INSERT_STATEMENT = "INSERT INTO RESULT (playername, score, date, gamemode) VALUES (?, ?, ?, ?)";

        SELECT_GAMEMODE_STATEMENT = "SELECT * FROM RESULT WHERE gamemode = ? ORDER BY score";

        DELETE_SINGLE_STATEMENT = "DELETE FROM RESULT WHERE playername = ? AND score = ? AND gamemode = ?";
        DELETE_GAMEMODE_STATEMENT = "DELETE FROM RESULT WHERE gamemode = ?";
        DELETE_ALL_STATEMENT = "DELETE FROM RESULT";
    }


    /* INSTANCE HOLDER */
    private static class Singleton { private static final ScoreDAO s_Instance = new ScoreDAO(); }


    public static ScoreDAO getInstance()
    {
        return Singleton.s_Instance;
    }


    /* PREVENT UNWANTED INSTANCE CREATIONS */
    private ScoreDAO()
    {
        if (Singleton.s_Instance != null)
            throw new InstantiationError("SINGLETON INSTANCE ALREADY EXISTS");
    }


    public List<Result> findAllByGameMode(Result.GameMode gameMode)
    {
        var ret = new ArrayList<Result>();

        try (var connection = DriverManager.getConnection(Configuration.getValue("DATABASE_URL"));
            var statement = connection.prepareStatement(SELECT_GAMEMODE_STATEMENT))
        {
            statement.setString(1, gameMode.name);
            var results = statement.executeQuery();

            while (results.next())
            {
                var result = new Result();
                result.setGameMode(gameMode);
                result.setPlayerName(results.getString("playername"));
                result.setScore(results.getInt("score"));
                result.setDate(Instant.parse(results.getString("date")));

                ret.add(result);
            }
        }
        catch (SQLException e) { e.printStackTrace(); }

        return ret;
    }


    /* CREATE A NEW THREAD THAT DELETES DATA THEN RETURN */
    public Task<Void> insert(Result r)
    {
        var insertTask = new Task<Void>()
        {
            @Override
            protected Void call()
            {
                try (var connection = DriverManager.getConnection(Configuration.getValue("DATABASE_URL"));
                    var statement = connection.prepareStatement(INSERT_STATEMENT))
                {
                    statement.setString(1, r.getPlayerName());
                    statement.setInt(2, r.getScore());
                    statement.setString(3, r.getDate().toString());
                    statement.setString(4, r.getGameMode().name);

                    statement.execute();
                }
                catch (SQLException exception) { exception.printStackTrace(); }

                return null;
            }
        };

        new Thread(insertTask).start();
        return insertTask;
    }


    public Task<Void> delete(Result r)
    {
        var deleteTask = new Task<Void>()
        {
            @Override
            protected Void call()
            {
                try (var connection = DriverManager.getConnection(Configuration.getValue("DATABASE_URL"));
                var statement = connection.prepareStatement(DELETE_SINGLE_STATEMENT))
                {
                    statement.setString(1, r.getPlayerName());
                    statement.setInt(2, r.getScore());
                    statement.setString(3, r.getGameMode().name);

                    statement.execute();
                }
                catch (SQLException exception) { exception.printStackTrace(); }

                return null;
            }
        };

        new Thread(deleteTask).start();
        return deleteTask;
    }
}
