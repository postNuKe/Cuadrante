 package es.progmac.cuadrante.info;

public class TurnInfo {
	private int id;
	private String name;
	
	public TurnInfo(){}
	
	public TurnInfo(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public TurnInfo(String name){
		this.id =  0;
		this.name = name;
	}
	
	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
}
