package com.tuanna.main;

import com.tuanna.states.MenuState;
import com.tuanna.states.PlayState;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import tests.SimpleSlickGame;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainGame extends StateBasedGame {

    public MainGame(String title) {
        super(title);
        addState(new PlayState(Constants.STATE_PLAY));
        addState(new MenuState(Constants.STATE_MENU));
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        getState(Constants.STATE_PLAY).init(gc, this);
        getState(Constants.STATE_MENU).init(gc, this);

        enterState(Constants.STATE_PLAY);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appGc;
            appGc = new AppGameContainer(new MainGame("Simple Carmageddon"));
            appGc.setDisplayMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, false);
            appGc.setTargetFrameRate(Constants.MAX_FPS);
            appGc.setShowFPS(true);
            // Update game even when the window is not focused
            appGc.setAlwaysRender(true);
            appGc.start();
        } catch (SlickException ex) {
            Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
