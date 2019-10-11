package smartcity.graphe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import smartcity.building.Building;
import smartcity.building.Factory;
import smartcity.building.RechargeStation;
import smartcity.building.Storage;
import smartcity.building.Store;
import smartcity.building.TaskBuilding;
import smartcity.building.NormalBuilding;


public class Graphe {

	private ArrayList<Point> pointList;
	private ArrayList<Arc> arcList = new ArrayList<Arc>();
	private ArrayList<RechargeStation> borneList;
	private ArrayList<Storage> storageList;
	
	public ArrayList<Storage> getStorageList(){
		return storageList;
	}
	
	private ArrayList<Store> storeList;
	
	public ArrayList<Store> getStoreList(){
		return storeList;
	}
	
	private ArrayList<Factory> factoryList;
	
	public ArrayList<Factory> getFactoryList(){
		return factoryList;
	}
	
	private ArrayList<TaskBuilding> taskBuildingList;
	
	public ArrayList<TaskBuilding> getTaskBuildingList() {
		return taskBuildingList;
	}


	private ArrayList<NormalBuilding> normalBuildingList;
	private int[] sommets;
	public Hmap map;
	private boolean firstGen;
	private int[] dx = {0,1,0,-1};
	private int[] dy = {1,0,-1,0};
	private int[] dxC = {1,1,0,-1,-1,-1,0,1};
	private int[] dyC = {0,-1,-1,-1,0,1,1,1};
	public ROAD[][] roadMap;

	/** Constructeur, initialise la carte utilise dans la theorie des graphes
	 * @param map une carte
	 */
	public Graphe(Hmap map) {
		this.map = map;
		this.roadMap = new ROAD[this.map.getTaille()][this.map.getTaille()];
	}

	/** Fait appel aux methodes necessaires pour preparer la carte a l'utilisation de la theorie des graphes
	 * 
	 */
	public void preparerGraphe() {
		this.findPoints();this.cleanPoints();
		this.addPointsToMap();
		this.findBuildings();
		this.findAllArcs();
	}

	/** Ajoute a pointList tous les points de type intersection, c'est a dire un "1" qui a au moins 3 autres "1" autour de lui
	 * les diagonales ne sont pas prises en compte
	 */
	public void findPoints(){
		this.borneList = new ArrayList<RechargeStation>();
		this.storageList = new ArrayList<Storage>();
		this.factoryList = new ArrayList<Factory>();
		this.storeList = new ArrayList<Store>();
		int nbVoisins;
		boolean vertical, horizontal;
		boolean top, left, right, bottom;
		this.pointList  = new ArrayList<Point>();
		for(int j = 0; j<map.getMap().length;j++) {
			for(int i = 0; i<map.getMap().length; i++) {
				if(map.getMap()[i][j]==1) {
					nbVoisins = 0;
					horizontal = vertical = top = left= right = bottom = false;
					try {
						if(map.getMap()[i-1][j]==1) {
							nbVoisins++;
							left = true;
						}
					} catch(Exception e) {}
					try {
						if(map.getMap()[i+1][j]==1) {
							nbVoisins++;
							right = true;
						}
					} catch(Exception e) {}
					try {
						if(map.getMap()[i][j-1]==1) {
							nbVoisins++;
							bottom = true;
						}
					} catch(Exception e) {}
					try {
						if(map.getMap()[i][j+1]==1) {
							nbVoisins++;
							top = true;
						}
					} catch(Exception e) {}
					if(nbVoisins != 2) {
						this.pointList.add(new Point(i,j));
					}
					else if(nbVoisins == 2 && (vertical || horizontal)) {
						this.pointList.add(new Point(i,j));
					}
					if(!firstGen)this.setRoad(top, bottom, left, right, i, j);
				}
				else if(map.getMap()[i][j]==2) {
					this.pointList.add(new Point(i,j));
					this.borneList.add(new RechargeStation(new Point(i,j)));
				}
				else if(map.getMap()[i][j]==3){
					this.pointList.add(new Point(i,j));
					this.storageList.add(new Storage(new Point(i,j)));
				}
				else if(map.getMap()[i][j]==4){
					this.pointList.add(new Point(i,j));
					this.storeList.add(new Store(new Point(i,j)));
				}
				else if(map.getMap()[i][j]==5){
					this.pointList.add(new Point(i,j));
					this.factoryList.add(new Factory(new Point(i,j)));
				}
			}
		}
	}
	
	private void setRoad(boolean top, boolean bottom, boolean left, boolean right, int x, int y) {
		if(top && bottom && left && right) this.roadMap[x][y] = ROAD.CROSS;
		else if(top && bottom && !left && !right) this.roadMap[x][y] = ROAD.HORIZONTAL;
		else if(!top && !bottom && left && right) this.roadMap[x][y] = ROAD.VERTICAL;
		else if(!top && bottom && left && right) this.roadMap[x][y] = ROAD.T_RIGHT;
		else if(top && !bottom && left && right) this.roadMap[x][y] = ROAD.T_LEFT;
		else if(top && bottom && !left && right) this.roadMap[x][y] = ROAD.T_NORMAL;
		else if(top && bottom && left && !right) this.roadMap[x][y] = ROAD.T_REVERSE;
		else if(top && !bottom && !left && !right) this.roadMap[x][y] = ROAD.END_LEFT;//
		else if(!top && bottom && !left && !right) this.roadMap[x][y] = ROAD.END_RIGHT;
		else if(!top && !bottom && !left && right) this.roadMap[x][y] = ROAD.END_TOP;
		else if(!top && !bottom && left && !right) this.roadMap[x][y] = ROAD.END_BOTTOM;//
		else if(top && !bottom && left && !right) this.roadMap[x][y] = ROAD.TURN_TR;
		else if(top && !bottom && !left && right) this.roadMap[x][y] = ROAD.TURN_BR;
		else if(!top && bottom && left && !right) this.roadMap[x][y] = ROAD.TURN_TL;
		else if(!top && bottom && !left && right) this.roadMap[x][y] = ROAD.TURN_BL;
	}
	
	public void afficherRoutes() {
		for(int i = 0; i< this.map.getTaille(); i++) {
			System.out.println("");
			System.out.println("");
			for(int j = 0; j< this.map.getTaille(); j++) {
				ROAD r = this.roadMap[i][j];
				if(r==ROAD.HORIZONTAL) System.out.print("-HO-");
				else if(r==ROAD.VERTICAL) System.out.print("-VE-");
				else if(r==ROAD.T_NORMAL) System.out.print("-IN-");
				else if(r==ROAD.T_REVERSE) System.out.print("-II-");
				else if(r==ROAD.T_LEFT) System.out.print("-IL-");
				else if(r==ROAD.T_RIGHT) System.out.print("-IR-");
				else if(r==ROAD.END_TOP) System.out.print("-ET-");
				else if(r==ROAD.END_BOTTOM) System.out.print("-EB-");
				else if(r==ROAD.END_LEFT) System.out.print("-EL-");
				else if(r==ROAD.END_RIGHT) System.out.print("-ER-");
				else if(r==ROAD.TURN_TL) System.out.print("-TL-");
				else if(r==ROAD.TURN_TR) System.out.print("-TR-");
				else if(r==ROAD.TURN_BL) System.out.print("-BL-");
				else if(r==ROAD.TURN_BR) System.out.print("-BR-");
				else if(r==ROAD.CROSS) System.out.print("-CR-"); 
				else System.out.print("-  -");
				
			}
		}
	}
	
	public ROAD getRoad(int x, int y) {
		return this.roadMap[x][y];
	}
	
	public void findTaskBuilding() {
		this.taskBuildingList = new ArrayList<TaskBuilding>();
		this.normalBuildingList = new ArrayList<NormalBuilding>();
		for(int i = 1; i < map.getMap().length-1; i++) {
			for(int j = 1; j < map.getMap().length-1; j++) {
				if(map.getMap()[i][j] == 0) {
					boolean roadNear = !(map.getMap()[i-1][j]==0&&map.getMap()[i+1][j]==0&&map.getMap()[i][j-1]==0&&map.getMap()[i][j+1]==0);
					if(roadNear)
						this.taskBuildingList.add(new TaskBuilding(new Point(i,j)));
					else
						this.normalBuildingList.add(new NormalBuilding(new Point(i,j)));
				}
			}
		}
	}
	
	public void findBuildings() {
		this.findTaskBuilding();
	}
	
	public boolean cleanPoints() {
		int around;
		int border;
		boolean modi = false;
		ArrayList<Point> toRemove = new ArrayList<Point>();
		this.pointList.add(new Point(0,this.map.getMap().length-1));
		this.pointList.add(new Point(0,0));
		this.pointList.add(new Point(this.map.getMap().length-1,0));
		this.pointList.add(new Point(this.map.getMap().length-1,this.map.getMap().length-1));
		for(Point pt : this.pointList) {
			around = 0;
			border = 0;
			for(int i = 0; i<8; i++) {
				try {
					if(this.map.getMap()[pt.getX()+this.dxC[i]][pt.getY()+this.dyC[i]] != 100) {
						if(this.pointList.contains(new Point(pt.getX()+this.dxC[i],pt.getY()+this.dyC[i]))) {
							around++;
							border++;
						}
						else {
							border++;
						}
					}
				}catch(Exception e) {}
			} 
			if(around >= 4 && border == 8) {
				toRemove.add(pt);
				modi = true;
			}
		}
		for(int j = 0; j<toRemove.size(); j++) {
			this.map.setMap(toRemove.get(j).getX(), toRemove.get(j).getY(), 0);
		}
		this.findPoints();
		return modi;
	}

	public int[] closestRechargeStation(int x, int y) {
		int[] indexDistance = new int[2];
		int distanceMin = 1000000, distanceCurrent, indexClosest = -1;
		for(Building station : this.borneList) {
			ArrayList<Integer> a = this.findWay(this.pointList.indexOf(new Point(x,y)), this.pointList.indexOf(station.getPosition()));
			distanceCurrent = this.getCheminDistance(a);
			if(distanceCurrent < distanceMin) {
				distanceMin = distanceCurrent;
				indexClosest = this.pointList.indexOf(station.getPosition());
			}
		}
		indexDistance[0] = indexClosest;
		indexDistance[1] = distanceMin;
		return indexDistance;
	}
	
	public int[] closestStore(int x, int y) {
		int[] indexDistance = new int[2];
		int distanceMin = 1000000, distanceCurrent, indexClosest = -1;
		for(Building store : this.storeList) {
			ArrayList<Integer> a = this.findWay(this.pointList.indexOf(new Point(x,y)), this.pointList.indexOf(store.getPosition()));
			distanceCurrent = this.getCheminDistance(a);
			if(distanceCurrent < distanceMin) {
				distanceMin = distanceCurrent;
				indexClosest = this.pointList.indexOf(store.getPosition());
			}
		}
		indexDistance[0] = indexClosest;
		indexDistance[1] = distanceMin;
		return indexDistance;
	}
	
	public int[] closestFactory(int x, int y) {
		int[] indexDistance = new int[2];
		int distanceMin = 1000000, distanceCurrent, indexClosest = -1;
		for(Building factory : this.factoryList) {
			ArrayList<Integer> a = this.findWay(this.pointList.indexOf(new Point(x,y)), this.pointList.indexOf(factory.getPosition()));
			distanceCurrent = this.getCheminDistance(a);
			if(distanceCurrent < distanceMin) {
				distanceMin = distanceCurrent;
				indexClosest = this.pointList.indexOf(factory.getPosition());
			}
		}
		indexDistance[0] = indexClosest;
		indexDistance[1] = distanceMin;
		return indexDistance;
	}
	
	/** Ajoute sur l'attribut "map" de l'element "Hmap map" les points de pointList en rempla�ant les "1" par des "4"
	 * 
	 */
	public void addPointsToMap() {
		for(int i = 0; i<this.pointList.size();i++) {
			this.map.setMap(this.pointList.get(i).getX(), this.pointList.get(i).getY(), 6);
		}
	}

	/** Appelle la methode "findArcs" pour tous les points de pointList, et ajoute chacun des arcs a "arcList" de faï¿½on ordonee
	 * 
	 */
	public void findAllArcs() {
		ArrayList<Arc> temp = new ArrayList<Arc>();
		for(Point pt : this.pointList) {
			temp = this.findArcs(pt);
			for(Arc arc : temp) {
				this.arcList.add(arc);
			}
		}
	}

	/** Genere une List des arcs existants ayant comme point initial le point "pt" passe en parametre
	 * @param pt le point initial des arcs recherches
	 * @return une List des arcs ayant comme point initial le point "pt" passe en parametre
	 */
	public ArrayList<Arc> findArcs(Point pt) {

		int[][] prepaArc;
		ArrayList<Arc> arcsFound = new ArrayList<Arc>();
		prepaArc = new int[this.map.getMap().length][this.map.getMap().length];
		for(int i = 0;i<prepaArc.length;i++) {
			for(int j = 0;j<prepaArc.length;j++) {
				prepaArc[i][j] = 0;
			}
		}
		int x,y,index,distance,val;

		prepaArc[pt.getX()][pt.getY()] = 1;
		for(int round = 0; round < 4 ; round++) 
		{

			try{ 
				{
					val = this.map.getMap()[pt.getX()+this.dx[round]][pt.getY()+this.dy[round]];
					if(val == 2 || val == 3 || val == 4 || val == 5 || val == 6) {
						x = pt.getX();
						y = pt.getY();
						x += dx[round];
						y += dy[round];
						prepaArc[x][y] = 1;
						distance = 1;
						if(this.pointList.contains(new Point(x,y)) && pt.equals(new Point(x,y)) == false) 
						{
							arcsFound.add(new Arc(this.pointList.indexOf(pt),this.pointList.indexOf(new Point(x,y)),distance));
						}
					}
					else if(val == 1) 
					{

						x = pt.getX();
						y = pt.getY();
						x += dx[round];
						y += dy[round];
						prepaArc[x][y] = 1;
						distance = 1;

						for(index = 0; index < 4; index++) {
							try {
								val = this.map.getMap()[x+this.dx[index]][y+this.dy[index]];
								if(prepaArc[x+dx[index]][y+dy[index]] == 0 && (val == 1 || val == 2 || val == 3 || val == 4 || val == 5 || val == 6)) 
								{
									x += dx[index];
									y += dy[index];
									prepaArc[x][y] = 1;
									index = -1;
									distance++;
									if(this.pointList.contains(new Point(x,y)) && pt.equals(new Point(x,y)) == false) 
									{
										arcsFound.add(new Arc(this.pointList.indexOf(pt),this.pointList.indexOf(new Point(x,y)),distance));
										index = 5;
									}
								}
							}catch(Exception e) {}
						}
					}
				}
			}catch(Exception e) {}
		}
		Collections.sort(arcsFound);
		return arcsFound;
	}


	/** Genere une List des index des points successeurs du point d'index i dans pointList
	 * @param i l'index d'un point dans pointList
	 * @return une List des index des points successeurs du point d'index i
	 */
	public ArrayList<Integer> suc(int i){
		ArrayList<Integer> suc = new ArrayList<Integer>();
		for(Arc ar : this.arcList) {
			if(ar.isSuc(i) == true && suc.contains(ar.indexFinal) == false) {suc.add(ar.indexFinal);}
		}
		suc.sort(null);
		return suc;
	}

	/** Renvoi la distance de l'arc entre les deux points passï¿½s en paramï¿½tre
	 * @param indexPointA : le point initial
	 * @param indexPointB : le point final
	 * @return la distance entre les deux points
	 */
	public int getDistanceBetween(int indexPointA, int indexPointB) {
		for(Arc ar : this.arcList) {
			if(ar.indexInitial == indexPointA && ar.indexFinal == indexPointB) {
				return ar.distance;
			}
		}
		return -1;
	}

	/** affiche une arrayList passee en parametre
	 * @param ar l'arrayList qui doit ï¿½tre affichee
	 */
	public void afficherArray(@SuppressWarnings("rawtypes") ArrayList ar) {
		for(int i = 0; i < ar.size(); i++) {
			System.out.println(ar.get(i));
		}
	}

	public void initialiser(int indexStart) {

		for(int i = 0; i<sommets.length; i++) {
			if(i == indexStart) {
				this.sommets[i] = 0; 
			}
			else {
				this.sommets[i] = 9999;
			}
		}
	}

	public int findMin(ArrayList<Integer> Q) {

		int mini = 9999;
		int sommet = -1;
		for(int s = 0; s<Q.size(); s++) {
			if(this.sommets[Q.get(s)]<=mini) {
				mini = this.sommets[Q.get(s)];
				sommet = Q.get(s);
			}
		}
		return sommet;
	}

	public ArrayList<Integer> findWay(int indexStart, int indexDestination){

		ArrayList<Integer> chemin = new ArrayList<Integer>();
		int[] chem;
		int s1;
		ArrayList<Integer> voisins;
		this.sommets = new int[this.pointList.size()];
		chem = new int[this.sommets.length];
		this.initialiser(indexStart);
		ArrayList<Integer> Q = new ArrayList<Integer>();
		for(int i = 0; i<this.pointList.size(); i++) {
			Q.add(i);
		}
		while(Q.isEmpty()==false) {
			s1 = this.findMin(Q);
			Q.remove((Integer) s1);
			voisins = this.suc(s1);
			for(int s2 : voisins) {
				if(this.sommets[s2] > this.sommets[s1] + this.getDistanceBetween(s1, s2)) {
					this.sommets[s2] = this.sommets[s1] + this.getDistanceBetween(s1, s2);
					chem[s2] = s1;
				}
			}
		}
		int s = indexDestination;
		while(s != indexStart) {
			chemin.add(s);
			s = chem[s];
		}
		chemin.add(indexStart);
		return chemin;
	}

	public int getCheminDistance(ArrayList<Integer> chemin ) {
		int distance = 0;
		for(int i = 0; i < chemin.size()-1;i++) {
			distance += this.getDistanceBetween(chemin.get(i), chemin.get(i+1));
		}
		return distance;
	}
	
	public int getPointIndex(Point pt) {
		if(this.pointList.contains(pt)) return this.pointList.indexOf(pt);
		return -1;
	}
	
	public Point getRandomPoint() {
		Random ran = new Random();
		int ranIndex = ran.nextInt(this.pointList.size());
		return this.pointList.get(ranIndex);
	}
	
	public int getMapSize() {
		return this.map.getMap().length;
	}
	
	public Building[][] generateBuildingsMap() {
		Building[][] buildings = new Building[this.getMapSize()][this.getMapSize()];
		
		ArrayList<Building> allBuildings = new ArrayList<Building>();

		allBuildings.addAll(borneList);
		allBuildings.addAll(storageList);
		allBuildings.addAll(storeList);
		allBuildings.addAll(taskBuildingList);
		allBuildings.addAll(factoryList);
		allBuildings.addAll(normalBuildingList);
		for(Building b : allBuildings)
			if(b.getPosition() != null)
				buildings[b.getPosition().getY()][b.getPosition().getX()] = b;
		
		return buildings;
	}
	
	public void addPoint(int x, int y){
		this.pointList.add(new Point(x,y));
		this.findAllArcs();
	}
	
	public void removePoint(int x, int y){
		this.pointList.remove(new Point(x,y));
		this.findAllArcs();
	}
	
	public ArrayList<Integer> findWay(int indexPointStart, int xTaskPoint, int yTaskPoint){
		this.addPoint(xTaskPoint, yTaskPoint);
		ArrayList<Integer> way = this.findWay(indexPointStart, this.pointList.indexOf(new Point(xTaskPoint,yTaskPoint)));
		this.removePoint(xTaskPoint, yTaskPoint);
		return way;
	}
	
	public Point getPoint(int p) {
		return this.pointList.get(p);
	}
	
}


