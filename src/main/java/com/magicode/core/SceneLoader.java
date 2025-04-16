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
    private Layer[][] worldMap;
    private static final String DEFAULT_SCENE = "/resources/levels/sceneStartGame";
    private int sceneWidth;
    private int sceneHeight;
    

    public SceneLoader(String scenePath) {
        loadScene(scenePath != null ? scenePath : DEFAULT_SCENE);
    }

    public SceneLoader() {
        this(DEFAULT_SCENE);
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
            sceneWidth = Integer.parseInt(parts[0]);
            sceneHeight = Integer.parseInt(parts[1]);
            collision = new Collision(sceneWidth*GamePanel.tileSize, sceneHeight*GamePanel.tileSize);
            interaction = new Interaction(sceneWidth*GamePanel.tileSize, sceneHeight*GamePanel.tileSize);
            worldMap = new Layer[sceneHeight][sceneWidth];
            for (int i = 0; i < sceneHeight; i++) {
                for (int j = 0; j < sceneWidth; j++) {
                    worldMap[i][j] = new Layer();
                }
            }

            for(int row = 0; row < sceneHeight; row++) {
                line = br.readLine();
                if (line == null && row != sceneHeight - 1) {
                    System.out.println("Сцена не до конца загрузилась!");
                    return;// Если файл закончился раньше, чем ожидалось
                }
                parts = line.split(" ");
                for(int col = 0; col < sceneWidth; col++) {

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

    }

}
