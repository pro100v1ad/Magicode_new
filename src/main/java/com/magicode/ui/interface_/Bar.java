package main.java.com.magicode.ui.interface_;


import main.java.com.magicode.core.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bar {

    private final int width;
    private final int height;

    private int posX, posY;

    private final int maxValue;
    private int currentValue;
    private float percent;

    private Color stripColor;
    private BufferedImage stripImage;

    private GamePanel gp;

    public Bar(GamePanel gp, int posX, int posY, int width, int height, int maxValue, int currentValue, Color stripColor) {

        this.gp = gp;

        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.stripColor = stripColor;

        this.stripImage = gp.textureAtlas.textures[17][0].getTexture();

    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setColor(Color color) {
        stripColor = color;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
        percent = (float)currentValue/maxValue;
    }


    public void draw(Graphics2D g) {

        g.setColor(stripColor);
        g.fillRect(posX + (int)(width*0.04), posY, (int)(width*percent*0.92), height);

        g.drawImage(stripImage, posX, posY, width, height, null);

    }


}
