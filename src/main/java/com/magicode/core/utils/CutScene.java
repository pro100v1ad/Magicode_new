package main.java.com.magicode.core.utils;


import main.java.com.magicode.core.GamePanel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.Math.sqrt;

public class CutScene {

    private String[] sequenceOfCommands;
    private boolean isStart;
    private int numberCurrentCommands;
    private int currentTime;

    private GamePanel gp;

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
        if(isStart) {

            try {
                if(sequenceOfCommands[numberCurrentCommands] != null) {

                    String[] command = sequenceOfCommands[numberCurrentCommands].split(" ");

                    if (Integer.parseInt(command[1]) > currentTime) {

                        gp.player.movePlayer(command[0], Integer.parseInt(command[2])); // Двигаю игрока

                        currentTime += 1;
                        if (currentTime == Integer.parseInt(command[1])) {
                            currentTime = 0;
                            numberCurrentCommands++;
                        }
                    }
                } else {
                    isStart = false;
                }
            }catch (Exception e) {
                isStart = false;
                System.out.println("Кат сцена завершена");
            }




        }
    }

    public void draw(Graphics2D g) {
        if(gp.player != null) {
            gp.player.draw(g);
        }

    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean getStart() {
        return isStart;
    }
}
