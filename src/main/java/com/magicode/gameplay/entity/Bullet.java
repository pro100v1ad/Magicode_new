package main.java.com.magicode.gameplay.entity;

import main.java.com.magicode.core.GamePanel;

import java.awt.*;


public class Bullet extends Entity{


    private int radius;
    private double angle;

    private GamePanel gp;

    public Bullet(GamePanel gp, int worldX, int worldY, double angle, int speed, int radius) {
        this.gp = gp;
        this.angle = angle;

        this.worldX = worldX;
        this.worldY = worldY;

        this.speed = speed;
        this.radius = radius;


    }



    /*
    //TEMP
        // Координаты точек
        double x1 = screenX, y1 = screenY;
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

        System.out.println("Угол поворота линии: " + angleDeg + "°");
     */

    public boolean update() {
        // Двигаем пулю по углу
        worldX += speed * Math.cos(Math.toRadians(angle));
        worldY += speed * Math.sin(Math.toRadians(angle));

        // Проверяем коллизии
        if(checkWallCollision()) {
            return false; // Пуля должна исчезнуть
        }

        return true; // Пуля продолжает существовать
    }

    private boolean checkWallCollision() {
        // Проверяем, вышла ли пуля за границы мира
        if(worldX < radius || worldX > gp.getWorldWidth() * GamePanel.tileSize - radius ||
                worldY < radius || worldY > gp.getWorldHeight() * GamePanel.tileSize - radius) {
            return true;
        }
        for(int i = 0; i < radius; i++) {
            for(int j = 0; j < radius; j++) {

                if(gp.getCollision().getCollisionMap()[(int)worldY + i][(int)worldX + j] == 1) {
                    return true;
                }
            }
        }

        return false;




    }
    public void draw(Graphics2D g) {

        g.setColor(Color.YELLOW);
        int screenX = (int) (worldX - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int) (worldY - gp.player.getWorldY() + gp.player.getScreenY());

        if (worldX + GamePanel.tileSize > gp.player.getWorldX() - gp.player.getScreenX() &&
                worldX - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                worldY + GamePanel.tileSize > gp.player.getWorldY() - gp.player.getScreenY() &&
                worldY - GamePanel.tileSize * 4 < gp.player.getWorldY() + gp.player.getScreenY())
        {

            g.fillOval(screenX, screenY, radius, radius);
        }

    }

}
