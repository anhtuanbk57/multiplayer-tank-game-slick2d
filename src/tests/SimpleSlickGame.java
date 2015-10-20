package tests;


import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleSlickGame extends BasicGame {

    public SimpleSlickGame(String gameName) {
        super(gameName);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        gc.setTargetFrameRate(60);
        gc.setVSync(true);
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawString("Howdy!", 100, 100);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appGc;
            appGc = new AppGameContainer(new SimpleSlickGame("Simple Slick Game"));
            appGc.setDisplayMode(640, 480, false);
            appGc.start();
        } catch (SlickException ex) {
            Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
