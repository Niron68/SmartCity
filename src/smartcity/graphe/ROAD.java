package smartcity.graphe;

public enum ROAD {
	CROSS("CROSS"),
	HORIZONTAL("HORIZONTAL"),
	VERTICAL("VERTICAL"),
	T_LEFT("T_LEFT"),
	T_RIGHT("T_RIGHT"),
	T_NORMAL("T_NORMAL"),
	T_REVERSE("T_REVERSE"),
	END_LEFT("END_LEFT"),
	END_RIGHT("END_RIGHT"),
	END_TOP("END_TOP"),
	END_BOTTOM("END_BOTTOM"),
	TURN_TL("TURN_TL"),
	TURN_TR("TURN_TR"),
	TURN_BL("TURN_BL"),
	TURN_BR("TURN_BR");
	
	
	private String imagePath;
	
	private ROAD(String imagePath) {
		this.imagePath = "ROAD_" + imagePath + ".png";
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
}
