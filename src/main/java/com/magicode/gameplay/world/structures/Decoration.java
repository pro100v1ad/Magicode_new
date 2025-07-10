package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;

import java.awt.*;

public class Decoration extends Structure { // Класс отвечающий за структуры декораций

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
        if(name.equals("bed")) {
            image = gp.textureAtlas.textures[18][3].getTexture();
        }
        if(name.equals("table")) {
            image = gp.textureAtlas.textures[18][4].getTexture();
        }
        if(name.equals("chair")) {
            image = gp.textureAtlas.textures[18][5].getTexture();
        }
        if(name.equals("carpet")) {
            image = gp.textureAtlas.textures[18][6].getTexture();
        }
        if(name.equals("calendar")) {
            image = gp.textureAtlas.textures[18][7].getTexture();
        }
    }


    @Override
    public void draw(Graphics2D g) {

        int screenX = (int) (x - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int) (y - gp.player.getWorldY() + gp.player.getScreenY());

        if (x + GamePanel.tileSize * 2 > gp.player.getWorldX() - gp.player.getScreenX() &&
                x - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                y + GamePanel.tileSize * 2 > gp.player.getWorldY() - gp.player.getScreenY() &&
                y - GamePanel.tileSize * 6 < gp.player.getWorldY() + gp.player.getScreenY())
        {
            g.drawImage(image, screenX, screenY, w, h, null);
        }


    }

}
