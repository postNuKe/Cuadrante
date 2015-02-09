package es.progmac.cuadrante.info;

import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.MyLog;


public class HotelInfo {
	private static final String TAG = "HotelInfo";
	private long id;
	private long comision_id;
	private String name;
	private String comments;
	private String start_date;
	private String end_date;
	private String daily_expenses;
	private String manutencion_expenses = "0.0";
	private String laundry;
	private int startY, startM, startD;
	private int endY, endM, endD;
	
	public HotelInfo() {

	}
	
	public HotelInfo(long id2, long comision_id2, String name2, String comments2, 
			String start_date2, String end_date2, String daily_expenses2, 
			String manutencion_expenses2, String laundry) {
		id = id2;
		baseHotelInfo(id2, comision_id2, name2, comments2, start_date2, end_date2, daily_expenses2,
				manutencion_expenses2, laundry);
	}
	
	public HotelInfo(long comision_id2, String name2, String comments2, String start_date2, 
			String end_date2, String daily_expenses2, String manutencion_expenses2, String laundry2) {
		baseHotelInfo(0, comision_id2, name2, comments2, start_date2, end_date2, daily_expenses2, 
				manutencion_expenses2, laundry2);
	}
	
	private void baseHotelInfo(long id2, long comision_id2, String name2, String comments2, 
			String start_date2, String end_date2, String daily_expenses2, 
			String manutencion_expenses2, String laundry2) {
		comision_id = comision_id2;
		name = name2;
		comments = comments2;
		start_date = start_date2;
		end_date = end_date2;
		setStartDate(start_date2);
		setEndDate(end_date2);		
		daily_expenses = daily_expenses2;
		manutencion_expenses = manutencion_expenses2;
		laundry = laundry2;
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
	
	public long getId() {
		return id;
	}

	public void setId(long id2) {
		id = id2;
	}
	
	public long getComisionId() {
		return comision_id;
	}

	public void setComisionId(long comision_id2) {
		comision_id = comision_id2;
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
		return comments;
	}

	public void setComments(String comments2) {
		comments = comments2;
	}
	
	public String getDailyExpenses() {
		return daily_expenses;
	}

	public void setDailyExpenses(String daily_expenses2) {
		daily_expenses = daily_expenses2;
	}
	
	public String getManutencionExpenses() {
		if(manutencion_expenses == null) manutencion_expenses = "0";
		//MyLog.d(TAG, "manutencion:" + manutencion_expenses);
		return manutencion_expenses;
	}

	public void setManutencionExpenses(String manutencion_expenses2) {
		manutencion_expenses = manutencion_expenses2;
	}
	
	public String getLaundry() {
		return laundry;
	}

	public void setLaundry(String laundry2) {
		laundry = laundry2;
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
}
