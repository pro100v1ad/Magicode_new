package main.java.com.magicode.gameplay.world.structures;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.Structure;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Bridge extends Structure {

    private int len;
    private boolean isBreak;
    private BufferedImage[] images;
    private GamePanel gp;

    private String filePath;
    private String condition;


    public Bridge(GamePanel gp, int x, int y, int bridgeLength, String code, String direction, boolean isBreak, String name, String filePath) {
        this.gp = gp;

        this.code = Integer.parseInt(code.split(":")[0]);
        this.radius = Integer.parseInt(code.split(":")[1]);

        this.name = name;
        this.len = bridgeLength;
        this.isBreak = isBreak;
        this.direction = direction;
        this.x = x;
        this.y = y;

        this.filePath = (filePath.equals("null")) ? null : filePath;


        loadDefaultValue();
        loadTextures();

    }

    private void loadDefaultValue() {
        if(filePath == null) {
            return;
        }
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + filePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }
            condition = line;

        }
        catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ошибка загрузки информации о задании для моста: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean getBreak() {
        return isBreak;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getLen() {
        return len;
    }

    public int getW() {
        int w;
        if(direction.equals("up") || direction.equals("down")) {
            w = 8*GamePanel.tileSize;
        } else {
            if(isBreak) {
                w = (11 + len/2)*GamePanel.tileSize;
            } else {
                w = (11 + len/2)*GamePanel.tileSize;
            }
        }
        return w;
    }

    public int getH() {
        int h;
        if(direction.equals("up") || direction.equals("down")) { // Если по вертикали, то надо редактировать!

            if(isBreak) {
                h = (13 + len/2)*GamePanel.tileSize;
            } else {
                h = (12 + len/2)*GamePanel.tileSize;
            }
        } else {
            h = 4*GamePanel.tileSize;
        }
        return h;
    }

    public void repair() {
        isBreak = false;
        loadTextures();
    }


    public boolean checkValues(int first, int second, String third, boolean fourth) {

        if(condition == null) return false;
        if(condition.equals("arg3 == \"key\" + (arg1 - arg2) * (arg1 + arg2)")) {
            return third.equals("key" + (first - second) * (first + second));
        }


        return false;
    }

    protected void loadTextures() {
        images = new BufferedImage[4];

        if(direction.equals("right") || direction.equals("left")){
            images[0] = gp.textureAtlas.textures[19][0].getTexture();
            images[1] = gp.textureAtlas.textures[19][1].getTexture();
            images[2] = gp.textureAtlas.textures[19][2].getTexture();
            images[3] = gp.textureAtlas.textures[19][3].getTexture();

        } else {
            images[0] = gp.textureAtlas.textures[19][4].getTexture();
            images[1] = gp.textureAtlas.textures[19][5].getTexture();
            images[2] = gp.textureAtlas.textures[19][6].getTexture();
            images[3] = gp.textureAtlas.textures[19][7].getTexture();
        }


    }
    @Override
    public void draw(Graphics2D g) {

        int screenX = (int) (x - gp.player.getWorldX() + gp.player.getScreenX());
        int screenY = (int) (y - gp.player.getWorldY() + gp.player.getScreenY());

        int sizeX;
        int sizeY;
        if(direction.equals("up") || direction.equals("down")) {
            sizeX = 10;
            sizeY = 20 + len*2;
        } else {
            sizeX = 20 + len*2;
            sizeY = 10;

        }

        if (x + GamePanel.tileSize*sizeX > gp.player.getWorldX() - gp.player.getScreenX() &&
                x - GamePanel.tileSize * 3 < gp.player.getWorldX() + gp.player.getScreenX() &&
                y + GamePanel.tileSize*sizeY > gp.player.getWorldY() - gp.player.getScreenY() &&
                y - GamePanel.tileSize * 4 < gp.player.getWorldY() + gp.player.getScreenY()) {


            if (direction.equals("right")) {
                g.drawImage(images[0], screenX, screenY, (int)(GamePanel.tileSize * 2.5), GamePanel.tileSize * 4, null);
                if (isBreak) {
                    g.drawImage(images[3], screenX + (int)(GamePanel.tileSize * 2.5), screenY, (int)(GamePanel.tileSize * 2.5), GamePanel.tileSize * 4, null);
                    for (int i = 0; i < len; i++) {
                        g.drawImage(images[2], screenX + GamePanel.tileSize * (5 + i), screenY, GamePanel.tileSize, GamePanel.tileSize * 4, null);
                    }
                    g.drawImage(images[1], screenX + GamePanel.tileSize * (5 + (len)), screenY, GamePanel.tileSize * 2, GamePanel.tileSize * 4, null);
                } else {
                    g.drawImage(images[2], screenX + (int)(GamePanel.tileSize * 2.5), screenY, GamePanel.tileSize, GamePanel.tileSize * 4, null);
                    g.drawImage(images[2], screenX + (int)(GamePanel.tileSize * 3.5), screenY, GamePanel.tileSize, GamePanel.tileSize * 4, null);
                    for (int i = 0; i < len; i++) {
                        g.drawImage(images[2], screenX + GamePanel.tileSize * (int)(4.5 + i), screenY, GamePanel.tileSize, GamePanel.tileSize * 4, null);
                    }
                    g.drawImage(images[1], screenX + GamePanel.tileSize * (int)(4.5 + (len)), screenY, GamePanel.tileSize * 2, GamePanel.tileSize * 4, null);
                }
            } else if (direction.equals("left")) {
                g.drawImage(images[0], screenX, screenY, (int)(GamePanel.tileSize * 2.5), GamePanel.tileSize * 4, null);
                if (isBreak) {
                    for (int i = 0; i < len; i++) {
                        g.drawImage(images[2], screenX + GamePanel.tileSize * (int)(2.5 + i), screenY, GamePanel.tileSize, GamePanel.tileSize * 4, null);
                    }
                    g.drawImage(images[3], screenX + GamePanel.tileSize * (int)(2.5 + len), screenY, (int)(GamePanel.tileSize * 2.5), GamePanel.tileSize * 4, null);
                    g.drawImage(images[1], screenX + GamePanel.tileSize * (int)(4.5 + (len)), screenY, GamePanel.tileSize * 2, GamePanel.tileSize * 4, null);

                } else {
                    g.drawImage(images[2], screenX + (int)(GamePanel.tileSize * 2.5), screenY, GamePanel.tileSize, GamePanel.tileSize * 4, null);
                    g.drawImage(images[2], screenX + (int)(GamePanel.tileSize * 3.5), screenY, GamePanel.tileSize, GamePanel.tileSize * 4, null);
                    for (int i = 0; i < len; i++) {
                        g.drawImage(images[2], screenX + GamePanel.tileSize * (int)(4.5 + i), screenY, GamePanel.tileSize, GamePanel.tileSize * 4, null);
                    }
                    g.drawImage(images[1], screenX + GamePanel.tileSize * (int)(4.5 + (len)), screenY, GamePanel.tileSize * 2, GamePanel.tileSize * 4, null);
                }

            } else if (direction.equals("down")) { /// ЕСли буду использовать его в вертикальном направлении, нужно подредактировать его отображение!
                g.drawImage(images[1], screenX, screenY, GamePanel.tileSize * 4, GamePanel.tileSize * 2, null);

                if (isBreak) {
                    g.drawImage(images[3], screenX, screenY + GamePanel.tileSize * 2, GamePanel.tileSize * 4, GamePanel.tileSize * 3, null);
                    for (int i = 0; i < len; i++) {
                        g.drawImage(images[2], screenX, screenY + GamePanel.tileSize * (9 +  i), GamePanel.tileSize * 4, GamePanel.tileSize, null);
                    }
                    g.drawImage(images[0], screenX, screenY + GamePanel.tileSize * (9 + (len)), GamePanel.tileSize * 4, GamePanel.tileSize * 2, null);
                } else {
                    g.drawImage(images[2], screenX, screenY + GamePanel.tileSize * 2, GamePanel.tileSize * 4, GamePanel.tileSize, null);
                    g.drawImage(images[2], screenX, screenY + GamePanel.tileSize * 3, GamePanel.tileSize * 4, GamePanel.tileSize, null);
                    for (int i = 0; i < len; i++) {
                        g.drawImage(images[2], screenX, screenY + GamePanel.tileSize * (8 + i), GamePanel.tileSize * 4, GamePanel.tileSize, null);
                    }
                    g.drawImage(images[0], screenX, screenY + GamePanel.tileSize * (8 + (len)), GamePanel.tileSize * 4, GamePanel.tileSize * 2, null);

                }
            } else {
                g.drawImage(images[1], screenX, screenY, GamePanel.tileSize * 4, GamePanel.tileSize * 2, null);
                if (isBreak) {
                    for (int i = 0; i < len; i++) {
                        g.drawImage(images[2], screenX, screenY + GamePanel.tileSize * (4 + i), GamePanel.tileSize * 4, GamePanel.tileSize, null);
                    }
                    g.drawImage(images[3], screenX, screenY + GamePanel.tileSize * (4 + len), GamePanel.tileSize * 4, (int)(GamePanel.tileSize * 2.5), null);
                    g.drawImage(images[0], screenX, screenY + GamePanel.tileSize * (9 + len), GamePanel.tileSize * 4, GamePanel.tileSize * 2, null);

                } else {
                    for (int i = 0; i < len + 2; i++) {
                        g.drawImage(images[2], screenX, screenY + GamePanel.tileSize * (4 + i), GamePanel.tileSize * 4, GamePanel.tileSize, null);
                    }
                    g.drawImage(images[0], screenX, screenY + GamePanel.tileSize * (8 + len), GamePanel.tileSize * 4, GamePanel.tileSize * 2, null);

                }
            }


        } // end if

    } // end draw

}

