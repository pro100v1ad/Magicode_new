package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.GameObject;
import main.java.com.magicode.gameplay.world.objects.Book;
import main.java.com.magicode.gameplay.world.objects.Key;
import main.java.com.magicode.gameplay.world.objects.Wrench;

import java.awt.*;

public class Chest extends Structure {

    private boolean isLock;
    private GamePanel gp;
    private int objectCode;
    private String objectName;

    public Chest(GamePanel gp, int x, int y, int w, int h, String code, boolean isLock, boolean state, String direction) {
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


        this.isLock = isLock;
        this.state = state;
        this.direction = direction;

        loadImage();
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