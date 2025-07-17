package main.java.com.magicode.ui.interface_;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;

public class TextBubble extends GUI {

    private String text;
    private int time;
    private int currentTime;
    private boolean isVisible;

    public TextBubble(String text, int time) {
        this.text = text;
        this.time = time;
        this.currentTime = 0;
        this.isVisible = false;
    }


    public void draw(Graphics2D g) {
        if (!isVisible) return;

        if (currentTime == time) {
            currentTime = 0;
            isVisible = false;
            return;
        } else {
            currentTime++;
        }

        Font font = my_font.deriveFont(24f);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        // Отступы внутри рамки
        int paddingX = 12;  // Увеличенный отступ для лучшего вида
        int paddingY = 6;

        // Размеры рамки
        int boxWidth = textWidth + 2 * paddingX;
        int boxHeight = textHeight + 2 * paddingY;

        // Позиционирование по центру снизу экрана
        int posX = (GamePanel.WIDTH - boxWidth) / 2;  // Центр по горизонтали
        int posY = GamePanel.HEIGHT - 40;             // Отступ снизу (можно регулировать)

        // Рисуем черный прямоугольник (фон) с закругленными углами (если нужно)
        g.setColor(new Color(0, 0, 0, 200));  // Полупрозрачный черный
        g.fillRoundRect(posX, posY - boxHeight, boxWidth, boxHeight, 10, 10);

        // Рисуем белую рамку
        g.setColor(Color.WHITE);
        g.drawRoundRect(posX, posY - boxHeight, boxWidth, boxHeight, 10, 10);

        // Рисуем белый текст, выровненный по центру
        g.setColor(Color.WHITE);
        g.drawString(text, posX + paddingX, posY - paddingY - fm.getDescent());
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        if (isVisible) {
            currentTime = 0;  // Сброс таймера при показе
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setText(String text) {
        this.text = text;
    }
}