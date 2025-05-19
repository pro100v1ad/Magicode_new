package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.GameObject;
import main.java.com.magicode.gameplay.world.objects.Book;
import main.java.com.magicode.gameplay.world.objects.Gun;
import main.java.com.magicode.gameplay.world.objects.Key;
import main.java.com.magicode.gameplay.world.objects.Wrench;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Chest extends Structure {

    private boolean isLock;
    private GamePanel gp;
    private int objectCode;
    private String objectName;
    private String filePath;

    private String condition;

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
            condition = line;

        }
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки информации о задании для сундука: " + e.getMessage());
            e.printStackTrace();
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


    public void unblockChest() {
        this.state = true;
        loadImage();
    }

    public boolean checkValues(int first, int second, String third, boolean fourth) {

        if(condition.equals("arg1 + arg2 == 10")) {
            return first + second == 10;
        }
        if(condition.equals("arg1 + arg2 == 8")) {
            return first + second == 8;
        }
        if(condition.equals("arg1 + 1 > arg2 - 1")) {
            return first + 1 > second - 1;
        }
        if(condition.equals("arg4")) {
            return fourth;
        }
        if(condition.equals("arg3 == \"key2\"")) {
            return third.equals("key2");
        }

        return false;
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
            case "gun":
                object = new Gun(gp, x, y, this.objectCode);
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