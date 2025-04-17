package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;

import java.awt.*;

public class Door extends Structure {

    private boolean isLock, state;
    private GamePanel gp;
    public Door(GamePanel gp, int x, int y, int w, int h, int code, boolean isLock, boolean state, String direction) {
        this.name = "door";
        this.code = code;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.gp = gp;
        this.isLock = isLock;
        this.state = state;
        this.direction = direction;

        loadImage();
    }

    private void loadImage() {
        if(direction.equals("up")) image = gp.textureAtlas.textures[10][0].getTexture();
        if(direction.equals("down")) image = gp.textureAtlas.textures[10][1].getTexture();
        if(direction.equals("right")) image = gp.textureAtlas.textures[10][2].getTexture();
        if(direction.equals("left")) image = gp.textureAtlas.textures[10][3].getTexture();
    }


    @Override
    public void draw(Graphics2D g) {

        int screenX = (int) (x - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int) (y - gp.player.getWorldY() + gp.player.getScreenY());

        if (x + GamePanel.tileSize > gp.player.getWorldX() - gp.player.getScreenX() &&
                x - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                y + GamePanel.tileSize > gp.player.getWorldY() - gp.player.getScreenY() &&
                y - GamePanel.tileSize * 4 < gp.player.getWorldY() + gp.player.getScreenY())
        {
            g.drawImage(image, screenX, screenY, w, h, null);
        }


    }

}
