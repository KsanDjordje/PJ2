package simulationPJ2;

import myUtility.RandomFunctions;

/**
 * This class simulates user-related functionality, such as generating user IDs, driver's license numbers, 
 * and determining whether the user is local.
 */
public class SimulateUser {

    /**
     * Generates a user ID based on whether the user is local or foreign.
     * 
     * @param isLocal A boolean indicating if the user is local (true) or foreign (false).
     * @return A String representing the generated user ID. For locals, the ID starts with "ID" and 
     *         contains a 9-character alphanumeric string. For foreigners, the ID starts with "PPT" 
     *         followed by a 10-character alphanumeric string.
     */
    public String generateUserID(Boolean isLocal) {
        RandomFunctions gen = new RandomFunctions();
        if (isLocal) {
            return "ID".concat(gen.generateString("l", 9));
        } else {
            return "PPT".concat(gen.generateString("f", 10));
        }
    }

    /**
     * Generates a random 9-digit driver's license number.
     * 
     * @return An Integer representing the randomly generated 9-digit driver's license number.
     */
    public Integer generateDriversLicense() {
        RandomFunctions gen = new RandomFunctions();
        int min = 100000000;
        int max = 999999999;
        return gen.generateRandomNumber(min, max);
    }

    /**
     * Generates whether the user is local or foreign with a probability of 60% for local.
     * 
     * @return A Boolean representing whether the user is local. It returns true with a probability of 60%, 
     *         indicating that the user is local, and false otherwise.
     */
    public Boolean generateIsLocal() {
        RandomFunctions gen = new RandomFunctions();
        return gen.getRandomTrueFalse(0.6);
    }
}
