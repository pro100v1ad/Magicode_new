package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StartMenu { // Класс отвечающий за начальное меню при запуске игры

    private GamePanel gp;
    private Button[] button;
    private boolean click;

    private BufferedImage gameName;

    private String text1;
    private String text2;
    private String text3;

    private int fontSize;
    private boolean button1State;

    public StartMenu(GamePanel gp, boolean isSave) {
        this.gp = gp;
        this.text1 = "Продолжить";
        this.text2 = "Новая игра";
        this.text3 = "Выход";
        this.button1State = isSave;
        this.fontSize = 48;
        button = new Button[3];
        button[0] = new Button(gp, GamePanel.WIDTH/2-(int)(text1.length()*fontSize/1.55)/2,
                GamePanel.HEIGHT/2-fontSize/2, text1, fontSize, button1State);
        button[1] = new Button(gp, GamePanel.WIDTH/2-(int)(text2.length()*fontSize/1.55)/2,
                GamePanel.HEIGHT/2-fontSize/2 + 100, text2, fontSize, true);
        button[2] = new Button(gp, GamePanel.WIDTH/2-(int)(text3.length()*fontSize/1.55)/2,
                GamePanel.HEIGHT/2-fontSize/2 + 200, text3, fontSize, true);

        gameName = gp.textureAtlas.textures[15][3].getTexture();
    }

    public void click() {
        click = true;
    }

    public void setState(boolean state) {
        this.button1State = state;
        button[0].setState(state);
    }


    public void update() {
        if(click) {
            button[0].click();
            button[1].click();
            button[2].click();
            click = false;
        }
        if(button[0].update()) {
            gp.continueGame();
            gp.state = GamePanel.GameState.Game;
        }
        if(button[1].update()) {
            gp.startNewGame();
            gp.state = GamePanel.GameState.Game;
        }
        if(button[2].update()) {
            gp.exitGame();
        }



    }

    public void draw(Graphics2D g) {

        g.setColor(new Color(0xFF1C1C1C, true));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.drawImage(gameName, 100, 100, 960, 128, null);

        button[0].draw(g);
        button[1].draw(g);
        button[2].draw(g);
    }

}
