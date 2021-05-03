<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="leopph" %>


<html>
    <head>
        <title>${sessionScope.gamemode} Leaderboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
    </head>

    <body>
        <table class="table table-dark table-striped">
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Score</th>
                <th>Date</th>
                <th>Actions</th>
            </tr>

            <c:forEach items="${sessionScope.results}" var="result" varStatus="loop">
                <tr>
                    <td>${loop.index + 1}</td>
                    <td>${result.playerName}</td>
                    <td>${result.score}</td>
                    <td><leopph:formatInstant arg="${result.date}"/></td>
                    <td>
                        <div class="btn-group" role="group">
                        <form method="post" action="resultController">
                            <input type="hidden" name="id" value="${result.ID}">
                            <input type="hidden" name="name" value="${result.playerName}">
                            <input type="hidden" name="score" value="${result.score}">
                            <input type="hidden" name="gamemode" value="${result.gameMode}">
                            <input type="hidden" name="date" value="${result.date}">
                            <button class="btn btn-light m-3" type="submit">Modify</button>
                        </form>

                        <form method="post" action="deleteResultController">
                            <input type="hidden" name="id" value="${result.ID}">
                            <input type="hidden" name="name" value="${result.playerName}">
                            <input type="hidden" name="score" value="${result.score}">
                            <input type="hidden" name="gamemode" value="${result.gameMode}">
                            <input type="hidden" name="date" value="${result.date}">
                            <button class="btn btn-light m-3" type="submit">Delete</button>
                        </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>

        </table>
    </body>
</html>
