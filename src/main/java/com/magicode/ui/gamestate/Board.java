package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Board extends GUI {

    private BufferedImage boardImage;

    private BufferedImage boardExit;
    private int posButtonExitX, posButtonExitY;

    private int posX, posY;

    private GamePanel gp;
    private boolean click;

    public Board(GamePanel gp, int posX, int posY) {

        this.gp = gp;

        boardImage = gp.textureAtlas.textures[15][4].getTexture();
        boardExit = gp.textureAtlas.textures[15][1].getTexture();

        this.posX = posX;
        this.posY = posY;

        this.posButtonExitX = posX + 380 - 64;
        this.posButtonExitY = posY + 8;



    }

    public void click() {
        click = true;
    }

    public void update() {

        if(!gp.state.equals(GamePanel.GameState.GameOpenBoard)) {
            return;
        }

        int mX = GamePanel.mouseX;
        int mY = GamePanel.mouseY;

        if(mX > posButtonExitX && mX < posButtonExitX + 64 && mY > posButtonExitY && mY < posButtonExitY + 64) {
            boardExit = gp.textureAtlas.textures[15][1].getTexture();
            if(click) {
                gp.state = GamePanel.GameState.Game;
            }
        } else {
            boardExit = gp.textureAtlas.textures[15][2].getTexture();
        }

        click = false;
    }

    public void draw(Graphics2D g) {

        if(!gp.state.equals(GamePanel.GameState.GameOpenBoard)) {
            return;
        }

        g.drawImage(boardImage, posX, posY, 384, 192, null);
        g.drawImage(boardExit, posButtonExitX, posButtonExitY, 64, 64, null);

    }


}
