package com.pavelsushko.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.pavelsushko.game.Arkanoid.*;

public class GameScreen extends InputAdapter implements Screen, Input.TextInputListener {
    private final Arkanoid game;
    private final OrthographicCamera camera;
    private final Texture background, neonLight;
    private final World world;
    private final Wall wall;
    private Ball ball;
    private final Platform platform;
    private final Array.ArrayIterator<Ball> colorBall;
    private final Array.ArrayIterator<Brick> brickIterator;
    private final Array<Brick> bricksToDestroy;
    private final BitmapFont font;
    private final Sound click, win;
    private float timeCount;
    private int worldTime;
    private boolean played;
    private String playerName;

    private int remaingLives = Integer.MAX_VALUE;

    private Box2DDebugRenderer debugRenderer;

    public GameScreen(final Arkanoid game) {
        this.game = game;
        click = Gdx.audio.newSound(Gdx.files.internal("sounds\\menu_click.ogg"));
        win = Gdx.audio.newSound(Gdx.files.internal("sounds\\win.ogg"));
        camera = new OrthographicCamera();
        background = new Texture("background.png");
        neonLight = new Texture("neon_light.png");
        world = new World(new Vector2(0, -0.05f), true);
        wall = new Wall(world, WIDTH, HEIGHT);
        ball = new Ball(world, WIDTH / 2f, HEIGHT / 2f - 80);
        colorBall = new Array.ArrayIterator<>(new Ball(world).createMany(100));
        platform = new Platform(world, WIDTH / 2f, HEIGHT / 8f);
        brickIterator = new Array.ArrayIterator<>(new Brick(world).createMany(10));
        bricksToDestroy = new Array<>(4);
        font = new BitmapFont();
        played = false;

        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.setToOrtho(false, WIDTH / SCALE, HEIGHT / SCALE);
        camera.update();
        debugRenderer.render(world, camera.combined);
        game.batch.setProjectionMatrix(camera.combined);
        update(deltaTime);
        game.batch.begin();
        game.batch.draw(background, 0, 0, WIDTH / SCALE, HEIGHT / SCALE);
        ball.draw(game.batch);
        platform.draw(game.batch);
        while (brickIterator.hasNext()) {
            Brick current = brickIterator.next();
            if (!current.toDestroy) {
                current.draw(game.batch);
            } else {
                bricksToDestroy.add(current);
                brickIterator.remove();
            }
        }
        brickIterator.reset();
        if (!brickIterator.hasNext()) {
            wall.body.setActive(false);
            platform.body.setActive(false);
            drawWinScene(game.batch);
        }
        game.batch.draw(neonLight, 0, 0, WIDTH / SCALE, HEIGHT / SCALE);
        camera.setToOrtho(false, WIDTH, HEIGHT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        font.draw(game.batch, String.format("TIME: %03d", worldTime), Wall.THICKNESS - 10, HEIGHT - Wall.THICKNESS / 3);
        game.batch.end();

        world.step(deltaTime, 8, 4);
        bricksToDestroy.forEach(brick -> world.destroyBody(brick.body));
        bricksToDestroy.clear();
        if (gameOver()) {
            played = true;
            game.setScreen(new MenuScreen(game));
        }
        if (!brickIterator.hasNext() && !played) {
            game.music.stop();
            win.play(1);
            played = true;
            ball.body.setActive(false);
            Gdx.input.setInputProcessor(null);
            Gdx.input.getTextInput(this, "YOU WON!", "", "ENTER YOUR NAME");
        }
        if (playerName != null) {
            game.music.stop();
            game.setScreen(new RecordsScreen(game, playerName, worldTime));
        }
    }

    private void drawWinScene(SpriteBatch batch) {
        while (colorBall.hasNext()) {
            Ball current = colorBall.next();
            current.body.setActive(true);
            current.draw(batch);
        }
        colorBall.reset();
    }

    @Override
    public void input(String text) {
        playerName = text;
    }

    @Override
    public void canceled() {
        playerName = "";
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if ((WIDTH - Wall.THICKNESS) / SCALE - Platform.WIDTH >= screenX / SCALE && Platform.WIDTH + Wall.THICKNESS / SCALE <= screenX / SCALE) {
            platform.body.setTransform((float) screenX / SCALE, (HEIGHT / 8f) / SCALE, 0);
        }
        return false;
    }

    private void update(float deltaTime) {
        if (!played) {
            timeCount += deltaTime;
            if (timeCount >= 1) {
                worldTime++;
                timeCount = 0;
            }
        }
    }

    private boolean gameOver() {
        if (isCurrentBallLost()) {
            decrementLives();
            if (remaingLives < 0) {
                return true;
            } else {
                resetBallAtStart();
            }
        }
        return false;
    }

    private void resetBallAtStart() {
        System.out.println("reseting to "+(WIDTH / 2)+", "+(HEIGHT / 2f - 80));
//        ball =  new Ball(world, WIDTH / 2f, HEIGHT / 2f - 80);
        ball.body.setTransform((WIDTH / 2) / Arkanoid.SCALE, (HEIGHT / 2f - 80) / Arkanoid.SCALE, ball.body.getAngle());
        ball.body.setLinearVelocity(0, 10f); // will launch vertical

    }

    private void decrementLives() {
        remaingLives--;
        // @Todo display
    }

        private boolean isCurrentBallLost() {
        return ball.body.getPosition().y < (platform.body.getPosition().y - Platform.HEIGHT * SCALE);
    }

    @Override
    public void show() {
        click.play(0.5f);
        Gdx.input.setInputProcessor(this);
        world.setContactListener(new WorldContactListener());

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        dispose();
    }

    @Override
    public void dispose() {
        click.dispose();
        world.dispose();
        ball.dispose();
        colorBall.forEach(Ball::dispose);
        platform.dispose();
        brickIterator.forEach(Brick::dispose);
        background.dispose();
        neonLight.dispose();
        font.dispose();
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
