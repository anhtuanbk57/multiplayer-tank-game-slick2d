package com.tuanna.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bullet extends DynamicObject {

    private static float DEFAULT_VELOCITY = 1.0f;
    private static float DEFAULT_RANGE = 1000;

    private float range_ = DEFAULT_RANGE;
    private float originX_;
    private float originY_;
    // Id of the object that generate this bullet
    private int ownerId_;

    public Bullet(Image image, float centerX, float centerY, float rotation, int ownerId) throws SlickException {
        super(image, centerX, centerY);
        velocity_ = DEFAULT_VELOCITY;
        rotation_ = rotation;
        ownerId_ = ownerId;
        originX_ = centerX;
        originY_ = centerY;
    }

    @Override
    public void setFriction(float friction) {
        // Always ignore friction for bullet
    }

    @Override
    public void update(int deltaT) {
        super.update(deltaT);
        double traverseDistance = Math.sqrt(getPower2(getCenterX() - originX_) + getPower2(getCenterY() + originY_));
        if (traverseDistance > range_) {
            destroy();
        }
    }

    private float getPower2(float x) {
        return x * x;
    }

    public void setRange(float range) {
        range_ = range;
    }

    public int getOwnerId() {
        return ownerId_;
    }
}
