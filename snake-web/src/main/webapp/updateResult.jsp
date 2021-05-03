<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
    <head>
        <title>Update Leaderboard Entry</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
    </head>

    <body>
        <div class="container text-center">
            <h1>The Snake Strikes Back</h1>
            <h2>Edit leaderboard entry</h2>

            <jsp:useBean id="result" type="hu.alkfejl.model.Result" scope="request"/>

            <form class="row" method="post" action="updateResultController">
                <div class="col-12">
                    <label for="name">Name: </label>
                    <input id="name" name="name" value="${result.playerName}">
                </div>
                <div class="col-12">
                    <label for="score">Score: </label>
                    <input id="score" name="score" pattern="^[0-9]+$" value="${result.score}">
                </div>
                <div class="col-12">
                    <button class="btn btn-primary" type="submit">Confirm</button>
                </div>

                <input type="hidden" name="gamemode" value="${result.gameMode}">
                <input type="hidden" name="id" value="${result.ID}">
                <input type="hidden" name="date" value="${result.date}">
            </form>
        </div>
    </body>
</html>
