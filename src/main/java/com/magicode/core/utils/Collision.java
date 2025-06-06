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


public class Collision { // Класс отвечающий за коллизию всех сущностей с препятствиями
    private int[][] collisionMap;
    private int mapX, mapY;
    public Collision(int mapX, int mapY) {
        this.mapX = mapX;
        this.mapY = mapY;
        collisionMap = new int[mapY][mapX];

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
            }

        }

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
            try{
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

    }

}