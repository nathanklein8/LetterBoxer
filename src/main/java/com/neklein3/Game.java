package com.neklein3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Game {

    private static ArrayList<Character> legalChars;
    private static TreeMap<Character, ArrayList<String>> legalWords;
    private static char[] v1;
    private static char[] v2;
    private static char[] v3;
    private static char[] v4;
    private ArrayList<String> solution;

    private static TreeMap<Character, ArrayList<String>> usedFirstWords;

    private static String f = "wordlist.txt";
    private static boolean custWords = false;

    public Game(String[] strings) {

        if (strings.length == 5) {
            custWords = true;
            f = strings[4];
        }

        Game.legalChars = new ArrayList<>();
        this.solution = new ArrayList<>();

        Game.usedFirstWords = new TreeMap<>();

        Game.v1 = new char[3];
        Game.v2 = new char[3];
        Game.v3 = new char[3];
        Game.v4 = new char[3];
        for(int i=0; i<3; i++) {
            char c1 = Character.toLowerCase(strings[0].charAt(i));
            char c2 = Character.toLowerCase(strings[1].charAt(i));
            char c3 = Character.toLowerCase(strings[2].charAt(i));
            char c4 = Character.toLowerCase(strings[3].charAt(i));
            v1[i] = c1;
            v2[i] = c2;
            v3[i] = c3;
            v4[i] = c4;
            legalChars.addAll(List.of(c1, c2, c3, c4));
        }

        Game.legalWords = new TreeMap<>();
        for (char c : legalChars) {
            legalWords.put(c, new ArrayList<>());
            usedFirstWords.put(c, new ArrayList<>());
        }

        if (!custWords) {
            parse(f);
        } else {
            System.out.println("using " + f + " as word list");
            File file = new File(f);
            try (Scanner in = new Scanner(file)) {
                while (in.hasNextLine()) {
                    String line = in.nextLine().strip();
                    if (line != "") {
                        String word = line.split(" ")[0].toLowerCase();
                        if (isLegal(word)) {
                            char c = word.charAt(0);
                            if (!legalWords.get(c).contains(word)) {
                                legalWords.get(c).add(word);
                            }
                        }
                    }
                }
            } catch (FileNotFoundException fnfe) {
                System.out.println("Error opening file");
            }
        }

        for (char c : legalChars) {
            Collections.sort(legalWords.get(c), new UniqueCharComparator());
            //System.out.println(legalWords.get(c));
        }

    }

    public void parse(String fileName) {
        // Use ClassLoader from the Game class
        ClassLoader classLoader = Game.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            // Read the file line by line
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (isLegal(line)) {
                        char c = line.charAt(0);
                        legalWords.get(c).add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Game(Game previous, String next) {
        this.solution = new ArrayList<>();
        for (String s : previous.solution) {
            this.solution.add(s);
        }
        if (next != null) {
            this.solution.add(next);
        }
    }

    public Collection<Game> getNeighbors() {
        ArrayList<Game> games = new ArrayList<>();
        int len = solution.size();
        if (len == 0) {
            for (char c : legalChars) {
                if (!hasWord(c)) continue;
                for (String word : legalWords.get(c)) {
                    if (!usedFirstWords.get(c).contains(word)) {
                        games.add(new Game(this, word));
                        usedFirstWords.get(c).add(word);
                        break;
                    }
                }
            }
        } else if (len == 2) {
            char c = this.solution.get(0).charAt(0);
            this.solution.clear();
            for (String word : legalWords.get(c)) {
                if (!usedFirstWords.get(c).contains(word)) {
                    games.add(new Game(this, word));
                    usedFirstWords.get(c).add(word);
                    break;
                }
            }
            games.add(new Game(this, null));
        } else {
            String last = this.solution.get(0);
            char c = last.charAt(last.length()-1);
            if (!hasWord(c)) {
                return games;
            }
            ArrayList<String> words = legalWords.get(c);
            for (String s : words) {
                if (formsSolution(s)) {
                    games.add(new Game(this, s));
                }
            }
        }
        return games;
    }

    private boolean formsSolution(String s) {
        int u = 0;
        for (char c : legalChars) {
            if (s.indexOf(c) != -1 || this.solution.get(0).indexOf(c) != -1) {
                u++;
            }
        }
        return u == 12;
    }

    private boolean hasWord(char c) {
        if (legalWords.keySet().contains(c)) {
            return !Game.legalWords.get(c).isEmpty();
        }
        return false;
    }

    public boolean isLegal(String s) {
        if (s.length() <= 5) {
            return false;
        }
        for (int i = 0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (!legalChars.contains(c)) {
                return false;
            }
        }
        for (int i=0; i<s.length() - 1; i++) {
            char current = s.charAt(i);
            char next = s.charAt(i+1);
            if (getSide(current) == getSide(next)) {
                return false;
            }
        }
        return true;
    }

    private int getSide(char c) {
        for (int i=0; i<3; i++) {
            if (c == v1[i]) {return 1;}
            if (c == v2[i]) {return 2;}
            if (c == v3[i]) {return 3;}
            if (c == v4[i]) {return 4;}
        }
        return 0;
    }

    public boolean isSolution() {
        String s = "";
        for(String word : solution) {
            s += word;
        }
        int numUsed = 0;
        for (char c : legalChars) {
            if (s.indexOf(c) != -1) {
                numUsed++;
            }
        }
        return numUsed == 12;
    }

    @Override
    public String toString() {
        String r = "";
        for (String s : solution) {
            r += s;
            r += " ";
        }
        return r;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Game) {
            Game g = (Game) o;
            int a = this.solution.size();
            int b = g.solution.size();
            if (a != b) {
                return false;
            } else {
                for (int i=0; i<a; i++) {
                    if (!this.solution.get(i).equals(g.solution.get(i))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

}