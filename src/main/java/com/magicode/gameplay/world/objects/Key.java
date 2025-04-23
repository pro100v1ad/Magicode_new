package main.java.com.magicode.gameplay.world.objects;

import main.java.com.magicode.core.GamePanel;
import main.java.com.magicode.gameplay.world.GameObject;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Key extends GameObject {
    private GamePanel gp;
    public Key(GamePanel gp) {
        name = "Key";
        code = 1;
        this.gp = gp;
        image = gp.textureAtlas.textures[13][0].getTexture();
        w = 32;
        h = 32;
    }

}

