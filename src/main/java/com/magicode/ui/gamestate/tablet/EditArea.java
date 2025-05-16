package main.java.com.magicode.ui.gamestate.tablet;

import main.java.com.magicode.ui.GUI;
import main.java.com.magicode.ui.gamestate.Tablet;

import java.awt.*;

public class EditArea extends GUI {

    private String name;
    private int markerLine, markerPos, markerWidth, markerHeight;

    private boolean isEditing = false;
    private String currentText = "";
    private long lastCursorBlink;
    private boolean cursorVisible = true;

    private Color color;

    public EditArea(int markerLine, int markerPos, int markerWidth, String name) {
        this.name = name;
        this.markerLine = markerLine;// Какая строка текста (индекс в массиве text[])
        this.markerPos = markerPos;// Позиция в строке (примерно 3-й символ)
        this.markerWidth = markerWidth;// Ширина в символах
        this.markerHeight = Tablet.getLineSpace();// Высота как у строки

        this.color = Color.GREEN;

    }

    public void startEditing() {
        this.isEditing = true;
        this.lastCursorBlink = System.currentTimeMillis();
        this.cursorVisible = true;
    }

    public void stopEditing() {
        this.isEditing = false;
        // Здесь можно сохранить изменения в основной текст
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void appendChar(char c) {
        // Проверяем, не превышает ли текст максимальную длину
        if (currentText.length() < markerWidth) {
            currentText += c;
        }
    }

    public void backspace() {
        if (!currentText.isEmpty()) {
            currentText = currentText.substring(0, currentText.length() - 1);
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY, int startRow, int verticalOffset, FontMetrics metrics) {
        if (markerLine >= startRow && markerLine < startRow + Tablet.getCountRowsVisible() - 1) {
            // Вычисляем координаты области
            int x = Tablet.getTextPosX() + metrics.charWidth('A') * markerPos - 28;
            int y = Tablet.getTextPosY() + verticalOffset +
                    (markerLine - startRow) * Tablet.getLineSpace() - metrics.getAscent() - 2;

            int width = metrics.charWidth('A') * markerWidth;
            int height = markerHeight;

            // Проверяем, находится ли курсор внутри прямоугольника
            return mouseX >= x && mouseX <= x + width &&
                    mouseY >= y && mouseY <= y + height;
        }
        return false;
    }

    public void update() {
// Мигание курсора
        if (isEditing && System.currentTimeMillis() - lastCursorBlink > 500) {
            cursorVisible = !cursorVisible;
            lastCursorBlink = System.currentTimeMillis();
        }
    }

    public void draw(Graphics2D g, int startRow, int verticalOffset, boolean isHovered) {
        // Проверяем, видна ли строка с маркером
        if (markerLine >= startRow && markerLine < startRow + Tablet.getCountRowsVisible() - 1) {
            // Вычисляем позицию прямоугольника
            int x = Tablet.getTextPosX() + g.getFontMetrics().charWidth('A') * markerPos;
            int y = Tablet.getTextPosY() + verticalOffset +
                    (markerLine - startRow) * Tablet.getLineSpace();

            // Рисуем прямоугольник
            g.setColor(new Color(255, 255, 0, 60)); // Полупрозрачный желтый
            g.fillRect(x + 2, y - g.getFontMetrics().getAscent() - 2,
                    g.getFontMetrics().charWidth('A') * markerWidth,
                    markerHeight);

            // Если наведен курсор, рисуем красную рамку
            if (isHovered) {
                g.setColor(Color.YELLOW);
                g.setStroke(new BasicStroke(2)); // Толщина рамки
                g.drawRect(x + 2, y - g.getFontMetrics().getAscent() - 2,
                        g.getFontMetrics().charWidth('A') * markerWidth,
                        markerHeight);
            }

            // Рисуем текст

            g.setColor(color);

            g.setFont(my_font.deriveFont(15.0f));
            String textToShow = isEditing ? currentText : getOriginalText();
            g.drawString(textToShow, x + 4, y);

            // Рисуем курсор, если область в режиме редактирования
            if (isEditing && cursorVisible) {
                int cursorX = x + 4 + g.getFontMetrics().stringWidth(textToShow);
                g.drawLine(cursorX, y - g.getFontMetrics().getAscent(),
                        cursorX, y);
            }
        }


    }

    private String getOriginalText() {
        // Здесь нужно получить оригинальный текст из Tablet.text
        // Это зависит от вашей реализации хранения текста
        return currentText; // временная заглушка
    }

    public String getName() {
        return name;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String string) {
        this.currentText = string;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
