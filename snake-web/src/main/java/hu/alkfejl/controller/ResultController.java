package hu.alkfejl.controller;

import hu.alkfejl.model.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;


@WebServlet("/resultController")
public class ResultController extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        var result = new Result();

        result.setID(Long.parseLong(req.getParameter("id")));
        result.setPlayerName(req.getParameter("name"));
        result.setScore(Integer.parseInt(req.getParameter("score")));
        result.setDate(Instant.parse(req.getParameter("date")));
        result.setGameMode(req.getParameter("gamemode").equals("SINGLE") ? Result.GameMode.SINGLE : Result.GameMode.MULTI);

        req.setAttribute("result", result);
        req.getRequestDispatcher("updateResult.jsp").forward(req, resp);
    }
}
