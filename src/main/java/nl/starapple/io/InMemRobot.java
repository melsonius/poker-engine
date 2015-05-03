package nl.starapple.io;

import nl.starapple.poker.HandInfo;
import nl.starapple.poker.HandResultInfo;
import nl.starapple.poker.MatchInfo;
import nl.starapple.poker.PokerMove;
import nl.starapple.poker.PreMoveInfo;
import nl.starapple.poker.Robot;

import java.io.IOException;

public class InMemRobot implements Robot {
    InMemParser handler;
    StringBuilder dump;
    int errorCounter;
    long startingTime;
    final int maxErrors = 2;
    final int maxTime = 300000; //bot may run max 5 minutes

    public InMemRobot(InMemParser parser) throws IOException {
        this.handler = parser;
        startingTime = System.currentTimeMillis();
        dump = new StringBuilder();
        errorCounter = 0;
    }

    @Override
    public void setup(long timeOut)
    {
//        handler.readLine(timeOut);
    }

    @Override
    public void writeMove(PokerMove move) {
        handler.sendLine(move.toString());
        addToDump(move.toString() + "\n");
    }

    @Override
    public PokerMove getMove(String botCodeName, long timeOut) {
        poker.PokerMove move = handler.getMove(timeOut);
        return new PokerMove(move.getAction(), move.getAmount());
    }

    @Override
    public void writeInfo(MatchInfo info) {
        String i = info.toString();
        handler.sendLine(i);
        addToDump(i + "\n");
    }

    @Override
    public void writeInfo(HandInfo info) {
        String i = info.toString();
        handler.sendLine(i);
        addToDump(i + "\n");
    }

    @Override
    public void writeInfo(PreMoveInfo info) {
        String i = info.toString();
        handler.sendLine(i);
        addToDump(i + "\n");
    }

    @Override
    public void writeInfo(String string) {
        handler.sendLine(string);
        addToDump(string + "\n");
    }

    @Override
    public void writeResult(HandResultInfo info) {
        String i = info.toString();
        handler.sendLine(i);
        addToDump(i + "\n");
    }

    public void addToDump(String dumpy){
        dump.append(dumpy);
    }
}
