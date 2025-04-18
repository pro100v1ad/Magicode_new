package main.java.com.magicode.core.utils;

import main.java.com.magicode.core.GamePanel;

import java.util.Arrays;

import static java.lang.Math.sqrt;

public class CutScene {

    private String[] route;
    private GamePanel gp;
    /*
    route ->
    [direction:distance:speed]
    distance - по пикселям
    speed - по пикселям
     */
    public CutScene(GamePanel gp, String[] route) {
        this.route = Arrays.copyOf(route, route.length);
        this.gp = gp;

    }

    public boolean update() {

        for(int i = 0; i < route.length; i++) {
            String str[] = route[i].split(":");

            if (route[route.length-1].split(":")[1].equals("0")) {
                return true;
            }

            if(!str[1].equals("0")) {

                int distance = Integer.parseInt(str[1]);
                int speed = Integer.parseInt(str[2]);
                if(str[0].equals("up")) {
                    if(distance - speed > 0) {
                        gp.player.setWorldY((int)(gp.player.getWorldY() - speed));
                        distance -= speed;
                    }
                    else {
                        gp.player.setWorldY((int)(gp.player.getWorldY() - distance));
                        distance = 0;
                    }

                    route[i] = "up:" + distance + ":" + speed;
                    break;
                } else if(str[0].equals("down")) {
                    if(distance - speed > 0) {
                        gp.player.setWorldY((int)(gp.player.getWorldY() + speed));
                        distance -= speed;
                    }
                    else {
                        gp.player.setWorldY((int)(gp.player.getWorldY() + distance));
                        distance = 0;
                    }

                    route[i] = "down:" + distance + ":" + speed;
                    break;
                } else if(str[0].equals("left")) {
                    if(distance - speed > 0) {
                        gp.player.setWorldX((int)(gp.player.getWorldX() - speed));
                        distance -= speed;
                    }
                    else {
                        gp.player.setWorldX((int)(gp.player.getWorldX() - distance));
                        distance = 0;
                    }
                    route[i] = "left:" + distance + ":" + speed;
                    break;
                } else if(str[0].equals("right")) {
                    if(distance - speed > 0) {
                        gp.player.setWorldX((int)(gp.player.getWorldX() + speed));
                        distance -= speed;
                    }
                    else {
                        gp.player.setWorldX((int)(gp.player.getWorldX() + distance));
                        distance = 0;
                    }
                    route[i] = "right:" + distance + ":" + speed;
                    break;
                } else if(str[0].equals("up_left")) {
                    double diagonalSpeed = speed*sqrt(2);
                    if(distance - diagonalSpeed > 0) {
                        gp.player.setWorldX((int)(gp.player.getWorldX() - diagonalSpeed));
                        gp.player.setWorldY((int)(gp.player.getWorldY() - diagonalSpeed));
                        distance -= speed;
                    }
                    else {
                        gp.player.setWorldX((int)(gp.player.getWorldX() - distance*sqrt(2)));
                        gp.player.setWorldY((int)(gp.player.getWorldY() - distance*sqrt(2)));
                        distance = 0;
                    }
                    route[i] = "up_left:" + distance + ":" + speed;
                    break;
                } else if(str[0].equals("up_right")) {
                    double diagonalSpeed = speed*sqrt(2);
                    if(distance - diagonalSpeed > 0) {
                        gp.player.setWorldX((int)(gp.player.getWorldX() + diagonalSpeed));
                        gp.player.setWorldY((int)(gp.player.getWorldY() - diagonalSpeed));
                        distance -= speed;
                    }
                    else {
                        gp.player.setWorldX((int)(gp.player.getWorldX() + distance*sqrt(2)));
                        gp.player.setWorldY((int)(gp.player.getWorldY() - distance*sqrt(2)));
                        distance = 0;
                    }
                    route[i] = "up_right:" + distance + ":" + speed;
                    break;
                } else if(str[0].equals("down_left")) {
                    double diagonalSpeed = speed*sqrt(2);
                    if(distance - diagonalSpeed > 0) {
                        gp.player.setWorldX((int)(gp.player.getWorldX() - diagonalSpeed));
                        gp.player.setWorldY((int)(gp.player.getWorldY() + diagonalSpeed));
                        distance -= speed;
                    }
                    else {
                        gp.player.setWorldX((int)(gp.player.getWorldX() - distance*sqrt(2)));
                        gp.player.setWorldY((int)(gp.player.getWorldY() + distance*sqrt(2)));
                        distance = 0;
                    }
                    route[i] = "down_left:" + distance + ":" + speed;
                    break;
                } else if(str[0].equals("down_right")) {
                    double diagonalSpeed = speed*sqrt(2);
                    if(distance - diagonalSpeed > 0) {
                        gp.player.setWorldX((int)(gp.player.getWorldX() + diagonalSpeed));
                        gp.player.setWorldY((int)(gp.player.getWorldY() + diagonalSpeed));
                        distance -= speed;
                    }
                    else {
                        gp.player.setWorldX((int)(gp.player.getWorldX() + distance*sqrt(2)));
                        gp.player.setWorldY((int)(gp.player.getWorldY() + distance*sqrt(2)));
                        distance = 0;
                    }
                    route[i] = "down_right:" + distance + ":" + speed;
                    break;
                }

            }

        }
        return false;
    }



}
