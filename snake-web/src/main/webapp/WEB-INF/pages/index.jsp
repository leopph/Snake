<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="leopph" %>


<leopph:template title="The Snake Strikes Back" subtitle="">
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
</leopph:template>
