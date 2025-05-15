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
        textures[0][1].loadTexture("/resources/background/parts1/wallUp.png");
        textures[0][2].loadTexture("/resources/background/parts1/wallLeft.png");
        textures[0][3].loadTexture("/resources/background/parts1/wallRight.png");
        textures[0][4].loadTexture("/resources/background/parts1/wallDownLeft.png");
        textures[0][5].loadTexture("/resources/background/parts1/wallDownRight.png");
        textures[0][6].loadTexture("/resources/background/parts1/wallUpLeft.png");
        textures[0][7].loadTexture("/resources/background/parts1/wallUpRight.png");
        textures[0][8].loadTexture("/resources/background/parts1/floor.png");

        textures[1][0].loadTexture("/resources/background/parts2/grass/grass1.png");
        textures[1][1].loadTexture("/resources/background/parts2/grass/grassTileDownLeft.png");
        textures[1][2].loadTexture("/resources/background/parts2/grass/grassTileDownRight.png");
        textures[1][3].loadTexture("/resources/background/parts2/grass/grassTileUpLeft.png");
        textures[1][4].loadTexture("/resources/background/parts2/grass/grassTileUpRight.png");
        textures[1][5].loadTexture("/resources/background/parts2/grass/grassTop.png");
        textures[1][6].loadTexture("/resources/background/parts2/grass/grassDown.png");
        textures[1][7].loadTexture("/resources/background/parts2/grass/grassRight.png");
        textures[1][8].loadTexture("/resources/background/parts2/grass/grassLeft.png");


        textures[2][0].loadTexture("/resources/background/parts2/water/waterTile.png");
        textures[2][1].loadTexture("/resources/background/parts2/water/waterUp.png");
        textures[2][2].loadTexture("/resources/background/parts2/water/waterDown.png");
        textures[2][3].loadTexture("/resources/background/parts2/water/waterLeft.png");
        textures[2][4].loadTexture("/resources/background/parts2/water/waterRight.png");
        textures[2][5].loadTexture("/resources/background/parts2/water/waterUpLeft.png");
        textures[2][6].loadTexture("/resources/background/parts2/water/waterUpRight.png");
        textures[2][7].loadTexture("/resources/background/parts2/water/waterDownLeft.png");
        textures[2][8].loadTexture("/resources/background/parts2/water/waterDownRight.png");
        textures[2][9].loadTexture("/resources/background/parts2/water/waterLeftUp.png");
        textures[2][10].loadTexture("/resources/background/parts2/water/waterLeftDown.png");
        textures[2][11].loadTexture("/resources/background/parts2/water/waterRightUp.png");
        textures[2][12].loadTexture("/resources/background/parts2/water/waterRightDown.png");

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

        textures[18][0].loadTexture("/resources/structures/decoration/bush.png");
        textures[18][1].loadTexture("/resources/structures/decoration/stone.png");
        textures[18][2].loadTexture("/resources/structures/decoration/tree.png");

        textures[19][0].loadTexture("/resources/structures/bridge/horizontal/bridgeStart.png");
        textures[19][1].loadTexture("/resources/structures/bridge/horizontal/bridgeEnd.png");
        textures[19][2].loadTexture("/resources/structures/bridge/horizontal/bridgePart.png");
        textures[19][3].loadTexture("/resources/structures/bridge/horizontal/bridgeBreak.png");
        textures[19][4].loadTexture("/resources/structures/bridge/vertical/bridgeBreak.png");
        textures[19][5].loadTexture("/resources/structures/bridge/vertical/bridgeEnd.png");
        textures[19][6].loadTexture("/resources/structures/bridge/vertical/bridgePart.png");
        textures[19][7].loadTexture("/resources/structures/bridge/vertical/bridgeStart.png");

        textures[20][0].loadTexture("/resources/background/parts1/wallPR.png");
        textures[20][1].loadTexture("/resources/background/parts1/wallPL.png");
        textures[20][2].loadTexture("/resources/background/parts1/wallPDR.png");
        textures[20][3].loadTexture("/resources/background/parts1/wallPDL.png");
        textures[20][4].loadTexture("/resources/background/parts1/wallAngleRight.png");
        textures[20][5].loadTexture("/resources/background/parts1/wallAngleLeft.png");
        textures[20][6].loadTexture("/resources/background/parts1/wallAngleUR.png");
        textures[20][7].loadTexture("/resources/background/parts1/wallAngleUL.png");

        textures[20][8].loadTexture("/resources/background/parts3/floor/floor.png");
        textures[20][9].loadTexture("/resources/background/parts3/bossFloor/bossFloorCenter.png");
        textures[20][10].loadTexture("/resources/background/parts3/bossFloor/bossFloorDown.png");
        textures[20][11].loadTexture("/resources/background/parts3/bossFloor/bossFloorUp.png");
        textures[20][12].loadTexture("/resources/background/parts3/bossFloor/bossFloorLeft.png");
        textures[20][13].loadTexture("/resources/background/parts3/bossFloor/bossFloorRight.png");
        textures[20][14].loadTexture("/resources/background/parts3/bossFloor/bossFloorLeftDown.png");
        textures[20][15].loadTexture("/resources/background/parts3/bossFloor/bossFloorLeftUp.png");
        textures[20][16].loadTexture("/resources/background/parts3/bossFloor/bossFloorRightDown.png");
        textures[20][17].loadTexture("/resources/background/parts3/bossFloor/bossFloorRightUp.png");
        textures[20][18].loadTexture("/resources/background/parts3/bossRedLine/bossRedLineDown.png");
        textures[20][19].loadTexture("/resources/background/parts3/bossRedLine/bossRedLineUp.png");
        textures[20][20].loadTexture("/resources/background/parts3/bossRedLine/bossRedLineLeft.png");
        textures[20][21].loadTexture("/resources/background/parts3/bossRedLine/bossRedLineRight.png");
        textures[20][22].loadTexture("/resources/background/parts3/bossRedLine/bossRedLineLeftDown.png");
        textures[20][23].loadTexture("/resources/background/parts3/bossRedLine/bossRedLineLeftUp.png");
        textures[20][24].loadTexture("/resources/background/parts3/bossRedLine/bossRedLineRightDown.png");
        textures[20][25].loadTexture("/resources/background/parts3/bossRedLine/bossRedLineRightUp.png");
        textures[20][26].loadTexture("/resources/background/parts3/walls/wallsDown.png");
        textures[20][27].loadTexture("/resources/background/parts3/walls/wallsUp.png");
        textures[20][28].loadTexture("/resources/background/parts3/walls/wallsLeft.png");
        textures[20][29].loadTexture("/resources/background/parts3/walls/wallsRight.png");
        textures[20][30].loadTexture("/resources/background/parts3/walls/wallsLeftDown.png");
        textures[20][31].loadTexture("/resources/background/parts3/walls/wallsLeftUp.png");
        textures[20][32].loadTexture("/resources/background/parts3/walls/wallsRightDown.png");
        textures[20][33].loadTexture("/resources/background/parts3/walls/wallsRightUp.png");
        textures[20][34].loadTexture("/resources/background/parts3/walls/wallsDownLeft.png");
        textures[20][35].loadTexture("/resources/background/parts3/walls/wallsDownRight.png");
        textures[20][36].loadTexture("/resources/background/parts3/walls/wallsUpLeft.png");
        textures[20][37].loadTexture("/resources/background/parts3/walls/wallsUpRight.png");
        textures[20][38].loadTexture("/resources/background/parts3/floor/bossSpawn.png");

        textures[21][0].loadTexture("/resources/structures/portal/portalHor.png");
        textures[21][1].loadTexture("/resources/structures/portal/portalVert.png");







    }

}
