package smartcity;

import java.util.ArrayList;

import smartcity.graphe.Graphe;
import smartcity.graphe.Hmap;

public class PlaySimulation {

	public static void main(String[] args) {
		Graphe gr = new Graphe(new Hmap(90,4));
		gr.preparerGraphe();
		ArrayList<Integer> chem = gr.findWay(1,545);
		for(int i : chem) System.out.println(i);
	}

}
