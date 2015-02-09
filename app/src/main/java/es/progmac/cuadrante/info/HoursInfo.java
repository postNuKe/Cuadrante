package es.progmac.cuadrante.info;

import android.util.SparseIntArray;

public class HoursInfo {	
	private int id;
	private int year;
	private int month;
	private double hours;
	private double reference;
	private double f2;
	private double f2_hours;
	private int guardias;
	
	public HoursInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public HoursInfo(int id, int year, int month, 
			double hours, double reference, double f2, int guardias, double f2_hours){
		_hoursInfo(id, year, month, hours, reference, f2, guardias, f2_hours);
	}
	
	public HoursInfo(int year, int month, 
			double hours, double reference, double f2, int guardias, double f2_hours){
		_hoursInfo(0, year, month, hours, reference, f2, guardias, f2_hours);
	}
	
	private void _hoursInfo(int id, int year, int month, 
			double hours, double reference, double f2, int guardias, double f2_hours){
		setId(id);
		setYear(year);
		setMonth(month);
		setHours(hours);
		setReference(reference);
		setF2(f2);
		setGuardias(guardias);
		setF2Hours(f2_hours);
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public int getYear(){
		return year;
	}
	public void setYear(int year){
		this.year = year;
	}
	
	public int getMonth(){
		return month;
	}
	public void setMonth(int month){
		this.month = month;
	}
	
	public double getHours(){
		return hours;
	}
	public void setHours(double hours){
		this.hours = hours;
	}
	
	public double getReference(){
		return reference;
	}
	public void setReference(double reference){
		this.reference = reference;
	}
	
	public double getF2(){
		return f2;
	}
	public void setF2(double f2){
		this.f2 = f2;
	}
	
	public int getGuardias(){
		return guardias;
	}
	public void setGuardias(int guardias){
		this.guardias = guardias;
	}
	
	public double getF2Hours(){
		return f2_hours;
	}
	public void setF2Hours(double f2_hours){
		this.f2_hours = f2_hours;
	}
	
	public double getF2Percent(){
		double percent = (getF2Hours() * 100)/getHours();
		if(percent > 0) return percent;
		else return 0;
	}
	
}
