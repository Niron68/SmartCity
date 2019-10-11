package smartcity.graphe;
import java.util.Random;

public class Hmap {
	private int[][][] hmap;
	private int taille;
	private boolean res;
	public int[][][] getHmap() {
		return hmap;
	}

	public void setHmap(int z, int x, int y, int value) {
		this.hmap[z][x][y] = value;
	}

	public int getTaille() {
		return taille;
	}

	public void setTaille(int taille) {
		this.taille = taille;
	}

	public int getNbcell() {
		return nbcell;
	}

	public void setNbcell(int nbcell) {
		this.nbcell = nbcell;
	}

	public double getDens() {
		return dens;
	}

	public void setDens(double dens) {
		this.dens = dens;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int x, int y, int value) {
		this.map[x][y] = value;
	}

	private int nbcell;
	private double dens;
	public int[][] map;
	
	public Hmap(int taille, double dens) {
		//this.res = true;
		//do{
		
		if(taille % 2 != 0 && taille % 3 != 0) {
			taille = 26;
			this.taille = 26;
		}
		
		this.dens = dens;
		this.taille = taille;
		
		if(taille % 2 == 0) {
			this.nbcell = 4;
			taille = taille/2;
			this.hmap = new int[this.nbcell][taille][taille];
		}else if(taille % 3 == 0) {
			taille = taille/3;
			this.nbcell = 9;
			this.hmap = new int[this.nbcell][taille][taille];
		}
		
		for(int i = 0; i < this.nbcell; i++) {
			for(int j = 0; j < this.taille/Math.sqrt(this.nbcell);j++) {
				for(int k = 0; k < this.taille/Math.sqrt(this.nbcell);k++) {
					this.hmap[i][j][k] = 0;
				}
			}
		}
		
		for(int i = 0;i < this.nbcell;i++) {
			for(int j = 0;j < taille;j++) {
				this.hmap[i][j][taille-1] = 1;
				this.hmap[i][taille-1][j] = 1;
			}
		}
		
		int[][] ncell = new int[3][3];
		if(this.nbcell == 9) {
			ncell[0][0] = 0;
			ncell[1][0] = 1;
			ncell[2][0] = 2;
			ncell[0][1] = 3;
			ncell[1][1] = 4;
			ncell[2][1] = 5;
			ncell[0][2] = 6;
			ncell[1][2] = 7;
			ncell[2][2] = 8;
		}else {
			ncell[0][0] = 0;
			ncell[1][0] = 1;
			ncell[0][1] = 2;
			ncell[1][1] = 3;
		}
		
		//Mettre le peripherique
		if(this.nbcell == 4) {
			for(int i = 0; i < taille; i++) {
				this.hmap[0][0][i] = 1;
				this.hmap[0][i][0] = 1;
				this.hmap[1][i][0] = 1;
				this.hmap[1][taille-1][i] = 1;
				this.hmap[3][taille-1][i] = 1;
				this.hmap[1][i][taille-1] = 1;
				this.hmap[3][i][taille-1] = 1;
				this.hmap[0][taille-1][i] = 1;
				this.hmap[2][0][i] = 1;
				this.hmap[2][taille-1][i] = 1;
			}
		}else if(this.nbcell == 9) {
			for(int i = 0; i < taille; i++) {
				this.hmap[0][0][i] = 1;
				this.hmap[3][0][i] = 1;
				this.hmap[6][0][i] = 1;
				this.hmap[0][i][0] = 1;
				this.hmap[1][i][0] = 1;
				this.hmap[2][i][0] = 1;
				this.hmap[2][taille-1][i] = 1;
				this.hmap[5][taille-1][i] = 1;
				this.hmap[8][taille-1][i] = 1;
				for(int j = 0; j < 9; j++) {
					this.hmap[j][i][taille-1] = 1;
					this.hmap[j][taille-1][i] = 1;
				}
			}
		}
		
		
		for(int i = 0;i < this.nbcell;i++) {
			
			int nbroute = 0;
			
			while(nbroute < this.taille*this.dens/Math.sqrt(this.nbcell)) {
				
				int posx = this.rand(0,taille-1);
				int posy = this.rand(0, taille-1);
				if(this.hmap[i][posx][posy] == 1) {
					int dir = rand(1,4);
					if(dir == 1 && posx < taille - 2 && posy != 0 && posx > 0) {
						try {
							int tm = taille - posx;
							int tailler = this.rand(1, tm);
							boolean faire = true;
							
							for(int j = 0; j < tailler; j++) {
								try {
									if(this.hmap[i][posx+j+1][posy+1] == 1 && this.hmap[i][posx+j][posy+1] == 1)
										faire =  false;
									if(this.hmap[i][posx+j+1][posy-1] == 1 && this.hmap[i][posx+j][posy-1] == 1)
										faire =  false;
								}catch(Exception e) {
									
								}
							}
							
							
							if(faire) {
								for(int j = 0; j < tailler; j++) {
									try {
										this.hmap[i][posx + j][posy] = 1;
									}catch(Exception e) {
										
									}
								}
							
								nbroute++;
							}
						}catch(Exception e) {
							
						}
						
					}else if(dir == 2 && posy < taille - 2 && posx != 0 && posy > 1) {
						try {
							int tm = taille - posy;
							int tailler = this.rand(1, tm);
							boolean faire = true;
							
							for(int j = 0; j < tailler; j++) {
								try {
									if(this.hmap[i][posx-1][posy+j+1] == 1 && this.hmap[i][posx-1][posy+j] == 1)
										faire =  false;
									if(this.hmap[i][posx+1][posy+j+1] == 1 && this.hmap[i][posx+1][posy+j] == 1)
										faire =  false;
								}catch(Exception e) {
									
								}
							}
							
							
							if(faire) {
								for(int j = 0; j < tailler; j++) {
									try {
										this.hmap[i][posx][posy + j] = 1;
									}catch(Exception e) {
										
									}
								}
								
								nbroute++;
							}
						}catch(Exception e) {
							
						}
					}else if(dir == 3 && posx > 0 && posy > 1 && posy < taille - 2) {
						try {
							int tm = taille - (taille - posx);
							int tailler = this.rand(1, tm);
							boolean faire = true;
							
							for(int j = 0; j < tailler; j++) {
								try {
									if(this.hmap[i][posx-j-1][posy+1] == 1 && this.hmap[i][posx-j][posy+1] == 1)
										faire =  false;
									if(this.hmap[i][posx-j-1][posy-1] == 1 && this.hmap[i][posx-j][posy-1] == 1)
										faire =  false;
								}catch(Exception e) {
									
								}
							}
							
							
							if(faire) {
								for(int j = 0; j < tailler; j++) {
									try {
										this.hmap[i][posx - j][posy] = 1;
									}catch(Exception e) {
										
									}
								}
								
								nbroute++;
							}
						}catch(Exception e) {
							
						}
					}else if(dir == 4 && posy > 0 && posx > 1 && posx < taille - 2) {
						try {
							int tm = taille - (taille - posy);
							int tailler = this.rand(1, tm);
							boolean faire = true;
							
							for(int j = 0; j < tailler; j++) {
								try {
									if(this.hmap[i][posx-1][posy-j-1] == 1 && this.hmap[i][posx-1][posy-j] == 1)
										faire =  false;
									if(this.hmap[i][posx+1][posy-j-1] == 1 && this.hmap[i][posx+1][posy-j] == 1)
										faire =  false;
								}catch(Exception e) {
									
								}
							}
							
							
							if(faire) {
								for(int j = 0; j < tailler; j++) {
									try {
												this.hmap[i][posx][posy - j] = 1;
									}catch(Exception e) {
										
									}
								}
								
								nbroute++;
							}
						}catch(Exception e) {
							
						}
					}
				}
			}
			
		}
		
		
		
		this.map = new int[this.taille][this.taille];
				
		
		
		for(int i = 0;i < this.taille; i++) {
			for(int j = 0;j < this.taille; j++) {
				int div = this.taille/(int)Math.sqrt(this.nbcell);
				int celly = i/div;
				int cellx = j/div;
				this.map[i][j] = this.hmap[ncell[cellx][celly]][(j)%div][(i)%div];
			}
		}
		
		/*
		for(int i = 0;i < this.taille; i++) {
			this.map[i][0] = 1;
			this.map[0][i] = 1;
			this.map[i][this.taille -1] = 1;
			this.map[this.taille-1][i] = 1;
		}
		*/
	
		
	}
	
	private int rand(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	public void afficher() {
		for(int i = 0;i< this.taille; i++) {
			System.out.print(i+" ");
			for(int j = 0; j < this.taille; j++) {
				System.out.print(this.map[i][j]);
			}
			System.out.println();
		}
	}
	
	public void placerBornes() {
		int nbtry = 0;
		while(nbtry <= this.taille) {
			int posx = rand(0,this.taille-8);
			int posy = rand(0,this.taille-10);
			boolean nplace = true;
			for(int i = 0;i < 8;i++) {
				for(int j = 0;j < 10; j++) {
					if(this.map[posx + i][posy + j] == 2) {
						nplace = false;
						nbtry++;
					}
				}
			}
			
			while(nplace) {
				int posx2 = rand(posx,posx+7);
				int posy2 = rand(posy,posy+9);
				if(this.map[posx2][posy2] == 0 && (this.map[posx2+1][posy2] == 1 || this.map[posx2-1][posy2] == 1 || this.map[posx2][posy2+1] == 1 || this.map[posx2][posy2-1] == 1)) {
					this.map[posx2][posy2] = 2;
					nplace = false;
				}
			}
		}
	}
	
	public Hmap(int taille) {
		this.taille = taille;
		this.dens = 0;
		this.nbcell = 0;
		for(int j = 0; j < this.taille/Math.sqrt(this.nbcell);j++) {
			for(int k = 0; k < this.taille/Math.sqrt(this.nbcell);k++) {
				this.map[j][k] = 0;
			}
		}
	}
	
	public boolean placerRoute(Point p1, Point p2) {
		boolean res = false;
		if(p1.getX() == p2.getX()) {
			if(p1.getY()>p2.getY()) {
				for(int i = 0; i < (p1.getY() - p2.getY()); i++) {
					this.map[p1.getX()][p2.getY()+i] = 1;
				}
				res = true;
			}else {
				for(int i = 0; i < (p2.getY() - p1.getY()); i++) {
					this.map[p1.getX()][p1.getY()+i] = 1;
				}
				res = true;
			}
		}
		if(p1.getY() == p2.getY()) {
			if(p1.getX()>p2.getX()) {
				for(int i = 0; i < (p1.getX() - p2.getX()); i++) {
					this.map[p2.getX()+i][p1.getY()] = 1;
				}
				res = true;
			}else {
				for(int i = 0; i < (p2.getX() - p1.getX()); i++) {
					this.map[p1.getX()+i][p1.getY()] = 1;
				}
				res = true;
			}
		}
		return res;
	}
	
	public void placerBorne(Point point) {
		this.map[point.getX()][point.getY()] = 2;
	}
	
	public void placerEntrepot(int nombre) {
		for(int i = 0; i < nombre; i++) {
			boolean placer = false;
			while(!placer) {
				int posx2 = rand(0,this.taille-1);
				int posy2 = rand(0,this.taille-1);
				if(this.map[posx2][posy2] == 0 && (this.map[posx2+1][posy2] == 1 || this.map[posx2-1][posy2] == 1 || this.map[posx2][posy2+1] == 1 || this.map[posx2][posy2-1] == 1)) {
					this.map[posx2][posy2] = 3;
					placer = true;
				}
			}
		}
	}
	
	public void placerEntrepot(Point point) {
		this.map[point.getX()][point.getY()] = 3;
	}
	
	public void placerMagasin(int nombre) {
		for(int i = 0; i < nombre; i++) {
			boolean placer = false;
			while(!placer) {
				int posx2 = rand(0,this.taille-1);
				int posy2 = rand(0,this.taille-1);
				if(this.map[posx2][posy2] == 0 && (this.map[posx2+1][posy2] == 1 || this.map[posx2-1][posy2] == 1 || this.map[posx2][posy2+1] == 1 || this.map[posx2][posy2-1] == 1)) {
					this.map[posx2][posy2] = 4;
					placer = true;
				}
			}
		}
	}
	
	public void placerUsine(int nombre) {
		for(int i = 0; i < nombre; i++) {
			boolean placer = false;
			while(!placer) {
				int posx2 = rand(0,this.taille-1);
				int posy2 = rand(0,this.taille-1);
				if(this.map[posx2][posy2] == 0 && (this.map[posx2+1][posy2] == 1 || this.map[posx2-1][posy2] == 1 || this.map[posx2][posy2+1] == 1 || this.map[posx2][posy2-1] == 1)) {
					this.map[posx2][posy2] = 5;
					placer = true;
				}
			}
		}
	}
	
	
}
