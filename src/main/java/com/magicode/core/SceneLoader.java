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
import main.java.com.magicode.gameplay.world.structures.*;
import main.java.com.magicode.spells.Spell;
import main.java.com.magicode.spells.spells.KeySpell;
import main.java.com.magicode.ui.gamestate.Board;

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
    private int sceneWidth;
    private int sceneHeight;
    private CutScene scene;
    private int cooldown;
    private boolean isCooldown;
    private boolean isCutScene;
    private Enemy[] enemies;
    private Board board;

    public SceneLoader(GamePanel gp, boolean isStart, String backgroundPath,
                       String structurePath, String objectPath, String enemiesPath, String spellsPath) {
        this.gp = gp;

        if(isStart) {
            if(backgroundPath == null && structurePath == null && spellsPath == null) {
                loadScene(DEFAULT_BACKGROUND, DEFAULT_STRUCTURE);
            } else {
                loadScene(backgroundPath, structurePath);
            }
            loadEnemies(enemiesPath);

        } else {
            loadSaveScene(backgroundPath, structurePath, objectPath);
            loadSaveEnemies(enemiesPath);
        }

        cooldown = 0;
        isCooldown = false;
    }

    private void loadSaveEnemies(String enemiesPath) {

        if (enemiesPath == null) {
            System.out.println("Враги на локации отсутствуют (путь не указан)");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(enemiesPath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                return;// Если файл закончился раньше, чем ожидалось
            }


            int enemyCount = Integer.parseInt(line);

            // Создаем массив нужного размера
            enemies = new Enemy[enemyCount];

            for(int i = 0; i < enemyCount; i++) {

                line = reader.readLine();

                String[] parts = line.split("_");

                if(parts[0].equals("slime")) {
                    Slime slime = new Slime(gp);
                    slime.setWorldX(Integer.parseInt(parts[1]));
                    slime.setWorldY(Integer.parseInt(parts[2]));
                    slime.setAggressive(parts[3].equals("true"));
                    enemies[i] = slime;
                }

            }


        } catch (Exception e) {
            System.err.println("Критическая ошибка загрузки врагов: ");
        }
    }

    // Метод загрузки врагов
    private void loadEnemies(String enemiesPath) {


        if (enemiesPath == null) {
            System.out.println("Враги на локации отсутствуют (путь не указан)");
            return;
        }

        try (InputStream is = getClass().getResourceAsStream(enemiesPath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + enemiesPath);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            line = br.readLine();
            int enemyCount = Integer.parseInt(line);

            // Создаем массив нужного размера
            enemies = new Enemy[enemyCount];

            for(int i = 0; i < enemyCount; i++) {

                line = br.readLine();

                String[] parts = line.split("_");

                if(parts[0].equals("slime")) {
                    Slime slime = new Slime(gp);
                    slime.setWorldX(Integer.parseInt(parts[1]));
                    slime.setWorldY(Integer.parseInt(parts[2]));
                    slime.setAggressive(parts[3].equals("true"));
                    enemies[i] = slime;
                }

            }


        } catch (Exception e) {
            System.err.println("Критическая ошибка загрузки врагов: ");
        }
    }

    private void loadSaveScene(String backgroundPath, String structurePath, String objectPath) {


        try (BufferedReader reader = new BufferedReader(new FileReader(backgroundPath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл saveScene пуст");
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

        } catch (IOException e) {
            System.err.println("Ошибка при загрузке заднего фона: " + e.getMessage());
        }
        // STRUCTURE
        try (BufferedReader reader = new BufferedReader(new FileReader(structurePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл structure пуст");
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
                            structure[5], structure[6].equals("true"), structure[8].equals("true"), structure[7], structure[9]);
                }
                if(structure[0].equals("tree") || structure[0].equals("bush") || structure[0].equals("stone")) {
                    // Формат name_x_y_w_h - для декораций
                    structures[i] = new Decoration(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                            Integer.parseInt(structure[3]), Integer.parseInt(structure[4]), structure[0]);
                }
                if(structure[0].equals("bridge")) {
                    //Формат name_x_y_len_direction_isBreak
                    structures[i] = new Bridge(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                            Integer.parseInt(structure[3]), structure[4], structure[5].equals("true"), structure[0]);
                }
                if(structure[0].equals("portal")) {
                    //Формат name_x_y_w_h_code:radius_direction
                    structures[i] = new Portal(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                            Integer.parseInt(structure[3]), Integer.parseInt(structure[4]), structure[5], structure[6]);
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

        } catch (IOException e) {
            System.err.println("Ошибка при загрузке структур: " + e.getMessage());
        }

        // OBJECTS
        try (BufferedReader reader = new BufferedReader(new FileReader(objectPath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл object пуст");
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
                System.out.println("Файл background пуст");
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
                    if(elements.isEmpty()) System.out.println("Я лох^ " + row + "_ " + col);
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

        }
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки сцены: " + e.getMessage());
            e.printStackTrace();
        }

        //STRUCTURE
        try (InputStream is = getClass().getResourceAsStream(structurePath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + structurePath);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Сначала подсчитаем общее количество структур
            int totalStructures = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Пропускаем пустые строки

                String[] parts = line.split(" ");
                totalStructures += parts.length;
            }

            if (totalStructures == 0) {
                System.out.println("Файл пуст или не содержит структур");
                return;
            }

            System.out.println("Количество структур: " + totalStructures);

            // Выделяем память
            structures = new Structure[totalStructures];
            objects = new GameObject[totalStructures];

            try (InputStream newIs = getClass().getResourceAsStream(structurePath)) {
                br = new BufferedReader(new InputStreamReader(newIs));

                int index = 0;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue; // Пропускаем пустые строки

                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        String[] structure = part.split("_");

                        try {
                            if (structure[0].equals("door")) {
                                structures[index++] = new Door(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                                        Integer.parseInt(structure[3]), Integer.parseInt(structure[4]),
                                        structure[5], structure[6].equals("true"), structure[8].equals("true"), structure[7]);
                            }
                            else if (structure[0].equals("hatch")) {
                                structures[index++] = new Hatch(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                                        Integer.parseInt(structure[3]), Integer.parseInt(structure[4]),
                                        structure[5], structure[6].equals("true"), structure[7]);
                            }
                            else if (structure[0].equals("chest")) {
                                structures[index++] = new Chest(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                                        Integer.parseInt(structure[3]), Integer.parseInt(structure[4]),
                                        structure[5], structure[6].equals("true"), structure[8].equals("true"), structure[7], structure[9]);
                            }
                            else if (structure[0].equals("tree") || structure[0].equals("bush") || structure[0].equals("stone")) {
                                structures[index++] = new Decoration(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                                        Integer.parseInt(structure[3]), Integer.parseInt(structure[4]), structure[0]);
                            }
                            else if (structure[0].equals("bridge")) {
                                structures[index++] = new Bridge(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                                        Integer.parseInt(structure[3]), structure[4], structure[5].equals("true"), structure[0]);
                            }
                            else if (structure[0].equals("portal")) {
                                structures[index++] = new Portal(gp, Integer.parseInt(structure[1]), Integer.parseInt(structure[2]),
                                        Integer.parseInt(structure[3]), Integer.parseInt(structure[4]), structure[5], structure[6]);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.err.println("Ошибка в формате структуры: " + part);
                            e.printStackTrace();
                        }
                    }
                }
            }





            if (structures != null) {
                if (collision != null) {
                    collision.loadStructure(structures);
                }
                if (interaction != null) {
                    interaction.loadStructure(structures);
                }
            }

        } catch (IOException | NumberFormatException e) {
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

    public Board getBoard() {
        return board;
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
            if(structure != null) {
                if(GamePanel.keys[5]) { // Если открыть-закрыть дверь

                    isCooldown = true;
                    if(structure.getName().equals("door") && structure.getState()) {
                        Door door = (Door) structure;
                        door.changeLock();
                    } else if(structure.getName().equals("hatch")) {
                        Hatch hatch = (Hatch) structure;
                        scene = new CutScene(gp, hatch.getRoute());
                        isCutScene = true;
                    } else if(structure.getName().equals("chest")) {
                        if(structure.getState()) {
                            int index = 0;
                            for (int i = 0; i < structures.length; i++) {
                                if (structures[i].equals(structure)) {
                                    index = i;
                                    break;
                                }
                            }
                            Chest chest = (Chest) structure;
                            objects[index] = chest.openChest(objects[index]);
                        } else {
                            Chest chest = (Chest) structure;
                            board = new Board(gp, 175, 150, chest.getFilePath());
                            gp.state = GamePanel.GameState.GameOpenBoard;
                        }
                    } else if(structure.getName().equals("portal")) {
                        gp.sceneChanger.setNumberActiveScene(gp.sceneChanger.getNumberActiveScene() + 1);
                    }

                    collision.reloadMap(worldMap, structures);
                    interaction.reloadMap(structures, objects);
                }

                if(GamePanel.keys[9]) { // k
                    isCooldown = true;
                    if(structure.getName().equals("chest")) {
                        Chest chest = (Chest) structure;
                        KeySpell keySpell;
                        Spell[] spells = gp.player.getSpells();
                        if (spells != null) {
                            for (Spell spell : spells) {
                                if (spell != null && spell.getName().equals("key")) {
                                    keySpell = (KeySpell) spell;
                                    if (chest.checkValues(keySpell.getCurrentFirst(),
                                            keySpell.getCurrentSecond(),
                                            keySpell.getCurrentThird(),
                                            keySpell.getCurrentFourth()) && !keySpell.getIsRecharge() && gp.player.getMana() >= 10) {
                                        chest.unblockChest();
                                        keySpell.setIsRecharge(true);
                                    }
                                }
                            }

                        }
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
                                        gp.tablet.getTextRedactor().addSpell("/resources/spells/repair");
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

        if (enemies != null) {
            for(Enemy enemy : enemies) {
                if(enemy != null) {
                    enemy.update();
                }
            }
        }

        updateBoard();

        Spell[] spells = gp.player.getSpells();
        if(spells != null) {
            for(Spell spell: spells) {
                if(spell != null) {
                    spell.recharge();
                }
            }
        }

    }

    public void updateBoard() {
        if(board != null) {
            board.update();
        }
    }

    public void drawRechargeIconSpells(Graphics2D g) {
        Spell[] spells = gp.player.getSpells();
        if(spells != null) {
            for(Spell spell: spells) {
                if(spell != null) {
                    spell.draw(g);
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
        if (structures == null) return;
        for(Structure s: structures) {
            if(s != null) {
                s.draw(g);
            }
        }
    }

    public void drawObjects(Graphics2D g) {
        if(objects == null) return;
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
        drawBoard(g);
        drawRechargeIconSpells(g);
    }

    private void drawEnemies(Graphics2D g) {
        if (enemies == null) return;
        for(Enemy enemy : enemies) {
            if(enemy != null) {
                enemy.draw(g);
            }
        }
    }

    private void drawBoard(Graphics2D g) {
        if(board != null) {
            board.draw(g);
        }
    }
}
