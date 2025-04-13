package main.java.com.magicode.core;

import main.java.com.magicode.core.utils.Collision;
import main.java.com.magicode.core.utils.Interaction;
import main.java.com.magicode.gameplay.world.Layer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SceneLoader {

    private Collision collision;
    private Interaction interaction;
    private Layer[][] worldMap;

    public SceneLoader() {
        loadScene("/resources/levels/sceneStartGame");
    }

    private void loadScene(String path) {
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
            int SceneWidth = Integer.parseInt(parts[0]);
            int SceneHeight = Integer.parseInt(parts[1]);
            collision = new Collision(SceneWidth*GamePanel.tileSize, SceneHeight*GamePanel.tileSize);
            interaction = new Interaction(SceneWidth*GamePanel.tileSize, SceneHeight*GamePanel.tileSize);
            worldMap = new Layer[SceneHeight][SceneWidth];
            for (int i = 0; i < SceneHeight; i++) {
                for (int j = 0; j < SceneWidth; j++) {
                    worldMap[i][j] = new Layer();
                }
            }

            for(int row = 0; row < SceneHeight; row++) {
                line = br.readLine();
                if (line == null && row != SceneHeight - 1) {
                    System.out.println("Сцена не до конца загрузилась!");
                    return;// Если файл закончился раньше, чем ожидалось
                }
                parts = line.split(" ");
                for(int col = 0; col < SceneWidth; col++) {

                    String elements[] = parts[col].split("_");
                    if(elements.length < 3) {
                        System.out.println("Данный фрагмент: <x:" + col + " y:" + row + ">, несоответствует формату: numberCollision_interactionCode:radius:fileName_layer1:layerN");
                        break;
                    }
                    worldMap[row][col].setCollision(Integer.parseInt(elements[0]) == 1);

                    worldMap[row][col].setLayers(elements[2]);

                    worldMap[row][col].setInteractionZone(elements[1]);

                }

            }

            collision.loadMap(worldMap);
            interaction.loadMap(worldMap);

            System.out.println("Успешная загрузка сцены: " + path);

        } catch (Exception e) {
            System.out.println("Ошибка в загрузке сцены");
        }
    }

    private void loadMap() {

    }

    public void update() {

    }

    public void draw(Graphics2D g) {

    }

}
