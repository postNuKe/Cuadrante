 package es.progmac.cuadrante.info;

public class TurnTypeInfo {
	private int turnId;
	private int typeId;
	private int orden;
	private int saliente;
	private String name;
	
	public TurnTypeInfo(){}
	
	public TurnTypeInfo(int turnId, int typeId, int orden, int saliente){
		_turnTypeInfo(turnId, typeId, orden, saliente, "");
	}
	public TurnTypeInfo(int turnId, int typeId, int orden, boolean saliente){
		int sal = 0;
		if(saliente) sal = 1;
		_turnTypeInfo(turnId, typeId, orden, sal, name);
	}
	public TurnTypeInfo(int turnId, int typeId, int orden, int saliente, String name){
		_turnTypeInfo(turnId, typeId, orden, saliente, name);
	}
	
	private void _turnTypeInfo(int turnId, int typeId, int orden, int saliente, String name){
		this.turnId = turnId;
		this.typeId = typeId;
		this.orden = orden;
		this.saliente = saliente;
		this.name = name;
	}
	
	public int getTurnId(){
		return this.turnId;
	}
	public void setTurnId(int turnId){
		this.turnId = turnId;
	}
	
	public int getTypeId(){
		return this.typeId;
	}
	public void setTypeId(int typeId){
		this.typeId = typeId;
	}
	
	public int getOrden(){
		return this.orden;
	}
	public void setOrden(int orden){
		this.orden = orden;
	}
	
	public int getSaliente(){
		return this.saliente;
	}
	public void setSaliente(int saliente){
		this.saliente = saliente;
	}
	public void switchSaliente(){
		if(this.saliente == 0) setSaliente(1);
		else setSaliente(0);
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
	

}
