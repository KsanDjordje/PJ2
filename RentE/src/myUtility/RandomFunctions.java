package myUtility;

import java.util.Random;
import javafx.scene.paint.Color;

/**
 * A utility class that provides random functions for generating strings, numbers,
 * boolean values, and colors.
 */
public class RandomFunctions {

    // Arrays for storing characters to be used in random string generation
    private static final String[] ForeignCharacters = {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };

    private static final String[] LocalCharacters = {
        "A", "B", "C", "Č", "Ć", "D", "Dž", "Đ", "E", "F", "G", "H", "I", "J", "K", "L", "Lj", "M", "N", "Nj", "O", "P", "R", "S", "Š", "T", "U", "V", "Z", "Ž",
        "a", "b", "c", "č", "ć", "d", "dž", "đ", "e", "f", "g", "h", "i", "j", "k", "l", "lj", "m", "n", "nj", "o", "p", "r", "s", "š", "t", "u", "v", "z", "ž",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };

    /**
     * Generates a random string using a specified alphabet and length.
     * 
     * @param alph The alphabet type, where "l" indicates LocalCharacters and anything else indicates ForeignCharacters.
     * @param length The length of the string to generate.
     * @return A randomly generated string of the specified length.
     */
    public String generateString(String alph, int length) {
        String[] alphabet = alph.equals("l") ? LocalCharacters : ForeignCharacters;
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        while (result.length() < length) {
            int index = random.nextInt(alphabet.length);
            result.append(alphabet[index]);
        }

        return result.toString();
    }

    /**
     * Generates a random number between the specified minimum and maximum values.
     * 
     * @param min The minimum bound.
     * @param max The maximum bound.
     * @return A random integer between min and max.
     */
    public int generateRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min)) + min;
    }

    /**
     * Generates a random boolean value based on a given probability of true.
     * 
     * @param probabilityTrue The probability (between 0 and 1) that the returned value will be true.
     * @return True with the specified probability, false otherwise.
     */
    public Boolean getRandomTrueFalse(double probabilityTrue) {
        Random rand = new Random();
        return rand.nextDouble() < probabilityTrue;
    }

    /**
     * Generates a color based on an alphanumeric thread identifier.
     * This method maps a thread identifier, generating colors that are visually distinct.
     * 
     * @param threadIdentifier The alphanumeric identifier of the thread (e.g., "A1", "B2").
     * @return A Color object derived from the thread identifier, or Color.GRAY if the identifier is null or empty.
     */
    public Color generateColorFromThreadIdentifier(String threadIdentifier) {
        if (threadIdentifier == null || threadIdentifier.isEmpty()) {
            return Color.GRAY; // Default color if the identifier is invalid
        }

        int uniqueNumber = 0;
        for (char ch : threadIdentifier.toCharArray()) {
            uniqueNumber += ch;
        }

        // Handle a number of unique colors (e.g., 25 here)
        int numberOfColors = 25;
        double hue = (uniqueNumber % numberOfColors) / (double) numberOfColors;

        return Color.hsb(hue * 360, 0.8, 0.8); // High saturation and brightness for vibrant colors
    }
}
