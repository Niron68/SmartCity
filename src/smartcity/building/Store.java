package smartcity.building;

import java.util.HashMap;

import javafx.scene.paint.Color;
import smartcity.Merchandise;
import smartcity.graphe.Point;

public class Store extends Building{

	private HashMap<Merchandise,Integer> merchandises;
	
	public HashMap<Merchandise,Integer> getMerchandises(){
		return merchandises;
	}
	
	public Store(Point pos) {
		super(pos);
		this.merchandises = new HashMap<Merchandise,Integer>();
		this.col = Color.RED;
	}
	
	public void addMerchandise(Merchandise merch, int quantite){
		this.merchandises.put(merch,quantite);
	}
	
	public void removeMerchandise(int idMerchandise,int quantite){
		for(Merchandise merch : this.merchandises.keySet()){
			if(merch.getId() == idMerchandise){
				this.merchandises.replace(merch, this.merchandises.get(merch)-quantite);
			}
		}
	}
}



