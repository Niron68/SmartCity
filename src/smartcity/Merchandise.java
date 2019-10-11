package smartcity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Merchandise {
	
	private static HashMap<Integer,Merchandise> merchandises = new HashMap<>();

	private int id;
	private String name;
	private int volume;
	private int price;
	private ArrayList<Tool> tools = new ArrayList<Tool>();
	private ArrayList<Merchandise> rawMaterials = new ArrayList<>();
	
	public Merchandise(int id, String name, int volume, int price) {
		this.id = id;
		this.name = name;
		this.volume = volume;
		this.price = price;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getVolume() {
		return this.volume;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public List<Tool> getTools() {
		return this.tools;
	}
	
	public List<Merchandise> getRawMaterials() {
		return this.rawMaterials;
	}
	
	public static HashMap<Integer,Merchandise> getMerchandises() {
		return Merchandise.merchandises;
	}
	
	/*
	 * Récupère les marchandises dans la base de données et les stock dans l'attribut static merchandises.
	 */
	public static void loadMerchandises() throws SQLException {
		Database db = Database.getInstance();
		PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM Marchandise");
		ResultSet res = stmt.executeQuery();
		while(res.next())
			merchandises.put(res.getInt(1), new Merchandise(res.getInt(1), res.getString(4), res.getInt(2), res.getInt(3)));
		
		stmt = db.getConnection().prepareStatement("SELECT * FROM Composer");
		res = stmt.executeQuery();
		while(res.next())
			for(int i = 0; i < res.getInt(3); i++)
				merchandises.get(res.getInt(1)).getRawMaterials().add(merchandises.get(res.getInt(2)));
		
		if(Tool.getTools().size() == 0)
			Tool.loadTools();
		stmt = db.getConnection().prepareStatement("SELECT * FROM Necessiter");
		res = stmt.executeQuery();
		while(res.next())
			merchandises.get(res.getInt(2)).getTools().add(Tool.getTools().get(res.getInt(1)));
	}
	
	@Override 
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		else {
			Merchandise merch = (Merchandise) obj;
			return this.id == merch.id && this.name.equals(merch.name);
		}
	}
}
