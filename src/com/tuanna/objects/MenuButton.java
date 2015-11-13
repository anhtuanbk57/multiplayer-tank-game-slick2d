package com.tuanna.objects;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.RoundedRectangle;

public class MenuButton extends RoundedRectangle {

    public static final int MENU_BUTTON_WIDTH = 300;
    public static final int  MENU_BUTTON_HEIGHT = 80;
    public static final int  MENU_BUTTON_MARGIN = 50;

    private static final float CORNER_RADIUS = 10;

    private Image normalBackground_;
    private Image pressedBackground_;
    private Font font_;
    private String text_;
    private float textPosX_;
    private float textPosY_;

    public MenuButton(Font font, String text, float x, float y, float width, float height) {
        super(x, y, width, height, CORNER_RADIUS);
        font_ = font;
        text_ = text;

        init();
    }

    private void init() {
        float textWidth = font_.getWidth(text_);
        float textHeight = font_.getLineHeight();
        textPosX_ = (width - textWidth) / 2 + x;
        textPosY_ = (height - textHeight) / 2 + y;

        try {
            pressedBackground_ = new Image("res/button_pressed.png");
            normalBackground_ = new Image("res/button_normal.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void draw() {
        normalBackground_.draw(x, y, width, height);
        font_.drawString(textPosX_, textPosY_, text_, Color.orange);
    }
}
