package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.SceneLoader;

import java.io.*;

public class SceneChanger { // Класс отвечающий за смену сцен

    private String[] sceneInfo;
    private final String FILEPATH = "/resources/levels/sceneInfo";
    private int numberActiveScene;

    private GamePanel gp;

    public SceneChanger(GamePanel gp, boolean isStart, String filePath) {
        this.gp = gp;

        if(isStart) {
            numberActiveScene = 0;
            gp.sceneLoader = new SceneLoader(gp, true, null, null, null, null, null); // Добавлен null для enemiesPath
            loadSceneInfo();
        } else {
            if(filePath != null) {
                readFile(filePath);
                gp.sceneLoader = new SceneLoader(gp, false,
                        gp.saveManager.getSaveFilePathBackground(),
                        gp.saveManager.getSaveFilePathStructure(),
                        gp.saveManager.getSaveFilePathObjects(),
                        gp.saveManager.getSaveFilePathEnemy(),
                        gp.saveManager.getSaveFilePathSpells());
            }
            loadSceneInfo();
        }
    }

    public void readFile(String filePath) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                return;
            }

            numberActiveScene = Integer.parseInt(line);

        } catch (Exception e) {
            System.out.println("Ошибка загрузки информации о сцене!");
        }
    }

    private void loadSceneInfo() {
        try (InputStream is = getClass().getResourceAsStream(FILEPATH);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            // Сначала считаем количество строк
            int lineCount = 0;
            while (br.readLine() != null) {
                lineCount++;
            }

            // Возвращаемся в начало файла
            is.close();
            InputStream is2 = getClass().getResourceAsStream(FILEPATH);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));

            // Инициализируем массив
            sceneInfo = new String[lineCount];

            // Заполняем массив
            String line;
            int index = 0;
            while ((line = br2.readLine()) != null) {
                sceneInfo[index++] = line;
            }

            System.out.println("Загружено сцен: " + sceneInfo.length);

        } catch (Exception e) {
            System.err.println("Ошибка загрузки sceneInfo: " + e.getMessage());
            sceneInfo = new String[0];
        }
    }

    public void setNumberActiveScene(int index) {
        if(index < 0 || index >= sceneInfo.length) {
            System.err.println("Ошибка: Неверный индекс сцены " + index +
                    ". Доступно сцен: " + sceneInfo.length);
            return; // Не меняем сцену если индекс невалидный
        }

        if(this.numberActiveScene != index) {
            loadScene(index);
        }
        this.numberActiveScene = index;
    }

    public int getNumberActiveScene() {
        return numberActiveScene;
    }

    public void loadScene(int index) {
        if(index < 0 || index >= sceneInfo.length) {
            System.err.println("Неверный индекс сцены: " + index);
            return;
        }

        String[] sceneData = sceneInfo[index].split(" ");
        if(sceneData.length < 3) {
            System.err.println("Неверный формат sceneInfo для сцены " + index);
            return;
        }

        try {
            gp.sceneLoader = new SceneLoader(
                    gp,
                    true,
                    sceneData[0] + "background",
                    sceneData[0] + "structure",
                    null,
                    sceneData[0] + "enemies",
                    null);

            // Установка позиции игрока
            gp.player.setWorldX(Integer.parseInt(sceneData[1]));
            gp.player.setWorldY(Integer.parseInt(sceneData[2]));

            this.numberActiveScene = index;
            gp.changeMusic();
        } catch (Exception e) {
            System.err.println("Ошибка загрузки сцены " + index + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getSceneCount() {
        return sceneInfo != null ? sceneInfo.length : 0;
    }
}

