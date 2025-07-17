package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.GameObject;
import main.java.com.magicode.gameplay.world.Structure;
import main.java.com.magicode.gameplay.world.objects.Book;
import main.java.com.magicode.spells.Spell;
import main.java.com.magicode.spells.spells.GunSpell;
import main.java.com.magicode.spells.spells.WrenchSpell;
import main.java.com.magicode.ui.interface_.TextBubble;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Events {

    private Map<Integer, String> chestTriggers; // Хранит триггеры для сундуков (id -> текст)
    private TextBubble currentTextBubble;

    public Events() {
        this.chestTriggers = new HashMap<>();
        this.currentTextBubble = new TextBubble("Empty", 240); // 240 кадров = ~4 секунды при 60 FPS
        initializeTriggers();
    }

    // Инициализация триггеров (можно вынести в конфиг файл)
    private void initializeTriggers() {
        // Пример: сундук с id 1 показывает сообщение при открытии
        chestTriggers.put(1001, "Попробуйте открыть следующую дверь, используя планшет.");
        chestTriggers.put(1002, "Откройте сундук");
    }

    public void checkTrigger(Structure structure) {

        int id = structure.getCode();

        if (chestTriggers.containsKey(id)) {
            currentTextBubble.setText(chestTriggers.get(id));
            currentTextBubble.setVisible(true);
        }
    }

    public void checkTrigger(Spell spell) {
        if(spell instanceof WrenchSpell) {

            currentTextBubble.setText("Используя это заклинание, вы можете починить мост!");
            currentTextBubble.setVisible(true);

        }

        if(spell instanceof GunSpell) {

            currentTextBubble.setText("Нажмите пробел, чтобы стрелять!");
            currentTextBubble.setVisible(true);

        }
    }

    public void checkTrigger(GameObject object) {
        if(object instanceof Book) {
            currentTextBubble.setText("Вы узнали что-то новое!");
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