package hu.alkfejl.controller;

import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.model.Result;
import hu.alkfejl.utility.ResultDeserializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;


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
        var result = ResultDeserializer.deserialize(
                req.getParameter("id"),
                req.getParameter("name"),
                req.getParameter("score"),
                req.getParameter("date"),
                req.getParameter("gamemode"));

        m_DAO.update(result);
        resp.sendRedirect("/snake");
    }
}
