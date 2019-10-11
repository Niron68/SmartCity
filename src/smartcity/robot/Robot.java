package smartcity.robot;

import java.util.ArrayList;

import smartcity.Merchandise;
import smartcity.Tool;
import smartcity.building.Store;
import smartcity.graphe.Graphe;
import smartcity.graphe.Point;
import smartcity.task.Task;

public class Robot {

	private Team team;
	private Point pos;
	private double speed;
	private double capacity;
	private double charge;
	private int batteryMax;
	private int battery;
	private Kind kind;
	private ArrayList<Tool> tools;
	private ArrayList<Merchandise> stock;
	private Task task;
	private ArrayList<Integer> chemins;
	private Direction lastDirection;
	private Graphe graphe;
	
	public Robot(Kind kd, Point pt,Graphe graphe) {
		this.kind = kd;
		this.pos = pt;
		switch(kd) {
			case CAR:
				this.capacity = 30;
				this.speed = 7;
				this.batteryMax = 25;
				break;
			case TRUCK:
				this.speed = 4;
				this.capacity = 70;
				this.batteryMax = 30;
			case BIKE:
				this.speed = 9;
				this.capacity = 15;
				this.batteryMax = 18;
			case DRONE:
				this.speed = 3;
				this.capacity = 8;
				this.batteryMax = 15;
				break;
		}

		this.setBattery(this.batteryMax);
		this.tools = new ArrayList<Tool>();
		this.stock = new ArrayList<Merchandise>();
		this.charge =0;
		this.graphe = graphe;
	}

	public Point getPosition() {
		return this.pos;
	}

	public void setPosition(Point pos) {
		this.pos = pos;
	}
	
	public double getSpeed() {
		return speed;
	}

	public double getCapacity() {
		return capacity;
	}
	
	public double getCharge() {
		return charge;
	}
	
	public int getBatteryMax() {
		return batteryMax;
	}

	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		this.battery = battery;
	}
	
	public Kind getKind() {
		return kind;
	}
	
	public ArrayList<Tool> getTools(){
		return this.tools;
	}
	
	public void addTool(Tool tool){
		this.tools.add(tool);
	}
	
	public Task getTask(){
		return this.task;
	}
	
	public void setTask(Task task){
		this.task = task;
	}
	
	public boolean isLibre(){
		return this.task==null;
	}

	
	public void addToStock(Merchandise merch) throws Exception{
		if(this.capacity < this.charge + merch.getVolume()){
			throw new Exception("You can't add this merchandise to the stock.");
		}
		else{
			this.stock.add(merch);
			this.charge = this.charge + merch.getVolume();
		}
	}
		
	public void removeMerchandise(Merchandise merch){
		this.stock.remove(merch);
		this.charge = this.charge - merch.getVolume();		
	}
	
	public void buy(Merchandise merch,int quantite, Store store){
		this.team.setMoney(this.team.getMoney() -merch.getPrice());
		store.removeMerchandise(merch.getId(),quantite);
	}
	
	public String toString(){
		return "Equipe "+this.team + "\n Position" + this.pos + "\n Capa" + this.capacity + "\n charge " + this.charge + "\n Type: " + this. kind + this.tools + this.stock + this.task;
	} 
	
	/*
	 * Déplace le robot dans la direction qu'il a en paramètre, d'un nombre de case provenant de son attribut speed, et décrémente la batterie à chaque mouvement.
	 * La dernière direction vient du retour de la méthode findDirection(), qu'on appelle à chaque fois qu'on arrive au point suivant de notre chemin.
	 */
	public void move(){
		for(int nbCases = 0; nbCases<this.speed;nbCases++){
			switch(this.lastDirection){
			case DROITE:
				this.pos.setX(this.pos.getX()+1);
				break;
			case GAUCHE:
				this.pos.setX(this.pos.getX()-1);
				break;
			case HAUT:
				this.pos.setY(this.pos.getY()+1);
				break;
			case BAS:
				this.pos.setY(this.pos.getY()+1);
				break;
			}
			this.battery--;
			if(this.pos.equals(this.graphe.getPoint(this.chemins.get(1)))){
				this.chemins.remove(this.chemins.get(0));
				if(this.chemins.get(1) != null){
					this.findDirection();
				}
			}
		}
	}
	
	/*
	 * Détermine les points par lesquels on passe pour arriver à la destination
	 * dont les coodonnées sont entrées en paramètres et détermine la première direction du trajet.
	 */
	public void setChemins(int x,int y){
		this.chemins = this.graphe.findWay(this.graphe.getPointIndex(this.pos),x,y);
		this.findDirection();
	}
	
	/*
	 * Détermine la direction à prendre à partir de la position actuelle pour rejoindre le prochain point sur le chemin.
	 */
	public void findDirection(){
		Point pointApresPos = this.graphe.getPoint(this.chemins.get(1));
		int difX = this.pos.getX()-pointApresPos.getX();
		int difY = this.pos.getY()-pointApresPos.getY();
		if(difX > 0){
			this.lastDirection = Direction.GAUCHE;
		}
		else if(difX < 0){
			this.lastDirection = Direction.DROITE;
		}
		else{
			if(difY > 0){
				this.lastDirection = Direction.BAS;
			}
			else{
				this.lastDirection = Direction.HAUT;
			}
		}
		
	}
	
	/*
	 * Choisit la première destination du robot en fonction de son stock et de la marchandise utile à la tâche; 
	 * s'il n'a pas l'objet ni l'occasion de le fabriquer, il se dirige au magasin le plus proche avec closestStore(),  
	 s'il peut fabriquer alors il se dirige vers la plus proche usine avec closestFactory(), 
	 sinon le robot se dirige vers le lieu de complétion de la tâche.
	 */
	public int[] getDestination(){
		Task task = this.getTask();
		if(!stock.contains(task.getMerchandiseRequired())){
			if(!stock.contains(task.getMerchandiseRequired().getRawMaterials())){
				return graphe.closestStore(this.pos.getX(), this.pos.getY());
			}
			else{
				return graphe.closestFactory(this.pos.getX(), this.pos.getY());
			}
		}
		else{
			return new int[]{task.getPosition().getX(),task.getPosition().getY()};
		}
	}

	public ArrayList<Merchandise> getStock() {
		return this.stock;
	}
}
	


