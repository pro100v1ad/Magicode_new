package main.java.com.magicode.core.utils;

import java.awt.image.BufferedImage;

public class Texture { // Вспомогательный класс для создания текстур

    private BufferedImage texture = null;
    ResourceLoader rs = new ResourceLoader();
    public void loadTexture(String PATH) {
        texture = rs.loadImage(PATH);
    }
    public BufferedImage getTexture() {
        return texture;
    }

}
