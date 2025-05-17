package main.java.com.magicode.core;


import main.java.com.magicode.Main;
import main.java.com.magicode.core.utils.*;
import main.java.com.magicode.gameplay.entity.Enemy;
import main.java.com.magicode.gameplay.entity.Player;
import main.java.com.magicode.ui.gamestate.MenuInGame;
import main.java.com.magicode.ui.gamestate.StartMenu;
import main.java.com.magicode.ui.gamestate.Tablet;
import main.java.com.magicode.ui.gamestate.Directory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/*
В этом классе происходят все моменты
 */

public class GamePanel extends JComponent {

    public static final String TITLE = "Magicode";
    public static boolean running; // Отвечает за то, что запущена игра или нет
    public static int isUpgrade = 1;
    public static enum GameState {
        StartMenu,
        Game,
        GameOpenTablet,
        GameOpenDirectory,
        GameOpenBoard,
        GameMenu;
    }

    public GameState state = GameState.StartMenu;

    public static boolean[] keys = new boolean[256]; // Содержит список состояния нажатия всех необходимых клавиш
    public static boolean[] mouseButtons = new boolean[3]; // Для левой, средней и правой кнопок
    public static int mouseX, mouseY; // У вас они уже есть
    public static int keyCooldown;

    public static final int MAS_HEIGHT = 45;
    public static final int MAS_WIDTH = 72;

    public static final int originalTileSize = 16;
    public static double scale = 2;
    public static int tileSize = (int)(originalTileSize*scale);
    public static int WIDTH = MAS_WIDTH*originalTileSize;
    public static int HEIGHT = MAS_HEIGHT*originalTileSize;

    private volatile long thread1WorkTime = 0;
    private volatile long thread2WorkTime = 0;
    private volatile long thread1LastCycle = 0;
    private volatile long thread2LastCycle = 0;
    private volatile long thread1WorkNanos = 0;
    private volatile long thread2WorkNanos = 0;
    private long lastTitleUpdateTime = System.nanoTime();

    // Настройка FPS UPS
    public static final float UPDATE_RATE = 40.0f;
    public static final float DRAW_RATE = 60.0f;
    public static final float UPDATE_RATE_Speed = UPDATE_RATE/100;
    public static final long IDLE_TIME = 1;
// Конец настройки FPS UPS

    // Настройки карты мира
    public static int whoHaveCollision[] = new int[100];
    // Конец настройки карты мира
// Объявление классов необходимых для работы игры
    private Thread thread1;
    private Thread thread2;
    private BufferedImage image;
    private Graphics2D g;
    private Sound sound = new Sound();
// Конец объявления классов необходимых для работы игры
    public GameSaveManager saveManager;

    public StartMenu startMenu;
    public MenuInGame menuInGame;
    public Tablet tablet;
    public Directory directory;

    // Объявление классов Необходимых в процессе разработки
    public Listeners listeners;
    public TextureAtlas textureAtlas;
    public SceneLoader sceneLoader;
    public SceneChanger sceneChanger;
    public Player player;


    public GamePanel() { // Конструктор (что-то делает)
        super();

        saveManager = new GameSaveManager();
        saveManager.ensureSaveDirectoryExists(); // проверка наличия папки.
        textureAtlas = new TextureAtlas(100, 100);

        startMenu = new StartMenu(this, saveManager.checkIfFilesExist());
        listeners = new Listeners(this);

        setWhoHaveCollision();



        setPreferredSize(new Dimension(WIDTH, HEIGHT)); // устанавливает размеры окна приложения
        setFocusable(true);
        requestFocus();
        addKeyListener(listeners); // Добавляет возможность считывать клавиши
        addMouseListener(listeners);
        addMouseMotionListener(listeners);
        addMouseWheelListener(listeners);



    }
    public void setWhoHaveCollision() {
        whoHaveCollision[0] = 1;
        whoHaveCollision[1] = 2;
        whoHaveCollision[2] = 3;
        whoHaveCollision[3] = 4;
        whoHaveCollision[4] = 5;
        whoHaveCollision[5] = 6;
        whoHaveCollision[6] = 7;
        whoHaveCollision[7] = 8;
        whoHaveCollision[8] = 21;
        whoHaveCollision[9] = 22;
        whoHaveCollision[10] = 23;
        whoHaveCollision[11] = 24;
        whoHaveCollision[12] = 25;
        whoHaveCollision[13] = 26;
        whoHaveCollision[14] = 27;
        whoHaveCollision[15] = 28;
        whoHaveCollision[16] = 29;
        whoHaveCollision[17] = 30;
        whoHaveCollision[18] = 31;
        whoHaveCollision[19] = 32;
        whoHaveCollision[20] = 33;
        whoHaveCollision[21] = 201;
        whoHaveCollision[22] = 202;
        whoHaveCollision[23] = 203;
        whoHaveCollision[24] = 204;
        whoHaveCollision[25] = 205;
        whoHaveCollision[26] = 206;
        whoHaveCollision[27] = 207;
        whoHaveCollision[28] = 208;
        whoHaveCollision[29] = 227;
        whoHaveCollision[30] = 228;
        whoHaveCollision[31] = 229;
        whoHaveCollision[32] = 230;
        whoHaveCollision[33] = 231;
        whoHaveCollision[34] = 232;
        whoHaveCollision[35] = 233;
        whoHaveCollision[36] = 234;
        whoHaveCollision[37] = 235;
        whoHaveCollision[38] = 236;
        whoHaveCollision[39] = 237;
        whoHaveCollision[40] = 238;
    }

    public void setupGame() {
//        playMusic(0);
    }


    public void run1() {
        int fps = 0;
        int upd = 0;
        int updl = 0;
        long count = 0;
        float deltaUpdate = 0;
        float deltaDraw = 0;
        long lastTime = Time.get();

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final float UPDATE_INTERVAL = (float)Time.SECOND / UPDATE_RATE;
        final float DRAW_INTERVAL = Time.SECOND / DRAW_RATE;

        while (running) {
            long cycleStart = Time.get();
            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;

            count += elapsedTime;

            // Обновление логики
            deltaUpdate += (elapsedTime / UPDATE_INTERVAL);
            while (deltaUpdate > 1) {
                update1();
                upd++;
                deltaUpdate--;
            }

            // Отрисовка
            deltaDraw += (elapsedTime / DRAW_INTERVAL);
            if (deltaDraw > 1) {
                render1();
                fps++;
                deltaDraw--;
            }

            // Измеряем время работы цикла
            thread1WorkNanos += Time.get() - cycleStart;

            // Обновляем заголовок раз в секунду
            if (count >= Time.SECOND) {
                double secondsPassed = count / 1e9;
                double thread1Load = (thread1WorkNanos / 1e7) / secondsPassed;
                double thread2Load = (thread2WorkNanos / 1e7) / secondsPassed;

                // Ограничиваем максимум 999% и округляем
                thread1Load = Math.min(Math.round(thread1Load * 10) / 10.0, 999.9);
                thread2Load = Math.min(Math.round(thread2Load * 10) / 10.0, 999.9);

                Main.setTitle(String.format("%s || Fps: %d | Upd: %d | Updl: %d | T1: %.1f%% | T2: %.1f%%",
                        TITLE, fps, upd, updl, thread1Load, thread2Load));

                // Сбрасываем счетчики
                fps = 0;
                upd = 0;
                updl = 0;
                count = 0;
                thread1WorkNanos = 0;
                thread2WorkNanos = 0;
            }

            // Регулируем FPS
            long cycleTime = Time.get() - cycleStart;
            long sleepTime = (long)(DRAW_INTERVAL - cycleTime) / 1000000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void run2() {
        int upd = 0;
        long count = 0;
        float deltaUpdate = 0;
        long lastTime = Time.get();

        final float UPDATE_INTERVAL = (float)Time.SECOND / UPDATE_RATE;

        while (running) {
            long cycleStart = Time.get();
            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;

            count += elapsedTime;

            // Обновление логики
            deltaUpdate += (elapsedTime / UPDATE_INTERVAL);
            while (deltaUpdate > 1) {
                update2();
                upd++;
                deltaUpdate--;
            }

            // Измеряем время работы цикла
            thread2WorkNanos += Time.get() - cycleStart;

            // Сбрасываем счетчики при обновлении заголовка (в run1)
            if (count >= Time.SECOND) {
                count = 0;
            }

            // Регулируем частоту обновлений
            long cycleTime = Time.get() - cycleStart;
            long sleepTime = (long)(UPDATE_INTERVAL - cycleTime) / 1000000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized String getWindowTitle(int fps, int upd, int updl) {
        double thread1Load = (double)thread1WorkTime / (Time.SECOND / 1000);
        double thread2Load = (double)thread2WorkTime / (Time.SECOND / 1000);

        return TITLE + " || Fps: " + fps +
                " | Upd: " + upd +
                " | Updl: " + updl +
                " | Thread1: " + String.format("%.1f", thread1Load * 100) + "%" +
                " | Thread2: " + String.format("%.1f", thread2Load * 100) + "%";
    }


    public void start() {
        if(running) return;
        running = true;

        thread1 = new Thread(this::run1);
        thread2 = new Thread(this::run2);
        thread1.start();
        thread2.start();
    }

    public void startNewGame() {
//        sceneLoader = new SceneLoader(this, null, null);
        sceneChanger = new SceneChanger(this, true, null);
        tablet = new Tablet(this, null);
        directory = new Directory(this);
        player = new Player(this, null, null);
        menuInGame = new MenuInGame(this);
    }

    public void continueGame() {
//        sceneLoader = new SceneLoader(this, saveManager.getSaveFilePathBackground(), saveManager.getSaveFilePathStructure());
        sceneChanger = new SceneChanger(this, false, saveManager.getSaveFilePathSceneInfo());
        tablet = new Tablet(this, saveManager.getSaveFilePathOpenSpellsInfo());
        directory = new Directory(this);
        tablet.loadSaveValues(saveManager.getSaveFilePathTabletInfo());
        player = new Player(this, saveManager.getSaveFilePathPlayer(), saveManager.getSaveFilePathSpells());
        menuInGame = new MenuInGame(this);

    }

    public void exitGame() {
        System.out.println("Выход из игры!");
        System.exit(0);
    }

    public void saveGame() {
        saveManager.saveGame(sceneLoader.getWorldMap(), sceneLoader.getStructures(), player, sceneChanger, sceneLoader.getObjects(), sceneLoader.getEnemies(), player.getSpells(), tablet);
        startMenu.setState(true);
    }


    public void click() {
        if(state.equals(GameState.StartMenu)) startMenu.click();
        if(state.equals(GameState.GameMenu) || state.equals(GameState.Game)) menuInGame.click();
        if(state.equals(GameState.GameOpenTablet)) {
            tablet.click();
            tablet.click2();
        }
        if(state.equals(GameState.GameOpenDirectory)){
            directory.click();
        }
        if(state.equals(GameState.GameOpenBoard)) {
            sceneLoader.getBoard().click();
        }
    }

    public void keyCooldown() {
        if(keyCooldown > 0) {
            keyCooldown++;
            if(keyCooldown == 20) keyCooldown = 0;
        } else {
            if(keys[6] || keys[7] || keys[8]) {
                keyCooldown++;
            }

        }
    }


    public int getWorldWidth() {
        return sceneLoader.getSceneWidth();
    }

    public int getWorldHeight() {
        return sceneLoader.getSceneHeight();
    }

    public Collision getCollision() {
        return sceneLoader.getCollision();
    }

    public char[] getTypedChars() {
        return listeners.getTypedChars();
    }

    public void update1() {
        if(state.equals(GameState.Game)) {
            player.update();
        }


    }

    public void update2() {

        keyCooldown();

        if(state.equals(GameState.StartMenu)) {
            startMenu.update();
        }

        if(state.equals(GameState.GameMenu) || state.equals(GameState.Game)) {
            menuInGame.update();
        }

        if(state.equals(GameState.Game) || state.equals(GameState.GameOpenBoard)) {
            sceneLoader.update();
        }

        if(state.equals(GameState.GameOpenTablet)) {
            tablet.update();
        }
        if(state.equals(GameState.GameOpenDirectory)) {
            directory.update();
        }



    }
    public void render1(){
        if(state.equals(GamePanel.GameState.StartMenu)) {
            startMenu.draw(g);
        }


        // Game
        if(state.equals(GameState.Game) || state.equals(GameState.GameOpenBoard)) { // Так как оно может быть еще не создано
            sceneLoader.draw(g);
            player.draw(g);
//            sceneLoader.getInteraction().drawInteractionZones(g);
        }

        if(state.equals(GameState.GameMenu) || state.equals(GameState.Game)) { // так как оно может быть еще не создано
            menuInGame.draw(g);
        }

        if(state.equals(GameState.GameOpenTablet)) {
            tablet.draw(g);
        }

        if(state.equals(GameState.GameOpenDirectory)) {
            directory.draw(g);
        }

        draw();
    }

    public void draw() { // Просто рисует полученный результат
        Graphics g2 = this.getGraphics();
        if(g2 != null) {
            g2.drawImage(image, 0, 0, null);
            g2.dispose();
        }
    }

    public void playMusic(int i) {

        sound.setFile(i);
        sound.play();
        sound.loop();

    }

    public void stopMusic() {

        sound.stop();
    }

    public void playSE(int i) {

        sound.setFile(i);
        sound.play();
    }

}
