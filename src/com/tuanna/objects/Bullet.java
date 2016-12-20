package com.tuanna.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bullet extends DynamicObject {

    private static final float DEFAULT_VELOCITY = 1.0f;
    private static final float MAX_LIVE_TIME = 1500;

    // Id of the object that generate this bullet
    private int ownerId_;

    public Bullet(Image image, float centerX, float centerY, float rotation, int ownerId) throws SlickException {
        super(image, centerX, centerY);
        velocity_ = DEFAULT_VELOCITY;
        rotation_ = rotation;
        ownerId_ = ownerId;
    }

    @Override
    public void setFriction(float friction) {
        // Always ignore friction for bullet
    }

    @Override
    public void update(int deltaT) {
        super.update(deltaT);
        if (liveTime_ > MAX_LIVE_TIME) {
            destroy();
        }
    }

    @Override
    public void draw(float xPos, float yPos) {
        // Skip drawing if bullet was destroy
        if (isDestroyed()) {
            return;
        }
        super.draw(xPos, yPos);
    }

    public int getOwnerId() {
        return ownerId_;
    }
}
