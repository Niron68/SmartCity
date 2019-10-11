package smartcity.robot;

public enum Kind {
	CAR("car"), TRUCK("truck"), BIKE("bike"), DRONE("drone"); 
	
	private String type;
	
	Kind(String str) {
		this.type = str;
	}
	
	public String getType() {return this.type;}
	
}
