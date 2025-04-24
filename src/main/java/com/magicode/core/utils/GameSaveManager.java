package main.java.com.magicode.core.utils;

import main.java.com.magicode.gameplay.world.Layer;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.structures.Chest;
import main.java.com.magicode.gameplay.world.structures.Door;
import main.java.com.magicode.gameplay.world.structures.Hatch;

import java.io.*;

public class GameSaveManager {
    private String saveFilePathBackground = "saves/backgroundSave.txt";
    private String saveFilePathStructure = "saves/structureSave.txt";
    // Сохранение игры
    public void saveGame(Layer[][] worldMap, Structure[] structures) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePathBackground))) {
            writer.write(worldMap[0].length + " " + worldMap.length + "\n");
            for(int i = 0; i < worldMap.length; i++) {
                for(int j = 0; j < worldMap[0].length; j++) {
                    writer.write(worldMap[i][j].getLayer(1) + ":" + worldMap[i][j].getLayer(2) + " ");
                }
                writer.write("\n");
            }



            System.out.println("BackGround сохранен в файл: " + saveFilePathBackground);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении заднего фона: " + e.getMessage());
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePathStructure))) {

            for(int i = 0; i < structures.length; i++) {
                if(structures[i] != null) {
                    if(structures[i].getName().equals("door")) {
                        Door door = (Door) structures[i];
                        writer.write("door_" + door.getX() + "_" + door.getY() + "_" + door.getW() + "_" + door.getH() +
                                "_" + door.getCode() + ":" + door.getRadius() + "_" + door.getLock() + "_" + door.getDirection()
                                + "_" + door.getState() + " ");
                    }
                    if(structures[i].getName().equals("hatch")) {
                        Hatch hatch = (Hatch) structures[i];
                        writer.write("hatch_" + hatch.getX() + "_" + hatch.getY() + "_" + hatch.getW() + "_" + hatch.getH() +
                                "_" + hatch.getCode() + ":" + hatch.getRadius() + "_" + hatch.getState() + "_" + hatch.getDefaultRoute() + " ");
                    }
                    if(structures[i].getName().equals("chest")) {
                        Chest chest = (Chest) structures[i];
                        writer.write("chest_" + chest.getX() + "_" + chest.getY() + "_" + chest.getW() + "_" + chest.getH() +
                                "_" + chest.getCode() + ":" + chest.getRadius() + "_" + chest.getLock() + "_" + chest.getDirection()
                                + "_" + chest.getState() + " ");
                    }

                }
            }



            System.out.println("Structure сохранен в файл: " + saveFilePathStructure);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении структур: " + e.getMessage());
        }
    }

    // Загрузка игры
    public String getSaveFilePathBackground() {
        return saveFilePathBackground;
    }
    public String getSaveFilePathStructure() {
        return saveFilePathStructure;
    }

    // Проверка и создание папки для сохранений
    public void ensureSaveDirectoryExists() {
        File saveDir = new File("saves");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    public boolean checkIfFilesExist() {

        // Создаем объекты File для файлов
        File file1 = new File(saveFilePathBackground);
        File file2 = new File(saveFilePathStructure);

        // Проверяем, существуют ли оба файла
        return file1.exists() && file2.exists();
    }


}
