package smartcity.task;

import smartcity.building.TaskBuilding;

public class BidTask extends Task {

	private int initialPrice;
	public int Bid1;
	public int Bid2;

	public BidTask(int reward, int time, int initialPrice, TaskBuilding build) {
		super(reward, time,build);
		this.initialPrice = initialPrice;
		Bid2 = Bid1 = 0;
	}

	public int getInitPrice() {
		return this.initialPrice;
	}

	public int makeOffer(int value) {
		if(Bid1 == 0) {
			Bid1 = value;
			return 1;
		}
		else if(Bid2 == 0) {
			Bid2 = value;
			return 2;
		}
		throw new IllegalArgumentException();
	}

	public int getWinner() {
		if(Bid1 == 0 || Bid2 == 0) throw new IllegalArgumentException();
		if(Bid1 > Bid2) return 1;
		return 2;
	}

}
