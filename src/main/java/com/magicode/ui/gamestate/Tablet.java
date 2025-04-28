package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tablet extends GUI {

    private BufferedImage imageTablet;

    private BufferedImage imageButtonExit;
    private int posButtonExitX, posButtonExitY;

    private Button buttonSave;
    private int posButtonSaveX, posButtonSaveY;


    //Text
    private String[] text;
    private int lineSpace;
    private int fontSize;
    private int countRowsVisible;
    private int textPosX, textPosY;
    private Color textColor;
    private int percent;

    private float scrollPosition; // Вместо percent
    // Вместо фиксированной скорости
    private static final float SCROLL_SPEED = 0.05f * GamePanel.UPDATE_RATE_Speed;  // Скорость скролла

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


        //text
        countRowsVisible = 27;
        text = new String[40 + 1];
        text[0] = "1 Hello WOrld!";
        text[1] = "2 Hi brooo";
        text[2] = "3 GoodBy -_+";
        text[3] = "4 GoodBy -_+";
        text[4] = "5 GoodBy -_+";
        text[5] = "6 GoodBy -_+";
        text[6] = "7 GoodBy -_+";
        text[7] = "8 GoodBy -_+";
        text[8] = "9 GoodBy -_+";
        text[9] = "10 GoodBy -_+";
        text[10] = "11 GoodBy -_+";
        text[11] = "12 GoodBy -_+";
        text[12] = "13 GoodBy -_+";
        text[13] = "14GoodBy -_+";
        text[14] = "15 GoodBy -_+";
        text[15] = "16 GoodBy -_+";
        text[16] = "17 GoodBy -_+";
        text[17] = "18 GoodBy -_+";
        text[18] = "19 GoodBy -_+";
        text[19] = "20 GoodBy -_+";
        text[20] = "21 GoodBy -_+";
        text[21] = "22 GoodBy -_+";
        text[22] = "23 GoodBy -_+";
        text[23] = "24 GoodBy -_+";
        text[24] = "25 GoodBy -_+";
        text[25] = "26 GoodBy -_+";
        text[26] = "27 GoodBy -_+";
        text[27] = "28 GoodBy -_+";
        text[28] = "29 GoodBy -_+";
        text[29] = "30 GoodBy -_+";
        text[30] = "31 GoodBy -_+";
        text[31] = "32 GoodBy -_+";
        text[32] = "33 GoodBy -_+";
        text[33] = "34 GoodBy -_+";
        text[34] = "35 GoodBy -_+";
        text[35] = "36 GoodBy -_+";
        text[36] = "37 GoodBy -_+";
        text[37] = "38 GoodBy -_+";
        text[38] = "39 GoodBy -_+";
        text[39] = "40 GoodBy -_+";
        fontSize = 15;
        lineSpace = (int)(fontSize*1.5);
        textPosX = 336 + fontSize;
        textPosY = 56 + (int)(fontSize*2.2);
        textColor = Color.WHITE;
        percent = 1000;

    }

    public void click() {
        click = true;
    }

//    private void drawText(Graphics2D g) {
//        g.setColor(textColor);
//        g.setFont(my_font.deriveFont((float)fontSize));
//
//        int countRows = text.length - countRowsVisible;
//        int startRow;
//        int indent;
//        if(countRows <= 0) {
//            startRow = 0;
//            indent = fontSize;
//        } else {
//            startRow = countRows*(percent/10)/100;
//            indent = (int)((fontSize)*((float)percent%10/10));
//        }
//
//        for(int i = startRow, j = 0; i < countRowsVisible; i++, j++) {
//            if(text[i] != null) {
//
//                g.drawString(text[i], textPosX, textPosY - indent + j*lineSpace);
//            }
//        }
//
//        g.setColor(new Color(30, 31, 34));
//        g.setColor(Color.white);
//        g.fillRect(351, 56, 300, fontSize);
//        g.fillRect(351, 48 + 600, 300, fontSize);
//
//    }

    private void drawText(Graphics2D g) {
        g.setColor(textColor);
        g.setFont(my_font.deriveFont((float)fontSize));

        TextDrawingContext context = calculateTextPositionContext();
        drawVisibleTextLines(g, context);
        drawScrollIndicators(g);
    }

    private TextDrawingContext calculateTextPositionContext() {
        TextDrawingContext context = new TextDrawingContext();
        int totalRows = text.length;

        if (totalRows <= countRowsVisible) {
            context.startRow = 0;
            context.verticalOffset = 0;
        } else {
            float maxScroll = totalRows - countRowsVisible;
            float rowOffset = scrollPosition % 1.0f; // Дробная часть

            context.startRow = (int)scrollPosition;
            context.verticalOffset = (int)(-lineSpace * rowOffset);
        }

        return context;
    }

    private void drawVisibleTextLines(Graphics2D g, TextDrawingContext context) {
        for (int i = 0; i < countRowsVisible; i++) {
            int textIndex = context.startRow + i;
            if (textIndex < text.length && text[textIndex] != null) {
                int yPos = textPosY + context.verticalOffset + i * lineSpace;
                g.drawString(text[textIndex], textPosX, yPos);
            }
        }
    }

    private void drawScrollIndicators(Graphics2D g) {
        // Верхний индикатор скролла
        g.setColor(new Color(30, 31, 34));
        g.fillRect(350, 54, 300, fontSize + 2);
        g.setColor(new Color(49, 52, 56));
        g.drawLine(350, 56 + fontSize, 651, 56 + fontSize);

        // Нижний индикатор скролла
        if (text.length > countRowsVisible) {
            g.setColor(new Color(30, 31, 34));
            g.fillRect(350, 48 + 600, 300, fontSize + 8);
        }
        g.setColor(new Color(49, 52, 56));
        g.drawLine(350, 48 + 600, 651, 48 + 600);
    }

    // Вспомогательный класс для передачи данных о позиционировании
    private static class TextDrawingContext {
        int startRow;
        int verticalOffset;
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

        scrollPosition += SCROLL_SPEED;
        if (scrollPosition > text.length - countRowsVisible) {
            scrollPosition = 0;
        }
    }

    public void draw(Graphics2D g) {

        g.drawImage(imageTablet, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);

        g.drawImage(imageButtonExit, posButtonExitX, posButtonExitY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);

        buttonSave.draw(g);

        drawText(g);

    }




}
