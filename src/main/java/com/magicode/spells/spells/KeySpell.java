package main.java.com.magicode.spells.spells;

import main.java.com.magicode.spells.Spell;

public class KeySpell extends Spell {

    private int currentFirst, currentSecond;
    private String currentThird;
    private boolean currentFourth;

    public KeySpell() {
        setDefaultValues();

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

    public void update() {

    }

}
