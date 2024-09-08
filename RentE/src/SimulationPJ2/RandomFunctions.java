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
     * Generates a color based on the thread's name.
     * 
     * @param threadName The name of the thread (e.g., "Thread-12").
     * @return A Color object derived from the thread number.
     */
    public Color generateColorFromThreadName(String threadName) {
        // Extract the number from the thread name (e.g., Thread-12)
        String[] parts = threadName.split("-");
        if (parts.length < 2) {
            return Color.GRAY; // Default color if extraction fails
        }

        try {
            // Parse the thread number from the thread name
            int threadNumber = Integer.parseInt(parts[1]);

            // Ensure we have a maximum of 25 threads
            int numberOfThreads = 25;
            // Generate a hue value to evenly distribute colors
            double hue = (threadNumber % numberOfThreads) / (double) numberOfThreads; // Hue between 0 and 1
            return Color.hsb(hue * 360, 0.8, 0.8); // Use high saturation and brightness
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Color.GRAY; // Default color on error
        }
    }
}
