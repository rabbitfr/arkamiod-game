package com.pavelsushko.game;

public class Level {

    int level ;
    String name;
    String solution;

    public Level(int level,
                 String levelName,
                 String levelSolution) {
        this.level = level;
        this.name = levelName;
        this.solution = levelSolution;

    }
}
