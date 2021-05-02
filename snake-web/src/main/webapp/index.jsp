<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Title</title>
    </head>

    <body>
        <form method="get" action="leaderboardController">
            <select id="gamemode" name="gamemode">
                <option value="single">Singleplayer</option>
                <option value="multi">Multiplayer </option>
            </select>
            <button type="submit"> List</button>
        </form>
    </body>
</html>
