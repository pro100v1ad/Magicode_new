package main.java.com.magicode.ui;

import java.awt.*;
import java.io.InputStream;

public class GUI {

    protected int posX;
    protected int posY;
    protected Font my_font;


    public GUI() {

        try {
            InputStream is = getClass().getResourceAsStream("/resources/font/my_font.ttf");
            if (is != null) {
                my_font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(32f); // Регистрируем и задаём размер
            }
            else {
                my_font = new Font("Arial", Font.PLAIN, 24); // Шрифт по умолчанию
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки шрифта: " + e.getMessage());
            my_font = new Font("Arial", Font.PLAIN, 24); // Шрифт по умолчанию
        }

    }

    public void draw(Graphics2D g) {

    }

}
