package com.pavelsushko.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import static com.pavelsushko.game.Arkanoid.*;

public class MenuScreen extends InputAdapter implements Screen {
    private final Arkanoid game;
    private final int BUTTON_WIDTH;
    private final int BUTTON_HEIGHT;
    private final OrthographicCamera camera;
    private final Texture background;
    private final Button[] button;
    private int mouseX;
    private int mouseY;

    public MenuScreen(final Arkanoid game) {
        this.game = game;
        camera = new OrthographicCamera();
        //game.music.play();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        background = new Texture("menu_background.png");
        button = new Button[3];
        button[0] = new Button("buttons\\btn_start.png", 1);
        button[1] = new Button("buttons\\btn_records.png", 2);
        button[2] = new Button("buttons\\btn_exit.png", 3);
        BUTTON_WIDTH = button[0].texture.getWidth() / 2;
        BUTTON_HEIGHT = button[0].texture.getHeight() / 2;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        int x = WIDTH / 2 - BUTTON_WIDTH / 2;
        int y = HEIGHT / 2 - BUTTON_HEIGHT / 2;
        int recordY = y - (BUTTON_HEIGHT + 10);
        int exitY = y - (BUTTON_HEIGHT + 10) * 2;
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0,0, WIDTH, HEIGHT);
        processButton(button[0], x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT);
        processButton(button[1], x, recordY, x + BUTTON_WIDTH, recordY + BUTTON_HEIGHT);
        processButton(button[2], x, exitY, x + BUTTON_WIDTH, exitY + BUTTON_HEIGHT);
        game.batch.end();
    }

    private void processButton(Button button, int leftX, int bottomY, int rightX, int topY) {
        if ((mouseX >= leftX) && (mouseX <= rightX) && (mouseY >= bottomY) && (mouseY <= topY)) {
            game.batch.draw(button.glow, leftX, bottomY, BUTTON_WIDTH, BUTTON_HEIGHT);
            if (Gdx.input.justTouched()) {
                switch (button.type) {
                    case 1:
                        game.setScreen(new GameScreen(game));
                        break;
                    case 2:
                        game.setScreen(new RecordsScreen(game));
                        break;
                    case 3:
                        Gdx.app.exit();
                }
            }
        } else {
            game.batch.draw(button.texture, leftX, bottomY, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = -screenY + HEIGHT;
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        for (Button btn : button) {
            btn.dispose();
        }
        background.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

}
