package com.tuanna.main;

import org.newdawn.slick.*;
import tests.SimpleSlickGame;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainGame extends BasicGame {

    public MainGame(String title) {
        super(title);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appGc;
            appGc = new AppGameContainer(new MainGame("Simple Carmageddon"));
            appGc.setDisplayMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, false);
            appGc.start();
        } catch (SlickException ex) {
            Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        gameContainer.setTargetFrameRate(Constants.MAX_FPS);
        // Update game even when the window is not focused
        gameContainer.setAlwaysRender(true);
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

    }
}
