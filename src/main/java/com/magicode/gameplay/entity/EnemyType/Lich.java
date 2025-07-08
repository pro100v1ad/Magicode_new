package main.java.com.magicode.gameplay.entity.EnemyType;

import main.java.com.magicode.ui.interface_.Bar;
import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.Animation;
import main.java.com.magicode.core.utils.ResourceLoader;
import main.java.com.magicode.gameplay.entity.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Lich extends Enemy {
    private Animation[] animations;
    private ResourceLoader resourceLoader;
    private Bar healthBar;

    // Параметры ауры
    private final int auraRadius = GamePanel.tileSize * 3;
    private final long auraCooldown = 4000;
    private final long auraDuration = 5000;
    private long lastAuraTime = 0;
    private boolean isAuraActive = false;
    private long auraStartTime = 0;

    // Эффект ауры
    private Color auraColor = new Color(150, 0, 200, 100);
    private int auraPulseSpeed = 5;
    private int currentAuraPulse = 0;

    public Lich(GamePanel gp, double spawnX, double spawnY) {
        super(gp);
        this.name = "lich";
        setDefaultValues();
        loadAnimations();
        this.worldX = spawnX;
        this.worldY = spawnY;
        this.aggressive = true;
    }

    @Override
    protected void setDefaultValues() {
        speed = 2.0;
        maxHealth = 120;
        health = maxHealth;
        damage = 4;
        detectionRange = GamePanel.tileSize * 10;
        aggressive = true;

        collisionWidth = (int)(GamePanel.tileSize*1.7/1.5);
        collisionHeight = GamePanel.tileSize*3/2;

        resourceLoader = new ResourceLoader();
        healthBar = new Bar(gp, 0, 0, (int)(GamePanel.tileSize * 2 / 1.5), 8,
                (int)maxHealth, (int)health, new Color(150, 0, 200));
    }

    @Override
    protected void loadAnimations() {
        animations = new Animation[2]; // Используем 2 анимации как у слайма

        // Берем анимации слайма для теста
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

    private void activateAura() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAuraTime >= auraCooldown) {
            isAuraActive = true;
            auraStartTime = currentTime;
            lastAuraTime = currentTime;
        }
    }

    private void checkAuraDamage() {
        if (!isAuraActive || gp.player == null) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - auraStartTime > auraDuration) {
            isAuraActive = false;
            return;
        }

        double distance = Math.sqrt(Math.pow(worldX - gp.player.getWorldX(), 2) +
                Math.pow(worldY - gp.player.getWorldY(), 2));

        if (distance <= auraRadius) {
            if (currentTime - lastDamageTime >= 1000) {
                gp.player.takeDamage(2);
                lastDamageTime = currentTime;
            }
        }
    }

    @Override
    public void update() {
        if (gp.state.equals(GamePanel.GameState.StartMenu)) return;

        if (isPlayerInRange()) {
            // Движение к игроку
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

            activateAura();
        }

        checkAuraDamage();

        if (isAuraActive) {
            currentAuraPulse = (currentAuraPulse + 1) % (2 * auraPulseSpeed);
        }

        animations[isPlayerInRange() ? 1 : 0].update(); // Используем angry анимацию при агрессии
        healthBar.setCurrentValue((int)health);

        long currentTime = System.currentTimeMillis();
        if (isInvulnerable && currentTime >= invulnerabilityEndTime) {
            isInvulnerable = false;
        }
    }

    private void moveOneStep(String dir) {
        double step = speed / Math.sqrt(2);

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
    public void draw(Graphics2D g) {
        if (gp.state.equals(GamePanel.GameState.StartMenu)) return;

        int screenX = (int)(worldX - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int)(worldY - gp.player.getWorldY() + gp.player.getScreenY());

        // Рисуем ауру
        if (isAuraActive) {
            int pulseSize = currentAuraPulse < auraPulseSpeed ?
                    currentAuraPulse : 2 * auraPulseSpeed - currentAuraPulse;
            int drawRadius = auraRadius + pulseSize * 2;

            g.setColor(auraColor);
            g.fillOval(screenX - drawRadius/2, screenY - drawRadius/2, drawRadius, drawRadius);
        }

        // Рисуем модельку слайма (временно для тестов)
        int enemyWidth = (int)(GamePanel.tileSize * 2/1);
        int enemyHeight = (int)(GamePanel.tileSize * 4/2);
        animations[isPlayerInRange() ? 1 : 0].draw(g, screenX, screenY, enemyWidth, enemyHeight);

        // Рисуем health bar
        healthBar.setPosX(screenX);
        healthBar.setPosY(screenY - 10);
        healthBar.draw(g);
    }

    @Override
    public void setAggressive(boolean aggressive) {
        // Лич всегда остается агрессивным, игнорируем параметр
        this.aggressive = true;
    }

    public boolean getAggressive() {
        return aggressive;
    }

}