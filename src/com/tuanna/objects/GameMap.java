package com.tuanna.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;

public class GameMap {

    private Tank playerTank_;
    private Image image_;
    private ArrayList<DynamicObject> gameObjects = new ArrayList<>();
    private ArrayList<Tank> enemyTanks_ = new ArrayList<>();

    private float screenWidth_;
    private float screenHeight_;
    private float halfWidth_;
    private float halfHeight_;
    private float rightBoundOffset_;
    private float bottomBoundOffset_;

    public GameMap(String imagePath, float screenWidth, float screenHeight) throws SlickException {
        image_ = new Image(imagePath);
        screenWidth_ = screenWidth;
        screenHeight_ = screenHeight;
        halfWidth_ = screenWidth / 2;
        halfHeight_ = screenHeight / 2;
        rightBoundOffset_ = screenWidth - image_.getWidth();
        bottomBoundOffset_ = screenHeight - image_.getHeight();
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
            object.update(deltaT);
        }
    }

    public void draw() {
        // Track moves in opposite direction to car
        float offsetX = -playerTank_.getCenterX() + halfWidth_;
        float shiftX = 0;
        // Check map bounds
        if (offsetX > 0) {
            shiftX = offsetX;
            offsetX = 0;
        }
        if (offsetX < rightBoundOffset_) {
            shiftX = offsetX - rightBoundOffset_;
            offsetX = rightBoundOffset_;
        }
        float offsetY = -playerTank_.getCenterY() + halfHeight_;
        float shiftY = 0;
        if (offsetY > 0) {
            shiftY = offsetY;
            offsetY = 0;
        }
        if (offsetY < bottomBoundOffset_) {
            shiftY = offsetY - bottomBoundOffset_;
            offsetY = bottomBoundOffset_;
        }
        image_.draw(offsetX, offsetY);

        float tankX = screenWidth_ / 2 - shiftX;
        tankX = tankX > 0 ? tankX : 0;
        tankX = tankX < screenWidth_ ? tankX : screenWidth_;
        float tankY = screenHeight_ / 2 - shiftY;
        tankY = tankY > 0 ? tankY : 0;
        tankY = tankY < screenHeight_ ? tankY : screenHeight_;
        playerTank_.draw(tankX, tankY);

        drawOtherObjects(offsetX, offsetY);
    }

    private void drawOtherObjects(float offsetX, float offsetY) {
        for (DynamicObject object : gameObjects) {
            object.draw(object.getCenterX() + offsetX, object.getCenterY() + offsetY);
        }
        for (Tank tank : enemyTanks_) {
            tank.draw(tank.getCenterX() + offsetX, tank.getCenterY() + offsetY);
        }
    }
}
