package main.java.com.magicode.spells.spells;

import main.java.com.magicode.spells.Spell;

import java.util.Arrays;

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

        System.out.println(Arrays.toString(saveChangeText));

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

}
