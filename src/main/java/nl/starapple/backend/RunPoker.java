// Copyright 2014 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//  
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package nl.starapple.backend;

import bot.JaegerBot;
import nl.starapple.io.IORobot;
import nl.starapple.io.InMemParser;
import nl.starapple.io.InMemRobot;
import nl.starapple.poker.MatchPlayer;
import nl.starapple.poker.PokerBot;
import nl.starapple.poker.Robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RunPoker {
    private static int gameType;
    private static int numberOfHands;
    private static int startingStack;
    private static int placesPaid;

    String bot1Command, bot2Command;

    String playerName1, playerName2;

    MatchPlayer engine;
    List<PokerBot> bots;


    double[] bot1Params = new double[]{
            0.95, 0.00, 0.00,
            0.05, 0.20, 0.40,
            0.51, 0.60, 0.75,
            0.05, 0.29, 0.75,
            0.75, 0.99, 0.00,
            0.95, 0.99, 0.00,
    };

    double[] bot2Params = new double[]{
            0.95, 0.00, 0.00,
            0.05, 0.20, 0.40,
            0.51, 0.60, 0.75,
            0.05, 0.29, 0.75,
            0.75, 0.99, 0.00,
            0.95, 0.99, 0.00,
    };


    //*
    public static void main(String args[]) throws Exception {
        int numReps = 1;
        if (args.length >= 4) {
            numReps = Integer.valueOf(args[3]);
        }

        // current best
        double[] bot1Params = new double[]{
                0.95, 0.00, 0.00,
                0.05, 0.20, 0.40,
                0.51, 0.60, 0.75,
                0.05, 0.29, 0.75,
                0.75, 0.99, 0.00,
                0.95, 0.99, 0.00,
        };

        // act first
        // respond to check this round
        // respond to bet this round
        // respond to check last round
        // respond to bet last round
        double[] bot2Params = new double[]{
//                0.95, 0.00, 0.00,
//                0.05, 0.20, 0.40,
//                0.51, 0.60, 0.75,
//                0.05, 0.29, 0.75,
//                0.75, 0.99, 0.00,
//                0.95, 0.99, 0.00,

                0.1582341210196752, 0.32673722602720234, 0.16813368273907014,
                0.6394187819550321, 0.46889810055137604, 1.1231643328713636,
                0.8903957730916621, 0.5978896493940581, 0.582029119990688,
                0.09417521061226142, 0.18353069893915405, 1.2190974049308645,
                1.7601316810651442, 0.13867256679856824, 0.8823894408193513,
                1.2571947807335297, 0.20119561714178874, 1.0490295770028804

//                0.73, 0.80, 0.85,   // first action
//                0.25, 0.98, 0.99,   // opp checked
//                0.32, 0.77, 0.88,   // opp bet
//                0.25, 0.98, 0.99,   // opp checked to end last round
//                0.32, 0.77, 0.88,   // opp called last round
//                0.50, 0.77, 0.88    // opp raised last round

        };

        long start = System.currentTimeMillis();
        // serial
        int p1Wins = 0;
        int p2Wins = 0;
        for (int i = 0; i < numReps || numReps < 0; ++i) {
            RunPoker run = new RunPoker(args, bot1Params, bot2Params);
            try {
                if (run.go()) {
                    ++p1Wins;
                } else {
                    ++p2Wins;
                }
            } catch (Exception e) {
                e.printStackTrace();
                run.finish();
            }

            System.out.println("P1 won " + p1Wins + " (" + (p1Wins * 100.0 / (p1Wins + p2Wins)) + "%)");
            System.out.println("P2 won " + p2Wins + " (" + (p2Wins * 100.0 / (p1Wins + p2Wins)) + "%)");
            System.out.println("Elapsed time (s): " + (System.currentTimeMillis() - start) / 1000.0);
        }

        System.exit(0);
    }

    /*/
    static int POPULATION_SIZE = 150;
    static int SOLUTION_LENGTH = 6 * 3;
    static int MAX_ITERATIONS = 100000;

    private static Population initPopulation() {
        Population result = new BasicPopulation(POPULATION_SIZE, null);

        BasicSpecies defaultSpecies = new BasicSpecies();
        defaultSpecies.setPopulation(result);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            final DoubleArrayGenome genome = randomGenome();
            genome.setPopulation(result);
            defaultSpecies.getMembers().add(genome);
        }

        result.setGenomeFactory(new DoubleArrayGenomeFactory(SOLUTION_LENGTH));
        result.getSpecies().add(defaultSpecies);

        return result;
    }

    private static DoubleArrayGenome randomGenome() {
        DoubleArrayGenome genome = new DoubleArrayGenome(SOLUTION_LENGTH);
        double[] param = genome.getData();

        for (int i = 0; i < param.length; ++i) {
            param[i] = ThreadLocalRandom.current().nextDouble(1.0);
        }

        return genome;
    }

    public static boolean doOneGame(String[] args, double[] params) {
        RunPoker app = new RunPoker(args, params);
        try {
            return !app.go();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                app.finish();
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
            }
        }

        return false;
    }

    public static boolean doOneGame(String[] args, double[] params1, double[] params2) {
        RunPoker app = new RunPoker(args, params1, params2);
        try {
            return !app.go();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                app.finish();
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
            }
        }

        return false;
    }


    public static void main(String args[]) throws Exception {
        Population population = initPopulation();

        CalculateScore score = new CalculateScore() {
            @Override
            public double calculateScore(MLMethod method) {
                double[] params = ((DoubleArrayGenome) method).getData();

                int numRuns = 50;
                DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
                do {
                    for (int i = 0; i < numRuns; ++i) {
                        if (doOneGame(args, params)) {
                            stats.accept(1.0);
                        } else {
                            stats.accept(0.0);
                        }
                    }
                } while (stats.getAverage() > 0.45 && stats.getCount() < 500);

                return stats.getAverage();

//                double[] params = ((DoubleArrayGenome) method).getData();
//
//                Population pop = ((DoubleArrayGenome) method).getPopulation();
//                DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
//                do {
//                    for (Genome g : pop.flatten()) {
//                        double[] oppParams = ((DoubleArrayGenome) g).getData();
//                        if (doOneGame(args, params, oppParams)) {
//                            stats.accept(1.0);
//                        } else {
//                            stats.accept(0.0);
//                        }
//                    }
//                } while (stats.getAverage() > 0.45 && stats.getCount() < 500);
//
//                return stats.getAverage();
            }

            @Override
            public boolean shouldMinimize() {
                return false;
            }

            @Override
            public boolean requireSingleThreaded() {
                return true;
            }
        };

        TrainEA genetic = new TrainEA(population, score);
        genetic.addOperation(0.30, new Splice(SOLUTION_LENGTH / 2));
        genetic.addOperation(0.70, new MutatePerturb(0.20));

        int iteration = 1;
        StringBuilder builder = new StringBuilder();
        while (iteration < MAX_ITERATIONS) {
            genetic.iteration();

            builder.setLength(0);
            builder.append("Iteration: ");
            builder.append(iteration++);
            System.out.println(builder.toString());

            List<Genome> genomes = genetic.getPopulation().flatten().stream()
                    .filter(g -> g.getScore() > 0)
                    .sorted((s1, s2) -> -Double.compare(s1.getScore(), s2.getScore()))
                    .limit(10)
                    .collect(Collectors.toList());

            for (Genome solution : genomes) {
                System.out.println(solution.getScore() + ": " + Arrays.toString(((DoubleArrayGenome) solution).getData()));
            }

            // clear the scores so they have to re-compute them
            for (Genome g : genetic.getPopulation().flatten()) {
                if (g.getBirthGeneration() + 2 < genetic.getIteration()) {
                    g.setScore(Double.NaN);
                    g.setAdjustedScore(Double.NaN);
                }
            }
        }


        List<Genome> genomes = genetic.getPopulation().flatten().stream()
                .sorted((s1, s2) -> -Double.compare(s1.getScore(), s2.getScore()))
                .limit(10)
                .collect(Collectors.toList());

        System.out.println("Final Numbers: ");
        for (Genome solution : genomes) {
            System.out.println(solution.getScore() + ": " + Arrays.toString(((DoubleArrayGenome) solution).getData()));
        }


        System.exit(0);
    }

    //*/
    public RunPoker(String[] args, double[] bot1Params, double[] bot2Params) {
        startingStack = Integer.valueOf(args[0]);
        this.bot1Command = args[1];
        this.bot2Command = args[2];

        this.playerName1 = "player1";
        this.playerName2 = "player2";

        this.bot1Params = bot1Params.clone();
        this.bot2Params = bot2Params.clone();

        bots = new ArrayList<>();
    }

    public RunPoker(String[] args, double[] bot2Params) {
        startingStack = Integer.valueOf(args[0]);
        this.bot1Command = args[1];
        this.bot2Command = args[2];

        this.playerName1 = "player1";
        this.playerName2 = "player2";

        this.bot2Params = bot2Params.clone();

        bots = new ArrayList<>();
    }


    private boolean go() throws IOException, InterruptedException {
        PokerBot player1, player2;
        Robot bot1, bot2;

        //setup the bots
//        bot1 = new InMemRobot(new InMemParser(new JaegerBot("player1")));

        Robot[] bot1Options = new Robot[]{
//                new InMemRobot(new InMemParser(new MonteCarloJaegerBotv11(100))),
//                new InMemRobot(new InMemParser(new RandoJaegerBot())),
//                new InMemRobot(new InMemParser(new HandRolledJaegerBot()))

//                new InMemRobot(new InMemParser(new MonteCarloJaegerBot(100, bot1Params)))
//                new InMemRobot(new InMemParser(new JaegerBotv10(100)))
//                new InMemRobot(new InMemParser(new JaegerBotv10(100, bot1Params)))
                new InMemRobot(new InMemParser(new JaegerBot(100, bot1Params))),
//                new InMemRobot(new InMemParser(new EvilJaegerBot("evil")))
        };
        bot1 = bot1Options[ThreadLocalRandom.current().nextInt(bot1Options.length)];

//        bot2 = new InMemRobot(new InMemParser(new EvilJaegerBot("evil")));
//        bot2 = new InMemRobot(new InMemParser(new JaegerBot("player2")));
        bot2 = new InMemRobot(new InMemParser(new JaegerBot(100, bot2Params)));
//        bot2 = new InMemRobot(new InMemParser(new MonteCarloJaegerBot(100)));
//        bot2 = new InMemRobot(new InMemParser(new MonteCarloJaegerBotv11(1000)));
//        bot2 = new InMemRobot(new InMemParser(new NeuralMonteCarloJaegerBot("neuralMcBot")));

        player1 = new PokerBot(bot1, playerName1);
        player2 = new PokerBot(bot2, playerName2);
        bots.add(player1);
        bots.add(player2);

        //gametype is omaha tournament (with two players):
        gameType = 13;
        //number of hands is unlimited
        numberOfHands = -1;
        //one winner
        placesPaid = 1;

        engine = new MatchPlayer(new ArrayList<>(bots), gameType, startingStack);
        engine.finishSetup(true);
        ArrayList<String> results = engine.run(numberOfHands, 0);
        boolean p1Wins = false;
        for (int i = 0; i < results.size(); i++) {
            String result = results.get(i);

            String[] parts = result.split("\\s+");
            String name = parts[0];
            int position = Integer.valueOf(parts[2]);
            if (name.equals(playerName1) && position == 1) {
                p1Wins = true;
            }

//            System.out.println(result);
            bot1.addToDump("Engine says: \"" + result + "\"\n");
            bot2.addToDump("Engine says: \"" + result + "\"\n");
        }

        finish();
        return p1Wins;
    }

    private void finish() throws InterruptedException {
        for (int i = 0; i < bots.size(); i++) {
            if (!(bots.get(i).getBot() instanceof IORobot)) {
                continue;
            }

            IORobot bot = (IORobot) bots.get(i).getBot();
            try {
                bot.finish();
            } catch (Exception e) {
            }
            Thread.sleep(400);
        }

        // write everything
        try {
            this.saveGame((IORobot) bots.get(0).getBot(), (IORobot) bots.get(1).getBot());
        } catch (Exception e) {
        }
    }

    /**
     * Transforms the old output from the engine to a nice bson format for the javascript visualizer
     *
     * @param input : old output from the engine
     * @return String : new bson format output
     */
    private String getPlayedGame(String input) {
        StringBuilder out = new StringBuilder();
        String[] lines = input.trim().split("\\r?\\n");

        out.append("{");

        for (int i = 0; i < lines.length; i++) {
            String[] part = lines[i].trim().split(" ");
            if (part[0].equals("Settings")) {
                out.append(makeKeyString("settings"));
                out.append("{");
                while (part[0].equals("Settings")) {
                    part = lines[i].trim().split(" ");
                    if (part[1].startsWith("seat")) {
                        out.deleteCharAt(out.length() - 1);
                        out.append("},");
                        out.append(makeKeyString("players"));
                        out.append("[");
                    }
                    while (part[1].startsWith("seat")) {
                        part = lines[i].trim().split(" ");
                        out.append("\"" + part[2] + "\",");
                        i++;
                        part = lines[i].trim().split(" ");
                        if (!part[1].startsWith("seat")) {
                            out.deleteCharAt(out.length() - 1);
                            out.append("],");
                        }
                    }
                    if (!part[1].equals("hand")) {
                        out.append(makeKeyString(part[1]));
                        out.append(part[2] + ",");
                    }
                    i++;
                    part = lines[i].trim().split(" ");
                    if (!part[0].equals("Settings")) {
                        out.append(makeKeyString("rounds"));
                        out.append("[{");
                        i--;
                    }
                }
            } else {
                if (part[1].equals("hand")) {
                    out.append("{");
                    continue;
                }
                if (part[1].equals("end")) {
                    out.append("},");
                    continue;
                }
                if (part[1].equals("dealerButton")) {
                    out.append(makeKeyString("dealerButton"));
                    out.append("\"" + part[2] + "\",");
                    continue;
                }
                if (part[1].equals("table")) {
                    out.append(makeKeyString("table"));
                    out.append("\"" + trimStuff(part[2]) + "\",");
                    continue;
                }
                if (part[0].equals("Result")) {
                    String potName = part[1].substring(0, 1).toUpperCase() + part[1].substring(1);
                    out.append(makeKeyString("result" + potName));
                    out.append("{");
                    String[] winners = trimStuff(part[2]).split(",");
                    String[] winner = winners[0].split(":");
                    out.append(makeKeyString(winner[0]));
                    out.append(winner[1]);
                    if (winners.length > 1) {
                        out.append(",");
                        winner = winners[1].split(":");
                        out.append(makeKeyString(winner[0]));
                        out.append(winner[1]);
                    }
                    out.append("}");
                    if (potName.startsWith("Sidepot"))
                        out.append(",");
                    continue;
                }
                if (part[0].startsWith("player") || (part[0].equals("Match") && (part[1].contains("pot") || part[1].equals("table")))) {
                    out.append(makeKeyString("actions"));
                    out.append("[");
                    while (part[0].startsWith("player") || (part[0].equals("Match") && (part[1].contains("pot") || part[1].equals("table")))) {
                        part = lines[i].trim().split(" ");
                        out.append("\"");
                        for (String s : part) {
                            out.append(trimStuff(s) + " ");
                        }
                        out.deleteCharAt(out.length() - 1);
                        out.append("\",");
                        i++;
                        part = lines[i].trim().split(" ");
                        if (!(part[0].startsWith("player") || (part[0].equals("Match") && (part[1].contains("pot") || part[1].equals("table"))))) {
                            out.deleteCharAt(out.length() - 1);
                            out.append("],");
                            i--;
                        }
                    }
                }
            }
        }

        out.deleteCharAt(out.length() - 1);
        out.append("]}");

        return out.toString();
    }

    private String makeKeyString(String input) {
        return "\"" + input + "\": ";
    }

    private String trimStuff(String input) {
        if (input.startsWith("[") && input.endsWith("]"))
            return input.substring(1, input.length() - 1);
        if (input.endsWith("%"))
            return input.substring(0, input.length() - 1);
        return input;
    }

    public void saveGame(IORobot bot1, IORobot bot2) throws Exception {

        PokerBot winner = this.engine.winningPokerBot();
        int score = this.engine.getHandNumber();

        System.out.println("Winner: " + winner.getName());
        System.out.println("Score: " + score);

//        System.out.println("Visualization:");
        // System.out.println(getPlayedGame(this.engine.getHistory())); // output for visuals

        System.out.println("Bot 1 Errors: ");
        System.out.println(bot1.getStderr());

//        System.out.println("Bot 1 Dump: ");
//        System.out.println(bot1.getDump());

        System.out.println("Bot 2 Errors: ");
        System.out.println(bot2.getStderr());

//        System.out.println("Bot 2 Dump: ");
//        System.out.println(bot2.getDump());
    }

}