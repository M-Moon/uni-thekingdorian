package controllers;

public enum GameState 
{
    HOME_SCREEN,
    PLAY,
    PAUSED,
    CUTSCENE,
    DEATH,
    END;

    /**
     * Determines whether the game mode is at the end
     * @return
     */
    public boolean hasEnded() {
        return this.ordinal() > 3;
    }

    public boolean isPlaying() {
        return this!=HOME_SCREEN && !this.hasEnded();
    }
}

