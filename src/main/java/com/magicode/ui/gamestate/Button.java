package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;

public class Button extends GUI {

    private String text;
    private Color colorLine;
    private float fontSize;
    private int weight, height;
    private boolean click;
    private GamePanel gp;
    private boolean state;


    public Button(GamePanel gp, int posX, int posY, String text, float fontSize, boolean state) {
        super();
        this.gp = gp;
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.fontSize = fontSize;
        this.colorLine = Color.white;
        this.state = state;

        weight = (int)(text.length()*fontSize/1.55);
        height = (int)fontSize;

    }

    public void click() {
        click = true;
    }

    public boolean update() {
        if(!state) {
            return false;
        }
        int mX = GamePanel.mouseX;
        int mY = GamePanel.mouseY;

        if(mX >= posX && mX <= posX+weight && mY >= posY && mY <= posY + height) {
            colorLine = Color.yellow;
            if(click) {
                click = false;
                return true;
            }

        } else {
            colorLine = Color.white;

        }
        click = false;
        return false;
    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(new Color(0x1AFF00FF, true));
        g.fillRect(posX, posY, weight, height);


        g.setFont(my_font.deriveFont(fontSize));

        if(state) {
            g.setColor(Color.white);
        } else {
            g.setColor(new Color(88, 88, 88, 255));
        }


        g.drawString(text, posX+8, posY+height-8);

        if(state) {
            g.setColor(new Color(0xFF990099, true));
        } else {
            g.setColor(new Color(88, 88, 88, 100));
        }
        g.setStroke(new BasicStroke(3));
        g.drawLine(posX, posY+height, posX+weight, posY + height);

        if(state) {
            g.setColor(colorLine);
        } else {
            g.setColor(new Color(88, 88, 88, 100));
        }

        g.drawLine(posX, posY+height+6, posX+weight, posY + height+6);

    }

}
