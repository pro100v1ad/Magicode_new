package main.java.com.magicode.gameplay.world;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Structure {

    protected String name;
    protected BufferedImage image;
    protected int code;
    protected int radius;
    protected boolean state; // Можно ли взаимодействовать
    protected String direction;
    protected int x, y, w, h;

    public Structure() {

    }

    public void draw(Graphics2D g) {

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getCode() {
        return code;
    }

    public int getRadius() {
        return radius;
    }

    public boolean getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getDirection() {
        return direction;
    }
}
