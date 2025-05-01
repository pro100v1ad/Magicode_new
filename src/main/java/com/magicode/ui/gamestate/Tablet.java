package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Tablet extends GUI {

    private BufferedImage imageTablet;

    private BufferedImage imageButtonExit;
    private int posButtonExitX, posButtonExitY;

    private Button buttonSave;
    private int posButtonSaveX, posButtonSaveY;


    //Text
    private String[] text;
    private int lineSpace;
    private int fontSize;
    private int countRowsVisible;
    private int textPosX, textPosY;
    private Color textColor;
    private float scrollPosition; // Вместо percent
    // Вместо фиксированной скорости
    private static final float SCROLL_SPEED = 0.05f * GamePanel.UPDATE_RATE_Speed;  // Скорость скролла

    private GamePanel gp;
    private boolean click;

    private List<TextHighlight> textHighlights = new ArrayList<>();

    private static final float SCROLL_SENSITIVITY = 0.5f; // Чувствительность прокрутки
    private static final float MAX_SCROLL_SPEED = 2.0f;   // Максимальная скорость прокрутки


    public Tablet(GamePanel gp) {
        this.gp = gp;

        imageTablet = gp.textureAtlas.textures[15][0].getTexture();
        imageButtonExit = gp.textureAtlas.textures[15][1].getTexture();

        posButtonExitX = 964;
        posButtonExitY = 64;

        posButtonSaveX = 848;
        posButtonSaveY = 608;
        buttonSave = new Button(gp, posButtonSaveX, posButtonSaveY, "Сохранить", 32, true);


        //text
        countRowsVisible = 27;
        text = new String[400 + 1];
        text[0] = "1 Hello WOrld!";
        text[1] = "2 Hi brooo";
        text[2] = "3 GoodBy -_+";
        text[3] = "4 GoodBy -_+";
        text[4] = "5 GoodBy -_+";
        text[5] = "6 GoodBy -_+";
        text[6] = "7 GoodBy -_+";
        text[7] = "8 GoodBy -_+";
        text[8] = "9 GoodBy -_+";
        text[9] = "10 GoodBy -_+";
        text[10] = "11 GoodBy -_+";
        text[11] = "12 GoodBy -_+";
        text[12] = "13 GoodBy -_+";
        text[13] = "14GoodBy -_+";
        text[14] = "15 GoodBy -_+";
        text[15] = "16 GoodBy -_+";
        text[16] = "17 GoodBy -_+";
        text[17] = "18 GoodBy -_+";
        text[18] = "19 GoodBy -_+";
        text[19] = "20 GoodBy -_+";
        text[20] = "21 GoodBy -_+";
        text[21] = "22 GoodBy -_+";
        text[22] = "23 GoodBy -_+";
        text[23] = "24 GoodBy -_+";
        text[24] = "25 GoodBy -_+";
        text[25] = "26 GoodBy -_+";
        text[26] = "27 GoodBy -_+";
        text[27] = "28 GoodBy -_+";
        text[28] = "29 GoodBy -_+";
        text[29] = "30 GoodBy -_+";
        text[30] = "31 GoodBy -_+";
        text[31] = "32 GoodBy -_+";
        text[32] = "33 GoodBy -_+";
        text[33] = "34 GoodBy -_+";
        text[34] = "35 GoodBy -_+";
        text[35] = "36 GoodBy -_+";
        text[36] = "37 GoodBy -_+";
        text[37] = "38 GoodBy -_+";
        text[38] = "39 GoodBy -_+";
        text[39] = "40 GoodBy -_+";
        fontSize = 15;
        lineSpace = (int)(fontSize*1.5);
        textPosX = 336 + fontSize;
        textPosY = 56 + (int)(fontSize*2.2);
        textColor = Color.WHITE;

        addTextHighlight(2, 3, 5, Color.RED);

        // Подсветить первые 2 символа в строке 5 синим цветом
        addTextHighlight(5, 4, 2, Color.BLUE);

        // Очистить все подсветки
        clearTextHighlights();

    }

    public void click() {
        click = true;
    }


    // Класс для хранения информации о подсветке
    private static class TextHighlight {
        int line;     // Номер строки
        int start;    // Начальный индекс символа
        int length;   // Длина подсвечиваемого текста
        Color color;  // Цвет подсветки

        TextHighlight(int line, int start, int length, Color color) {
            this.line = line;
            this.start = start;
            this.length = length;
            this.color = color;
        }
    }

    // Метод для добавления подсветки
    public void addTextHighlight(int line, int start, int length, Color color) {
        if (line >= 0 && line < text.length && text[line] != null) {
            start = Math.min(start, text[line].length());
            length = Math.min(length, text[line].length() - start);
            textHighlights.add(new TextHighlight(line, start, length, color));
        }
    }

    // Метод для очистки всех подсветок
    public void clearTextHighlights() {
        textHighlights.clear();
    }

    private void drawText(Graphics2D g) {
        g.setColor(textColor);
        g.setFont(my_font.deriveFont((float)fontSize));

        TextDrawingContext context = calculateTextPositionContext();
        drawVisibleTextLines(g, context);
        drawRectangleMarker(g, context);
        drawScrollIndicators(g);
    }

    private TextDrawingContext calculateTextPositionContext() {
        TextDrawingContext context = new TextDrawingContext();
        int totalRows = text.length;

        if (totalRows <= countRowsVisible) {
            context.startRow = 0;
            context.verticalOffset = 0;
        } else {
            float maxScroll = totalRows - countRowsVisible;
            float rowOffset = scrollPosition % 1.0f; // Дробная часть

            context.startRow = (int)scrollPosition;
            context.verticalOffset = (int)(-lineSpace * rowOffset);
        }

        return context;
    }

    // Новый метод для отрисовки прямоугольника
    private void drawRectangleMarker(Graphics2D g, TextDrawingContext context) {
        // Параметры прямоугольника (можно вынести в поля класса)
        int markerLine = 2;       // Какая строка текста (индекс в массиве text[])
        int markerPos = 3;        // Позиция в строке (примерно 3-й символ)
        int markerWidth = 5;      // Ширина в символах
        int markerHeight = lineSpace; // Высота как у строки

        // Проверяем, видна ли строка с маркером
        if (markerLine >= context.startRow && markerLine < context.startRow + countRowsVisible) {
            // Вычисляем позицию прямоугольника
            int x = textPosX + g.getFontMetrics().charWidth('A') * markerPos; // Примерная ширина символа
            int y = textPosY + context.verticalOffset + (markerLine - context.startRow) * lineSpace;

            // Рисуем прямоугольник
            g.setColor(new Color(255, 0, 0, 100)); // Полупрозрачный красный
            g.fillRect(x, y - g.getFontMetrics().getAscent(),
                    g.getFontMetrics().charWidth('A') * markerWidth,
                    markerHeight);
        }
    }

    private void drawVisibleTextLines(Graphics2D g, TextDrawingContext context) {
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i < countRowsVisible; i++) {
            int textIndex = context.startRow + i;
            if (textIndex >= text.length || text[textIndex] == null) continue;

            int yPos = textPosY + context.verticalOffset + i * lineSpace;
            String line = text[textIndex];

            // Получаем подсветки для текущей строки
            List<TextHighlight> lineHighlights = textHighlights.stream()
                    .filter(h -> h.line == textIndex)
                    .sorted(Comparator.comparingInt(h -> h.start))
                    .collect(Collectors.toList());

            if (lineHighlights.isEmpty()) {
                // Нет подсветок - рисуем строку целиком
                g.setColor(textColor);
                g.drawString(line, textPosX, yPos);
            } else {
                // Рисуем строку по частям с подсветками
                int xPos = textPosX;
                int currentPos = 0;

                for (TextHighlight hl : lineHighlights) {
                    // Текст до подсветки
                    if (currentPos < hl.start) {
                        String part = line.substring(currentPos, hl.start);
                        g.setColor(textColor);
                        g.drawString(part, xPos, yPos);
                        xPos += fm.stringWidth(part);
                        currentPos = hl.start;
                    }

                    // Подсвеченный текст
                    if (currentPos == hl.start) {
                        int end = Math.min(hl.start + hl.length, line.length());
                        String part = line.substring(hl.start, end);
                        g.setColor(hl.color);
                        g.drawString(part, xPos, yPos);
                        xPos += fm.stringWidth(part);
                        currentPos = end;
                    }
                }

                // Остаток строки после последней подсветки
                if (currentPos < line.length()) {
                    g.setColor(textColor);
                    g.drawString(line.substring(currentPos), xPos, yPos);
                }
            }
        }
    }

    private void drawScrollIndicators(Graphics2D g) {
        // Верхний индикатор скролла
        g.setColor(new Color(30, 31, 34));
        g.fillRect(350, 54, 300, fontSize + 2);

        g.setColor(new Color(49, 52, 56));
        g.drawLine(350, 56 + fontSize, 651, 56 + fontSize);

        // Нижний индикатор скролла
        if (text.length > countRowsVisible) {
            g.setColor(new Color(30, 31, 34));
            g.fillRect(350, 48 + 600, 300, fontSize + 8);
        }
        g.setColor(new Color(49, 52, 56));
        g.drawLine(350, 48 + 600, 651, 48 + 600);
    }

    // Вспомогательный класс для передачи данных о позиционировании
    private static class TextDrawingContext {
        int startRow;
        int verticalOffset;
    }

    public void handleMouseWheel(int wheelRotation) {
        if (text.length <= countRowsVisible) return; // Прокрутка не нужна

        // Фиксированный шаг прокрутки (не зависящий от количества строк)
        float scrollStep = wheelRotation * SCROLL_SENSITIVITY;

        // Применяем шаг к текущей позиции
        scrollPosition -= scrollStep;

        // Ограничение диапазона
        float maxScroll = text.length - countRowsVisible;
        scrollPosition = Math.max(0, Math.min(scrollPosition, maxScroll));
    }

    /**
     * Получает текущую позицию прокрутки в процентах (0-100)
     * @return процент прокрутки (0 - начало, 100 - конец)
     */
    public float getScrollPercent() {
        if (text.length <= countRowsVisible) {
            return 0; // Весь текст виден, прокрутки нет
        }
        float maxScroll = text.length - countRowsVisible;
        return (scrollPosition / maxScroll) * 100f;
    }

    /**
     * Устанавливает позицию прокрутки в процентах
     * @param percent значение от 0 до 100
     */
    public void setScrollPercent(float percent) {
        percent = Math.max(0, Math.min(100, percent)); // Ограничиваем 0-100
        if (text.length > countRowsVisible) {
            float maxScroll = text.length - countRowsVisible;
            scrollPosition = (percent / 100f) * maxScroll;
        } else {
            scrollPosition = 0;
        }
    }

    public void update() {

        int mX = GamePanel.mouseX;
        int mY = GamePanel.mouseY;

        if(mX >= posButtonExitX && mX <= posButtonExitX + GamePanel.tileSize*2
                && mY >= posButtonExitY && mY <= posButtonExitY + GamePanel.tileSize*2) {
            if(click) {
                click = false;
                gp.state = GamePanel.GameState.Game;
            }
            imageButtonExit = gp.textureAtlas.textures[15][2].getTexture();
        } else {
            imageButtonExit = gp.textureAtlas.textures[15][1].getTexture();
        }

        if(buttonSave.update()) {
            System.out.println("Изменения сохранены!");
            gp.state = GamePanel.GameState.Game;
        }
        click = false;

        // Ограничиваем позицию прокрутки
        if (text.length > countRowsVisible) {
            scrollPosition = Math.max(0, Math.min(scrollPosition, text.length - countRowsVisible));
        } else {
            scrollPosition = 0;
        }

//        float currentScroll = getScrollPercent();
//        System.out.println("Прокручено: " + currentScroll + "%");

    }

    public void draw(Graphics2D g) {

        g.drawImage(imageTablet, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);

        g.drawImage(imageButtonExit, posButtonExitX, posButtonExitY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);

        buttonSave.draw(g);

        drawText(g);
        drawSlider(g, getScrollPercent());

    }

    public void drawSlider(Graphics2D g, float percent) {

        int distanse = 524;


        g.setColor(Color.YELLOW);

        g.fillRect(791, 86 + (int)(distanse*percent/100), 12, 32);

    }

}
