package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;
import main.java.com.magicode.ui.gamestate.tablet.EditArea;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Directory extends GUI {

    private int directoryStance;

    private BufferedImage directoryImage;

    private BufferedImage directoryExit;
    private int posButtonExitX, posButtonExitY;

    private BufferedImage directoryButtonLeft;
    private int posButtonLeftX, posButtonLeftY;

    private BufferedImage directoryButtonRight;
    private int posButtonRightX, posButtonRightY;

    private Button[] spellButtons;
    private int buttonCount;
    private int posButtonX, posButtonY;
    private String[] buttonNames;

    //Text
    private String[] text;
    private static int lineSpace;
    private int fontSize;
    private static int textPosX, textPosY;
    private Color textColor;
    private int textNavigationX, textNavigationY;
    private String[] currentText;

    private GamePanel gp;
    private boolean click;

    public Directory(GamePanel gp) {
        this.gp = gp;

        directoryImage = gp.textureAtlas.textures[16][0].getTexture();
        directoryExit = gp.textureAtlas.textures[15][1].getTexture();
        directoryButtonLeft = gp.textureAtlas.textures[16][1].getTexture();
        directoryButtonRight = gp.textureAtlas.textures[16][2].getTexture();

        currentText = new String[100];
        fontSize = 15;
        lineSpace = (int)(fontSize*1.5);
        textNavigationX = 615;
        textNavigationY = 80;

        posButtonExitX = 1049;
        posButtonExitY = 15;

        spellButtons = new Button[5];

        posButtonX = 128;
        posButtonY = 128;
        buttonCount = 0;
        buttonNames =  new String[] {"Переменные()","Операторы()","Символы()","Условия()","Циклы()"};

        for (int i = 0; i < 5; i++) {

            addInfo();

        }


    }

    public void addInfo(){
    spellButtons[buttonCount]=new Button(this.gp,posButtonX,posButtonY+buttonCount*64,buttonNames[buttonCount],32,true);
    spellButtons[buttonCount].setTextColor(Color.BLACK);
    spellButtons[buttonCount].setBackgroundColor(new Color(255, 255, 255, 0));
    spellButtons[buttonCount].setStaticLineColor(new Color(255, 255, 255, 0));
    spellButtons[buttonCount].setPassiveLineColor(new Color(140, 140, 140, 200));
    spellButtons[buttonCount].setActiveLineColor(Color.pink);
    buttonCount++;
    }

    public void click() {
        click = true;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(28, 28, 28, 100));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.drawImage(directoryImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);

        g.drawImage(directoryExit, posButtonExitX, posButtonExitY, GamePanel.tileSize * 2, GamePanel.tileSize * 2, null);
        for (Button button : spellButtons) {
            if (button != null) {
                button.draw(g);
            }
        }
        textDraw(g);
    }

    public void textDraw(Graphics2D g) {
        if (currentText != null) {
            g.setColor(Color.BLACK);
            Font defaultFont = new Font("Comic Sans MS", Font.BOLD, fontSize);
            for(int i = 0; i < currentText.length; i++) {
                if(currentText[i] != null) {
                    if(i == 0 ) g.setFont(my_font.deriveFont((float)fontSize*2)); else g.setFont(defaultFont.deriveFont((float)fontSize));
                    g.drawString(currentText[i], textNavigationX, textNavigationY+i*lineSpace);
                }
            }
        }
    }

    public void textOut(String name) {

        String filePath = "src/resources/gui/directory/codeInfo/";

        switch (name) {
            case "Переменные()":
                filePath = filePath + "variables.txt";
                break;
            case "Операторы()":
                filePath = filePath + "operators.txt";
                break;
            case "Символы()":
                filePath = filePath + "symbols.txt";
                break;
            case "Условия()":
                filePath = filePath + "conditions.txt";
                break;
            case "Циклы()":
                filePath = filePath + "cycles.txt";
                break;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;
            }
            currentText[0] = line;
            int linecounter = 1;
            while (line != null) {
                line = reader.readLine();
                currentText[linecounter] = line;
                linecounter++;
            }

            System.out.println("Текст загружен из файла: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке текста: " + e.getMessage());
        }
    }

    public void update() {

        int mX = GamePanel.mouseX;
        int mY = GamePanel.mouseY;

        if (mX >= posButtonExitX && mX <= posButtonExitX + GamePanel.tileSize * 2
                && mY >= posButtonExitY && mY <= posButtonExitY + GamePanel.tileSize * 2) {
            if (click) {
                click = false;
                gp.state = GamePanel.GameState.Game;
            }
            directoryExit = gp.textureAtlas.textures[15][2].getTexture();
        } else {
            directoryExit = gp.textureAtlas.textures[15][1].getTexture();
        }

        for(Button button : spellButtons) {
            if (button != null && click) {
                button.click();
                if (button.update()) {
                    textOut(button.getText());
                }
            }
        }
        click = false;
    }
}