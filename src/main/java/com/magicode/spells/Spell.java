package main.java.com.magicode.spells;

public abstract class Spell {

    protected int defaultFirst, defaultSecond;
    protected String defaultThird;
    protected boolean defaultFourth;
    protected String name;

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

    public String getName() {
        return name;
    }
}
