package es.progmac.cuadrante.lib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.Weeks;

import es.progmac.cuadrante.AppWidgetWeekProvider;
import es.progmac.cuadrante.LoginActivity;
import es.progmac.cuadrante.MainActivity;
import es.progmac.cuadrante.R;
import es.progmac.cuadrante.SettingsActivity;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.cuadrante.info.TipoServicioInfo;
import es.progmac.android.others.ListPreferenceMultiSelect;
import es.progmac.android.others.UserEmailFetcher;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

public class Cuadrante{
	
	public static final String TAG = "Cuadrante";
	/**
	 * Para que los debug funcionen o no, ya que consumen recursos y no nos interesa en producción 
	 */
	public static boolean DEBUG_MODE = true;
	//cuadrante.java
	//layout/dialog_whatsnew.xml
	//AndroidManifiest.xml
	//values/strings.xml
	public static final String APP_VERSION_DATE = "2 de noviembre de 2014";
	/**
	 * Url donde se encuentra la sección ayuda de la app
	 */
	public static final String URL_HELP = "http://progmac.hol.es/faq/?lang=es";
	/**
	 * Url de la aplicación en Google Play
	 */
	public static final String APP_GOOGLE_PLAY_URL = 
			"https://play.google.com/store/apps/details?id=es.progmac.cuadrante";
	/**
	 * Email mio de contacto para los usuarios
	 */
	public static final String EMAIL_CONTACT = "progmac.es@gmail.com";
	/**
	 * Nombre de la "tabla" donde se guardan los shared preferences en el 
	 * fichero backup xml
	 * @see es.progmac.cuadrante.lib.DatabaseAssistant
	 */
	public static final String BACKUP_DB_TABLE_NAME_SP = "shared_preferences";
    
    public static final SparseArray<String> GUARDIAS_COMBINADAS = new SparseArray<String>();
    static{
    	GUARDIAS_COMBINADAS.put(0, "Ninguna");
    	GUARDIAS_COMBINADAS.put(1, "Clase A");
    	GUARDIAS_COMBINADAS.put(2, "Clase B");
    	GUARDIAS_COMBINADAS.put(3, "Clase C");
    	GUARDIAS_COMBINADAS.put(4, "Clase D");
    	GUARDIAS_COMBINADAS.put(5, "Clase E");
    	GUARDIAS_COMBINADAS.put(6, "Clase F");
    };
	
    /**
     * Horario por defecto tanto inicio como fin, 1, 2, 3, 4
     */
    public static final String SCHEDULE_DV = "00:00";
    /**
     * Horario nulo, es decir, como se guarda en la bd si no se han añadido horas
     */
    public static final String SCHEDULE_NULL = "";
	/**
	 * Color por defecto de fondo para el día actual
	 */
	public static final int TODAY_DEFAULT_BG_COLOR = -6029568;
	/**
	 * Color por defecto del texto para el día actual
	 */
	public static final int TODAY_DEFAULT_TEXT_COLOR = -1;
	/**
	 * Color por defecto de fondo para un servicio
	 */
	public static final int SERVICE_DEFAULT_BG_COLOR = -1;// white//-16739328
	/**
	 * Color por defecto del texto para un servicio
	 */
	public static final int SERVICE_DEFAULT_TEXT_COLOR = -16777212;// black//-1
	/**
	 * Color de fondo para el dia festivo
	 */
	public static final int SERVICE_DEFAULT_BG_COLOR_HOLIDAY = -321527;
	/**
	 * Color de texto para el dia festivo
	 */
	public static final int SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY = -1;// white	
	/**
	 * Horas del índice, para conseguir el F2
	 */
	public static final int F2_HOURS = 10;
	/**
	 * Número mínimo de alertas para conseguir el F2
	 */
	public static final int F2_ALERTS = 4;
	/**
	 * Constante de resultado entre activitys para decir que se borra algo.
	 * Mirar HotelActivity en onDeleteData(View v)
	 */
	public static final int RESULT_DELETE = 10;
	/**
	 * Constante de resultado entre activitys para decir que se ha producido
	 * un error. Mirar LoginActivity
	 */
	public static final int RESULT_ERROR = 11;
	/**
	 * Palabra secreta que se usa para codificar la contraseña
	 */
	public static final byte[] SECRET_KEY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6};
	/**
	 * Texto por defecto para un día del calendario que esté en comisión
	 */
	public static final String CALENDAR_SERVICE_COMISION = "COMISIÓN";
	/**
	 * Texto por defecto para un día del calendario que sea un saliente de servicio
	 */
	public static final String CALENDAR_SERVICE_SALIENTE = "SAL";
	/**
	 * Respuesto si se envia un email
	 */
	public static final int REQUEST_CODE_EMAIL = 10;
	/**
	 * Respuesta al volver de la pantalla de crear tipo
	 */
	public static final int REQUEST_CODE_CREATE_TYPE = 11;
	/**
	 * Respuesta al guardar
	 */
	public static final int REQUEST_CODE_SAVE = 12;
	/**
	 * Respuesta al cargar
	 */
	public static final int REQUEST_CODE_LOAD = 13;
	/**
	 * Respuesta al guardar las preferencias del login
	 */
	public static final int REQUEST_CODE_LOGIN_SAVE = 14;	
	/**
	 * Diálogo de ventana novedades
	 */
	public static final int DIALOG_ID_WHATSNEW = 4;
	/**
	 * Diálogo de ventana primeros pasos
	 */
	public static final int DIALOG_ID_FIRSTSTEPS = 5;
	/**
	 * Tipo de dia para un servicio o tipo.
	 * normal, ningún índice de horas
	 */
	public static final int TYPE_DAY_NULL= 0;
	/**
	 * Tipo de dia para un servicio o tipo.
	 * Días ordinarios no computables (Bajas, vacaciones ordinarias, permisos incorporación, etc.)
	 * a 5.35
	 */
	public static final int TYPE_DAY_ORDINARY = 1;
	/**
	 * Tipo de dia para un servicio o tipo.
	 * Días especiales no computables (Permisos "urgentes", Asuntos particulares y Permisos de SS o Navidad)
	 * a 7.5
	 */
	public static final int TYPE_DAY_ESPECIAL = 2;

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String HOURS = "hours";
    public static final String MINUTES = "minutes";
	
	public static DatabaseHandler db;
	
	/**
	 * Verifica primero si un tipo de servicio es un tipo de permiso (asuntos
	 * particulares o vacaciones) y si es así, si se pueden grabar entre un intervalo
	 * de fechas si hay suficientes dias de permiso disponibles.
	 * @param typeId tipo de servicio a comprobar
	 * @param date
	 * @return
	 */
	public static boolean canServiceSaves(Context context, int typeId, String date){
		return canServiceSaves(context, typeId, date, date);
	}
	
	/**
	 * Verifica primero si un tipo de servicio es un tipo de permiso (asuntos
	 * particulares o vacaciones) y si es así, si se pueden grabar entre un intervalo
	 * de fechas si hay suficientes dias de permiso disponibles.
	 * @param typeId tipo de servicio a comprobar
	 * @param dStart 
	 * @param dEnd 
	 * @return
	 */
	public static boolean canServiceSaves(Context context, int typeId, DateTime dStart, DateTime dEnd){
		return canServiceSaves(context, typeId, 
				CuadranteDates.formatDate(dStart.getYear(), dStart.getMonthOfYear(), dStart.getDayOfMonth()), 
				CuadranteDates.formatDate(dEnd.getYear(), dEnd.getMonthOfYear(), dEnd.getDayOfMonth()));
	}
	
	/**
	 * Verifica primero si un tipo de servicio es un tipo de permiso (asuntos
	 * particulares o vacaciones) y si es así, si se pueden grabar entre un intervalo
	 * de fechas si hay suficientes dias de permiso disponibles.
	 * @param typeId tipo de servicio a comprobar
	 * @param dStart yyyy-mm-dd
	 * @param dEnd yyyy-mm-dd
	 * @return
	 */
	public static boolean canServiceSaves(Context context, int typeId, String dStart, String dEnd){
		DatabaseHandler db = new DatabaseHandler(context);
		//verificamos que si el tipo de servicio es vacaciones o
		//asuntos particulares entonces mira si hay suficientes dias
		//para el limite que tiene cada uno quitando sábados y domingos	
		//Log.d("canServiceSaves", "typeId:" + typeId + " dStart:" + dStart + " dEnd:" + dEnd);
		int type_service_holidays = Sp.getHolidaysTypeService(context);
		int type_service_own_affairs = Sp.getOwnAffairsTypeService(context);
		if(typeId == type_service_holidays ||
				typeId == type_service_own_affairs){
			//Log.d("canServiceSaves", "permiso: " + typeId);
			int daysPref = 0, typePref = 0, restDays = 0, daysInterval = 0;
			
			CuadranteDates tmpDates = new CuadranteDates(dStart);
			DateTime dtDS = new DateTime(tmpDates.getYear(), tmpDates.getMonth(), tmpDates.getDay(), 0, 0);
			tmpDates = new CuadranteDates(dEnd);
			DateTime dtDE = new DateTime(tmpDates.getYear(), tmpDates.getMonth(), tmpDates.getDay(), 0, 0);
			daysInterval = Days.daysBetween(dtDS, dtDE).getDays();
				
	        //indicamos los dias de vacaciones gastados
	        DateTime date = new DateTime();
	        String dateAnualStart, dateAnualEnd;
	        //si el mes actual es enero entonces tenemos que poner el intervalo
	        //del año pasado al actual
	        if(date.getMonthOfYear() == 1){
	        	dateAnualStart = CuadranteDates.formatDate(date.minusYears(1).getYear(), 02, 01);
	        	dateAnualEnd = CuadranteDates.formatDate(date.getYear(), 01, 31);
	        }else{
	        	dateAnualStart = CuadranteDates.formatDate(date.getYear(), 02, 01);
	        	dateAnualEnd = CuadranteDates.formatDate(date.plusYears(1).getYear(), 01, 31);        	
	        }
	        //Log.d("canServiceSaves", "dateAnualStart:" + dateAnualStart + " dateAnualEnd:" + dateAnualEnd);

			if(typeId == type_service_holidays){
				typePref = type_service_holidays;
				daysPref = Sp.getHolidaysDays(context);
				//includeWeekend = true;
				//daysInterval = db.getDaysIntervalWithoutWeekendDays(dStart, dEnd).size();							
			}else{
				typePref = type_service_own_affairs;
				daysPref = Sp.getOwnAffairsDays(context);
				//includeWeekend = true;
				//daysInterval = Cuadrante.getDaysIntervalOnlyWeekendDays(dStart, dEnd);
			}	        
	        //obtenemos los servicios que sean de este tipo entre
	        //la fecha del año actual 1 de Febrero al 31 de enero
	        List<ServicioInfo> servicesAnual = db.getServicesFromTypeService(
	        		typePref, 
	        		dateAnualStart,
	        		dateAnualEnd,
	        		true, false);	
	        
	        //obtenemos los servicios de este tipo que haya entre
	        //las fechas de selección de rango de fechas, por si el
	        //usuario ha seleccionado un rango donde ya había dias
	        //servicios de este tipo, por si quiere corregir lo hecho
	        //por él anteriormente
	        List<ServicioInfo> servicesDateRange = db.getServicesFromTypeService(
	        		typePref, 
	        		dStart,
	        		dEnd,
	        		true, false);
	        
	        if(typeId == type_service_holidays){
		        int numServicesAnualSpent = getDaysHolidaysSpent(servicesAnual);
		        int numServicesDateRangeSpent = getDaysHolidaysSpent(servicesDateRange);
		        restDays = daysPref	- numServicesAnualSpent + numServicesDateRangeSpent;
		        
		        //miramos cuantos dias hay disponibles entre las fechas seleccionadas
		        List<ServicioInfo> tmpServices = new ArrayList<ServicioInfo>();
		        DateTime tmpDT = dtDS;
		        List<ServicioInfo> servicesHolidays = db.getHolidaysFromInterval(dStart, dEnd);
		        while(tmpDT.isBefore(dtDE) || tmpDT.isEqual(dtDE)){
		        	String tmpDate = CuadranteDates.formatDate(tmpDT);
		        	int tmpIsHoliday = 0;
		        	//miramos si en el intervalo de tiempo hay algún dia
		        	//festivo de base de datos
		        	if(servicesHolidays.size() > 0){
		        		//Log.d("canServiceSaves", "intervalo fecha:" + tmpDate);
		        		for(ServicioInfo tmpService : servicesHolidays){
		        			if(tmpService.getDate().equals(tmpDate)){
			        			//Log.d("canServiceSaves", "festivo:" + tmpService.getDate());
		        				tmpIsHoliday = 1;
		        			}
		        		}
		        	}else{
		        		tmpIsHoliday = 0;
		        	}
		        	tmpServices.add(new ServicioInfo(tmpDate, tmpIsHoliday));
		        	tmpDT = tmpDT.plusDays(1);
		        }
		        daysInterval = getDaysHolidaysSpent(tmpServices);
		        
		        //Log.d("canServiceSaves", "daysPref:" + daysPref + " numServicesAnualSpent:" + numServicesAnualSpent + " numServicesDateRangeSpent:" + numServicesDateRangeSpent + " daysInterval:" + daysInterval);
	        }else{
		        restDays = daysPref	- servicesAnual.size() + servicesDateRange.size();
		        //Log.d("canServicesSaves", "daysPref:" + daysPref + " servicesAnual.size():" + servicesAnual.size() + " servicesDateRagne.size():" + servicesDateRange.size());
	        }

	        //si los dias que quedan de vacaciones o asuntos propios
	        //es mayor que los dias del intervalo que ha solicitado
	        //grabar el usuario pues se puede grabar, si no significa
	        //que no hay suficientes dias de cupo
	        //Log.d("canServiceSaves", "restDays:" + restDays + " daysInterval:" + daysInterval);
	        if(restDays > 0 && restDays >= daysInterval) return true;
	        else return false;
		}
		//si no pertenece a un tipo de permiso entonces sí se pueden grabar los datos
		return true;		
	}	
	
	/**
	 * Obtiene el número de dias gastados para las vacaciones
	 * @param services servicios de vacaciones
	 * @return número de días gastados
	 */
	public static int getDaysHolidaysSpent(List<ServicioInfo> services){
		boolean isFirstDayOfRange = true;
        boolean isLastDayOfRange = false;
        DateTime dtTomorrow = null;
        int i = 0;
        /** Cuenta el número de días gastados de vacaciones */
        int countServicesSpent = 0;
        /** Cuenta el número de días gastados naturales */
        int numNaturalServices = 0;
        /** Cuenta el número de servicios de un rango */
        int countServicesRange = 1;
        /** cuenta el número de servicios gastados en cada rango */
        int countServicesRangeSpent = 0;
        /**
         * si el primer dia del rango es festivo, sábado o domingo para no contarlo si hay igual
         * o más de 5 días hábiles.
         */
        boolean isFirstDayHoliday = false;
        /** 
         * Cuenta el número de días de vacaciones gastados en rangos de menos de 5 dias ya que
         * con la nueva orden de vacas se puede hasta 6 dias al año gastar menos sin contar findes
         */
        int countMinusSix = 0;
        
        while (i < services.size()) {
    		ServicioInfo service = services.get(i);
        	DateTime dt = service.getDateTime();//CuadranteDates.getDateTime(service.getDate());
        	//miramos si hay un servicio siguiente y si es así si está seguido
        	//el día, si no, entonces es que es el fin del rango de fechas
        	if(i + 1 < services.size()){
        		ServicioInfo serviceTomorrow = services.get(i+1);
        		dtTomorrow = serviceTomorrow.getDateTime();
        		/*
            	dtTomorrow = new DateTime(
            			serviceTomorrow.getYear(), 
            			serviceTomorrow.getMonth(), 
            			serviceTomorrow.getDay(), 0, 0);
            			*/
            	if(dt.plusDays(1).getDayOfMonth() != dtTomorrow.getDayOfMonth()){
            		isLastDayOfRange = true;
            	}
        	}else if(i == services.size() - 1){//ultimo dia de vacas
        		isLastDayOfRange = true;
        	}
    		//Log.d("getDaysHolidaysSpent", "servicio:" + service.getDate());
    		
        	if(isFirstDayOfRange){//si es el primer dia del rango que lo cuente siempre independientemente que día sea
	        	//Log.d("setPreference", service.getDate() + " dia de la semana:" + dt.getDayOfWeek());
	        	if(service.getIsHoliday() == 1 
	    			|| dt.getDayOfWeek() == DateTimeConstants.SATURDAY 
	    			|| dt.getDayOfWeek() == DateTimeConstants.SUNDAY){
		        	//Log.d("getDaysHolidaysSpent", "    inicio rango festivo o sabado o domingo");
	        		//countServicesSpent++;
	        		isFirstDayHoliday = true;
	        	}else{
	        		numNaturalServices++;
	        	}
	        	countServicesSpent++;
	        	countServicesRangeSpent++;
	        	//Log.d("getDaysHolidaysSpent", "    primer dia del rango, gastado siempre " + countServicesSpent);
	        	isFirstDayOfRange = false;
	        //último dia del rango o ultimo dia de vacaciones del año
        	}else if(isLastDayOfRange){ 
        		//Log.d("getDaysHolidaysSpent", "    termina el rango");
        		//miramos si siendo el ultimo rango, es domingo y si los dias
        		//naturales gastados son 5, eso significa que ha iniciado las
        		//vacas el lunes y terminado el domingo, por lo que gastados
        		//son 5 dias y entran regalados el fin de semana por lo que el
        		//domingo no se puede restar.
        		
        		//si el ultimo dia es entre semana hay que sumar un dia natural
        		//gastando, ejemplo si empieza el Lunes 03/12/2012 y termina el
        		//10/12/2012 está el jueves 6 y sabado 8 festivo.
        		if(dt.getDayOfWeek() != DateTimeConstants.SATURDAY 
            			&& dt.getDayOfWeek() != DateTimeConstants.SUNDAY 
            			&& service.getIsHoliday() == 0){
        			numNaturalServices++;
        			//countServicesRangeSpent++;
        		}
        		
        		//si el primer dia es festivo, sabado o domingo y los días naturales son igual o 
        		//mayor a 5 no contar ese día
        		if(isFirstDayHoliday && numNaturalServices >= 5){
    	        	countServicesSpent--;
    	        	countServicesRangeSpent--;        			
        		}
         		//si acaba en festivo o domingo y los dias naturales son 5
        		//sumamos
        		if(dt.getDayOfWeek() == DateTimeConstants.SUNDAY
    				|| service.getIsHoliday() == 1){
    				//&& numNaturalServices == 5){
        			//Log.d("getDaysHolidaysSpent", "        empezó el lunes y termina el domingo, bien no sumamos más");        			
        		}else if(numNaturalServices < 5){//el rango es menor de 5 dias, por lo que todos los dias independientemente que dias se tienen que sumar
        			//Log.d("getDaysHolidaysSpent", "        countServicesSpent:" + countServicesSpent);
        			//ahora con la nueva orden se puede gastar menos de 5 dias sin contar fines
        			//sumando 6 al año, lo normal que hace la gente es de viernes a lunes, gasto
        			//2 pero me dan el finde por medio
        			if(countMinusSix < 6){
        				countMinusSix += countServicesRangeSpent + 1;  
        				countServicesSpent++;
        			}else{
            			countServicesSpent = countServicesSpent - countServicesRangeSpent + countServicesRange;        				
        			}
        			
        			//Log.d("getDaysHolidaysSpent", "        el rango es menor de 5 sumamos todos los servicios del rango countServicesSpent:" + countServicesSpent + " countServicesRangeSpent:" + countServicesRangeSpent + " countServicesRange:" + countServicesRange);
        		}else{
        			countServicesSpent++;
        			//Log.d("getDaysHolidaysSpent", "        sumamos otro dia gastado " + countServicesSpent);        			
        		}
        		numNaturalServices = 0;
        		isFirstDayHoliday = false;
        		isFirstDayOfRange = true;   
        		isLastDayOfRange = false;
        		countServicesRange = 0;
        		countServicesRangeSpent = 0;
        	}else if(dt.getDayOfWeek() != DateTimeConstants.SATURDAY 
        			&& dt.getDayOfWeek() != DateTimeConstants.SUNDAY 
        			&& service.getIsHoliday() == 0){
        		countServicesSpent++;
        		countServicesRangeSpent++;
        		numNaturalServices++;
        		//Log.d("getDaysHolidaysSpent", "    entre semana y no festivo " + countServicesSpent);
        	}  		
        	countServicesRange++;
        	i++;
    	}
        return countServicesSpent;
	}
	
    /**
     * Los datos de login los deja a los valores por defecto
     * @param context
     */
    public static void setLoginDataDefaultValue(Context context){
    	Sp.setLoginActivate(context, Sp.SP_LOGIN_ACTIVATE_DV);
    	Sp.setLoginPassword(context, Sp.SP_LOGIN_PASSWORD_DV);
    	Sp.setLoginExpireSessionDuration(context, Sp.SP_LOGIN_EXPIRE_SESSION_DURATION_DV);
    	Sp.setLoginExpireSessionDate(context, Sp.SP_LOGIN_EXPIRE_SESSION_DATE_DV);
    	Sp.setWidgetActivate(context, Sp.SP_WIDGET_ACTIVATE_DV);
    }	
    
	/**
	 * Verifica si el usuario está logeado, si no lo está lo envia a la pantalla
	 * de login
	 * @param activity
	 */
	public static void checkSignIn(Activity activity){
		Context context = activity.getApplicationContext();
		if(Sp.getLoginActivate(context)){
			int[] sessions = context.getResources().getIntArray(R.array.login_expire_session_values);
			int session_length = sessions[Sp.getLoginExpireSessionDuration(context)];
			
			DateTime session_date = CuadranteDates.getDateTimefromDate(Sp.getLoginExpireSessionDate(context));
			int seconds_duration = Seconds.secondsBetween(session_date, new DateTime()).getSeconds();
			
			if(seconds_duration > session_length){
				 Intent intent = new Intent(context, LoginActivity.class);
				 activity.startActivity(intent);
				 activity.finish();
			}else{
				Cuadrante.autoBackupDB(activity);			
			}
		}else{
			Cuadrante.autoBackupDB(activity);			
		}
	}
	
	/**
	 * Termina la sesión
	 * @param context
	 */
	public static void logOut(final Context context){
		if(Sp.getLoginActivate(context)){
	    	Sp.setLoginExpireSessionDate(context, Sp.SP_LOGIN_EXPIRE_SESSION_DATE_DV);			
		}
	}
	
	/**
	 * De una cadena de texto, devuelve la parte correspondiente al número 
	 * máximo de caracteres para las abreviaturas de servicios en el calendario
	 * @param context
	 * @param text Cadena de texto a dividir
	 * @return
	 */
	public static String getLengthServiceNameSubstring(Context context, String text){
		int name_end = Sp.getLengthServiceName(context);
		if(text.length() < name_end) name_end = text.length();
		return text.substring(0, name_end);		
	}
	
	/**
	 * Revisa si hay que generar un backup de la base de datos
	 * @param activity
	 */
	public static void autoBackupDB(Activity activity){
		Context context = activity.getApplicationContext();
		String autoBackup = Sp.getBackupDbAuto(context);
		boolean firstRun = Sp.getIsFirstRun(context);
		String autoBackupDate = Sp.getBackupDbAutoDate(context);
		DateTime dtToday = new DateTime();
		String dateToday = CuadranteDates.formatDate(dtToday);
		//Log.d("autoBackupDB", "autoBackup:" + autoBackup + " date:" + autoBackupDate);
		if(!autoBackup.equals("never") && !firstRun && !dateToday.equals(autoBackupDate)){
			CuadranteDates autoDate = new CuadranteDates(autoBackupDate);
			DateTime autoDT = new DateTime(autoDate.getYear(), autoDate.getMonth(), autoDate.getDay(), 0, 0);
			Days days = Days.daysBetween(autoDT, dtToday);
			
			//Log.d("autoBackupDB", "autoDT week:" + autoDT.getWeekOfWeekyear() + " dtToday week:" + dtToday.getWeekOfWeekyear());
			//Log.d("autoBackupDB", "autoDT month:" + autoDT.getMonthOfYear() + " dtToday month:" + dtToday.getMonthOfYear());
			
			if((autoBackup.equals("day") && days.getDays() > 0) 
				|| (autoBackup.equals("week") && autoDT.getWeekOfWeekyear() != dtToday.getWeekOfWeekyear())
				|| (autoBackup.equals("month") && autoDT.getMonthOfYear() != dtToday.getMonthOfYear())){
					Intent intent = new Intent(context, SettingsActivity.class);
					intent.putExtra(Extra.AUTO_BACKUP, true);
					// setResult(RESULT_OK, intent);
					// startActivity(intent);				
					activity.startActivityForResult(intent, 101);	
					Sp.setBackupDBAutoDate(context, CuadranteDates.formatDate(dtToday));
			}
		}		
	}
	
    /**
     * Verifica si pasados 200 ejecuciones de la aplicación se muestra un diálogo de si al usuario
     * no le importa votar por la app en google play 
     * @param activity
     * @param action OnCreate | OnPause , Action method from Activity
     */
	public static void voteForMe(Activity activity, String action){
		Context context = activity.getApplicationContext();
		if(Sp.getShowVoteForMe(context)){
			int c = Sp.getNumRun(context);
	    	if(action.equals("OnPause")){
				if(!activity.isFinishing()){
					c--;
					Sp.setNumRun(context, c);
				}    		
	    	}else if(action.equals("OnCreate")){
		    	c++;
		    	Sp.setNumRun(context, c);		    	
		    	if(c > 200){
		    		activity.showDialog(MainActivity.DIALOG_VOTE_FOR_ME);
		    		Sp.setShowVoteForMe(context, false);
		    	}
	    	}
		}
    }
	
	/**
	 * Rellena la base de datos con muchos servicios para el desarrollo
	 * @param context
	 */
	public static void setDBtoFill(final Context context) {
		final ProgressDialog pd = ProgressDialog.show(context,
				"Llenando base de datos",
				"Se están generando servicios aleatorios, espere por favor",
				true, false);
		new Thread(new Runnable(){
			public void run(){
				DateTime dt = new DateTime().minusYears(1);
				DateTime dtFuture = new DateTime();
				db = new DatabaseHandler(context);
				db.destroyServiceTable();
				List<TipoServicioInfo> type_services = db.getAllTipoServicios();
				Random random = new Random();
				String cArray[] = new String[] { "j asfjasklñfj fjasklñfjaslñdjf asdjfl asdjfklñ asjdklñf jasdklñfj asklñfjaslñdjf klñasdj f lñasjdfklñ jasdflj asfjaslñdfj asklñjd fklñasdjfaklñ sj dfasdj flñasdjfklñj asdfklñj asdlñkfj asklñfj asklñdfj asklñdfj aklñsdjf lñasjdf klñasjf lñasdjf alñskfj aslñfj aklsalñskdfj ñlasdjfkl asjdlñfj asdlñfj asklñfj asdlñfj aslñdj flñasdj fñlasdj flñasdjflñ asjdflñ kasjdflñ asdjf asdlñfj asdlñfj aslñdfj asdlñjf lñasdjfklñ asdjflñk asdjflñ asdjflñ asdjfklñ jasdlñfj asdlñkfj asdlñfj asdlñkfj asdlñfj asdklñfj asdlñjflñasdk fjaslñ", 
						"dlafj ñsdfjaklñsjfñlasj fñlasjdflñ asj", "" };
				//Log.d("setDbToFill", "Inicio:" + new DateTime().toString());
				while(dt.isBefore(dtFuture) || dt.isEqual(dtFuture)){
					int i = random.nextInt(type_services.size());
					TipoServicioInfo type_service = type_services.get(i);
					ServicioInfo service = new ServicioInfo(
							CuadranteDates.formatDate(dt),
							type_service.getId(), 
							type_service.getName(), 
							type_service.getBgColor(),
							type_service.getTextColor(), 
							type_service.getStartSchedule(),
							type_service.getEndSchedule(), 
							type_service.getStartSchedule2(),
							type_service.getEndSchedule2(), 
							type_service.getStartSchedule3(),
							type_service.getEndSchedule3(), 
							type_service.getStartSchedule4(),
							type_service.getEndSchedule4(), 
							cArray[random.nextInt(3)], 0,
							type_service.getGuardiaCombinada(),
							type_service.getTypeDay(),
							0,
							type_service.getSuccessionCommand());
					db.addService(service);
					//Log.d("setDBtoFill", "date:" + CuadranteDates.formatDate(dt) + " type_id:" + type_service.getId());
					dt = dt.plusDays(1);
				}
				//Log.d("setDbToFill", "Fin:" + new DateTime().toString());
				pd.dismiss();
			}
		}).start();	
		
	}	
	
	/**
	 * Genera algunas ventanas de texto plano dependiendo del id del diálogo
	 * @param context
	 * @param dialog_id
	 */
    public static void showInflateDialog(Context context, int dialog_id) {
    	LayoutInflater inflater = LayoutInflater.from(context);
    	int layout = 0, title = 0, button = 0;
    	switch (dialog_id) {
			case DIALOG_ID_WHATSNEW:
				layout = R.layout.dialog_whatsnew;
				title = R.string.title_changelog;
				button = R.string.ok;
				break;
	
			case DIALOG_ID_FIRSTSTEPS:
				layout = R.layout.dialog_firststeps;
				title = R.string.title_firststeps;
				button = R.string.ok;
				break;
		}
        View view = inflater.inflate(layout, null);
      	
  	  	Builder builder = new AlertDialog.Builder(context);

	  	builder.setView(view).setTitle(context.getResources().getString(title))
	  	.setPositiveButton(context.getResources().getString(button), 
	  			new DialogInterface.OnClickListener() {
	  		@Override
	  		public void onClick(DialogInterface dialog, int which) {
	  			dialog.dismiss();
	  		}
	    });
  	
	  	builder.create().show();
    }	
    
	/**
	 * Lo que se hará la primera vez que se instala la aplicación
	 * @param context
	 */
	public static void setFirstRun(Context context){
		if(Sp.getIsFirstRun(context)){
			Sp.setIsFirstRun(context, false);
			Sp.setBackupDBAutoDate(context, CuadranteDates.formatDate(new DateTime()));
			
			//guardamos el email de gmail del usuario
			String userEmail = UserEmailFetcher.getEmail(context);
			if(userEmail == null){
				Sp.setEmail(context);
			}else{
				Sp.setEmail(context, userEmail);
			}
			
			//Mostramos la ventana de pasos a seguir.
			//showInflateDialog(context, DIALOG_ID_FIRSTSTEPS);
		}		
		
		//Si ha cambiado el número de la versión de la aplicación entonces
		//mostramos el dialog del changelog
		int currentVersionNumber = 0;
		try {
   	 		PackageInfo pi 			= context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    	 	currentVersionNumber	= pi.versionCode;
   	 	} catch (Exception e) {}	

   	 	if (currentVersionNumber > Sp.getChangeLogVersionKey(context)) {   	 		
   	 		showInflateDialog(context, DIALOG_ID_WHATSNEW);
   	 		Sp.setChangeLogVersionKey(context, currentVersionNumber);
   	 	}
	}    
	
	/**
	 * Obtiene las horas trabajadas en un servicio
	 * @param year
	 * @param month
	 * @param day
	 * @param startSchedule
	 * @param endSchedule
	 * @param isServicePreviousMonth
	 * @return Period
	 */
	public static Period getTimeFromService(int year,
			int month, int day, String startSchedule, String endSchedule,
			boolean isServicePreviousMonth){
		return _getTimeFromService(year, month, day, startSchedule, 
				endSchedule, isServicePreviousMonth);
	}
	
	/**
	 * Obtiene las horas trabajadas en un servicio
	 * @param year
	 * @param month
	 * @param day
	 * @param startSchedule
	 * @param endSchedule
	 * @return Period
	 */
	public static Period getTimeFromService(int year,
			int month, int day, String startSchedule, String endSchedule){
		return getTimeFromService(year, month, day, startSchedule, endSchedule, false);
	}
	
	private static Period _getTimeFromService(int year,
			int month, int day, String startSchedule, String endSchedule, 
			boolean isServicePreviousMonth){
		String[] start = new String[2];
		String[] end = new String[2];
		start = startSchedule.split(":");
		end = endSchedule.split(":");
		int startHour = Integer.parseInt(start[0]);
		int startMinutes = Integer.parseInt(start[1]);
		int endHour = Integer.parseInt(end[0]);
		int endMinutes = Integer.parseInt(end[1]);
		//Log.d("getTime", year + " " + month + " " + day);
		DateTime dtStart = new DateTime(year,
				month, day,
				startHour, startMinutes);
		DateTime dtEnd = new DateTime(year,
				month, day,
				endHour, endMinutes);

		Duration duration = new Duration(dtStart, dtEnd);
		//si sale negativo es que la hora final es inferior a la hora
		//inicial (22:00 - 06:00) asi que sumamos un día más a la fecha fin 
		//para poder restarla bien y que salga las horas correctas.
		if(duration.getMillis() <= 0){
			dtEnd = dtEnd.plusDays(1);
			duration = new Duration(dtStart, dtEnd);
		}	
		//si al sumar un dia hemos cambiado de mes
		if((dtEnd.getMonthOfYear() > dtStart.getMonthOfYear())
				|| (dtEnd.getYear() > dtStart.getYear())){//cambio de año
			if(isServicePreviousMonth){//servicio del mes pasado
				dtStart = dtStart.plusDays(1).hourOfDay().setCopy(0)
						.minuteOfHour().setCopy(0);						
			}else{//servicio de este mes pero termina el 1 del mes siguiente
				dtEnd = dtEnd.hourOfDay().setCopy(0);
				dtEnd = dtEnd.minuteOfHour().setCopy(0);				
			}
			duration = new Duration(dtStart, dtEnd);
		}else if(isServicePreviousMonth){//significa que el servicio del mes
			//pasado no tiene termina el horario en el mes actual
			return new Period();			
		}
		return duration.toPeriod();
	}
	
	/**
	 * Obtiene el índice de horas totales del servicio que estén dentro de los
	 * horarios de festivos y nocturnos para cobrar el F2. 
	 * Sábado empieza a las 15:00 y termina el lunes a las 06:00 eso se divide
	 * entre 2 y el número que sale, en total durante el mes debe de pasar 10
	 * para cobrar el F2. También se deben de sumar el número de horas nocturas
	 * no festivas, que empieza a las 22:00 hasta las 06:00 y se divide entre 4.
	 * @param year
	 * @param month
	 * @param day
	 * @param startSchedule
	 * @param endSchedule
	 * @param isServicePreviousMonth si el servicio que se pasa es el ultimo del mes
	 * anterior para que coja y solo mire el F2 a partir de las 00:00
	 */
	public static HashMap<String, Double> getHoursToF2(DatabaseHandler db, int year, int month, int day, 
			String startSchedule, String endSchedule, boolean isServicePreviousMonth){
		return _getHoursToF2(db, year, month, day, startSchedule, endSchedule, isServicePreviousMonth);	
	}
	
	/**
	 * Obtiene el índice de horas totales del servicio que estén dentro de los
	 * horarios de festivos y nocturnos para cobrar el F2. 
	 * Sábado empieza a las 15:00 y termina el lunes a las 06:00 eso se divide
	 * entre 2 y el número que sale, en total durante el mes debe de pasar 10
	 * para cobrar el F2. También se deben de sumar el número de horas nocturas
	 * no festivas, que empieza a las 22:00 hasta las 06:00 y se divide entre 4.
	 * @param year
	 * @param month
	 * @param day
	 * @param startSchedule
	 * @param endSchedule
	 */
	public static HashMap<String, Double> getHoursToF2(DatabaseHandler db, int year, int month, int day, 
			String startSchedule, String endSchedule){
		return _getHoursToF2(db, year, month, day, startSchedule, endSchedule, false);	
	}
	
	private static HashMap<String, Double> _getHoursToF2(DatabaseHandler db, int year, int month, int day, 
			String startSchedule, String endSchedule, boolean isServicePreviousMonth){
		HashMap<String, Double> data = new HashMap<String, Double>();
		HashMap<String, Double> data_final = new HashMap<String, Double>();
		data_final.put("indice", 0.0);
		data_final.put("hours", 0.0);
		
		String[] start = new String[2];
		String[] end = new String[2];
		start = startSchedule.split(":");
		end = endSchedule.split(":");
		int startHour = (Integer.parseInt(start[0]) >= 0) ? Integer.parseInt(start[0]) : 0;
		int startMinutes = (Integer.parseInt(start[1]) >= 0) ? Integer.parseInt(start[1]) : 0;
		int endHour = (Integer.parseInt(end[0]) >= 0) ? Integer.parseInt(end[0]) : 0;
		int endMinutes = (Integer.parseInt(end[1]) >= 0) ? Integer.parseInt(end[1]) : 0;
		DateTime dtStart = new DateTime(year,
				month, day,
				startHour, startMinutes);
		DateTime dtEnd = new DateTime(year,
				month, day,
				endHour, endMinutes);	
		
		Duration duration = new Duration(dtStart, dtEnd);
		//si sale negativo es que la hora final es inferior a la hora
		//inicial (22:00 - 06:00) asi que sumamos un día más a la fecha fin 
		//para poder restarla bien y que salga las horas correctas.
		//MyLog.d(TAG, "duration start-end:" +  duration.getMillis());
		if(duration.getMillis() <= 0){
			dtEnd = dtEnd.plusDays(1);
		}
		
		//si al sumar un dia hemos cambiado de mes
		if(dtEnd.getMonthOfYear() != dtStart.getMonthOfYear()){
			MyLog.d(TAG, isServicePreviousMonth);
			if(isServicePreviousMonth){//servicio del mes pasado
				dtStart = dtStart.plusDays(1).hourOfDay().setCopy(0)
						.minuteOfHour().setCopy(0);						
			}else{//servicio de este mes pero termina el 1 del mes siguiente
				dtEnd = dtEnd.hourOfDay().setCopy(0);
				dtEnd = dtEnd.minuteOfHour().setCopy(0);		
				MyLog.d(TAG, dtEnd.toString());
			}
		}else if(isServicePreviousMonth){//significa que el servicio del mes
			//pasado no tiene, termina el horario en el mes actual
			return data_final;			
		}
		Interval interval = new Interval(dtStart, dtEnd);
		MyLog.d(TAG, interval.toString());	
		Interval intvlChristmas = null;
		Interval intvlYearEnd = null;
		Interval intvlNewYear = null;
		/**
		 * O.G 10 del 16 de junio de 2006 
		 */
		//intervalo de navidad
		List<Interval> list_intvl = new ArrayList<Interval>();
		if((month == 12 && day >= 23) || (month == 1 && day <= 3)){
			//MyLog.d(TAG, "Intervalo navidad");
			intvlChristmas = 
					new Interval(new DateTime(year, 12, 24, 15, 0), new DateTime(year, 12, 26, 6, 0));
			data = getF2HSES(intvlChristmas, interval);
			data_final.put("indice", data_final.get("indice") + data.get("indice"));
			data_final.put("hours", data_final.get("hours") + data.get("hours"));

			//intervalo de fin de año
			intvlYearEnd = 
					new Interval(new DateTime(year, 12, 31, 15, 0), new DateTime(year + 1, 1, 2, 6, 0));
			data = getF2HSES(intvlYearEnd, interval);
			data_final.put("indice", data_final.get("indice") + data.get("indice"));
			data_final.put("hours", data_final.get("hours") + data.get("hours"));
			
			//año nuevo
			intvlNewYear = 
					new Interval(new DateTime(year - 1, 12, 31, 15, 0), new DateTime(year, 1, 2, 6, 0));
			data = getF2HSES(intvlNewYear, interval);
			data_final.put("indice", data_final.get("indice") + data.get("indice"));
			data_final.put("hours", data_final.get("hours") + data.get("hours"));
			//Log.d("horas fin de año", "horas fin de año:" + hours);
			list_intvl.add(intvlChristmas);
			list_intvl.add(intvlYearEnd);
			list_intvl.add(intvlNewYear);
		}

		//días festivos
		String dS = CuadranteDates.formatDate(dtStart);
		String dE = CuadranteDates.formatDate(dtEnd);
		//MyLog.d("buscar festivos entre las fechas", dS + " " + dE);
		List<ServicioInfo> holidays = db.getHolidaysFromInterval(dS, dE);
		if(holidays.size() > 0){
			//MyLog.d(TAG, "holidays size:" + holidays.size());
			Interval intvlHoliday;
			for(ServicioInfo holiday: holidays){	
				boolean isHSES = false;
				//miramos que el intervalo del festivo no esté dentro de los especiales
				if(intvlChristmas != null){
					if(intvlChristmas.gap(holiday.getInterval()) == null){
						if(intvlChristmas.overlaps(holiday.getInterval())) isHSES = true;
					}					
				}
				if(intvlYearEnd != null){
					if(intvlYearEnd.gap(holiday.getInterval()) == null){
						if(intvlYearEnd.overlaps(holiday.getInterval())) isHSES = true;
					}					
				}
				if(intvlNewYear != null){
					if(intvlNewYear.gap(holiday.getInterval()) == null){
						if(intvlNewYear.overlaps(holiday.getInterval())) isHSES = true;
					}					
				}
				//nos aseguramos que no entra en navidad y año nuevo
				if(!isHSES){
					intvlHoliday = new Interval(
							holiday.getDateTime(), 
							holiday.getDateTime().plusDays(1));
					//MyLog.d(TAG, "intervalo festivo: " + intvlHoliday.toString());
					//MyLog.d(TAG, "horas festivo: " + getF2Weekend(intvlHoliday, interval));
					data = getF2Weekend(intvlHoliday, interval, list_intvl);
					data_final.put("indice", data_final.get("indice") + data.get("indice"));
					data_final.put("hours", data_final.get("hours") + data.get("hours"));
					list_intvl.add(intvlHoliday);
				}
			}
		}		
		
		//MyLog.d(TAG, "después de festivos f2:" + hours);
		
		//intervalo lunes noche
		//MyLog.d("lunes", "lunes");
		Interval intvlMonday = getIntvlF2(dtStart, DateTimeConstants.MONDAY);
		data = getF2Weekdays(intvlMonday, interval, list_intvl);
		data_final.put("indice", data_final.get("indice") + data.get("indice"));
		data_final.put("hours", data_final.get("hours") + data.get("hours"));

		//Log.d("F2", "intervalo lunes:" + intvlMonday.toString());
		//intervalo martes noche
		//MyLog.d("martes", "martes");
		Interval intvlTuesday = getIntvlF2(dtStart, DateTimeConstants.TUESDAY);
		data = getF2Weekdays(intvlTuesday, interval, list_intvl);
		data_final.put("indice", data_final.get("indice") + data.get("indice"));
		data_final.put("hours", data_final.get("hours") + data.get("hours"));

		//Log.d("F2", "intervalo martes:" + intvlTuesday.toString());
		//intervalo miercoles noche
		//MyLog.d("miercoles", "miercoles");
		Interval intvlWednesday = getIntvlF2(dtStart, DateTimeConstants.WEDNESDAY);
		data = getF2Weekdays(intvlWednesday, interval, list_intvl);
		data_final.put("indice", data_final.get("indice") + data.get("indice"));
		data_final.put("hours", data_final.get("hours") + data.get("hours"));

		//Log.d("F2", "intervalo miercoles:" + intvlWednesday.toString());
		//intervalo jueves noche
		//MyLog.d("jueves", "jueves");
		Interval intvlThursday = getIntvlF2(dtStart, DateTimeConstants.THURSDAY);
		data = getF2Weekdays(intvlThursday, interval, list_intvl);
		data_final.put("indice", data_final.get("indice") + data.get("indice"));
		data_final.put("hours", data_final.get("hours") + data.get("hours"));

		//Log.d("F2", "intervalo jueves:" + intvlThursday.toString());
		//intervalo viernes noche
		//MyLog.d("viernes", "viernes");
		Interval intvlFriday = getIntvlF2(dtStart, DateTimeConstants.FRIDAY);
		data = getF2Weekdays(intvlFriday, interval, list_intvl);
		data_final.put("indice", data_final.get("indice") + data.get("indice"));
		data_final.put("hours", data_final.get("hours") + data.get("hours"));

		//Log.d("F2", "intervalo viernes:" + intvlFriday.toString());
		//intervalo fin de semana
		//MyLog.d("fin de semana", "fin de semana");
		Interval intvlWeekEnd = getIntvlF2(dtStart, DateTimeConstants.SATURDAY);
		//Log.d("F2", "intervalo fin de semana" + intvlWeekEnd);
		data = getF2Weekend(intvlWeekEnd, interval, list_intvl);
		data_final.put("indice", data_final.get("indice") + data.get("indice"));
		data_final.put("hours", data_final.get("hours") + data.get("hours"));
		
		MyLog.d("F2", "hours final:" + data_final.get("hours"));
		return data_final;		
	}	

	/**
	 * Obtiene el índice F2 en los horarios de especial significación
	 * @param intvlF2 intervalo de las horas de navidad y fin de año
	 * @param interval intervalo entre los dos horarios del servicio
	 * @return índice f2
	 */
	public static HashMap<String, Double> getF2HSES(Interval intvlF2, Interval interval){
		return _getF2(intvlF2, interval, 1, new ArrayList<Interval>());
	}
	
	/**
	 * Obtiene el índice F2 en los horarios de entre semana
	 * @param intvlF2 intervalo de las horas de noche 22:00 a 06:00
	 * @param interval intervalo entre los dos horarios del servicio
	 * @return índice f2
	 */
	public static HashMap<String, Double> getF2Weekdays(Interval intvlF2, Interval interval){
		return _getF2(intvlF2, interval, 0.25, new ArrayList<Interval>());
	}
	
	/**
	 * Obtiene el índice F2 en los horarios de entre semana
	 * @param intvlF2 intervalo de las horas de noche 22:00 a 06:00
	 * @param interval intervalo entre los dos horarios del servicio
	 * @param list Lista de intervalos que se restará el f2 si el intervalo de superposición de
	 * los horarios iniciales están dentro de cada uno de la lista
	 * @return índice f2
	 */
	public static HashMap<String, Double> getF2Weekdays(Interval intvlF2, Interval interval, List<Interval> list){
		return _getF2(intvlF2, interval, 0.25, list);
	}
	
	/**
	 * Obtiene el índice F2 en los horarios de fin de semana
	 * @param intvlF2 intervalo de las horas
	 * @param interval intervalo entre los dos horarios del servicio
	 * @return índice f2
	 */
	public static HashMap<String, Double> getF2Weekend(Interval intvlF2, Interval interval){
		return _getF2(intvlF2, interval, 0.5, new ArrayList<Interval>());
	}
	/**
	 * Obtiene el índice F2 en los horarios de fin de semana
	 * @param intvlF2 intervalo de las horas
	 * @param interval intervalo entre los dos horarios del servicio
	 * @param list Lista de intervalos que se restará el f2 si el intervalo de superposición de
	 * los horarios iniciales están dentro de cada uno de la lista
	 * @return índice f2
	 */
	public static HashMap<String, Double> getF2Weekend(Interval intvlF2, Interval interval, List<Interval> list){
		return _getF2(intvlF2, interval, 0.5, list);
	}
	
	/**
	 * Base para obtener el índice de F2
	 * @param intvlF2 intervalo de las horas de noche 22:00 a 06:00
	 * @param interval intervalo entre los dos horarios del servicio
	 * @param divisor 4 para entre semana, 2 para el finde, navidad y fin de año
	 * @return
	 */
	private static HashMap<String, Double> _getF2(
			Interval intvlF2, Interval interval, double divisor, List<Interval> list){
		double indice = 0;
		double hours = 0;
		if(intvlF2.gap(interval) == null){
			//miramos si hay superposición de uno con el otro
			if(intvlF2.overlaps(interval)){
				//MyLog.d("_getF2", intvlF2.toString());
				//obtenemos el periodo que hay de superposición
				Interval intvlOverlap = intvlF2.overlap(interval);
				hours = ((double)intvlOverlap.toDuration().getStandardMinutes()/60);
				MyLog.d(TAG, hours);
				indice = hours * divisor;
				if(list.size() > 0){
					for (Interval intvl : list) {
						//MyLog.d("_getF2 quitar", "intvlOverlap:" + intvlOverlap.toString() + " intvl:" + intvl.toString());
						if(intvlOverlap.overlaps(intvl)){
							Interval intvlOverlap2 = intvlOverlap.overlap(intvl);
							double hours2 = ((double)intvlOverlap2.toDuration().getStandardMinutes()/60);
							hours -= hours2;
							indice -= hours2 * divisor;
						}					
					}
				}
			}			
		}
		HashMap<String, Double> data = new HashMap<String, Double>();
		data.put("indice", indice);
		data.put("hours", hours);
		return data;
	}
	
	/**
	 * Devuelve el intervalo para el f2 dependiendo del día de la semana que se quiere saber
	 * De lunes a viernes de 22:00 a 06:00, y de sabado 15:00 a lunes 06:00
	 * @param dt
	 * @param dow Day Of Week DateTimeConstants.MONDAY
	 * @return
	 */
	public static Interval getIntvlF2(DateTime dt, int dow){
		int sHour = 22, sMin = 0, ePlusDays = 1, eHour = 6, eMin = 0;
		dt = getNearestDayOfWeek(dt, dow);
		if(dow > DateTimeConstants.FRIDAY){
			sHour = 15; sMin = 0;
			ePlusDays = 2; eHour = 6; eMin = 0;
		}
		return new Interval(
						dt.withHourOfDay(sHour).withMinuteOfHour(sMin), 
						dt.plusDays(ePlusDays).withHourOfDay(eHour).withMinuteOfHour(eMin));		
	}

	/**
	 * Devuelve la fecha más cercana a un día de la semana
	 * @param dt fecha
	 * @param dow día de la semana más cercano DateTimeConstants.MONDAY
	 * @return
	 */
	public static DateTime getNearestDayOfWeek(DateTime dt, int dow) {
		DateTime t1 = dt.withDayOfWeek(dow);
		DateTime t2 = t1.isBefore(dt) ? t1.plusWeeks(1) : t1.minusWeeks(1);
		return  Math.abs(Days.daysBetween(t1, dt).getDays()) < 
				Math.abs(Days.daysBetween(t2, dt).getDays()) ? t1 : t2;
	}

	/**
	 * Obtiene las horas de referencia de un mes
	 * @param month_days Dias naturales del més
	 * @param ordinary_days Días ordinarios no computables (5.35 por día)
	 * @param especial_days Días especiales no computables (7.5 por día)
	 * @param holidays Días festivos
	 * @param week_hours Cómputo de horas semanales (37.5)
	 * @return
	 */
	public static double getReferenceHours(
			double month_days, double ordinary_days, double especial_days, double holidays, 
			double week_hours){
		Log.d("getReferenHours", "month_days:" + month_days + " | ordinary_days:" + ordinary_days + " | especial_days:" + especial_days + " | holidays:" + holidays + " | week_hours:" + week_hours);
		double formula1 = (month_days - ordinary_days) / 7;
		//Log.d("getReferenceHours", "formula1:" + formula1);
		double formula2 = (especial_days * 1 + holidays * 1) / 5;	
		//Log.d("getReferenceHours", "formula2:" + formula2);
		double reference_hours = (formula1 - formula2) * week_hours;
		//reference_hours*= 100;
		//reference_hours = Math.round(reference_hours);
		//reference_hours/= 100;
		//return round(Math.round(reference_hours*100.0)/100.0);
		return round(doubleTwoDigits(reference_hours));	
	}

    /**
     * Devuele las horas de referencia según la nueva O.G, si son 4 semanas 150, 5 semanas 185 horas.
     * @param calendar
     * @return
     */
    public static int getReferenceHours2(Context ctx, Calendar calendar){
        Map<String, DateTime> aMap = CuadranteDates.getStartEndMonth(calendar);
        int weeks = Weeks.weeksBetween(
                aMap.get(CuadranteDates.FIRST_DAY), aMap.get(CuadranteDates.LAST_DAY)).getWeeks();
        MyLog.d(TAG, "first:" + aMap.get(CuadranteDates.FIRST_DAY) + " last:" + aMap.get(CuadranteDates.LAST_DAY));
        MyLog.d(TAG, "semanas:" + weeks);
        switch (Sp.getSpWorkdayWeekHours(ctx)) {
            case "37.5":
                if(weeks == 4) return 150;
                else if(weeks == 5) return 185;
                break;
            case "40":
                if(weeks == 4) return 160;
                else if(weeks == 5) return 200;
                break;
            default:
                return 0;
        }
        return 0;
    }
	
	/**
	 * Devuelve un double con solo dos dígitos.
	 * @param d
	 * @return
	 */
	public static double doubleTwoDigits(double d){
		d*= 100;
		d = Math.round(d);
		d/= 100;
		return d;
	}

	
	/**
	 * Redondea un double, si después de la coma no llega a 0.25, lo deja en 0.25, 0.5, 0.75 y si
	 * supera todos estos lo deja en el entero superior
	 * @param d
	 * @return
	 */
	public static double round(double d){
	    double dAbs = Math.abs(d);
	    int i = (int) dAbs;
	    double result = dAbs - (double) i;
	    double digits = 0.0;
	    if(result == 0){
	    	digits = 0;
	    /*}else if(result<=0.25){
	        digits = 0.25;            */
	    }else if(result<=0.5){
	        digits = 0.5;            
	    /*}else if(result<=0.75){
	        digits = 0.75;*/            
		}else{
	        digits = 1;            
	    }
	    return d<0 ? -(i+digits) : i+digits;  
	}	
	
	/**
	 * Refresca el widget del home screen
	 * @param context
	 */
	public static void refreshWidget(Context context){
		Intent intent = new Intent(context, AppWidgetWeekProvider.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int ids[] = AppWidgetManager.getInstance(context)
				.getAppWidgetIds(new ComponentName(context, AppWidgetWeekProvider.class));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		context.sendBroadcast(intent);		
	}
	
	/**
	 * Se borran las horas de la tabla Hours pertenecientes a date
	 * @param date Y-MM-dd
	 */
	public static void deleteHoursDate(Context context, String date){
		CuadranteDates tmpDate = new CuadranteDates(date);
		db = new DatabaseHandler(context);
		db.deleteHours(tmpDate.getYear(), tmpDate.getMonth());
		if(tmpDate.getDay() >= 28){
			CuadranteDates tmpDate2 = new CuadranteDates(tmpDate.getDateTime().plusMonths(1));
			db.deleteHours(tmpDate2.getYear(), tmpDate2.getMonth());
		}		
	}
	
	/**
	 * Verifica si los horarios de un servicio hacen que el servicio termine al dia siguiente
	 * @param startSchedule 00:00
	 * @param endSchedule
	 * @param startSchedule2
	 * @param endSchedule2
	 * @param startSchedule3
	 * @param endSchedule3
	 * @param startSchedule4
	 * @param endSchedule4
	 * @return true termina al dia siguiente, false no
	 */
	public static boolean isServiceEndNextDay(String startSchedule, String endSchedule, 
			String startSchedule2, String endSchedule2, String startSchedule3, String endSchedule3, 
			String startSchedule4, String endSchedule4){
		
		DateTime dtStart = new DateTime().withHourOfDay(CuadranteDates.getHour(startSchedule))
				.withMinuteOfHour(CuadranteDates.getMinutes(startSchedule));
		DateTime dtEnd = new DateTime().withHourOfDay(CuadranteDates.getHour(endSchedule))
				.withMinuteOfHour(CuadranteDates.getMinutes(endSchedule));
		DateTime dtStart2 = new DateTime().withHourOfDay(CuadranteDates.getHour(startSchedule2))
				.withMinuteOfHour(CuadranteDates.getMinutes(startSchedule2));
		DateTime dtEnd2 = new DateTime().withHourOfDay(CuadranteDates.getHour(endSchedule2))
				.withMinuteOfHour(CuadranteDates.getMinutes(endSchedule2));
		DateTime dtStart3 = new DateTime().withHourOfDay(CuadranteDates.getHour(startSchedule3))
				.withMinuteOfHour(CuadranteDates.getMinutes(startSchedule3));
		DateTime dtEnd3 = new DateTime().withHourOfDay(CuadranteDates.getHour(endSchedule3))
				.withMinuteOfHour(CuadranteDates.getMinutes(endSchedule3));
		DateTime dtStart4 = new DateTime().withHourOfDay(CuadranteDates.getHour(startSchedule4))
				.withMinuteOfHour(CuadranteDates.getMinutes(startSchedule4));
		DateTime dtEnd4 = new DateTime().withHourOfDay(CuadranteDates.getHour(endSchedule4))
				.withMinuteOfHour(CuadranteDates.getMinutes(endSchedule4));
		//MyLog.d(TAG, "1:" + dtStart + " | " + dtEnd);
		Duration dur1 = new Duration(dtStart, dtEnd);
		Duration dur2 = new Duration(dtStart2, dtEnd2);
		Duration dur3 = new Duration(dtStart3, dtEnd3);
		Duration dur4 = new Duration(dtStart4, dtEnd4);
		//MyLog.d(TAG, "interval1:" + dur1.getMillis());		
		
		if(dur1.getMillis() < 0 || dur2.getMillis() < 0 
				|| dur3.getMillis() < 0 || dur4.getMillis() < 0 ||
				((startSchedule.equals(endSchedule)
					&& !startSchedule.equals(Cuadrante.SCHEDULE_NULL)
					&& !endSchedule.equals(Cuadrante.SCHEDULE_NULL)) || 
				(startSchedule2.equals(endSchedule2)
					&& !startSchedule2.equals(Cuadrante.SCHEDULE_NULL)
					&& !endSchedule2.equals(Cuadrante.SCHEDULE_NULL)) || 
				(startSchedule3.equals(endSchedule3)
					&& !startSchedule3.equals(Cuadrante.SCHEDULE_NULL)
					&& !endSchedule3.equals(Cuadrante.SCHEDULE_NULL)) || 
				(startSchedule4.equals(endSchedule4)
					&& !startSchedule4.equals(Cuadrante.SCHEDULE_NULL)
					&& !endSchedule4.equals(Cuadrante.SCHEDULE_NULL)))){
			return true;
		}
		return false;
	}

    /**
     * Obtiene la jornada laboral diaria HOURS = 7 MINUTES = 30, u 8 y 0
     * @param ctx
     * @return
     */
    public static Map<String, Integer> getTimeWorkDate(Context ctx){
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        switch (Sp.getSpWorkdayWeekHours(ctx)) {
            case "37.5":
                aMap.put(HOURS , 7);
                aMap.put(MINUTES , 30);
                break;
            case "40":
                aMap.put(HOURS , 8);
                aMap.put(MINUTES , 0);
                break;
        }
        return aMap;
    }

    /**
     * De unos minutos devuelve las horas y minutos correspondientes
     * @param minutes
     * @return
     */
    public static Map<String, Integer> getHoursFromMinutes(int minutes){
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put(HOURS, 0);
        aMap.put(MINUTES, minutes);
        if (minutes >= 60) {
            //sumamos la división entre los minutos y 60
            aMap.put(HOURS, minutes / 60);
            //el resto de minutos los ponemos como minutos
            aMap.put(MINUTES, minutes % 60);
        }
        return aMap;
    }
}