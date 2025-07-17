package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.structures.Chest;
import main.java.com.magicode.gameplay.world.structures.Door;
import main.java.com.magicode.ui.interface_.TextBubble;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Events {
    private GamePanel gp;
    private Map<Integer, String> chestTriggers; // Хранит триггеры для сундуков (id -> текст)
    private TextBubble currentTextBubble;
    private int activeTriggerId = -1;

    public Events(GamePanel gp) {
        this.gp = gp;
        this.chestTriggers = new HashMap<>();
        this.currentTextBubble = new TextBubble("Empty", 120); // 120 кадров = ~2 секунды при 60 FPS
        initializeTriggers();
    }

    // Инициализация триггеров (можно вынести в конфиг файл)
    private void initializeTriggers() {
        // Пример: сундук с id 1 показывает сообщение при открытии
        chestTriggers.put(1001, "Вы нашли древний артефакт!");
        chestTriggers.put(2, "Этот сундук был заперт много лет...");
    }

    public void checkTrigger(Structure structure) {

        int id = structure.getCode();

        if (chestTriggers.containsKey(id)) {
            activeTriggerId = id;
            currentTextBubble.setText(chestTriggers.get(id));
            currentTextBubble.setVisible(true);
        }
    }

    public void draw(Graphics2D g) {
        if (currentTextBubble != null) {
            currentTextBubble.draw(g);
        }
    }

    public void addChestTrigger(int chestId, String message) {
        chestTriggers.put(chestId, message);
    }

    public void removeChestTrigger(int chestId) {
        chestTriggers.remove(chestId);
    }
}