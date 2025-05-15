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
        this.name = "slime";
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        worldX = GamePanel.tileSize * 10;
        worldY = GamePanel.tileSize * 10;
        speed = 1.25;
        maxHealth = 50;
        health = maxHealth;
        damage = 10;
        detectionRange = GamePanel.tileSize * 5;
        aggressive = false;

        collisionWidth = (int)(GamePanel.tileSize*1.7/1.5);
        collisionHeight =(int)(GamePanel.tileSize*3/2.5);

        resourceLoader = new ResourceLoader();
    }

    protected void loadAnimations() {
        animations = new Animation[1];

        BufferedImage[] Images = new BufferedImage[3];
        Images[0] = resourceLoader.loadImage("/resources/enemies/slime/Slimes1.png");
        Images[1] = resourceLoader.loadImage("/resources/enemies/slime/Slimes2.png");
        Images[2] = resourceLoader.loadImage("/resources/enemies/slime/Slimes3.png");
        animations[0] = new Animation(Images, 5);

    }

    @Override
    public void update() {
        if(gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }

        // Сохраняем старые координаты для отката при коллизии
        double oldX = worldX;
        double oldY = worldY;

        // Простое патрулирование
        if(!aggressive) {
            if(Math.random() < 0.01) {
                int randomDir = (int) (Math.random() * 4);
                switch (randomDir) {
                    case 0:
                        direction = "up";
                        break;
                    case 1:
                        direction = "down";
                        break;
                    case 2:
                        direction = "left";
                        break;
                    case 3:
                        direction = "right";
                        break;
                    default:
                        direction = "down";
                }
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
            case "up": animations[0].update(); break;
            case "down": animations[0].update(); break;
            case "left": animations[0].update(); break;
            case "right": animations[0].update(); break;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if(gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }

        int screenX = (int)(worldX - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int)(worldY - gp.player.getWorldY() + gp.player.getScreenY());
        int enemyWidth = (int)(GamePanel.tileSize * 2/1.5);
        int enemyHeight = (int)(GamePanel.tileSize * 4/2.5);

        switch(direction) {
            case "up": animations[0].draw(g, screenX, screenY, enemyWidth, enemyHeight); break;
            case "down": animations[0].draw(g, screenX, screenY, enemyWidth, enemyHeight); break;
            case "left": animations[0].draw(g, screenX, screenY, enemyWidth, enemyHeight); break;
            case "right": animations[0].draw(g, screenX, screenY, enemyWidth, enemyHeight); break;
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

    public boolean getAggressive() {
        return aggressive;
    }
}