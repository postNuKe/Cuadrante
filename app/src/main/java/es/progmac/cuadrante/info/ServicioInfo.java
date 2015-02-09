package es.progmac.cuadrante.info;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import android.content.Context;

import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.Sp;


public class ServicioInfo {
	private long id;
	private String date;
	private int type_id = 0;
	private String name = "";
	private Integer is_holiday = 0;
	private Integer is_important = 0;
	private String bg_color = String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR);
	private String text_color = String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR);
	private String start_schedule = Cuadrante.SCHEDULE_NULL;
	private String end_schedule = Cuadrante.SCHEDULE_NULL;
	private String start_schedule2 = Cuadrante.SCHEDULE_NULL;
	private String end_schedule2 = Cuadrante.SCHEDULE_NULL;
	private String start_schedule3 = Cuadrante.SCHEDULE_NULL;
	private String end_schedule3 = Cuadrante.SCHEDULE_NULL;
	private String start_schedule4 = Cuadrante.SCHEDULE_NULL;
	private String end_schedule4 = Cuadrante.SCHEDULE_NULL;
	private String comments = "";
	private Integer guardia_combinada = 0;
	private int type_day = 0;
	private Integer succession_command = 0;
	private int year, month, day;
	
	public ServicioInfo() {

	}
	
	public ServicioInfo(String date, Integer is_holiday){
		this.is_holiday = is_holiday;
		setDate(date);
	}
	
	public ServicioInfo(int id, String date, int type_id, String name, 
			String bg_color, String text_color,
			String start_schedule, String end_schedule,
			String start_schedule2, String end_schedule2,
			String start_schedule3, String end_schedule3,
			String start_schedule4, String end_schedule4,
			String comments, Integer is_holiday, Integer guardia_combinada,
			int type_day, int is_important, Integer succession_command) {
		_servicioInfo(id, date, type_id, name, bg_color, text_color,
				start_schedule, end_schedule, start_schedule2, end_schedule2,
				start_schedule3, end_schedule3, start_schedule4, end_schedule4,
				comments, is_holiday, guardia_combinada, type_day, is_important, succession_command);
	}
	
	public ServicioInfo(String date, int type_id, String name, 
			String bg_color, String text_color,
			String start_schedule, String end_schedule,
			String start_schedule2, String end_schedule2,
			String start_schedule3, String end_schedule3,
			String start_schedule4, String end_schedule4,
			String comments, Integer is_holiday,
			Integer guardia_combinada, int type_day, int is_important, Integer succession_command) {
		_servicioInfo(0, date, type_id, name, bg_color, text_color,
				start_schedule, end_schedule, start_schedule2, end_schedule2,
				start_schedule3, end_schedule3, start_schedule4, end_schedule4,
				comments, is_holiday, guardia_combinada, type_day, is_important, succession_command);
	}
	
	private void _servicioInfo(long id, String date, int type_id, String name, 
			String bg_color, String text_color,
			String start_schedule, String end_schedule,
			String start_schedule2, String end_schedule2,
			String start_schedule3, String end_schedule3,
			String start_schedule4, String end_schedule4,
			String comments, Integer is_holiday, Integer guardia_combinada,
			int type_day, int is_important, Integer succession_command){
		this.id = id;
		this.date = date;
		this.type_id = type_id;
		this.name = name;
		this.is_holiday = is_holiday;
		this.is_important = is_important;
		this.bg_color = bg_color;
		this.text_color = text_color;
		this.start_schedule = start_schedule;
		this.end_schedule = end_schedule;
		this.start_schedule2 = start_schedule2;
		this.end_schedule2 = end_schedule2;
		this.start_schedule3 = start_schedule3;
		this.end_schedule3 = end_schedule3;
		this.start_schedule4 = start_schedule4;
		this.end_schedule4 = end_schedule4;
		this.comments = comments;
		this.guardia_combinada = guardia_combinada;
		this.type_day = type_day;	
		this.succession_command = succession_command;		
	}
	
	/**
	 * Declara los valores por defecto para poner un dia como festivo o no,
	 * es decir, cambia los colores y el valor del holiday
	 * @param isHoliday
	 */
	public void setAsHoliday(boolean isHoliday, Context context){
		if(isHoliday){
			this.is_holiday = 1;
			/*
			this.bg_color = String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY);
			this.text_color = String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY);
			*/
			this.bg_color = Integer.toString(Sp.getHolidayBgColor(context));
			this.text_color = Integer.toString(Sp.getHolidayTextColor(context));
		}else{
			this.is_holiday = 0;
			this.bg_color = String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR);
			this.text_color = String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR);			
		}
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		CuadranteDates temp = new CuadranteDates(date);
		this.year = temp.getYear();
		this.month = temp.getMonth();
		this.day = temp.getDay();
		this.date = date;
	}
	
	public DateTime getDateTime(){
		return new DateTime(this.year, this.month, this.day, 0, 0);
	}
	
	public DateTime getDateTimeEnd(){
		return new DateTime(this.year, this.month, this.day, 23, 59);
	}
	
	public Interval getInterval(){
		return new Interval(getDateTime(), getDateTimeEnd());
	}

	public int getTypeId() {
		return this.type_id;
	}

	public void setTypeId(int type_id) {
		this.type_id = type_id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsHoliday() {
		return this.is_holiday;
	}

	public void setIsHoliday(Integer is_holiday) {
		this.is_holiday = is_holiday;
	}

	public Integer getIsImportant() {
		return this.is_important;
	}

	public void setIsImportant(Integer is_important) {
		this.is_important = is_important;
	}

	public String getBgColor() {
		return this.bg_color;
	}

	public void setBgColor(String bg_color) {
		this.bg_color = bg_color;
	}

	public String getTextColor() {
		return this.text_color;
	}

	public void setTextColor(String text_color) {
		this.text_color = text_color;
	}

	public String getStartSchedule() {
		return this.start_schedule;
	}

	public void setStartSchedule(String start_schedule) {
		this.start_schedule = start_schedule;
	}

	public String getEndSchedule() {
		return this.end_schedule;
	}

	public void setEndSchedule(String end_schedule) {
		this.end_schedule = end_schedule;
	}

	public String getStartSchedule2() {
		return this.start_schedule2;
	}

	public void setStartSchedule2(String start_schedule2) {
		this.start_schedule2 = start_schedule2;
	}

	public String getEndSchedule2() {
		return this.end_schedule2;
	}

	public void setEndSchedule2(String end_schedule2) {
		this.end_schedule2 = end_schedule2;
	}

	public String getStartSchedule3() {
		return this.start_schedule3;
	}

	public void setStartSchedule3(String start_schedule3) {
		this.start_schedule3 = start_schedule3;
	}

	public String getEndSchedule3() {
		return this.end_schedule3;
	}

	public void setEndSchedule3(String end_schedule3) {
		this.end_schedule3 = end_schedule3;
	}

	public String getStartSchedule4() {
		return this.start_schedule4;
	}

	public void setStartSchedule4(String start_schedule4) {
		this.start_schedule4 = start_schedule4;
	}

	public String getEndSchedule4() {
		return this.end_schedule4;
	}

	public void setEndSchedule4(String end_schedule4) {
		this.end_schedule4 = end_schedule4;
	}
	
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Integer getGuardiaCombinada() {
		return this.guardia_combinada;
	}

	public void setGuardiaCombinada(Integer guardia_combinada) {
		this.guardia_combinada = guardia_combinada;
	}
	
	public int getTypeDay() {
		return this.type_day;
	}

	public void setTypeDay(int type_day) {
		this.type_day = type_day;
	}
	
	public Integer getSuccessionCommand() {
		return this.succession_command;
	}

	public void setSuccessionCommand(Integer succession_command) {
		this.succession_command = succession_command;
	}
	
	public int getDay() {
		return this.day;
	}

	public int getYear() {
		return this.year;
	}

	public int getMonth() {
		return this.month;
	}
	
	public boolean isServiceEndNextDay(){
		return Cuadrante.isServiceEndNextDay(start_schedule, end_schedule, 
				start_schedule2, end_schedule2, start_schedule3, end_schedule3, 
				start_schedule4, end_schedule4);
	}
}
