package main.java.com.magicode.gameplay.world;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Layer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int firstLayer;
    private int secondLayer;
    private boolean collision = false;
    private String interactionZone;
    private BufferedImage imageFirst;
    private BufferedImage imageSecond;
    private ResourceLoader resourceLoader;

    private GamePanel gp;

    public Layer(GamePanel gp) {
        this.gp = gp;
        resourceLoader = new ResourceLoader();
    }

    public void setLayers(String input) {
        // Разделяем строку по символу '_'
        String[] parts = input.split(":");

        // Проверяем, что строка разделилась на две части
        if (parts.length == 2) {
            // Преобразуем первую часть в число
            firstLayer = Integer.parseInt(parts[0]);

            // Преобразуем вторую часть в число
            secondLayer = Integer.parseInt(parts[1]);



            for(int i = 0; i < GamePanel.whoHaveCollision.length; i++) {
                if(GamePanel.whoHaveCollision[i] != 0 && (GamePanel.whoHaveCollision[i] == firstLayer || GamePanel.whoHaveCollision[i] == secondLayer)) {
                    setCollision(true);
                    break;
                }
            }

        } else {
            System.out.println("Строка не соответствует формату 'число_число'");
        }

        setImage();

    }

    private void setImage() {
        if(firstLayer > 0 && firstLayer < 10) {
            imageFirst = gp.textureAtlas.textures[0][firstLayer-1].getTexture();
        }
        if(firstLayer > 10 && firstLayer < 20) {
            imageFirst = gp.textureAtlas.textures[1][firstLayer-10-1].getTexture();
        }
        if(firstLayer > 20 && firstLayer < 35) {
            imageFirst = gp.textureAtlas.textures[2][firstLayer-20-1].getTexture();
        }
        if(firstLayer > 200 && firstLayer < 300) {
            imageFirst = gp.textureAtlas.textures[20][firstLayer-200-1].getTexture();
        }




        if(secondLayer > 0 && secondLayer < 10) {
            imageSecond = gp.textureAtlas.textures[0][secondLayer-1].getTexture();
        }
        if(secondLayer > 10 && secondLayer < 20) {
            imageSecond = gp.textureAtlas.textures[1][secondLayer-10-1].getTexture();
        }
        if(secondLayer > 20 && secondLayer < 35) {
            imageSecond = gp.textureAtlas.textures[2][secondLayer-20-1].getTexture();
        }
        if(secondLayer > 200 && secondLayer < 300) {
            imageSecond = gp.textureAtlas.textures[20][secondLayer-200-1].getTexture();
        }

    }

    public int getLayer(int number) {
        if(number == 1) return firstLayer;
        else return secondLayer;
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    public boolean getCollision() {
        return collision;
    }

    public void setInteractionZone(String interactionZone) {
        this.interactionZone = interactionZone;
    }
    public String getInteractionZone() {
        return interactionZone;
    }

    public void draw(Graphics2D g, int x, int y, int w, int h) {
        g.drawImage(imageFirst, x, y, w, h, null);
        g.drawImage(imageSecond, x, y, w, h, null);
    }
}
