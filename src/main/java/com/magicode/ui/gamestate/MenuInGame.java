package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuInGame { // Класс отвечающий за меню в игре

    private GamePanel gp;
    private Button[] button;
    private boolean click;

    private String text1;
    private String text2;

    private boolean state;

    private BufferedImage buttonMenuImage;
    private int posButtonMenuX, posButtonMenuY;

    private BufferedImage buttonDirectoryImage;
    private BufferedImage directory;
    private int posButtonDirectoryX, posButtonDirectoryY;

    private BufferedImage buttonTabletImage;
    private BufferedImage tablet;
    private int posButtonTabletX, posButtonTabletY;

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
        buttonMenuImage = gp.textureAtlas.textures[14][0].getTexture();
        posButtonMenuX = 10;
        posButtonMenuY = 10;

        buttonDirectoryImage = gp.textureAtlas.textures[14][2].getTexture();
        directory = gp.textureAtlas.textures[14][4].getTexture();
        posButtonDirectoryX = 10;
        posButtonDirectoryY = 96;

        buttonTabletImage = gp.textureAtlas.textures[14][2].getTexture();
        tablet = gp.textureAtlas.textures[14][5].getTexture();
        posButtonTabletX = 10;
        posButtonTabletY = 232;

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
            if(button[0].update() || GamePanel.keys[6]) { // если true, то нажата кнопка
                state = false;
                gp.state = GamePanel.GameState.Game;
                GamePanel.keys[6] = false;
            }
            if(button[1].update()) {
                state = false;
                gp.state = GamePanel.GameState.StartMenu;
                gp.saveGame();
            }
        } else {
            int mX = GamePanel.mouseX;
            int mY = GamePanel.mouseY;
            if(GamePanel.keys[6]) {
                click = false;
                gp.state = GamePanel.GameState.GameMenu;
                state = true;
                GamePanel.keys[6] = false;
            }
            if(GamePanel.keys[7]) {
                click = false;
                gp.state = GamePanel.GameState.GameOpenTablet;
                state = false;
                GamePanel.keys[7] = false;
            }
            if(GamePanel.keys[8]) {
                click = false;
                gp.state = GamePanel.GameState.GameOpenDirectory;
                state = false;
                GamePanel.keys[8] = false;
            }
            if(mX >= posButtonMenuX && mX <= posButtonMenuX + GamePanel.tileSize*2
                    && mY >= posButtonMenuY && mY <= posButtonMenuY + GamePanel.tileSize*2) {
                buttonMenuImage = gp.textureAtlas.textures[14][1].getTexture();
                if(click) {
                    click = false;
                    gp.state = GamePanel.GameState.GameMenu;
                    state = true;
                }
            } else {
                buttonMenuImage = gp.textureAtlas.textures[14][0].getTexture();
            }

            if(mX >= posButtonDirectoryX && mX <= posButtonDirectoryX + GamePanel.tileSize*3.5
                    && mY >= posButtonDirectoryY && mY <= posButtonDirectoryY + GamePanel.tileSize*3.5) {
                buttonDirectoryImage = gp.textureAtlas.textures[14][3].getTexture();
                if(click) {
                    click = false;
                    gp.state = GamePanel.GameState.GameOpenDirectory;
                    state = false;
                }
            } else {
                buttonDirectoryImage = gp.textureAtlas.textures[14][2].getTexture();
            }

            if(mX >= posButtonTabletX && mX <= posButtonTabletX + GamePanel.tileSize*3.5
                    && mY >= posButtonTabletY && mY <= posButtonTabletY + GamePanel.tileSize*3.5) {
                buttonTabletImage = gp.textureAtlas.textures[14][3].getTexture();
                if(click) {
                    gp.state = GamePanel.GameState.GameOpenTablet;
                    state = false;
                }
            } else {
                buttonTabletImage = gp.textureAtlas.textures[14][2].getTexture();
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

            g.drawImage(buttonMenuImage, posButtonMenuX, posButtonMenuY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);

            g.drawImage(buttonDirectoryImage, posButtonDirectoryX, posButtonDirectoryY, (int)(GamePanel.tileSize*3.5), (int)(GamePanel.tileSize*3.5), null);
            g.drawImage(directory, posButtonDirectoryX + GamePanel.tileSize/2, posButtonDirectoryY + GamePanel.tileSize/2
                    , (int)(GamePanel.tileSize*2.5), (int)(GamePanel.tileSize*2.5), null);

            g.drawImage(buttonTabletImage, posButtonTabletX, posButtonTabletY, (int)(GamePanel.tileSize*3.5), (int)(GamePanel.tileSize*3.5), null);
            g.drawImage(tablet, posButtonTabletX + GamePanel.tileSize/2, posButtonTabletY + GamePanel.tileSize/2
                    , (int)(GamePanel.tileSize*2.5), (int)(GamePanel.tileSize*2.5), null);


        }


    }

}
