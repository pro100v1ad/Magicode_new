package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.Animation;
import main.java.com.magicode.core.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity {
    protected GamePanel gp;
    protected Animation[] animations;
    protected ResourceLoader resourceLoader;

    protected int detectionRange; // Дистанция обнаружения игрока
    protected int damage; // Урон, который наносит враг
    protected int attackCooldown; // Задержка между атаками
    protected int attackTimer; // Таймер для атаки

    protected boolean aggressive; // Агрессивный ли враг (преследует игрока)

    public Enemy(GamePanel gp) {
        this.gp = gp;
        this.resourceLoader = new ResourceLoader();
        this.collisionCode = 3; // Уникальный код для врагов
        this.direction = "down";
        setDefaultValues();
        loadAnimations();
    }

    protected abstract void setDefaultValues();
    protected abstract void loadAnimations();

    public abstract void update();

    public void draw(Graphics2D g) {
        if(gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }

        int enemyWidth = (int)(GamePanel.tileSize*2/2);
        int enemyHeight = (int)(GamePanel.tileSize*4/2);

        switch (direction) {
            case "up": animations[1].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight); break;
            case "down": animations[0].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight); break;
            case "left", "up_left", "down_left":
                animations[2].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight); break;
            case "right", "up_right", "down_right":
                animations[3].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight); break;
            case "null": animations[0].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight);
        }
    }

    protected boolean isPlayerInRange() {
        if (gp.getPlayer() == null) return false; // Добавляем проверку на null

        Player player = gp.getPlayer();
        double distance = Math.sqrt(Math.pow(worldX - player.getWorldX(), 2) +
                Math.pow(worldY - player.getWorldY(), 2));
        return distance <= detectionRange;
    }

    protected void moveTowardsPlayer() {
        if (gp.getPlayer() == null) return; // Добавляем проверку на null

        Player player = gp.getPlayer();
        double dx = player.getWorldX() - worldX;
        double dy = player.getWorldY() - worldY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if(distance != 0) {
            dx /= distance;
            dy /= distance;

            worldX += dx * speed;
            worldY += dy * speed;

            // Обновляем направление для анимации
            if(Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 0 ? "right" : "left";
            } else {
                direction = dy > 0 ? "down" : "up";
            }
        }
    }

    public void setWorldX(double worldX) {
        this.worldX = worldX;
    }

    public void setWorldY(double worldY) {
        this.worldY = worldY;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

}