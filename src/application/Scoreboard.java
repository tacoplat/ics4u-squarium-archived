package application;

public class Scoreboard {

    //Instance fields
    String playerName;
    String mode;
    int score;

    public Scoreboard(String nameIn, String modeIn, Integer scoreIn) {
        playerName = nameIn;
        mode = modeIn;
        score = scoreIn;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMode() {
        return mode;
    }

    public int getScore() {
        return score;
    }

    public void getScores(String path) {

    }


}
