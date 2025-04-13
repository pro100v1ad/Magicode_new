package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;

public class Entity {
    public double worldX, worldY; // Точные координаты
    public int screenX, screenY;  // Координаты на экране
    public double speed;
    public String direction = "null";

    public double maxHealth;
    public double health;
    public double regeneration;

    public int collisionX;
    public int collisionY;
    public int collisionWidth = GamePanel.tileSize;
    public int collisionHeight = GamePanel.tileSize;

    public int collisionCode = 2; // Уникальный код для игрока


}
