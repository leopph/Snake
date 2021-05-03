<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="leopph" %>


<leopph:template title="Update Leaderboard Entry" subtitle="Update Leaderboard Entry" homeButton="true">
    <jsp:useBean id="result" type="hu.alkfejl.model.Result" scope="request"/>

    <form class="row" method="post" action="updateResultController">
        <div class="col-12">
            <label for="name">Name: </label>
            <input id="name" name="name" required="required" value="${result.playerName}">
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
</leopph:template>
