package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;

import java.awt.*;

public class Decoration extends Structure {

    private GamePanel gp;

    public Decoration(GamePanel gp, int x, int y, int w, int h, String name) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.name = name;

        loadImage();

    }

    public void loadImage() {
        if(name.equals("tree")) {
            image = gp.textureAtlas.textures[18][2].getTexture();
        }
        if(name.equals("stone")) {
            image = gp.textureAtlas.textures[18][1].getTexture();
        }
        if(name.equals("bush")) {
            image = gp.textureAtlas.textures[18][0].getTexture();
        }
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
