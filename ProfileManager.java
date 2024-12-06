import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

/**
 * Manages player profiles for Boulder Dash.
 * Handles profile creation, saving, loading, deletion, and ID management.
 */
public class ProfileManager {

    /**
     * Prompts the user to create a new player profile via a dialog window.
     * @return The created PlayerProfile object.
     */
    public static PlayerProfile promptForProfile() {
        Stage dialog = new Stage();
        dialog.setTitle("Create Profile");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label nameLabel = new Label("Enter Name:");
        TextField nameField = new TextField();

        PlayerProfile createdPlayerProfile = new PlayerProfile();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText("Name is Required");
                alert.setContentText("Please enter a name.");
                alert.showAndWait();
            } else {
                createdPlayerProfile.setName(name);
                saveProfileToFile(createdPlayerProfile);
                dialog.close();
            }
        });

        VBox dialogBox = new VBox(10, nameLabel, nameField, saveButton);
        dialogBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene dialogScene = new Scene(dialogBox, 300, 150);
        dialog.setScene(dialogScene);
        dialog.showAndWait();

        return createdPlayerProfile;
    }

    /**
     * Saves a player profile to a text file.
     * @param profile The PlayerProfile to save.
     */
    public static void saveProfileToFile(final PlayerProfile profile) {
        String outputFile = "txt/" + profile.getPlayerId() + ".txt";
        try {
            PrintWriter out = new PrintWriter(outputFile);
            out.println(profile.getPlayerId());
            out.println(profile.getName());
            out.print(profile.getMaxLevelReached());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a player profile from specified file.
     * @param fileName The name of the file to load.
     * @return The created PlayerProfile, or null if an error occurs.
     */
    public static PlayerProfile loadProfileFromFile(final String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int playerId = Integer.parseInt(reader.readLine());
            String playerName = reader.readLine();
            int maxLevelReached = Integer.parseInt(reader.readLine());
            return new PlayerProfile(playerId, playerName, maxLevelReached);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading profile from file: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all available player profiles from the text files.
     * @return An Arraylist of PlayerProfile objects.
     */
    public static ArrayList<PlayerProfile> getAvailableProfiles() {
        ArrayList<PlayerProfile> profiles = new ArrayList<>();
        File folder = new File("txt");

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.matches("\\d+\\.txt"));
            if (files != null) {
                for (File file : files) {
                    try (Scanner scanner = new Scanner(file)) {
                        int playerId = Integer.parseInt(scanner.nextLine());
                        String name = scanner.nextLine();
                        int maxLevelReached = Integer.parseInt(scanner.nextLine());
                        profiles.add(new PlayerProfile(playerId, name, maxLevelReached));
                    } catch (IOException | NumberFormatException e) {
                        System.err.println("Error reading profile file: " + file.getName());
                    }
                }
            }
        }
        return profiles;
    }

    /**
     * Deletes the player profile with the selected ID.
     * @param idToDelete The ID of the profile to delete.
     */
    public static void deleteProfile(final int idToDelete) {
        String folderPath = "txt";
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Folder does not exist or is not a directory: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.matches("\\d+\\.txt"));

        if (files == null || files.length == 0) {
            System.out.println("No matching files found in the folder.");
            return;
        }

        for (File file : files) {
            try (Scanner scanner = new Scanner(file)) {
                int playerId = Integer.parseInt(scanner.nextLine());
                if (playerId == idToDelete) {
                    try {
                        scanner.close();
                        java.nio.file.Files.delete(file.toPath());
                    } catch (IOException e) {
                        System.out.println("Failed to delete file: " + file.getName());
                    }
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error reading file: " + file.getName());
            }
        }
    }

    /**
     * Retrieves the next player ID to set, then increments 'NextPlayerID'.
     * @return The next player ID.
     */
    public static int getNextPlayerId() {
        int playerId = 0;
        String filePath = "txt/NextPlayerId.txt";
        File readFile = new File(filePath);

        //Read Next Player ID
        try {
            Scanner in = new Scanner(readFile);
            playerId = Integer.parseInt(in.nextLine());
        } catch (FileNotFoundException exception) {
            System.out.println("Error in finding file");
        }

        //Increment Next Player ID by 1
        try {
            PrintWriter out = new PrintWriter(filePath);
            out.print(playerId + 1);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot write file");
        }

        return playerId;
    }

    /**
     * Checks if a player ID currently has a save file.
     * @param id The player ID.
     * @return True if the save file exists, false if a save file cannot be found.
     */
    public static boolean doesPlayerSaveFileExist(final int id) {
        String folderPath = "txt";
        String fileName = "Save" + id + ".txt";
        File file = new File(folderPath, fileName);
        return file.exists() && file.isFile();
    }

}
