package com.pavelsushko.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Arkanoid extends Game {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final String TITLE = "Arkanoid";
    public static final float SCALE = 60f;
    public Music music;
    public SpriteBatch batch;

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
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
