package com.pavelsushko.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

public class Arkanoid extends Game {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final String TITLE = "Arkamiod";
    public static final float SCALE = 60f;
    public Music music;
    public SpriteBatch batch;
    public static Random generator = new Random();

    public static Map<String, Texture> filesTexture = new HashMap<String,Texture>();

    public static Levels levels = new Levels(100);

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));

        // loadings asset
        // extract !
        String [] filesIcons= Gdx.files.internal("files.txt").readString("UTF-8").split("\n");
        Gdx.app.log("INFO", "files : "+filesIcons.length);
        for(String filePath: filesIcons) {
            Gdx.app.log("Loading", filePath+" "+Gdx.files.internal(filePath).exists());
            filesTexture.put(filePath.split("/")[1], new Texture(filePath));

        }

        // loadings level names
        String [] levelsNames= Gdx.files.internal("levels.txt").readString("UTF-8").split("\n");
        String [] levelsSolutions= Gdx.files.internal("levels_solutions.txt").readString("UTF-8").split("\n");

        // creating levels
        for ( int level = 0 ; level < levelsNames.length; level++) {
            levels.add(new Level(level, levelsNames[level], levelsSolutions[level]));
        }

        batch = new SpriteBatch();
        //music.setLooping(true);
        //music.setVolume(0.3f);
        //music.play();
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        music.dispose();
    }
}
