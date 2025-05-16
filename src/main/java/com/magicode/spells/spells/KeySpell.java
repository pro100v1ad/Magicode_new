package main.java.com.magicode.spells.spells;

import main.java.com.magicode.spells.Spell;

public class KeySpell extends Spell {

    private int currentFirst, currentSecond;
    private String currentThird;
    private boolean currentFourth;
    private String[] saveChangeText;

    public KeySpell(String[] saveChangeText) {
        setDefaultValues();

        this.name = saveChangeText[0];

        saveChangeText = new String[saveChangeText.length-1];
        for(int i = 0; i < saveChangeText.length-1; i++) {
            this.saveChangeText[i] = saveChangeText[i+1];
        }

        currentFirst = defaultFirst;
        currentSecond = defaultSecond;
        currentThird = defaultThird;
        currentFourth = defaultFourth;
    }

    public void setDefaultValues() {

        defaultFirst = 4;
        defaultSecond = 5;
        defaultThird = "key";
        defaultFourth = false;

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

    public void update() {

    }

}
