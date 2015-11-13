package com.tuanna.states;

import com.tuanna.main.Constants;
import com.tuanna.objects.MenuButton;
import org.newdawn.slick.Font;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class MenuState extends BasicGameState {

    private StateBasedGame basedGame_;
    private GameContainer gameContainer_;
    private MenuButton createBox_;
    private MenuButton joinBox_;
    private MenuButton exitBox_;
    private Image cover_;

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
        int buttonWidth = MenuButton.MENU_BUTTON_WIDTH;
        int buttonHeight = MenuButton.MENU_BUTTON_HEIGHT;
        int xPos = (Constants.WINDOW_WIDTH - buttonWidth) / 2;
        int yPos = (Constants.WINDOW_HEIGHT - buttonHeight * 3 - MenuButton.MENU_BUTTON_MARGIN * 2) / 2;

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
        createBox_ = new MenuButton(menuFont, Constants.CREATE_TEXT, xPos, yPos, buttonWidth, buttonHeight);
        yPos = yPos + buttonHeight + MenuButton.MENU_BUTTON_MARGIN;
        joinBox_ = new MenuButton(menuFont, Constants.JOIN_TEXT, xPos, yPos, buttonWidth, buttonHeight);
        yPos = yPos + buttonHeight + MenuButton.MENU_BUTTON_MARGIN;
        exitBox_ = new MenuButton(menuFont, Constants.EXIT_TEXT, xPos, yPos, buttonWidth, buttonHeight);

        cover_ = new Image("res/menu-cover.jpg");
    }

    @Override
    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(cover_, 0, 0);
        createBox_.draw();
        joinBox_.draw();
        exitBox_.draw();
    }

    @Override
    public void update(GameContainer gc, StateBasedGame stateBasedGame, int i) throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        basedGame_ = game;
        gameContainer_ = container;
        container.getInput().addMouseListener(this);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        basedGame_ = null;
        gameContainer_ = null;
        container.getInput().removeMouseListener(this);
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        super.mouseClicked(button, x, y, clickCount);

        if (createBox_.contains(x, y)) {
            basedGame_.enterState(Constants.STATE_PLAY);
        } else if (exitBox_.contains(x, y)) {
            gameContainer_.exit();
        }
    }
}
