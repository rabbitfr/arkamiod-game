package com.pavelsushko.game.desktop;

import com.badlogic.gdx.Files;
import com.pavelsushko.game.Arkanoid;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.foregroundFPS = 60;
        cfg.width = Arkanoid.WIDTH;
        cfg.height = Arkanoid.HEIGHT;
        cfg.resizable = false;
        cfg.title = Arkanoid.TITLE;
        cfg.addIcon("icon.png", Files.FileType.Internal);
        new LwjglApplication(new Arkanoid(), cfg);
    }
}