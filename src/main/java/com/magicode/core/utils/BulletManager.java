package main.java.com.magicode.core.utils;
import main.java.com.magicode.gameplay.entity.Bullet;
import main.java.com.magicode.gameplay.entity.Player;

import java.awt.*;
import java.util.ArrayList;

public class BulletManager {

    private ArrayList<Bullet> bullets = new ArrayList<>();

    // Добавление новой пули
    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    // Удаление пули по индексу
    public void removeBullet(int index) {
        if (index >= 0 && index < bullets.size()) {
            bullets.remove(index);
        }
    }

    // Удаление конкретной пули (по объекту)
    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
    }

    // Получение пули по индексу
    public Bullet getBullet(int index) {
        if (index >= 0 && index < bullets.size()) {
            return bullets.get(index);
        }
        return null;
    }

    // Получение количества пуль
    public int getBulletCount() {
        return bullets.size();
    }

    //Проверка урона игроку
    public void checkBulletFromPlayer(Player player) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);

            // Создаем прямоугольник для пули
            Rectangle bulletRect = new Rectangle(
                    (int)bullet.getWorldX(),
                    (int)bullet.getWorldY(),
                    bullet.getRadius(),
                    bullet.getRadius()
            );
//            System.out.println("bullet - x:" + (int)bullet.getWorldX() + " y:" + (int)bullet.getWorldY());

            // Создаем прямоугольник для игрока
            Rectangle playerRect = new Rectangle(
                    (int) player.getWorldX(),
                    (int) player.getWorldY(),
                    (int) player.getCollisionWidth(),
                    (int) player.getCollisionHeight()
            );
//            System.out.println("player - x:" + (int)player.getWorldX() + " y:" + (int)player.getWorldY());

            // Проверяем пересечение
            if (bulletRect.intersects(playerRect)) {
                bullets.remove(i); // Удаляем пулю
                player.takeDamage((int)bullet.getDamage()); // Наносим урон игроку
            }
        }
    }


    // Обновление всех пуль
    public void updateAllBullets() {
        // Используем обычный for, чтобы избежать ConcurrentModificationException при удалении
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            boolean isAlive = bullet.update(); // update() возвращает false, если пулю нужно удалить

            if (!isAlive) {
                bullets.remove(i);
            }
        }
    }

    // Рисование всех пуль
    public void drawAllBullets(Graphics2D g) {
        for(int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.draw(g);
        }
    }

    // Очистка всех пуль
    public void clearAllBullets() {
        bullets.clear();
    }
}
