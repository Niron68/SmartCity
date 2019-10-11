package smartcity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Tool {

	private static HashMap<Integer,Tool> tools = new HashMap<Integer,Tool>();
	
	private int id;
	private String name;
	
	public Tool(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/*
	 * Récupère les outils dans la base de données et les stocks dans l'attribut static tools
	 */
	public static void loadTools() throws SQLException {
		Database db = Database.getInstance();
		PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM Outils");
		ResultSet res = stmt.executeQuery();
		while(res.next())
			tools.put(res.getInt(1), new Tool(res.getInt(1), res.getString(2)));
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static HashMap<Integer,Tool> getTools() {
		return Tool.tools;
	}
	
	@Override 
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		else {
			Tool tool = (Tool) obj;
			return (tool.id == this.id && tool.name.equals(this.name)); 
		}
	}
}
