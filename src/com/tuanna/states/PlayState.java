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


public class PlayState extends BasicGameState implements NetworkHelper.NetworkMessageListener {

    private int stateId_;

    private GameMap map_;
    private NetworkHelper networkHelper_;

    private float x, y, rotation, velocity;

    public PlayState(int stateId) {
        stateId_ = stateId;
    }

    @Override
    public int getID() {
        return stateId_;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        map_ = new GameMap("res/map.jpg", Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        Tank playerTank = new Tank("res/tank3.png", "res/smoke.png", Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2);
        playerTank.setBulletImage(new Image("res/bullet.png"));
        playerTank.setBoundDrawEnable(true);
        map_.setPlayerTank(playerTank);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        map_.draw();

        graphics.drawString(String.format("X: %.0f Y: %.0f Rotation: %.0f Velocity %.2f", x, y, rotation, velocity), 10, 100);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input input = gameContainer.getInput();

        Tank playerTank = map_.getPlayerTank();
        if (input.isKeyDown(Input.KEY_W)) {
            playerTank.accelerate();
        }
        if (input.isKeyDown(Input.KEY_S)) {
            playerTank.brake();
        }
        if (input.isKeyDown(Input.KEY_A)) {
            playerTank.turnLeft();
        }
        if (input.isKeyDown(Input.KEY_D)) {
            playerTank.turnRight();
        }
        if (input.isKeyDown(Input.KEY_J)) {
            Bullet bullet = playerTank.fire();
            map_.addObject(bullet);
        }
        map_.updateObjectsState(i);

        networkHelper_.sendMoveMessage(playerTank.getId(), playerTank.getCenterX(), playerTank.getCenterY(),
                playerTank.getRotation(), playerTank.getVelocity());
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        try {
            networkHelper_ = new NetworkHelper(InetAddress.getByName("localhost"), Constants.DESTINATION_PORT);
            networkHelper_.setNetworkMessageListener(this);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        networkHelper_.removeNetworkMessageListener();
    }

    @Override
    public void onMoveMessageReceived(int id, float x, float y, float rotation, float velocity) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.velocity = velocity;
    }

    @Override
    public void onShootMessageReceived(int id, float x, float y, float rotation) {

    }

    @Override
    public void onDestroyMessageReceived(int id, float x, float y) {

    }
}
