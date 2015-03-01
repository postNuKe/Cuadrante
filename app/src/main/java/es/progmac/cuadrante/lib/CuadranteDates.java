package es.progmac.cuadrante.lib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import es.progmac.cuadrante.R;

import android.content.Context;
import android.util.Log;

/**
 * Clase para el manejo de una fecha en formato YYYY-mm-dd
 * @author david4
 *
 */
public class CuadranteDates {
    public static final String FIRST_DAY = "firstDay";
    public static final String LAST_DAY = "lastDay";
	private static final String TAG = "CuadranteDates";
	private int year = 0, month = 0, day = 0;

	String date = "";
	private DateTime dt;
	/**
	 * Constructor para el manejo de una fecha en formato Y-MM-dd
	 * @param date Y-MM-dd
	 */
	public CuadranteDates(String date) {
		if(date.length() == 0){
			DateTime today = new DateTime().withMillisOfDay(0);
			_GRSDates(today);
		}else{
			String[] temp = date.split("-");
			this.year = Integer.valueOf(temp[0]);
			this.month = Integer.valueOf(temp[1]);
			this.day = Integer.valueOf(temp[2]);
			this.date = date;		
			this.dt = new DateTime(this.year, this.month, this.day, 0, 0);
		}
	}
	
	
	public CuadranteDates(DateTime date){
		_GRSDates(date);
	}
	
	private void _GRSDates(DateTime date){
		this.year = date.getYear();
		this.month = date.getMonthOfYear();
		this.day = date.getDayOfMonth();
		this.date = CuadranteDates.formatDate(date);
		this.dt = date;		
	}
	
	public CuadranteDates setTime(int hour, int minute){
		this.dt = dt.withHourOfDay(hour).withMinuteOfHour(minute);
		return this;
	}
	
	public int getYear(){
		return this.year;
	}
	
	public int getMonth(){
		return this.month;
	}
	
	public int getDay(){
		return this.day;
	}

    /**
     * Devuelve el dia de una fecha en formato YYYY-mm-dd
     * @param date
     * @return
     */
    public static int getDay(String date){
        CuadranteDates dt = new CuadranteDates(date);
        return dt.getDay();
    }
    /**
     * Devuelve el mes de una fecha en formato YYYY-mm-dd
     * @param date
     * @return
     */
    public static int getMonth(String date){
        CuadranteDates dt = new CuadranteDates(date);
        return dt.getMonth();
    }
    /**
     * Devuelve el año de una fecha en formato YYYY-mm-dd
     * @param date
     * @return
     */
    public static int getYear(String date){
        CuadranteDates dt = new CuadranteDates(date);
        return dt.getYear();
    }

	public String getDate(){
		return this.date;
	}
	
	public DateTime getDateTime(){
		return this.dt;
	}
	
	/**
	 * Devuelve los días entre dos fechas en formato yyyy-mm-dd
	 * @param date1 formato yyyy-MM-dd
	 * @return date2 devuelve el número de dias, negativo es que date1 es mayor
	 * que date2
	 */
	public static int getDaysBetweenDates(String date1, String date2){
		CuadranteDates dt1 = new CuadranteDates(date1);
		CuadranteDates dt2 = new CuadranteDates(date2);
		Days d = Days.daysBetween(dt1.getDateTime(), dt2.getDateTime());
		return d.getDays();	
	}
	
	/**
	 * Devuelve la fecha actual en formato YYYY-MM-dd
	 * @return
	 */
	public static String getFormatDate(){
		CuadranteDates dtToday = new CuadranteDates("");
		return formatDate(dtToday.getYear(), dtToday.getMonth(), dtToday.getDay());
	}
	
	/**
	 * Crea una cadena de fecha en formato yyyy-mm-dd
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String formatDate(int year, int month, int day){
		return year + "-" + pad(month) + "-" + pad(day);
	}
	/**
	 * Crea una cadena de fecha en formato yyyy-MM-dd
	 * @param dt Datetime
	 * @return String yyyy-MM-dd
	 */
	public static String formatDate(DateTime dt){
		return formatDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
	}
    /**
     * Crea una cadena de fecha en formato yyyy-MM-dd
     * @param calendar
     * @return String yyyy-MM-dd
     */
    public static String formatDate(Calendar calendar){
        return formatDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }
	/**
	 * Crea una cadena de fecha en formato dd-MM-yyyy
	 * @param date formato yyyy-MM-dd
	 * @return String dd-MM-yyyy
	 */
	public static String formatDateToHumans(String date){
		CuadranteDates dt = new CuadranteDates(date);
		return formatDateToHumans(dt.getDay(), dt.getMonth(), dt.getYear());		
	}
	/**
	 * Crea una cadena de fecha en formato dd-MM-yyyy
	 * @param dt
	 * @return String dd-MM-yyyy
	 */
	public static String formatDateToHumans(DateTime dt){
		return formatDateToHumans(dt.getDayOfMonth(), dt.getMonthOfYear(), dt.getYear());		
	}
	/**
	 * Crea una cadena de fecha en formato dd-MM-yyyy
	 * @return String dd-MM-yyyy
	 */
	public String formatDateToHumans(){
		return formatDateToHumans(this.getDay(), this.getMonth(),this.getYear());
	}
	/**
	 * Crea una cadena de fecha en formato dd-MM-yyyy
	 * @param day
	 * @param month
	 * @param year
	 * @return String dd-MM-yyyy
	 */
	public static String formatDateToHumans(int day, int month, int year){
		return  pad(day) + "-" + pad(month) + "-" + year;		
	}
	/**
	 * Crea una cadena de fecha en formato dd de MM
	 * @return dd de MM
	 */
	public String formatDateToHumans2(){
		String month;
		if(this.dt != null){
			month = this.dt.monthOfYear().getAsText();
			return this.getDay() + " de " + month;
		}
		return null;
	}	
	/**
	 * Crea una cadena de fecha en formato dd de MM
	 * @param date yyyy-MM-dd
	 * @return String dd de MM
	 */
	public static String formatDateToHumans2(String date){
		CuadranteDates dt = new CuadranteDates(date);
		return dt.formatDateToHumans2();		
	}
	/**
	 * Crea una cadena de fecha en formato dd de MM de yyyy
	 * @return String dd de MM de yyyy
	 */
	public String formatDateToHumans3(){
		String month;
		if(this.dt != null){
			month = this.dt.monthOfYear().getAsText();
			return this.getDay() + " de " + month + " de " + dt.getYear();
		}
		return null;
	}
	/**
	 * Crea una cadena de fecha en formato dd de MM de yyyy
	 * @param date yyyy-MM-dd
	 * @return String dd de MM de yyyy
	 */
	public static String formatDateToHumans3(String date){
		CuadranteDates dt = new CuadranteDates(date);
		return dt.formatDateToHumans3();		
	}

    /**
     * Crea una cadena de fecha en formato dd MM yyyy
     * @param date
     * @return 02 Febrero 2015
     */
    public static String formatDateToHumans4(String date){
        CuadranteDates cDate = new CuadranteDates(date);
        return cDate.getDateTime().toString("dd MMMM YYYY");
    }
	
	/**
	 * devuelve la fecha en DateTime, sobre una fecha en formato yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static DateTime getDateTime(String date){
		CuadranteDates splitDate = new CuadranteDates(date);
		DateTime dt = new DateTime(splitDate.getYear(), splitDate.getMonth(), splitDate.getDay(), 0, 0);
		return dt;
	}
	/**
	 * Genera en formato cadena la hora, 16:30
	 * 
	 * @param hourOfDay
	 * @param minuteOfHour
	 * @return string
	 */
	public static String formatTime(int hourOfDay, int minuteOfHour) {
		return pad(hourOfDay) + ':' + pad(minuteOfHour);
	}
	/**
	 * Genera en formato cadena la hora, 16:30
	 * @param dt
	 * @return
	 */
	public static String formatTime(DateTime dt){
		return formatTime(dt.getHourOfDay(), dt.getMinuteOfHour());
	}
	
	/**
	 * Genera una cadena con la fecha en formato Y-MM-dd HH:mm
	 * @param dt
	 * @return yY-MM-dd HH:mm
	 */
	public static String formatDateTime(DateTime dt){
		//return formatDate(dt) + " " + formatTime(dt);
		return dt.toString("Y-MM-dd HH:mm");
	}
	
	/**
	 * Devuelve un objeto DateTime sobre una cadena de fecha Y-MM-dd HH:mm
	 * @param date en formato Y-MM-dd HH:mm
	 * @return DateTime
	 */
	public static DateTime getDateTimefromDate(String date){
		String[] temp = date.split("-");
		String[] temp2 = temp[2].split(" ");
		String[] temp3 = temp2[1].split(":");
		return new DateTime(
				Integer.valueOf(temp[0]), 
				Integer.valueOf(temp[1]), 
				Integer.valueOf(temp2[0]), 
				Integer.valueOf(temp3[0].trim()), 
				Integer.valueOf(temp3[1]));
	}

	/**
	 * Añade el 0 a los números inferiores a 10
	 * 
	 * @param c
	 * @return string
	 */
	public static final String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	/**
	 * Devuelve la hora de un horario en formato 00:00
	 * @param time horario en formato string
	 * @return int horas
	 */
	public static int getHour(String time){
		String[] temp;
		if(time.length() == 5){
			temp = time.split(":");
			time = temp[0];
		}else{
			time = "0";
		}
		return Integer.valueOf(time);		
	}
	
	/**
	 * Devuelve los minutos de un horario en formato 00:00
	 * @param time horario en formato string
	 * @return int minutos
	 */
	public static int getMinutes(String time){
		String[] temp;
		if(time.length() == 5){
			temp = time.split(":");
			time = temp[1];
		}else{
			time = "0";
		}
		return Integer.valueOf(time);		
	}
	
	/**
	 * Si una hora es null devuelve 00:00
	 * @param schedule
	 * @return
	 */
	public static String verifySchedule(String schedule){
		if(schedule == null){
			return "00:00";
		}else{
			return schedule;
		}
	}
	
	/**
	 * Devuelve el nombre corto de un día de la semana
	 * L, M, X, J, V, S, D
	 * Para conseguir otro nombre corto usar dt.dayOfWeek().getAsShortText()
	 * @param dayWeek
	 * @return
	 */
	public static String getDayOfWeekAsShort(Context context, int dayWeek){
		String shortName = "";
		switch (dayWeek) {
		case 1:
			shortName = context.getResources().getString(R.string.monday_short);
			break;
		case 2:
			shortName = context.getResources().getString(R.string.tuesday_short);
			break;
		case 3:
			shortName = context.getResources().getString(R.string.wednesday_short);
			break;
		case 4:
			shortName = context.getResources().getString(R.string.thursday_short);
			break;
		case 5:
			shortName = context.getResources().getString(R.string.friday_short);
			break;
		case 6:
			shortName = context.getResources().getString(R.string.saturday_short);
			break;
		case 7:
			shortName = context.getResources().getString(R.string.sunday_short);
			break;

		default:
			break;
		}
		return shortName;
		
	}
	
	/**
	 * De un mes, devuelve el trimestre al que pertenece
	 * @param month
	 * @return
	 */
	public static int getQuarter(int month){
		switch (month) {
		case 1:
		case 2:
		case 3:
			return 1;
		case 4:
		case 5:
		case 6:
			return 2;
		case 7:
		case 8:
		case 9:
			return 3;
		case 10:
		case 11:
		case 12:
			return 4;

		default:
			return 0;
		}
	}
	
	/**
	 * De un trimestre devuelve los números de los meses que están incluidos
	 * @param quarter
	 * @return
	 */
	public static ArrayList<Integer> getMonthsQuarter(int quarter){
		ArrayList<Integer> list = new ArrayList<Integer>();
		switch (quarter) {
		case 1:
			list.add(1);
			list.add(2);
			list.add(3);
			break;
		case 2:
			list.add(4);
			list.add(5);
			list.add(6);
			break;
		case 3:
			list.add(7);
			list.add(8);
			list.add(9);
			break;
		case 4:
			list.add(10);
			list.add(11);
			list.add(12);
			break;

		default:
			list.add(0);
			break;
		}
		return list;
		
	}

    /**
     * De un mes devuelve la lista de todos los meses del trimestre al que pertenece
     * @param month
     * @return
     */
    public static ArrayList<Integer> getQuarterMonths(int month){
        return CuadranteDates.getMonthsQuarter(CuadranteDates.getQuarter(month));
    }

    /**
     * De un mes, devuelve el cuatrimestre al que pertenece
     * @param month
     * @return
     */
    public static int getQuarter2(int month){
        switch (month) {
            case 1:
            case 2:
            case 3:
            case 4:
                return 1;
            case 5:
            case 6:
            case 7:
            case 8:
                return 2;
            case 9:
            case 10:
            case 11:
            case 12:
                return 3;

            default:
                return 0;
        }
    }

    /**
     * De un cuatrimestre devuelve los números de los meses que están incluidos
     * @param quarter
     * @return
     */
    public static ArrayList<Integer> getMonthsQuarter2(int quarter){
        ArrayList<Integer> list = new ArrayList<Integer>();
        switch (quarter) {
            case 1:
                list.add(1);
                list.add(2);
                list.add(3);
                list.add(4);
                break;
            case 2:
                list.add(5);
                list.add(6);
                list.add(7);
                list.add(8);
                break;
            case 3:
                list.add(9);
                list.add(10);
                list.add(11);
                list.add(12);
                break;

            default:
                list.add(0);
                break;
        }
        return list;

    }
    /**
     * De un mes devuelve la lista de todos los meses del trimestre al que pertenece
     * @param month
     * @return
     */
    public static ArrayList<Integer> getQuarter2Months(int month){
        return CuadranteDates.getMonthsQuarter2(CuadranteDates.getQuarter2(month));
    }

    /**
     * Obtiene de un calendario el correcto mes, según la nueva forma de mostrar el calendario con
     * todas las semanas. Si por ejemplo estamos a 1 de marzo y es viernes, estamos en la semana
     * del mes anterior, por lo que el mes que se devuelve sera febrero.
     * @param calendar
     * @return
     */
    public static Calendar getCalendar(Calendar calendar){
        DateTime dtFirstDay, dtLastDay, dtFinal, dt2, dt = new DateTime(calendar);
        Map<String, DateTime> aMap = getStartEndMonth(calendar);
        //MyLog.d(TAG, aMap.get("firstDay").toString());
        //MyLog.d(TAG, aMap.get("lastDay").toString());

        dt = dt.millisOfDay().withMinimumValue();
        dt2 = dt.millisOfDay().withMaximumValue();
        dtFirstDay = aMap.get("firstDay");
        dtLastDay = aMap.get("lastDay");

        if(dt.isBefore(dtFirstDay)) {
            dtFinal = dt.minusMonths(1);
        }else if(dt2.isAfter(dtLastDay)){
            dtFinal = dt.plusMonths(1);
        }else{
            dtFinal = dt;
        }
        //MyLog.d(TAG, "dtFinal:" + dtFinal.getDayOfMonth());
        calendar.set(dtFinal.getYear(), dtFinal.getMonthOfYear() - 1, dtFinal.getDayOfMonth());
        return calendar;
    }

    /**
     * Obtiene las fechas de inicio y fin de un mes
     * Map<String, DateTime> aMap = getStartEndMonth(calendar);
     * para contabilizar bien con el jodatime tiene que ser por ejemplo de 00:00 a 00:00 del dia
     * siguiente se cuenta como un dia, no 00:00 a 23:59
     * @param calendar
     * @return firstDay, lastDay
     */
    public static Map<String, DateTime> getStartEndMonth(Calendar calendar){
        Map<String, DateTime> aMap = new HashMap<>();
        DateTime dtFirstDay, dtLastDay, dt2, dt = new DateTime(calendar);
        dt = dt.millisOfDay().withMinimumValue();
        dt2 = dt.millisOfDay().withMaximumValue();
        dtFirstDay = new DateTime(dt).withDayOfMonth(1);
        dtLastDay = new DateTime(dt).dayOfMonth().withMaximumValue();

        if(dtFirstDay.getDayOfWeek() > 4){//jueves, pasamos a la siguiente semana
            dtFirstDay = dtFirstDay.plusWeeks(1).withDayOfWeek(1);
        }else{//cogemos toda la semana desde el lunes, siendo del mes pasado
            dtFirstDay = dtFirstDay.withDayOfWeek(1);
        }
        aMap.put(FIRST_DAY, dtFirstDay);
        if(dtLastDay.getDayOfWeek() >= 4){//cogemos el lunes
            dtLastDay = dtLastDay.plusWeeks(1).withDayOfWeek(1);
        }else{
            dtLastDay = dtLastDay.withDayOfWeek(1);
        }
        aMap.put(LAST_DAY, dtLastDay);
        return aMap;
    }

    /**
     * De un mes devuelve cuantas semanas tiene
     * @param calendar
     * @return
     */
    public static int getNumWeeksMonth(Calendar calendar){
        Map<String, DateTime> aMap = CuadranteDates.getStartEndMonth(calendar);
        return Weeks.weeksBetween(
                aMap.get(CuadranteDates.FIRST_DAY), aMap.get(CuadranteDates.LAST_DAY)).getWeeks();
    }
}