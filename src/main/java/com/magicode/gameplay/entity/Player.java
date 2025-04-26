package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.Animation;
import main.java.com.magicode.core.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;

import static java.lang.Math.sqrt;

public class Player extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private GamePanel gp;
    private Animation[] animations;
    private ResourceLoader resourceLoader;

    private final int screenX;
    private final int screenY;
    private String lastDirection = "";

    public Player(GamePanel gp, String filePath){
        this.gp = gp;
        resourceLoader = new ResourceLoader();

        screenX = GamePanel.WIDTH/2 - GamePanel.tileSize;
        screenY = GamePanel.HEIGHT/2 - GamePanel.tileSize*2;

        collisionWidth = (int)(GamePanel.tileSize*1.7/2);
        collisionHeight = GamePanel.tileSize*3/2;
        collisionCode = 2;

        loadAnimation();

        if(filePath != null) {
            loadPlayerFromFile(filePath);
        } else {
            setDefaultValues();
        }

    }

    public void loadAnimation() {
        animations = new Animation[4];

        // Анимация "вниз" (новый массив для каждой анимации)
        BufferedImage[] downImages = new BufferedImage[4];
        downImages[0] = resourceLoader.loadImage("/resources/player/down/playerDown1.png");
        downImages[1] = resourceLoader.loadImage("/resources/player/down/playerDown2.png");
        downImages[2] = resourceLoader.loadImage("/resources/player/down/playerDown3.png");
        downImages[3] = resourceLoader.loadImage("/resources/player/down/playerDown4.png");
        animations[0] = new Animation(downImages, 5);

        // Анимация "вверх"
        BufferedImage[] upImages = new BufferedImage[4];
        upImages[0] = resourceLoader.loadImage("/resources/player/up/playerUp1.png");
        upImages[1] = resourceLoader.loadImage("/resources/player/up/playerUp2.png");
        upImages[2] = resourceLoader.loadImage("/resources/player/up/playerUp3.png");
        upImages[3] = resourceLoader.loadImage("/resources/player/up/playerUp4.png");
        animations[1] = new Animation(upImages, 5);

        // Анимация "влево"
        BufferedImage[] leftImages = new BufferedImage[4];
        leftImages[0] = resourceLoader.loadImage("/resources/player/left/playerLeft1.png");
        leftImages[1] = resourceLoader.loadImage("/resources/player/left/playerLeft2.png");
        leftImages[2] = resourceLoader.loadImage("/resources/player/left/playerLeft3.png");
        leftImages[3] = resourceLoader.loadImage("/resources/player/left/playerLeft4.png");
        animations[2] = new Animation(leftImages, 5);

        // Анимация "вправо"
        BufferedImage[] rightImages = new BufferedImage[4];
        rightImages[0] = resourceLoader.loadImage("/resources/player/right/playerRight1.png");
        rightImages[1] = resourceLoader.loadImage("/resources/player/right/playerRight2.png");
        rightImages[2] = resourceLoader.loadImage("/resources/player/right/playerRight3.png");
        rightImages[3] = resourceLoader.loadImage("/resources/player/right/playerRight4.png");
        animations[3] = new Animation(rightImages, 5);
    }

    public void setDefaultValues() {
//        worldX = 10;
//        worldY = 10;

        worldX = GamePanel.tileSize*35;
        worldY = GamePanel.tileSize*17;
        float pixelsPerSecond = 200f;
        speed = (pixelsPerSecond * GamePanel.scale) / GamePanel.UPDATE_RATE; // scale минимум 1/4 и максимум 2.
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public boolean loadPlayerFromFile(String filePath) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return false;// Если файл закончился раньше, чем ожидалось
            }

            String parts[] = line.split("_");
            worldX = Integer.parseInt(parts[0]);
            worldY = Integer.parseInt(parts[1]);
            // Потом поменяю //////////
            float pixelsPerSecond = 200f;
            speed = (pixelsPerSecond * GamePanel.scale) / GamePanel.UPDATE_RATE; // scale минимум 1/4 и максимум 2.
            ///////////////////

            System.out.println("Игрок успешно загружен!");
        } catch (Exception e) {
            System.out.println("Ошибка загрузки игрока!");
            return false;
        }


        return true;
    }


    public void update() {
//        System.out.println("Pos: " + worldX + " : " + worldY);
        if(gp.sceneLoader.getCutScene()) {
            return;
        }
        if(gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }
        // Определяет направление движения все 8
        if (GamePanel.keys[0] && GamePanel.keys[3]) {
            direction = "up_right";
        } else if (GamePanel.keys[0] && GamePanel.keys[1]) {
            direction = "up_left";
        } else if (GamePanel.keys[2] && GamePanel.keys[1]) {
            direction = "down_left";
        } else if (GamePanel.keys[2] && GamePanel.keys[3]) {
            direction = "down_right";
        } else if(GamePanel.keys[0]) {
            direction = "up";
        } else if(GamePanel.keys[1]) {
            direction = "left";
        } else if(GamePanel.keys[2]) {
            direction = "down";
        } else if(GamePanel.keys[3]) {
            direction = "right";
        } else direction = "null";
        //Проверка коллизии
        if(direction.equals("up_right")) {
            for(int i = 0; i < speed/sqrt(2); i++) {// Обработка движения вверх
                if(gp.getCollision().checkCollisionUp(this)) {
                    if(worldY > - 0) worldY -= 1;
                }
                if(gp.getCollision().checkCollisionRight(this)) {
                    if (worldX < gp.getWorldWidth()*GamePanel.tileSize-GamePanel.tileSize*2-1) worldX += 1;
                }
            }
        } else if(direction.equals("up_left")) {
            for(int i = 0; i < speed/sqrt(2); i++) {// Обработка движения вверх
                if(gp.getCollision().checkCollisionUp(this)) {
                    if(worldY > - 0) worldY -= 1;
                }
                if(gp.getCollision().checkCollisionLeft(this)) {
                    if(worldX > 1) worldX -= 1;
                }
            }
        } else if(direction.equals("down_left")) {
            for(int i = 0; i < speed/sqrt(2); i++) {// Обработка движения вверх
                if(gp.getCollision().checkCollisionDown(this)) {
                    if(worldY < gp.getWorldHeight()*GamePanel.tileSize-GamePanel.tileSize*4 - 1) worldY += 1;
                }
                if(gp.getCollision().checkCollisionLeft(this)) {
                    if(worldX > 1) worldX -= 1;
                }
            }
        } else if(direction.equals("down_right")) {
            for(int i = 0; i < speed/sqrt(2); i++) {// Обработка движения вверх
                if(gp.getCollision().checkCollisionDown(this)) {
                    if(worldY < gp.getWorldHeight()*GamePanel.tileSize-GamePanel.tileSize*4 - 1) worldY += 1;
                }
                if(gp.getCollision().checkCollisionRight(this)) {
                    if(worldX < gp.getWorldWidth()*GamePanel.tileSize-GamePanel.tileSize*2-1) worldX += 1;
                }
            }
        } else if(direction.equals("up")) {
            for(int i = 0; i < speed/sqrt(2); i++) {
                if(worldY > - 0 && gp.getCollision().checkCollisionUp(this)) worldY -= 1;
            }
        } else if(direction.equals("down")) {
            for(int i = 0; i < speed/sqrt(2); i++) if(gp.getCollision().checkCollisionDown(this) && worldY < gp.getWorldHeight()*GamePanel.tileSize-GamePanel.tileSize*4 - 1) worldY += 1;
        } else if(direction.equals("left")) {
            for(int i = 0; i < speed/sqrt(2); i++) if(worldX > 1 && gp.getCollision().checkCollisionLeft(this)) worldX -= 1;
        } else if(direction.equals("right")) {
            for(int i = 0; i < speed/sqrt(2); i++) if(gp.getCollision().checkCollisionRight(this) && worldX < gp.getWorldWidth()*GamePanel.tileSize-GamePanel.tileSize*2-1) worldX += 1;
        }


        switch (direction) {
            case "up": animations[1].update(); break;
            case "down": animations[0].update(); break;
            case "left": case "up_left": case "down_left": animations[2].update(); break;
            case "right": case "up_right": case "down_right": animations[3].update(); break;
        }

    }


    public void draw(Graphics2D g) {
        if(gp.sceneLoader.getCutScene()) {
            return;
        }
        if(gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }
        int playerWidth = (int)(GamePanel.tileSize*2/2);
        int playerHeight = (int)(GamePanel.tileSize*4/2);
        switch (direction) { // Анимирует движение по направлениям
            case "up": animations[1].draw(g, screenX, screenY, playerWidth, playerHeight); break;
            case "down": animations[0].draw(g, screenX, screenY, playerWidth, playerHeight); break;
            case "left", "up_left", "down_left": animations[2].draw(g, screenX, screenY, playerWidth, playerHeight); break;
            case "right", "up_right", "down_right": animations[3].draw(g, screenX, screenY, playerWidth, playerHeight); break;
            case "null": animations[0].draw(g, screenX, screenY, playerWidth, playerHeight);
        }

    }

}
