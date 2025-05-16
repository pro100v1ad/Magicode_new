package main.java.com.magicode.core.utils;

import main.java.com.magicode.gameplay.entity.Enemy;
import main.java.com.magicode.gameplay.entity.EnemyType.Slime;
import main.java.com.magicode.gameplay.entity.Player;
import main.java.com.magicode.gameplay.world.GameObject;
import main.java.com.magicode.gameplay.world.Layer;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.objects.Book;
import main.java.com.magicode.gameplay.world.objects.Key;
import main.java.com.magicode.gameplay.world.objects.Wrench;
import main.java.com.magicode.gameplay.world.structures.*;
import main.java.com.magicode.spells.Spell;
import main.java.com.magicode.spells.spells.KeySpell;
import main.java.com.magicode.ui.gamestate.Tablet;
import main.java.com.magicode.ui.gamestate.tablet.EditArea;

import java.io.*;

public class GameSaveManager {
    private final String saveFilePathBackground = "saves/backgroundSave.txt";
    private final String saveFilePathStructure = "saves/structureSave.txt";
    private final String saveFilePathPlayer = "saves/playerSave.txt";
    private final String saveFilePathObjects = "saves/objectSave.txt";
    private final String saveFilePathEnemy = "saves/enemySave.txt";
    private final String saveFilePathSpells = "saves/spellsSave.txt";
    private final String saveFilePathTabletInfo = "saves/tabletInfo.txt";
    private final String saveFilePathSceneInfo = "saves/sceneInfo.txt";

    // Сохранение игры
    public void saveGame(Layer[][] worldMap, Structure[] structures, Player player, SceneChanger sceneChanger, GameObject[] objects, Enemy[] enemies, Spell[] spells, Tablet tablet) {
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
            if(structures == null) {
                writer.write("");
            } else {
                for (int i = 0; i < structures.length; i++) {
                    if (structures[i] != null) {
                        if (structures[i].getName().equals("door")) {
                            Door door = (Door) structures[i];
                            writer.write("door_" + door.getX() + "_" + door.getY() + "_" + door.getW() + "_" + door.getH() +
                                    "_" + door.getCode() + ":" + door.getRadius() + "_" + door.getLock() + "_" + door.getDirection()
                                    + "_" + door.getState() + " ");
                        }
                        if (structures[i].getName().equals("hatch")) {
                            Hatch hatch = (Hatch) structures[i];
                            writer.write("hatch_" + hatch.getX() + "_" + hatch.getY() + "_" + hatch.getW() + "_" + hatch.getH() +
                                    "_" + hatch.getCode() + ":" + hatch.getRadius() + "_" + hatch.getState() + "_" + hatch.getDefaultRoute() + " ");
                        }
                        if (structures[i].getName().equals("chest")) {
                            Chest chest = (Chest) structures[i];
                            writer.write("chest_" + chest.getX() + "_" + chest.getY() + "_" + chest.getW() + "_" + chest.getH() +
                                    "_" + chest.getCode() + ":" + chest.getRadius() + ":" + chest.getObjectName() + "_" + chest.getLock() + "_" + chest.getDirection()
                                    + "_" + chest.getState() + "_" + chest.getFilePath() + " ");
                        }
                        if(structures[i].getName().equals("tree") || structures[i].getName().equals("stone") || structures[i].getName().equals("bush")) {
                            Decoration decoration = (Decoration) structures[i];
                            writer.write(decoration.getName() + "_" + decoration.getX() + "_" + decoration.getY() + "_" +
                                    decoration.getW() + "_" + decoration.getH() + " ");
                        }
                        if(structures[i].getName().equals("bridge")) {
                            Bridge bridge = (Bridge) structures[i];
                            writer.write("bridge_" + bridge.getX() + "_" + bridge.getY() + "_" + bridge.getLen() + "_" +
                                    bridge.getDirection() + "_" + bridge.getBreak() + " ");
                        }
                        if(structures[i].getName().equals("portal")) {
                            Portal portal = (Portal) structures[i];
                            writer.write("portal_" + portal.getX() + "_" + portal.getY() + "_" + portal.getW() + "_" + portal.getH() +
                                    "_" + portal.getCode() + ":" + portal.getRadius() + "_" + portal.getDirection());
                        }

                    }
                }
            }


            System.out.println("Structure сохранен в файл: " + saveFilePathStructure);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении структур: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePathPlayer))) {

            writer.write((int)(player.getWorldX()) + "_" + (int)(player.getWorldY()) + "\n");

            writer.write(player.getCountBook() + "\n");

            writer.write((int)player.getMaxHealth() + "_" + (int)player.getHealth() + "\n");

            writer.write((int)player.getMaxMana() + "_" + (int)player.getMana() + "\n");



            System.out.println("Player сохранен в файл: " + saveFilePathStructure);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении игрока: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePathSceneInfo))) {

            writer.write(sceneChanger.getNumberActiveScene() + "");

            System.out.println("sceneInfo сохранен в файл: " + saveFilePathSceneInfo);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении информации о сцене: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePathObjects))) {
            if(objects == null) {
                writer.write("");
            } else {
                writer.write(objects.length + "\n");

                for (GameObject object : objects) { // Сохранение объектов
                    if (object == null) continue;
                    if (object.getName().equals("key")) {
                        Key key = (Key) object;
                        writer.write("key_" + key.getPosX() + "_" + key.getPosY() + "_" + key.getCode() + "\n");
                    }
                    if (object.getName().equals("book")) {
                        Book book = (Book) object;
                        writer.write("book_" + book.getPosX() + "_" + book.getPosY() + "_" + book.getCode() + "\n");
                    }
                    if (object.getName().equals("wrench")) {
                        Wrench wrench = (Wrench) object;
                        writer.write("wrench_" + wrench.getPosX() + "_" + wrench.getPosY() + "_" + wrench.getCode() + "\n");
                    }


                }
            }

            System.out.println("objects сохранен в файл: " + saveFilePathObjects);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении объектов: " + e.getMessage());
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePathEnemy))) {
            if(enemies == null) {
                writer.write("");
            } else {
                writer.write(enemies.length + "\n");
                for (Enemy enemy : enemies) { // Сохранение объектов
                    if (enemy == null) continue;
                    if (enemy.getName().equals("slime")) {
                        Slime slime = (Slime) enemy;
                        writer.write("slime_" + (int)slime.getWorldX() + "_" +
                                (int)slime.getWorldY() + "_" + slime.getAggressive() + "\n");
                    }

                }
            }

            System.out.println("enemy сохранен в файл: " + saveFilePathEnemy);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении врагов: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePathSpells))) {
            if(spells == null) {
                writer.write("");
            } else {
                writer.write(spells.length + "\n");
                for (Spell spell : spells) { // Сохранение объектов
                    if (spell == null) continue;
                    if (spell.getName().equals("key")) {
                        KeySpell keySpell = (KeySpell) spell;
                        writer.write(keySpell.getVisible() + "_key");
                        for(String string: keySpell.getSaveChangeText()) {
                            writer.write("_" + string);
                        }
                        writer.write("\n");
                    }

                }
            }

            System.out.println("spells сохранен в файл: " + saveFilePathSpells);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении заклинаний: " + e.getMessage());
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePathTabletInfo))) {
            if(tablet == null) {
                writer.write("");
            } else {
                writer.write(tablet.getN() + "_" + tablet.getPlus() + "_" + tablet.getMinus() + "_" + tablet.getExclamationMark() + "\n");
                writer.write(tablet.getEditAreas().length + "\n");
                for(EditArea editArea: tablet.getEditAreas()) {
                    if(editArea != null) {
                        if(editArea.getName().contains("key")) {
                            writer.write("key_" + editArea.getName().charAt(editArea.getName().length() - 1));

                            if((editArea.getCurrentText().isEmpty())) {
                                writer.write("_null\n");
                            } else {
                                writer.write("_" + editArea.getCurrentText() + "\n");
                            }

                        }

                        // Сюда остальные заклинания
                    }
                }

            }

            System.out.println("Данные планшета сохранены в файл: " + saveFilePathTabletInfo);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении данных планшета: " + e.getMessage());
        }



    }

    // Загрузка игры
    public String getSaveFilePathBackground() {
        return saveFilePathBackground;
    }
    public String getSaveFilePathStructure() {
        return saveFilePathStructure;
    }
    public String getSaveFilePathPlayer() {
        return saveFilePathPlayer;
    }
    public String getSaveFilePathSceneInfo() {
        return saveFilePathSceneInfo;
    }
    public String getSaveFilePathObjects() {
        return saveFilePathObjects;
    }
    public String getSaveFilePathEnemy() {
        return saveFilePathEnemy;
    }
    public String getSaveFilePathSpells() {
        return saveFilePathSpells;
    }
    public String getSaveFilePathTabletInfo() {
        return saveFilePathTabletInfo;
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
        File file3 = new File(saveFilePathPlayer);
        File file4 = new File(saveFilePathSceneInfo);
        File file5 = new File(saveFilePathObjects);
        File file6 = new File(saveFilePathEnemy);
        File file7 = new File(saveFilePathSpells);
        File file8 = new File(saveFilePathTabletInfo);

        // Проверяем, существуют ли оба файла
        return file1.exists() && file2.exists() && file3.exists() && file4.exists() && file5.exists() && file6.exists()
                && file7.exists() && file8.exists();
    }


}
