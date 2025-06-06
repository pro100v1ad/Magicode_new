package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.GUI;

import java.awt.*;

public class Button extends GUI { // Класс отвечающий за кнопку

    private String text;
    private Color colorLine;
    private float fontSize;
    private int width, height;
    private boolean click;
    private GamePanel gp;
    private boolean state;
    private int textWidth; // Будем хранить ширину текста
    private int textHeight; // И высоту текста

    private Color textColor;
    private Color backgroundColor;
    private Color staticLineColor;
    private Color passiveLineColor;
    private Color activeLineColor;

    public Button(GamePanel gp, int posX, int posY, String text, float fontSize, boolean state) {
        super();
        this.gp = gp;
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.fontSize = fontSize;
        this.colorLine = Color.white;
        this.state = state;
        this.textColor = Color.WHITE;
        this.passiveLineColor = Color.WHITE;
        this.activeLineColor = Color.YELLOW;

        this.textWidth = (int)(text.length() * fontSize / 1.55);
        this.textHeight = (int)fontSize;

        // Начальные размеры с отступами
        this.width = textWidth + 16;
        this.height = textHeight + 16;

        this.backgroundColor = new Color(0x1AFF00FF, true);
        this.staticLineColor = new Color(0xFF990099, true);
    }


    public void click() {
        click = true;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public String getText() {
        return text;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setStaticLineColor(Color staticLineColor) {
        this.staticLineColor = staticLineColor;
    }

    public boolean update() {
        if(!state) {
            return false;
        }
        int mX = GamePanel.mouseX;
        int mY = GamePanel.mouseY;

        if(mX >= posX && mX <= posX + width && mY >= posY && mY <= posY + height) {
            colorLine = activeLineColor;
            if(click) {
                click = false;
                return true;
            }
        } else {
            colorLine = passiveLineColor;
        }
        click = false;
        return false;
    }


    public void setTextColor(Color textColor){
        this.textColor = textColor;
    }

    public void setPassiveLineColor(Color passiveLineColor){
        this.colorLine = passiveLineColor;
        this.passiveLineColor = passiveLineColor;
    }

    public void setActiveLineColor(Color activeLineColor){
        this.activeLineColor = activeLineColor;
    }

    @Override
    public void draw(Graphics2D g) {
        Font font = my_font.deriveFont(fontSize);
        g.setFont(font);

        // Получаем точные метрики текста
        FontMetrics fm = g.getFontMetrics();
        this.textWidth = fm.stringWidth(text);
        this.textHeight = fm.getHeight();

        // Обновляем размеры кнопки с учетом точных метрик
        this.width = textWidth + 16; // 8px padding с каждой стороны
        this.height = textHeight + 16;

        // Рисуем фон
        g.setColor(backgroundColor);
        g.fillRect(posX, posY, width, height);

        // Рисуем текст
        if(state) {
            g.setColor(textColor);
        } else {
            g.setColor(new Color(88, 88, 88, 255));
        }

        // Центрируем текст вертикально
        int textY = posY + ((height - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, posX + 8, textY);

        // Рисуем линии
        if(state) {
            g.setColor(staticLineColor);
        } else {
            g.setColor(new Color(88, 88, 88, 100));
        }
        g.setStroke(new BasicStroke(3));
        g.drawLine(posX, posY + height, posX + width, posY + height);

        if(state) {
            g.setColor(colorLine);
        } else {
            g.setColor(new Color(88, 88, 88, 100));
        }
        g.drawLine(posX, posY + height + 6, posX + width, posY + height + 6);
    }

}