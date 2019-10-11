package smartcity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import javafx.application.Platform;
import smartcity.building.Building;
import smartcity.graphe.Graphe;
import smartcity.graphe.Hmap;
import smartcity.robot.Robot;
import smartcity.robot.Team;
import smartcity.task.Task;
import smartcity.ui.Equipe;

public class Partie extends Observable {
	
	private Building[][] buildingsMap;
	public Graphe mapGraphe;
	private Team teamA = new Team();
	private Team teamB = new Team();
	private Thread gameThread;
	private ArrayList<Task> availableTask = new ArrayList<Task>();
	
	public final static double TICK_RATE = 1;
	private String cityName;
	private long msStarted = System.currentTimeMillis();
	
	public long getStartMilliseconds() {
		return msStarted;
	}
	
	public long getElapsedMilliseconds() {
		return msStarted == -1 ? -1 : System.currentTimeMillis() - msStarted;
	}
	
	public String getCityName() {
		return cityName;
	}

	public Partie(int tailleMap, double densMap, String cityName, Equipe configEquipe1, Equipe configEquipe2) {
		this.cityName = cityName;
		Hmap hmap = new Hmap(tailleMap , densMap);
		hmap.placerBornes();
		hmap.placerEntrepot(hmap.getTaille()/5);
		hmap.placerMagasin(hmap.getTaille()/5);
		hmap.placerUsine(hmap.getTaille()/5);
		this.mapGraphe = new Graphe(hmap);
		this.mapGraphe.preparerGraphe();
		this.buildingsMap = this.mapGraphe.generateBuildingsMap();
		this.mapGraphe.map.afficher();
		this.mapGraphe.afficherRoutes();
		teamA.setGraphe(mapGraphe);
		teamB.setGraphe(mapGraphe);
		teamA.setTeamName(configEquipe1.nomProperty().get());
		teamA.createRobots(configEquipe1.getKindsArray(),true);
		teamA.setMoney(100);
		teamB.setTeamName(configEquipe2.nomProperty().get());
		teamB.createRobots(configEquipe2.getKindsArray(),false);
		teamB.setMoney(100);
		for(int i =0;i<4;i++){
			this.taskCreator();
		}
	}

	public Building[][] getBuildingsMap() {
		return this.buildingsMap;
	}

	public Team getTeam(int i){
		return i == 0 ? teamA : i == 1 ? teamB : null;
	}
	
	/*
	 * M�thode appel�e � chaque tour du jeu pour faire avancer la partie.
	 */
	public void updateLoop() throws Exception {
		System.out.println("updateLoop");
		teamA.updateLoop(availableTask);
		teamB.updateLoop(availableTask);
	}
	
	public  boolean partieFinie() {
		return this.teamA.getMoney() > 500 || this.teamB.getMoney() > 500;
	}
	
	/*
	 * G�n�re une t�che � l'emplacement d'un b�timent(TaskBuilding) al�atoire et d�marre sa dur�e de vie.  
	 */
	public void taskCreator(){
		Random rand = new Random();
		int nbAleatoireDeLaRandomette = rand.nextInt(mapGraphe.getTaskBuildingList().size());
		Task tache = new Task(100,100,mapGraphe.getTaskBuildingList().get(nbAleatoireDeLaRandomette));
		availableTask.add(tache);
		tache.beginTask();
	}
	

	public void startGame() {
		double interval = 1000.0D / TICK_RATE;
		this.gameThread = new Thread(() -> {
			try {
				Thread.sleep((int) interval);
				while(true) {
					long startMs = System.currentTimeMillis();
					try {
						this.updateLoop();
					}
					catch(Exception e) {
						System.err.println("An error occured during the update loop!");
						e.printStackTrace();
					}
					Platform.runLater(() -> {
						this.setChanged();
						this.notifyObservers();
					});
					if(this.partieFinie()) {
						System.out.println("Partie terminée");
						Thread.sleep(2000);
						// Afficher écran de fin
						return;
					}
					long endMs = System.currentTimeMillis();
					
					long timeTaken = endMs - startMs;
					
					if(timeTaken > interval)
						System.err.println("La boucle n'a pas pu se terminer à temps pour correspondre au tick rate ! Le jeu tourne au ralenti !");
					else
						Thread.sleep((int) (interval - timeTaken));
				}
			} catch (/*InterruptedException |*/ Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		this.gameThread.start();
		this.msStarted = System.currentTimeMillis();
	}
}
