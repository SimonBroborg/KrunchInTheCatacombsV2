package main;

import state.GameStateManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 */
public class InputHandler extends KeyAdapter
{
    private GameStateManager gsm;

    public InputHandler(GameStateManager gsm) {
	this.gsm = gsm;
    }

    public void keyPressed(final KeyEvent e) {
	gsm.keyPressed(e.getKeyCode());
    }

    public void keyReleased(final KeyEvent e) {
        gsm.keyReleased(e.getKeyCode());
    }

}
