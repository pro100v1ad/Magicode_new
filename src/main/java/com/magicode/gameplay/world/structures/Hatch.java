package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;

import java.awt.*;

public class Hatch extends Structure {


    private int nextPlayerX, nextPlayerY;
    private GamePanel gp;

    public Hatch(GamePanel gp, int x, int y, int w, int h, String code, boolean state, int nextPlayerX, int nextPlayerY) {
        this.name = "hatch";
        this.code = Integer.parseInt(code.split(":")[0]);
        this.radius = Integer.parseInt(code.split(":")[1]);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.nextPlayerX = nextPlayerX;
        this.nextPlayerY = nextPlayerY;

        this.gp = gp;
        this.state = state;

        loadImage();
    }

    private void loadImage() {

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
