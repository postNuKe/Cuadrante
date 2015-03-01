package es.progmac.cuadrante.lib;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Gestión de las variables SharedPreferences
 * @author david
 *
 */
public class Sp {
	public static SharedPreferences sharedPrefs;
	
	/**
	 * Nombre de la variable que guarda el número de dias de las vacaciones
	 */
	public static final String SP_DAYS_HOLIDAYS = "days_holidays";
	public static final String SP_DAYS_HOLIDAYS_DV = "22";
	/**
	 * Nombre de la variable que guarda el número de dias de asuntos particulares
	 */
	public static final String SP_DAYS_OWN_AFFAIRS = "days_own_affairs";
	public static final String SP_DAYS_OWN_AFFAIRS_DV = "5";
	/**
	 * Nombre de la variable que guarda el tipo de servicio asignado para las vacaciones
	 */
	public static final String SP_TYPE_SERVICE_HOLIDAYS = "type_service_holidays";
	/**
	 * Id del tipo de servicio asignado a las vacaciones por defecto
	 */
	public static final String SP_TYPE_SERVICE_HOLIDAYS_DV = "2";
	/**
	 * Nombre de la variable que guarda el tipo de servicio asignado para los asuntos particulares
	 */
	public static final String SP_TYPE_SERVICE_OWN_AFFAIRS = "type_service_own_affairs";
	/**
	 * Id del tipo de servicio asignado a los asuntos propios por defecto
	 */
	public static final String SP_TYPE_SERVICE_OWN_AFFAIRS_DV = "1";
	/**
	 * Nombre de la variable que guarda el tipo de servicio asignado para las bajas médicas
	 */
	public static final String SP_TYPE_SERVICE_MEDICAL_LEAVE = "type_service_medical_leave";
	/**
	 * Id del tipo de servicio asignado a las vacaciones por defecto
	 */
	public static final String SP_TYPE_SERVICE_MEDICAL_LEAVE_DV = "3";
	/**
	 * Nombre por defecto de la variable que guarda los dias que falten del mes
	 * para la notificación al usuario que no llega al F2
	 */
	public static final String SP_F2_NOTIFY_REST_DAYS = "f2_rest_days";
	public static final String SP_F2_NOTIFY_REST_DAYS_DV = "5";
	/**
	 * Variable que guarda si se activa o no la notificación para el F2
	 */
	public static final String SP_F2_NOTIFY_ACTIVE = "f2_notify_active";
	/**
	 * Nombre de la variable que guarda la fecha de cuando se notificó la ultima vez
	 */
	public static final String SP_F2_NOTIFY_DATE = "f2_notify_date";	
	/**
	 * Guarda el nombre de si se activa el sonido en la notificación del F2
	 */
	public static final String SP_F2_NOTIFY_FLAG_SOUND = "f2_notify_flag_sound";
	/**
	 * Nombre de la variable shared preference, donde se guarda el tiempo de auto-backup
	 */
	public static final String SP_BACKUP_DB_AUTO = "backup_db_auto";
	public static final String SP_BACKUP_DB_AUTO_DV = "month";
	/**
	 * Nombre de la variable de la fecha de la última copia de la bd
	 */
	public static final String SP_BACKUP_DB_AUTO_DATE = "auto_backup_db_date";
	/**
	 * Variable que guarda si es la primera vez que se ejecuta la app
	 * true es la primera vez, false no
	 */
	public static final String SP_IS_FIRST_RUN = "app_is_first_run";
	/**
	 * Variable que guarda la versión de la applicación para mostrar el changelog
	 */
	public static final String SP_CHANGELOG_VERSION_KEY = "changelog_version_key";
	/**
	 * Nombre de la variable
	 */
	public static final String SP_EMAIL = "email";
	public static final String SP_EMAIL_DV = "tuemail@email.com";
	
	/**
	 * Variable que guarda la fecha de la última notificación del F2
	 */
	public static final String SP_ALERT_NOTIFY_DATE = "notifyDate";	
	/**
	 * Nombre de la variable que guarda si se mostrará la ventana de votar o no
	 */
	public static final String SP_SHOW_VOTE_FOR_ME = "show_vote_for_me";
	public static final boolean SP_SHOW_VOTE_FOR_ME_DV = true;
	/**
	 * Nombre de la variable que guarda cuantas veces se ha ejecutado la app
	 */
	public static final String SP_NUM_RUN = "num_run";
	public static final int SP_NUM_RUN_DV = 0;
	/**
	 * Nombre de la variable que guarda la dieta diaria
	 */
	public static String SP_DAILY_EXPENSES = "daily_expenses";
	public static String SP_DAILY_EXPENSES_DV = "0";//77.13
	/**
	 * Nombre de la variable que guarda la manutención diaria
	 */
	public static String SP_DAILY_MANUTENCION_EXPENSES = "daily_manutencion_expenses";
	public static String SP_DAILY_MANUTENCION_EXPENSES_DV = "0";//28.21
	/**
	 * Nombre de la variable que guarda el cómputo de horas semanales
	 */
	public static String SP_COMPUTING_HOURS_PER_WEEK = "computing_hours_per_week";
	public static String SP_COMPUTING_HOURS_PER_WEEK_DV = "0";//37.5
	
	/**
	 * Listado de tipos de servicios seleccionados para las comisiones
	 */
	public static String SP_TYPES_SERVICES_COMISION = "types_services_comision";
	/**
	 * Tipo de servicio seleccionado por defecto de la listado de tipos para
	 * comisiones.
	 */
	public static String SP_TYPE_SERVICE_DEFAULT_COMISION = "type_service_default_comision";
	/**
	 * Variable que guarda si se activa o no el P.A.S
	 */
	public static final String SP_PAS_ACTIVE = "pas_active";
	/**
	 * Variable que guarda en la semana actual que tipo servicios toca
	 */
	public static final String SP_PAS = "pas";
	/**
	 * tipo de semana por defecto del Pas
	 */
	public static final String SP_PAS_DV = "p";
	/**
	 * Variable que guarda el año de cuando se ha activado el pas
	 */
	public static final String SP_PAS_YEAR = "pas_year";
	/**
	 * Variable que guarda que semana del año que se ha activado
	 */
	public static final String SP_PAS_WEEK = "pas_week";
	/**
	 * Variable que guarda el número de caracteres máximos para las abreviaturas
	 * de los servicios
	 */
	public static final String SP_LENGTH_SERVICE_NAME = "length_service_name";
	/**
	 * Número de caracteres como máximo para las abreviaturas de los servicios
	 */
	public static final String SP_LENGTH_SERVICE_NAME_DV = "3";
	/**
	 * Variable que guarda si se activa o no el intervalo de 15 minutos
	 */
	public static final String SP_INTERVAL_MINUTES = "interval_minutes_active";
	/**
	 * Valor por defecto para el intervalo de 15 minutos en el timepicker
	 */
	public static final boolean SP_INTERVAL_MINUTES_DV = false;

	/** Variable que guarda si está activo o no el login en la app */
	public static final String SP_LOGIN_ACTIVATE = "login_activate";
	/** Valor por defecto para la activación del login en la app */
	public static final boolean SP_LOGIN_ACTIVATE_DV = false;
	/** Variable que guarda la contraseña para la app si el login está activado */
	public static final String SP_LOGIN_PASSWORD = "login_password";
	/** Valor por defecto para el password del login */
	public static final String SP_LOGIN_PASSWORD_DV = "";
	/** Variable que guarda el tiempo de expiración de la sesión del login */
	public static final String SP_LOGIN_EXPIRE_SESSION_DURATION = "login_expire_session_duration";
	/** Valor por defecto para el tiempo de expiración de la sesión */
	public static final int SP_LOGIN_EXPIRE_SESSION_DURATION_DV = 0;
	/** Variable que guarda la última fecha de autentificación del usuario */
	public static final String SP_LOGIN_EXPIRE_SESSION_DATE = "login_expire_session_date";
	/** Valor por defecto para la última fecha de autentificación del usuario */
	public static final String SP_LOGIN_EXPIRE_SESSION_DATE_DV = "1982-07-22 08:00";	
	
	/** Variable que guarda si está activo o no el login en la app */
	public static final String SP_WIDGET_ACTIVATE = "widget_activate";
	public static final boolean SP_WIDGET_ACTIVATE_DV = false;

	/** Variable que guarda del widget semanal que dia comienza */
	public static final String SP_WIDGET_WEEK_FIRST_DAY = "widget_week_first_day";
	public static final String SP_WIDGET_WEEK_FIRST_DAY_DV = "today";
	
	/** Variable que guarda si los domingos tienen que tener color rojo */
	public static final String SP_SUNDAY_COLOR_ACTIVE = "sunday_color_active";
	public static final boolean SP_SUNDAY_COLOR_ACTIVE_DV = false;
	
	/** Variable que guarda el color de fondo para los domingos */
	public static final String SP_SUNDAY_BG_COLOR = "sunday_bg_color";
	public static final int SP_SUNDAY_BG_COLOR_DV = Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY;
	
	/** Variable que guarda el color de texto para los domingos */
	public static final String SP_SUNDAY_TEXT_COLOR = "sunday_text_color";
	public static final int SP_SUNDAY_TEXT_COLOR_DV = Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY;
	
	/** Variable que guarda si se pregunta al guardar un servicio de 24 horas */
	public static final String SP_ASK_SERVICE_24_HOURS = "ask_service_24_hours";
	public static final boolean SP_ASK_SERVICE_24_HOURS_DV = true;
	
	/** Variable que guarda si se activa o no el ver las comisiones cortas o largas */
	public static final String SP_COMMISSIONS_DAYS_LENGTH_ACTIVE = "commissions_days_length_active";
	public static final boolean SP_COMMISSIONS_DAYS_LENGTH_ACTIVE_DV = false;
	/** Variable que guarda como se ve el total de dieta en cada comisión */
	public static final String SP_COMMISSIONS_TOTAL_EXPENSES = "commissions_total_expenses";
	public static final String SP_COMMISSIONS_TOTAL_EXPENSES_DV = "0";
	/** Variable que guarda la longitud para las comisiones para cortas o largas */
	public static final String SP_COMMISSIONS_DAYS_LENGTH = "commissions_days_length";
	public static final int SP_COMMISSIONS_DAYS_LENGTH_DV = 10;
	/** Variable que guarda si se activa o no el ver el derecho a recibir la manutención en
	 * comisiones de un solo día */
	public static final String SP_COMMISSIONS_MANUTENCION_ACTIVE = "commissions_manutencion_active";
	public static final boolean SP_COMMISSIONS_MANUTENCION_ACTIVE_DV = true;

	/** Variable que guarda el último tipo de servicio usado en el calendario */
	public static final String SP_LAST_USED_TYPE_SERVICE_NAME = "last_type_service";
	/** Número de caracteres como máximo para las abreviaturas de los servicios */
	public static final int SP_LAST_USED_TYPE_SERVICE_NAME_DV = 0;
	
	/** Variable que guarda si el día actual no tendrá el color por defecto */
	public static final String SP_TODAY_DEFAULT_COLOR_ACTIVE = "today_default_color_active";
	public static final boolean SP_TODAY_DEFAULT_COLOR_ACTIVE_DV = false;
	
	/** Variable que guarda el color de fondo para el día actual */
	public static final String SP_TODAY_BG_COLOR = "today_bg_color";
	public static final int SP_TODAY_BG_COLOR_DV = Cuadrante.TODAY_DEFAULT_BG_COLOR;
	
	/** Variable que guarda el color de texto para el día actual */
	public static final String SP_TODAY_TEXT_COLOR = "today_text_color";
	public static final int SP_TODAY_TEXT_COLOR_DV = Cuadrante.TODAY_DEFAULT_TEXT_COLOR;
	
	/** Variable que guarda el color de fondo para los festivos */
	public static final String SP_HOLIDAY_BG_COLOR = "holiday_bg_color";
	public static final int SP_HOLIDAY_BG_COLOR_DV = Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY;
	/** Variable que guarda el color de texto para los festivos */
	public static final String SP_HOLIDAY_TEXT_COLOR = "holiday_text_color";
	public static final int SP_HOLIDAY_TEXT_COLOR_DV = Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY;
	
	/** Variable que guarda el color de fondo para los servicios por defecto */
	public static final String SP_SERVICE_DEFAULT_BG_COLOR = "service_default_bg_color";
	public static final int SP_SERVICE_DEFAULT_BG_COLOR_DV = Cuadrante.SERVICE_DEFAULT_BG_COLOR;
	/** Variable que guarda el color de texto para los servicios por defecto */
	public static final String SP_SERVICE_DEFAULT_TEXT_COLOR = "service_default_text_color";
	public static final int SP_SERVICE_DEFAULT_TEXT_COLOR_DV = Cuadrante.SERVICE_DEFAULT_TEXT_COLOR;
	
	/** Variable que guarda si se quiere mostrar la opción 'Entrar en el servicio' */
	public static final String SP_ENTER_TO_SERVICE = "enter_to_service";
	public static final boolean SP_ENTER_TO_SERVICE_DV = true;
	/** Variable que guarda si se quiere mostrar la opción 'Crear nuevo tipo de servicio' */
	public static final String SP_CREATE_NEW_TYPE_SERVICE = "create_new_type_service";
	public static final boolean SP_CREATE_NEW_TYPE_SERVICE_DV = true;

    /** Variable que guarda que formato de calendario se debe de mostrar */
    public static final String SP_WORKDAY_WEEK_HOURS = "workday_week_hours";
    /** Tipo de calendario a mostrar*/
    public static final String SP_WORKDAY_WEEK_HOURS_DV = "37.5";

    /** Variable que guarda que computo mensual se debe mostrar, mensual, trimestral, cuatrimestral */
    public static final String SP_WORKDAY_COMPUTING_HOURS = "workday_computing_hours";
    /** Tipo de calendario a mostrar*/
    public static final String SP_WORKDAY_COMPUTING_HOURS_DV = "monthly";

	/**
	 * Map que guarda los nombres de las shared_preferences y que tipo de 
	 * dato son para usarlos después en la importación de la copia de seguridad
	 */
    public static final Map<String, String> SHARED_PREFERENCES = new HashMap<String, String>();
    static{
    	SHARED_PREFERENCES.put(SP_ALERT_NOTIFY_DATE, "string");		    
    	SHARED_PREFERENCES.put(SP_ASK_SERVICE_24_HOURS, "boolean");		    
    	SHARED_PREFERENCES.put(SP_BACKUP_DB_AUTO, "string");
    	SHARED_PREFERENCES.put(SP_BACKUP_DB_AUTO_DATE, "string");
    	SHARED_PREFERENCES.put(SP_CHANGELOG_VERSION_KEY, "int");
    	SHARED_PREFERENCES.put(SP_COMMISSIONS_DAYS_LENGTH, "int");
    	SHARED_PREFERENCES.put(SP_COMMISSIONS_DAYS_LENGTH_ACTIVE, "boolean");
    	SHARED_PREFERENCES.put(SP_COMMISSIONS_MANUTENCION_ACTIVE, "boolean");
    	SHARED_PREFERENCES.put(SP_COMMISSIONS_TOTAL_EXPENSES, "string");
    	SHARED_PREFERENCES.put(SP_COMPUTING_HOURS_PER_WEEK, "string");
    	SHARED_PREFERENCES.put(SP_CREATE_NEW_TYPE_SERVICE, "boolean");		    
    	SHARED_PREFERENCES.put(SP_DAILY_EXPENSES, "string");
    	SHARED_PREFERENCES.put(SP_DAILY_MANUTENCION_EXPENSES, "string");
    	SHARED_PREFERENCES.put(SP_DAYS_HOLIDAYS, "string");
    	SHARED_PREFERENCES.put(SP_DAYS_OWN_AFFAIRS, "string");
    	SHARED_PREFERENCES.put(SP_EMAIL, "string");
    	SHARED_PREFERENCES.put(SP_ENTER_TO_SERVICE, "boolean");		    
    	SHARED_PREFERENCES.put(SP_F2_NOTIFY_ACTIVE, "boolean");
    	SHARED_PREFERENCES.put(SP_F2_NOTIFY_FLAG_SOUND, "boolean");
    	SHARED_PREFERENCES.put(SP_F2_NOTIFY_REST_DAYS, "string");
    	SHARED_PREFERENCES.put(SP_HOLIDAY_BG_COLOR, "int");		        	
    	SHARED_PREFERENCES.put(SP_HOLIDAY_TEXT_COLOR, "int");		    
    	SHARED_PREFERENCES.put(SP_INTERVAL_MINUTES, "boolean");
    	SHARED_PREFERENCES.put(SP_IS_FIRST_RUN, "boolean");
    	SHARED_PREFERENCES.put(SP_LAST_USED_TYPE_SERVICE_NAME, "int");
    	SHARED_PREFERENCES.put(SP_LENGTH_SERVICE_NAME, "string");
    	SHARED_PREFERENCES.put(SP_LOGIN_ACTIVATE, "boolean");
    	SHARED_PREFERENCES.put(SP_LOGIN_PASSWORD, "string");
    	SHARED_PREFERENCES.put(SP_NUM_RUN, "int");		    
    	SHARED_PREFERENCES.put(SP_PAS_ACTIVE, "boolean");
    	SHARED_PREFERENCES.put(SP_PAS, "string");
    	SHARED_PREFERENCES.put(SP_PAS_YEAR, "int");
    	SHARED_PREFERENCES.put(SP_PAS_WEEK, "int");
    	SHARED_PREFERENCES.put(SP_SERVICE_DEFAULT_BG_COLOR, "int");		        	
    	SHARED_PREFERENCES.put(SP_SERVICE_DEFAULT_TEXT_COLOR, "int");		        	
    	SHARED_PREFERENCES.put(SP_SHOW_VOTE_FOR_ME, "boolean");	
    	SHARED_PREFERENCES.put(SP_SUNDAY_BG_COLOR, "int");		        	
    	SHARED_PREFERENCES.put(SP_SUNDAY_COLOR_ACTIVE, "boolean");		    
    	SHARED_PREFERENCES.put(SP_SUNDAY_TEXT_COLOR, "int");		    
    	SHARED_PREFERENCES.put(SP_TODAY_BG_COLOR, "int");		        	
    	SHARED_PREFERENCES.put(SP_TODAY_DEFAULT_COLOR_ACTIVE, "boolean");		    
    	SHARED_PREFERENCES.put(SP_TODAY_TEXT_COLOR, "int");		    
    	SHARED_PREFERENCES.put(SP_TYPE_SERVICE_DEFAULT_COMISION, "string");
    	SHARED_PREFERENCES.put(SP_TYPE_SERVICE_HOLIDAYS, "string");
    	SHARED_PREFERENCES.put(SP_TYPE_SERVICE_OWN_AFFAIRS, "string");
    	SHARED_PREFERENCES.put(SP_TYPE_SERVICE_MEDICAL_LEAVE, "string");
    	SHARED_PREFERENCES.put(SP_TYPES_SERVICES_COMISION, "string");
    	SHARED_PREFERENCES.put(SP_WIDGET_ACTIVATE, "boolean");
        SHARED_PREFERENCES.put(SP_WIDGET_WEEK_FIRST_DAY, "string");
        SHARED_PREFERENCES.put(SP_WORKDAY_WEEK_HOURS, "string");
        SHARED_PREFERENCES.put(SP_WORKDAY_COMPUTING_HOURS, "string");
    };
    


	public Sp() {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * De un shared preference en formato String devuelve su valor en int, y si el valor es ""
     * devuelve defValue y si a su vez es "" devuelve 0
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getSpStringToInt(Context context, String key, String defValue){
    	if(defValue.equals("") || defValue.equals(null)) defValue = "0";
    	String value = PreferenceManager.getDefaultSharedPreferences(context)
    			.getString(key, defValue);
    	if(value.equals("") || value.equals(null)) value = defValue;
		return Integer.parseInt(value);
    	
    }
    
    /**
     * De un shared preference en formato String devuelve su valor en float, y si el valor es ""
     * devuelve defValue y si a su vez es "" devuelve 0
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static float getSpStringToFloat(Context context, String key, String defValue){
    	if(defValue.equals("") || defValue.equals(null)) defValue = "0";
    	String value = PreferenceManager.getDefaultSharedPreferences(context)
    			.getString(key, defValue);
    	if(value.equals("") || value.equals(null)) value = defValue;
		return Float.parseFloat(value);
    }	
	/**
	 * Devuelve si está activado o no el login en la app
	 * @param context
	 * @return true o false
	 */
    public static boolean getLoginActivate(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_LOGIN_ACTIVATE, SP_LOGIN_ACTIVATE_DV);
    }
    
    /**
     * Establece si el login se activa o no en la app
     * @param context
     * @param activate
     */
    public static void setLoginActivate(Context context, boolean activate){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putBoolean(SP_LOGIN_ACTIVATE, activate).commit();
    }
    
    /**
     * Devuelve la contraseña del login
     * @param context
     * @return String
     */
    public static String getLoginPassword(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_LOGIN_PASSWORD, SP_LOGIN_PASSWORD_DV);    	
    }
    
    /**
     * Estable la contraseña del login
     * @param context
     * @param password
     */
    public static void setLoginPassword(Context context, String password){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putString(SP_LOGIN_PASSWORD, password).commit();
    }
    
    /**
     * Devuelve la duración que debe de durar la sesión
     * @param context
     * @return String
     */
    public static int getLoginExpireSessionDuration(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_LOGIN_EXPIRE_SESSION_DURATION, SP_LOGIN_EXPIRE_SESSION_DURATION_DV);    	
    }
    
    /**
     * Estable la duración que debe de durar la sesión
     * @param context
     * @param expire
     */
    public static void setLoginExpireSessionDuration(Context context, int expire){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putInt(SP_LOGIN_EXPIRE_SESSION_DURATION, expire).commit();
    }
    
    /**
     * Devuelve la fecha cuando se ha logeado el usuario por última vez
     * @param context
     * @return String
     */
    public static String getLoginExpireSessionDate(Context context){
        String var = PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_LOGIN_EXPIRE_SESSION_DATE, SP_LOGIN_EXPIRE_SESSION_DATE_DV);   
        if(var.equals("")) var = SP_LOGIN_EXPIRE_SESSION_DATE_DV;
        return var;
    }
    
    /**
     * Establece la fecha cuando se ha logeado el usuario por última vez
     * @param context
     * @param date
     */
    public static void setLoginExpireSessionDate(Context context, String date){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putString(SP_LOGIN_EXPIRE_SESSION_DATE, date).commit();
    }
    
    /**
     * Devuelve el número de caracteres como máximo a mostrar en las abreviaturas
     * de los servicios
     * @param context
     */
    public static int getLengthServiceName(Context context){
    	//al ser una preferencia grabada con el EditTextPreference, se graba
    	//como String, asi que para obtener el valor que sabemos que int
    	/*
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_LENGTH_SERVICE_NAME, SP_LENGTH_SERVICE_NAME_DV));
                */
    	return getSpStringToInt(context, SP_LENGTH_SERVICE_NAME, SP_LENGTH_SERVICE_NAME_DV);
    }  

    /**
     * Devuelve el valor de cada cuanto hay que generar el backup
     * @param context
     */
    public static String getBackupDbAuto(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_BACKUP_DB_AUTO, SP_BACKUP_DB_AUTO_DV);   
    }  

    /**
     * Devuelve la fecha de cuando fue la última vez que se hizo el backup
     * @param context
     */
    public static String getBackupDbAutoDate(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_BACKUP_DB_AUTO_DATE, "");   
    }  
    
    /**
     * Establece la fecha cuando se ha hecho el backup
     * @param context
     * @param date
     */
    public static void setBackupDBAutoDate(Context context, String date){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putString(SP_BACKUP_DB_AUTO_DATE, date).commit();
    }    

	/**
	 * Devuelve si se es la primera vez que se ejecuta la app
	 * @param context
	 * @return true o false
	 */
    public static boolean getIsFirstRun(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_IS_FIRST_RUN, true);
    }
    
    /**
     * Establece el que ya se haya ejecutado la app por primera vez
     * @param context
     * @param value
     */
    public static void setIsFirstRun(Context context, boolean value){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putBoolean(SP_IS_FIRST_RUN, value).commit();
    }    

	/**
	 * Devuelve si hay que mostrar o no la ventana de votar
	 * @param context
	 * @return true o false
	 */
    public static boolean getShowVoteForMe(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_SHOW_VOTE_FOR_ME, SP_SHOW_VOTE_FOR_ME_DV);
    }
    
    /**
     * Establece si a partir de ahora se mostrará o no la ventana de votar por la app
     * @param context
     * @param show
     */
    public static void setShowVoteForMe(Context context, boolean show){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putBoolean(SP_SHOW_VOTE_FOR_ME, show).commit();
    }    

    /**
     * Devuelve las veces que se ha ejecutado la aplicación
     * @param context
     */
    public static int getNumRun(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_NUM_RUN, SP_NUM_RUN_DV);   
    }  

    /**
     * Estable la duración que debe de durar la sesión
     * @param context
     * @param count
     */
    public static void setNumRun(Context context, int count){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putInt(SP_NUM_RUN, count).commit();
    }
    
    /**
     * Devuelve el email del usuario
     * @param context
     */
    public static String getEmail(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_EMAIL, SP_EMAIL_DV);   
    }  
    
    /**
     * Establece el email del usuario
     * @param context
     * @param email
     */
    public static void setEmail(Context context, String email){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putString(SP_EMAIL, email).commit();
    }   
    
    /**
     * Establece el email del usuario con el valor por defecto
     * @param context
     */
    public static void setEmail(Context context){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putString(SP_EMAIL, SP_EMAIL_DV).commit();
    }    
    
    /**
     * Devuelve la versión guardada de la aplicación
     * @param context
     */
    public static int getChangeLogVersionKey(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_CHANGELOG_VERSION_KEY, 0);   
    }  
    
    /**
     * Estable el número de versión de la app
     * @param context
     * @param version
     */
    public static void setChangeLogVersionKey(Context context, int version){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putInt(SP_CHANGELOG_VERSION_KEY, version).commit();
    }
    
	/**
	 * Si está activado o no la notificación para el f2
	 * @param context
	 * @return true o false
	 */
    public static boolean getF2NotifyActive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_F2_NOTIFY_ACTIVE, true);
    }

    /**
     * Devuelve la fecha de cuando fue la última vez que se notificó el f2
     * @param context
     */
    public static String getF2NotifyDate(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_F2_NOTIFY_DATE, "");   
    }  
    
    /**
     * Establece la fecha de cuando se ha notificado el f2
     * @param context
     * @param date
     */
    public static void setF2NotifyDate(Context context, String date){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putString(SP_F2_NOTIFY_DATE, date).commit();
    }    

    /**
     * Devuelve cuantos dias son para el fin del mes para la notificación del f2
     * @param context
     */
    public static int getF2NotifyRestDays(Context context){
    	/*
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_F2_NOTIFY_REST_DAYS, SP_F2_NOTIFY_REST_DAYS_DV);  
                */
    	return getSpStringToInt(context, SP_F2_NOTIFY_REST_DAYS, SP_F2_NOTIFY_REST_DAYS_DV);
    }  

    /**
     * Establece los días que faltan para la notificación del f2
     * @param context
     * @param days
     */
    public static void setF2NotifyRestDays(Context context, String days){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putString(SP_F2_NOTIFY_REST_DAYS, days).commit();
    }    

	/**
	 * Devuelve si está activado el sonido para la notificación del f2
	 * @param context
	 * @return true o false
	 */
    public static boolean getF2NotifyFlagSound(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_F2_NOTIFY_FLAG_SOUND, true);
    }    
    
    /**
     * Devuelve el tipo de servicio vinculado con las vacaciones
     * de los servicios
     * @param context
     */
    public static int getHolidaysTypeService(Context context){
    	//al ser una preferencia grabada con el EditTextPreference, se graba
    	//como String, asi que para obtener el valor que sabemos que int
    	/*
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_TYPE_SERVICE_HOLIDAYS, SP_TYPE_SERVICE_HOLIDAYS_DV)); 
                */
    	return getSpStringToInt(context, SP_TYPE_SERVICE_HOLIDAYS, SP_TYPE_SERVICE_HOLIDAYS_DV);
    }  
    
    /**
     * Devuelve el número de dias de vacaciones
     * de los servicios
     * @param context
     */
    public static int getHolidaysDays(Context context){
    	//al ser una preferencia grabada con el EditTextPreference, se graba
    	//como String, asi que para obtener el valor que sabemos que int
        return getSpStringToInt(context, SP_DAYS_HOLIDAYS, SP_DAYS_HOLIDAYS_DV);   
    }  

    
    /**
     * Devuelve el tipo de servicio vinculado con los asuntos particulares
     * de los servicios
     * @param context
     */
    public static int getOwnAffairsTypeService(Context context){
    	//al ser una preferencia grabada con el EditTextPreference, se graba
    	//como String, asi que para obtener el valor que sabemos que int
    	return getSpStringToInt(context, SP_TYPE_SERVICE_OWN_AFFAIRS, SP_TYPE_SERVICE_OWN_AFFAIRS_DV);
    	/*
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_TYPE_SERVICE_OWN_AFFAIRS, SP_TYPE_SERVICE_OWN_AFFAIRS_DV));   
                */
    }  
    
    /**
     * Devuelve el número de dias de asuntos particulares
     * de los servicios
     * @param context
     */
    public static int getOwnAffairsDays(Context context){
    	//al ser una preferencia grabada con el EditTextPreference, se graba
    	//como String, asi que para obtener el valor que sabemos que int
    	/*
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_DAYS_OWN_AFFAIRS, SP_DAYS_OWN_AFFAIRS_DV));   
                */
    	return getSpStringToInt(context, SP_DAYS_OWN_AFFAIRS, SP_DAYS_OWN_AFFAIRS_DV);
    }  
    
    /**
     * Devuelve el tipo de servicio vinculado con las bajas médicas
     * @param context
     */
    public static int getTypeServiceMedicalLeave(Context context){
    	return getSpStringToInt(
    			context, SP_TYPE_SERVICE_MEDICAL_LEAVE, SP_TYPE_SERVICE_MEDICAL_LEAVE_DV);
    }  
    
    /**
     * Establece tipo de servicio vinculado a las bajas médicas
     * @param context
     * @param id
     */
    public static void setTypeServiceMedicalLeave(Context context, int id){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putString(SP_TYPE_SERVICE_MEDICAL_LEAVE, String.valueOf(id)).commit();
    }
    
    /**
     * Devuelve la dieta diaria
     * @param context
     */
    public static String getDailyExpenses(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_DAILY_EXPENSES, SP_DAILY_EXPENSES_DV);   
    }
    
    /**
     * Devuelve la manutención diaria
     * @param context
     */
    public static String getDailyManutencionExpenses(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_DAILY_MANUTENCION_EXPENSES, SP_DAILY_MANUTENCION_EXPENSES_DV);   
    }    
    
    /**
     * Devuelve el cómputo de horas semanales
     * @param context
     */
    public static Float getComputingHoursPerWeek(Context context){
        return getSpStringToFloat(
        		context, SP_COMPUTING_HOURS_PER_WEEK, SP_COMPUTING_HOURS_PER_WEEK_DV);   
    }    
    
	/**
	 * Si está activado o no el Pas
	 * @param context
	 * @return true o false
	 */
    public static boolean getPasActive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_PAS_ACTIVE, false);
    }    
 
    /**
     * Devuelve la semana de Pas seleccionada por el usuario
     * @param context
     */
    public static String getPas(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_PAS, SP_PAS_DV);   
    } 
    
    /**
     * Establece la semana del Pas
     * @param context
     */
    public static void setPas(Context context, String pas){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SP_PAS, pas).commit();  
    }    
    
    /**
     * Devuelve el año cuando se activo el Pas
     * @param context
     */
    public static int getPasYear(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_PAS_YEAR, new DateTime().getYear());   
    }        
    
    /**
     * Establece el año de selección del Pas
     * @param context
     * @param year
     */
    public static void setPasYear(Context context, int year){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_PAS_YEAR, year).commit();
    }
    
    /**
     * Devuelve la semana del año cuando se activo el Pas
     * @param context
     */
    public static int getPasWeek(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_PAS_WEEK, new DateTime().getWeekOfWeekyear());   
    }        
    
    /**
     * Establece la semana de selección del Pas
     * @param context
     * @param week
     */
    public static void setPasWeek(Context context, int week){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_PAS_WEEK, week).commit();
    }
    
	/**
	 * Si está activado o no el intervalo de minutos en servicios o tipos
	 * @param context
	 * @return true o false
	 */
    public static boolean getIntervalMinutesActive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_INTERVAL_MINUTES, SP_INTERVAL_MINUTES_DV);
    }    
    
	/**
	 * Devuelve si está activado o no el widget en la app
	 * @param context
	 * @return true o false
	 */
    public static boolean getWidgetActivate(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_WIDGET_ACTIVATE, SP_WIDGET_ACTIVATE_DV);
    }
    
    /**
     * Establece si el widget se activa o no en la app
     * @param context
     * @param activate
     */
    public static void setWidgetActivate(Context context, boolean activate){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
    			putBoolean(SP_WIDGET_ACTIVATE, activate).commit();
    }

    /**
     * Devuelve el día que comienza el widget semanal
     * @param context
     */
    public static String getWidgetWeekFistDay(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_WIDGET_WEEK_FIRST_DAY, SP_WIDGET_WEEK_FIRST_DAY_DV);   
    } 
    
	/**
	 * Si está activa o no los domingos en color rojo
	 * @param context
	 * @return true o false
	 */
    public static boolean getSundayColorActive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_SUNDAY_COLOR_ACTIVE, SP_SUNDAY_COLOR_ACTIVE_DV);
    } 
    
    /**
     * Devuelve color de fondo para los domingos
     * @param context
     */
    public static int getSundayBgColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_SUNDAY_BG_COLOR, SP_SUNDAY_BG_COLOR_DV);   
    }        
    
    /**
     * Establece el color de fondo para los domingos
     * @param context
     * @param color
     */
    public static void setSundayBgColor(Context context, int color){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_SUNDAY_BG_COLOR, color).commit();
    }
    
    /**
     * Devuelve color de texto para los domingos
     * @param context
     */
    public static int getSundayTextColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_SUNDAY_TEXT_COLOR, SP_SUNDAY_TEXT_COLOR_DV);   
    }        
    
    /**
     * Establece el color de texto para los domingos
     * @param context
     * @param color
     */
    public static void setSundayTextColor(Context context, int color){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_SUNDAY_TEXT_COLOR, color).commit();
    }
    
	/**
	 * Si está activa o no el color por defecto para el día actual
	 * @param context
	 * @return true colores cambiados, false por defecto
	 */
    public static boolean getTodayColorActive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_TODAY_DEFAULT_COLOR_ACTIVE, SP_TODAY_DEFAULT_COLOR_ACTIVE_DV);
    } 
    
    /**
     * Devuelve color de fondo para el día actual
     * @param context
     */
    public static int getTodayBgColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_TODAY_BG_COLOR, SP_TODAY_BG_COLOR_DV);   
    }        
    
    /**
     * Establece el color de fondo para el día actual
     * @param context
     * @param color
     */
    public static void setTodayBgColor(Context context, int color){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_TODAY_BG_COLOR, color).commit();
    }
    
    /**
     * Devuelve color de texto para el día actual
     * @param context
     */
    public static int getTodayTextColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_TODAY_TEXT_COLOR, SP_TODAY_TEXT_COLOR_DV);   
    }        
    
    /**
     * Establece el color de texto para el día actual
     * @param context
     * @param color
     */
    public static void setTodayTextColor(Context context, int color){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_TODAY_TEXT_COLOR, color).commit();
    }  
    
    /**
     * Devuelve color de fondo para los festivos
     * @param context
     */
    public static int getHolidayBgColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_HOLIDAY_BG_COLOR, SP_HOLIDAY_BG_COLOR_DV);   
    }        
    /**
     * Establece el color de fondo para los festivos
     * @param context
     * @param color
     */
    public static void setHolidayBgColor(Context context, int color){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_HOLIDAY_BG_COLOR, color).commit();
    }
    
    /**
     * Devuelve color de texto para los festivos
     * @param context
     */
    public static int getHolidayTextColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_HOLIDAY_TEXT_COLOR, SP_HOLIDAY_TEXT_COLOR_DV);   
    }        
    /**
     * Establece el color de texto para los festivos
     * @param context
     * @param color
     */
    public static void setHolidayTextColor(Context context, int color){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_HOLIDAY_TEXT_COLOR, color).commit();
    }   
    
    /**
     * Devuelve color de fondo para los servicos por defecto
     * @param context
     */
    public static int getServiceDefaultBgColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_SERVICE_DEFAULT_BG_COLOR, SP_SERVICE_DEFAULT_BG_COLOR_DV);   
    }        
    /**
     * Establece el color de fondo para los servicos por defecto
     * @param context
     * @param color
     */
    public static void setServiceDefaultBgColor(Context context, int color){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_SERVICE_DEFAULT_BG_COLOR, color).commit();
    }
    
    /**
     * Devuelve color de texto para los servicos por defecto
     * @param context
     */
    public static int getServiceDefaultTextColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_SERVICE_DEFAULT_TEXT_COLOR, SP_SERVICE_DEFAULT_TEXT_COLOR_DV);   
    }        
    /**
     * Establece el color de texto para los servicos por defecto
     * @param context
     * @param color
     */
    public static void setServiceDefaultTextColor(Context context, int color){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_SERVICE_DEFAULT_TEXT_COLOR, color).commit();
    }       
    
	/**
	 * Si está activado o no el preguntar cuando se graba un servicio de 24 horas
	 * @param context
	 * @return true o false
	 */
    public static boolean getAskService24Hours(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_ASK_SERVICE_24_HOURS, SP_ASK_SERVICE_24_HOURS_DV);
    }   
    
    
	/**
	 * Devuelve si está activado o no el que se vean las comisiones cortas y largas
	 * @param context
	 * @return true o false
	 */
    public static boolean getCommissionsDaysLengthActive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_COMMISSIONS_DAYS_LENGTH_ACTIVE, SP_COMMISSIONS_DAYS_LENGTH_ACTIVE_DV);
    }    
	/**
	 * Devuelve si está activado o no el que se vea el derecho a recibir la manutención
	 * @param context
	 * @return true o false
	 */
    public static boolean getCommissionsManutencionActive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_COMMISSIONS_MANUTENCION_ACTIVE, SP_COMMISSIONS_MANUTENCION_ACTIVE_DV);
    }    
    /**
     * Devuelve la longitud de las comisiones para considerarlas cortas o largas
     * @param context
     */
    public static int getCommissionsDaysLength(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_COMMISSIONS_DAYS_LENGTH, SP_COMMISSIONS_DAYS_LENGTH_DV);   
    }    
    /**
     * Devuelve como se verá el total de dieta para cada comisión
     * @param context
     * @return String
     */
    public static String getCommissionsTotalExpenses(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_COMMISSIONS_TOTAL_EXPENSES, SP_COMMISSIONS_TOTAL_EXPENSES_DV);    	
    }  
    
    /**
     * Devuelve el último tipo de servicio usado en el calendario
     * @param context
     */
    public static int getLastUsedTypeService(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SP_LAST_USED_TYPE_SERVICE_NAME, SP_LAST_USED_TYPE_SERVICE_NAME_DV);   
    }        
    
    /**
     * Establece el último tipo de servicio usado en el calendario
     * @param context
     * @param typeId
     */
    public static void setLastUsedTypeService(Context context, int typeId){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_LAST_USED_TYPE_SERVICE_NAME, typeId).commit();
    }
    
    /**
     * Establece a 0 el último tipo de servicio usado en el calendario
     * @param context
     */
    public static void setLastUsedTypeServiceDV(Context context){
    	PreferenceManager.getDefaultSharedPreferences(context).edit().
			putInt(SP_LAST_USED_TYPE_SERVICE_NAME, SP_LAST_USED_TYPE_SERVICE_NAME_DV).commit();
    }
    
	/**
	 * Si está activado o no el mostrar la opción de Entrar en el servicio en el calendario
	 * @param context
	 * @return true o false
	 */
    public static boolean getEnterToService(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_ENTER_TO_SERVICE, SP_ENTER_TO_SERVICE_DV);
    }   
	/**
	 * Si está activado o no el mostrar la opción de Crear nuevo tipo de servicio
	 * @param context
	 * @return true o false
	 */
    public static boolean getCreateNewTypeService(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SP_CREATE_NEW_TYPE_SERVICE, SP_CREATE_NEW_TYPE_SERVICE_DV);
    }

    /**
     * Devuelve la jornada laboral 37.5 o 40
     * @param context
     */
    public static String getSpWorkdayWeekHours(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_WORKDAY_WEEK_HOURS, SP_WORKDAY_WEEK_HOURS_DV);
    }

    /**
     * Devuelve el tipo de computo de la jornada laboral, mensual, trimestral, cuatrimestral
     * @param context
     */
    public static String getSpWorkdayComputingHours(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(SP_WORKDAY_COMPUTING_HOURS, SP_WORKDAY_COMPUTING_HOURS_DV);
    }


}