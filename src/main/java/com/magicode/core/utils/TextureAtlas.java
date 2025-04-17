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

        textures[0][0].loadTexture("/resources/background/parts1/wallDown.png");
        textures[0][1].loadTexture("/resources/background/parts1/wallUP.png");
        textures[0][2].loadTexture("/resources/background/parts1/wallLeft.png");
        textures[0][3].loadTexture("/resources/background/parts1/wallRight.png");
        textures[0][4].loadTexture("/resources/background/parts1/wallDownLeft.png");
        textures[0][5].loadTexture("/resources/background/parts1/wallDownRight.png");
        textures[0][6].loadTexture("/resources/background/parts1/wallUpLeft.png");
        textures[0][7].loadTexture("/resources/background/parts1/wallUpRight.png");
        textures[0][8].loadTexture("/resources/background/parts1/floor.png");

        textures[1][0].loadTexture("/resources/background/grass/grassCenter.png");

        textures[2][0].loadTexture("/resources/background/water/waterCenter.png");

        textures[10][0].loadTexture("/resources/structures/door/doorUp.png");
        textures[10][1].loadTexture("/resources/structures/door/doorDown.png");
        textures[10][2].loadTexture("/resources/structures/door/doorRight.png");
        textures[10][3].loadTexture("/resources/structures/door/doorLeft.png");


    }

}
