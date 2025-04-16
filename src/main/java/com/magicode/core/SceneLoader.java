package main.java.com.magicode.core;

import main.java.com.magicode.core.utils.Collision;
import main.java.com.magicode.core.utils.Interaction;
import main.java.com.magicode.gameplay.world.Layer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SceneLoader {

    private Collision collision;
    private Interaction interaction;
    private GamePanel gp;
    private Layer[][] worldMap;
    private static final String DEFAULT_SCENE = "/resources/levels/sceneStartGame";
    private int sceneWidth;
    private int sceneHeight;
    

    public SceneLoader(GamePanel gp, String scenePath) {
        this.gp = gp;
        loadScene(scenePath != null ? scenePath : DEFAULT_SCENE);
    }

    private void loadScene(String path) {
        System.out.println("Началась загрузка сцены!");
        long startTime = System.nanoTime();
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + path);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }

            String parts[] = line.split(" ");

            if(parts.length != 2) {
                System.out.println("При загрузке сцены, первая строка не соответствует формату \"число число\"");
                return;
            }
            sceneWidth = Integer.parseInt(parts[0]);
            sceneHeight = Integer.parseInt(parts[1]);
            collision = new Collision(sceneWidth*GamePanel.tileSize, sceneHeight*GamePanel.tileSize);
            interaction = new Interaction(sceneWidth*GamePanel.tileSize, sceneHeight*GamePanel.tileSize);
            worldMap = new Layer[sceneHeight][sceneWidth];
            for (int i = 0; i < sceneHeight; i++) {
                for (int j = 0; j < sceneWidth; j++) {
                    worldMap[i][j] = new Layer(gp);
                }
            }

            // Замените этот блок
            for(int row = 0; row < sceneHeight; row++) {
                line = br.readLine();

                // Оптимизированная обработка строки
                parts = line.split(" ");
                for(int col = 0; col < sceneWidth; col++) {
                    String elements = parts[col];
                    // Упрощенная обработка
                    worldMap[row][col].setLayers(elements);
                }
            }

            collision.loadMap(worldMap);
//            interaction.loadMap(worldMap);

            System.out.println("Успешная загрузка сцены: " + path);
            System.out.println("Сцена загрузилась за: " + ((System.nanoTime()-startTime)/1000000000) + " секунд!");
        }// Вместо общего Exception лучше ловить конкретные исключения
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки сцены: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMap() {

    }
    
    public int getSceneWidth() {
        return sceneWidth;
    }

    public int getSceneHeight() {
        return sceneHeight;
    }

    public Collision getCollision() {
        return collision;
    }

    public Layer[][] getWorldMap() {
        return worldMap;
    }

    public void update() {

    }

    public void draw(Graphics2D g) {
        int worldCol = 0;
        int worldRow = 0;



        while (worldCol < gp.getWorldWidth() && worldRow < gp.getWorldHeight()) {


            int worldX = worldCol * GamePanel.tileSize;
            int worldY = worldRow * GamePanel.tileSize;
            int screenX = (int) (worldX - gp.player.getWorldX() + gp.player.getScreenX());
            int screenY = (int) (worldY - gp.player.getWorldY() + gp.player.getScreenY());

            if (worldX + GamePanel.tileSize > gp.player.getWorldX() - gp.player.getScreenX() &&
                    worldX - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                    worldY + GamePanel.tileSize > gp.player.getWorldY() - gp.player.getScreenY() &&
                    worldY - GamePanel.tileSize * 4 < gp.player.getWorldY() + gp.player.getScreenY()) {
                try {

                    worldMap[worldRow][worldCol].draw(g, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize);

                } catch (Exception e) {
                    System.out.println("BackGround: Не удалось загрузить на позицию " + screenX + " и " + screenY);
                }
            }

            worldCol++;
            if (worldCol == gp.getWorldWidth()) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

}
