package main.java.com.magicode.gameplay.entity.EnemyType;

import main.java.com.magicode.ui.interface_.Bar;
import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.Animation;
import main.java.com.magicode.core.utils.ResourceLoader;
import main.java.com.magicode.gameplay.entity.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class Slime extends Enemy {
    private Animation[] animations;
    private ResourceLoader resourceLoader;
    private long lastDamageTime = 0;
    private final long damageCooldown = 1000;
    private Bar healthBar;

    private final double homeX;
    private final double homeY;
    private boolean wasAggressive = false;
    private boolean isAngry = false;
    private boolean isReturningHome = false;

    public Slime(GamePanel gp, double spawnX, double spawnY) {
        super(gp);
        this.name = "slime";
        this.homeX = spawnX;
        this.homeY = spawnY;
        setDefaultValues();
        loadAnimations();
    }

    @Override
    protected void setDefaultValues() {
        worldX = homeX;
        worldY = homeY;
        speed = 3.75;
        maxHealth = 50;
        health = maxHealth;
        damage = 3;
        detectionRange = GamePanel.tileSize * 5;
        aggressive = false;

        collisionWidth = (int)(GamePanel.tileSize*1.7/1.5);
        collisionHeight = (int)(GamePanel.tileSize*3/2);

        resourceLoader = new ResourceLoader();
        healthBar = new Bar(gp, 0, 0, (int)(GamePanel.tileSize * 2 / 1.5), 8, (int)maxHealth, (int)health, new Color(200, 0, 0));
    }

    protected void loadAnimations() {
        animations = new Animation[2];

        BufferedImage[] normalImages = new BufferedImage[3];
        normalImages[0] = resourceLoader.loadImage("/resources/enemies/slime/Slimes1.png");
        normalImages[1] = resourceLoader.loadImage("/resources/enemies/slime/Slimes2.png");
        normalImages[2] = resourceLoader.loadImage("/resources/enemies/slime/Slimes3.png");
        animations[0] = new Animation(normalImages, 5);

        BufferedImage[] angryImages = new BufferedImage[3];
        angryImages[0] = resourceLoader.loadImage("/resources/enemies/slime/slime_angry1.png");
        angryImages[1] = resourceLoader.loadImage("/resources/enemies/slime/slime_angry2.png");
        angryImages[2] = resourceLoader.loadImage("/resources/enemies/slime/slime_angry3.png");
        animations[1] = new Animation(angryImages, 5);
    }

    private void checkPlayerCollisionAndDamage() {
        if (gp.getCollision().checkEntityCollision(this, gp.player)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDamageTime >= damageCooldown) {
                gp.player.setHealth((int)gp.player.getHealth() - damage);
                lastDamageTime = currentTime;
            }
        }
    }

    private void chasePlayer() {
        double playerX = gp.player.getWorldX();
        double playerY = gp.player.getWorldY();

        double dx = playerX - worldX;
        double dy = playerY - worldY;

        String[] priorities = Math.abs(dx) > Math.abs(dy) ?
                new String[]{dx > 0 ? "right" : "left", dy > 0 ? "down" : "up"} :
                new String[]{dy > 0 ? "down" : "up", dx > 0 ? "right" : "left"};

        for (String dir : priorities) {
            moveOneStep(dir);
            direction = dir;
            break;
        }
    }


    private void returnToHome() {
        double dx = homeX - worldX;
        double dy = homeY - worldY;

        // Если уже дома, прекращаем возвращение
        if (Math.abs(dx) < speed && Math.abs(dy) < speed) {
            worldX = homeX;
            worldY = homeY;
            isReturningHome = false;
            return;
        }

        // Двигаемся к дому
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = dx > 0 ? "right" : "left";
        } else {
            direction = dy > 0 ? "down" : "up";
        }

        moveOneStep(direction);
    }

    private void moveOneStep(String dir) {
        double step = speed / sqrt(2);

        switch (dir) {
            case "up":
                if (worldY > 0 && gp.getCollision().checkCollisionUp(this)) {
                    worldY -= step;
                }
                break;
            case "down":
                if (worldY < gp.getWorldHeight() * GamePanel.tileSize - GamePanel.tileSize * 4 - 1
                        && gp.getCollision().checkCollisionDown(this)) {
                    worldY += step;
                }
                break;
            case "left":
                if (worldX > 1 && gp.getCollision().checkCollisionLeft(this)) {
                    worldX -= step;
                }
                break;
            case "right":
                if (worldX < gp.getWorldWidth() * GamePanel.tileSize - GamePanel.tileSize * 2 - 1
                        && gp.getCollision().checkCollisionRight(this)) {
                    worldX += step;
                }
                break;
        }
    }


    @Override
    public void update() {
        if(gp.state.equals(GamePanel.GameState.StartMenu)) return;

        boolean playerInRange = isPlayerInRange();
        // Проверяем, появился ли игрок в зоне агрессии
        if (playerInRange) {
            aggressive = true;
            isAngry = true;
            isReturningHome = false;
        }
        // Если игрок вышел из зоны и мы не были агрессивны
        else if (wasAggressive) {
            aggressive = false;
            isAngry = false;
            isReturningHome = true;
        }

        wasAggressive = playerInRange;

        if (aggressive) {
            // Преследуем игрока (приоритетнее возвращения домой)
            chasePlayer();
        } else if (isReturningHome) {
            // Возвращаемся домой
            returnToHome();
        }

        checkPlayerCollisionAndDamage();
        animations[isAngry ? 1 : 0].update();
        healthBar.setCurrentValue((int)health);

        // Проверяем, закончилось ли бессмертие
        long currentTime = System.currentTimeMillis();
        if (isInvulnerable && currentTime >= invulnerabilityEndTime) {
            isInvulnerable = false;
        }

    }

    @Override
    public void draw(Graphics2D g) {
        if(gp.state.equals(GamePanel.GameState.StartMenu)) return;

        int screenX = (int)(worldX - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int)(worldY - gp.player.getWorldY() + gp.player.getScreenY());
        int enemyWidth = (int)(GamePanel.tileSize * 2/1.5);
        int enemyHeight = (int)(GamePanel.tileSize * 4/2.5);

        animations[isAngry ? 1 : 0].draw(g, screenX, screenY, enemyWidth, enemyHeight);
        healthBar.setPosX(screenX);
        healthBar.setPosY(screenY - 10);
        healthBar.draw(g);
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
        wasAggressive = aggressive;
        isAngry = aggressive;
        if (!aggressive) {
            isReturningHome = true;
        }
    }

    public boolean getAggressive() {
        return aggressive;
    }
}