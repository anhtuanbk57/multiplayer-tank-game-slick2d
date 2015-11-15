package com.tuanna.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.Transform;

public abstract class DynamicObject extends Polygon {

    protected float friction_;
    protected float velocity_;
    protected float rotation_;

    private boolean isBoundDrawEnable_;
    private boolean isDestroyed_;
    protected long liveTime_;

    private Image image_;

    public DynamicObject(String imagePath, float centerX, float centerY) throws SlickException {
        this(new Image(imagePath), centerX, centerY);
    }

    public DynamicObject(Image image, float centerX, float centerY) throws SlickException {
        image_ = image;

        int halfWidth = image_.getWidth() / 2;
        int halfHeight = image_.getHeight() / 2;
        addPoint(centerX - halfWidth, centerY - halfHeight);
        addPoint(centerX + halfWidth, centerY - halfHeight);
        addPoint(centerX + halfWidth, centerY + halfHeight);
        addPoint(centerX - halfWidth, centerY + halfHeight);
    }

    public void setBoundDrawEnable(boolean isEnable) {
        isBoundDrawEnable_ = isEnable;
    }

    /**
     * @param deltaT Milliseconds since last update
     */
    public void update(int deltaT) {
        if (isDestroyed_) {
            return;
        }

        liveTime_ += deltaT;
        applyFrictionToVelocity(deltaT);

        double radians = Math.toRadians(rotation_);
        float incrementX = (float) (velocity_ * deltaT * Math.cos(radians));
        // Since the Y coordinate in Slick increase when going from top to bottom,
        // we must negate the value calculated from rotation
        float incrementY = (float) - (velocity_ * deltaT * Math.sin(radians));
        setCenterX(getCenterX() + incrementX);
        setCenterY(getCenterY() + incrementY);
    }

    private void applyFrictionToVelocity(int deltaT) {
        if (velocity_ > 0) {
            velocity_ -= friction_ * deltaT;
            velocity_ = velocity_ > 0 ? velocity_ : 0;
        } else {
            velocity_ += friction_ * deltaT;
            velocity_ = velocity_ < 0 ? velocity_ : 0;
        }
    }

    public void draw() {
        draw(getCenterX(), getCenterY());
    }

    public void draw(float xPos, float yPos) {
        if (isDestroyed_) {
            return;
        }

        image_.setRotation(-rotation_);
        image_.drawCentered(xPos, yPos);

        if (isBoundDrawEnable_) {
            Shape bound = transform(new Transform(
                    Transform.createRotateTransform((float) Math.toRadians(-rotation_), xPos, yPos),
                    Transform.createTranslateTransform(xPos - getCenterX(), yPos - getCenterY())
            ));
            ShapeRenderer.draw(bound);
        }
    }

    public float getFriction() {
        return friction_;
    }

    public void setFriction(float friction) {
        friction_ = friction;
    }

    public boolean isDestroyed() {
        return isDestroyed_;
    }

    public void destroy() {
        isDestroyed_ = true;
    }

    public float getRotation() {
        return rotation_;
    }

    public void setVelocity(float velocity) {
        velocity_ = velocity;
    }

    public void setRotation(float rotation) {
        rotation_ = rotation;
    }

    public float getVelocity() {
        return velocity_;
    }
}
