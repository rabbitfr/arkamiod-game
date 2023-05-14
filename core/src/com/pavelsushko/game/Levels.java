package com.pavelsushko.game;

import java.util.ArrayList;

public class Levels extends ArrayList<Level> {

    private int current = 0 ;

    public Levels(int count) {
        super(count);
    }

    public Level currentLevel() {
        return get(current);
    }

    public Level nextLevel() {
        current++;
        return get(current);
    }

}
