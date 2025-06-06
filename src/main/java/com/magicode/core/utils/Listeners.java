package main.java.com.magicode.core.utils;


import main.java.com.magicode.core.GamePanel;

import java.awt.event.*;

/*
Класс для считывания нажатий мышки и клавиатуры
 */
public class Listeners implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {
    GamePanel gp;

    private StringBuilder typedChars = new StringBuilder();
    private boolean shouldCaptureInput = false;


    public Listeners(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            gp.click();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Определяем какая кнопка была нажата
        if (e.getButton() == MouseEvent.BUTTON1) { // Левая кнопка
            GamePanel.mouseButtons[0] = true;
        } else if (e.getButton() == MouseEvent.BUTTON2) { // Средняя кнопка
            GamePanel.mouseButtons[1] = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) { // Правая кнопка
            GamePanel.mouseButtons[2] = true;
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Определяем какая кнопка была отпущена
        if (e.getButton() == MouseEvent.BUTTON1) {
            GamePanel.mouseButtons[0] = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            GamePanel.mouseButtons[1] = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            GamePanel.mouseButtons[2] = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        GamePanel.mouseX = e.getX();
        GamePanel.mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GamePanel.mouseX = e.getX();
        GamePanel.mouseY = e.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (shouldCaptureInput) {
            char c = e.getKeyChar();
            if (c != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(c)) {
                typedChars.append(c);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if(key == KeyEvent.VK_D) {
            GamePanel.keys[3] = true;
        }
        if(key == KeyEvent.VK_A) {
            GamePanel.keys[1] = true;
        }
        if(key == KeyEvent.VK_W) {
            GamePanel.keys[0] = true;
        }
        if(key == KeyEvent.VK_S) {
            GamePanel.keys[2] = true;
        }
        if(key == KeyEvent.VK_SHIFT) {
            GamePanel.keys[4] = true;
        }
        if(key == KeyEvent.VK_F) {
            GamePanel.keys[5] = true;
        }
        if(key == KeyEvent.VK_ESCAPE) {
            if(GamePanel.keyCooldown == 0) {
                GamePanel.keys[6] = true;
            }
        }
        if(key == KeyEvent.VK_E) {
            if(GamePanel.keyCooldown == 0) {
                GamePanel.keys[7] = true;
            }
        }
        if(key == KeyEvent.VK_U) {
            if(GamePanel.keyCooldown == 0) {
                GamePanel.keys[8] = true;
            }
        }
        if(key == KeyEvent.VK_K) {
            GamePanel.keys[9] = true;
        }
        if(key == KeyEvent.VK_R) {
            GamePanel.keys[10] = true;
        }
        if(key == KeyEvent.VK_SPACE) {
            GamePanel.keys[11] = true;
        }


        if (shouldCaptureInput) {
            if (key == KeyEvent.VK_BACK_SPACE) {
                typedChars.append('\b');
            }
            else if (key == KeyEvent.VK_ENTER) {
                typedChars.append('\n');
            }
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();
        if(key == KeyEvent.VK_D) {
            GamePanel.keys[3] = false;
        }
        if(key == KeyEvent.VK_A) {
            GamePanel.keys[1] = false;
        }
        if(key == KeyEvent.VK_W) {
            GamePanel.keys[0] = false;
        }
        if(key == KeyEvent.VK_S) {
            GamePanel.keys[2] = false;
        }
        if(key == KeyEvent.VK_SHIFT) {
            GamePanel.keys[4] = false;
        }
        if(key == KeyEvent.VK_F) {
            GamePanel.keys[5] = false;
        }
        if(key == KeyEvent.VK_ESCAPE) {
            GamePanel.keys[6] = false;
        }
        if(key == KeyEvent.VK_E) {
            GamePanel.keys[7] = false;
        }
        if(key == KeyEvent.VK_U) {
            GamePanel.keys[8] = false;
        }
        if(key == KeyEvent.VK_K) {
            GamePanel.keys[9] = false;
        }
        if(key == KeyEvent.VK_R) {
            GamePanel.keys[10] = false;
        }
        if(key == KeyEvent.VK_SPACE) {
            GamePanel.keys[11] = false;
        }


    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (gp.state == GamePanel.GameState.GameOpenTablet) {
            gp.tablet.handleMouseWheel(e.getWheelRotation());
        }
    }

    public char[] getTypedChars() {
        char[] chars = typedChars.toString().toCharArray();
        typedChars.setLength(0);
        return chars;
    }

    public void setShouldCaptureInput(boolean shouldCapture) {
        this.shouldCaptureInput = shouldCapture;
        if (!shouldCapture) {
            typedChars.setLength(0); // Очищаем буфер при отключении
        }
    }
}