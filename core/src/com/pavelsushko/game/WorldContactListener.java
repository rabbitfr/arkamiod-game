package com.pavelsushko.game;

import com.badlogic.gdx.physics.box2d.*;
import static com.badlogic.gdx.math.MathUtils.*;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if ((a.getUserData() instanceof Ball || b.getUserData() instanceof Ball) &&
                (a.getUserData() instanceof Brick || b.getUserData() instanceof Brick)) {
            Fixture ball = a.getUserData() instanceof Ball ? a : b;
            Fixture brick = b.getUserData() instanceof Brick ? b : a;
            ((Brick) brick.getUserData()).toDestroy = true;
            ((Ball) ball.getUserData()).playDestroySound();
            float angle = random(PI / 6, 5 * PI / 6);
            ball.getBody().setLinearVelocity(11 * cos(angle), 11 * sin(angle));
        }
        if ((a.getUserData() instanceof Ball || b.getUserData() instanceof Ball) &&
                (a.getUserData() instanceof Platform || b.getUserData() instanceof Platform)) {
            Fixture ball = a.getUserData() instanceof Ball ? a : b;
            Fixture platform = b.getUserData() instanceof Platform ? b : a;
            ((Ball) ball.getUserData()).playHitSound(1);
            float angle = getAngle(ball.getBody(), platform.getBody());
            ball.getBody().setLinearVelocity(10 * cos(angle), 10 * sin(angle));
        }
        if ((a.getUserData() instanceof Ball || b.getUserData() instanceof Ball) &&
                (a.getUserData() instanceof Wall || b.getUserData() instanceof Wall)) {
            Fixture ball = a.getUserData() instanceof Ball ? a : b;
            ((Ball) ball.getUserData()).playHitSound(0.4f);
        }
    }

    private float getAngle(Body ball, Body platform) {
        float ballX = ball.getPosition().x;
        float platformX = platform.getPosition().x;
        return platformX - ballX + PI / 2;
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
