package main.java.com.magicode.core.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class Sound { // Класс отвечающий за звук

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
                return;
            }

            // Оберните InputStream в BufferedInputStream
            BufferedInputStream bufferedInputStream = new BufferedInputStream(audioSrc);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedInputStream);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            System.out.println("Ошибка в Sound: " + e.getMessage());
        }

        setVolume(0.05f);
    }

    public void play(int i) {
        if (clip == null) {
            return;
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop(int i) {
        if (clip == null) {
            return;
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip == null) {
            return;
        }
        clip.stop();
        clip.close();
    }

    // Метод для изменения громкости
    public void setVolume(float volume) {
        if (clip == null) {
            return;
        }

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Устанавливаем громкость (в децибелах)
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
}