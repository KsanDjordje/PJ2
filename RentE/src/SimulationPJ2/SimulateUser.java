package SimulationPJ2;

public class SimulateUser {
	
	public String generateUserID(Boolean isLocal) {
		RandomFunctions gen = new RandomFunctions();
		if(isLocal) {
			return "ID".concat(gen.generateString("l", 9));
		}else {
			return "PPT".concat(gen.generateString("f", 10));
		}
	}
	public Integer generateDriversLicense() {
		RandomFunctions gen = new RandomFunctions();
		int min = 100000000; // Minimum value (smallest 9-digit number)
        int max = 999999999; // Maximum value (largest 9-digit number)
        return gen.generateRandomNumber(min,max);
	}
	public Boolean generateIsLocal() {
		RandomFunctions gen = new RandomFunctions();

		return gen.getRandomTrueFalse(0.6);
	}
}
