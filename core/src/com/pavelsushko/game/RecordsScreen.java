package com.pavelsushko.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import static com.pavelsushko.game.Arkanoid.*;

public class RecordsScreen implements Screen {
    private final Arkanoid game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final Sound click;
    private final BitmapFont font;
    private final FileHandle file;
    private final OrderedMap<Integer, String> record;
    private final float SCALE_TEXT = 1.8f;

    public RecordsScreen(final Arkanoid game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        click = Gdx.audio.newSound(Gdx.files.internal("sounds\\menu_click.ogg"));
        click.play(0.5f);
        //game.music.play();
        background = new Texture("records_background.png");
        font = new BitmapFont();
        record = new OrderedMap<>();
        file = Gdx.files.local("Records");
        readRecords();
    }

    public RecordsScreen(final Arkanoid game, String playerName, int time) {
        this.game = game;
        camera = new OrthographicCamera();
        click = Gdx.audio.newSound(Gdx.files.internal("sounds\\menu_click.ogg"));
        //game.music.play();
        background = new Texture("records_background.png");
        file = Gdx.files.local("Records");
        writeNewRecord(playerName, time);
        font = new BitmapFont();
        record = new OrderedMap<>();
        readRecords();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.setToOrtho(false, WIDTH / SCALE_TEXT, HEIGHT / SCALE_TEXT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, WIDTH / SCALE_TEXT, HEIGHT / SCALE_TEXT);
        if (record.isEmpty())
            font.draw(game.batch, "NO WINNERS YET", WIDTH / SCALE_TEXT / 2 - 65, HEIGHT / SCALE_TEXT / 2);
        else
            drawTable(game.batch);
        game.batch.end();
        if (Gdx.input.justTouched()) {
            game.setScreen(new MenuScreen(game));
        }
    }

    public void writeNewRecord(String name, int time) {
        if (!name.isEmpty()) {
            file.writeString(time + "\n", true);
            file.writeString(name + "\n", true);
        }
    }

    public void readRecords() {
        if (file.exists()) {
            String[] records = file.readString().split("\n");
            for (int i = 0; i < records.length - 1; i += 2) {
                int time = Integer.parseInt(records[i]);
                String name = records[i + 1];
                record.put(time, name);
            }
            record.orderedKeys().sort();
        }
    }

    private void drawTable(SpriteBatch batch) {
        int number = 0;
        ObjectMap.Entries<Integer, String> iterator = record.iterator();
        while (iterator.hasNext()) {
            ObjectMap.Entry<Integer, String> entry = iterator.next();
            font.draw(batch, String.format("%-40.20s%03d", entry.value, entry.key), WIDTH / SCALE_TEXT / 8, HEIGHT / SCALE_TEXT / 1.5f - 20 * number);
            number++;
        }
        iterator.reset();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        background.dispose();
        font.dispose();
        click.dispose();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
