package smartcity.building;

import javafx.scene.paint.Color;
import smartcity.graphe.Point;

public class RechargeStation extends Building{

	public static int price;
	
	public RechargeStation(Point pt) {
		super(pt);
		this.col = Color.BLUEVIOLET;
	}
	
	public static int getPrice() {
		return price;
	}
	
}
