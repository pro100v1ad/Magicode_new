package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.GameObject;
import main.java.com.magicode.gameplay.world.objects.Book;
import main.java.com.magicode.gameplay.world.objects.Key;
import main.java.com.magicode.gameplay.world.objects.Wrench;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Chest extends Structure {

    private boolean isLock;
    private GamePanel gp;
    private int objectCode;
    private String objectName;
    private String filePath;

    private int[] needDefaultFirst, needDefaultSecond;
    private String[] needDefaultThird;
    private boolean[] needDefaultFourth;

    public Chest(GamePanel gp, int x, int y, int w, int h, String code, boolean isLock, boolean state, String direction, String filePath) {
        this.gp = gp;
        this.name = "chest";
        String[] parts = code.split(":"); // Немного изменил парсинг (для работы третьего опционального параметра)
        this.code = Integer.parseInt(parts[0]);
        this.radius = Integer.parseInt(parts[1]);
        this.objectCode = Integer.parseInt(parts[0]); // Сундук не может быть пустым теперь
        this.objectName = parts[2];

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.filePath = (filePath.equals("null")) ? null : filePath;

        this.isLock = isLock;
        this.state = state;
        this.direction = direction;
        loadDefaultValue();
        loadImage();

//        System.out.println(Arrays.toString(needDefaultFirst));
//        System.out.println(Arrays.toString(needDefaultSecond));
//        System.out.println(Arrays.toString(needDefaultThird));
//        System.out.println(Arrays.toString(needDefaultFourth));
    }
    private void loadDefaultValue() {
        if(filePath == null) {
            return;
        }
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + filePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }
            String[] parts = line.split("_");
            needDefaultFirst = parseNullableIntArray(parts[0]);
            needDefaultSecond = parseNullableIntArray(parts[1]);
            needDefaultThird = parseNullableStringArray(parts[2]);
            needDefaultFourth = parseNullableBooleanArray(parts[3]);


        }
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки информации о задании для сундука: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Парсинг int[] или null
    private static int[] parseNullableIntArray(String str) {
        if (str.equals("null")) {
            return null;
        }
        if (str.indexOf(':') != -1) {
            String[] elements = str.split(":");
            int[] result = new int[elements.length];
            for (int i = 0; i < elements.length; i++) {
                result[i] = Integer.parseInt(elements[i]);
            }
            return result;
        } else {
            return new int[]{Integer.parseInt(str)};
        }
    }

    // Парсинг String[] или null
    private static String[] parseNullableStringArray(String str) {
        if (str.equals("null")) {
            return null;
        }
        if (str.indexOf(':') != -1) {
            return str.split(":");
        } else {
            return new String[]{str};
        }
    }

    // Парсинг boolean[] или null
    private static boolean[] parseNullableBooleanArray(String str) {
        if (str.equals("null")) {
            return null;
        }
        if (str.indexOf(':') != -1) {
            return new boolean[]{true, false};
        } else {
            return new boolean[]{Boolean.parseBoolean(str)};
        }
    }

    private void loadImage() {
        if(direction.equals("up")) {
            if(isLock) {
                if(state) image = gp.textureAtlas.textures[12][0].getTexture();
                else image = gp.textureAtlas.textures[12][8].getTexture();
            }
            else image = gp.textureAtlas.textures[12][4].getTexture();
        }
        if(direction.equals("down")) {
            if(isLock) {
                if(state) image = gp.textureAtlas.textures[12][1].getTexture();
                else image = gp.textureAtlas.textures[12][9].getTexture();
            }
            else image = gp.textureAtlas.textures[12][5].getTexture();
        }
        if(direction.equals("right")) {
            if(isLock) {
                if(state) image = gp.textureAtlas.textures[12][2].getTexture();
                else image = gp.textureAtlas.textures[12][10].getTexture();
            }
            else image = gp.textureAtlas.textures[12][6].getTexture();
        }
        if(direction.equals("left")) {
            if(isLock) {
                if(state) image = gp.textureAtlas.textures[12][3].getTexture();
                else image = gp.textureAtlas.textures[12][11].getTexture();
            }
            else image = gp.textureAtlas.textures[12][7].getTexture();
        }
    }

    public boolean getLock() {
        return isLock;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getFilePath() {
        return filePath;
    }

    public int[] getNeedDefaultFirst() {
        return needDefaultFirst;
    }

    public int[] getNeedDefaultSecond() {
        return needDefaultSecond;
    }

    public String[] getNeedDefaultThird() {
        return needDefaultThird;
    }

    public boolean[] getNeedDefaultFourth() {
        return needDefaultFourth;
    }

    public void unblockChest() {
        this.state = true;
        loadImage();
    }

    public boolean checkValues(int first, int second, String third, boolean fourth) {
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;

        // Проверка для first и second (должны быть на одинаковых индексах)
        if (needDefaultFirst != null && needDefaultSecond != null) {
            // Ищем минимальную длину, чтобы не выйти за границы массивов
            int minLength = Math.min(needDefaultFirst.length, needDefaultSecond.length);
            for (int i = 0; i < minLength; i++) {
                if (needDefaultFirst[i] == first && needDefaultSecond[i] == second) {
                    flag1 = true;
                    flag2 = true;
                    break; // Нашли совпадение на одном индексе - можно выходить
                }
            }
        } else {
            // Если один из массивов null, считаем проверку пройденной
            flag1 = (needDefaultFirst == null);
            flag2 = (needDefaultSecond == null);
        }

        // Остальные проверки (как в оригинале)
        if (needDefaultThird != null && third != null) {
            for (int i = 0; i < needDefaultThird.length; i++) {
                if (needDefaultThird[i] != null && needDefaultThird[i].equals(third)) {
                    flag3 = true;
                    break;
                }
            }
        } else {
            flag3 = true;
        }

        if (needDefaultFourth != null) {
            for (int i = 0; i < needDefaultFourth.length; i++) {
                if (needDefaultFourth[i] == fourth) {
                    flag4 = true;
                    break;
                }
            }
        } else {
            flag4 = true;
        }

        return flag1 && flag2 && flag3 && flag4;
    }

    public GameObject openChest(GameObject object) {
        isLock = false;
        code = 0;
        radius = 0;
        loadImage();
        return setObjectByCode(object, objectName);
    }

    private GameObject setObjectByCode(GameObject object, String objectName) { // Добавил функцию
        switch (objectName) {
            case "key":
                object = new Key(gp, x, y, this.objectCode);
                break;
            case "book":
                object = new Book(gp, x, y, this.objectCode);
                break;
            case "wrench":
                object = new Wrench(gp, x, y, this.objectCode);
                break;

            default:
                System.out.println("Error = NULL");
                object = null;
        }
        return object;
    }

    @Override
    public void draw(Graphics2D g) {

        int screenX = (int) (x - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int) (y - gp.player.getWorldY() + gp.player.getScreenY());

        if (x + GamePanel.tileSize > gp.player.getWorldX() - gp.player.getScreenX() &&
                x - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                y + GamePanel.tileSize > gp.player.getWorldY() - gp.player.getScreenY() &&
                y - GamePanel.tileSize * 4 < gp.player.getWorldY() + gp.player.getScreenY())
        {
            g.drawImage(image, screenX, screenY, w, h, null);
        }

    }
}