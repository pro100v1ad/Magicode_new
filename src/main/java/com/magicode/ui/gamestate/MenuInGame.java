package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuInGame {

    private GamePanel gp;
    private Button[] button;
    private boolean click;

    private String text1;
    private String text2;

    private boolean state;
    private BufferedImage buttonImage;
    private int posButtonX, posButtonY;

    private int fontSize;

    public MenuInGame(GamePanel gp) {
        this.gp = gp;
        this.text1 = "Продолжить";
        this.text2 = "Выход";

        this.fontSize = 48;
        button = new Button[2];
        button[0] = new Button(gp, GamePanel.WIDTH/2-(int)(text1.length()*fontSize/1.55)/2,
                GamePanel.HEIGHT/2-fontSize/2, text1, fontSize, true);
        button[1] = new Button(gp, GamePanel.WIDTH/2-(int)(text2.length()*fontSize/1.55)/2,
                GamePanel.HEIGHT/2-fontSize/2 + 100, text2, fontSize, true);

        this.state = false;
        buttonImage = gp.textureAtlas.textures[14][0].getTexture();
        posButtonX = 10;
        posButtonY = 10;

    }

    public void click() {
        click = true;
    }

    public void update() {

        if(state) {
            if(click) {
                button[0].click();
                button[1].click();
                click = false;
            }
            if(button[0].update()) { // если true ,то нажата кнопка
                state = false;
                gp.state = GamePanel.GameState.Game;

            }
            if(button[1].update()) {
                state = false;
                gp.state = GamePanel.GameState.StartMenu;
                gp.saveGame();
            }
        } else {
            int mX = GamePanel.mouseX;
            int mY = GamePanel.mouseY;
            if(mX >= posButtonX && mX <= posButtonX + GamePanel.tileSize*2
                    && mY >= posButtonY && mY <= posButtonY + GamePanel.tileSize*2) {
                buttonImage = gp.textureAtlas.textures[14][1].getTexture();
                if(click) {
                    click = false;
                    gp.state = GamePanel.GameState.GameMenu;
                    state = true;
                }
            } else {
                buttonImage = gp.textureAtlas.textures[14][0].getTexture();
            }
        }



        click = false;
    }

    public void draw(Graphics2D g) {
        if(state) {
            g.setColor(new Color(28, 28, 28, 100));
            g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

            button[0].draw(g);
            button[1].draw(g);
        } else {

            g.drawImage(buttonImage, posButtonX, posButtonY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);

        }


    }

}
