<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<head>
    <title>Update Leaderboard Entry</title>
</head>
<body>
    <jsp:useBean id="result" type="hu.alkfejl.model.Result" scope="request"/>
    <form method="post" action="updateResultController">
        <label for="name">Name: </label>
        <input id="name" name="name" value="${result.playerName}">
        <br/>

        <label for="score">Score: </label>
        <input id="score" name="score" value="${result.score}">

        <input type="hidden" name="gamemode" value="${result.gameMode}">
        <input type="hidden" name="id" value="${result.ID}">
        <input type="hidden" name="date" value="${result.date}">
        <button type="submit">Confirm</button>
    </form>
</body>
</html>
