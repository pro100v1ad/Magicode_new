package main.java.com.magicode.core;

import main.java.com.magicode.core.utils.Collision;
import main.java.com.magicode.core.utils.CutScene;
import main.java.com.magicode.core.utils.Interaction;
import main.java.com.magicode.gameplay.entity.Enemy;
import main.java.com.magicode.gameplay.entity.EnemyType.Slime;
import main.java.com.magicode.gameplay.world.GameObject;
import main.java.com.magicode.gameplay.world.Layer;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.objects.Book;
import main.java.com.magicode.gameplay.world.objects.Key;
import main.java.com.magicode.gameplay.world.objects.Wrench;
import main.java.com.magicode.gameplay.world.structures.Chest;
import main.java.com.magicode.gameplay.world.structures.Door;
import main.java.com.magicode.gameplay.world.structures.Hatch;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;

public class SceneLoader {

    private Collision collision;
    private Interaction interaction;
    private GamePanel gp;
    private Layer[][] worldMap;
    private Structure[] structures;
    private GameObject[] objects;
    public static final String DEFAULT_BACKGROUND = "/resources/levels/scenes/start/background";
    public static final String DEFAULT_STRUCTURE = "/resources/levels/scenes/start/structure";
    public static final String DEFAULT_ENEMY = "/resources/levels/scenes/start/enemies";
    private int sceneWidth;
    private int sceneHeight;
    private CutScene scene;
    private int cooldown;
    private boolean isCooldown;
    private boolean isCutScene;
    private Enemy[] enemies;

    public SceneLoader(GamePanel gp, boolean isStart, String backgroundPath,
                       String structurePath, String objectPath, String enemiesPath) {
        this.gp = gp;
        if(backgroundPath != null && structurePath != null) {
            if(isStart) {
                loadScene(backgroundPath, structurePath);
                loadEnemies(enemiesPath); // Гарантированно вызываем loadEnemies
            } else {
                loadSaveScene(backgroundPath, structurePath, objectPath);
                loadEnemies(enemiesPath); // Гарантированно вызываем loadEnemies
            }
        } else {
            loadScene(DEFAULT_BACKGROUND, DEFAULT_STRUCTURE);
            loadEnemies(DEFAULT_ENEMY); // Гарантированно вызываем loadEnemies
        }
        cooldown = 0;
        isCooldown = false;
    }

    // Метод загрузки врагов
    private void loadEnemies(String enemiesPath) {
        System.out.println("Начало загрузки врагов. Путь: " + enemiesPath);

        if (enemiesPath == null || enemiesPath.isEmpty()) {
            System.out.println("Путь к врагам не указан");
            enemies = new Enemy[0];
            return;
        }

        try (InputStream is = getClass().getResourceAsStream(enemiesPath)) {
            if (is == null) {
                System.err.println("Файл не найден в ресурсах: " + enemiesPath);
                enemies = new Enemy[0];
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int enemyCount = 0;

            // Первый проход - подсчет количества врагов
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) enemyCount++;
            }

            // Создаем массив нужного размера
            enemies = new Enemy[enemyCount];
            int index = 0;

            // Второй проход - чтение данных
            try (InputStream is2 = getClass().getResourceAsStream(enemiesPath)) {
                BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));

                while ((line = br2.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    try {
                        String[] parts = line.split("_");
                        if (parts.length < 4) {
                            System.err.println("Некорректный формат строки: " + line);
                            continue;
                        }

                        String enemyType = parts[0];
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        boolean aggressive = Boolean.parseBoolean(parts[3]);

                        if (index >= enemies.length) {
                            System.err.println("Превышение размера массива");
                            break;
                        }

                        switch (enemyType.toLowerCase()) {
                            case "slime":
                                Slime slime = new Slime(gp);
                                slime.setWorldX(x);
                                slime.setWorldY(y);
                                slime.setAggressive(aggressive);
                                enemies[index++] = slime;
                                break;
                            default:
                                System.err.println("Неизвестный тип врага: " + enemyType);
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка парсинга строки: " + line);
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Успешно загружено врагов: " + index);

        } catch (Exception e) {
            System.err.println("Критическая ошибка загрузки врагов: ");
            e.printStackTrace();
            enemies = new Enemy[0];
        }
    }

    private void loadSaveScene(String backgroundPath, String structurePath, String objectPath) {


        try (BufferedReader reader = new BufferedReader(new FileReader(backgroundPath))) {
            String line;
            line = reader.readLine();
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

            for(int row = 0; row < sceneHeight; row++) {
                line = reader.readLine();
                if(line == null) {
                    break;
                }
                // Оптимизированная обработка строки
                parts = line.split(" ");
                for(int col = 0; col < sceneWidth; col++) {
                    String elements = parts[col];
                    // Упрощенная обработка
                    worldMap[row][col].setLayers(elements);
                }
            }

            setWorldMap(worldMap);


            System.out.println("Задний фон загружен из файла: " + backgroundPath);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке заднего фона: " + e.getMessage());
        }
        // STRUCTURE
        try (BufferedReader reader = new BufferedReader(new FileReader(structurePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }

            String parts[] = line.split(" ");
            structures = new Structure[parts.length];
            objects = new GameObject[parts.length];
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
                            structure[7]);
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



            System.out.println("Структуры загружены из файла: " + structurePath);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке структур: " + e.getMessage());
        }

        // OBJECTS
        try (BufferedReader reader = new BufferedReader(new FileReader(objectPath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }

            objects = new GameObject[Integer.parseInt(line)];
            for(int i = 0; i < objects.length; i++) {
                line = reader.readLine();
                if(line == null) break;
                String[] parts = line.split("_");
                if(parts[0].equals("key")) {
                    objects[i] = new Key(gp, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                }
                if(parts[0].equals("book")) {
                    objects[i] = new Book(gp, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                }
                if(parts[0].equals("wrench")) {
                    objects[i] = new Wrench(gp, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                }

            }

            interaction.loadObjects(objects);

            System.out.println("Объекты загружены из файла: " + objectPath);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке объектов: " + e.getMessage());
        }

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

            if(structures != null) {
                if(collision != null) {
                    collision.loadStructure(structures);
                }
                if(interaction != null) {
                    interaction.loadStructure(structures);
                }

            }

            setWorldMap(worldMap);

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
            objects = new GameObject[parts.length];
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
                            structure[7]);
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

    // Методы для работы с врагами
    public Enemy[] getEnemies() {
        return enemies;
    }

    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
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

    public void setWorldMap(Layer[][] worldMap) {
        this.worldMap = worldMap;
        if (collision != null) {
            collision.loadMap(worldMap);
        }
    }

    public void setStructures(Structure[] structures) {
        this.structures = structures;
        if (collision != null) {
            collision.loadStructure(structures);
        }
        if (interaction != null) {
            interaction.loadStructure(structures);
        }
    }

    public Structure[] getStructures() {
        return structures;
    }

    public GameObject[] getObjects() {
        return objects;
    }

    public void setObjects(GameObject[] objects) {
        this.objects = objects;
    }

    public void update() {

        if(gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }

        if(isCutScene) {
            isCutScene = !scene.update();
            return;
        }


        if(cooldown == 20) {
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
                        int index = 0;
                        for(int i = 0; i < structures.length; i++) { // Это я пытался придумать как мне несколько объектов загружать в массив объектов
                            // Додумался лишь до того, что если массив структур и объектов одного размера. То и по индексу объекты будут равны со структурой которая их создает
                            // Эт не сильно оптимально, т.к. те структуры что не создают объект есть, а в массиве объектов место под это выделено, и получается что оно всегда пустует
                            // Нужно что-то более умное придумать для этого. Чтобы каждый новый объект добавлялся в конец массива. И при удалении одного объекта его места в массиве мог занять другой.
                            // Коммент сотри когда прочитаешь.
                            if(structures[i].equals(structure)) {
                                index = i;
                                break;
                            }
                        }
                        Chest chest = (Chest) structure;
                        objects[index] = chest.openChest(objects[index]);
                    }

                    collision.reloadMap(worldMap, structures);
                    interaction.reloadMap(structures, objects);
                }
            }

            if(!isCooldown) {
                GameObject object = interaction.isPlayerInInteractionZone(objects);
                if (object != null) {
                    if (GamePanel.keys[5]) { // Если открыть-закрыть дверь

                        isCooldown = true;
                        if (object.getName().equals("key")) {
                            for (int i = 0; i < objects.length; i++) {
                                if(objects[i] != null) {
                                    if (objects[i].equals(object)) {
                                        objects[i] = null;
                                        System.out.println("Ключ подобран!");
                                        break;
                                    }
                                }

                            }
                        }
                        if(object.getName().equals("book")) {
                            for (int i = 0; i < objects.length; i++) {
                                if(objects[i] != null) {
                                    if (objects[i].equals(object)) {
                                        objects[i] = null;
                                        System.out.println("Книга подобрана!");
                                        gp.directory.addInfo();
                                        gp.player.setCountBook(gp.player.getCountBook() + 1);
                                        break;
                                    }
                                }

                            }
                        }

                        if(object.getName().equals("wrench")) {
                            for (int i = 0; i < objects.length; i++) {
                                if(objects[i] != null) {
                                    if (objects[i].equals(object)) {
                                        objects[i] = null;
                                        System.out.println("Гаечный ключ подобран!");
                                        gp.sceneChanger.setNumberActiveScene(gp.sceneChanger.getNumberActiveScene() + 1);
                                        break;
                                    }
                                }

                            }
                        }
                        interaction.reloadMap(structures, objects);
                    }
                }

            }

        }
        for(Enemy enemy : enemies) {
            if(enemy != null) {
                enemy.update();
            }
        }

        System.out.println("Активных врагов: " + Arrays.stream(enemies).filter(Objects::nonNull).count());

        for(Enemy enemy : enemies) {
            if(enemy != null) {
                enemy.update();
                System.out.println("Враг на позиции: " + enemy.getWorldX() + "," + enemy.getWorldY());
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

    public void drawObjects(Graphics2D g) {
        for(GameObject o: objects) {
            if(o != null) {
                o.draw(g);
            }
        }
    }

    public void draw(Graphics2D g) {
        if(gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        drawBackground(g);
        drawStructure(g);
        drawObjects(g);
        drawEnemies(g);
    }

    private void drawEnemies(Graphics2D g) {
        for(Enemy enemy : enemies) {
            if(enemy != null) {
                enemy.draw(g);
            }
        }
    }
}
