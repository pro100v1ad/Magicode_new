package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.Animation;
import main.java.com.magicode.core.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity { // Родительский класс для всех врагов
    protected GamePanel gp;
    protected Animation[] animations;
    protected ResourceLoader resourceLoader;

    protected String name;
    protected int detectionRange; // Дистанция обнаружения игрока
    protected int damage; // Урон, который наносит враг
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

        int enemyWidth = GamePanel.tileSize*2/2;
        int enemyHeight = GamePanel.tileSize*4/2;

        switch (direction) {
            case "up": animations[1].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight); break;
            case "down": animations[0].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight); break;
            case "left":
            case "up_left":
            case "down_left":
                animations[2].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight); break;
            case "right":
            case "up_right":
            case "down_right":
                animations[3].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight); break;
            case "null": animations[0].draw(g, (int)worldX, (int)worldY, enemyWidth, enemyHeight);
        }
    }

    protected boolean isPlayerInRange() {
        if (gp.player == null) return false;

        Player player = gp.player;
        double distance = Math.sqrt(Math.pow(worldX - player.getWorldX(), 2) +
                Math.pow(worldY - player.getWorldY(), 2));
        return distance <= detectionRange;
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

    public String getName() {
        return name;
    }

    public double getDamage() {
        return damage;
    }

    public void takeDamage(int damage) {
        long currentTime = System.currentTimeMillis();

        // Если игрок не в режиме бессмертия, наносим урон
        if (!isInvulnerable) {
            health -= damage;
            isInvulnerable = true; // Включаем бессмертие
            invulnerabilityEndTime = currentTime + damageCooldown; // Устанавливаем время окончания
        }
    }

}