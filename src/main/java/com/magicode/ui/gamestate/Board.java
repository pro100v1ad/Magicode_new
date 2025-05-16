package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Board extends GUI {

    private BufferedImage boardImage;

    private BufferedImage boardExit;
    private int posButtonExitX, posButtonExitY;

    private String filePath;
    private String[] text;
    private int countRows;

    private int posX, posY;

    private GamePanel gp;
    private boolean click;

    public Board(GamePanel gp, int posX, int posY, String filePath) {

        this.gp = gp;

        this.countRows = 10;
        this.filePath = filePath;
        text = new String[countRows];

        boardImage = gp.textureAtlas.textures[15][4].getTexture();
        boardExit = gp.textureAtlas.textures[15][1].getTexture();

        this.posX = posX;
        this.posY = posY;

        this.posButtonExitX = posX + 380*2 - 72;
        this.posButtonExitY = posY + 16;

        openFile();

    }

    public void click() {
        click = true;
    }

    public void openFile() {
        if(filePath == null) {
            text[1] = "Ну что могу сказать... ";
            text[2] = "Разраб забыл добавить задание...";
            return;
        }
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + filePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }

            int index = 0;
            do {
                text[index++] = line;
            } while ((line = br.readLine()) != null && index < countRows);


        }
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки задания: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update() {

        if(!gp.state.equals(GamePanel.GameState.GameOpenBoard)) {
            return;
        }

        int mX = GamePanel.mouseX;
        int mY = GamePanel.mouseY;
        if(GamePanel.keys[6]) {
            gp.state = GamePanel.GameState.Game;
            GamePanel.keys[6] = false;
            return;
        }
        if(mX > posButtonExitX && mX < posButtonExitX + 64 && mY > posButtonExitY && mY < posButtonExitY + 64) {
            boardExit = gp.textureAtlas.textures[15][2].getTexture();
            if(click) {
                gp.state = GamePanel.GameState.Game;
            }
        } else {
            boardExit = gp.textureAtlas.textures[15][1].getTexture();
        }

        click = false;
    }

    public void draw(Graphics2D g) {

        if(!gp.state.equals(GamePanel.GameState.GameOpenBoard)) {
            return;
        }
        g.drawImage(boardImage, posX, posY, 384*2, 192*2, null);
        g.drawImage(boardExit, posButtonExitX, posButtonExitY, 64, 64, null);

        g.setColor(new Color(160, 190, 250));
        g.setFont(my_font.deriveFont(32f));

        for(int i = 0; i < text.length; i++) {
            if(text[i] != null) {
                g.drawString(text[i], posX + 32, posY + 48 + i*32);
            }
        }

    }


}
