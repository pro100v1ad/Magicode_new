package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tablet {

    private BufferedImage imageTablet;

    private BufferedImage imageButtonExit;
    private int posButtonExitX, posButtonExitY;

    private Button buttonSave;
    private int posButtonSaveX, posButtonSaveY;


    private GamePanel gp;
    private boolean click;

    public Tablet(GamePanel gp) {
        this.gp = gp;

        imageTablet = gp.textureAtlas.textures[15][0].getTexture();
        imageButtonExit = gp.textureAtlas.textures[15][1].getTexture();

        posButtonExitX = 964;
        posButtonExitY = 64;

        posButtonSaveX = 848;
        posButtonSaveY = 608;
        buttonSave = new Button(gp, posButtonSaveX, posButtonSaveY, "Сохранить", 32, true);

    }

    public void click() {
        click = true;
    }

    public void update() {

        int mX = GamePanel.mouseX;
        int mY = GamePanel.mouseY;

        if(mX >= posButtonExitX && mX <= posButtonExitX + GamePanel.tileSize*2
                && mY >= posButtonExitY && mY <= posButtonExitY + GamePanel.tileSize*2) {
            if(click) {
                click = false;
                gp.state = GamePanel.GameState.Game;
            }
            imageButtonExit = gp.textureAtlas.textures[15][2].getTexture();
        } else {
            imageButtonExit = gp.textureAtlas.textures[15][1].getTexture();
        }

        if(buttonSave.update()) {
            System.out.println("Изменения сохранены!");
            gp.state = GamePanel.GameState.Game;
        }
        click = false;
    }

    public void draw(Graphics2D g) {

        g.drawImage(imageTablet, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);

        g.drawImage(imageButtonExit, posButtonExitX, posButtonExitY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);

        buttonSave.draw(g);

    }


}
