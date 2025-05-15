package main.java.com.magicode;

import main.java.com.magicode.core.GamePanel;

import javax.swing.*;

public class Main {
    public static JFrame f = new JFrame("");
    public static void main(String[] args) {
        GamePanel panel = new GamePanel();


        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setContentPane(panel);

        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setResizable(false);


        panel.setupGame();
        panel.start();


    }

    public static void setTitle(String name) {
        f.setTitle(name);
    }

    public static String getTitle() {
        return f.getTitle();
    }
}
