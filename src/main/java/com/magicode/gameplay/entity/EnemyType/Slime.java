package main.java.com.magicode.gameplay.entity.EnemyType;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.Animation;
import main.java.com.magicode.core.utils.ResourceLoader;
import main.java.com.magicode.gameplay.entity.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.sqrt;

public class Slime extends Enemy {
    private Animation[] animations;
    private ResourceLoader resourceLoader;
    private String lastDirection = "";

    public Slime(GamePanel gp) {
        super(gp);
        System.out.println("Slime created at: " + worldX + ", " + worldY);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        worldX = GamePanel.tileSize * 10;
        worldY = GamePanel.tileSize * 10;
        speed = 1.5;
        maxHealth = 50;
        health = maxHealth;
        damage = 10;
        detectionRange = GamePanel.tileSize * 5;
        aggressive = false;

        collisionWidth = (int)(GamePanel.tileSize * 1.5 / 2);
        collisionHeight = (int)(GamePanel.tileSize * 1.5 / 2);

        resourceLoader = new ResourceLoader();
    }

    protected void loadAnimations() {
        animations = new Animation[4]; // 4 направления

        // Анимация "вниз"
        BufferedImage[] downImages = new BufferedImage[4];
        downImages[0] = resourceLoader.loadImage("/resources/enemies/slime/down/slime_down1.png");
        downImages[1] = resourceLoader.loadImage("/resources/enemies/slime/down/slime_down2.png");
        downImages[2] = resourceLoader.loadImage("/resources/enemies/slime/down/slime_down3.png");
        downImages[3] = resourceLoader.loadImage("/resources/enemies/slime/down/slime_down4.png");
        animations[0] = new Animation(downImages, 10);

        // Анимация "вверх"
        BufferedImage[] upImages = new BufferedImage[4];
        upImages[0] = resourceLoader.loadImage("/resources/enemies/slime/up/slime_up1.png");
        upImages[1] = resourceLoader.loadImage("/resources/enemies/slime/up/slime_up2.png");
        upImages[2] = resourceLoader.loadImage("/resources/enemies/slime/up/slime_up3.png");
        upImages[3] = resourceLoader.loadImage("/resources/enemies/slime/up/slime_up4.png");
        animations[1] = new Animation(upImages, 10);

        // Анимация "влево"
        BufferedImage[] leftImages = new BufferedImage[4];
        leftImages[0] = resourceLoader.loadImage("/resources/enemies/slime/left/slime_left1.png");
        leftImages[1] = resourceLoader.loadImage("/resources/enemies/slime/left/slime_left2.png");
        leftImages[2] = resourceLoader.loadImage("/resources/enemies/slime/left/slime_left3.png");
        leftImages[3] = resourceLoader.loadImage("/resources/enemies/slime/left/slime_left4.png");
        animations[2] = new Animation(leftImages, 10);

        // Анимация "вправо"
        BufferedImage[] rightImages = new BufferedImage[4];
        rightImages[0] = resourceLoader.loadImage("/resources/enemies/slime/right/slime_right1.png");
        rightImages[1] = resourceLoader.loadImage("/resources/enemies/slime/right/slime_right2.png");
        rightImages[2] = resourceLoader.loadImage("/resources/enemies/slime/right/slime_right3.png");
        rightImages[3] = resourceLoader.loadImage("/resources/enemies/slime/right/slime_right4.png");
        animations[3] = new Animation(rightImages, 10);
    }

    @Override
    public void update() {
        if(gp.sceneLoader.getCutScene() || gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }

        // Сохраняем старые координаты для отката при коллизии
        double oldX = worldX;
        double oldY = worldY;

        // Простое патрулирование
        if(!aggressive) {
            if(Math.random() < 0.01) {
                direction = switch((int)(Math.random() * 4)) {
                    case 0 -> "up";
                    case 1 -> "down";
                    case 2 -> "left";
                    case 3 -> "right";
                    default -> direction;
                };
            }

            // Движение с проверкой коллизий
            switch(direction) {
                case "up":
                    for(int i = 0; i < speed/sqrt(2); i++) {
                        if(worldY > 0 && gp.getCollision().checkCollisionUp(this)) {
                            worldY -= 1;
                        }
                    }
                    break;
                case "down":
                    for(int i = 0; i < speed/sqrt(2); i++) {
                        if(worldY < gp.getWorldHeight()*GamePanel.tileSize-GamePanel.tileSize*4-1 &&
                                gp.getCollision().checkCollisionDown(this)) {
                            worldY += 1;
                        }
                    }
                    break;
                case "left":
                    for(int i = 0; i < speed/sqrt(2); i++) {
                        if(worldX > 1 && gp.getCollision().checkCollisionLeft(this)) {
                            worldX -= 1;
                        }
                    }
                    break;
                case "right":
                    for(int i = 0; i < speed/sqrt(2); i++) {
                        if(worldX < gp.getWorldWidth()*GamePanel.tileSize-GamePanel.tileSize*2-1 &&
                                gp.getCollision().checkCollisionRight(this)) {
                            worldX += 1;
                        }
                    }
                    break;
            }
        }
        else if(isPlayerInRange()) {
            moveTowardsPlayer();
            // Проверка коллизий после движения к игроку
            if(gp.getCollision().checkCollisionUp(this) ||
                    gp.getCollision().checkCollisionDown(this) ||
                    gp.getCollision().checkCollisionLeft(this) ||
                    gp.getCollision().checkCollisionRight(this)) {
                worldX = oldX;
                worldY = oldY;
            }
        }

        // Обновление анимации
        switch(direction) {
            case "up": animations[1].update(); break;
            case "down": animations[0].update(); break;
            case "left": animations[2].update(); break;
            case "right": animations[3].update(); break;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if(gp.sceneLoader.getCutScene() || gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }

        int screenX = (int)(worldX - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int)(worldY - gp.player.getWorldY() + gp.player.getScreenY());
        int enemyWidth = (int)(GamePanel.tileSize * 1.5);
        int enemyHeight = (int)(GamePanel.tileSize * 1.5);

        switch(direction) {
            case "up": animations[1].draw(g, screenX, screenY, enemyWidth, enemyHeight); break;
            case "down": animations[0].draw(g, screenX, screenY, enemyWidth, enemyHeight); break;
            case "left": animations[2].draw(g, screenX, screenY, enemyWidth, enemyHeight); break;
            case "right": animations[3].draw(g, screenX, screenY, enemyWidth, enemyHeight); break;
            default: animations[0].draw(g, screenX, screenY, enemyWidth, enemyHeight);
        }
    }

    @Override
    public void setWorldX(double worldX) {
        super.setWorldX(worldX);
    }

    @Override
    public void setWorldY(double worldY) {
        super.setWorldY(worldY);
    }

    @Override
    public void setAggressive(boolean aggressive) {
        super.setAggressive(aggressive);
    }
}