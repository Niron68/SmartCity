package smartcity.graphe;

public final class Point {

	private int x,y;
	
	public Point() {
		this.x = this.y = 0;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Point) {
			Point p = (Point) o;
			return this.x == p.x && this.y == p.y;
		}
		return false;
	}
	
	public String toString() {
		return "(" + this.getX() + ";" + this.getY() + ")";
	}
	
}
