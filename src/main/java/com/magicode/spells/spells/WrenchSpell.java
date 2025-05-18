package main.java.com.magicode.spells.spells;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.spells.Spell;

import java.awt.*;

public class WrenchSpell extends Spell {

    private int currentFirst, currentSecond;
    private String currentThird;
    private boolean currentFourth;
    private String[] saveChangeText;

    private GamePanel gp;

    public WrenchSpell(GamePanel gp, String[] saveChangeText) {
        this.gp = gp;
        setDefaultValues();

        this.isVisible = saveChangeText[0].equals("true");
        this.name = saveChangeText[1];

        this.saveChangeText = new String[saveChangeText.length-1];
        for(int i = 0; i < saveChangeText.length-2; i++) {
            this.saveChangeText[i] = saveChangeText[i+2];
        }


        currentFirst = defaultFirst;
        currentSecond = defaultSecond;
        currentThird = defaultThird;
        currentFourth = defaultFourth;

        currentRechargeTime = 0;
        rechargeTime = (int)(5 * GamePanel.UPDATE_RATE); // 5 секунд
        isRecharge = false;

        posIconX = 232;
        posIconY = 625;
        wordToUse = "R";

        rechargeBackgroundIconSpell = gp.textureAtlas.textures[17][1].getTexture();
        rechargeFrame = gp.textureAtlas.textures[17][2].getTexture();
        rechargeIconSpell = gp.textureAtlas.textures[13][2].getTexture();
    }

    public void setDefaultValues() {

        defaultFirst = 4;
        defaultSecond = 5;
        defaultThird = "key";
        defaultFourth = false;

    }

    public boolean first(String string) {
        if(string.equals("null")) return true;
        if(string.length() < 2) return false;
        char operator = string.charAt(0);
        int num = Integer.parseInt(Character.isDigit(string.charAt(1)) ? string.charAt(1) + "" : "0");


        if(operator != '+' && operator != '-' || num == 0) return false;

        switch (operator) {
            case '+': {
                currentFirst = defaultFirst + num;
                break;
            }
            case '-': {
                currentFirst = defaultFirst - num;
                break;
            }
        }

        return true;
    }

    public boolean second(String string) {
        if(string.equals("null")) return true;
        if(string.length() < 2) return false;
        char operator = string.charAt(0);
        int num = Integer.parseInt(Character.isDigit(string.charAt(1)) ? string.charAt(1) + "" : "0");

        if(operator != '+' && operator != '-' || num == 0) return false;

        switch (operator) {
            case '+': {
                currentSecond = defaultSecond + num;
                break;
            }
            case '-': {
                currentSecond = defaultSecond - num;
                break;
            }
        }

        return true;
    }

    public boolean third(String string) {

        if(string.equals("null")) return true;
        if(string.length() < 2) return false;
        char operator = string.charAt(0);
        char num = (Character.isDigit(string.charAt(1)) || Character.isLetter(string.charAt(1))) ? string.charAt(1) : '0';

        if(operator != '+' || num == '0') return false;

        currentThird = defaultThird + num;

        return true;
    }

    public boolean fourth(String string) {
        if(string.equals("null")) return true;
        if(string.isEmpty()) return false;
        char operator = string.charAt(0);

        if(operator != '!') {
            return false;
        }

        currentFourth = !defaultFourth;

        return true;
    }




    public int getCurrentFirst() {
        return currentFirst;
    }

    public int getCurrentSecond() {
        return currentSecond;
    }

    public String getCurrentThird() {
        return currentThird;
    }

    public boolean getCurrentFourth() {
        return currentFourth;
    }

    public String[] getSaveChangeText() {
        return saveChangeText;
    }

    public boolean getIsRecharge() {
        return isRecharge;
    }
    public void setIsRecharge(boolean recharge) {
        isRecharge = recharge;
        gp.player.setMana(gp.player.getMana() - 10);
    }

    @Override
    public void recharge() {

        if(isRecharge) {
            currentRechargeTime++;
            if(currentRechargeTime == rechargeTime) {
                currentRechargeTime = 0;
                isRecharge = false;
            }
        }

    }

    @Override
    public boolean update(String string, int index) {

        boolean flag = true;

        switch (index) {
            case 1: flag = first(string); break;
            case 2: flag = second(string); break;
            case 3: flag = third(string); break;
            case 4: flag = fourth(string); break;
        }


        return flag;
    }

    @Override
    public void draw(Graphics2D g) {

        g.drawImage(rechargeBackgroundIconSpell, posIconX, posIconY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);
        g.setColor(new Color(255, 255, 255, 100));
        g.fillRect(posIconX+4, posIconY+4, GamePanel.tileSize*2-8, (int)((GamePanel.tileSize*2-8)*((float)currentRechargeTime/rechargeTime)));
        g.drawImage(rechargeIconSpell, posIconX+10, posIconY+10, (int)(GamePanel.tileSize*2.5)-6, (int)(GamePanel.tileSize*2.5)-6, null);
        g.drawImage(rechargeFrame, posIconX, posIconY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);

        g.setFont(my_font.deriveFont(16f));
        g.setColor(new Color(160, 190, 250));
        g.drawString(wordToUse, (int)(posIconX + GamePanel.tileSize*3.0/2)-2, (int)(posIconY+GamePanel.tileSize*3.5/2)-2);

    }

}
