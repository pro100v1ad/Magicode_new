package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.objects.Key;
import main.java.com.magicode.spells.Spell;
import main.java.com.magicode.spells.spells.GunSpell;
import main.java.com.magicode.spells.spells.KeySpell;
import main.java.com.magicode.spells.spells.WrenchSpell;
import main.java.com.magicode.ui.GUI;
import main.java.com.magicode.ui.gamestate.tablet.EditArea;
import main.java.com.magicode.ui.gamestate.tablet.TextRedactor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Tablet extends GUI {

    private BufferedImage imageTablet;

    private BufferedImage imageButtonExit;
    private int posButtonExitX, posButtonExitY;

    private Button buttonSave;
    private int posButtonSaveX, posButtonSaveY;

    private EditArea hoveredEditArea = null;
    private EditArea activeEditArea = null;
    //Text
    private String[] text;
    private static int lineSpace;
    private int fontSize;
    private static int countRowsVisible;
    private static int textPosX, textPosY;
    private Color textColor;
    private float scrollPosition; // Вместо percent
    // Вместо фиксированной скорости
    private static final float SCROLL_SPEED = 0.05f * GamePanel.UPDATE_RATE_Speed;  // Скорость скролла

    private GamePanel gp;
    private boolean click;

    private static final float SCROLL_SENSITIVITY = 0.5f; // Чувствительность прокрутки
    private static final float MAX_SCROLL_SPEED = 2.0f;   // Максимальная скорость прокрутки

    private TextRedactor textRedactor;
    private EditArea[] editAreas;

    private Button[] buttons;
    private int countButton;

    private int N;
    private int plus;
    private int minus;
    private int exclamationMark;

    private int currentN;
    private int currentPlus;
    private int currentMinus;
    private int currentExclamationMark;

    public Tablet(GamePanel gp, String filePath) {
        this.gp = gp;

        imageTablet = gp.textureAtlas.textures[15][0].getTexture();
        imageButtonExit = gp.textureAtlas.textures[15][1].getTexture();

        posButtonExitX = 964;
        posButtonExitY = 64;

        posButtonSaveX = 848;
        posButtonSaveY = 608;
        buttonSave = new Button(gp, posButtonSaveX, posButtonSaveY, "Сохранить", 32, true);

        setDefaultValues();

        //text
        countRowsVisible = 27;
        fontSize = 15;
        lineSpace = (int)(fontSize*1.5);
        textPosX = 336 + fontSize;
        textPosY = 56 + (int)(fontSize*2.2);
        textColor = Color.WHITE;


        //Левая часть планшета

        buttons = new Button[5];
        countButton = 1;
        buttons[0] = new Button(gp, 128, 96, "Spells", 24, false);
        buttons[0].setBackgroundColor(new Color(0, 0, 0, 0));
        buttons[0].setStaticLineColor(new Color(88, 88, 88));

        textRedactor = new TextRedactor(this, filePath);
        editAreas = textRedactor.getEditAreas();
//        textRedactor.addSpell("/resources/spells/gun");
    }

    public void addButton(String name) {
        buttons[countButton] = new Button(gp, 160, 96 + countButton*64, name, 24, false);
        buttons[countButton].setBackgroundColor(new Color(0, 0, 0, 0));
        buttons[countButton++].setStaticLineColor(new Color(88, 88, 88));
    }

    public void setDefaultValues() {

        N = 0;
        plus = 0;
        minus = 0;
        exclamationMark = 0;

        currentN = N;
        currentPlus = plus;
        currentMinus = minus;
        currentExclamationMark = exclamationMark;

    }

    public void setN(int N) {
        this.N = N;
    }

    public void setMinus(int minus) {
        this.minus = minus;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public void setExclamationMark(int exclamationMark) {
        this.exclamationMark = exclamationMark;
    }

    public String[] getText() {
        if (text == null) {
            return null;
        }
        return Arrays.copyOf(text, text.length);
    }

    public void setText(String[] text) {
        this.text = text != null ? Arrays.copyOf(text, text.length) : null;
    }

    public EditArea[] getEditAreas() {
        return editAreas;
    }

    public TextRedactor getTextRedactor() {
        return textRedactor;
    }

    public int getN() {
        return N;
    }

    public int getPlus() {
        return plus;
    }

    public int getMinus() {
        return minus;
    }

    public int getExclamationMark() {
        return exclamationMark;
    }

    public static int getLineSpace() {
        return lineSpace;
    }

    public static int getCountRowsVisible() {
        return countRowsVisible;
    }

    public static int getTextPosX() {
        return textPosX;
    }

    public static int getTextPosY() {
        return textPosY;
    }

    public void click() {
        click = true;
    }

    private void drawText(Graphics2D g) {
        g.setColor(textColor);
        g.setFont(my_font.deriveFont((float)fontSize));

        TextDrawingContext context = calculateTextPositionContext();
        drawVisibleTextLines(g, context);
        drawScrollIndicators(g);
        for (EditArea editArea : editAreas) {
            if (editArea != null) {
                // Передаем информацию о том, является ли эта область текущей hovered
                editArea.draw(g, context.startRow, context.verticalOffset,
                        editArea == hoveredEditArea);
            }
        }
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

    private void drawVisibleTextLines(Graphics2D g, TextDrawingContext context) {
        for (int i = 0; i < countRowsVisible; i++) {
            int textIndex = context.startRow + i;
            if (textIndex < text.length && text[textIndex] != null) {
                int yPos = textPosY + context.verticalOffset + i * lineSpace;
                g.drawString(text[textIndex], textPosX, yPos);
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
        float scrollStep = -wheelRotation * SCROLL_SENSITIVITY;

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

    private void handleTextInput() {
        char[] chars = gp.getTypedChars();
        for (char c : chars) {
            if (c == '\b') {
                activeEditArea.backspace();
            }
            else if (c == '\n') {
                activeEditArea.stopEditing();
                activeEditArea = null;
                gp.listeners.setShouldCaptureInput(false);
            }
            else {
                activeEditArea.appendChar(c);
            }
        }
    }

    public void click2() {
        if (hoveredEditArea != null) {
            if (activeEditArea != null) {
                activeEditArea.stopEditing();
            }
            activeEditArea = hoveredEditArea;
            activeEditArea.startEditing();
            gp.listeners.setShouldCaptureInput(true);
        }
        else if (activeEditArea != null) {
            activeEditArea.stopEditing();
            activeEditArea = null;
            gp.listeners.setShouldCaptureInput(false);
        }
    }

    public void loadSaveValues(String filePath) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл данных планшета, пуст!");
                return;// Если файл закончился раньше, чем ожидалось
            }

            String[] parts = line.split("_");
            N = Integer.parseInt(parts[0]);
            plus = Integer.parseInt(parts[1]);
            minus = Integer.parseInt(parts[2]);
            exclamationMark = Integer.parseInt(parts[3]);

            //Загрузка сохраненных данных, что ввел игрок.
            int count = Integer.parseInt(reader.readLine());

            for(int i = 0; i < count; i++) {
                // key_1_+1 - формат загрузки
                line = reader.readLine();
                if (line == null) continue;
                parts = line.split("_");
                if(parts[0].equals("key")) {
                    for(EditArea editArea : editAreas) {
                        if(editArea == null) continue;
                        if(editArea.getName().contains("key")
                                && (editArea.getName().charAt(editArea.getName().length() - 1) + "").equals(parts[1])) {
                            if(parts[2].equals("null")) {
                                editArea.setCurrentText("");
                            } else {
                                editArea.setCurrentText(parts[2]);
                            }

                        }
                    }
                }

                if(parts[0].equals("wrench")) {
                    for(EditArea editArea : editAreas) {
                        if(editArea == null) continue;
                        if(editArea.getName().contains("wrench")
                                && (editArea.getName().charAt(editArea.getName().length() - 1) + "").equals(parts[1])) {
                            if(parts[2].equals("null")) {
                                editArea.setCurrentText("");
                            } else {
                                editArea.setCurrentText(parts[2]);
                            }

                        }
                    }
                }

                if(parts[0].equals("gun")) {
                    for(EditArea editArea : editAreas) {
                        if(editArea == null) continue;
                        if(editArea.getName().contains("gun")
                                && (editArea.getName().charAt(editArea.getName().length() - 1) + "").equals(parts[1])) {
                            if(parts[2].equals("null")) {
                                editArea.setCurrentText("");
                            } else {
                                editArea.setCurrentText(parts[2]);
                            }

                        }
                    }
                }
                // Тута остальные заклинания
            }

        } catch (Exception e) {
            System.out.println("Ошибка загрузки данных в планшете!");
        }
    }

    public void update() {

        int mX = GamePanel.mouseX;
        int mY = GamePanel.mouseY;

        if(GamePanel.keys[6]) {
            click = false;
            gp.state = GamePanel.GameState.Game;
            GamePanel.keys[6] = false;
        }

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

        for(EditArea editArea: editAreas) {
            if (editArea != null) {
                editArea.update();
            }

        }

        // Включаем/выключаем захват ввода в зависимости от наличия активной области
        gp.listeners.setShouldCaptureInput(activeEditArea != null);

        // Обработка текстового ввода
        if (activeEditArea != null && activeEditArea.isEditing()) {
            handleTextInput();
        }

        hoveredEditArea = null;

        TextDrawingContext context = calculateTextPositionContext();
        FontMetrics metrics = gp.getGraphics().getFontMetrics(my_font.deriveFont((float)fontSize));

        for (EditArea editArea : editAreas) {
            if (editArea != null &&
                    editArea.isMouseOver(mX, mY, context.startRow, context.verticalOffset, metrics)) {
                hoveredEditArea = editArea;
                break;
            }
        }

        updateLeftPartsTablet();


        // Обработка логики заклинаний.
        int curN = N;
        int curPlus = plus;
        int curMinus = minus;
        int curExclamationMark = exclamationMark;

        for (EditArea editArea : editAreas) {
            if(editArea != null) {
                if(editArea.getName().contains("key")) {
                    Spell[] spells = gp.player.getSpells();
                    if(spells != null) {
                        for (Spell spell : spells) {
                            if(spell != null) {
                                if (spell.getName().equals("key")) {
                                    KeySpell keySpell = (KeySpell) spell;
                                    if (keySpell.update(editArea.getCurrentText(),
                                            Integer.parseInt(editArea.getName().charAt(editArea.getName().length() - 1) + ""))) {


                                        if(editArea.getCurrentText().charAt(0) == '+') {
                                            curPlus--;
                                        }
                                        if(editArea.getCurrentText().charAt(0) == '-') {
                                            curMinus--;
                                        }
                                        if(editArea.getCurrentText().charAt(0) == '!') {
                                            curExclamationMark--;
                                        }

                                        if(curPlus >= 0 && curMinus >= 0) {
                                            editArea.setColor(Color.GREEN);
                                            keySpell.setState(true);
                                        }
                                        else {
                                            editArea.setColor(Color.RED);
                                            keySpell.setState(false);
                                        }

                                    } else {
                                        editArea.setColor(Color.RED);
                                    }

                                }

                            }
                        }
                    }
                }

                if(editArea.getName().contains("wrench")) {
                    Spell[] spells = gp.player.getSpells();
                    if(spells != null) {
                        for(Spell spell: spells) {
                            if(spell != null) {
                                if (spell.getName().equals("wrench")) {
                                    WrenchSpell wrenchSpell = (WrenchSpell) spell;
                                    if (wrenchSpell.update(editArea.getCurrentText(),
                                            Integer.parseInt(editArea.getName().charAt(editArea.getName().length() - 1) + ""))) {


                                        if(editArea.getCurrentText().charAt(0) == '+') {
                                            curPlus--;
                                        }
                                        if(editArea.getCurrentText().charAt(0) == '-') {
                                            curMinus--;
                                        }
                                        if(editArea.getCurrentText().charAt(0) == '!') {
                                            curExclamationMark--;
                                        }

                                        if(curPlus >= 0 && curMinus >= 0) {
                                            editArea.setColor(Color.GREEN);
                                            wrenchSpell.setState(true);
                                        }
                                        else {
                                            editArea.setColor(Color.RED);
                                            wrenchSpell.setState(false);
                                        }
                                    } else {
                                        editArea.setColor(Color.RED);
                                    }

                                }
                            }
                        }
                    }
                }

                if(editArea.getName().contains("gun")) {
                    Spell[] spells = gp.player.getSpells();
                    if(spells != null) {
                        for(Spell spell: spells) {
                            if(spell != null) {
                                if (spell.getName().equals("gun")) {
                                    GunSpell gunSpell = (GunSpell) spell;
                                    if (gunSpell.update(editArea.getCurrentText(),
                                            Integer.parseInt(editArea.getName().charAt(editArea.getName().length() - 1) + ""))) {

                                        if(Character.isDigit(editArea.getCurrentText().charAt(1))) {
                                            curN -= Integer.parseInt(editArea.getCurrentText().charAt(1) + "");
                                        }
                                        if(editArea.getCurrentText().charAt(0) == '+') {
                                            curPlus--;
                                        }
                                        if(editArea.getCurrentText().charAt(0) == '-') {
                                            curMinus--;
                                        }
                                        if(editArea.getCurrentText().charAt(0) == '!') {
                                            curExclamationMark--;
                                        }

                                        if(curN >= 0) {
                                            editArea.setColor(Color.GREEN);
                                            gunSpell.setState(true);
                                        }
                                        else {
                                            editArea.setColor(Color.RED);
                                            gunSpell.setState(false);
                                        }
                                    } else {
                                        editArea.setColor(Color.RED);
                                    }

                                }
                            }
                        }
                    }
                }

            }
        }

        currentN = curN;
        currentPlus = curPlus;
        currentMinus = curMinus;
        currentExclamationMark = curExclamationMark;

    }

    public void updateLeftPartsTablet() {
        for(Button button: buttons) {
            if(button != null) {
                button.update();
            }

        }
    }

    public void draw(Graphics2D g) {

        g.drawImage(imageTablet, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);

        g.drawImage(imageButtonExit, posButtonExitX, posButtonExitY, GamePanel.tileSize*2, GamePanel.tileSize*2, null);

        buttonSave.draw(g);

        drawText(g);
        drawSlider(g, getScrollPercent());

        drawRightPartsTablet(g);
        drawLeftPartsTablet(g);

    }

    public void drawSlider(Graphics2D g, float percent) {

        int distanse = 524;

        g.setColor(Color.YELLOW);

        g.fillRect(791, 86 + (int)(distanse*percent/100), 12, 32);



    }

    public void drawRightPartsTablet(Graphics2D g) {

        int N = this.currentN;
        int plus = this.currentPlus;
        int minus = this.currentMinus;
        int exclamationMark = this.currentExclamationMark;

        g.setColor(Color.WHITE);
        g.setFont(my_font.deriveFont(32f));

        g.drawString("N: ", 864, 256);
        if(N == 0) {
            g.setColor(Color.YELLOW);
            g.drawString(N + "", 900, 256);
        } else if(N > 0) {
            g.setColor(Color.GREEN);
            g.drawString(N + "", 900, 256);
        } else {
            g.setColor(Color.RED);
            g.drawString(N + "", 900, 256);
        }
        g.setColor(Color.white);


        g.drawString("+: ", 864, 333);
        if(plus == 0) {
            g.setColor(Color.YELLOW);
            g.drawString(plus + "", 900, 333);
        } else if(plus > 0) {
            g.setColor(Color.GREEN);
            g.drawString(plus + "", 900, 333);
        } else {
            g.setColor(Color.RED);
            g.drawString(plus + "", 900, 333);
        }
        g.setColor(Color.white);


        g.drawString("-: ", 864, 410);
        if(minus == 0) {
            g.setColor(Color.YELLOW);
            g.drawString(minus + "", 900, 410);
        } else if(minus > 0) {
            g.setColor(Color.GREEN);
            g.drawString(minus + "", 900, 410);
        } else {
            g.setColor(Color.RED);
            g.drawString(minus + "", 900, 410);
        }
        g.setColor(Color.white);


        g.drawString("!: ", 864, 487);
        if(exclamationMark == 0) {
            g.setColor(Color.YELLOW);
            g.drawString(exclamationMark + "", 900, 487);
        } else if(exclamationMark > 0) {
            g.setColor(Color.GREEN);
            g.drawString(exclamationMark + "", 900, 487);
        } else {
            g.setColor(Color.RED);
            g.drawString(exclamationMark + "", 900, 487);
        }
        g.setColor(Color.white);



    }

    public void drawLeftPartsTablet(Graphics2D g) {
        for(Button button: buttons) {
            if(button != null) {
                button.draw(g);
            }

        }
    }

}
