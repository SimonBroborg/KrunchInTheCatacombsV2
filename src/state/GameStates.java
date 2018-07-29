package state;

/**
 *
 */
public enum GameStates {

    MENU_STATE(new MenuState()),
    LEVEL_1_STATE(new Level1State()),
    QUIT_STATE(new QuitState());

    public final GameState state;

    GameStates(GameState gs) {
        this.state = gs;
    }

    GameState getState() {
        return this.state;
    }
}
