package smartcity.ui;

import java.io.File;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import smartcity.Partie;
import smartcity.building.Building;
import smartcity.building.Factory;
import smartcity.building.NormalBuilding;
import smartcity.building.RechargeStation;
import smartcity.building.Storage;
import smartcity.building.Store;
import smartcity.building.TaskBuilding;

public class MapView extends GridPane {
	
	private Partie partie;
	private Image store = new Image("file:ressources" + File.separator + "store2.png");
	private Image storage = new Image("file:ressources" + File.separator + "storage.png");
	private Image taskBuilding = new Image("file:ressources" + File.separator + "_building.png");
	private Image normalBuilding = new Image("file:ressources" + File.separator + "building.png");
	private Image rechargeStation = new Image("file:ressources" + File.separator + "recharge2.png");
	private Image factory = new Image("file:ressources" + File.separator + "factory.png");
	
	public MapView(Partie partie) {
		this.partie = partie;
		this.init();
	}
	
	public void init() {
		this.getChildren().clear();
		
		Building[][] buildingsMap = partie.getBuildingsMap();
		for(int x = 0; x < buildingsMap.length; x++)
			for(int y = 0; y < buildingsMap[x].length; y++) {
				Building b = buildingsMap[x][y];
				if(this.partie.mapGraphe.roadMap[y][x]!=null) {
					ImageView v = new ImageView();
					v.setImage(new Image("file:ressources" + File.separator + "" + this.partie.mapGraphe.getRoad(y, x).getImagePath()));
					this.add(v,x,y);
				}
				else if(b instanceof Storage) {
					ImageView v = new ImageView();
					v.setImage(storage);
					this.add(v,x,y);
				}
				else if(b instanceof Store) {
					ImageView v = new ImageView();
					v.setImage(store);
					this.add(v,x,y);
				}
				else if(b instanceof NormalBuilding) {
					ImageView v = new ImageView();
					v.setImage(normalBuilding);
					this.add(v,x,y);
				}
				else if(b instanceof TaskBuilding) {
					ImageView v = new ImageView();
					v.setImage(getRBImage());
					this.add(v,x,y);
				}
				else if(b instanceof RechargeStation) {
					ImageView v = new ImageView();
					v.setImage(rechargeStation);
					this.add(v,x,y);
				}
				else if(b instanceof Factory) {
					ImageView v = new ImageView();
					v.setImage(factory);
					this.add(v,x,y);
				}
				else {
					this.add(new CaseView(b != null ? b.getColor() : Color.GREY), x, y);
				}
			}
	}
	
	private Image getRBImage(){
		return new Image("file:ressources" + File.separator + "taskbuilding" + (int)(Math.floor(Math.random()*4)+1) + ".png");
	}

}
