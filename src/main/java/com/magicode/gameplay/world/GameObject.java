package main.java.com.magicode.gameplay.world;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    protected String name;
    protected BufferedImage image;
    protected int code;
    protected int radius;
    protected int posX, posY, wight, height;


    public void draw(Graphics2D g) {

    }

    public int getCode() {
        return code;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getWight() {
        return wight;
    }

    public int getHeight() {
        return height;
    }

    public int getRadius() {
        return radius;
    }

    public String getName() {
        return name;
    }
}
