package SimulationPJ2;

import java.util.Random;

import javafx.scene.paint.Color;

public class RandomFunctions {
	private static final String[] ForeignCharacters = {
	        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
	        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
	        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
	    };
	private static final String[] LocalCharacters= {
			"A", "B", "C", "Č", "Ć", "D", "Dž", "Đ", "E", "F", "G", "H", "I", "J", "K", "L", "Lj", "M", "N", "Nj", "O", "P", "R", "S", "Š", "T", "U", "V", "Z", "Ž",
	        "a", "b", "c", "č", "ć", "d", "dž", "đ", "e", "f", "g", "h", "i", "j", "k", "l", "lj", "m", "n", "nj", "o", "p", "r", "s", "š", "t", "u", "v", "z", "ž",
	        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
	};
	public String generateString(String alph, int length) {
		String[] alphabet = alph == "l"? LocalCharacters : ForeignCharacters;
		Random random = new Random();
		int min = 0;
		int max = alphabet.length;
		String result = "";
		while(result.length() < length) {
			int num = random.nextInt((max - min) ) + min;
			result  = result.concat(alphabet[num]);
		}
		 return result;
	}
	public int generateRandomNumber(int min, int max) {
        Random rand = new Random();
        
        int randomNum = rand.nextInt((max - min) ) + min;
        return randomNum;
    }
	public Boolean getRandomTrueFalse(double probabilityTrue) {
        Random rand = new Random();
        double randomValue = rand.nextDouble(); // Generate a random double between 0.0 and 1.0
        
        if (randomValue < probabilityTrue) {
            return true;
        } else {
            return false;
        }
    }
	
    /**
     * Generates a color based on an alphanumeric thread identifier.
     * 
     * @param threadIdentifier The alphanumeric identifier of the thread (e.g., "A1", "B2").
     * @return A Color object derived from the string.
     */
    public Color generateColorFromThreadIdentifier(String threadIdentifier) {
        if (threadIdentifier == null || threadIdentifier.isEmpty()) {
            return Color.GRAY; // Default color if the identifier is invalid
        }

        // Convert the thread identifier into a unique number by combining the characters
        int uniqueNumber = 0;

        for (char ch : threadIdentifier.toCharArray()) {
            // Accumulate the character values into a unique number
            uniqueNumber += ch;
        }

        // Ensure we handle a reasonable number of unique colors (e.g., 25 distinct colors)
        int numberOfColors = 25;
        // Generate a hue value to evenly distribute colors
        double hue = (uniqueNumber % numberOfColors) / (double) numberOfColors; // Hue between 0 and 1

        // Return a color based on the calculated hue, with fixed saturation and brightness
        return Color.hsb(hue * 360, 0.8, 0.8); // High saturation and brightness
    }
}
