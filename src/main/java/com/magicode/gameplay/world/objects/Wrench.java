package main.java.com.magicode.gameplay.world.objects;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.GameObject;

import java.awt.*;

public class Wrench extends GameObject { // Класс отвечающий за объект гаечный ключ

    private GamePanel gp;

    public Wrench(GamePanel gp, int posX, int posY, int code) {

        name = "wrench";
        this.gp = gp;
        this.image = gp.textureAtlas.textures[13][2].getTexture();
        this.posX = posX;
        this.posY = posY;
        this.wight = 32;
        this.height = 32;
        this.code = code;
        this.radius = 16;

    }

    public void draw(Graphics2D g) {


        int screenX = (int) (posX - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int) (posY - gp.player.getWorldY() + gp.player.getScreenY());

        if (posX + GamePanel.tileSize > gp.player.getWorldX() - gp.player.getScreenX() &&
                posX - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                posY + GamePanel.tileSize > gp.player.getWorldY() - gp.player.getScreenY() &&
                posY - GamePanel.tileSize * 4 < gp.player.getWorldY() + gp.player.getScreenY())
        {
            g.drawImage(image, screenX, screenY, wight*2, height*2, null);
        }

    }

}
