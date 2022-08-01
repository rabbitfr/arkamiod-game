package com.pavelsushko.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ball implements Disposable {

    public Body body;
    public float RADIUS = 0.20f;
    private Texture texture;
    private Array<String> path;
    private final World world;
    private final Sound hit = Gdx.audio.newSound(Gdx.files.internal("sounds\\touch_glass_1.ogg"));
    private final Sound destroy = Gdx.audio.newSound(Gdx.files.internal("sounds\\breaking_ice.ogg"));

    public Ball(World world, float x, float y) {
        this.world = world;
        create(x, y);
        texture = new Texture("red_ball.png");
    }

    public Ball(World world){
        this.world = world;
        path = new Array<>();
        path.add("red_ball.png");
        path.add("indigo_ball.png");
        path.add("blue_ball.png");
        path.add("yellow_ball.png");
        path.add("green_ball.png");
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x - RADIUS, body.getPosition().y - RADIUS, RADIUS * 2, RADIUS * 2);
    }

    public void setTexture() {
        //texture.dispose();
        texture = new Texture(path.get(MathUtils.random(0, 4)));
    }

    public void playHitSound(float volume) {
        float pitch = MathUtils.random(0.5f, 2);
        hit.play(volume, pitch, 0);
    }

    public void playDestroySound() {
        float pitch = MathUtils.random(0.5f, 2);
        destroy.play(0.5f, pitch, 0);
    }

    private void create(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / Arkanoid.SCALE, y / Arkanoid.SCALE);
        bodyDef.fixedRotation = false;
        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;
        fixtureDef.density = 1f;
        body.setLinearVelocity(0, -10f);
        body.createFixture(fixtureDef).setUserData(this);

        circle.dispose();
    }

    public Array<Ball> createMany(int amount) {
        Array<Ball> balls = new Array<>();
        for (int i = 0; i < amount; i++) {
            balls.add(new Ball(world));
            Ball current = balls.get(i);
            current.create(0, 0);
            current.setTexture();
            current.RADIUS = 1f;
            current.body.setActive(false);
            current.body.setLinearVelocity(MathUtils.random()*5, MathUtils.random()*5);
        }
        return balls;
    }

    @Override
    public void dispose() {
        texture.dispose();
        hit.dispose();
        destroy.dispose();
    }
}
