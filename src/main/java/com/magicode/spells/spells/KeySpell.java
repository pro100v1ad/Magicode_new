package main.java.com.magicode.spells.spells;

import main.java.com.magicode.spells.Spell;

public class KeySpell extends Spell {

    private int currentFirst, currentSecond;
    private String currentThird;
    private boolean currentFourth;
    private String[] saveChangeText;

    public KeySpell(String[] saveChangeText) {
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
    }

    public void setDefaultValues() {

        defaultFirst = 4;
        defaultSecond = 5;
        defaultThird = "key";
        defaultFourth = false;

    }

    public boolean first(String string) {

        char operator = string.charAt(0);

        int num = string.charAt(1);


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

    public void update() {

    }

}
