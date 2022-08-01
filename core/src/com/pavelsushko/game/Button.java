package com.pavelsushko.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class Button implements Disposable {
    public final Texture texture;
    public Texture glow;
    public final int type;

    public Button(String texturePath, int type) {
        texture = new Texture(texturePath);
        this.type = type;
        assignGlow();
    }

    private void assignGlow() {
        switch (type) {
            case 1:
                glow = new Texture("buttons\\btn_start_glow.png");
                break;
            case 2:
                glow = new Texture("buttons\\btn_records_glow.png");
                break;
            case 3:
                glow = new Texture("buttons\\btn_exit_glow.png");
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        glow.dispose();
    }
}
