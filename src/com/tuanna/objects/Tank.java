package com.tuanna.objects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

public class Tank extends DynamicObject {

    private static final float DEFAULT_HANDLING = 4;
    private static final float MAX_VELOCITY = 0.3f;
    private static final float DEFAULT_ACCELERATION = 0.015f;
    private static final float BRAKE_MODIFIER = 0.03f;
    private static final long RELOAD_INTERVAL = 2000;  // 2 sec
    private static final int MAX_HITS = 2;

    private ParticleSystem smokeParticle_;
    private ParticleSystem fireParticle_;
    private Animation exploseAnimation_;
    private int id_;
    private long lastShootTime_;
    private boolean isDamaged_;
    private int hitRemaining_ = MAX_HITS;

    private TankStatusListener listener_;

    public Tank(String carImage, float centerX, float centerY) throws SlickException {
        super(carImage, centerX, centerY);

        friction_ = 0.0003f;
        smokeParticle_ = new ParticleSystem(new Image("res/smoke.png"));
        smokeParticle_.addEmitter(new FireEmitter(0, 0, 10));
        fireParticle_ = new ParticleSystem(new Image("res/fire.png"));
        fireParticle_.addEmitter(new FireEmitter(0, 0, 20));
        SpriteSheet sheet = new SpriteSheet("res/explosion.png", 94, 91);
        exploseAnimation_ = new Animation(sheet, 200);
        exploseAnimation_.setLooping(false);

        id_ = (int) System.currentTimeMillis();
    }

    public void setTankStatusListener(TankStatusListener listener) {
        listener_ = listener;
    }

    public void accelerate() {
        float newVelocity = velocity_ + DEFAULT_ACCELERATION;
        velocity_ = newVelocity < MAX_VELOCITY ? newVelocity : MAX_VELOCITY;
    }

    public void brake() {
        float newVelocity = velocity_ - BRAKE_MODIFIER;
        velocity_ = newVelocity > -MAX_VELOCITY ? newVelocity : -MAX_VELOCITY;
    }

    public void turnLeft() {
        rotation_ += DEFAULT_HANDLING * (velocity_ / MAX_VELOCITY);
    }

    public void turnRight() {
        rotation_ -= DEFAULT_HANDLING * (velocity_ / MAX_VELOCITY);
    }

    public void takeDamage() {
        isDamaged_ = true;
        if (--hitRemaining_ <= 0) {
            destroy();
            exploseAnimation_.start();
        }
        if (listener_ != null) {
            listener_.onBeingHit(id_, isDestroyed());
        }
    }

    @Override
    public void update(int deltaT) {
        super.update(deltaT);
        if (isDestroyed()) {
            fireParticle_.update(deltaT);
            if (!exploseAnimation_.isStopped()) {
                exploseAnimation_.update(deltaT);
            }
        }
        if (isDamaged_) {
            smokeParticle_.update(deltaT);
        }
    }

    public int getId() {
        return id_;
    }

    public boolean canShoot() throws SlickException {
        long interval = liveTime_ - lastShootTime_;
        if (interval < RELOAD_INTERVAL) {
            // Reload has not been completed
            return false;
        }
        lastShootTime_ = liveTime_;
        return true;
    }

    @Override
    public void draw(float xPos, float yPos) {
        super.draw(xPos, yPos);
        if (isDestroyed()) {
            if (!exploseAnimation_.isStopped()) {
                exploseAnimation_.draw(getMinX(), getMinY());
            } else {
                fireParticle_.render(xPos, yPos);
            }
        }
        if (isDamaged_) {
            smokeParticle_.render(xPos, yPos);
        }
    }

    public interface TankStatusListener {
        void onBeingHit(int tankId, boolean isDestroyed);
    }
}
