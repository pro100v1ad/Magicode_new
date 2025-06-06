package main.java.com.magicode.core.utils;
import main.java.com.magicode.gameplay.entity.Bullet;
import main.java.com.magicode.gameplay.entity.Enemy;
import main.java.com.magicode.gameplay.entity.Player;

import java.awt.*;
import java.util.ArrayList;

public class BulletManager { // Класс отвечающий за управление пулями

    private ArrayList<Bullet> bullets = new ArrayList<>();

    // Добавление новой пули
    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    //Проверка урона игроку
    public void checkBulletFromPlayer(Player player) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if(!bullet.getTypeBullet()) {
                // Создаем прямоугольник для пули
                Rectangle bulletRect = new Rectangle(
                        (int)bullet.getWorldX(),
                        (int)bullet.getWorldY(),
                        bullet.getRadius(),
                        bullet.getRadius()
                );

                // Создаем прямоугольник для игрока
                Rectangle playerRect = new Rectangle(
                        (int) player.getWorldX(),
                        (int) player.getWorldY(),
                        player.getCollisionWidth(),
                        player.getCollisionHeight()
                );

                // Проверяем пересечение
                if (bulletRect.intersects(playerRect)) {
                    bullets.remove(i); // Удаляем пулю
                    player.takeDamage((int)bullet.getDamage()); // Наносим урон игроку
                }
            }
        }
    }

    //Проверка урона врагам
    public void checkBulletFromEnemy(Enemy enemy) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if(bullet.getTypeBullet()) {
                // Создаем прямоугольник для пули
                Rectangle bulletRect = new Rectangle(
                        (int)bullet.getWorldX(),
                        (int)bullet.getWorldY(),
                        bullet.getRadius(),
                        bullet.getRadius()
                );
                // Создаем прямоугольник для игрока
                Rectangle playerRect = new Rectangle(
                        (int) enemy.getWorldX(),
                        (int) enemy.getWorldY(),
                        enemy.getCollisionWidth(),
                        enemy.getCollisionHeight()
                );
                // Проверяем пересечение
                if (bulletRect.intersects(playerRect)) {
                    bullets.remove(i); // Удаляем пулю
                    enemy.takeDamage((int)bullet.getDamage()); // Наносим урон игроку
                }
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

}
