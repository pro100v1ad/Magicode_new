package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;

import java.awt.*;

public class Chest extends Structure {

    private boolean isLock;
    private GamePanel gp;

    public Chest(GamePanel gp, int x, int y, int w, int h, String code, boolean isLock, boolean state, String direction) {
        this.name = "chest";
        this.code = Integer.parseInt(code.split(":")[0]);
        this.radius = Integer.parseInt(code.split(":")[1]);
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
        if(direction.equals("up")) {
            if(isLock) {
                if(state) image = gp.textureAtlas.textures[12][0].getTexture();
                else image = gp.textureAtlas.textures[12][8].getTexture();
            }
            else image = gp.textureAtlas.textures[12][4].getTexture();
        }
        if(direction.equals("down")) {
            if(isLock) {
                if(state) image = gp.textureAtlas.textures[12][1].getTexture();
                else image = gp.textureAtlas.textures[12][9].getTexture();
            }
            else image = gp.textureAtlas.textures[12][5].getTexture();
        }
        if(direction.equals("right")) {
            if(isLock) {
                if(state) image = gp.textureAtlas.textures[12][2].getTexture();
                else image = gp.textureAtlas.textures[12][10].getTexture();
            }
            else image = gp.textureAtlas.textures[12][6].getTexture();
        }
        if(direction.equals("left")) {
            if(isLock) {
                if(state) image = gp.textureAtlas.textures[12][3].getTexture();
                else image = gp.textureAtlas.textures[12][11].getTexture();
            }
            else image = gp.textureAtlas.textures[12][7].getTexture();
        }
    }

    public boolean getLock() {
        return isLock;
    }

    public void openChest() {
        isLock = false;
        code = 0;
        radius = 0;
        loadImage();
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
