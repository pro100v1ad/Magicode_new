package main.java.com.magicode.gameplay.world.objects;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.GameObject;

import java.awt.*;

public class Signs extends GameObject { // Класс отвечающий за объект знак

    private GamePanel gp;

    public Signs(GamePanel gp, int posX, int posY, int code, String name) {

        this.name = name;
        this.gp = gp;
        this.posX = posX;
        this.posY = posY;
        this.wight = 32;
        this.height = 32;
        this.code = code;
        this.radius = 16;

        loadImage();

    }

    public void loadImage() {
        if(name.equals("N")) {
            this.image = gp.textureAtlas.textures[13][4].getTexture();
        }
        if(name.equals("plus")) {
            this.image = gp.textureAtlas.textures[13][6].getTexture();
        }
        if(name.equals("minus")) {
            this.image = gp.textureAtlas.textures[13][5].getTexture();
        }
        if(name.equals("exclamationMark")) {
            this.image = gp.textureAtlas.textures[13][7].getTexture();
        }

    }

    public void draw(Graphics2D g) {


        int screenX = (int) (posX - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int) (posY - gp.player.getWorldY() + gp.player.getScreenY());

        if (posX + GamePanel.tileSize > gp.player.getWorldX() - gp.player.getScreenX() &&
                posX - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                posY + GamePanel.tileSize > gp.player.getWorldY() - gp.player.getScreenY() &&
                posY - GamePanel.tileSize * 4 < gp.player.getWorldY() + gp.player.getScreenY())
        {
            g.drawImage(image, screenX, screenY, wight, height, null);
        }

    }

}
