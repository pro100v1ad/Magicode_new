package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.ui.GUI;

import java.awt.*;

public class Button extends GUI {

    private String text;
    private float fontSize;
    private int weight, height;

    public Button(int posX, int posY, String text, float fontSize, Color color, boolean state) {
        super();
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.fontSize = fontSize;

        weight = (int)(text.length()*fontSize/1.6);
        height = (int)fontSize;

        System.out.println(weight + " " + height);
    }

    public boolean update() {

        return false;
    }

    @Override
    public void draw(Graphics2D g) {
//        g.setColor(new Color(255, 255, 255, 20));
//        g.fillRect(posX, posY, weight, height);
        g.setColor(new Color(0x1AFF00FF, true));
        g.fillRect(posX, posY, weight, height);

        g.setColor(Color.WHITE);
        g.setFont(my_font.deriveFont(fontSize));
        g.drawString(text, posX+16, posY+height-16);

    }

}
