package main.java.com.magicode.core.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {

    public BufferedImage loadImage(String path) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            if (inputStream == null) {
                System.out.println("Ошибка: изображение не найдено! " + path);
                return null;
            }
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}