package hu.alkfejl.dao;

import hu.alkfejl.config.Configuration;
import hu.alkfejl.model.Result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SinglePlayerScoreDAO
{
    /* SQL STATEMENT TEMPLATES */
    private static final String INSERT_STATEMENT;
    private static final String SELECT_STATEMENT;
    private static final String DELETE_STATEMENT;

    static
    {
        INSERT_STATEMENT = "INSERT INTO RESULT (playername, score, gamemode) VALUES (?, ?, ?)";
        SELECT_STATEMENT = "SELECT * FROM RESULT WHERE gamemode = 'SINGLE' ORDER BY score";
        DELETE_STATEMENT = "DELETE FROM RESULT WHERE name = ? and score = ? gamemode = SINGLE";
    }


    /* INSTANCE HOLDER */
    private static class Singleton
    {
        private static final SinglePlayerScoreDAO s_Instance = new SinglePlayerScoreDAO();
    }


    public static SinglePlayerScoreDAO getInstance()
    {
        return Singleton.s_Instance;
    }


    /* PREVENT UNWANTED INSTANCE CREATIONS */
    private SinglePlayerScoreDAO()
    {
        if (Singleton.s_Instance != null)
            throw new InstantiationError("SINGLETON INSTANCE ALREADY EXISTS");
    }


    public List<Result> findAll()
    {
        var ret = new ArrayList<Result>();

        try (Connection connection = DriverManager.getConnection(Configuration.getValue("DATABASE_URL")))
        {
            var results = connection.createStatement().executeQuery(SELECT_STATEMENT);

            while (results.next())
            {
                var result = new Result();
                result.setPlayerName(results.getString("playername"));
                result.setScore(results.getInt("score"));
                result.setGameMode((results.getString("gamemode").equals("SINGLE") ? Result.GameMode.SINGLE : Result.GameMode.MULTI));

                ret.add(result);
            }
        }
        catch (SQLException exception) { exception.printStackTrace(); }

        return ret;
    }
}
