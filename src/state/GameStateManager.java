package state;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Keeps track of the current state and helps with switching between game states
 */
public class GameStateManager {
    private GameStates prevState = null;
    private GameStates currentState = null;

    public GameStateManager() {
        // Sets the GSM for every state
        for (GameStates state : GameStates.values()) {
            state.getState().init(this);
        }
        setState(GameStates.MENU_STATE);
    }

    public void setState(GameStates state) {
        prevState = currentState;
        currentState = state;
    }

    public void update(Point mousePos) {
        currentState.getState().update(mousePos);
    }

    public void draw(Graphics2D g2d) {
        currentState.getState().draw(g2d);
    }

    public void keyPressed(int k) {
        currentState.getState().keyPressed(k);
    }

    public void keyReleased(int k) {
        currentState.getState().keyReleased(k);
    }

    public void mouseClicked(MouseEvent e) {
        currentState.getState().mouseClicked(e);
    }

    public void mouseMoved(MouseEvent e) {
        currentState.getState().mouseMoved(e);
    }

    public GameStates getPrevState() {
        return prevState;
    }
}
