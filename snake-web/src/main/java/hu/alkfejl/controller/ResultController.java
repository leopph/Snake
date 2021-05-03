package hu.alkfejl.controller;

import hu.alkfejl.utility.ResultDeserializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/resultController")
public class ResultController extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        var result = ResultDeserializer.deserialize(
                req.getParameter("id"),
                req.getParameter("name"),
                req.getParameter("score"),
                req.getParameter("date"),
                req.getParameter("gamemode"));

        req.setAttribute("result", result);
        req.getRequestDispatcher("updateResult").forward(req, resp);
    }
}
