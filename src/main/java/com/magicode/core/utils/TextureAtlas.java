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
        textures[2][1].loadTexture("/resources/background/water/waterUp.png");
        textures[2][2].loadTexture("/resources/background/water/waterDown.png");
        textures[2][3].loadTexture("/resources/background/water/waterLeft.png");
        textures[2][4].loadTexture("/resources/background/water/waterRight.png");
        textures[2][5].loadTexture("/resources/background/water/waterUpLeft.png");
        textures[2][6].loadTexture("/resources/background/water/waterUpRight.png");
        textures[2][7].loadTexture("/resources/background/water/waterDownLeft.png");
        textures[2][8].loadTexture("/resources/background/water/waterDownRight.png");
        textures[2][9].loadTexture("/resources/background/water/waterLeftUp.png");
        textures[2][10].loadTexture("/resources/background/water/waterLeftDown.png");
        textures[2][11].loadTexture("/resources/background/water/waterRightUp.png");
        textures[2][12].loadTexture("/resources/background/water/waterRightDown.png");

        textures[10][0].loadTexture("/resources/structures/door/doorUp.png");
        textures[10][1].loadTexture("/resources/structures/door/doorDown.png");
        textures[10][2].loadTexture("/resources/structures/door/doorRight.png");
        textures[10][3].loadTexture("/resources/structures/door/doorLeft.png");
        textures[10][4].loadTexture("/resources/structures/door/doorOpenUp.png");
        textures[10][5].loadTexture("/resources/structures/door/doorOpenDown.png");
        textures[10][6].loadTexture("/resources/structures/door/doorOpenRight.png");
        textures[10][7].loadTexture("/resources/structures/door/doorOpenLeft.png");
        textures[10][8].loadTexture("/resources/structures/door/doorLockUp.png");
        textures[10][9].loadTexture("/resources/structures/door/doorLockDown.png");
        textures[10][10].loadTexture("/resources/structures/door/doorLockRight.png");
        textures[10][11].loadTexture("/resources/structures/door/doorLockLeft.png");

        textures[11][0].loadTexture("/resources/structures/hatch/hatch.png");

        textures[12][0].loadTexture("/resources/structures/chest/chestCloseUp.png");
        textures[12][1].loadTexture("/resources/structures/chest/chestCloseDown.png");
        textures[12][2].loadTexture("/resources/structures/chest/chestCloseRight.png");
        textures[12][3].loadTexture("/resources/structures/chest/chestCloseLeft.png");
        textures[12][4].loadTexture("/resources/structures/chest/chestOpenUp.png");
        textures[12][5].loadTexture("/resources/structures/chest/chestOpenDown.png");
        textures[12][6].loadTexture("/resources/structures/chest/chestOpenRight.png");
        textures[12][7].loadTexture("/resources/structures/chest/chestOpenLeft.png");
        textures[12][8].loadTexture("/resources/structures/chest/chestLockUp.png");
        textures[12][9].loadTexture("/resources/structures/chest/chestLockDown.png");
        textures[12][10].loadTexture("/resources/structures/chest/chestLockRight.png");
        textures[12][11].loadTexture("/resources/structures/chest/chestLockLeft.png");

        textures[13][0].loadTexture("/resources/objects/key.png");
        textures[13][1].loadTexture("/resources/objects/book.png");
        textures[13][2].loadTexture("/resources/objects/wrench.png");

        textures[14][0].loadTexture("/resources/gui/button/menuButtonPassive.png");
        textures[14][1].loadTexture("/resources/gui/button/menuButtonActive.png");
        textures[14][2].loadTexture("/resources/gui/button/buttonEmptyPassive.png");
        textures[14][3].loadTexture("/resources/gui/button/buttonEmptyActive.png");
        textures[14][4].loadTexture("/resources/gui/button/directory.png");
        textures[14][5].loadTexture("/resources/gui/button/tablet.png");

        textures[15][0].loadTexture("/resources/gui/tablet/tabletOpen.png");
        textures[15][1].loadTexture("/resources/gui/tablet/buttonExitPassive.png");
        textures[15][2].loadTexture("/resources/gui/tablet/buttonExitActive.png");
        textures[15][3].loadTexture("/resources/gui/gameName.png");

        textures[16][0].loadTexture("/resources/gui/directory/DirectoryOpen.png");
        textures[16][1].loadTexture("/resources/gui/directory/DirectoryArrowLeft.png");
        textures[16][2].loadTexture("/resources/gui/directory/DirectoryArrowRight.png");

        textures[17][0].loadTexture("/resources/gui/interface_/bar.png");



    }

}
