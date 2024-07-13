package main;

public class Location {
	private int x;
	private int y;
	
	public Location(int x, int y) throws OutOfRadiusException {
		if(x >= 20 || x < 0 || y >= 20 || y < 0) {
			throw new OutOfRadiusException();
		}else {
			this.x = x;
			this.y = y;
		}		
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	Boolean isWide() {
		if(this.x > 14 || this.x < 5 || this.y > 14 || this.y < 5) {
			return true;
		}
		return false;
	}
}
