package com.tuanna.states;

import com.tuanna.main.Constants;
import com.tuanna.network.NetworkHelper;
import com.tuanna.objects.Bullet;
import com.tuanna.objects.GameMap;
import com.tuanna.objects.Tank;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;


public class PlayState extends BasicGameState implements NetworkHelper.NetworkMessageListener, Tank.TankStatusListener {

    private int stateId_;

    private GameMap map_;
    private Tank playerTank_;
    private Tank enemyTank_;
    private NetworkHelper networkHelper_;
    private Image bulletImage_;
    private Image victoryImage_;
    private Image defeatImage_;

    private boolean isWin_;
    private boolean isLost_;

    // Temp
    float x, y, rotation, velocity;

    public PlayState(int stateId) {
        stateId_ = stateId;

        try {
            networkHelper_ = new NetworkHelper(InetAddress.getByName("localhost"), Constants.DESTINATION_PORT);
            networkHelper_.setNetworkMessageListener(this);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getID() {
        return stateId_;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        map_ = new GameMap("res/map.png", Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        Random random = new Random();
        int x = random.nextInt(1000) + 600;
        int y = random.nextInt(1000) + 600;
        playerTank_ = new Tank("res/tank1.png", x, y);
        playerTank_.setBoundDrawEnable(true);
        playerTank_.setTankStatusListener(this);
        map_.setPlayerTank(playerTank_);
        enemyTank_ = new Tank("res/tank1.png", 600, 600);
        enemyTank_.setBoundDrawEnable(true);
        map_.addEnemyTank(enemyTank_);

        bulletImage_ = new Image("res/bullet.png");
        victoryImage_ = new Image("res/victory.png");
        defeatImage_ = new Image("res/defeat.png");
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame game, Graphics graphics) throws SlickException {
        // Map also takes care of drawing any object has been add to it, include tanks
        map_.draw();

        if (isWin_) {
            victoryImage_.drawCentered(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2);
        } else if (isLost_) {
            defeatImage_.drawCentered(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2);
        }
        graphics.drawString(String.format("X: %.0f Y: %.0f Rotation: %.0f Velocity: %.2f", x, y, rotation, velocity), 100, 100);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input input = gameContainer.getInput();
        if (input.isKeyDown(Input.KEY_ESCAPE)) {
            stateBasedGame.enterState(Constants.STATE_MENU);
        }
        map_.updateObjectsState(i);
        // Skip other update if game was over
        if (isLost_ || isWin_) {
            return;
        }
        if (input.isKeyDown(Input.KEY_W)) {
            playerTank_.accelerate();
        }
        if (input.isKeyDown(Input.KEY_S)) {
            playerTank_.brake();
        }
        if (input.isKeyDown(Input.KEY_A)) {
            playerTank_.turnLeft();
        }
        if (input.isKeyDown(Input.KEY_D)) {
            playerTank_.turnRight();
        }
        if (input.isKeyDown(Input.KEY_J) && playerTank_.canShoot()) {
            Bullet bullet = new Bullet(
                    bulletImage_.copy(),
                    playerTank_.getCenterX(), playerTank_.getCenterY(),
                    playerTank_.getRotation(),
                    playerTank_.getId());
            networkHelper_.sendShootMessage(
                    playerTank_.getId(),
                    playerTank_.getCenterX(), playerTank_.getCenterY(),
                    playerTank_.getRotation());
            map_.addObject(bullet);
        }

        networkHelper_.sendMoveMessage(playerTank_.getId(), playerTank_.getCenterX(), playerTank_.getCenterY(),
                playerTank_.getRotation(), playerTank_.getVelocity());
    }

    @Override
    public void onMoveMessageReceived(int id, float x, float y, float rotation, float velocity) {
        // Should check for null because enemy tank may not be initialized yet
        if (enemyTank_ != null) {
            enemyTank_.setCenterX(x);
            enemyTank_.setCenterY(y);
            enemyTank_.setRotation(rotation);
            enemyTank_.setVelocity(velocity);
        }
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.velocity = velocity;
    }

    @Override
    public void onShootMessageReceived(int id, float x, float y, float rotation) {
        try {
            Bullet bullet = new Bullet(bulletImage_.copy(), x, y, rotation, id);
            map_.addObject(bullet);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBeingHitMessageReceived(int id, float x, float y, boolean isDestroyed) {
        enemyTank_.setCenterX(x);
        enemyTank_.setCenterY(y);
        if (isDestroyed) {
            enemyTank_.destroy();
            isWin_ = true;
        } else {
            enemyTank_.takeDamage();
        }
    }

    @Override
    public void onBeingHit(int tankId, boolean isDestroyed) {
        if (tankId == playerTank_.getId()) {
            networkHelper_.sendBeingHitMessage(tankId, playerTank_.getCenterX(), playerTank_.getCenterY(), isDestroyed);
            isLost_ = isDestroyed;
        }
    }
}
