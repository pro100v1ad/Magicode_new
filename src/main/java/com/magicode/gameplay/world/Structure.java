package main.java.com.magicode.gameplay.world;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Structure {

    protected String name;
    protected BufferedImage image;
    protected int code;
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

    public String getName() {
        return name;
    }

    public String getDirection() {
        return direction;
    }
}
