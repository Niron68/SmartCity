package smartcity.robot;

import java.util.ArrayList;
import java.util.List;

import smartcity.Merchandise;
import smartcity.Tool;
import smartcity.building.Factory;
import smartcity.building.Store;
import smartcity.graphe.Graphe;
import smartcity.graphe.Point;
import smartcity.task.Task;

public class Team {
	
	private Robot[] robotL;
	int robotNbr,money=0;
	private String teamName;
	private Graphe graphe;
	private ArrayList<Task> tasks;
	
	public Team() {
		this.robotL = new Robot[this.robotNbr];
		this.tasks = new ArrayList<Task>();
	}
	
	public void createRobots(Kind[] kinds,boolean top){
		int compteur;
		this.robotNbr = kinds.length;
		int indiceRob = 0;
		if(top) compteur = 0;
		else compteur = this.graphe.getMapSize()-1;
		Robot[] robrob = new Robot[kinds.length];
		for(Kind type : kinds){
			robrob[indiceRob]=new Robot(type,new Point(0,compteur),graphe);
			if(top)compteur++;
			else compteur--;
			indiceRob++;
		}
		robotL = robrob;
	}
	
	public void setGraphe(Graphe graphe){
		this.graphe = graphe;
	}
	
	public Robot[] getRobotL() {
		return this.robotL;
	}

	public int getRobotNbr() {
		return robotNbr;
	}

	public String getTeamName() {
		return this.teamName;
	}
	
	public void setTeamName(String name){
		this.teamName = name;
	}
	
	public int getMoney() {
		return this.money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public void acceptTask(Task task,Robot[] robotlist){
		for(Robot rob : robotlist){
			rob.setTask(task);
		}
		tasks.add(task);
	}
	
	/*
	 * Pour une t�che donn�e, retourne le robot parmi une liste qui se trouve au plus pr�s de son emplacement.
	 */
	public Robot getCloserRobot(Task task,Robot[] robots){
		int index = -1;
		int indiceTask = graphe.getPointIndex(task.getPosition());
		int distanceMin = 10000;
		for(int i=0;i<robots.length;i++){
			int indiceRob = graphe.getPointIndex(robots[i].getPosition());
			//calculer distance entre task.getPosition() et robots[i].getPosition()
			int distance = graphe.getCheminDistance(graphe.findWay(indiceRob, indiceTask));
			if(distance < distanceMin){
				distanceMin = distance;
				index = i;
			}
		}
		return index != -1 ? robots[index] : null;
	}
	
	/*
	 * Renvoie tout les robots qui ne sont pas affect�s � une t�che.
	 */
	public List<Robot> getRobotsLibres() {
		List<Robot> RobotsLibre = new ArrayList<Robot>();
		for(int i=0; i<this.robotNbr;i++){
			if(this.robotL[i].isLibre()){
				RobotsLibre.add(this.robotL[i]);
			}
		}
		return RobotsLibre;
	}

	/*
	 * Retourne une liste de robots pouvant r�pondre � une t�che gr�ce � leur disponibilit� et 
	 * aux outils requis pour fabriquer la marchandise voulue.	  
	 */
	public List<Robot> getRobotsWithTools(Task task){
		List<Tool> tools = task.getMerchandiseRequired().getTools();
		List<Robot> robotsLibres = this.getRobotsLibres();
		List<Robot> robotList = new ArrayList<Robot>();
		for(int t=0;t<tools.size();t++){
			List<Robot> listTempo = new ArrayList<Robot>();
			for(Robot rob : robotsLibres){
				if(rob.getTools().contains(tools.get(t))){
					listTempo.add(rob);
				}
			}
			if(listTempo.size() > 0) {
				Robot robotPlusPres = getCloserRobot(task,listTempo.toArray(new Robot[listTempo.size()]));
				if(!robotList.contains(robotPlusPres))
					robotList.add(robotPlusPres);
			}
		}
		return robotList;		
	} 
	 /*
	  * D�termine le score d'int�r�t de la t�che en param�tre  en prenant compte de la distance entre elle et
	  *  les robots devant la r�aliser et la r�compense de celle-ci.
	  */
	public int getScoreTask(Task task){
		List<Robot> robs = this.getRobotsWithTools(task);
		int distance = 1;
		for(Robot rob : robs){
			int depart = graphe.getPointIndex(task.getPosition());
			int arrive = graphe.getPointIndex(rob.getPosition());
			ArrayList<Integer> chemin = graphe.findWay(depart, arrive); 
			distance += graphe.getCheminDistance(chemin);
		}
		int score =  task.getReward() / distance;
		return score;
	}
	
	/*
	 * D�termine la meilleure t�che � faire parmi toute la liste gr�ce au score calcul� plus t�t. 
	 */
	public Task WhichTaskCanIDo(ArrayList<Task> taskList){
		int scoreMax = 0;
		Task taskReturn = null;
		for(Task task : taskList){
			int score = this.getScoreTask(task);
			if(score > scoreMax){
				scoreMax = score;
				taskReturn = task;
			}
		}
		return taskReturn;
	}
	
	/*
	 * Renvoie toutes les t�ches que l'�quipe est en capacit� de r�aliser,
	 * par rapport aux outils dont les robots disposent, dans la liste entr�e en param�tre
	 */
	public ArrayList<Task> getPossibleTask(ArrayList<Task> list){
		ArrayList<Task> taskList = new ArrayList<Task>();
		ArrayList<Robot> rob = (ArrayList<Robot>) this.getRobotsLibres();
		for(Task task : list) {
			List<Tool> tools = task.getMerchandiseRequired().getTools();
			boolean[] toolsFound = new boolean[tools.size()];
			for(Tool tool : tools) {
				int compteur = 0;
				for(Robot robot : rob)
					if(robot.getTools().contains(tool))
						toolsFound[compteur] = true;
				compteur++;
			}
			
			boolean missingTool = false;
			for(boolean toolFound : toolsFound)
				if(!toolFound)
					missingTool = true;
			
			if(!missingTool)
				taskList.add(task);
		}
		return taskList;
	}
	

	/*
	 * Une t�che se voit accomplie puis effac�e si le robot se trouve � l'emplacement avec la marchandise voulue.
	 * Si l'�quipe n'a pas ex�cut� la t�che dans le temps imparti elle re�oit seulement la r�compense.
	 */

	public void completeTask(){
		for(Robot rob : robotL){
			Task task = rob.getTask();
			if(rob.getPosition() == task.getPosition() && rob.getStock().contains(task.getMerchandiseRequired())){
				rob.getStock().remove(task.getMerchandiseRequired());
				if(task.isTimeOver()){
					this.setMoney(this.money + task.getReward()/2);
				}
				this.setMoney(this.money +task.getReward());
				this.removePartout(task);
			}
		}
	}
	
	/*
	 * Les robots ne sont plus affect�s � la t�che termin�e et cette derni�re n'apparait plus.
	 */
	public void removePartout(Task task){
		for(Robot rob : robotL){
			if(rob.getTask() == task){
				rob.setTask(null);
			}
		}
		this.tasks.remove(task);
	}
	
	/*
	 * M�thode lanc�e r�guli�rement g�rant l'avanc�e de la partie.
	 * Elle s'occupe de toutes les actions que l'�quipe doit effectuer durant un tour,
	 * c'est � dire l'affectation des t�ches, les d�placements des robots, l'achat de marchandises,
	 * la confection des marchandises et la compl�tion des t�ches.
	 */
	public void updateLoop(ArrayList<Task> availableTask) throws Exception {
		int compteur = 0;
		//Affectation des taches
		while(this.getRobotsLibres().size()!=0 && compteur<10) {
			ArrayList<Task> tacheA = this.getPossibleTask(availableTask);
			compteur++;
			Task bestTask = this.WhichTaskCanIDo(tacheA);
			if(bestTask != null) {
				ArrayList<Robot> robotTask = (ArrayList<Robot>) this.getRobotsWithTools(bestTask);
				Robot[] robots = robotTask.toArray(new Robot[robotTask.size()]);
				this.acceptTask(bestTask, robots);
			}
		}
		//Déplacement des robots
		for(Robot rob : this.getRobotL()) {
			rob.setChemins(rob.getDestination()[0],rob.getDestination()[1]);
			rob.findDirection();
			rob.move();
		}
		//Achat marchandise
		for(Task tache : this.tasks) {
			for(Merchandise merch : tache.getMerchandiseRequired().getRawMaterials()) {
				this.buyMerchandise(merch,1);
			}
		}
		
		//Crafting
		for(Task task : this.tasks) {
			this.craftMerchandise(task);
		}
		
		//Complétion des taches 
		this.completeTask();
		
	}

	/*
	 * Permet � un ou plusieurs robots ,positionn�(s) � l'endroit d'un magasin, d'acheter les marchandises requises par la t�che 
	 * � laquelle il est affect� seulement s'il en a le besoin. Met � jour l'inventaire du robot.
	 */
	public void buyMerchandise(Merchandise merch, int quantite) throws Exception{
		for(Robot rob : this.robotL) {
			for(Store store : this.graphe.getStoreList()) {
				if(rob.getPosition() == store.getPosition()) {
					if(rob.getTask().getMerchandiseRequired().getRawMaterials().contains(merch) && !rob.getStock().contains(merch) && store.getMerchandises().get(merch)>quantite){
						for(int i = 0; i<quantite;i++) {
							rob.addToStock(merch);
						}
						rob.buy(merch, quantite, store);
					}
				}
			}
		}		
	}
	
	/*
	 * Lance la confection des objets demand�s par les t�ches pour tout les robots qui sont en position pour le faire.
	 * On v�rifie toutes les conditions pour la confection en d�but de m�thode.
	 */
	public void craftMerchandise(Task task) throws Exception {
		Merchandise merch = task.getMerchandiseRequired();
		ArrayList<Robot> robots = new ArrayList<Robot>();
		boolean craftPossible = true;
		//On garde que les robots qui sont sur la tache
		for (Robot rob : this.robotL) {
			if(rob.getTask().equals(task)) {
				robots.add(rob);
			}
		}
		Point p = robots.get(0).getPosition();
		//On check qu'ils sont tous au même endroit et dans une factory
		boolean position = true;
		for(Robot rob : robots) {
			if(!rob.getPosition().equals(p)) {
				position = false;
			}
		}
		boolean positionFactory = false;
		for(Factory facto : this.graphe.getFactoryList()) {
			if(facto.getPosition().equals(p)) {
				positionFactory = true;
			}
		}
		craftPossible = position && positionFactory;
		//On check pas qu'ils ont les outils vu que les robots ont été choisi grâce aux outils
		//On check qu'ils ont les items
		for(Merchandise material : merch.getRawMaterials()) {
			boolean possessed = false;
			for(Robot rob : robots) {
				if(rob.getStock().contains(material)) {
					possessed = true;
				}
			}
			if(!possessed) {
				craftPossible = false;
			}
		}
		//Si tout est bon on donne l'item craft� en retirant les composants des robots
		if(craftPossible) {
			for(Merchandise material : merch.getRawMaterials()) {
				for(Robot rob : robots) {
					rob.removeMerchandise(material);
				}
			}
			robots.get(0).addToStock(merch);
		}
	}
}