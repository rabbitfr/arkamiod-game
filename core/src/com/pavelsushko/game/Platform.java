package com.pavelsushko.game;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Platform implements Disposable {

    public final Body body;
    public final static float WIDTH = 1.2f;
    public final static float HEIGHT = 0.1f;
    private final Texture texture;

    public Platform(World world, float x, float y) {
        texture = new Texture("platform.png");
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x / Arkanoid.SCALE, y / Arkanoid.SCALE);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(WIDTH, HEIGHT);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.friction = 0.7f;
        body.createFixture(fixtureDef).setUserData(this);

        rectangle.dispose();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x - WIDTH / 2 * 2.6f, body.getPosition().y - HEIGHT / 2 * 6f, WIDTH * 2.6f, HEIGHT * 7);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
