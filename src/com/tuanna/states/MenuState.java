package com.tuanna.states;

import com.tuanna.main.Constants;
import com.tuanna.widgets.TextBox;
import org.newdawn.slick.Font;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class MenuState extends BasicGameState {

    private StateBasedGame basedGame_;
    private GameContainer gameContainer_;
    private TextBox startBox_;
    private TextBox exitBox_;

    private int stateId_;

    public MenuState(int stateId) {
        stateId_ = stateId;
    }

    @Override
    public int getID() {
        return stateId_;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        basedGame_ = stateBasedGame;
        gameContainer_ = gameContainer;
        int xPos = Constants.WINDOW_WIDTH / 4;
        int yPos = Constants.WINDOW_HEIGHT / 5;
        int width = xPos * 2;
        int height = yPos;

        java.awt.Font awtFont;
        try {
            InputStream is = ResourceLoader.getResourceAsStream("fonts/ChamsBold.ttf");
            awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
            awtFont = awtFont.deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return;
        }
        Font menuFont = new TrueTypeFont(awtFont , true);
        startBox_ = new TextBox(menuFont, Constants.START_TEXT, xPos, yPos, width, height);

        yPos = yPos * 3;
        exitBox_ = new TextBox(menuFont, Constants.EXIT_TEXT, xPos, yPos, width, height);

        gameContainer.getInput().addMouseListener(this);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        startBox_.draw();
        exitBox_.draw();
    }

    @Override
    public void update(GameContainer gc, StateBasedGame stateBasedGame, int i) throws SlickException {
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        super.mouseClicked(button, x, y, clickCount);

        if (startBox_.contains(x, y)) {
            basedGame_.enterState(Constants.STATE_PLAY);
        } else if (exitBox_.contains(x, y)) {
            gameContainer_.exit();
        }
    }
}
