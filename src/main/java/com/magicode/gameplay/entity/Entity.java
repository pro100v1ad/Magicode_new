package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;

public class Entity {
    protected double worldX, worldY; // Точные координаты
    protected double speed;
    protected String direction = "null";

    protected double maxHealth;
    protected double health;
    protected double regeneration;

    protected int collisionX;
    protected int collisionY;
    protected int collisionWidth;
    protected int collisionHeight;

    protected int collisionCode; // Уникальный код для игрока

    public double getWorldX() {
        return  worldX;
    }
    public double getWorldY() {
        return  worldY;
    }
    public double getSpeed() {
        return  speed;
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
    public double getRegeneration() {
        return regeneration;
    }
    public int getCollisionX() {
        return collisionX;
    }
    public int getCollisionY() {
        return collisionY;
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

}
