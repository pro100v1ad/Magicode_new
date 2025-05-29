package main.java.com.magicode.gameplay.entity.EnemyType;

import main.java.com.magicode.ui.interface_.Bar;
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
    private long lastDamageTime = 0;
    private final long damageCooldown = 1000; // мс
    private Bar healthBar;  // добавляем поле для бара

    public Slime(GamePanel gp) {
        super(gp);
        this.name = "slime";
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        worldX = GamePanel.tileSize * 10;
        worldY = GamePanel.tileSize * 10;
        speed = 3.75;
        maxHealth = 50;
        health = maxHealth;
        damage = 3;
        detectionRange = GamePanel.tileSize * 15;
        aggressive = false;

        collisionWidth = (int)(GamePanel.tileSize*1.7/1.5);
        collisionHeight = (int)(GamePanel.tileSize*3/2);

        resourceLoader = new ResourceLoader();
        healthBar = new Bar(gp, 0, 0, (int)(GamePanel.tileSize * 2 / 1.5), 8, (int)maxHealth, (int)health, new Color(200, 0, 0));
    }

    protected void loadAnimations() {
        animations = new Animation[1];

        BufferedImage[] Images = new BufferedImage[3];
        Images[0] = resourceLoader.loadImage("/resources/enemies/slime/Slimes1.png");
        Images[1] = resourceLoader.loadImage("/resources/enemies/slime/Slimes2.png");
        Images[2] = resourceLoader.loadImage("/resources/enemies/slime/Slimes3.png");
        animations[0] = new Animation(Images, 5);

    }

    private void checkPlayerCollisionAndDamage() {
        // Проверяем коллизию с игроком
        if (gp.getCollision().checkEntityCollision(this, gp.player)) {
            long currentTime = System.currentTimeMillis();
            // Проверяем, прошло ли достаточно времени с последнего урона
            if (currentTime - lastDamageTime >= damageCooldown) {
                // Наносим урон игроку
                gp.player.setHealth((int)gp.player.getHealth() - damage);
                lastDamageTime = currentTime; // Обновляем время последнего урона
            }
        }
    }

    private void updateAggressiveMovement() {
        double playerX = gp.player.getWorldX();
        double playerY = gp.player.getWorldY();

        double dx = playerX - worldX;
        double dy = playerY - worldY;

        String[] priorities;

        if (Math.abs(dx) > Math.abs(dy)) {
            priorities = new String[]{
                    dx > 0 ? "right" : "left",
                    dy > 0 ? "down" : "up"
            };
        } else {
            priorities = new String[]{
                    dy > 0 ? "down" : "up",
                    dx > 0 ? "right" : "left"
            };
        }

        // Пробуем по приоритетам
        for (String dir : priorities) {
                moveOneStep(dir);
                direction = dir;
                break;
        }
    }

    private void moveOneStep(String dir) {
        switch (dir) {
            case "up":
                for (int i = 0; i < speed / sqrt(2); i++) {
                    if (worldY > 0 && gp.getCollision().checkCollisionUp(this)) {
                        worldY -= 1;
                    }
                }
                break;
            case "down":
                for (int i = 0; i < speed / sqrt(2); i++) {
                    if (worldY < gp.getWorldHeight() * GamePanel.tileSize - GamePanel.tileSize * 4 - 1
                            && gp.getCollision().checkCollisionDown(this)) {
                        worldY += 1;
                    }
                }
                break;
            case "left":
                for (int i = 0; i < speed / sqrt(2); i++) {
                    if (worldX > 1 && gp.getCollision().checkCollisionLeft(this)) {
                        worldX -= 1;
                    }
                }
                break;
            case "right":
                for (int i = 0; i < speed / sqrt(2); i++) {
                    if (worldX < gp.getWorldWidth() * GamePanel.tileSize - GamePanel.tileSize * 2 - 1
                            && gp.getCollision().checkCollisionRight(this)) {
                        worldX += 1;
                    }
                }
                break;
        }
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
            updateAggressiveMovement();
        }

        checkPlayerCollisionAndDamage();

        // Обновление анимации
        switch(direction) {
            case "up": animations[0].update(); break;
            case "down": animations[0].update(); break;
            case "left": animations[0].update(); break;
            case "right": animations[0].update(); break;
        }
        healthBar.setCurrentValue((int)health);
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
        healthBar.setPosX(screenX);
        healthBar.setPosY(screenY - 10); // чуть выше слайма
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
    }

    public boolean getAggressive() {
        return aggressive;
    }
}