package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.GameObject;
import main.java.com.magicode.gameplay.world.Layer;
import main.java.com.magicode.gameplay.world.Structure;

import java.awt.*;

public class Interaction { // Класс отвечающий за взаимодействие с объектами

    private int[][] interactionMap;
    private GamePanel gp;
    private int mapX, mapY;

    public Interaction(GamePanel gp, int mapX, int mapY) {
        this.gp = gp;
        this.mapX = mapX;
        this.mapY = mapY;
        interactionMap = new int[mapY][mapX];

        for(int i = 0; i < mapY; i++) {
            for(int j = 0; j < mapX; j++) {
                interactionMap[i][j] = 0;
            }
        }

    }

    public void reloadMap(Structure[] structures, GameObject[] objects) {
        for(int i = 0; i < mapY; i++) {
            for(int j = 0; j < mapX; j++) {
                interactionMap[i][j] = 0;
            }
        }
        loadStructure(structures);
        loadObjects(objects);
    }

    public void loadObjects(GameObject[] objects) {

        if(objects == null) {
            return;
        }
        for(int i = 0; i < objects.length; i++) {

            if(objects[i] != null) {
                if(objects[i].getCode() > 1) {
                    fillCircleOptimized(objects[i].getPosX() + objects[i].getWight()/2,
                            objects[i].getPosY() + objects[i].getHeight()/2, objects[i].getRadius(), objects[i].getCode());
                }
            }

        }

    }


    public void loadStructure(Structure[] structures) {
        if(structures == null) {
            return;
        }
        for(int i = 0; i < structures.length; i++) {

            if(structures[i] != null) {
                if(structures[i].getCode() > 1) {
                    fillCircleOptimized(structures[i].getX() + structures[i].getW()/2,
                            structures[i].getY() + structures[i].getH()/2, structures[i].getRadius(), structures[i].getCode());
                }
            }

        }

    }

    private void fillCircleOptimized(int centerX, int centerY, int radius, int value) {
        int width = interactionMap[0].length;
        int height = interactionMap.length;

        int startX = Math.max(0, centerX - radius);
        int endX = Math.min(width - 1, centerX + radius);
        int startY = Math.max(0, centerY - radius);
        int endY = Math.min(height - 1, centerY + radius);

        int radiusSquared = radius * radius;

        for (int y = startY; y <= endY; y++) {
            int dy = y - centerY;
            int dySquared = dy * dy;

            for (int x = startX; x <= endX; x++) {
                int dx = x - centerX;
                if (dx * dx + dySquared <= radiusSquared) {
                    interactionMap[y][x] = value;
                }
            }
        }
    }


    public Structure isPlayerInInteractionZone(Structure[] structures) {
        int playerX = (int)gp.player.getWorldX();
        int playerY = (int)gp.player.getWorldY();
        int playerW = gp.player.getCollisionWidth();
        int playerH = gp.player.getCollisionHeight();

        // Определяем область вокруг игрока для проверки
        int startX = Math.max(0, playerX - 50);
        int endX = Math.min(mapX - 1, playerX + playerW + 50);
        int startY = Math.max(0, playerY - 50);
        int endY = Math.min(mapY - 1, playerY + playerH + 50);

        for(int y = startY; y <= endY; y++) {
            for(int x = startX; x <= endX; x++) {
                if(interactionMap[y][x] > 0) {
                    // Проверяем пересечение
                    if(x >= playerX && x <= playerX + playerW && y >= playerY && y <= playerY + playerH) {
                        for(Structure structure : structures) {
                            if(structure != null) {
                                if(structure.getCode() == interactionMap[y][x]) return structure;
                            }

                        }
                    }

                }
            }
        }
        return null;
    }

    public GameObject isPlayerInInteractionZone(GameObject[] objects) {
        int playerX = (int)gp.player.getWorldX();
        int playerY = (int)gp.player.getWorldY();
        int playerW = gp.player.getCollisionWidth();
        int playerH = gp.player.getCollisionHeight();

        // Определяем область вокруг игрока для проверки
        int startX = Math.max(0, playerX - 50);
        int endX = Math.min(mapX - 1, playerX + playerW + 50);
        int startY = Math.max(0, playerY - 50);
        int endY = Math.min(mapY - 1, playerY + playerH + 50);

        for(int y = startY; y <= endY; y++) {
            for(int x = startX; x <= endX; x++) {
                if(interactionMap[y][x] > 0) {
                    // Проверяем пересечение
                    if(x >= playerX && x <= playerX + playerW && y >= playerY && y <= playerY + playerH) {
                        for(GameObject object : objects) {
                            if(object != null) {
                                if(object.getCode() == interactionMap[y][x]) return object;
                            }

                        }
                    }

                }
            }
        }
        return null;
    }

}
