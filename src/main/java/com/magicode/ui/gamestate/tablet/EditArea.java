package main.java.com.magicode.ui.gamestate.tablet;

import main.java.com.magicode.ui.gamestate.Tablet;

import java.awt.*;

public class EditArea {

    private String name;
    private int markerLine, markerPos, markerWidth, markerHeight;
    private boolean isEdit;

    public EditArea(int markerLine, int markerPos, int markerWidth, String name) {
        this.name = name;
        this.markerLine = markerLine;// Какая строка текста (индекс в массиве text[])
        this.markerPos = markerPos;// Позиция в строке (примерно 3-й символ)
        this.markerWidth = markerWidth;// Ширина в символах
        this.markerHeight = Tablet.getLineSpace();// Высота как у строки

    }

    public void update() {

    }

    public void draw(Graphics2D g, int startRow, int verticalOffset) {
        // Проверяем, видна ли строка с маркером
        if (markerLine >= startRow && markerLine < startRow + Tablet.getCountRowsVisible() - 1) {
            // Вычисляем позицию прямоугольника
            int x = Tablet.getTextPosX() + g.getFontMetrics().charWidth('A') * markerPos; // Примерная ширина символа
            int y = Tablet.getTextPosY() + verticalOffset + (markerLine - startRow) * Tablet.getLineSpace();

            // Рисуем прямоугольник
            g.setColor(new Color(255, 255, 0, 100)); // Полупрозрачный красный
            g.fillRect(x + 2, y - g.getFontMetrics().getAscent() - 2,
                    g.getFontMetrics().charWidth('A') * markerWidth,
                    markerHeight);
        }
    }

}
