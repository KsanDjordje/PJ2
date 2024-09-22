package location;

/**
 * Represents a location in a 2D coordinate system.
 */
public class Location {
    private int x;
    private int y;

    /**
     * Creates a Location object with specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @throws OutOfRadiusException if the coordinates are out of the defined radius (0 to 19)
     */
    public Location(int x, int y) throws OutOfRadiusException {
        validateCoordinates(x, y);
        this.x = x;
        this.y = y;		
    }

    /**
     * Creates a Location object from string representations of coordinates.
     *
     * @param str the string representation of the x-coordinate
     * @param str2 the string representation of the y-coordinate
     * @throws OutOfRadiusException if the coordinates are out of the defined radius (0 to 19)
     */
    public Location(String str, String str2) throws OutOfRadiusException {
        str = str.replace("\"", "");
        str2 = str2.replace("\"", "");

        int x = Integer.parseInt(str);
        int y = Integer.parseInt(str2);
        validateCoordinates(x, y);
        this.x = x;
        this.y = y;
    }

    /**
     * Validates the given coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @throws OutOfRadiusException if the coordinates are out of the defined radius (0 to 19)
     */
    private void validateCoordinates(int x, int y) throws OutOfRadiusException {
        if (x >= 20 || x < 0 || y >= 20 || y < 0) {
            throw new OutOfRadiusException();
        }
    }

    /**
     * Returns a string representation of the Location.
     *
     * @return a string in the format "(x,y)"
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * Gets the x-coordinate.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate.
     *
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate.
     *
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Determines if the location is in a wide area.
     *
     * @return true if the location is wide; false otherwise
     */
    Boolean isWide() {
        return (this.x > 14 || this.x < 5 || this.y > 14 || this.y < 5);
    }
}
