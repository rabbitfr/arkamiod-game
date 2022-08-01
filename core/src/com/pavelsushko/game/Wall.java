package com.pavelsushko.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Wall {

    public final static float THICKNESS = 40;
    public final Body body;

    public Wall(World world, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        ChainShape chain = new ChainShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(THICKNESS / Arkanoid.SCALE, 0);
        vertices[1] = new Vector2(THICKNESS / Arkanoid.SCALE, (height - THICKNESS) / Arkanoid.SCALE);
        vertices[2] = new Vector2((width - THICKNESS) / Arkanoid.SCALE, (height - THICKNESS) / Arkanoid.SCALE);
        vertices[3] = new Vector2((width - THICKNESS) / Arkanoid.SCALE, 0);
        chain.createChain(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chain;
        fixtureDef.friction = 0.5f;
        body.createFixture(fixtureDef).setUserData(this);

        chain.dispose();
    }

}