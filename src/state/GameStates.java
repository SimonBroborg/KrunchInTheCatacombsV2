package state;

/**
 *
 */
public enum GameStates {

    MENU_STATE(new MenuState()),
    LEVEL_1_STATE(new Level1State("resources/Maps/level1.txt")),
    LEVEL_2_STATE(new Level2State("resources/Maps/level2.txt")),
    QUIT_STATE(new QuitState());

    public final GameState state;

    GameStates(GameState gs) {
        this.state = gs;
    }

    GameState getState() {
        return this.state;
    }
}
