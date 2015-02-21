package nl.starapple.io;

import bot.Bot;
import bot.BotState;
import poker.PokerMove;

public class InMemParser {
    final Bot bot;
    BotState currentState = new BotState();

    public InMemParser(Bot bot) {
        this.bot = bot;
    }

    public PokerMove getMove(long timeout) {
        return bot.getMove(currentState, timeout);
    }

    public void sendLine(String input) {
        if (input.length() == 0) {
            return;
        }

        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            if (parts.length == 3 && parts[0].equals("Action")) {
                // we need to move
                PokerMove move = bot.getMove(currentState, Long.valueOf(parts[2]));
                System.out.println(move.toString());
                System.out.flush();
            } else if (parts.length == 3 && parts[0].equals("Settings")) {    // Update the state with settings info
                currentState.updateSetting(parts[1], parts[2]);
            } else if (parts.length == 3 && parts[0].equals("Match")) {        // Update the state with match info
                currentState.updateMatch(parts[1], parts[2]);
            } else if (parts.length == 3 && parts[0].startsWith("player")) {    // Update the state with info about the moves
                currentState.updateMove(parts[0], parts[1], parts[2]);
            } else if (line.contains("end of hand")) {
                bot.endOfHand(currentState);
            } else {
                System.err.printf("Unable to parse line ``%s''\n", line);
            }
        }
    }
}
