package smartcity.building;

import javafx.scene.paint.Color;
import smartcity.graphe.Point;
import smartcity.task.Task;

public class TaskBuilding extends Building {
	
	private Task task;

	public TaskBuilding(Point pos) {
		super(pos);
		this.col = Color.BISQUE;
	}
	
	public Task getTask(){
		return this.task;
	}
}
