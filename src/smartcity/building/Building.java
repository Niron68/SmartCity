package smartcity.building;

import javafx.scene.paint.Color;
import smartcity.graphe.Point;

public class Building {

	private Point position;
	protected Color col;
	
	public Building(Point pos) {
		this.position = pos;
	}
	
	public Point getPosition() {
		return this.position;
	}
	
	public Color getColor() {
		return this.col;
	}
	
}
