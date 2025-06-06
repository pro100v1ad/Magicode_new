package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;

import java.awt.*;

public class Portal extends Structure { // Класс отвечающий за структуру портал

    private GamePanel gp;

    public Portal(GamePanel gp, int x, int y, int w, int h, String code, String direction) {

        this.name = "portal";

        this.gp = gp;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.state = true;


        this.code = Integer.parseInt(code.split(":")[0]);
        this.radius = Integer.parseInt(code.split(":")[1]);
        this.direction = direction;

        loadImage();
    }

    public void loadImage() {
        if(direction.equals("right") || direction.equals("left")) {
            image = gp.textureAtlas.textures[21][0].getTexture();
        } else {
            image = gp.textureAtlas.textures[21][1].getTexture();
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
