package com.pavelsushko.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Brick implements Disposable {

    public Body body;
    public boolean toDestroy = false;
    private final Texture texture;
    private final float WIDTH = 0.62f;
    private final float HEIGHT = 0.25f;
    private World world;

    public Brick(World world, float x, float y) {
        create(world, x, y);
        texture = new Texture("brick.png");
    }

    public Brick(World world) {
        this.world = world;
        texture = new Texture("brick.png");
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x - WIDTH, body.getPosition().y - HEIGHT, WIDTH * 2, HEIGHT * 2);
    }

    private void create(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / Arkanoid.SCALE, y / Arkanoid.SCALE);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(WIDTH, HEIGHT);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.friction = 0;
        body.createFixture(fixtureDef).setUserData(this);

        rectangle.dispose();
    }

    public Array<Brick> createMany(int amount) {
        Array<Brick> bricks = new Array<>();
        float x;
        float y = Arkanoid.HEIGHT - 30 - Wall.THICKNESS - HEIGHT * Arkanoid.SCALE;
        int xOffset = 0;
        int yOffset = 1;
        for (int i = 0; i < amount; i++) {
            x = xOffset * WIDTH * 2 * Arkanoid.SCALE + WIDTH * Arkanoid.SCALE + 30 + Wall.THICKNESS;
            xOffset++;
            bricks.add(new Brick(world, x, y));
            if (x + WIDTH * Arkanoid.SCALE * 2 > Arkanoid.WIDTH - Wall.THICKNESS) {
                y = Arkanoid.HEIGHT - 30 - Wall.THICKNESS - HEIGHT * Arkanoid.SCALE - yOffset * HEIGHT * 2 * Arkanoid.SCALE;
                xOffset = 0;
                yOffset++;
            }
        }
        return bricks;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}