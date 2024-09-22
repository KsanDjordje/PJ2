package location;

/**
 * Exception thrown when a location's coordinates are outside the defined radius.
 */
public class OutOfRadiusException extends Exception {
    
    /**
	 * Current version
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs a new OutOfRadiusException with a default error message.
     */
    public OutOfRadiusException() {
        super("Coordinates are out of the defined radius.");
    }

    /**
     * Constructs a new OutOfRadiusException with a specified error message.
     *
     * @param message the detail message
     */
    public OutOfRadiusException(String message) {
        super(message);
    }
}