package main.java.com.magicode.ui.interface_;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;

public class Hints extends GUI {

    private String text;
    private boolean isVisible;

    public Hints(String text) {
        this.text = text;
        this.isVisible = false;
    }

    public void draw(Graphics2D g) {
        if (!isVisible) return;
        Font font = my_font.deriveFont(16f);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();


        int paddingX = 20;
        int paddingY = 12;

        int boxWidth = textWidth + 2 * paddingX;
        int boxHeight = textHeight + 2 * paddingY;

        int posX = GamePanel.WIDTH - boxWidth - 20;  // 20px от правого края
        int posY = GamePanel.HEIGHT - boxHeight - 20; // 20px от нижнего края

        // Градиентный фон
        GradientPaint gradient = new GradientPaint(
                posX, posY, new Color(30, 30, 40, 220),
                posX, posY + boxHeight, new Color(60, 60, 80, 220)
        );
        g.setPaint(gradient);
        g.fillRoundRect(posX, posY, boxWidth, boxHeight, 15, 15);

        // Тонкая рамка с эффектом свечения
        g.setColor(new Color(150, 180, 255, 150));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(posX, posY, boxWidth, boxHeight, 15, 15);

        // Тень для текста
        g.setColor(new Color(0, 0, 0, 150));
        g.drawString(text, posX + paddingX + 1, posY + paddingY + fm.getAscent() + 1);

        // Основной текст
        g.setColor(new Color(240, 240, 255));
        g.drawString(text, posX + paddingX, posY + paddingY + fm.getAscent());

        // Декоративный элемент в углу
        g.setColor(new Color(150, 180, 255, 100));
        g.fillOval(posX + boxWidth - 10, posY + boxHeight - 10, 20, 20);
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setText(String text) {
        this.text = text;
    }
}