package main.java.com.magicode.gameplay.world;

import main.java.com.magicode.core.GamePanel;

public class Layer {
    private int firstLayer;
    private int secondLayer;
    private boolean collision = false;
    private String interactionZone;

    public void setLayers(String input) {
        // Разделяем строку по символу '_'
        String[] parts = input.split(":");

        // Проверяем, что строка разделилась на две части
        if (parts.length == 2) {
            // Преобразуем первую часть в число
            firstLayer = Integer.parseInt(parts[0]);

            // Преобразуем вторую часть в число
            secondLayer = Integer.parseInt(parts[1]);

            for(int i = 0; i < GamePanel.whoHaveCollision.length; i++) {
                if(GamePanel.whoHaveCollision[i] != 0 && (GamePanel.whoHaveCollision[i] == firstLayer || GamePanel.whoHaveCollision[i] == secondLayer)) {
                    setCollision(true);
                    break;
                }
            }

        } else {
            System.out.println("Строка не соответствует формату 'число_число'");
        }
    }

    public int getLayer(int number) {
        if(number == 1) return firstLayer;
        else return secondLayer;
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    public boolean getCollision() {
        return collision;
    }

    public void setInteractionZone(String interactionZone) {
        this.interactionZone = interactionZone;
    }
    public String getInteractionZone() {
        return interactionZone;
    }
}
