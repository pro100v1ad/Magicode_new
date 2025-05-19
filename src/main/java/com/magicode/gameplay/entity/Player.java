package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.core.utils.Animation;
import main.java.com.magicode.core.utils.BulletManager;
import main.java.com.magicode.core.utils.ResourceLoader;
import main.java.com.magicode.spells.Spell;
import main.java.com.magicode.spells.spells.GunSpell;
import main.java.com.magicode.spells.spells.KeySpell;
import main.java.com.magicode.spells.spells.WrenchSpell;
import main.java.com.magicode.ui.gamestate.Directory;
import main.java.com.magicode.ui.interface_.Bar;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static java.lang.Math.sqrt;

public class Player extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private GamePanel gp;
    private Animation[] animations;
    private ResourceLoader resourceLoader;

    private final int screenX;
    private final int screenY;

    private double maxMana;
    private double mana;

    private Spell[] spells;
    private int countSpells;
    public static final String DEFAULT_SPELLS = "/resources/levels/scenes/start/spells";

    private int countBook;

    private Bar healthBar;
    private Bar manaBar;

    private long lastDamageTime = 0;
    private final long damageCooldown = 1000;

    private BulletManager bulletManager;
    private boolean click;

    public Player(GamePanel gp, String playerFilePath, String spellFilePath) {
        this.gp = gp;
        resourceLoader = new ResourceLoader();
        bulletManager = new BulletManager();

        screenX = GamePanel.WIDTH/2 - GamePanel.tileSize;
        screenY = GamePanel.HEIGHT/2 - GamePanel.tileSize*2;

        collisionWidth = (int)(GamePanel.tileSize*1.7/2);
        collisionHeight = GamePanel.tileSize*2;
        collisionCode = 2;

        loadAnimation();

        countSpells = 0;
        if(playerFilePath != null && spellFilePath != null) {
            loadPlayerFromFile(playerFilePath);
            loadSaveSpells(spellFilePath);
        } else {
            setDefaultValues();
        }

        int healthBarWidth = 300;
        int healthBarHeight = 32;
        int healthBarPosX = GamePanel.WIDTH/2 - healthBarWidth/2;
        int healthBarPosY = GamePanel.HEIGHT - healthBarHeight*2;
        healthBar = new Bar(gp, healthBarPosX, healthBarPosY, healthBarWidth, healthBarHeight, (int)maxHealth, (int)health, Color.red);

        int manaBarWidth = 400;
        int manaBarHeight = 32;
        int manaBarPosX = GamePanel.WIDTH/2 - manaBarWidth/2;
        int manaBarPosY = GamePanel.HEIGHT - manaBarHeight*3;
        manaBar = new Bar(gp, manaBarPosX, manaBarPosY, manaBarWidth, manaBarHeight, (int)maxMana, (int)mana, Color.blue);



    }

    public void loadAnimation() {
        animations = new Animation[5];

        // Анимация "вниз" (новый массив для каждой анимации)
        BufferedImage[] downImages = new BufferedImage[6];
        downImages[0] = resourceLoader.loadImage("/resources/player/down/playerDown1.png");
        downImages[1] = resourceLoader.loadImage("/resources/player/down/playerDown2.png");
        downImages[2] = resourceLoader.loadImage("/resources/player/down/playerDown3.png");
        downImages[3] = resourceLoader.loadImage("/resources/player/down/playerDown4.png");
        downImages[4] = resourceLoader.loadImage("/resources/player/down/playerDown5.png");
        downImages[5] = resourceLoader.loadImage("/resources/player/down/playerDown6.png");
        animations[0] = new Animation(downImages, 5);

        // Анимация "вверх"
        BufferedImage[] upImages = new BufferedImage[6];
        upImages[0] = resourceLoader.loadImage("/resources/player/up/playerUp1.png");
        upImages[1] = resourceLoader.loadImage("/resources/player/up/playerUp2.png");
        upImages[2] = resourceLoader.loadImage("/resources/player/up/playerUp3.png");
        upImages[3] = resourceLoader.loadImage("/resources/player/up/playerUp4.png");
        upImages[4] = resourceLoader.loadImage("/resources/player/up/playerUp5.png");
        upImages[5] = resourceLoader.loadImage("/resources/player/up/playerUp6.png");
        animations[1] = new Animation(upImages, 5);

        // Анимация "влево"
        BufferedImage[] leftImages = new BufferedImage[6];
        leftImages[0] = resourceLoader.loadImage("/resources/player/left/playerLeft1.png");
        leftImages[1] = resourceLoader.loadImage("/resources/player/left/playerLeft2.png");
        leftImages[2] = resourceLoader.loadImage("/resources/player/left/playerLeft3.png");
        leftImages[3] = resourceLoader.loadImage("/resources/player/left/playerLeft4.png");
        leftImages[4] = resourceLoader.loadImage("/resources/player/left/playerLeft5.png");
        leftImages[5] = resourceLoader.loadImage("/resources/player/left/playerLeft6.png");
        animations[2] = new Animation(leftImages, 5);

        // Анимация "вправо"
        BufferedImage[] rightImages = new BufferedImage[6];
        rightImages[0] = resourceLoader.loadImage("/resources/player/right/playerRight1.png");
        rightImages[1] = resourceLoader.loadImage("/resources/player/right/playerRight2.png");
        rightImages[2] = resourceLoader.loadImage("/resources/player/right/playerRight3.png");
        rightImages[3] = resourceLoader.loadImage("/resources/player/right/playerRight4.png");
        rightImages[4] = resourceLoader.loadImage("/resources/player/right/playerRight5.png");
        rightImages[5] = resourceLoader.loadImage("/resources/player/right/playerRight6.png");
        animations[3] = new Animation(rightImages, 5);

        // Анимация "вправо"
        BufferedImage[] nullImages = new BufferedImage[6];
        nullImages[0] = resourceLoader.loadImage("/resources/player/null/playerNull1.png");
        nullImages[1] = resourceLoader.loadImage("/resources/player/null/playerNull2.png");
        nullImages[2] = resourceLoader.loadImage("/resources/player/null/playerNull3.png");
        nullImages[3] = resourceLoader.loadImage("/resources/player/null/playerNull4.png");
        nullImages[4] = resourceLoader.loadImage("/resources/player/null/playerNull5.png");
        nullImages[5] = resourceLoader.loadImage("/resources/player/null/playerNull6.png");
        animations[4] = new Animation(nullImages, 5);
    }

    public void loadSaveSpells(String spellsPath) {
        if(spellsPath == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(spellsPath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл saveSpells пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }
            int enemyCount = Integer.parseInt(line);
            // Создаем массив нужного размера
            spells = new Spell[enemyCount];
            countSpells = 0;

            while((line = reader.readLine()) != null) {
                String[] parts = line.split("_");
                if(parts[1].equals("key")) {
                    addSpell("key", parts);
                }
                if(parts[1].equals("wrench")) {
                    addSpell("wrench", parts);
                }
                if(parts[1].equals("gun")) {
                    addSpell("gun", parts);
                }
            }






        } catch (Exception e) {
            System.err.println("Критическая ошибка загрузки сохраненных заклинаний: ");
        }
    }

    public void loadSpells(String spellPath) {
        try (InputStream is = getClass().getResourceAsStream(spellPath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + spellPath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл spells пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }

            int count = Integer.parseInt(line);
            // Создаем массив нужного размера
            spells = new Spell[count];
            countSpells = 0;

            while((line = br.readLine()) != null) {
                String[] parts = line.split("_");
                if(parts[1].equals("key")) {
                    addSpell("key", parts);
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка при загрузке заклинаний: " + e.getMessage());
        }
    }

    public void addSpell(String name, String[] parts) {
        if(name.equals("key")) {
            spells[countSpells++] = new KeySpell(gp, parts);
        }

        if(name.equals("wrench")) {
            spells[countSpells++] = new WrenchSpell(gp, parts);
        }

        if(name.equals("gun")) {
            spells[countSpells++] = new GunSpell(gp,parts);
        }
    }

    public void click() {
        click = true;
    }

    public void setDefaultValues() {

        countBook = 0;

        maxHealth = 100;
        health = 10;

        maxMana = 100;
        mana = 10;

        loadSpells(DEFAULT_SPELLS);

        worldX = GamePanel.tileSize*35;
        worldY = GamePanel.tileSize*17;
        float pixelsPerSecond = 200f;
        speed = (pixelsPerSecond * GamePanel.scale) / GamePanel.UPDATE_RATE; // scale минимум 1/4 и максимум 2.

    }

    public Spell[] getSpells() {
        return spells;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public void setCountBook(int countBook) {
        this.countBook = countBook;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public int getCountBook() {
        return countBook;
    }

    public double getMana() {
        return mana;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public void loadPlayerFromFile(String filePath) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл savePlayer пуст");
                return ;// Если файл закончился раньше, чем ожидалось
            }

            String[] parts = line.split("_");
            worldX = Integer.parseInt(parts[0]);
            worldY = Integer.parseInt(parts[1]);
            line = reader.readLine();
            countBook = Integer.parseInt(line);
            gp.directory = new Directory(gp);
            for(int i = 0; i < countBook; i++) {
                gp.directory.addInfo();
            }


            line = reader.readLine();
            parts = line.split("_");
            maxHealth = Integer.parseInt(parts[0]);
            health = Integer.parseInt(parts[1]);

            line = reader.readLine();
            parts = line.split("_");
            maxMana = Integer.parseInt(parts[0]);
            mana = Integer.parseInt(parts[1]);



            // Потом поменяю //////////
            float pixelsPerSecond = 200f;
            speed = (pixelsPerSecond * GamePanel.scale) / GamePanel.UPDATE_RATE; // scale минимум 1/4 и максимум 2.
            ///////////////////

        } catch (Exception e) {
            System.out.println("Ошибка загрузки игрока!");
        }

    }


    public void update() {
//        System.out.println("Pos: " + worldX + " : " + worldY);
        if(gp.sceneLoader.getCutScene()) {
            return;
        }
        if(gp.state.equals(GamePanel.GameState.StartMenu) || gp.state.equals(GamePanel.GameState.GameOpenBoard)) {
            return;
        }

        // Проверка коллизии с врагами
        if(gp.getEnemies() != null) { // Добавьте проверку на null
            for (Enemy enemy : gp.getEnemies()) {
                if (enemy != null && gp.getCollision().checkEntityCollision(this, enemy)) {
                    handleEnemyCollision(enemy);
                }
            }
        }

        // Определяет направление движения все 8
        if (GamePanel.keys[0] && GamePanel.keys[3]) {
            direction = "up_right";
        } else if (GamePanel.keys[0] && GamePanel.keys[1]) {
            direction = "up_left";
        } else if (GamePanel.keys[2] && GamePanel.keys[1]) {
            direction = "down_left";
        } else if (GamePanel.keys[2] && GamePanel.keys[3]) {
            direction = "down_right";
        } else if(GamePanel.keys[0]) {
            direction = "up";
        } else if(GamePanel.keys[1]) {
            direction = "left";
        } else if(GamePanel.keys[2]) {
            direction = "down";
        } else if(GamePanel.keys[3]) {
            direction = "right";
        } else direction = "null";
        //Проверка коллизии
        if(direction.equals("up_right")) {
            for(int i = 0; i < speed/sqrt(2); i++) {// Обработка движения вверх
                if(gp.getCollision().checkCollisionUp(this)) {
                    if(worldY > - 0) worldY -= 1;
                }
                if(gp.getCollision().checkCollisionRight(this)) {
                    if (worldX < gp.getWorldWidth()*GamePanel.tileSize-GamePanel.tileSize*2-1) worldX += 1;
                }
            }
        } else if(direction.equals("up_left")) {
            for(int i = 0; i < speed/sqrt(2); i++) {// Обработка движения вверх
                if(gp.getCollision().checkCollisionUp(this)) {
                    if(worldY > - 0) worldY -= 1;
                }
                if(gp.getCollision().checkCollisionLeft(this)) {
                    if(worldX > 1) worldX -= 1;
                }
            }
        } else if(direction.equals("down_left")) {
            for(int i = 0; i < speed/sqrt(2); i++) {// Обработка движения вверх
                if(gp.getCollision().checkCollisionDown(this)) {
                    if(worldY < gp.getWorldHeight()*GamePanel.tileSize-GamePanel.tileSize*4 - 1) worldY += 1;
                }
                if(gp.getCollision().checkCollisionLeft(this)) {
                    if(worldX > 1) worldX -= 1;
                }
            }
        } else if(direction.equals("down_right")) {
            for(int i = 0; i < speed/sqrt(2); i++) {// Обработка движения вверх
                if(gp.getCollision().checkCollisionDown(this)) {
                    if(worldY < gp.getWorldHeight()*GamePanel.tileSize-GamePanel.tileSize*4 - 1) worldY += 1;
                }
                if(gp.getCollision().checkCollisionRight(this)) {
                    if(worldX < gp.getWorldWidth()*GamePanel.tileSize-GamePanel.tileSize*2-1) worldX += 1;
                }
            }
        } else if(direction.equals("up")) {
            for(int i = 0; i < speed/sqrt(2); i++) {
                if(worldY > - 0 && gp.getCollision().checkCollisionUp(this)) worldY -= 1;
            }
        } else if(direction.equals("down")) {
            for(int i = 0; i < speed/sqrt(2); i++) if(gp.getCollision().checkCollisionDown(this) && worldY < gp.getWorldHeight()*GamePanel.tileSize-GamePanel.tileSize*4 - 1) worldY += 1;
        } else if(direction.equals("left")) {
            for(int i = 0; i < speed/sqrt(2); i++) if(worldX > 1 && gp.getCollision().checkCollisionLeft(this)) worldX -= 1;
        } else if(direction.equals("right")) {
            for(int i = 0; i < speed/sqrt(2); i++) if(gp.getCollision().checkCollisionRight(this) && worldX < gp.getWorldWidth()*GamePanel.tileSize-GamePanel.tileSize*2-1) worldX += 1;
        }


        switch (direction) {
            case "up": animations[1].update(); break;
            case "down": animations[0].update(); break;
            case "left": case "up_left": case "down_left": animations[2].update(); break;
            case "right": case "up_right": case "down_right": animations[3].update(); break;
            case "null": animations[4].update(); break;
        }

//        System.out.println("Player - x: " + worldX + ", y: " + worldY);

        if(health < maxHealth) health += 0.02;
        if(mana < maxMana) mana += 0.02;

        healthBar.setCurrentValue((int)health);
        manaBar.setCurrentValue((int)mana);

        bulletManager.updateAllBullets();

        if(click) {
            // Координаты точек
            double x1 = screenX + (float)GamePanel.tileSize/2 + 8, y1 = screenY + (float)GamePanel.tileSize + 8;
            double x2 = GamePanel.mouseX, y2 = GamePanel.mouseY;

            // Вычисляем разницу координат
            double deltaX = x2 - x1;
            double deltaY = y2 - y1;

            // Вычисляем угол в радианах с помощью Math.atan2
            double angleRad = Math.atan2(deltaY, deltaX);

            // Преобразуем радианы в градусы
            double angleDeg = Math.toDegrees(angleRad);

            // Угол может быть отрицательным (приводим к диапазону [0, 360))
            if (angleDeg < 0) {
                angleDeg += 360;
            }

            int radius = 16;
            Bullet bullet = new Bullet(gp, (int)worldX + collisionWidth/2, (int)worldY + collisionHeight/2, angleDeg, 10, radius);
            bulletManager.addBullet(bullet);
        }

        click = false;
    }

    private void handleEnemyCollision(Enemy enemy) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDamageTime >= damageCooldown) {
            // Наносим урон
            health -= enemy.getDamage();
            lastDamageTime = currentTime;
        }
    }

    public void draw(Graphics2D g) {
        if(gp.sceneLoader.getCutScene()) {
            return;
        }
        if(gp.state.equals(GamePanel.GameState.StartMenu) || gp.state.equals(GamePanel.GameState.GameOpenBoard)) {
            return;
        }
        int playerWidth = (int)(GamePanel.tileSize*2/2);
        int playerHeight = (int)(GamePanel.tileSize*4/2);
        switch (direction) { // Анимирует движение по направлениям
            case "up": animations[1].draw(g, screenX, screenY, playerWidth, playerHeight); break;
            case "down": animations[0].draw(g, screenX, screenY, playerWidth, playerHeight); break;
            case "left":
            case "up_left":
            case "down_left": animations[2].draw(g, screenX, screenY, playerWidth, playerHeight); break;
            case "right":
            case "up_right":
            case "down_right": animations[3].draw(g, screenX, screenY, playerWidth, playerHeight); break;
            case "null": animations[4].draw(g, screenX, screenY, playerWidth, playerHeight);
        }

        healthBar.draw(g);
        manaBar.draw(g);

        bulletManager.drawAllBullets(g);
    }

}
