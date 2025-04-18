package main.java.com.magicode.core;

import main.java.com.magicode.core.utils.Collision;
import main.java.com.magicode.core.utils.CutScene;
import main.java.com.magicode.core.utils.Interaction;
import main.java.com.magicode.gameplay.world.Layer;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.structures.Chest;
import main.java.com.magicode.gameplay.world.structures.Door;
import main.java.com.magicode.gameplay.world.structures.Hatch;

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
    private Structure[] structures;
    private static final String DEFAULT_BACKGROUND = "/resources/levels/parts1/background";
    private static final String DEFAULT_STRUCTURE = "/resources/levels/parts1/structure";
    private int sceneWidth;
    private int sceneHeight;
    private CutScene scene;
    private int cooldown;
    private boolean isCooldown;
    private boolean isCutScene;
    

    public SceneLoader(GamePanel gp, String backgroundPath, String structurePath) {
        this.gp = gp;
        loadScene(backgroundPath != null ? backgroundPath : DEFAULT_BACKGROUND, structurePath != null ? structurePath : DEFAULT_STRUCTURE);

        cooldown = 0;
        isCooldown = false;
    }

    private void loadScene(String backgroundPath, String structurePath) {
        long startTime = System.nanoTime();

        try (InputStream is = getClass().getResourceAsStream(backgroundPath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + backgroundPath);
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
            interaction = new Interaction(gp, sceneWidth*GamePanel.tileSize, sceneHeight*GamePanel.tileSize);
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

            System.out.println("Успешная загрузка сцены: " + backgroundPath);
//            System.out.println("Сцена загрузилась за: " + ((System.nanoTime()-startTime)/1000000) + " миллисекунд!");
        }// Вместо общего Exception лучше ловить конкретные исключения
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки сцены: " + e.getMessage());
            e.printStackTrace();
        }

        //STRUCTURE
        startTime = System.nanoTime();
        try (InputStream is = getClass().getResourceAsStream(structurePath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + structurePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }
            String parts[] = line.split(" ");
            structures = new Structure[parts.length];
            for(int i = 0; i < parts.length; i++) {
                String structure[] = parts[i].split("_");
                if(structure[0].equals("door")) {
                    //Формат: name_x_y_w_h_code:radius_isLock_direction_state - для двери
                    structures[i] = new Door(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                            Integer.parseInt(structure[3]), Integer.parseInt(structure[4]),
                            structure[5], structure[6].equals("true"), structure[8].equals("true"), structure[7]);
                }

                if(structure[0].equals("hatch")) {
                    //Формат: name_x_y_w_h_code:radius_state_nextX_nextY - для люка
                    structures[i] = new Hatch(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                            Integer.parseInt(structure[3]), Integer.parseInt(structure[4]),
                            structure[5], structure[6].equals("true"),
                            structure[7].split(";"));
                }

                if(structure[0].equals("chest")) {
                    //Формат: name_x_y_w_h_code:radius_isLock_direction_state - для сундука
                    structures[i] = new Chest(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                            Integer.parseInt(structure[3]), Integer.parseInt(structure[4]),
                            structure[5], structure[6].equals("true"), structure[8].equals("true"), structure[7]);
                }
            }


            if(structures != null) {
                if(collision != null) {
                    collision.loadStructure(structures);
                }
                if(interaction != null) {
                    interaction.loadStructure(structures);
                }

            }

            System.out.println("Успешная загрузка структур: " + backgroundPath);
//            System.out.println("Структуры загрузились за: " + ((System.nanoTime()-startTime)/1000000) + " миллисекунд!");
        }
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки структур: " + e.getMessage());
            e.printStackTrace();
        }

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

    public Interaction getInteraction() {
        return interaction;
    }

    public Layer[][] getWorldMap() {
        return worldMap;
    }

    public boolean getCutScene() {
        return isCutScene;
    }

    public void update() {

        if(isCutScene) {
            isCutScene = !scene.update();
            return;
        }

        if(cooldown == 40) {
            cooldown = 0;
            isCooldown = false;
        }
        if(isCooldown) cooldown++;

        if(cooldown == 0) {
            Structure structure = interaction.isPlayerInInteractionZone(structures);
            if(structure != null && structure.getState()) {
                if(GamePanel.keys[5]) { // Если открыть-закрыть дверь

                    isCooldown = true;
                    if(structure.getName().equals("door")) {
                        Door door = (Door) structure;
                        door.changeLock();
                    } else if(structure.getName().equals("hatch")) {
                        Hatch hatch = (Hatch) structure;
                        scene = new CutScene(gp, hatch.getRoute());
                        isCutScene = true;
                    } else if(structure.getName().equals("chest")) {
                        Chest chest = (Chest) structure;
                        chest.openChest();
                    }

                    collision.reloadMap(worldMap, structures);
                    interaction.reloadMap(structures);
                }
            }
        }


    }

    public void drawBackground(Graphics2D g) {
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

    public void drawStructure(Graphics2D g) {
        for(Structure s: structures) {
            if(s != null) {
                s.draw(g);
            }
        }
    }

    public void draw(Graphics2D g) {
        drawBackground(g);
        drawStructure(g);
    }
}
