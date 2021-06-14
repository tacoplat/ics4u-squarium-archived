package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class ProfileController extends Controller {

    // Instance fields
    ObservableList<Profile> profileList = FXCollections.observableArrayList();
    private final boolean debug = false;

    // FXML Id elements
    @FXML
    private TableView<Profile> profileTable;

    @FXML private TableColumn<Profile, String> col0;
    @FXML private TableColumn<Profile, Integer> col1;

    /**
     * The default JavaFX initialize method.
     */
    public void initialize() {

        // Configure the table columns to hold the scoreboard data and styles.
        col0.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        col0.setReorderable(false);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        col1.setReorderable(false);

        profileList.add(new Profile("Create New Profile", 0));

        profileTable.setItems(profileList);
        profileTable.setFixedCellSize(24);

        String profileSave = Controller.getDefaultPath() + "/profile.save";

        try {
            // Generate directory if it does not exist
            Files.createDirectories(Paths.get(Controller.getDefaultPath()));
            Files.createFile(Paths.get(profileSave));
        } catch (IOException ex) {
            System.out.printf("File creation error: " + ex.getMessage());
        }


        // Update list and sort by ascending ID.
        obtainProfilesFromFile(Controller.getDefaultPath() + "/profile.save");
        sortProfilesById(profileList);

        // Try to set the default selected profile to the current one.
        try {
            int index = 1;
            for (int i = 0; i < profileList.size(); i++) {
                if (profileList.get(i).getId().equals(Controller.getCurrentProfileId())) {
                    index = i;
                }
            }
            profileTable.getSelectionModel().select(index); // Set the default selected row.
        } catch (NullPointerException e) {
            // If there is no current profile, set row 2 as default.
            profileTable.getSelectionModel().select(1); // Set the default selected row.
        }
        if (profileList.size() == 1) profileTable.getSelectionModel().select(0); // Set the default selected row if there are no pre-existing profiles.

    }

    /**
     * Updates the ListView with saved profiles.
     * @param path - The path of the profiles file.
     */
    private void obtainProfilesFromFile(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);

            String str;

            // Read through the lines of the profiles file.
            while ((str = br.readLine()) != null) {
                if (debug) System.out.println(str); // Print line to console.

                // Split the line by the delimiter, store in a String array.
                String[] extract = str.split(">%");

                // Add the data to the ListView list to display.
                Profile temp = new Profile(extract[0], Integer.valueOf(extract[1]));
                profileList.add(temp);
            }

            fr.close();

        } catch (FileNotFoundException e) {
            System.out.println("Cannot locate profile file.");
            newProfileFile(path);
        } catch (IOException e) {
            System.out.println("Could not read from the file.");
            e.printStackTrace();
        }
    }

    /**
     * Updates the profile list data to the profile file.
     *
     */
    public void updateProfileFile() {
        try {
            FileWriter fw = new FileWriter(Controller.getDefaultPath() + "/profile.save");

            // Blank string to hold written content.
            StringBuilder entry = new StringBuilder();

            profileList.remove(0);

            // Format the entry String for the program.
            for (Profile profile : profileList) {
                entry.append(profile.getPlayerName()).append(">%").append(profile.getId()).append("\n");
            }

            profileList.add(0, new Profile("Create New Profile", 0));
            fw.write(entry.toString());
            fw.close();

        } catch (IOException e) {
            System.out.println("Could not write to the specified file");
            e.printStackTrace();
        }
    }

    /**
     * Use Selection Sort to sort the profile entries by ascending ID.
     *
     * @param listIn - The data list to be sorted (holds Profile objects).
     */
    private void sortProfilesById(ObservableList<Profile> listIn) {

        // Iterate through the list.
        for (int i = 0; i < listIn.size()-1; i++) {
            int min = i;

            for (int j = i+1; j < listIn.size(); j++) {
                // Compare the current element to the max element. If greater, reevaluate max value.
                if (listIn.get(j).getId() < listIn.get(min).getId()) {
                    min = j;
                }
            }

            // Swap necessary elements.
            Profile temp = listIn.get(min);
            listIn.set(min, listIn.get(i));
            listIn.set(i, temp);
        }
    }

    /**
     * Create a new profile.
     * @param playerNameIn - The profile's player name.
     */
    private void newProfile(String playerNameIn) {
        // Get the next available ID.
        int nextAvailableId = profileList.get(profileList.size()-1).getId() + 1;

        // Create a new profile.
        Profile temp = new Profile(playerNameIn, nextAvailableId);

        // Add it to the list.
        profileList.add(temp);

        updateProfileFile();
    }

    /**
     * Show a profile creation dialog.
     */
    private void profileCreationDialog() {
        // Configure a text input dialog.
        TextInputDialog dialog = new TextInputDialog("Profile Name");
        dialog.setTitle("Profile Creation");
        dialog.setContentText("Please enter a name for your new profile: ");

        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        configurePopupIcons(dialog);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::newProfile);
    }

    /**
     * Show a profile rename dialog.
     */
    @FXML
    private void renameProfile() {
        // Get the selected profile.
        Profile selection = profileTable.getSelectionModel().getSelectedItem();

        // Configure a text input dialog.
        TextInputDialog dialog = new TextInputDialog(selection.getPlayerName());
        dialog.setTitle("Profile Rename");
        dialog.setContentText("Please enter a new name for your profile: ");

        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        configurePopupIcons(dialog);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            // Get the index at which the selected profile has the same ID as the current list element.
            int index = 1;
            for (int i = 0; i < profileList.size(); i++) {
                if (profileList.get(i).getId().equals(selection.getId())) {
                    index = i;
                }
            }
            profileList.get(index).setPlayerName(result.get()); // Update the profile's name.

            // Trigger a table refresh.
            profileTable.getColumns().get(0).setVisible(false);
            profileTable.getColumns().get(0).setVisible(true);

            updateProfileFile(); // Update the profile.save file.
        }
    }

    /**
     * Shows a dialog that prompts the user to create a new profile file.
     * @param path - The path of the file.
     */
    private void newProfileFile(String path) {

        // Set up a confirmation dialog.
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Missing Profile File");
        dialog.setContentText("Could not locate the profile file. Create a new one?");

        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        configurePopupIcons(dialog);

        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes"); // Relabel the ok button.
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No"); // Relabel the cancel button.

        Optional<ButtonType> result = dialog.showAndWait(); // Show dialog.

        // Button actions.
        if (result.isPresent() && result.get() == ButtonType.OK) {
            File newProfileFile = new File(path);
            try {
                newProfileFile.createNewFile(); // Attempt to create the new file.
            } catch (IOException e) {
                System.out.println("File already exists.");
                e.printStackTrace();
            }
            System.out.println("Profile file created.");
        } else {
            dialog.close();
            System.out.println("User cancelled action.");
        }
    }

    /**
     * Process the user's profile selection.
     * @param event JavaFX ActionEvent
     */
    @FXML
    private void processSelection(ActionEvent event) {
        try {
            Profile selection = profileTable.getSelectionModel().getSelectedItem();

            // If the user selects "Create New Profile", create a profile.
            if (selection.getId() == 0) {
                profileCreationDialog();
            } else {
                // Otherwise, select the profile and proceed.
                Controller.setProfile(selection);
                changeScreen("fxml-layouts/main-screen.fxml/", event);
            }
        } catch (NullPointerException e) {

            // Configure and show a dialog to prompt the user to select a valid choice.
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);

            dialog.setTitle("Selection Error");
            dialog.setContentText("Please select an option.");

            dialog.setGraphic(null);
            dialog.setHeaderText(null);

            configurePopupIcons(dialog);

            dialog.show();
        }
    }

    /**
     * Deletes the selected profile.
     */
    @FXML
    private void deleteProfile() {
        // Get the selected profile and its ID.
        Profile selection = profileTable.getSelectionModel().getSelectedItem();
        int idToDelete = selection.getId();

        // Check if the selected profile is not "Create New Profile".
        if (idToDelete != 0) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);

            dialog.setTitle("Delete Profile");
            dialog.setContentText("Are you sure you want to delete this profile?\nThis action cannot be reverted.");

            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            configurePopupIcons(dialog);

            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Delete"); // Relabel the ok button.

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int index = 1;

                // Iterate through the list and find the element with the specified ID.
                for (int i = 0; i < profileList.size(); i++) {
                    if (profileList.get(i).getId() == idToDelete) {
                        index = i;
                    }
                }

                // Remove from list and update file.
                profileList.remove(index);
                updateProfileFile();
            }
        }
    }



}
