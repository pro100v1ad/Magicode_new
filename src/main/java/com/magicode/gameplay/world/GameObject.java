package main.java.com.magicode.gameplay.world;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    protected String name;
    protected BufferedImage image;
    protected int code;
    protected int x, y, w, h;
    protected boolean visible;

    public GameObject() {
        visible = false;
    }

    public void draw(Graphics2D g, int screenX, int screenY) {
        if (visible && image != null) {
            g.drawImage(image, screenX, screenY, w, h, null);
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
