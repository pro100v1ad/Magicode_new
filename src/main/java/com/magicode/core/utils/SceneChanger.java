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
        try (InputStream is = getClass().getResourceAsStream(FILEPATH)) {
            if (is == null) {
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                return;// Если файл закончился раньше, чем ожидалось
            }
            sceneInfo = new String[3];
            sceneInfo[0] = line;
            sceneInfo[1] = br.readLine();
            sceneInfo[2] = br.readLine();

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
        gp.sceneLoader = new SceneLoader(
                gp,
                true,
                sceneInfo[index].split(" ")[0] + "background",
                sceneInfo[index].split(" ")[0] + "structure",
                null,
                sceneInfo[index].split(" ")[0] + "enemies",
                null);
        gp.player.setWorldX(Integer.parseInt(sceneInfo[index].split(" ")[1]));
        gp.player.setWorldY(Integer.parseInt(sceneInfo[index].split(" ")[2]));
        gp.changeMusic();
    }
}
