package main.java.com.magicode.core.utils;

public class TextureAtlas {

    public Texture textures[][];

    public TextureAtlas(int row, int col) {
        textures = new Texture[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                textures[i][j] = new Texture();
            }
        }

        textures[0][0].loadTexture("/resources/background/dirt/dirtCenter.png");

        textures[1][0].loadTexture("/resources/background/grass/grassCenter.png");


    }

}
