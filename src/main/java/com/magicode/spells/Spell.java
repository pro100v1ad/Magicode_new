package main.java.com.magicode.spells;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Spell extends GUI {

    protected int defaultFirst, defaultSecond;
    protected String defaultThird;
    protected boolean defaultFourth;
    protected String name;
    protected boolean isVisible;

    protected int rechargeTime;
    protected int currentRechargeTime;
    protected boolean isRecharge;

    protected BufferedImage rechargeIconSpell;
    protected BufferedImage rechargeFrame;
    protected BufferedImage rechargeBackgroundIconSpell;
    protected int posIconX, posIconY;
    protected String wordToUse;
    protected int wordX, wordY;
    protected float fontSize;

    public Spell() {

    }

    public int getDefaultFirst() {
        return defaultFirst;
    }

    public int getDefaultSecond() {
        return defaultSecond;
    }

    public String getDefaultThird() {
        return defaultThird;
    }

    public boolean getDefaultFourth() {
        return defaultFourth;
    }

    public void setDefaultFirst(int defaultFirst) {
        this.defaultFirst = defaultFirst;
    }

    public void setDefaultSecond(int defaultSecond) {
        this.defaultSecond = defaultSecond;
    }

    public void setDefaultThird(String defaultThird) {
        this.defaultThird = defaultThird;
    }

    public void setDefaultFourth(boolean defaultFourth) {
        this.defaultFourth = defaultFourth;
    }

    public boolean getVisible() {
        return isVisible;
    }

    public String getName() {
        return name;
    }

    public boolean update(String string, int index) {
        return true;
    }

    public void recharge() {

    }

    public void draw(Graphics2D g) {

//        System.out.println((float)currentRechargeTime/rechargeTime);

        g.drawImage(rechargeBackgroundIconSpell, posIconX, posIconY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);
        g.setColor(new Color(255, 255, 255, 100));
        g.fillRect(posIconX+4, posIconY+4, GamePanel.tileSize*2-8, (int)((GamePanel.tileSize*2-8)*((float)currentRechargeTime/rechargeTime)));
        g.drawImage(rechargeIconSpell, posIconX+8, posIconY+8, GamePanel.tileSize*2-16, GamePanel.tileSize*2-16, null);
        g.drawImage(rechargeFrame, posIconX, posIconY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);

        g.setFont(my_font.deriveFont(fontSize));
        g.setColor(new Color(160, 190, 250));
        g.drawString(wordToUse, wordX, wordY);

    }
}
