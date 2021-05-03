package hu.alkfejl.controller;

import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.model.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/leaderboardController")
public class LeaderboardController extends HttpServlet
{
    private final ScoreDAO m_DAO;


    public LeaderboardController()
    {
        try { Class.forName("org.sqlite.JDBC"); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        m_DAO = ScoreDAO.getInstance();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        if (req.getParameter("gamemode").equals("single"))
        {
            req.getSession().setAttribute("results", m_DAO.findAllByGameMode(Result.GameMode.SINGLE));
            req.getSession().setAttribute("gamemode", "Singleplayer");
        }
        else if (req.getParameter("gamemode").equals("multi"))
        {
            req.getSession().setAttribute("results", m_DAO.findAllByGameMode(Result.GameMode.MULTI));
            req.getSession().setAttribute("gamemode", "Multiplayer");
        }
        else
            throw new IllegalArgumentException("Game mode is invalid!");

        resp.sendRedirect("leaderboard");
    }
}
