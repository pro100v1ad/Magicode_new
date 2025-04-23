package main.java.com.magicode.ui.gamestate;

import main.java.com.magicode.core.GamePanel;

import java.awt.*;

public class StartMenu {

    private GamePanel gp;
    private Button[] button;

    public StartMenu(GamePanel gp) {
        this.gp = gp;

        button = new Button[3];
        button[0] = new Button(100, 100, "Продолжить", 128, new Color(88, 88, 88), false);
    }

    public void update() {

        if(button[0].update()) {
            gp.state = GamePanel.GameState.Game;
        }

    }

    public void draw(Graphics2D g) {

        g.setColor(new Color(0xFF1C1C1C, true));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        button[0].draw(g);
    }

}
