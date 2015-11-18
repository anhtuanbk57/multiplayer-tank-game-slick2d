package com.tuanna.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

public class GameMap {

    private Tank playerTank_;
    private Image image_;
    private ArrayList<DynamicObject> gameObjects = new ArrayList<>();
    private ArrayList<Tank> enemyTanks_ = new ArrayList<>();
    private Rectangle battleGround_;

    private float screenWidth_;
    private float screenHeight_;
    private float halfWidth_;
    private float halfHeight_;

    public GameMap(String imagePath, float screenWidth, float screenHeight) throws SlickException {
        image_ = new Image(imagePath);
        screenWidth_ = screenWidth;
        screenHeight_ = screenHeight;
        halfWidth_ = screenWidth / 2;
        halfHeight_ = screenHeight / 2;

        // Hard code according to map file.
        battleGround_ = new Rectangle(373, 365, 2540, 2264);
    }

    public void setPlayerTank(Tank playerTank) {
        playerTank_ = playerTank;
    }

    public void addEnemyTank(Tank enemyTank) {
        if (enemyTank == null) {
            return;
        }
        enemyTanks_.add(enemyTank);
    }

    public void addObject(DynamicObject object) {
        if (object == null) {
            return;
        }
        gameObjects.add(object);
    }

    public void updateObjectsState(int deltaT) {
        playerTank_.update(deltaT);
        for (DynamicObject object : gameObjects) {
            if (object.isDestroyed()) {
                continue;
            }
            object.update(deltaT);
            if (object instanceof Bullet) {
                boolean bulletFromEnemy = ((Bullet) object).getOwnerId() != playerTank_.getId();
                boolean bulletHitPlayer = playerTank_.contains(object) || playerTank_.intersects(object);
                if (bulletFromEnemy && bulletHitPlayer) {
                    playerTank_.takeDamage();
                    // Destroy bullet after it hit the tank
                    object.destroy();
                }
            }
        }
        for (Tank tank : enemyTanks_) {
            tank.update(deltaT);
        }
        // If player tank go outside play area, destroy it
        if (!playerTank_.isDestroyed() && !battleGround_.contains(playerTank_)) {
            playerTank_.destroy();
        }
    }

    public void draw() {
        // Track moves in opposite direction to car
        float offsetX = -playerTank_.getCenterX() + halfWidth_;
        float offsetY = -playerTank_.getCenterY() + halfHeight_;
        image_.draw(offsetX, offsetY);
        playerTank_.draw(halfWidth_, halfHeight_);

        drawOtherObjects(offsetX, offsetY);
    }

    private void drawOtherObjects(float offsetX, float offsetY) {
        // TODO: Schedule for deleting destroyed objects after 5 - 10 seconds.
        for (DynamicObject object : gameObjects) {
            object.draw(object.getCenterX() + offsetX, object.getCenterY() + offsetY);
        }
        for (Tank tank : enemyTanks_) {
            tank.draw(tank.getCenterX() + offsetX, tank.getCenterY() + offsetY);
        }
    }
}
