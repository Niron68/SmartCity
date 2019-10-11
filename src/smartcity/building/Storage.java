package smartcity.building;

import smartcity.graphe.Point;
import javafx.scene.paint.Color;

public class Storage extends Building {

	private int capacity;
	
	public Storage(Point position) {
		super(position);
		this.col = Color.ORANGE;
	}
	
	public int getCapacity() {
		return this.capacity;
	}

}
