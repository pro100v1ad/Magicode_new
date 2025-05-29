package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.entity.Entity;
import main.java.com.magicode.gameplay.world.Layer;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.structures.Bridge;
import main.java.com.magicode.gameplay.world.structures.Chest;
import main.java.com.magicode.gameplay.world.structures.Door;
import main.java.com.magicode.gameplay.world.structures.Portal;

import java.awt.*;


public class Collision {
    private int[][] collisionMap; // true - есть коллизия
    private int mapX, mapY;
    public Collision(int mapX, int mapY) {
        this.mapX = mapX;
        this.mapY = mapY;
        collisionMap = new int[mapY][mapX];/////////////////////////////////Решить проблему коллизии при пустой карте

        for(int i = 0; i < mapY; i++) {
            for(int j = 0; j < mapX; j++) {
                collisionMap[i][j] = 0;
            }
        }

    }

    public int[][] getCollisionMap() {
        return collisionMap;
    }

    public void reloadMap(Layer[][] worldMap, Structure[] structures) {
        for(int i = 0; i < mapY; i++) {
            for(int j = 0; j < mapX; j++) {
                collisionMap[i][j] = 0;
            }
        }
        loadMap(worldMap);
        loadStructure(structures);
    }

    public void loadMap(Layer[][] worldMap) {
        if(worldMap == null) {
            System.out.println("Карта коллизии не загрузилась!");
            return;
        }
        try {
            int row = worldMap.length;
            int col = worldMap[0].length;
            for(int i = 0; i < row; i++) {
                for(int j = 0; j < col; j++) {
                    if(worldMap[i][j].getCollision()) {

                        for(int i2 = i*GamePanel.tileSize; i2 < (i+1)*GamePanel.tileSize; i2++) {
                            for(int j2 = j*GamePanel.tileSize; j2 < (j+1)*GamePanel.tileSize; j2++) {
                                try {
                                    if(collisionMap[i2][j2] != 0) {
                                        System.out.println("Почему-то там уже стоит 1");
                                    }
                                    collisionMap[i2][j2] = 1;
                                } catch (Exception e) {
                                    System.out.println("Выход за массив: " + i + " and " + j);
                                }

                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка загрузи карты Коллизии!");
        }




    }

    public void loadStructure(Structure[] structures) {

        for(int i = 0; i < structures.length; i++) {

            if(structures[i] != null) {
                if(structures[i].getName().equals("door")) {
                    Door door = (Door) structures[i];
                    if(door.getLock()) {
                        int x = door.getX();
                        int y = door.getY();
                        int w = door.getW();
                        int h = door.getH();

                        for(int row = y; row < y+h; row++) {
                            for(int col = x; col < x+w; col++) {
                                collisionMap[row][col] = 1;
                            }
                        }

                    }

                } else if(structures[i].getName().equals("chest")) {
                    Chest chest = (Chest) structures[i];
                    int x = chest.getX();
                    int y = chest.getY();
                    int w = chest.getW();
                    int h = chest.getH();

                    for(int row = y; row < y+h; row++) {
                        for(int col = x; col < x+w; col++) {
                            collisionMap[row][col] = 1;
                        }
                    }

                } else if(structures[i].getName().equals("tree") || structures[i].getName().equals("stone") || structures[i].getName().equals("bush")) {
                    int x = structures[i].getX();
                    int y = structures[i].getY();
                    int w = structures[i].getW();
                    int h = structures[i].getH();

                    for(int row = y; row < y+h; row++) {
                        for(int col = x; col < x+w; col++) {
                            collisionMap[row][col] = 1;
                        }
                    }
                } else if(structures[i].getName().equals("bridge")) {
                    Bridge bridge = (Bridge) structures[i];

                    int x = bridge.getX();
                    int y = bridge.getY();
                    int w = bridge.getW();
                    int h = bridge.getH();

                    if(bridge.getBreak()) {
                        for(int row = y; row < y+h; row++) {
                            for(int col = x; col < x+w; col++) {
                                collisionMap[row][col] = 0;
                                if(row < y + 8) collisionMap[row][col] = 1;
                                if(row > y+h-32) collisionMap[row][col] = 1;
                                if(col > x+w*3/4 + 16 && col < x+w-64) {
                                    collisionMap[row][col] = 1;
                                }
                            }
                        }
                    } else {
                        for(int row = y; row < y+h; row++) {
                            for(int col = x; col < x+w; col++) {
                                if(col < x+w-32) collisionMap[row][col] = 0;
                                if(row < y + 8) collisionMap[row][col] = 1;
                                if(row > y+h-32) collisionMap[row][col] = 1;
                            }
                        }
                    }

                } else if(structures[i].getName().equals("portal")) {
                    Portal portal = (Portal) structures[i];

                    int x = portal.getX();
                    int y = portal.getY();
                    int w = portal.getW();
                    int h = portal.getH();

                    for(int row = y; row < y+h; row++) {
                        for(int col = x; col < x+w; col++) {
                            collisionMap[row][col] = 1;
                        }
                    }

                }
            }//end if

        }//end for

    }

    public boolean checkEntityCollision(Entity entity1, Entity entity2) {
        Rectangle rect1 = new Rectangle(
                (int)entity1.getWorldX(),
                (int)entity1.getWorldY(),
                entity1.getCollisionWidth(),
                entity1.getCollisionHeight()
        );
        Rectangle rect2 = new Rectangle(
                (int)entity2.getWorldX(),
                (int)entity2.getWorldY(),
                entity2.getCollisionWidth(),
                entity2.getCollisionHeight()
        );
        return rect1.intersects(rect2);
    }

    public boolean checkCollisionUp(Entity entity) {
        int x = (int)entity.getWorldX();
        int y = (int)entity.getWorldY();
        int w = entity.getCollisionWidth();
        int h = entity.getCollisionHeight();
        int code = entity.getCollisionCode();
        boolean flag = false;
        for(int i = x; i < x + w; i++) {
//            if(collisionMap[y + h - h/3+1][i] != entity.getCollisionCode()) System.out.println(" Разберись почему нет кода сущности тут up" + entity.getCollisionCode());
            try{
//                if(collisionMap[y-1][i] == 1) flag = true;
                if(collisionMap[y + h - h/3-1][i] == 1) flag = true;
            }catch (Exception e) {
                System.out.println("Вышло за пределы коллизии массива");
            }
            if(flag) break;
        }
        if(!flag) {
            movePlayerPosition("up", x, y, w, h, code);
        }
        return !flag;
    }
    public boolean checkCollisionDown(Entity entity) {
        int x = (int)entity.getWorldX();
        int y = (int)entity.getWorldY();
        int w = entity.getCollisionWidth();
        int h = entity.getCollisionHeight();
        int code = entity.getCollisionCode();
        boolean flag = false;
        for(int i = x; i < x + w; i++) {
//            if(collisionMap[y+h-1][i] != entity.getCollisionCode()) System.out.println(" Разберись почему нет кода сущности тут down" + entity.getCollisionCode());
            try{
                if(collisionMap[y+h][i] == 1) flag = true;
            }catch (Exception e) {
                System.out.println("Вышло за пределы коллизии массива");
            }
            if(flag) break;
        }
        if(!flag) {
            movePlayerPosition("down", x, y, w, h, code);
        }
        return !flag;
    }
    public boolean checkCollisionRight(Entity entity) {
        int x = (int)entity.getWorldX();
        int y = (int)entity.getWorldY();
        int w = entity.getCollisionWidth();
        int h = entity.getCollisionHeight();
        int code = entity.getCollisionCode();
        boolean flag = false;
        for(int i = y; i < y + h; i++) {
            if(i > y + h - h/3) {
//                if (collisionMap[i][x + w - 1] != entity.getCollisionCode())
//                    System.out.println(" Разберись почему нет кода сущности тут right" + entity.getCollisionCode());
                try {
                    if (collisionMap[i][x + w] == 1) flag = true;
                } catch (Exception e) {
                    System.out.println("Вышло за пределы коллизии массива");
                }
                if (flag) break;
            }
        }
        if(!flag) {
            movePlayerPosition("right", x, y, w, h, code);
        }

        return !flag;
    }
    public boolean checkCollisionLeft(Entity entity) {
        int x = (int)entity.getWorldX();
        int y = (int)entity.getWorldY();
        int w = entity.getCollisionWidth();
        int h = entity.getCollisionHeight();
        int code = entity.getCollisionCode();
        boolean flag = false;
        for(int i = y; i < y + h; i++) {
            if(i > y + h - h/3) {
//                if (collisionMap[i][x] != entity.getCollisionCode())
//                    System.out.println(" Разберись почему нет кода сущности тут left" + entity.getCollisionCode());
                try {
                    if (collisionMap[i][x - 1] == 1) flag = true;
                } catch (Exception e) {
                    System.out.println("Вышло за пределы коллизии массива");
                }
                if (flag) break;
            }
        }
        if(!flag) {
            movePlayerPosition("left", x, y, w, h, code);
        }
        return !flag;
    }


    public void movePlayerPosition(String direction, int x, int y, int w, int h, int code) {

        for(int i = y; i < y + h; i++) {
            for(int j = x; j < x + w; j++) {
                if(collisionMap[i][j] == code) collisionMap[i][j] = 0;
            }
        }

        switch (direction) {
            case "up": if(y > 0) y--; break;
            case "down": if(y < mapY) y++; break;
            case "left": if(x > 0) x--; break;
            case "right": if(x < mapX) x++; break;
        }

        for(int i = y; i < y + h; i++) {
            for(int j = x; j < x + w; j++) {
                if(collisionMap[i][j] == 0 && i > y + h - h/3) collisionMap[i][j] = code;
            }
        }
//        System.out.println();
//        System.out.println();
//        for(int i = y-5; i < y + h +5; i++) {
//            for(int j = x-5; j < x + w +5; j++) {
//                System.out.print(collisionMap[i][j] + " ");
//            }
//            System.out.println();
//        }


    }

//    public void drawEntity(Graphics2D g) {
//        g.setColor(Color.YELLOW);
//
//        // Перебираем всю карту коллизий
//        for (int worldY = 0; worldY < GamePanel.worldHeight; worldY++) {
//            for (int worldX = 0; worldX < GamePanel.worldWidth; worldX++) {
//
//                // Если в этой точке есть коллизия сущности (например, игрока)
//                if (collisionMap[worldY][worldX] == 2) {
//
//                    // Преобразуем мировые координаты в экранные
//                    int screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
//                    int screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);
//
//                    // Проверяем, находится ли точка в зоне видимости камеры
//                    if (worldX > gp.player.worldX - gp.player.screenX - 32 &&
//                            worldX < gp.player.worldX + gp.player.screenX + 32 &&
//                            worldY > gp.player.worldY - gp.player.screenY - 32 &&
//                            worldY < gp.player.worldY + gp.player.screenY + 32) {
//
//                        // Рисуем пиксель коллизии
//                        g.fillRect(screenX, screenY, 1, 1);
//                    }
//                }
//            }
//        }
//    }

//    public void drawCollision(Graphics2D g) {
//        g.setColor(Color.BLUE);
//
//        // Перебираем всю карту коллизий
//        for (int worldY = 0; worldY < GamePanel.HEIGHT; worldY++) {
//            for (int worldX = 0; worldX < GamePanel.WIDTH; worldX++) {
//
//                // Если в этой точке есть коллизия сущности (например, игрока)
//                if (collisionMap[worldY][worldX] == 1) {
//
//                    // Преобразуем мировые координаты в экранные
//                    int screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
//                    int screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);
//
//                    // Проверяем, находится ли точка в зоне видимости камеры
//                    if (worldX > gp.player.worldX - gp.player.screenX - 32 &&
//                            worldX < gp.player.worldX + gp.player.screenX + 32 &&
//                            worldY > gp.player.worldY - gp.player.screenY - 32 &&
//                            worldY < gp.player.worldY + gp.player.screenY + 32) {
//
//                        // Рисуем пиксель коллизии
//                        g.fillRect(screenX, screenY, 1, 1);
//                    }
//                }
//            }
//        }
//    }

//    public void drawTiles(Graphics2D g) {
//        g.setColor(Color.RED);
//
//        // Перебираем все тайлы мира
//        for (int worldRow = 0; worldRow < GamePanel.maxWorldRow; worldRow++) {
//            for (int worldCol = 0; worldCol < GamePanel.maxWorldCol; worldCol++) {
//
//                // Проверяем, есть ли у тайла коллизия
//                if (gp.backGround.worldMap[worldRow][worldCol].getCollision()) {
//
//                    // Мировые координаты тайла
//                    int worldX = worldCol * GamePanel.tileSize;
//                    int worldY = worldRow * GamePanel.tileSize;
//
//                    // Преобразуем мировые координаты в экранные (относительно камеры)
//                    int screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
//                    int screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);
//
//                    // Проверяем, находится ли тайл в зоне видимости камеры
//                    if (worldX + GamePanel.tileSize > gp.player.worldX - gp.player.screenX &&
//                            worldX - GamePanel.tileSize < gp.player.worldX + gp.player.screenX &&
//                            worldY + GamePanel.tileSize > gp.player.worldY - gp.player.screenY &&
//                            worldY - GamePanel.tileSize < gp.player.worldY + gp.player.screenY) {
//
//                        // Рисуем квадрат коллизии
//                        g.drawRect(screenX, screenY, GamePanel.tileSize, GamePanel.tileSize);
//                    }
//                }
//            }
//        }
//    }
}