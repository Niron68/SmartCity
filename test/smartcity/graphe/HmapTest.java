package smartcity.graphe;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class HmapTest {
	
	Hmap map;
	boolean res;
	boolean res2;
	int count;

	@Test
	public void testRoute() {
		res = true;
		count = 1000;
		for(int i = 0; i < 1000;i++) {
			res2 = true;
			map = new Hmap(27,2.5);
			int[][] maptest = map.getMap();
			for(int x = 0; x < map.getTaille()-2; x++) {
				for(int y = 0; y < map.getTaille()-2; y++) {
					if(maptest[x][y] == 1) {
						if(maptest[x+1][y] == 1)
							if(maptest[x][y+1] == 1)
								if(maptest[x+1][y+1] == 1)  {
									res = false;
									res2 = false;
								}
					}
				}
			}
			if(!res2) count--;
			map.afficher();
			System.out.println( " - " + res2 +"------------------------");
		}
		System.out.println(count/10);
		assertTrue("2 routes sont cote a cote",res);
	}

}
