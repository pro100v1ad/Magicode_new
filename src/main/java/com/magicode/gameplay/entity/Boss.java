package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.Animation;
import main.java.com.magicode.core.utils.BulletManager;
import main.java.com.magicode.ui.interface_.Bar;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Boss extends Enemy {

    private BufferedImage[] imagesRed;
    private BufferedImage[] imagesGreen;
    private boolean isAnimation;
    private boolean isAnimationRed;
    private boolean isAnimationGreen;

    private Animation animationGreen;
    private Animation animationRed;

    private int updateCount;
    private int attackCooldown;
    private int currentAttackPattern;

    // Параметры балансировки
    private final int bulletSpeedPhase1 = 8;
    private final int bulletSpeedPhase2 = 10;
    private final int bulletRadius = 32;
    private final int phase1Cooldown = 60;
    private final int phase2Cooldown = 45;

    private Bar bar;

    public Boss(GamePanel gp, int x, int y, int health, int maxHealth) {
        super(gp);
        this.worldX = x;
        this.worldY = y;
        this.collisionWidth = GamePanel.tileSize * 4;
        this.collisionHeight = GamePanel.tileSize * 4;
        this.name = "boss";

        this.health = health;
        this.maxHealth = maxHealth;
        isAnimation = false;

        this.attackCooldown = phase1Cooldown;
        updateCount = 0;
        currentAttackPattern = 0;

        loadImage();

        animationGreen = new Animation(imagesGreen, 12);
        animationRed = new Animation(imagesRed, 12);

        bar = new Bar(gp, (int)worldX, (int)worldY, 128,24, maxHealth, health, Color.green);
    }

    @Override
    public void setDefaultValues() {}

    @Override
    public void loadAnimations() {}

    public void loadImage() {
        imagesGreen = new BufferedImage[9];
        imagesRed = new BufferedImage[9];

        for (int i = 0; i < 9; i++) {
            imagesGreen[i] = gp.textureAtlas.textures[23][i].getTexture();
            imagesRed[i] = gp.textureAtlas.textures[23][i + 9].getTexture();
        }
    }

    public void startAnimation() {
        isAnimation = true;

        if (health > maxHealth / 2) {
            isAnimationGreen = true;
            executePhaseOneAttack();
        } else {
            isAnimationRed = true;
            executePhaseTwoAttack();
        }
    }

    private void executePhaseOneAttack() {
        // Паттерн атак для первой фазы
        switch (currentAttackPattern) {
            case 0:
                singleAttack();
                break;
            case 1:
                fanAttack();
                break;
            case 2:
                trackingAttack(3, 15);
                break;
        }

        // Переходим к следующей атаке в последовательности
        currentAttackPattern = (currentAttackPattern + 1) % 3;
    }

    private void executePhaseTwoAttack() {
        // Паттерн атак для второй фазы
        switch (currentAttackPattern) {
            case 0:
                fanAttack();
                break;
            case 1:
                circularAttack(12);
                break;
            case 2:
                trackingAttack(5, 10);
                break;
            case 3:
                waveAttack();
                break;
        }

        // Переходим к следующей атаке в последовательности
        currentAttackPattern = (currentAttackPattern + 1) % 4;
    }

    private void singleAttack() {
        double x = getCenterX();
        double y = getCenterY();
        double angle = calculateAngleToPlayer(x, y);

        createBullet(x, y, angle, getCurrentBulletSpeed());
    }

    private void fanAttack() {
        double x = getCenterX();
        double y = getCenterY();
        double mainAngle = calculateAngleToPlayer(x, y);
        int bullets = 5;
        int spread = 30;

        for (int i = 0; i < bullets; i++) {
            double angle = mainAngle + (i - bullets / 2) * (spread / (bullets - 1));
            createBullet(x, y, angle, getCurrentBulletSpeed());
        }
    }

    private void trackingAttack(int bulletCount, int spread) {
        double x = getCenterX();
        double y = getCenterY();
        double mainAngle = calculateAngleToPlayer(x, y);

        for (int i = 0; i < bulletCount; i++) {
            double angle = mainAngle + (i - bulletCount / 2) * spread;
            createBullet(x, y, angle, getCurrentBulletSpeed());
        }
    }

    private void circularAttack(int bullets) {
        double x = getCenterX();
        double y = getCenterY();
        double angleStep = 360.0 / bullets;

        for (int i = 0; i < bullets; i++) {
            createBullet(x, y, i * angleStep, getCurrentBulletSpeed());
        }
    }

    private void waveAttack() {
        double x = getCenterX();
        double y = getCenterY();
        int waves = 12;

        for (int i = 0; i < waves; i++) {
            double angle = i * (360.0 / waves);
            createBullet(x, y, angle + 5, getCurrentBulletSpeed());
            createBullet(x, y, angle - 5, getCurrentBulletSpeed());
        }
    }

    private double getCenterX() {
        return worldX + (double) collisionWidth / 2;
    }

    private double getCenterY() {
        return worldY + (double) collisionHeight / 2;
    }

    private double calculateAngleToPlayer(double x, double y) {
        double angle = Math.toDegrees(Math.atan2(
                gp.player.getWorldY() - y,
                gp.player.getWorldX() - x
        ));
        return angle < 0 ? angle + 360 : angle;
    }

    private int getCurrentBulletSpeed() {
        return health > maxHealth / 2 ? bulletSpeedPhase1 : bulletSpeedPhase2;
    }

    private void createBullet(double x, double y, double angle, int speed) {
        Bullet bullet = new Bullet(gp, (int) x, (int) y, angle, speed, bulletRadius, false, 3);
        gp.player.getBulletManager().addBullet(bullet);
    }

    public void update() {
        updateCount++;

        // Обновляем кулдаун в зависимости от фазы
        attackCooldown = health > maxHealth / 2 ? phase1Cooldown : phase2Cooldown;

        if (updateCount >= attackCooldown) {
            startAnimation();
            updateCount = 0;
        }

        if (isAnimation) {
            if (isAnimationGreen) {
                animationGreen.update();
                if (animationGreen.currentFrame == imagesGreen.length - 1 && animationGreen.updatesCount == 0) {
                    resetAnimation();
                }
            } else if (isAnimationRed) {
                animationRed.update();
                if (animationRed.currentFrame == imagesRed.length - 1 && animationRed.updatesCount == 0) {
                    resetAnimation();
                }
            }
        }

        bar.setCurrentValue((int)health);
        if(health > maxHealth/3 && health < maxHealth*2/3) {
            bar.setColor(Color.YELLOW);
        }
        if(health < maxHealth/3) {
            bar.setColor(Color.RED);
        }

        // Проверяем, закончилось ли бессмертие
        long currentTime = System.currentTimeMillis();
        if (isInvulnerable && currentTime >= invulnerabilityEndTime) {
            isInvulnerable = false;
        }
    }

    private void resetAnimation() {
        isAnimation = false;
        isAnimationGreen = false;
        isAnimationRed = false;
        animationGreen.reset();
        animationRed.reset();
    }

    public void draw(Graphics2D g) {
        int screenX = (int) (worldX - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int) (worldY - gp.player.getWorldY() + gp.player.getScreenY());

        if (isOnScreen()) {
            if (isAnimationGreen) {
                animationGreen.draw(g, screenX, screenY, collisionWidth, collisionHeight);
            } else if (isAnimationRed) {
                animationRed.draw(g, screenX, screenY, collisionWidth, collisionHeight);
            } else if (health > maxHealth / 2) {
                // Статичное изображение, когда не анимируется
                g.drawImage(imagesGreen[0], screenX, screenY, collisionWidth, collisionHeight, null);
            } else {
                g.drawImage(imagesRed[0], screenX, screenY, collisionWidth, collisionHeight, null);
            }
            bar.setPosX(screenX);
            bar.setPosY(screenY-16);
            bar.draw(g);
        }
    }

    private boolean isOnScreen() {
        return worldX + GamePanel.tileSize * 4 > gp.player.getWorldX() - gp.player.getScreenX() &&
                worldX - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                worldY + GamePanel.tileSize * 4 > gp.player.getWorldY() - gp.player.getScreenY() &&
                worldY - GamePanel.tileSize * 4 < gp.player.getWorldY() + gp.player.getScreenY();
    }
}