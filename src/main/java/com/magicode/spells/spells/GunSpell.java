package main.java.com.magicode.spells.spells;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.spells.Spell;

public class GunSpell extends Spell {

    private int currentDamage, currentReload;
    private String[] saveChangeText;

    private GamePanel gp;

    public GunSpell(GamePanel gp, String[] saveChangeText) {
        this.gp = gp;
        setDefaultValues();

        this.isVisible = saveChangeText[0].equals("true");
        this.name = saveChangeText[1];

        this.saveChangeText = new String[saveChangeText.length-1];
        for(int i = 0; i < saveChangeText.length-2; i++) {
            this.saveChangeText[i] = saveChangeText[i+2];
        }


        currentDamage = defaultFirst;
        currentReload = defaultSecond;


        currentRechargeTime = 0;
        rechargeTime = (int)(5 * GamePanel.UPDATE_RATE); // 5 секунд
        isRecharge = false;

        posIconX = 100;
        posIconY = 625;
        wordToUse = "Space";

        rechargeBackgroundIconSpell = gp.textureAtlas.textures[17][1].getTexture();
        rechargeFrame = gp.textureAtlas.textures[17][2].getTexture();
        rechargeIconSpell = gp.textureAtlas.textures[13][3].getTexture();
    }

    public void setDefaultValues() {

        defaultFirst = 5;
        defaultSecond = 3;


    }

    public boolean first(String string) {
        if(string.equals("null")) return true;
        if(string.length() < 2) return false;
        char operator = string.charAt(0);
        int num = Integer.parseInt(Character.isDigit(string.charAt(1)) ? string.charAt(1) + "" : "0");


        if(operator != '+' && operator != '-' || num == 0) return false;

        switch (operator) {
            case '+': {
                currentDamage = defaultFirst + num;
                break;
            }
            case '-': {
                currentDamage = defaultFirst - num;
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
                currentReload = defaultSecond + num;
                break;
            }
            case '-': {
                currentReload = defaultSecond - num;
                break;
            }
        }

        return true;
    }

    public int getCurrentDamage() {
        return currentDamage;
    }

    public int getCurrentReload() {
        return currentReload;
    }

    public String[] getSaveChangeText() {
        return saveChangeText;
    }

    public boolean getIsRecharge() {
        return isRecharge;
    }
    public void setIsRecharge(boolean recharge) {
        isRecharge = recharge;
        gp.player.setMana(gp.player.getMana() - 5);
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

        }

        return flag;
    }

}
