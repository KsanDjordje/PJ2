package myUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for file deletion operations.
 */
public class FileDeleter {

    /**
     * Deletes a file at the specified path if it exists.
     *
     * @param filePath the path of the file to be deleted
     */
    public static void deleteFileIfExists(String filePath) {
        Path path = Paths.get(filePath);
        try {
            if (Files.exists(path)) {
                Files.delete(path);
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("File does not exist.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting the file: " + e.getMessage());
        }
    }
}
