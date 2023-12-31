package com.neklein3;


import java.util.*;

/**
 * Generic solver class to solve puzzles of different various configs
 *
 * @author Nathan Klein
 */
public class Solver {
    /** Hashmap of Configs to build the predecessor map*/
    HashSet<Game> seen;
    /** LinkedList of configs that represents the queue*/
    LinkedList<Game> queue;
    /** the number of total configs created*/
    private int numConfigs = 1;
    /** the number of unique configs generated*/
    private int uniqueConfigs;

    /**
     * returns the total number of configurations generated by the Solver
     * should only be called after solve() has been called
     * @return int - total number of configurations generated
     */
    public int getNumConfigs() {
        return numConfigs;
    }

    /**
     * returns the number of unique configurations generated by the Solver
     * should only be called after solve() has been called
     * @return int - number of Unique configurations
     */
    public int getUniqueConfigs() {
        return uniqueConfigs;
    }

    /**
     * Constructs a new solver with a new hashmap and linkedlist
     */
    public Solver() {
        this.seen = new HashSet<>();
        this.queue = new LinkedList<>();
    }

    /**
     * finds the shortest path to a configuration that is a valid solution
     * @param configuration starting configuration to solve
     * @return LinkedList of Configurations that represent the shortest path to a valid solution
     */
    public ArrayList<Game> solve(Game configuration) {

        ArrayList<Game> solutions = new ArrayList<>();

        queue.add(configuration);
        Game next = queue.remove(0);
        while (true) {
            if (next.isSolution()) {
                solutions.add(new Game(next, null));
            }
            Collection<Game> neighbors = next.getNeighbors();
            numConfigs += neighbors.size();
            for (Game c : neighbors) {
                if (!seen.contains(c)) {
                    queue.add(c);
                    seen.add(c);
                }
            }
            if (queue.isEmpty()) { // if there is no solution
                uniqueConfigs = seen.size();
                break;
            } else { // there stuff in the queue so there might be a solution
                next = queue.remove(0);
            }
        }

        return solutions;

    }

}
