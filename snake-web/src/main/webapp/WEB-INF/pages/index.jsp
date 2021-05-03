<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>The Snake Strikes Back</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">        <title>Title</title>
    </head>

    <body>
        <div class="container text-center">
            <h1>The Snake Strikes Back </h1>
            <h2>Leaderboard</h2>

            <form class="row g-1" method="get" action="leaderboardController">
                <div>
                    <label for="gamemode">Select a gamemode to list:</label>
                </div>
                <div class="col-12">
                    <select id="gamemode" name="gamemode">
                        <option value="single">Singleplayer</option>
                        <option value="multi">Multiplayer </option>
                    </select>
                </div>
                <div class="col-12">
                    <button class="btn btn-primary" type="submit"> Go</button>
                </div>
            </form>
        </div>
    </body>
</html>
