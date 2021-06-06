package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.Optional;

public class ScoreboardController extends Controller {

    // Instance fields
    private ObservableList<Scoreboard> scores =
            FXCollections.observableArrayList();
    private static String defaultScoreboardPath = "src/application/appdata/";
    private final boolean debug = false;

    // FXML Id references
    @FXML
    private TableView<Scoreboard> scoreboardTable;

    @FXML private TableColumn<Scoreboard, String> col0;
    @FXML private TableColumn<Scoreboard, String> col1;
    @FXML private TableColumn<Scoreboard, Integer> col2;

    /**
     * The default JavaFX initialize method.
     */
    public void initialize() {

        // Configure the table columns to hold the scoreboard data and styles.
        col0.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        col0.setReorderable(false);
        col1.setCellValueFactory(new PropertyValueFactory<>("mode"));
        col1.setReorderable(false);
        col2.setCellValueFactory(new PropertyValueFactory<>("score"));
        col2.setReorderable(false);

        scoreboardTable.setItems(scores);
        scoreboardTable.setFixedCellSize(24);

        // Update the scoreboard from the save file & sort entries descending by score.
        obtainScoresFromFile(defaultScoreboardPath + "/score.save");
        sortScoreboard(scores);
    }

    /**
     * Handles the reset button press.
     */
    @FXML
    private void resetScoreboard() {

        // Set up a confirmation dialog.
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Erase Scores");
        dialog.setContentText("Are you sure you want to erase the scoreboard?\nThis action cannot be reverted.");

        dialog.setHeaderText(null);
        dialog.setGraphic(null);

        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Erase"); // Relabel the ok button.

        Optional<ButtonType> result = dialog.showAndWait();

        // Button actions.
        if (result.isPresent() && result.get() == ButtonType.OK) {
            eraseScores(defaultScoreboardPath + "/score.save"); // Erase the scoreboard.
            System.out.println("Scoreboard erased.");
        } else {
            dialog.close();
            System.out.println("User cancelled action.");
        }
    }

    /**
     * Sets the default score file path.
     * @param pathIn - The specified file path.
     */
    public static void setDefaultScoreboardPath(String pathIn) {
        defaultScoreboardPath = pathIn;
        ProfileController.setDefaultProfilePath(pathIn);
    }

    /**
     * Gets the default score file path.
     * @return The path to the save file.
     */
    public static String getDefaultScoreboardPath() {
        return defaultScoreboardPath;
    }

    /**
     * Erases saved data from the score tracking file. Resets the table as well.
     * @param path - The path of the file as a String.
     */
    private void eraseScores(String path) {
        try {
            // Write a blank file at the path specified.
            FileWriter fw = new FileWriter(path, false);
            fw.close();
        } catch (IOException e) {
            System.out.print("Could not erase file.");
            e.printStackTrace();
        }
        scores.clear(); // Clear the table.
    }

    /**
     * Writes scoreboard data to the scores file.
     *
     * @param playerNameIn - The player's selected name.
     * @param modeIn - The game mode.
     * @param scoreIn - The player's score.
     */
    public static void writeScoreToFile(String playerNameIn, String modeIn, Integer scoreIn) {
        try {
            // Append the information (separated by delimiters) to the file.
            FileWriter fw = new FileWriter(defaultScoreboardPath + "/score.save", true);
            String entry = "\n" + playerNameIn + ">%" + modeIn + ">%" + scoreIn;

            fw.write(entry);
            fw.close();

        } catch (IOException e) {
            System.out.println("Could not write to the specified file");
            e.printStackTrace();
        }
    }

    /**
     * Updates the table with saved score entries.
     * @param path - The path of the scores file.
     */
    private void obtainScoresFromFile(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);

            String str;

            // Read through the lines of the scores file.
            while ((str = br.readLine()) != null) {
                if (debug) System.out.println(str); // Print line to console.

                // Split the line by the delimiter, store in a String array.
                String[] extract = str.split(">%");

                // Add the data to the TableView list to display.
                Scoreboard temp = new Scoreboard(extract[0], extract[1], Integer.valueOf(extract[2]));
                scores.add(temp);
            }

            fr.close();

        } catch (FileNotFoundException e) {
            System.out.println("Cannot locate scoreboard file.");
            newScoreFile(path);
        } catch (IOException e) {
            System.out.println("Could not read from the file.");
            e.printStackTrace();
        }
    }

    /**
     * Shows a dialog that prompts the user to create a new score file.
     * @param path - The path of the file.
     */
    private void newScoreFile(String path) {

        // Set up a confirmation dialog.
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Missing Score File");
        dialog.setContentText("Could not locate the score file. Create a new one?");

        dialog.setHeaderText(null);
        dialog.setGraphic(null);

        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes"); // Relabel the ok button.
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No"); // Relabel the cancel button.

        Optional<ButtonType> result = dialog.showAndWait(); // Show dialog.

        // Button actions.
        if (result.isPresent() && result.get() == ButtonType.OK) {
            File newScoreFile = new File(path);
            try {
                newScoreFile.createNewFile(); // Attempt to create the new file.
            } catch (IOException e) {
                System.out.println("File already exists.");
                e.printStackTrace();
            }
            System.out.println("Score file created.");
        } else {
            dialog.close();
            System.out.println("User cancelled action.");
        }
    }

    /**
     * Use Selection Sort to sort the scoreboard entries by descending
     * score.
     * @param listIn - The data list to be sorted (holds Scoreboard objects).
     */
    private void sortScoreboard(ObservableList<Scoreboard> listIn) {

        // Iterate through the list.
        for (int i = 0; i < listIn.size()-1; i++) {
            int max = i;

            for (int j = i+1; j < listIn.size(); j++) {
                // Compare the current element to the max element. If greater, reevaluate max value.
                if (listIn.get(j).getScore() > listIn.get(max).getScore()) {
                    max = j;
                }
            }

            // Swap necessary elements.
            Scoreboard temp = listIn.get(max);
            listIn.set(max, listIn.get(i));
            listIn.set(i, temp);
        }

    }

}
