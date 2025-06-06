package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;

public class Entity { // Родительский класс отвечающий за все сущности

    protected double worldX, worldY; // Точные координаты
    protected double speed;
    protected String direction = "null";

    protected double maxHealth;
    protected double health;
    protected double regeneration;
    protected double damage;

    protected int collisionX;
    protected int collisionY;
    protected int collisionWidth;
    protected int collisionHeight;

    protected int collisionCode; // Уникальный код для игрока

    protected boolean isInvulnerable = false; // Добавляем флаг бессмертия
    protected long invulnerabilityEndTime = 0; // Время окончания бессмертия
    protected long lastDamageTime = 0;
    protected final long damageCooldown = 500;

    public double getWorldX() {
        return  worldX;
    }
    public double getWorldY() {
        return  worldY;
    }
    public String getDirection() {
        return  direction;
    }
    public double getMaxHealth() {
        return maxHealth;
    }
    public double getHealth() {
        return health;
    }
    public int getCollisionCode() {
        return collisionCode;
    }
    public int getCollisionHeight() {
        return collisionHeight;
    }
    public int getCollisionWidth() {
        return collisionWidth;
    }
    public double getDamage() {return damage;}
}
