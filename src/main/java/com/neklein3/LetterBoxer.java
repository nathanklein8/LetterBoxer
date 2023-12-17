package com.neklein3;

import java.util.ArrayList;
import java.util.List;

public class LetterBoxer {

    public static final void usage(String error) {
        if (error != null) {
            System.out.println(error);
        }
        System.out.println("java -jar LB.jar [c1] [c2] [c3] [c4] opt[word list]");
    }

    public static void main(String[] args) {
        ArrayList<String> help = new ArrayList<>(List.of("h", "-h", "help", "-help"));
        if (args.length > 0) {
            if (help.contains(args[0].toLowerCase())) {
                usage(null);
            }
        }
        if (args.length < 4 || args.length > 6) {
            usage("invalid number of arguments");
            return;
        } else {

            for (int i=0; i<4; i++) {
                String side = args[i];
                if (side.length() != 3) {
                    usage("each side should have exactly 3 characterts!");
                    return;
                }
            }

            Solver solver = new Solver();
            System.out.println("Finding legal words...");
            Game game = new Game(args);
            System.out.println("Testing combinations...");
            ArrayList<Game> solutions = solver.solve(game);
            if (solutions.size() == 0) {
                System.out.println("No solutions found :(");
            } else {
                int x = 1;
                System.out.println("Solutions:");
                for (Game g : solutions) {
                    System.out.println("  " + x++ + ": " + g);
                }
                System.out.println("" + solver.getNumConfigs() + " combinations tried");
            }
        }
    }
}