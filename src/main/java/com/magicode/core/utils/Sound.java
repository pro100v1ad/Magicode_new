package main.java.com.magicode.core.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class Sound {

    private Clip clip;
    private String[] soundFiles = new String[30];

    public Sound() {
        // Указываем пути к звуковым файлам относительно ресурсов
        soundFiles[0] = "/resources/sounds/Menu.wav";
        soundFiles[1] = "/resources/sounds/Merchant.wav";
        soundFiles[2] = "/resources/sounds/Dungeon.wav";
        soundFiles[3] = "/resources/sounds/FinalBattle.wav";
        soundFiles[4] = "/resources/sounds/gameover.wav";
        soundFiles[5] = "/resources/sounds/parry.wav";
        soundFiles[6] = "/resources/sounds/repair.wav";
        soundFiles[7] = "/resources/sounds/stairs.wav";
        soundFiles[8] = "/resources/sounds/hitmonster.wav";
        soundFiles[9] = "/resources/sounds/receivedamage.wav";
        soundFiles[10] = "/resources/sounds/chest.wav";
    }

    public void setFile(int i) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(soundFiles[i]);
            if (audioSrc == null) {
                System.out.println("Ошибка: звуковой файл не найден! " + soundFiles[i]);
                return;
            }

            // Оберните InputStream в BufferedInputStream
            BufferedInputStream bufferedInputStream = new BufferedInputStream(audioSrc);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedInputStream);
            clip = AudioSystem.getClip();
            clip.open(ais);
            System.out.println("Звуковой файл успешно загружен: " + soundFiles[i]);
        } catch (Exception e) {
            System.out.println("Ошибка в Sound: " + e.getMessage());
        }
    }

    public void play(int i) {
        if (clip == null) {
            System.out.println("Ошибка: звуковой файл не загружен!");
            System.out.println("Ошибка была в файле:" + i);
            return;
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop(int i) {
        if (clip == null) {
            System.out.println("Ошибка: звуковой файл не загружен для лупа!");
            System.out.println("Ошибка была в файле:" + i);
            return;
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip == null) {
            System.out.println("Ошибка: звуковой файл не загружен для стопа!");
            return;
        }
        clip.stop();
        clip.close();
    }
}