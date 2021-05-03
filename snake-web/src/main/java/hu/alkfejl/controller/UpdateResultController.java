package hu.alkfejl.controller;

import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.utility.ResultDeserializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/updateResultController")
public class UpdateResultController extends HttpServlet
{
    private final ScoreDAO m_DAO;


    public UpdateResultController()
    {
        m_DAO = ScoreDAO.getInstance();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        if (req.getParameter("name").isBlank() || !req.getParameter("score").matches("(^[0-9]+$)"))
        {
            System.out.println("Got invalid parameter format. Redirecting to home...");
            resp.sendRedirect("index");
            return;
        }

        var result = ResultDeserializer.deserialize(
                req.getParameter("id"),
                req.getParameter("name"),
                req.getParameter("score"),
                req.getParameter("date"),
                req.getParameter("gamemode"));

        m_DAO.update(result);
        resp.sendRedirect("leaderboardController?gamemode=" + result.getGameMode().name.toLowerCase());
    }
}
