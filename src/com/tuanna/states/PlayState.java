package com.tuanna.states;

import com.tuanna.main.Constants;
import com.tuanna.objects.Bullet;
import com.tuanna.objects.GameMap;
import com.tuanna.objects.Tank;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class PlayState extends BasicGameState {

    private int stateId_;

    private GameMap map_;

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
    }
}
