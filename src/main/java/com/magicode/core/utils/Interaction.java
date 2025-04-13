package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Layer;

public class Interaction {

    private int[][] interactionMap; // true - есть коллизия

    private int mapX, mapY;

    public Interaction(int mapX, int mapY) {

        this.mapX = mapX;
        this.mapY = mapY;
        interactionMap = new int[mapY][mapX];

        for(int i = 0; i < mapY; i++) {
            for(int j = 0; j < mapX; j++) {
                interactionMap[i][j] = 0;
            }
        }

    }

//    public void loadInteractionMapFromObjects(SuperObject[] object) {
//
//        for(int i = 0; i < object.length; i++) {
//
//            if(object[i] != null) {
//
//                if(object[i].getName().equals("Book")) {
//
//                    OBJ_Book book = (OBJ_Book) object[i];
////                    System.out.println("Книга на позицию успешно загружена" + book.worldX + book.worldY);
//                    fillCircleOptimized(interactionMap, book.getInteractionCenterX(), book.getInteractionCenterY(), book.getInteractionRadius(), book.getInteractionCode());
//
//                }
//
//            }
//
//        }
//
//    }

//    public void clearObjectFromMap(SuperObject object) {
//        if(object.getName().equals("Book")) {
//            OBJ_Book book = (OBJ_Book) object;
//            fillCircleOptimized(interactionMap, book.getInteractionCenterX(), book.getInteractionCenterY(), book.getInteractionRadius(), 0);
//
//        }
//    }

    public void loadMap(Layer[][] worldMap) {
        int row = worldMap.length;
        int col = worldMap[0].length;

        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                String elements[] = worldMap[i][j].getInteractionZone().split(":");
                if(elements.length != 3) {
                    System.out.println("Ошибка чтения кода и радиуса взаимодействия " + i + " and " + j);
                }
                int code = Integer.parseInt(elements[0]);
                int radius = Integer.parseInt(elements[1]);
                fillCircleOptimized(j*GamePanel.tileSize, i*GamePanel.tileSize, radius, code);


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
//
//
//    public SuperObject isPlayerInInteractionZone() {
//        int playerX = (int)gp.player.worldX;
//        int playerY = (int)gp.player.worldY;
//        int playerW = gp.player.collisionWidth;
//        int playerH = gp.player.collisionHeight;
//
//        // Определяем область вокруг игрока для проверки
//        int startX = Math.max(0, playerX - 50);
//        int endX = Math.min(mapX - 1, playerX + playerW + 50);
//        int startY = Math.max(0, playerY - 50);
//        int endY = Math.min(mapY - 1, playerY + playerH + 50);
//
//        for(int y = startY; y <= endY; y++) {
//            for(int x = startX; x <= endX; x++) {
//                if(interactionMap[y][x] > 0) {
//                    // Проверяем пересечение
//                    if(x >= playerX && x <= playerX + playerW && y >= playerY && y <= playerY + playerH) {
//                        for(SuperObject obj : gp.oSetter.obj) {
//                            if(obj != null) {
//                                if(obj.getInteractionCode() == interactionMap[y][x]) return obj;
//                            }
//
//                        }
//                    }
//
//                }
//            }
//        }
//        return null;
//    }

    // Добавить для отладки желтые полупрозрачные круги зоны интеракции
//    public void drawInteractionZones(Graphics2D g) {
//        g.setColor(new Color(255, 255, 0, 100)); // Желтый полупрозрачный
//
//        for(int y = 0; y < mapY; y++) {
//            for(int x = 0; x < mapX; x++) {
//                if(interactionMap[y][x] > 0) {
//                    int screenX = x - (int)gp.player.worldX + gp.player.screenX;
//                    int screenY = y - (int)gp.player.worldY + gp.player.screenY;
//                    g.fillRect(screenX, screenY, 1, 1);
////                    System.out.println("Круг загружен на позицию " + screenX + " and " + screenY);
//                }
//            }
//        }
//    }
}
