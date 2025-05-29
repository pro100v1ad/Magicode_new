package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {

    private BufferedImage[] images;
    private BufferedImage currentImage;
    private double fps;
    public int currentFrame;
    public int updatesCount;
    private final int updatesPerFrame;

    public Animation(BufferedImage[] images, double fps) {
        if (images == null || images.length == 0) {
            throw new IllegalArgumentException("Изображение пусто или отсутствует!");
        }
        if (fps == 0) {
            throw new IllegalArgumentException("fps не должен быть 0!");
        }

        this.images = images;
        this.fps = fps;
        this.currentFrame = 0;
        this.currentImage = images[0];

        // Рассчитываем, сколько update() должно пройти между кадрами
        this.updatesPerFrame = (int)(GamePanel.UPDATE_RATE / fps); // UPDATE_RATE - количество вызовов update в секунду
        this.updatesCount = 0;
    }

    public int getUpdatesCount() {
        return updatesCount;
    }

    public void reset() {
        this.currentFrame = 0;
        this.currentImage = images[0];
        this.updatesCount = 0;
    }

    public void update() {
        updatesCount++;
        if(updatesCount >= updatesPerFrame) {
            currentFrame = (currentFrame + 1) % images.length;
            currentImage = images[currentFrame];
            updatesCount = 0;
        }
    }

    public void draw(Graphics2D g, int x, int y, int w, int h) {
        if(currentImage != null) {
            g.drawImage(currentImage, x, y, w, h, null);
        }

    }

}
