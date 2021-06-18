package application;

/**
 * Represents a user profile.
 */
public class Profile {

    // Instance fields
    private String playerName;
    private Integer id;

    /**
     * Constructor for Profiles.
     * @param playerNameIn - The player's name.
     * @param idIn - The numeric id of the profile.
     */
    public Profile(String playerNameIn, Integer idIn) {
        playerName = playerNameIn;
        id = idIn;
    }

    /**
     * Set the player name of this profile.
     * @param playerNameIn - The new player name.
     */
    public void setPlayerName(String playerNameIn) {
        playerName = playerNameIn;
    }

    /**
     * Returns the value of this profile's player name.
     * @return playerName - The player name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the value of this profile's id.
     * @return id = The player's id.
     */
    public Integer getId() {
        return id;
    }

}
