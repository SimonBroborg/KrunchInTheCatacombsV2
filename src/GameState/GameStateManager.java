package GameState;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of the current state and helps with switching between game states
 */
public class GameStateManager
{
    private List<GameState> gameStates;
    private int currentState;

    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;

    public GameStateManager() {
        gameStates = new ArrayList<>();
        currentState = LEVEL1STATE;
        gameStates.add(new MenuState(this));
        gameStates.add(new Level1State(this));

        for(GameState state: gameStates){
            state.init();
	}
    }

    public void setState(int state){
        currentState = state;
        //getCurrentState().init();
	System.out.println(currentState);
    }

    public void update(){
        getCurrentState().update();
    }

    public void draw(Graphics2D g2d){
   	 getCurrentState().draw(g2d);
    }

    public void keyPressed(int k){
        getCurrentState().keyPressed(k);
    }

    public void keyReleased(int k){
        getCurrentState().keyReleased(k);
    }

    public void mouseClicked(MouseEvent e){
        getCurrentState().mouseClicked(e);
    }
    public void mouseMoved(MouseEvent e){
        getCurrentState().mouseMoved(e);
    }

    private GameState getCurrentState(){
        return gameStates.get(currentState);
    }
}
