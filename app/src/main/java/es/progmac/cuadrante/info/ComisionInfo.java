package es.progmac.cuadrante.info;

import org.joda.time.Interval;

import es.progmac.cuadrante.lib.CuadranteDates;

import android.util.Log;


public class ComisionInfo {
	private long id;
	private String name;
	private String comments;
	private String start_date;
	private String end_date;
	private int view_total_expenses;
	private int startY, startM, startD;
	private int endY, endM, endD;
	Interval interval;
	
	public ComisionInfo() {

	}
	
	public ComisionInfo(long id2, String name2, String comments2, 
			String start_date2, String end_date2, int view_total_expenses2) {
		id = id2;
		_ComisionInfo(id2, name2, comments2, start_date2, end_date2, view_total_expenses2);
	}
	
	public ComisionInfo(String name2, String comments2, 
			String start_date2, String end_date2, int view_total_expenses2) {
		_ComisionInfo(0, name2, comments2, start_date2, end_date2, view_total_expenses2);
	}
	
	private void _ComisionInfo(long id2, String name2, String comments2, 
			String start_date2, String end_date2, int view_total_expenses2) {
		name = name2;
		comments = comments2;
		start_date = start_date2;
		end_date = end_date2;
		setStartDate(start_date2);
		setEndDate(end_date2);	
		setViewTotalExpenses(view_total_expenses2);	
		setInterval();
	}
	/**
	 * @param start_date2 formato yyyy-MM-dd
	 */
	public void setStartDate(String start_date2) {
		CuadranteDates temp = new CuadranteDates(start_date2);
		startY = temp.getYear();
		startM = temp.getMonth();
		startD = temp.getDay();
		start_date = start_date2;
	}
	/**
	 * @param end_date2 formato yyyy-MM-dd
	 */
	public void setEndDate(String end_date2) {
		CuadranteDates temp = new CuadranteDates(end_date2);
		endY = temp.getYear();
		endM = temp.getMonth();
		endD = temp.getDay();
		end_date = end_date2;
	}
	
	private void setInterval(){
		CuadranteDates temp = new CuadranteDates(start_date);
		interval = new Interval(
				new CuadranteDates(start_date).getDateTime(), 
				//tenemos que sumarle un dia para que en el calendario se 
				//muestre el última dia de la comisión correctamente
				new CuadranteDates(end_date).getDateTime().plusDays(1) 
				);
	}
	
	public Interval getInterval(){
		return interval;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id2) {
		id = id2;
	}

	public String getStartDate() {
		return start_date;
	}
	
	public String getEndDate() {
		return end_date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name2) {
		name = name2;
	}
	
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments2) {
		comments = comments2;
	}

	public int getStartDay() {
		return startD;
	}
	
	public int getStartMonth() {
		return startM;
	}
	
	public int getStartYear() {
		return startY;
	}
	
	public int getEndDay() {
		return endD;
	}
	
	public int getEndMonth() {
		return endM;
	}
	
	public int getEndYear() {
		return endY;
	}
	
	public int getViewTotalExpenses() {
		return view_total_expenses;
	}		
	public void setViewTotalExpenses(int view_total_expenses2) {
		view_total_expenses = view_total_expenses2;
	}	
	
}
