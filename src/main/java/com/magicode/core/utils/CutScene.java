package main.java.com.magicode.core.utils;


import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.ui.interface_.TextBubble;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CutScene {

    private String[] sequenceOfCommands;
    private boolean isStart;
    private int numberCurrentCommands;
    private int currentTime;

    private GamePanel gp;
    private TextBubble textBubble;

    private boolean isDarkeningScreen = false;
    private int darkeningScreenTime; // Время затемнения в кадрах (например, 2 сек при 60 FPS = 120)
    private int darkeningScreenCurrentTime = 0;
    private int darkeningScreenPeak; // Пиковая прозрачность (0-255)

    public CutScene(GamePanel gp, String filePath) {

        this.gp = gp;

        loadCommands(filePath);
        isStart = false;
        numberCurrentCommands = 0;
        currentTime = 0;



    }

    private void loadCommands(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int i = 0;
            while(br.readLine() != null) {
                i++;
            }

            sequenceOfCommands = new String[i];

            try (InputStream is2 = getClass().getResourceAsStream(filePath)) {
                if (is2 == null) {
                    return;
                }
                BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
                String line;
                int i2 = 0;
                while((line = br2.readLine()) != null) {
                    sequenceOfCommands[i2] = line;
                    i2++;
                }
            } catch (Exception e) {
                System.err.println("Критическая ошибка заполнение последовательности команд ");
            }


        } catch (Exception e) {
            System.err.println("Критическая ошибка загрузки кат-сцены: ");
        }
    }

    public void update() {
        if(gp.state.equals(GamePanel.GameState.GameMenu) || gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }
        if(isStart) {

            try {
                if(sequenceOfCommands[numberCurrentCommands] != null) {

                    String[] command = sequenceOfCommands[numberCurrentCommands].split("_");

                    if(command[0].equals("portal")) {
                        gp.sceneChanger.setNumberActiveScene(gp.sceneChanger.getNumberActiveScene() + 1);
                        gp.changeMusic();
                    }

                    if(command[0].equals("dark")) {
                        darkeningScreenCurrentTime = 0;
                        darkeningScreenTime = Integer.parseInt(command[1]);
                        darkeningScreenPeak = Integer.parseInt(command[2]);
                        isDarkeningScreen = true;
                        numberCurrentCommands++;
                        return;
                    }

                    if(command[0].equals("createObject")) {
                        gp.sceneLoader.addObject(command[1], Integer.parseInt(command[4]), Integer.parseInt(command[2]), Integer.parseInt(command[3]), 0);
                        numberCurrentCommands++;
                        return;
                    }
                    if(command[0].equals("deleteObject")) {
                        gp.sceneLoader.deleteObject(Integer.parseInt(command[1]));
                        numberCurrentCommands++;
                        return;
                    }

                    if(command[0].equals("text")) {
                        textBubble = new TextBubble(command[1], Integer.parseInt(command[2]));
                        textBubble.setVisible(true);
                        numberCurrentCommands++;
                    } else {
                        if (Integer.parseInt(command[1]) > currentTime) {

                            gp.player.movePlayer(command[0], Integer.parseInt(command[2])); // Двигаю игрока

                            currentTime += 1;
                            if (currentTime == Integer.parseInt(command[1])) {
                                currentTime = 0;
                                numberCurrentCommands++;
                            }
                        }
                    }
                } else {
                    isStart = false;
                }
            }catch (Exception e) {
                isStart = false;
                gp.player.setVisibleSpells(true);
                gp.player.setVisibleHealthBar(true);
                gp.player.setVisibleManaBar(true);
                gp.menuInGame.setVisibleTablet(true);
                gp.menuInGame.setVisibleDirectory(true);
                System.out.println("Кат сцена завершена");
            }




        }
    }

    public void draw(Graphics2D g) {

        if(gp.state.equals(GamePanel.GameState.GameMenu) || gp.state.equals(GamePanel.GameState.StartMenu)) {
            return;
        }

        if(gp.player != null) {
            gp.player.draw(g);
        }

        if(textBubble != null) {
            textBubble.draw(g);
        }

        // Для рисования затемнения экрана

        if (isDarkeningScreen) {
            if (darkeningScreenCurrentTime >= darkeningScreenTime) {
                isDarkeningScreen = false;
                darkeningScreenCurrentTime = 0;
            } else {
                darkeningScreenCurrentTime++;
            }

            // Вычисляем прозрачность (сначала растёт, потом убывает)
            int transparency;
            if (darkeningScreenCurrentTime <= darkeningScreenTime / 2) {
                // Первая половина - затемнение (0 → peak)
                float progress = (float) darkeningScreenCurrentTime / (darkeningScreenTime / 2);
                transparency = (int) (progress * darkeningScreenPeak);
            } else {
                // Вторая половина - осветление (peak → 0)
                float progress = (float) (darkeningScreenCurrentTime - darkeningScreenTime / 2) / (darkeningScreenTime / 2);
                transparency = (int) ((1 - progress) * darkeningScreenPeak);
            }

            // Ограничиваем прозрачность (на всякий случай)
            transparency = Math.max(0, Math.min(255, transparency));

            // Рисуем затемнение
            g.setColor(new Color(0, 0, 0, transparency));
            g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        }



    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean getStart() {
        return isStart;
    }
}
