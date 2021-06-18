package application;

/**
 * Represents a scoreboard listing.
 */
public class Scoreboard {

    //Instance fields
    String playerName;
    String mode;
    int score;

    /**
     * Constructor for Scoreboard objects.
     * @param nameIn - The player's name.
     * @param modeIn - The mode of the game.
     * @param scoreIn - The player's score.
     */
    public Scoreboard(String nameIn, String modeIn, Integer scoreIn) {
        playerName = nameIn;
        mode = modeIn;
        score = scoreIn;
    }

    /**
     * Returns the player name.
     * @return playerName - The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the selected game mode.
     * @return mode - The game mode.
     */
    public String getMode() {
        return mode;
    }

    /**
     * Returns the recorded score.
     * @return score - The game's score.
     */
    public int getScore() {
        return score;
    }

}
