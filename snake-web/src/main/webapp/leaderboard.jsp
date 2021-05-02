<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
    <head>
        <title>${sessionScope.gamemode} Leaderboard</title>
    </head>

    <body>
        <table>
            <tr>
                <th>Name</th>
                <th>Score</th>
                <th>Date</th>
                <th>Actions</th>
            </tr>

            <c:forEach items="${sessionScope.results}" var="result">
                <tr>
                    <td>${result.playerName}</td>
                    <td>${result.score}</td>
                    <td>${result.date}</td>
                    <td>
                        <form method="post" action="resultController">
                            <input type="hidden" name="id" value="${result.ID}">
                            <input type="hidden" name="name" value="${result.playerName}">
                            <input type="hidden" name="score" value="${result.score}">
                            <input type="hidden" name="gamemode" value="${result.gameMode}">
                            <input type="hidden" name="date" value="${result.date}">
                            <button type="submit">test</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>

        </table>
    </body>
</html>
