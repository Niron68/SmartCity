package smartcity.graphe;

public class Arc implements Comparable<Arc>{

	int indexInitial, indexFinal;
	int distance;
	
	public Arc(int indexPointInitial, int indexPointFinal, int poids) {
		this.indexInitial = indexPointInitial;
		this.indexFinal = indexPointFinal;
		this.distance = poids;
	}

	public int getDistance() 
	{
		return this.distance;
	}
	
	@Override
	public String toString() 
	{
		return "(" + this.indexInitial + ";" + this.indexFinal + ";" + this.distance +")";
	}
	
	public boolean isSuc(int indexI) {
		if (this.indexInitial == indexI) {return true;}
		return false;
	}
	
	public boolean isPred(int indexF) {
		if (this.indexFinal == indexF) {return true;}
		return false;
	}

	@Override
	public int compareTo(Arc arg0) {
		
		int compareIndex = ((Arc) arg0).indexFinal; 
		
		return this.indexFinal - compareIndex;
	}
	
}
