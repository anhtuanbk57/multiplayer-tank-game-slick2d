package com.tuanna.widgets;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.ShapeRenderer;

public class TextBox extends RoundedRectangle {

    private static final float CORNER_RADIUS = 10;

    private Font font_;
    private String text_;
    private float textPosX_;
    private float textPosY_;

    public TextBox(Font font, String text, float x, float y, float width, float height) {
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
    }

    public void draw() {
        ShapeRenderer.draw(this);
        font_.drawString(textPosX_, textPosY_, text_, Color.orange);
    }
}
