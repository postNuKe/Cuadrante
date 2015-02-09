package es.progmac.cuadrante.info;



import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;

public class TipoServicioInfo{
	private int id;
	private String title;
	private String name;
	private Integer is_date_range;
	private String bg_color;
	private String text_color;
	private String start_schedule;
	private String end_schedule;
	private String start_schedule2;
	private String end_schedule2;
	private String start_schedule3;
	private String end_schedule3;
	private String start_schedule4;
	private String end_schedule4;
	private Integer guardia_combinada;
	private int type_day;
	private Integer succession_command;
	private Integer ask_schedule;
	
	public TipoServicioInfo() {

	}
	
	public TipoServicioInfo(int id, String title, String name, 
			Integer is_date_range, String bg_color, String text_color,
			String start_schedule, String end_schedule,
			String start_schedule2, String end_schedule2,
			String start_schedule3, String end_schedule3,
			String start_schedule4, String end_schedule4, 
			Integer guardia_combinada, int type_day, 
			Integer succession_command, Integer ask_schedule) {
		this.id = id;
		this.title = title;
		this.name = name;
		this.setIsDateRange(is_date_range);
		this.bg_color = bg_color;
		this.text_color = text_color;
		this.start_schedule = CuadranteDates.verifySchedule(start_schedule);
		this.end_schedule = CuadranteDates.verifySchedule(end_schedule);
		this.start_schedule2 = CuadranteDates.verifySchedule(start_schedule2);
		this.end_schedule2 = CuadranteDates.verifySchedule(end_schedule2);
		this.start_schedule3 = CuadranteDates.verifySchedule(start_schedule3);
		this.end_schedule3 = CuadranteDates.verifySchedule(end_schedule3);
		this.start_schedule4 = CuadranteDates.verifySchedule(start_schedule4);
		this.end_schedule4 = CuadranteDates.verifySchedule(end_schedule4);
		this.guardia_combinada = guardia_combinada;
		this.type_day = type_day;
		this.succession_command = succession_command;
		this.ask_schedule = ask_schedule;
	}
	
	public TipoServicioInfo(String title, String name, Integer is_date_range, 
			String bg_color, String text_color,
			String start_schedule, String end_schedule,
			String start_schedule2, String end_schedule2,
			String start_schedule3, String end_schedule3,
			String start_schedule4, String end_schedule4, 
			Integer guardia_combinada, int type_day, 
			Integer succession_command, Integer ask_schedule) {
		this.title = title;
		this.name = name;
		this.setIsDateRange(is_date_range);
		this.bg_color = bg_color;
		this.text_color = text_color;
		this.start_schedule = CuadranteDates.verifySchedule(start_schedule);
		this.end_schedule = CuadranteDates.verifySchedule(end_schedule);
		this.start_schedule2 = CuadranteDates.verifySchedule(start_schedule2);
		this.end_schedule2 = CuadranteDates.verifySchedule(end_schedule2);
		this.start_schedule3 = CuadranteDates.verifySchedule(start_schedule3);
		this.end_schedule3 = CuadranteDates.verifySchedule(end_schedule3);
		this.start_schedule4 = CuadranteDates.verifySchedule(start_schedule4);
		this.end_schedule4 = CuadranteDates.verifySchedule(end_schedule4);
		this.guardia_combinada = guardia_combinada;
		this.type_day = type_day;
		this.succession_command = succession_command;
		this.ask_schedule = ask_schedule;
	}
	
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Nombre largo
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}
	/**
	 * Nombre largo
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Abreviatura
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Abreviatura
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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

	public Integer getIsDateRange() {
		return is_date_range;
	}

	public void setIsDateRange(Integer is_date_range) {
		this.is_date_range = is_date_range;
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
	
	/**
	 * 0 dia ordinario, 
	 * 1 día ordinario no computable 5.35 Bajas, vacaciones ordinarias, permisos incorporación, etc,
	 * 2 día especial no computable 7.5 Permisos "urgentes", Asuntos particulares y Permisos de SS o Navidad
	 * 3 día no computable (-1 día mensual)
	 * @param type_day
	 */
	public void setTypeDay(int type_day) {
		this.type_day = type_day;
	}
	
	public Integer getSuccessionCommand() {
		return this.succession_command;
	}

	public void setSuccessionCommand(Integer succession_command) {
		this.succession_command = succession_command;
	}
	
	public Integer getAskSchedule() {
		return ask_schedule;
	}

	public void setAskSchedule(Integer ask_schedule) {
		this.ask_schedule = ask_schedule;
	}	
	
	/** 
	 * Determina si el servicio termina al dia siguiente 
	 * return boolean
	 * */
	public boolean isServiceEndNextDay(){
		return Cuadrante.isServiceEndNextDay(start_schedule, end_schedule, 
				start_schedule2, end_schedule2, start_schedule3, end_schedule3, 
				start_schedule4, end_schedule4);
	}	

}