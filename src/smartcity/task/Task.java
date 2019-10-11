package smartcity.task;

import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

import smartcity.Merchandise;
import smartcity.building.TaskBuilding;
import smartcity.graphe.Point;

public class Task {

	private int reward;
	private long time;
	private Date dateBeginning;
	private TaskBuilding building;
	private Merchandise merchandisedRequired;
			
	public Task(int reward, long time, TaskBuilding build)  {
		try{
			Merchandise.loadMerchandises();
			this.reward = reward;
			Random rand = new Random();
			this.time = time;
			this.building = build;
			this.merchandisedRequired = Merchandise.getMerchandises().get(rand.nextInt(28)+6);
		}
		catch(Exception e) {}
	}
	
	public int getReward() {
		return this.reward;
	}
	
	public void beginTask() {
		this.dateBeginning = new Date();
	}
	
	public long elapsedTime() {
			Date dateActual = new Date();
			return ((dateActual.getTime() - this.dateBeginning.getTime())/1000);
	}
	
	public boolean isTimeOver() {
		return this.time > elapsedTime();
	}
	
	public Point getPosition() {
		return this.building.getPosition();
	}
	
	public Merchandise getMerchandiseRequired() {
		return this.merchandisedRequired;
	}
	
	@Override 
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		Task task = (Task) obj;
		return task.reward == this.reward && task.time == this.time && task.merchandisedRequired.equals(this.merchandisedRequired);
	}
}