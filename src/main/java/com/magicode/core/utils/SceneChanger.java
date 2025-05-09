package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.SceneLoader;
import main.java.com.magicode.gameplay.world.GameObject;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.structures.Chest;
import main.java.com.magicode.gameplay.world.structures.Door;
import main.java.com.magicode.gameplay.world.structures.Hatch;
import main.java.com.magicode.ui.gamestate.Directory;

import java.awt.*;
import java.io.*;

public class SceneChanger {

    private String[] sceneInfo;
    private final String FILEPATH = "/resources/levels/sceneInfo";
    private int numberActiveScene;

    private GamePanel gp;

    public SceneChanger(GamePanel gp, boolean isStart, String filePath) {
        this.gp = gp;

        if(isStart) {
            numberActiveScene = 0;
            gp.sceneLoader = new SceneLoader(gp, true, null, null, null, null); // Добавлен null для enemiesPath
            loadSceneInfo();
        } else {
            if(filePath != null) {
                readFile(filePath);
                gp.sceneLoader = new SceneLoader(gp, false,
                        gp.saveManager.getSaveFilePathBackground(),
                        gp.saveManager.getSaveFilePathStructure(),
                        gp.saveManager.getSaveFilePathObjects(),
                        null); // Добавлен null для enemiesPath
            }
            loadSceneInfo();
        }
    }

    public void readFile(String filePath) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;
            }

            numberActiveScene = Integer.parseInt(line);

            System.out.println("Информация о сцене успешно загружен!");
        } catch (Exception e) {
            System.out.println("Ошибка загрузки информации о сцене!");
        }
    }

    private void loadSceneInfo() {
        try (InputStream is = getClass().getResourceAsStream(FILEPATH)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + FILEPATH);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }
            sceneInfo = new String[3];
            sceneInfo[0] = line;
            sceneInfo[1] = br.readLine();
            sceneInfo[2] = br.readLine();


            System.out.println("Успешная загрузка информации о сценах: " + FILEPATH);
        }
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки информации о сценах: " + e.getMessage());
        }
    }

    public void setNumberActiveScene(int numberActiveScene) {
        if(this.numberActiveScene != numberActiveScene) { // Если сцены различаются, то происходит замена
            loadScene(numberActiveScene);
        }
        this.numberActiveScene = numberActiveScene;
    }

    public int getNumberActiveScene() {
        return numberActiveScene;
    }

    public void loadScene(int index) {
        if (index < 0 || index >= sceneInfo.length) {
            System.err.println("Неверный индекс сцены: " + index);
            return;
        }

        String[] sceneData = sceneInfo[index].split(" ");
        if (sceneData.length < 3) {
            System.err.println("Неверный формат данных сцены");
            return;
        }

        try {
            // Установка позиции игрока
            gp.getPlayer().setWorldX(Integer.parseInt(sceneData[1]));
            gp.getPlayer().setWorldY(Integer.parseInt(sceneData[2]));

            // Формирование путей
            String sceneBasePath = sceneData[0]; // Будет "/levels/scenes/start/"
            String enemiesPath = sceneBasePath + "enemies";

            System.out.println("Проверяем путь к врагам: " + enemiesPath);

            // Проверка существования файла в ресурсах
            try (InputStream is = getClass().getResourceAsStream(enemiesPath)) {
                if (is == null) {
                    System.err.println("Файл врагов не найден: " + enemiesPath);
                    enemiesPath = null;
                } else {
                    System.out.println("Файл врагов успешно обнаружен!");
                }
            }

            // Создаем новый SceneLoader
            gp.sceneLoader = new SceneLoader(
                    gp,
                    true,
                    sceneBasePath + "background",
                    sceneBasePath + "structure",
                    sceneBasePath + "objects",
                    enemiesPath
            );

        } catch (NumberFormatException | IOException e) {
            System.err.println("Ошибка загрузки сцены: " + e.getMessage());
        }
    }

}
