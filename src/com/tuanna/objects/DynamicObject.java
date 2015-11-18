package com.tuanna.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.*;

public abstract class DynamicObject extends Polygon {

    protected float friction_;
    protected float velocity_;
    protected float rotation_;

    private boolean isBoundDrawEnable_;
    private boolean isDestroyed_;
    protected long liveTime_;

    private Image image_;
    private Shape transformedShape_;

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
        float incrementX = calculateIncrementX(deltaT, radians);
        // Since the Y coordinate in Slick increase when going from top to bottom,
        // we must negate the value calculated from rotation
        float incrementY = calculateIncrementY(deltaT, radians);
        setCenterX(getCenterX() + incrementX);
        setCenterY(getCenterY() + incrementY);
    }

    private float calculateIncrementX(int deltaT, double radians) {
        return (float) (velocity_ * deltaT * Math.cos(radians));
    }

    private float calculateIncrementY(int deltaT, double radians) {
        return (float) - (velocity_ * deltaT * Math.sin(radians));
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

    public void draw(float xPos, float yPos) {
        image_.setRotation(-rotation_);
        image_.drawCentered(xPos, yPos);

        transformedShape_ = transform(new Transform(
                Transform.createRotateTransform((float) Math.toRadians(-rotation_), xPos, yPos),
                Transform.createTranslateTransform(xPos - getCenterX(), yPos - getCenterY())
        ));
        if (isBoundDrawEnable_) {
            ShapeRenderer.draw(transformedShape_);
        }
    }

    /**
     * Check if this object collide with another object in the current moving direction.
     * @param origin A Shape object to be checked with.
     */
    public boolean isCollideWith(Shape origin) {
        Shape object = origin;
        if (origin instanceof DynamicObject) {
            object = ((DynamicObject) origin).getTransformedShape();
        }
        boolean isCollide = transformedShape_.intersects(object) && isMovingToward(object);
        if (isCollide) {
            velocity_ = 0;
        }
        return isCollide;
    }

    public boolean isMovingToward(Shape object) {
        // Estimate new position with fake data
        double radians = Math.toRadians(rotation_);
        float newX = getCenterX() + calculateIncrementX(100, radians);
        float newY = getCenterY() + calculateIncrementY(100, radians);

        Vector2f newPos = new Vector2f(newX, newY);
        Vector2f currentPos = new Vector2f(getCenterX(), getCenterY());
        Vector2f objectPos = new Vector2f(object.getCenterX(), object.getCenterY());
        // Use distanceSquared to avoid additional sqrt, mean better performance
        return newPos.distanceSquared(objectPos) < currentPos.distanceSquared(objectPos);
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

    public void setFriction(float friction) {
        friction_ = friction;
    }

    public Shape getTransformedShape() {
        return transformedShape_;
    }
}
