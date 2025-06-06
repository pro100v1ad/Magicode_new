package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EndBoard extends GUI { // Класс отвечающий за доску окончания игры

    private BufferedImage boardImage;

    private int posX, posY;

    private GamePanel gp;
    private boolean click;

    private boolean isWin;
    private Button button;

    public EndBoard(GamePanel gp, int posX, int posY, boolean isWin) {

        this.gp = gp;
        this.isWin = isWin;
        boardImage = gp.textureAtlas.textures[15][4].getTexture();

        this.posX = posX;
        this.posY = posY;

        button = new Button(gp, posX+240, posY+300, "Закончить игру!", 32f, true);


    }

    public void click() {
        click = true;
    }


    public void update() {

        if(!gp.state.equals(GamePanel.GameState.GameOpenEndBoard)) {
            return;
        }

        if(click) {
            button.click();
            click = false;
        }

        if(button.update()) {
            gp.endGame();
        }



        click = false;
    }

    public void draw(Graphics2D g) {

        if(!gp.state.equals(GamePanel.GameState.GameOpenEndBoard)) {
            return;
        }
        g.drawImage(boardImage, posX, posY, 384*2, 192*2, null);


        g.setFont(my_font.deriveFont(32f));

        if(isWin) {
            g.setColor(Color.GREEN);
            g.drawString("          Вы победили!!!", posX + 32, posY + 48 + 1 *32);
        } else {
            g.setColor(Color.RED);
            g.drawString("          Вы проиграли!!!", posX + 32, posY + 48 + 2 *32);
        }

        button.draw(g);

    }
}
