package SimulationPJ2;

public class SimulateUser {
	
	public String generateUserID(Boolean isLocal) {
		RandomStringGenerator gen = new RandomStringGenerator();
		if(isLocal) {
			return "ID".concat(gen.generateString("l", 9));
		}else {
			return "PPT".concat(gen.generateString("f", 10));
		}
	}
	public Integer generateDriversLicense() {
		RandomStringGenerator gen = new RandomStringGenerator();
		int min = 100000000; // Minimum value (smallest 9-digit number)
        int max = 999999999; // Maximum value (largest 9-digit number)
        return gen.generateRandomNumber(min,max);
	}
	public Boolean generateIsLocal() {
		RandomStringGenerator gen = new RandomStringGenerator();

		return gen.getRandomTrueFalse(0.6);
	}
}
