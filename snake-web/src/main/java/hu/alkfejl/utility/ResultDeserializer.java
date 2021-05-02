package hu.alkfejl.utility;

import hu.alkfejl.model.Result;

import java.time.Instant;


public class ResultDeserializer
{
    public static Result deserialize(String... args)
    {
        var result = new Result();

        switch (args.length)
        {
            case 5:
                result.setGameMode(args[4].equals("SINGLE") ? Result.GameMode.SINGLE : Result.GameMode.MULTI);
            case 4:
                result.setDate(Instant.parse(args[3]));
            case 3:
                result.setScore(Integer.parseInt(args[2]));
            case 2:
                result.setPlayerName(args[1]);
            case 1:
                result.setID(Long.parseLong(args[0]));
                break;
            default:
                throw new IllegalArgumentException("Cannot deserialize result. Number of arguments is invalid.");
        }

        return result;
    }
}
