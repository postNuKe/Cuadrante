package es.progmac.cuadrante.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import es.progmac.cuadrante.info.ComisionInfo;
import es.progmac.cuadrante.info.HotelInfo;
import es.progmac.cuadrante.info.HoursInfo;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.cuadrante.info.TipoServicioInfo;
import es.progmac.cuadrante.info.TurnInfo;
import es.progmac.cuadrante.info.TurnTypeInfo;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHandler";
	private SQLiteDatabase db;
	private int numOpenDb = 0;
	
	// declare constants fields
	public static final String DB_NAME = "cuadrante";
	public static final int DB_VERSION = 11;

	public static final String SERVICE_TABLE_NAME = "service";
	public static final String SERVICE_COLUMN_ID = "_id";
	public static final String SERVICE_COLUMN_DATE = "date";
	public static final String SERVICE_COLUMN_TYPE_ID = "type_id";
	/** Abreviatura */
	public static final String SERVICE_COLUMN_NAME = "name";
	public static final String SERVICE_COLUMN_BG_COLOR = "bg_color";
	public static final String SERVICE_COLUMN_TEXT_COLOR = "text_color";
	public static final String SERVICE_COLUMN_START_SCHEDULE = "start_schedule";
	public static final String SERVICE_COLUMN_END_SCHEDULE = "end_schedule";
	public static final String SERVICE_COLUMN_START_SCHEDULE2 = "start_schedule2";
	public static final String SERVICE_COLUMN_END_SCHEDULE2 = "end_schedule2";
	public static final String SERVICE_COLUMN_START_SCHEDULE3 = "start_schedule3";
	public static final String SERVICE_COLUMN_END_SCHEDULE3 = "end_schedule3";
	public static final String SERVICE_COLUMN_START_SCHEDULE4 = "start_schedule4";
	public static final String SERVICE_COLUMN_END_SCHEDULE4 = "end_schedule4";
	public static final String SERVICE_COLUMN_COMMENTS = "comments";
	public static final String SERVICE_COLUMN_IS_HOLIDAY = "is_holiday";
	public static final String SERVICE_COLUMN_GUARDIA_COMBINADA = "guardia_combinada";
	public static final String SERVICE_COLUMN_TYPE_DAY = "type_day";
	public static final String SERVICE_COLUMN_IS_IMPORTANT = "is_important";
	public static final String SERVICE_COLUMN_SUCCESSION_COMMAND = "succession_command";
	
	private String[] allColumnsService = { 
			SERVICE_COLUMN_ID,
			SERVICE_COLUMN_DATE,
			SERVICE_COLUMN_TYPE_ID,
			SERVICE_COLUMN_NAME,
			SERVICE_COLUMN_BG_COLOR,
			SERVICE_COLUMN_TEXT_COLOR,
			SERVICE_COLUMN_START_SCHEDULE,
			SERVICE_COLUMN_END_SCHEDULE,
			SERVICE_COLUMN_START_SCHEDULE2,
			SERVICE_COLUMN_END_SCHEDULE2,
			SERVICE_COLUMN_START_SCHEDULE3,
			SERVICE_COLUMN_END_SCHEDULE3,
			SERVICE_COLUMN_START_SCHEDULE4,
			SERVICE_COLUMN_END_SCHEDULE4,
			SERVICE_COLUMN_COMMENTS,
			SERVICE_COLUMN_IS_HOLIDAY,
			SERVICE_COLUMN_GUARDIA_COMBINADA,
			SERVICE_COLUMN_TYPE_DAY,
			SERVICE_COLUMN_IS_IMPORTANT,
			SERVICE_COLUMN_SUCCESSION_COMMAND,
	};

	// declared constant SQL Expression
	private static final String SERVICE_TABLE_CREATE = "CREATE TABLE "
			+ SERVICE_TABLE_NAME + " ( " 
			+ SERVICE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SERVICE_COLUMN_DATE + " TEXT NOT NULL, " // date YYYY-mm-dd
			+ SERVICE_COLUMN_TYPE_ID + " INTEGER DEFAULT 0, " // -->SERVICE_TYPE_TABLE_NAME._id
			+ SERVICE_COLUMN_NAME + " TEXT DEFAULT(''), " // abreviatura
			+ SERVICE_COLUMN_BG_COLOR + " TEXT NOT NULL, " // color de fondo de cada servicio en el calendario
			+ SERVICE_COLUMN_TEXT_COLOR + " TEXT NOT NULL, " // color del texto de cada servicio en el calendario
			+ SERVICE_COLUMN_START_SCHEDULE + " TEXT DEFAULT(''), "//00:00
			+ SERVICE_COLUMN_END_SCHEDULE + " TEXT DEFAULT(''), "//00:00
			+ SERVICE_COLUMN_START_SCHEDULE2 + " TEXT DEFAULT(''), "//00:00
			+ SERVICE_COLUMN_END_SCHEDULE2 + " TEXT DEFAULT(''), "//00:00
			+ SERVICE_COLUMN_START_SCHEDULE3 + " TEXT DEFAULT(''), "//00:00
			+ SERVICE_COLUMN_END_SCHEDULE3 + " TEXT DEFAULT(''), "//00:00
			+ SERVICE_COLUMN_START_SCHEDULE4 + " TEXT DEFAULT(''), "//00:00
			+ SERVICE_COLUMN_END_SCHEDULE4 + " TEXT DEFAULT(''), " //00:00
			+ SERVICE_COLUMN_COMMENTS + " TEXT DEFAULT(''), " 
			+ SERVICE_COLUMN_IS_HOLIDAY + " INTEGER DEFAULT 0, " // 0 false, 1 true
			+ SERVICE_COLUMN_GUARDIA_COMBINADA + " INTEGER DEFAULT 0, "
			+ SERVICE_COLUMN_TYPE_DAY + " INTEGER DEFAULT 0, " //0 false, 1 true
			+ SERVICE_COLUMN_IS_IMPORTANT + " INTEGER DEFAULT 0, "
			+ SERVICE_COLUMN_SUCCESSION_COMMAND + " INTEGER DEFAULT 0 "//0 ninguna, 1 E12, 2 E13
			+ ");";
	
//******************************************
		

	public static final String TYPE_SERVICE_TABLE_NAME = "type_service";
	public static final String TYPE_SERVICE_COLUMN_ID = "_id";
	/**
	 * Nombre largo
	 */
	public static final String TYPE_SERVICE_COLUMN_TITLE = "title";
	/**
	 * Abreviatura
	 */
	public static final String TYPE_SERVICE_COLUMN_NAME = "name";
	public static final String TYPE_SERVICE_COLUMN_BG_COLOR = "bg_color";
	public static final String TYPE_SERVICE_COLUMN_TEXT_COLOR = "text_color";
	public static final String TYPE_SERVICE_COLUMN_START_SCHEDULE = "start_schedule";
	public static final String TYPE_SERVICE_COLUMN_END_SCHEDULE = "end_schedule";
	public static final String TYPE_SERVICE_COLUMN_START_SCHEDULE2 = "start_schedule2";
	public static final String TYPE_SERVICE_COLUMN_END_SCHEDULE2 = "end_schedule2";
	public static final String TYPE_SERVICE_COLUMN_START_SCHEDULE3 = "start_schedule3";
	public static final String TYPE_SERVICE_COLUMN_END_SCHEDULE3 = "end_schedule3";
	public static final String TYPE_SERVICE_COLUMN_START_SCHEDULE4 = "start_schedule4";
	public static final String TYPE_SERVICE_COLUMN_END_SCHEDULE4 = "end_schedule4";
	public static final String TYPE_SERVICE_COLUMN_IS_DATE_RANGE = "is_date_range";
	public static final String TYPE_SERVICE_COLUMN_GUARDIA_COMBINADA = "guardia_combinada";
	public static final String TYPE_SERVICE_COLUMN_TYPE_DAY = "type_day";
	public static final String TYPE_SERVICE_COLUMN_SUCCESSION_COMMAND = "succession_command";
	public static final String TYPE_SERVICE_COLUMN_ASK_SCHEDULE = "ask_schedule";

	private String[] allColumnsTypeService = { 
			TYPE_SERVICE_COLUMN_ID,
			TYPE_SERVICE_COLUMN_TITLE,
			TYPE_SERVICE_COLUMN_NAME,
			TYPE_SERVICE_COLUMN_BG_COLOR,
			TYPE_SERVICE_COLUMN_TEXT_COLOR,
			TYPE_SERVICE_COLUMN_START_SCHEDULE,
			TYPE_SERVICE_COLUMN_END_SCHEDULE,
			TYPE_SERVICE_COLUMN_START_SCHEDULE2,
			TYPE_SERVICE_COLUMN_END_SCHEDULE2,
			TYPE_SERVICE_COLUMN_START_SCHEDULE3,
			TYPE_SERVICE_COLUMN_END_SCHEDULE3,
			TYPE_SERVICE_COLUMN_START_SCHEDULE4,
			TYPE_SERVICE_COLUMN_END_SCHEDULE4, 
			TYPE_SERVICE_COLUMN_IS_DATE_RANGE,
			TYPE_SERVICE_COLUMN_GUARDIA_COMBINADA,
			TYPE_SERVICE_COLUMN_TYPE_DAY,
			TYPE_SERVICE_COLUMN_SUCCESSION_COMMAND,
			TYPE_SERVICE_COLUMN_ASK_SCHEDULE
	};
	
	
	// declared constant SQL Expression
	private static final String TYPE_SERVICE_TABLE_CREATE = "CREATE TABLE "
			+ TYPE_SERVICE_TABLE_NAME + " ( "
			+ TYPE_SERVICE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TYPE_SERVICE_COLUMN_TITLE + " TEXT NOT NULL, " // nombre largo
			+ TYPE_SERVICE_COLUMN_NAME + " TEXT NOT NULL, "// abreviatura
			+ TYPE_SERVICE_COLUMN_BG_COLOR + " TEXT NOT NULL, " // color de fondo de cada servicio en el calendario
			+ TYPE_SERVICE_COLUMN_TEXT_COLOR + " TEXT NOT NULL, " // color del texto de cada servicio en el calendario
			+ TYPE_SERVICE_COLUMN_START_SCHEDULE + " TEXT DEFAULT(''), "//00:00
			+ TYPE_SERVICE_COLUMN_END_SCHEDULE + " TEXT DEFAULT(''), "//00:00
			+ TYPE_SERVICE_COLUMN_START_SCHEDULE2 + " TEXT DEFAULT(''), "//00:00
			+ TYPE_SERVICE_COLUMN_END_SCHEDULE2 + " TEXT DEFAULT(''), "//00:00
			+ TYPE_SERVICE_COLUMN_START_SCHEDULE3 + " TEXT DEFAULT(''), "//00:00
			+ TYPE_SERVICE_COLUMN_END_SCHEDULE3 + " TEXT DEFAULT(''), "//00:00
			+ TYPE_SERVICE_COLUMN_START_SCHEDULE4 + " TEXT DEFAULT(''), "//00:00
			+ TYPE_SERVICE_COLUMN_END_SCHEDULE4 + " TEXT DEFAULT(''), "//00:00
			+ TYPE_SERVICE_COLUMN_IS_DATE_RANGE + " INTEGER DEFAULT 0, "// 0 false, 1 true
			+ TYPE_SERVICE_COLUMN_GUARDIA_COMBINADA + " INTEGER DEFAULT 0, "
			+ TYPE_SERVICE_COLUMN_TYPE_DAY + " INTEGER DEFAULT 0, " //0 false, 1 true //0 dia ordinario, 1 día ordinario no computable 5.35, 2 día especial no computable 7.5
			+ TYPE_SERVICE_COLUMN_SUCCESSION_COMMAND + " INTEGER DEFAULT 0, " //0 ninguna, 1 E12, 2 E13
			+ TYPE_SERVICE_COLUMN_ASK_SCHEDULE + " INTEGER DEFAULT 0 " //0 false, 1 true
			+ ");";
	
//******************************************
	
	public static final String COMISION_TABLE_NAME = "comision";
	public static final String COMISION_COLUMN_ID = "_id";
	public static final String COMISION_COLUMN_NAME = "name";
	public static final String COMISION_COLUMN_COMMENTS= "comments";
	public static final String COMISION_COLUMN_START_DATE = "start_date";
	public static final String COMISION_COLUMN_END_DATE = "end_date";
	public static final String COMISION_COLUMN_VIEW_TOTAL_EXPENSES = "view_total_expenses";
	private String[] allColumnsComision = { 
			COMISION_COLUMN_ID,
			COMISION_COLUMN_NAME,
			COMISION_COLUMN_COMMENTS,
			COMISION_COLUMN_START_DATE,
			COMISION_COLUMN_END_DATE,
			COMISION_COLUMN_VIEW_TOTAL_EXPENSES
			};	
	// declared constant SQL Expression
	private static final String COMISION_TABLE_CREATE = "CREATE TABLE "
			+ COMISION_TABLE_NAME + " ( "
			+ COMISION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COMISION_COLUMN_NAME + " TEXT NOT NULL, "
			+ COMISION_COLUMN_COMMENTS + " TEXT DEFAULT(''), " 
			+ COMISION_COLUMN_START_DATE + " TEXT NOT NULL, " // date YYYY-mm-dd
			+ COMISION_COLUMN_END_DATE + " TEXT NOT NULL," // date YYYY-mm-dd
			+ COMISION_COLUMN_VIEW_TOTAL_EXPENSES + " INTEGER DEFAULT 0 " // 0 total, 1 80_20_laundry
			+ ");";	
	
//******************************************
	
	public static final String HOTEL_TABLE_NAME = "hotel";
	public static final String HOTEL_COLUMN_ID = "_id";
	public static final String HOTEL_COLUMN_COMISION_ID = "comision_id";
	public static final String HOTEL_COLUMN_NAME = "name";
	public static final String HOTEL_COLUMN_COMMENTS= "comments";
	public static final String HOTEL_COLUMN_START_DATE = "start_date";
	public static final String HOTEL_COLUMN_END_DATE = "end_date";
	public static final String HOTEL_COLUMN_DAILY_EXPENSES = "daily_expenses";//dieta diaria
	public static final String HOTEL_COLUMN_MANUTENCION_EXPENSES = "manutencion_price";//manutencion diaria
	public static final String HOTEL_COLUMN_LAUNDRY = "laundry";//lavandería
	private String[] allColumnsHotel = { 
			HOTEL_COLUMN_ID,
			HOTEL_COLUMN_COMISION_ID,
			HOTEL_COLUMN_NAME,
			HOTEL_COLUMN_COMMENTS,
			HOTEL_COLUMN_START_DATE,
			HOTEL_COLUMN_END_DATE,
			HOTEL_COLUMN_DAILY_EXPENSES,
			HOTEL_COLUMN_LAUNDRY,
			HOTEL_COLUMN_MANUTENCION_EXPENSES,
			};	
	// declared constant SQL Expression
	private static final String HOTEL_TABLE_CREATE = "CREATE TABLE "
			+ HOTEL_TABLE_NAME + " ( "
			+ HOTEL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ HOTEL_COLUMN_COMISION_ID + " INTEGER DEFAULT 0, " // -->COMISION_TABLE_NAME._id
			+ HOTEL_COLUMN_NAME + " TEXT NOT NULL, "
			+ HOTEL_COLUMN_COMMENTS + " TEXT DEFAULT(''), " 
			+ HOTEL_COLUMN_START_DATE + " TEXT NOT NULL, " // date YYYY-mm-dd
			+ HOTEL_COLUMN_END_DATE + " TEXT NOT NULL, " // date YYYY-mm-dd
			+ HOTEL_COLUMN_DAILY_EXPENSES + " NUMERIC DEFAULT(0.0),"
			+ HOTEL_COLUMN_LAUNDRY + " NUMERIC DEFAULT(0.0),"
			+ HOTEL_COLUMN_MANUTENCION_EXPENSES + " NUMERIC DEFAULT(0.0)"
			+ ");";			
	
//******************************************

	public static final String HOURS_TABLE_NAME = "hours";
	public static final String HOURS_COLUMN_ID = "_id";
	public static final String HOURS_COLUMN_YEAR = "year";
	public static final String HOURS_COLUMN_MONTH = "month";
	public static final String HOURS_COLUMN_HOURS = "hours";
	public static final String HOURS_COLUMN_REFERENCE = "reference";
	public static final String HOURS_COLUMN_F2 = "f2";
	public static final String HOURS_COLUMN_GUARDIAS = "guardias";
	public static final String HOURS_COLUMN_F2_HOURS = "f2_hours";
	private String[] allColumnsHours = { 
			HOURS_COLUMN_ID,
			HOURS_COLUMN_YEAR,
			HOURS_COLUMN_MONTH,
			HOURS_COLUMN_HOURS,
			HOURS_COLUMN_REFERENCE,
			HOURS_COLUMN_F2,
			HOURS_COLUMN_GUARDIAS,
			HOURS_COLUMN_F2_HOURS,
	};	
	// declared constant SQL Expression
	private static final String HOURS_TABLE_CREATE = "CREATE TABLE "
			+ HOURS_TABLE_NAME + " ( "
			+ HOURS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ HOURS_COLUMN_YEAR + " INTEGER NOT NULL, "
			+ HOURS_COLUMN_MONTH + " INTEGER NOT NULL, "
			+ HOURS_COLUMN_HOURS + " NUMERIC DEFAULT(0.0), " 
			+ HOURS_COLUMN_REFERENCE + " NUMERIC DEFAULT(0.0), "
			+ HOURS_COLUMN_F2 + " NUMERIC DEFAULT(0.0), "
			+ HOURS_COLUMN_GUARDIAS + " INTEGER DEFAULT 0, "
			+ HOURS_COLUMN_F2_HOURS + " NUMERIC DEFAULT(0.0)"
			+ ");";				
	//******************************************	
	
	public static final String TURN_TABLE_NAME = "turn";
	public static final String TURN_COLUMN_ID = "_id";
	public static final String TURN_COLUMN_NAME = "name";
	private String[] allColumnsTurn = { 
			TURN_COLUMN_ID,
			TURN_COLUMN_NAME,
	};	
	private static final String TURN_TABLE_CREATE = "CREATE TABLE "
			+ TURN_TABLE_NAME + " ( "
			+ TURN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TURN_COLUMN_NAME + " TEXT NOT NULL"
			+ ");";	
	
	//******************************************	
	
	public static final String TURN_TYPE_TABLE_NAME = "turn_type";
	public static final String TURN_TYPE_COLUMN_TURN_ID = "turn_id";
	public static final String TURN_TYPE_COLUMN_TYPE_ID = "type_id";
	public static final String TURN_TYPE_COLUMN_ORDEN = "orden";
	/** Si se añade un servicio llamado saliente después de este tipo */
	public static final String TURN_TYPE_COLUMN_SALIENTE = "saliente";
	private String[] allColumnsTurnType = { 
			TURN_TYPE_COLUMN_TURN_ID,
			TURN_TYPE_COLUMN_TYPE_ID,
			TURN_TYPE_COLUMN_ORDEN,
			TURN_TYPE_COLUMN_SALIENTE,
	};	
	private static final String TURN_TYPE_TABLE_CREATE = "CREATE TABLE "
			+ TURN_TYPE_TABLE_NAME + " ( "
			+ TURN_TYPE_COLUMN_TURN_ID + " INTEGER NOT NULL, "
			+ TURN_TYPE_COLUMN_TYPE_ID + " INTEGER NOT NULL, "
			+ TURN_TYPE_COLUMN_ORDEN + " INTEGER NOT NULL, "
			+ TURN_TYPE_COLUMN_SALIENTE + " INTEGER DEFAULT 0, "
			+ "PRIMARY KEY (" + TURN_TYPE_COLUMN_TURN_ID + ", " + TURN_TYPE_COLUMN_TYPE_ID + ", " 
				+ TURN_TYPE_COLUMN_ORDEN + ")"
			+ ");";				
	
	
	//******************************************	
	
	private static final String DB_DESTROY = "DROP TABLE IF EXISTS ";

	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SERVICE_TABLE_CREATE);
		DateTime date = new DateTime();
		//creamos los festivos de un año atrás hasta 9 años para adelante
		date = date.minusYears(1);
		String sqlColumns = "INSERT INTO " + SERVICE_TABLE_NAME + "(" 
				+ SERVICE_COLUMN_DATE + ", " 
				+ SERVICE_COLUMN_COMMENTS + ", " 
				+ SERVICE_COLUMN_IS_HOLIDAY + ", " 
				+ SERVICE_COLUMN_BG_COLOR + ", " 
				+ SERVICE_COLUMN_TEXT_COLOR + ") ";
		for(int i = 1; i <= 10; i++){		
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 1, 1) + "', 'Año nuevo', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 1, 6) + "', 'Reyes', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 5, 1) + "', 'Día del trabajador', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 8, 15) + "', 'Asunción de la Virgen, excepto en Tenerife', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 10, 12) + "', 'Día del Pilar / Hispanidad', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 11, 1) + "', 'Todos los Santos', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 12, 6) + "', 'Constitución', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 12, 8) + "', 'Inmaculada', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");
			db.execSQL(sqlColumns + "VALUES ('" + CuadranteDates.formatDate(date.getYear(), 12, 25) + "', 'Navidad', '1', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY) + "', '" + String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY) + "')");			
			date = date.plusYears(1);
		}
		
		db.execSQL(TYPE_SERVICE_TABLE_CREATE);
		sqlColumns = "INSERT INTO " + TYPE_SERVICE_TABLE_NAME + "(" 
				+ TYPE_SERVICE_COLUMN_TITLE + ", " 
				+ TYPE_SERVICE_COLUMN_NAME+ ", " 
				+ TYPE_SERVICE_COLUMN_BG_COLOR + ", " 
				+ TYPE_SERVICE_COLUMN_TEXT_COLOR + ", " 
				+ TYPE_SERVICE_COLUMN_IS_DATE_RANGE + ", " 
				+ TYPE_SERVICE_COLUMN_GUARDIA_COMBINADA + ", " 
				+ TYPE_SERVICE_COLUMN_TYPE_DAY + ") ";
		db.execSQL(sqlColumns + " VALUES (\"ALERTA\", \"A0\", -15454022, -1, 0, 1, 0);");
		db.execSQL(sqlColumns + " VALUES (\"ASUNTOS PARTICULARES\", \"AP\", -9178339, -16777216, 0, 0, 1);");
		db.execSQL(sqlColumns + " VALUES (\"BAJA\", \"BAJ\", -287128, -16777216, 1, 0, 1);");
		db.execSQL(sqlColumns + " VALUES (\"LIBRE\", \"L\", -16139282, -16777216, 1, 0, 0);");
		db.execSQL(sqlColumns + " VALUES (\"JUICIO\", \"JUI\", -9226445, -1, 0, 0, 0);");
		db.execSQL(sqlColumns + " VALUES (\"VACACIONES\", \"VAC\", -6100697, -16777216, 1, 0, 1);");
		db.execSQL(sqlColumns + " VALUES (\"LIBRE FESTIVO\", \"LF\", -16139282, -16777216, 0, 0, 0);");
		db.execSQL(sqlColumns + " VALUES (\"PERMISO INCORPORACIÓN\", \"PIN\", -10448442, -16777216, 1, 0, 1);");
		db.execSQL(sqlColumns + " VALUES (\"PERMISO SS Y NAVIDAD\", \"PSN\", -10448442, -16777216, 1, 0, 1);");
		db.execSQL(sqlColumns + " VALUES (\"PERMISO URGENTE\", \"PUR\", -10448442, -16777216, 1, 0, 1);");
		
		sqlColumns = "INSERT INTO " + TYPE_SERVICE_TABLE_NAME + "(" 
				+ TYPE_SERVICE_COLUMN_TITLE + ", " 
				+ TYPE_SERVICE_COLUMN_NAME+ ", " 
				+ TYPE_SERVICE_COLUMN_BG_COLOR + ", " 
				+ TYPE_SERVICE_COLUMN_TEXT_COLOR + ", " 
				+ TYPE_SERVICE_COLUMN_START_SCHEDULE + ", "
				+ TYPE_SERVICE_COLUMN_END_SCHEDULE + ", "
				+ TYPE_SERVICE_COLUMN_START_SCHEDULE2 + ", "
				+ TYPE_SERVICE_COLUMN_END_SCHEDULE2 + ") ";
		//11
		db.execSQL(sqlColumns + " VALUES (\"MAÑANA\", \"M\", -3238898, -16777216, '10:00', '12:00', '" + Cuadrante.SCHEDULE_NULL +"', '" + Cuadrante.SCHEDULE_NULL +"');");
		db.execSQL(sqlColumns + " VALUES (\"MAÑANA-NOCHE\", \"MN\", -25088, -16777216, '10:00', '12:00', '23:00', '03:00');");
		db.execSQL(sqlColumns + " VALUES (\"NOCHE\", \"N\", -14588160, -1, '23:00', '03:00', '" + Cuadrante.SCHEDULE_NULL +"', '" + Cuadrante.SCHEDULE_NULL +"');");
		db.execSQL(sqlColumns + " VALUES (\"PUERTAS\", \"P\", -3238898, -16777216, '10:00', '12:00', '16:00', '18:00');");
		db.execSQL(sqlColumns + " VALUES (\"TARDE\", \"T\", -29165, -16777216, '16:00', '18:00', '" + Cuadrante.SCHEDULE_NULL +"', '" + Cuadrante.SCHEDULE_NULL +"');");
		
		
		db.execSQL(COMISION_TABLE_CREATE);
		db.execSQL(HOTEL_TABLE_CREATE);
		db.execSQL(HOURS_TABLE_CREATE);
		
		db.execSQL(TURN_TABLE_CREATE);
		db.execSQL("INSERT INTO " + TURN_TABLE_NAME + " VALUES ('1', 'AFRICANO');");
		db.execSQL(TURN_TYPE_TABLE_CREATE);
		db.execSQL("INSERT INTO " + TURN_TYPE_TABLE_NAME + " VALUES ('1', '15', '0', '0');");//tarde
		db.execSQL("INSERT INTO " + TURN_TYPE_TABLE_NAME + " VALUES ('1', '12', '1', '1');");//m-n
		db.execSQL("INSERT INTO " + TURN_TYPE_TABLE_NAME + " VALUES ('1', '4', '2', '0');");//libre
		db.execSQL("INSERT INTO " + TURN_TYPE_TABLE_NAME + " VALUES ('1', '4', '3', '0');");//libre
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < 10){
			db.execSQL("ALTER TABLE " + HOTEL_TABLE_NAME + " ADD COLUMN " + HOTEL_COLUMN_MANUTENCION_EXPENSES + " NUMERIC DEFAULT(0.0);");			
		}
		//para que se borren las horas y las coja bien a partir de ahora cuando se actualicen
		//las horas de referencia semanales
		if(oldVersion < 11){
			db.execSQL("DELETE FROM " + HOURS_TABLE_NAME);
			db.execSQL(TURN_TABLE_CREATE);
			db.execSQL(TURN_TYPE_TABLE_CREATE);
		}
        //Cambiar todas las guardias combinadas a valor 1, tipo de dia mayor que 0 a 1
        if(oldVersion < 12){
            db.execSQL("UPDATE " + SERVICE_TABLE_NAME + " SET " +
                    SERVICE_COLUMN_GUARDIA_COMBINADA + " = '1' " +
                    "WHERE " + SERVICE_COLUMN_GUARDIA_COMBINADA + " > '0' ");
            db.execSQL("UPDATE " + SERVICE_TABLE_NAME + " SET " +
                    SERVICE_COLUMN_TYPE_DAY + " = '1' " +
                    "WHERE " + SERVICE_COLUMN_TYPE_DAY + " > '0' ");

            db.execSQL("UPDATE " + TYPE_SERVICE_TABLE_NAME + " SET " +
                    TYPE_SERVICE_COLUMN_TYPE_DAY + " = '1' " +
                    "WHERE " + TYPE_SERVICE_COLUMN_TYPE_DAY + " > '0' ");
            db.execSQL("UPDATE " + TYPE_SERVICE_TABLE_NAME + " SET " +
                    TYPE_SERVICE_COLUMN_TYPE_DAY + " = '1' " +
                    "WHERE " + TYPE_SERVICE_COLUMN_TYPE_DAY + " > '0' ");
        }
		/*
		if(oldVersion < 2){//columna important
			db.execSQL("ALTER TABLE " + SERVICE_TABLE_NAME + " ADD COLUMN " + SERVICE_COLUMN_IS_IMPORTANT + " INTEGER DEFAULT 0;");
		}		
		
		if(oldVersion < 2){//tabla horas mensuales
			db.execSQL(HOURS_TABLE_CREATE);
		}		
		if(oldVersion < 3){//columna succession_command
			db.execSQL("ALTER TABLE " + SERVICE_TABLE_NAME + " ADD COLUMN " + SERVICE_COLUMN_SUCCESSION_COMMAND + " INTEGER DEFAULT 0;");
			db.execSQL("ALTER TABLE " + TYPE_SERVICE_TABLE_NAME + " ADD COLUMN " + TYPE_SERVICE_COLUMN_SUCCESSION_COMMAND + " INTEGER DEFAULT 0;");
		}		
		if(oldVersion < 4){//columna ask_schedule
			db.execSQL("ALTER TABLE " + TYPE_SERVICE_TABLE_NAME + " ADD COLUMN " + TYPE_SERVICE_COLUMN_ASK_SCHEDULE + " INTEGER DEFAULT 0;");			
		}
		if(oldVersion < 5){//columna f2_hours
			db.execSQL("DELETE FROM " + HOURS_TABLE_NAME);
			db.execSQL("ALTER TABLE " + HOURS_TABLE_NAME + " ADD COLUMN " + HOURS_COLUMN_F2_HOURS + " NUMERIC DEFAULT(0.0);");			
		}
		if(oldVersion < 6){//columna comision.view_total_expenses
			db.execSQL("ALTER TABLE " + COMISION_TABLE_NAME + " ADD COLUMN " + COMISION_COLUMN_VIEW_TOTAL_EXPENSES + " INTEGER DEFAULT 0;");			
		}
		*/
	}	
/******************************************************************************
	All CRUD(Create, Read, Update, Delete) Operations
******************************************************************************/
	public void setDb(SQLiteDatabase db) throws SQLException{
		this.db = db;
		numOpenDb++;
	}
	public void openRead() throws SQLException {
		//SQLiteDatabase db = this.getReadableDatabase();
		//if(!this.db.isOpen() || this.db == null)
		if(numOpenDb == 0)
			this.db = this.getReadableDatabase();
		numOpenDb++;
	}
	public void openWrite() throws SQLException {
		if(!this.db.isOpen())
			this.db = this.getWritableDatabase();
		numOpenDb++;
	}
	public void closeDB(){
		if(numOpenDb == 1) this.db.close();
		numOpenDb--;
	}

	public void destroyServiceTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + SERVICE_TABLE_NAME);
		db.close();
	}
	public void destroyTypeServiceTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TYPE_SERVICE_TABLE_NAME);
		db.close();
	}
	public void destroyComisionTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + COMISION_TABLE_NAME);
		db.close();
	}
	public void destroyHotelTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + HOTEL_TABLE_NAME);
		db.close();
	}
	public void destroyHoursTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + HOURS_TABLE_NAME);
		db.close();
	}
	public void destroyTurnTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TURN_TABLE_NAME);
		db.close();
	}
	public void destroyTurnTypeTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TURN_TYPE_TABLE_NAME);
		db.close();
	}
	public void destroy(){
		destroyServiceTable();
		destroyTypeServiceTable();
		destroyComisionTable();
		destroyHotelTable();
		destroyHoursTable();
		destroyTurnTable();
		destroyTurnTypeTable();
	}
	
	
	public ServicioInfo addService(ServicioInfo service) {
		SQLiteDatabase db = this.getWritableDatabase();
		// preparamos los datos a insertar para el nuevo servicio
		ContentValues values = new ContentValues();
		if(service.getId() > 0){
			values.put(SERVICE_COLUMN_ID, service.getId());
		}
		values.put(SERVICE_COLUMN_DATE, service.getDate());
		values.put(SERVICE_COLUMN_TYPE_ID, service.getTypeId());
		values.put(SERVICE_COLUMN_NAME, service.getName());
		values.put(SERVICE_COLUMN_BG_COLOR, service.getBgColor());
		values.put(SERVICE_COLUMN_TEXT_COLOR, service.getTextColor());
		values.put(SERVICE_COLUMN_START_SCHEDULE, service.getStartSchedule());
		values.put(SERVICE_COLUMN_END_SCHEDULE, service.getEndSchedule());
		values.put(SERVICE_COLUMN_START_SCHEDULE2, service.getStartSchedule2());
		values.put(SERVICE_COLUMN_END_SCHEDULE2, service.getEndSchedule2());
		values.put(SERVICE_COLUMN_START_SCHEDULE3, service.getStartSchedule3());
		values.put(SERVICE_COLUMN_END_SCHEDULE3, service.getEndSchedule3());
		values.put(SERVICE_COLUMN_START_SCHEDULE4, service.getStartSchedule4());
		values.put(SERVICE_COLUMN_END_SCHEDULE4, service.getEndSchedule4());
		values.put(SERVICE_COLUMN_COMMENTS, service.getComments());
		values.put(SERVICE_COLUMN_IS_HOLIDAY, service.getIsHoliday());
		values.put(SERVICE_COLUMN_GUARDIA_COMBINADA, service.getGuardiaCombinada());
		values.put(SERVICE_COLUMN_TYPE_DAY, service.getTypeDay());
		values.put(SERVICE_COLUMN_IS_IMPORTANT, service.getIsImportant());
		values.put(SERVICE_COLUMN_SUCCESSION_COMMAND, service.getSuccessionCommand());

		long insertId = db.insert(SERVICE_TABLE_NAME, null,
				values);
		Cursor cursor = db.query(SERVICE_TABLE_NAME,
				allColumnsService, SERVICE_COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		ServicioInfo newServicio = cursorToService(cursor);
		//muy importante esto, primero se cierra el cursor y despues la bd
		cursor.close();
		db.close();
		return newServicio;
	}
	
	public Integer updateServicio(ServicioInfo service) {
		SQLiteDatabase db = this.getWritableDatabase();
		// preparamos los datos a actualizar para el servicio
		ContentValues values = new ContentValues();
		values.put(SERVICE_COLUMN_TYPE_ID, service.getTypeId());
		values.put(SERVICE_COLUMN_NAME, service.getName());
		values.put(SERVICE_COLUMN_BG_COLOR, service.getBgColor());
		values.put(SERVICE_COLUMN_TEXT_COLOR, service.getTextColor());
		values.put(SERVICE_COLUMN_START_SCHEDULE, service.getStartSchedule());
		values.put(SERVICE_COLUMN_END_SCHEDULE, service.getEndSchedule());
		values.put(SERVICE_COLUMN_START_SCHEDULE2, service.getStartSchedule2());
		values.put(SERVICE_COLUMN_END_SCHEDULE2, service.getEndSchedule2());
		values.put(SERVICE_COLUMN_START_SCHEDULE3, service.getStartSchedule3());
		values.put(SERVICE_COLUMN_END_SCHEDULE3, service.getEndSchedule3());
		values.put(SERVICE_COLUMN_START_SCHEDULE4, service.getStartSchedule4());
		values.put(SERVICE_COLUMN_END_SCHEDULE4, service.getEndSchedule4());
		values.put(SERVICE_COLUMN_COMMENTS, service.getComments());
		values.put(SERVICE_COLUMN_IS_HOLIDAY, service.getIsHoliday());
		values.put(SERVICE_COLUMN_GUARDIA_COMBINADA, service.getGuardiaCombinada());
		values.put(SERVICE_COLUMN_TYPE_DAY, service.getTypeDay());
		values.put(SERVICE_COLUMN_IS_IMPORTANT, service.getIsImportant());
		values.put(SERVICE_COLUMN_SUCCESSION_COMMAND, service.getSuccessionCommand());

		String[] whereArgs = { service.getDate() };
		Integer update = db.update(
				SERVICE_TABLE_NAME, 
				values,
				SERVICE_COLUMN_DATE + " = ?", 
				whereArgs);
		db.close();
		return update;
	}

	/**
	 * Actualiza todos los servicios que tengan el type_id que se le pase a 0.
	 * Se usa cuando se elimina un tipo de servicio.
	 * @param type_id tipo de servicio que se quiere cambiar a 0
	 * @return Integer número de registros actualizados
	 */
	public void updateServiciosto0(Integer type_id, String append_comments){
		SQLiteDatabase db = this.getWritableDatabase();
		/*
		ContentValues values = new ContentValues();
		values.put(SERVICE_COLUMN_TYPE_ID, 0);
		values.put(SERVICE_COLUMN_COMMENTS, append_comments + " || " + SERVICE_COLUMN_COMMENTS); //comments = 'string' || comments
		String[] whereArgs = { String.valueOf(type_id) };
		Integer update = db.update(
				SERVICE_TABLE_NAME, 
				values, 
				SERVICE_COLUMN_TYPE_ID + " = ?", 
				whereArgs);
		db.close();
		*/
		//tenemos que concatenar el texto con los propios comentarios del servicio asi que la única
		//manera es modificando el query a pelo para añadir || que une dos cadenas en sqlite
		db.execSQL("UPDATE " + SERVICE_TABLE_NAME + " SET " + 
				SERVICE_COLUMN_TYPE_ID + " = '0', " +
				SERVICE_COLUMN_COMMENTS + " = '" + append_comments + "' || " + SERVICE_COLUMN_COMMENTS + " " + 
				"WHERE " + SERVICE_COLUMN_TYPE_ID + " = '" + type_id + "' ");
		db.close();
	}
	
	public Integer updateServiciosFromTipo(Integer type_id, String name, 
			String bg_color, String text_color){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SERVICE_COLUMN_NAME, name);
		values.put(SERVICE_COLUMN_BG_COLOR, bg_color);
		values.put(SERVICE_COLUMN_TEXT_COLOR, text_color);
		String[] whereArgs = { String.valueOf(type_id) };
		Integer update = db.update(
				SERVICE_TABLE_NAME, 
				values, 
				SERVICE_COLUMN_TYPE_ID + " = ?", 
				whereArgs);
		TipoServicioInfo typeService = getTipoServicio(type_id);
		db = this.getWritableDatabase();
		if(Cuadrante.isServiceEndNextDay(
				typeService.getStartSchedule(), typeService.getEndSchedule(),
				typeService.getStartSchedule2(), typeService.getEndSchedule2(),
				typeService.getStartSchedule3(), typeService.getEndSchedule3(),
				typeService.getStartSchedule4(), typeService.getEndSchedule4())){
			//obtenemos la fecha de cada saliente
			ArrayList<String> salienteList = new ArrayList<String>();
			Cursor cursor = db.query(SERVICE_TABLE_NAME,
					allColumnsService, SERVICE_COLUMN_TYPE_ID + " = ?", whereArgs, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				ServicioInfo servicio = cursorToService(cursor);
				salienteList.add("'" + new CuadranteDates(servicio.getDateTime().plusDays(1)).getDate() + "'");
				cursor.moveToNext();
			}		
			String str = salienteList.toString();
			str = str.replaceAll("[\\[\\]]", "");
			MyLog.d(TAG, str);
			//actualizamos solo los servicios que creemos que son salientes pero que contengan el
			//nombre SAL
			values = new ContentValues();
			values.put(SERVICE_COLUMN_BG_COLOR, bg_color);
			values.put(SERVICE_COLUMN_TEXT_COLOR, text_color);
			String[] whereArgs1 = {"0", Cuadrante.CALENDAR_SERVICE_SALIENTE };
			update = db.update(
					SERVICE_TABLE_NAME, 
					values, 
					SERVICE_COLUMN_DATE + " IN(" + str + ") AND " 
							+ SERVICE_COLUMN_TYPE_ID + " = ? AND " 
							+ SERVICE_COLUMN_NAME + " = ?", 
					whereArgs1);		
		}
		
		db.close();
		return update;
	}
	
	/**
	 * Actualiza el tipo de dia de todos los servicios vinculados a un tipo
	 * @param type_id
	 * @param type_day
	 * @return
	 */
	public Integer updateServicesFromTypeTypeDay(Integer type_id, int type_day){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SERVICE_COLUMN_TYPE_DAY, type_day);
		String[] whereArgs = { String.valueOf(type_id) };
		Integer update = db.update(
				DatabaseHandler.SERVICE_TABLE_NAME, 
				values, 
				DatabaseHandler.SERVICE_COLUMN_TYPE_ID + " = ?", 
				whereArgs);
		db.close();
		return update;
	}	
	
	/**
	 * Actualiza los colores de todos los festivos
	 * @return Integer
	 */
	public Integer updateHolidays(Context context){		
		return _updateHolidays(
				Sp.getHolidayBgColor(context),
				Sp.getHolidayTextColor(context));
				//Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY,
				//Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY);
	}
	/** 
	 * Actualiza los colores de todos los festivos 
	 * @param bg_color color de fondo
	 * @param text_color color de texto
	 * @return Integer
	 * */
	private Integer _updateHolidays(int bg_color, int text_color){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SERVICE_COLUMN_BG_COLOR, bg_color);
		values.put(SERVICE_COLUMN_TEXT_COLOR, text_color);
		String[] whereArgs = {};
		Integer update = db.update(
				SERVICE_TABLE_NAME, 
				values, 
				SERVICE_COLUMN_IS_HOLIDAY + " = 1", 
				whereArgs);
		db.close();
		return update;
	}

	public void deleteServicio(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(SERVICE_TABLE_NAME,
				SERVICE_COLUMN_ID + " = " + id, null);
		db.close();
	}
	
	/**
	 * Elimina todos los servicios entre dos fechas
	 * @param dateStart Y-MM-dd
	 * @param dateEnd Y-MM-dd
	 */
	public void deleteServicesByDateRange(String dateStart, String dateEnd){
		SQLiteDatabase db = this.getWritableDatabase();
		String[] whereArgs = { dateStart, dateEnd };
		db.delete(SERVICE_TABLE_NAME, 
				SERVICE_COLUMN_DATE + " >= ? AND " + SERVICE_COLUMN_DATE + " <= ?", 
				whereArgs);
		db.close();
	}

	public List<ServicioInfo> getAllServicios() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<ServicioInfo> servicios = new ArrayList<ServicioInfo>();

		Cursor cursor = db.query(SERVICE_TABLE_NAME,
				allColumnsService, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ServicioInfo servicio = cursorToService(cursor);
			servicios.add(servicio);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return servicios;
	}
	
	/**
	 * Obtiene los servicios de un mes con una clausula Where
	 * @param year yyyy
	 * @param month mm
	 * @param where sin incluir la clausula where
	 * @return
	 */
	private List<ServicioInfo> getServicesInMonthWhere(int year, int month, String where){
		SQLiteDatabase db = this.getReadableDatabase();
		List<ServicioInfo> servicios = new ArrayList<ServicioInfo>();
		String month2;
		if (month < 10)
			month2 = "0" + String.valueOf(month);
		else
			month2 = String.valueOf(month);
		if(where.length() > 0){
			where = " AND " + where;
		}
		Cursor cursor = db.query(SERVICE_TABLE_NAME,
				allColumnsService, SERVICE_COLUMN_DATE + " LIKE '" + year
						+ "-" + month2 + "-%'" + where, null,// new String[] {"2012",
														// "08"},//args,
				null, null, SERVICE_COLUMN_DATE + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ServicioInfo servicio = cursorToService(cursor);
			servicios.add(servicio);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return servicios;		
	}
	
	/**
	 * Obtiene todos los servicios dentro de un mes
	 * @param year yyyy
	 * @param month mm
	 * @return
	 */
	public List<ServicioInfo> getServicesInMonth(int year, int month) {
		return this.getServicesInMonthWhere(year, month, "");
	}
	
	/**
	 * Obtieen las guardias dentro de un mes
	 * @param year yyyy
	 * @param month mm
	 * @return
	 */
	public List<ServicioInfo> getGuardiasCombinadasServicesInMonth(int year, int month){
		return this.getServicesInMonthWhere(year, month, DatabaseHandler.SERVICE_COLUMN_GUARDIA_COMBINADA + " > '0'");
	}
	
	/**
	 * Devuelve todos los servicios asignados en un intervalo de tiempo
	 * incluyendo los fines de semana
	 * @param typeId
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	public List<ServicioInfo> getServicesFromTypeService(int typeId, 
			String dateStart, String dateEnd){
		return _getServicesFromTypeService(typeId, dateStart, dateEnd, true, true);
	}
	
	/**
	 * Devuelve todos los servicios asignados a un tipo en un intervalo de 
	 * tiempo
	 * @param typeId
	 * @param dateStart YYYY-MM-dd
	 * @param dateEnd YYYY-MM-dd
	 * @param includeWeekend true para incluir los findes y festivos
	 * @param order true Date DESC, false DATE ASC
	 * @return
	 */
	public List<ServicioInfo> getServicesFromTypeService(int typeId, 
			String dateStart, String dateEnd, boolean includeWeekend, boolean order){
		return _getServicesFromTypeService(typeId, dateStart, dateEnd, includeWeekend, order);
	}
	
	/**
	 * 
	 * @param typeId
	 * @param dateStart
	 * @param dateEnd
	 * @param includeWeekend
	 * @param order true Date DESC, false DATE ASC
	 * @return
	 */
	private List<ServicioInfo> _getServicesFromTypeService(int typeId, 
			String dateStart, String dateEnd, boolean includeWeekend, boolean order){
		//SQLiteDatabase db = this.getReadableDatabase();
		//cambiamos a esta función para poder llamar dentro a otras funciones
		//de acceso a la db
		this.openRead();
		
		String sqlNotIn = "";
		//si no hay que incluir los findes y festivos
		if(!includeWeekend){
			List<String> datesNoInclude = this.getDaysIntervalOnlyWeekendDays(dateStart, dateEnd);
			sqlNotIn = " AND " + SERVICE_COLUMN_DATE + " NOT IN(";
			String sqlDates = "";
			for(String n: datesNoInclude)
			{
			    if(n != null){
			    	sqlDates += "'" + n + "', ";
			    }
			}		
			if(sqlDates.length() > 0){
				sqlNotIn += sqlDates.substring(0, sqlDates.length() - 2);
				sqlNotIn += ") ";				
			}else{
				sqlNotIn = "";
			}
		}		
		String colDateOrder = " DESC";
		if(!order) colDateOrder = " ASC";
		List<ServicioInfo> servicios = new ArrayList<ServicioInfo>();
		MyLog.d("getServicesFromTypeService", typeId + " " + dateStart + " " + dateEnd);
		Cursor cursor = this.db.query(SERVICE_TABLE_NAME,
				allColumnsService, SERVICE_COLUMN_TYPE_ID + " = '" + typeId + "' " +
						"AND " + SERVICE_COLUMN_DATE + " >= date('" + dateStart + "') " +
						"AND " + SERVICE_COLUMN_DATE + " <= date('" + dateEnd + "') " + 
						sqlNotIn, 
				null, null, null, SERVICE_COLUMN_DATE + colDateOrder);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ServicioInfo servicio = cursorToService(cursor);
			servicios.add(servicio);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		//db.close();
		this.closeDB();
		return servicios;
		
	}
	
	/**
	 * 
	 * @param date yyyy-MM-dd
	 * @return
	 */
	public ServicioInfo getServicio(String date) {
		//SQLiteDatabase db = this.getReadableDatabase();
		this.openRead();
		String[] args = new String[] { date };
		Cursor cursor = db.rawQuery("SELECT * FROM "
				+ SERVICE_TABLE_NAME + " WHERE " + SERVICE_COLUMN_DATE + " = ?", args);

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			ServicioInfo service = cursorToService(cursor);
			cursor.close();
			//db.close();
			this.closeDB();
			return service;
		} else {
			//db.close();
			this.closeDB();
			return new ServicioInfo();
		}

	}	
	
	/**
	 * 
	 * @param _ID
	 * @return
	 */
	public ServicioInfo getServicio(long _ID) {
		//SQLiteDatabase db = this.getReadableDatabase();
		this.openRead();
		String[] args = new String[] { String.valueOf(_ID) };
		Cursor cursor = db.rawQuery("SELECT * FROM "
				+ SERVICE_TABLE_NAME + " WHERE " + SERVICE_COLUMN_ID + " = ?", args);

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			ServicioInfo service = cursorToService(cursor);
			cursor.close();
			//db.close();
			this.closeDB();
			return service;
		} else {
			//db.close();
			this.closeDB();
			return new ServicioInfo();
		}
	}	

	/**
	 * Encuentra los servicios entre dos fechas
	 * @param start_date yyyy-MM-dd
	 * @param end_date yyyy-MM-dd
	 * @return List<ServicioInfo>
	 */
	public List<ServicioInfo> getServicesFromInterval(String start_date, String end_date){
		SQLiteDatabase db = this.getReadableDatabase();
		List<ServicioInfo> servicios = new ArrayList<ServicioInfo>();

		Cursor cursor = db.query(SERVICE_TABLE_NAME,
				allColumnsService, SERVICE_COLUMN_DATE + " >= '" + start_date + "' AND " + SERVICE_COLUMN_DATE + " <= '" + end_date + "'", 
				null, null, null, SERVICE_COLUMN_DATE + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ServicioInfo servicio = cursorToService(cursor);
			servicios.add(servicio);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return servicios;	
	}	
	
	/**
	 * Encuentra los servicios que contengan en sus comentarios la frase de la
	 * variable 'search'
	 * @param  search
	 * @return List<ServicioInfo>
	 */
	public List<ServicioInfo> getServicesFromSearchComments(String search, String dateStart,
			String dateEnd, int[] typesIds){
		SQLiteDatabase db = this.getReadableDatabase();
		List<ServicioInfo> servicios = new ArrayList<ServicioInfo>();
		
		String whereDateStart = "";
		if(dateStart.length() > 0){
			whereDateStart = " AND " + SERVICE_COLUMN_DATE + " >= '" + dateStart + "' ";
		}
		String whereDateEnd = "";
		if(dateEnd.length() > 0){
			whereDateEnd = " AND " + SERVICE_COLUMN_DATE + " <= '" + dateEnd + "' ";
		}
		String whereTypes = "";
		if(typesIds.length > 0){
			String typesString = "";
			for(int i=0; i < typesIds.length; i++){
				typesString += typesIds[i] + ",";
			}
			typesString = typesString.substring(0, typesString.length()-1);
			whereTypes = " AND " + SERVICE_COLUMN_TYPE_ID + " IN(" + typesString + ") ";
		}
		//MyLog.d(TAG, SERVICE_COLUMN_COMMENTS + " LIKE '%" + search + "%' " + whereDateStart + whereDateEnd + whereTypes);
		Cursor cursor = db.query(SERVICE_TABLE_NAME,
				allColumnsService, SERVICE_COLUMN_COMMENTS + " LIKE '%" + search + "%' "
				+ whereDateStart + whereDateEnd + whereTypes, 
				null, null, null, SERVICE_COLUMN_DATE + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ServicioInfo servicio = cursorToService(cursor);
			servicios.add(servicio);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return servicios;	
	}
	
	
	
	public TipoServicioInfo addTipoServicio(TipoServicioInfo tipoServicio) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		// preparamos los datos a insertar para el nuevo servicio
		ContentValues values = new ContentValues();
		if(tipoServicio.getId() > 0){
			values.put(SERVICE_COLUMN_ID, tipoServicio.getId());
		}		
		values.put(TYPE_SERVICE_COLUMN_TITLE, tipoServicio.getTitle());
		values.put(TYPE_SERVICE_COLUMN_NAME, tipoServicio.getName());
		values.put(TYPE_SERVICE_COLUMN_IS_DATE_RANGE, tipoServicio.getIsDateRange());
		values.put(TYPE_SERVICE_COLUMN_BG_COLOR, tipoServicio.getBgColor());
		values.put(TYPE_SERVICE_COLUMN_TEXT_COLOR, tipoServicio.getTextColor());
		values.put(TYPE_SERVICE_COLUMN_START_SCHEDULE, tipoServicio.getStartSchedule());
		values.put(TYPE_SERVICE_COLUMN_END_SCHEDULE, tipoServicio.getEndSchedule());
		values.put(TYPE_SERVICE_COLUMN_START_SCHEDULE2, tipoServicio.getStartSchedule2());
		values.put(TYPE_SERVICE_COLUMN_END_SCHEDULE2, tipoServicio.getEndSchedule2());
		values.put(TYPE_SERVICE_COLUMN_START_SCHEDULE3, tipoServicio.getStartSchedule3());
		values.put(TYPE_SERVICE_COLUMN_END_SCHEDULE3, tipoServicio.getEndSchedule3());
		values.put(TYPE_SERVICE_COLUMN_START_SCHEDULE4, tipoServicio.getStartSchedule4());
		values.put(TYPE_SERVICE_COLUMN_END_SCHEDULE4, tipoServicio.getEndSchedule4());
		values.put(TYPE_SERVICE_COLUMN_GUARDIA_COMBINADA, tipoServicio.getGuardiaCombinada());
		values.put(TYPE_SERVICE_COLUMN_TYPE_DAY, tipoServicio.getTypeDay());
		values.put(TYPE_SERVICE_COLUMN_SUCCESSION_COMMAND, tipoServicio.getSuccessionCommand());
		values.put(TYPE_SERVICE_COLUMN_ASK_SCHEDULE, tipoServicio.getAskSchedule());

		long insertId = db.insert(TYPE_SERVICE_TABLE_NAME,
				null, values);
		Cursor cursor = db.query(TYPE_SERVICE_TABLE_NAME,
				allColumnsTypeService, TYPE_SERVICE_COLUMN_ID + " = "
						+ insertId, null, null, null, null);
		if (cursor != null) cursor.moveToFirst();
		TipoServicioInfo newTipoServicio = cursorToTypeService(cursor);
		cursor.close();
		db.close();
		return newTipoServicio;
	}

	public Integer updateTipoServicio(TipoServicioInfo tipoServicio) {
		SQLiteDatabase db = this.getWritableDatabase();
		// preparamos los datos a actualizar para el servicio
		ContentValues values = new ContentValues();
		values.put(TYPE_SERVICE_COLUMN_TITLE, tipoServicio.getTitle());
		values.put(TYPE_SERVICE_COLUMN_NAME, tipoServicio.getName());
		values.put(TYPE_SERVICE_COLUMN_IS_DATE_RANGE, tipoServicio.getIsDateRange());
		values.put(TYPE_SERVICE_COLUMN_BG_COLOR, tipoServicio.getBgColor());
		values.put(TYPE_SERVICE_COLUMN_TEXT_COLOR, tipoServicio.getTextColor());
		values.put(TYPE_SERVICE_COLUMN_START_SCHEDULE, tipoServicio.getStartSchedule());
		values.put(TYPE_SERVICE_COLUMN_END_SCHEDULE, tipoServicio.getEndSchedule());
		values.put(TYPE_SERVICE_COLUMN_START_SCHEDULE2, tipoServicio.getStartSchedule2());
		values.put(TYPE_SERVICE_COLUMN_END_SCHEDULE2, tipoServicio.getEndSchedule2());
		values.put(TYPE_SERVICE_COLUMN_START_SCHEDULE3, tipoServicio.getStartSchedule3());
		values.put(TYPE_SERVICE_COLUMN_END_SCHEDULE3, tipoServicio.getEndSchedule3());
		values.put(TYPE_SERVICE_COLUMN_START_SCHEDULE4, tipoServicio.getStartSchedule4());
		values.put(TYPE_SERVICE_COLUMN_END_SCHEDULE4, tipoServicio.getEndSchedule4());
		values.put(TYPE_SERVICE_COLUMN_GUARDIA_COMBINADA, tipoServicio.getGuardiaCombinada());
		values.put(TYPE_SERVICE_COLUMN_TYPE_DAY, tipoServicio.getTypeDay());
		values.put(TYPE_SERVICE_COLUMN_SUCCESSION_COMMAND, tipoServicio.getSuccessionCommand());
		values.put(TYPE_SERVICE_COLUMN_ASK_SCHEDULE, tipoServicio.getAskSchedule());

		String[] whereArgs = { String.valueOf(tipoServicio.getId()) };
		
		Integer update = db.update(TYPE_SERVICE_TABLE_NAME, values,
				"_id = ?", whereArgs);
		db.close();
		
		return update;

	}	
	
	public void deleteTipoServicio(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TYPE_SERVICE_TABLE_NAME,
				TYPE_SERVICE_COLUMN_ID + " = " + id, null);
		db.close();
	}

	public List<TipoServicioInfo> getAllTipoServicios() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<TipoServicioInfo> tipo_servicios = new ArrayList<TipoServicioInfo>();

		Cursor cursor = db.query(TYPE_SERVICE_TABLE_NAME,
				allColumnsTypeService, null, null, null, null,
				TYPE_SERVICE_COLUMN_TITLE + " ASC");

		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				TipoServicioInfo tipo_servicio = cursorToTypeService(cursor);
				tipo_servicios.add(tipo_servicio);
				cursor.moveToNext();
			}
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return tipo_servicios;
	}
	
	public List<TipoServicioInfo> getAllTipoServiciosForAddTurn() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<TipoServicioInfo> tipo_servicios = new ArrayList<TipoServicioInfo>();

		Cursor cursor = db.query(TYPE_SERVICE_TABLE_NAME,
				allColumnsTypeService, TYPE_SERVICE_COLUMN_ASK_SCHEDULE + " = '0'", null, null, null,
				TYPE_SERVICE_COLUMN_TITLE + " ASC");

		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				TipoServicioInfo tipo_servicio = cursorToTypeService(cursor);
				tipo_servicios.add(tipo_servicio);
				cursor.moveToNext();
			}
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return tipo_servicios;
	}	

	public TipoServicioInfo getTipoServicio(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] args = new String[] { String.valueOf(id) };
		Cursor cursor = db.rawQuery("SELECT * FROM "
				+ TYPE_SERVICE_TABLE_NAME + " WHERE _id = ?", args);
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			TipoServicioInfo typeService = cursorToTypeService(cursor);
			cursor.close();
			db.close();
			return typeService;
		} else {
			db.close();
			return new TipoServicioInfo();
		}

	}	
	
	/**
	 * Obtiene agrupado por año el número de veces que aparece un tipo en servicios
	 * Column1 -> year
	 * Column2 -> count
	 * @param type_id
	 * @return
	 */
	public Cursor getTypeServiceHistory(int type_id){
		SQLiteDatabase db = this.getReadableDatabase();
		String[] columns = new String[] {"strftime('%Y', " + SERVICE_COLUMN_DATE + " ) as year", "COUNT(*) as count"};
		String[] selectionArgs = new String[] {String.valueOf(type_id)};
		String selection = SERVICE_COLUMN_TYPE_ID + " = ?";
		String groupBy = "strftime('%Y', " + SERVICE_COLUMN_DATE + " )";
		String orderBy = "year DESC";
		//select strftime("%Y", date) as year, COUNT(*)  from service where type_id = '1' GROUP BY strftime("%Y", date) ORDER BY year DESC
		//Cursor c = db.rawQuery("SELECT strftime('%Y', " + SERVICE_COLUMN_DATE + " ), COUNT(*)  FROM " + SERVICE_TABLE_NAME + " WHERE " + SERVICE_COLUMN_TYPE_ID + " = ? GROUP BY strftime('%Y', " + SERVICE_COLUMN_DATE + ")", args);
		Cursor c = db.query(SERVICE_TABLE_NAME, columns, selection, selectionArgs, groupBy, null, orderBy);
		//db.close();
		return c;		
	}
	
	

	protected ServicioInfo cursorToService(Cursor cursor) {
		ServicioInfo servicio = new ServicioInfo();

		servicio.setId(cursor.getLong(0));
		servicio.setDate(cursor.getString(1));
		servicio.setTypeId(cursor.getInt(2));
		servicio.setName(cursor.getString(3));
		servicio.setBgColor(cursor.getString(4));
		servicio.setTextColor(cursor.getString(5));
		servicio.setStartSchedule(cursor.getString(6));
		servicio.setEndSchedule(cursor.getString(7));
		servicio.setStartSchedule2(cursor.getString(8));
		servicio.setEndSchedule2(cursor.getString(9));
		servicio.setStartSchedule3(cursor.getString(10));
		servicio.setEndSchedule3(cursor.getString(11));
		servicio.setStartSchedule4(cursor.getString(12));
		servicio.setEndSchedule4(cursor.getString(13));
		servicio.setComments(cursor.getString(14));
		servicio.setIsHoliday(cursor.getInt(15));
		servicio.setGuardiaCombinada(cursor.getInt(16));
		servicio.setTypeDay(cursor.getInt(17));
		servicio.setIsImportant(cursor.getInt(18));
		servicio.setSuccessionCommand(cursor.getInt(19));

		return servicio;

	}	
	
	private TipoServicioInfo cursorToTypeService(Cursor cursor) {
		TipoServicioInfo tipo_servicio = new TipoServicioInfo();

		tipo_servicio.setId(cursor.getInt(0));
		tipo_servicio.setTitle(cursor.getString(1));
		tipo_servicio.setName(cursor.getString(2));
		tipo_servicio.setBgColor(cursor.getString(3));
		tipo_servicio.setTextColor(cursor.getString(4));
		tipo_servicio.setStartSchedule(cursor.getString(5));
		tipo_servicio.setEndSchedule(cursor.getString(6));
		tipo_servicio.setStartSchedule2(cursor.getString(7));
		tipo_servicio.setEndSchedule2(cursor.getString(8));
		tipo_servicio.setStartSchedule3(cursor.getString(9));
		tipo_servicio.setEndSchedule3(cursor.getString(10));
		tipo_servicio.setStartSchedule4(cursor.getString(11));
		tipo_servicio.setEndSchedule4(cursor.getString(12));
		tipo_servicio.setIsDateRange(cursor.getInt(13));
		tipo_servicio.setGuardiaCombinada(cursor.getInt(14));
		tipo_servicio.setTypeDay(cursor.getInt(15));
		tipo_servicio.setSuccessionCommand(cursor.getInt(16));
		tipo_servicio.setAskSchedule(cursor.getInt(17));

		return tipo_servicio;

	}		
	
	/**
	 * Entre dos fechas devuelve los fines de semana y festivos
	 * @param dateStart YYYY-MM-dd
	 * @param dateEnd YYYY-MM-dd
	 * @return
	 */
	public List<String> getDaysIntervalOnlyWeekendDays(String dateStart, String dateEnd){
		return this._getIntervalDays(dateStart, dateEnd, true);	
	}

	/**
	 * Entre dos fechas devuelve los fines de semana y festivos
	 * @param dateStart DateTime
	 * @param dateEnd DateTime
	 * @return
	 */
	public List<String> getDaysIntervalWithWeekendDays(DateTime dateStart, DateTime dateEnd){
		return this.getDaysIntervalOnlyWeekendDays(
				CuadranteDates.formatDate(dateStart.getYear(), dateStart.getMonthOfYear(), 
						dateStart.getDayOfMonth()), 
				CuadranteDates.formatDate(dateEnd.getYear(), dateEnd.getMonthOfYear(), 
						dateEnd.getDayOfMonth()));	
	}
	
	/**
	 * Devuelve entre dos fechas todos los dias sin incluir los fines de semana y festivos
	 * @param dateStart YYYY-MM-dd
	 * @param dateEnd YYYY-MM-dd
	 * @return
	 */
	public List<String> getDaysIntervalWithoutWeekendDays(String dateStart, String dateEnd){
		return this._getIntervalDays(dateStart, dateEnd, false);
	}

	/**
	 * Devuelve entre dos fechas todos los dias sin incluir los fines de semana y festivos
	 * @param dateStart DateTime
	 * @param dateEnd DateTime
	 * @return
	 */
	public List<String> getDaysIntervalWithoutWeekendDays(DateTime dateStart, DateTime dateEnd){
		return this.getDaysIntervalWithoutWeekendDays(
				CuadranteDates.formatDate(dateStart.getYear(), dateStart.getMonthOfYear(), 
						dateStart.getDayOfMonth()), 
				CuadranteDates.formatDate(dateEnd.getYear(), dateEnd.getMonthOfYear(), 
						dateEnd.getDayOfMonth()));	
	}
	
	private List<String> _getIntervalDays(String dateStart, String dateEnd, boolean getWeekend){
		CuadranteDates temp = new CuadranteDates(dateStart);
		DateTime dS = new DateTime(temp.getYear(), temp.getMonth(), temp.getDay(), 0, 0);		
		temp = new CuadranteDates(dateEnd);
		DateTime dE = new DateTime(temp.getYear(), temp.getMonth(), temp.getDay(), 0, 0);	
		
		List<String> datesNoInclude = new ArrayList<String>();
		//obtenemos los dias dentro del intervalo que sean festivos para
		//quitarlos junto con los fines de semana
		List<ServicioInfo> holidays = this.getHolidaysFromInterval(dateStart, dateEnd);
		for(ServicioInfo service: holidays){
			datesNoInclude.add(service.getDate());
		}
		
		
		while (dS.isBefore(dE) || dS.isEqual(dE)) {
			if(getWeekend){
				if(dS.getDayOfWeek() == DateTimeConstants.SATURDAY 
						|| dS.getDayOfWeek() == DateTimeConstants.SUNDAY){
					datesNoInclude.add(CuadranteDates.formatDate(dS.getYear(), dS.getMonthOfYear(), dS.getDayOfMonth()));
				}				
			}else{
				if(dS.getDayOfWeek() != DateTimeConstants.SATURDAY 
						&& dS.getDayOfWeek() != DateTimeConstants.SUNDAY){
					datesNoInclude.add(CuadranteDates.formatDate(dS.getYear(), dS.getMonthOfYear(), dS.getDayOfMonth()));
				}				
			}
		    dS = dS.plusDays(1);
		}			
		return datesNoInclude;

	}
	
	/**
	 * Obtiene los dias festivos entre dos fechas
	 * @param startDate yyyy-mm-dd
	 * @param endDate yyyy-mm-dd
	 * @return
	 */
	public List<ServicioInfo> getHolidaysFromInterval(String startDate, String endDate) {
		//SQLiteDatabase db = this.getReadableDatabase();
		this.openRead();
		List<ServicioInfo> servicios = new ArrayList<ServicioInfo>();

		Cursor cursor = this.db.query(SERVICE_TABLE_NAME,
				allColumnsService, SERVICE_COLUMN_DATE + " >= '" + startDate + "' " 
				+ "AND " + SERVICE_COLUMN_DATE + " <= '" + endDate + "' "
				+ "AND " + SERVICE_COLUMN_IS_HOLIDAY + " = '1'", null,
				null, null, SERVICE_COLUMN_DATE + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ServicioInfo servicio = cursorToService(cursor);
			servicios.add(servicio);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		this.closeDB();
		return servicios;
	}
	
	public void deleteComision(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(COMISION_TABLE_NAME,
				COMISION_COLUMN_ID + " = " + id, null);
		deleteHotelFromComision(id);
		db.close();
	}
	
	
	public ComisionInfo insertComision(ComisionInfo comision){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		if(comision.getId() > 0) values.put(COMISION_COLUMN_ID, comision.getId());
		values.put(COMISION_COLUMN_NAME, comision.getName());
		values.put(COMISION_COLUMN_COMMENTS, comision.getComments());
		values.put(COMISION_COLUMN_START_DATE, comision.getStartDate());
		values.put(COMISION_COLUMN_END_DATE, comision.getEndDate());
		values.put(COMISION_COLUMN_VIEW_TOTAL_EXPENSES, comision.getViewTotalExpenses());
		long insertId = db.insert(COMISION_TABLE_NAME, null,
				values);
		Cursor cursor;
		cursor = db.query(COMISION_TABLE_NAME,
				allColumnsComision, COMISION_COLUMN_ID + " = " + insertId,
				null, null, null, null);			

		cursor.moveToFirst();
		ComisionInfo newComision = cursorToComision(cursor);
		//muy importante esto, primero se cierra el cursor y despues la bd
		cursor.close();
		db.close();
		return newComision;		
	}
	
	public Integer updateComision(ComisionInfo comision) {
		SQLiteDatabase db = this.getWritableDatabase();
		// preparamos los datos a insertar para la nueva comisión
		ContentValues values = new ContentValues();
		values.put(COMISION_COLUMN_ID, comision.getId());
		values.put(COMISION_COLUMN_NAME, comision.getName());
		values.put(COMISION_COLUMN_COMMENTS, comision.getComments());
		values.put(COMISION_COLUMN_START_DATE, comision.getStartDate());
		values.put(COMISION_COLUMN_END_DATE, comision.getEndDate());
		values.put(COMISION_COLUMN_VIEW_TOTAL_EXPENSES, comision.getViewTotalExpenses());

		Integer update = db.update(
				COMISION_TABLE_NAME, 
				values,
				COMISION_COLUMN_ID + " = " + comision.getId(), 
				null);
		db.close();
		return update;
	}		
	
	public ComisionInfo getComision(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] args = new String[] { String.valueOf(id) };
		Cursor cursor = db.rawQuery("SELECT * FROM "
				+ COMISION_TABLE_NAME + " WHERE " + COMISION_COLUMN_ID + " = ?", args);
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			ComisionInfo comision = cursorToComision(cursor);
			db.close();
			return comision;
		} else {
			db.close();
			return new ComisionInfo();
		}
	}	
	
	/**
	 * Obtiene todas las comisiones que empiezan y acaban en el mes
	 * @param year
	 * @param month
	 * @return
	 */
	public List<ComisionInfo> getComisionInMonth(int year, int month) {
		String month2 = CuadranteDates.pad(month);
		SQLiteDatabase db = this.getReadableDatabase();
		List<ComisionInfo> comisions = new ArrayList<ComisionInfo>();

		Cursor cursor = db.query(COMISION_TABLE_NAME,
				allColumnsComision, COMISION_COLUMN_START_DATE + " LIKE '" + year
						+ "-" + month2 + "-%' OR "
						+ COMISION_COLUMN_END_DATE + " LIKE '" + year + "-" + month2 + "-%' OR ("
						+ COMISION_COLUMN_START_DATE + " < '" + year
						+ "-" + month2 + "-01' AND " 
						+ COMISION_COLUMN_END_DATE + " > '" + year + "-" + month2 + "-28')"
						, null, null, null, COMISION_COLUMN_START_DATE + " DESC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ComisionInfo comision = cursorToComision(cursor);
			comisions.add(comision);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return comisions;
	}
	
	/**
	 * Obtiene todas las comisiones que empiezan en un año
	 * @param year
	 * @return
	 */
	public List<ComisionInfo> getComisionInYear(int year) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<ComisionInfo> comisions = new ArrayList<ComisionInfo>();

		Cursor cursor = db.query(COMISION_TABLE_NAME,
				allColumnsComision, COMISION_COLUMN_START_DATE + " LIKE '" + year
						+ "-%'"
						, null, null, null, COMISION_COLUMN_START_DATE + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ComisionInfo comision = cursorToComision(cursor);
			comisions.add(comision);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return comisions;
	}

	/**
	 * Obtiene todas las comisiones que hay activas entre dos fechas
	 * @param dateStart yyyy-mm-dd
	 * @param dateEnd yyyy-mm-dd
	 * @return
	 */
	public List<ComisionInfo> getComisionFromInterval(String dateStart, String dateEnd) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<ComisionInfo> comisions = new ArrayList<ComisionInfo>();

		Cursor cursor = db.query(COMISION_TABLE_NAME,
				allColumnsComision, "(" + COMISION_COLUMN_START_DATE + " >= '" + dateStart  + "' AND "
						+ COMISION_COLUMN_END_DATE + " <= '" + dateEnd + "')"
						+ " OR (" 
								+ COMISION_COLUMN_START_DATE + " <= '" + dateStart + "' AND "
								+ COMISION_COLUMN_END_DATE + " >= '" + dateEnd + "')"
						+ " OR (" 
								+ COMISION_COLUMN_START_DATE + " <= '" + dateStart + "' AND "
								+ COMISION_COLUMN_END_DATE + " >= '" + dateStart + "' AND "
								+ COMISION_COLUMN_END_DATE + " <= '" + dateEnd + "')"
						+ " OR (" 
								+ COMISION_COLUMN_START_DATE + " >= '" + dateStart + "' AND "
								+ COMISION_COLUMN_START_DATE + " <= '" + dateEnd + "' AND "
								+ COMISION_COLUMN_END_DATE + " >= '" + dateEnd + "')"
						, null, null, null, COMISION_COLUMN_START_DATE + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ComisionInfo comision = cursorToComision(cursor);
			comisions.add(comision);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return comisions;
	}
	
	
	/**
	 * Obtiene todas las comisiones
	 * @return
	 */
	public List<ComisionInfo> getAllCommissions() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<ComisionInfo> commissions = new ArrayList<ComisionInfo>();

		Cursor cursor = db.query(COMISION_TABLE_NAME,
				allColumnsComision, null, null, null, null, COMISION_COLUMN_START_DATE + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ComisionInfo commission = cursorToComision(cursor);
			commissions.add(commission);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return commissions;
	}
	
	protected ComisionInfo cursorToComision(Cursor cursor) {
		ComisionInfo comision = new ComisionInfo(
				cursor.getLong(0),
				cursor.getString(1),
				cursor.getString(2),
				cursor.getString(3),
				cursor.getString(4),
				cursor.getInt(5)
				);

		return comision;

	}	
	
	/**
	 * Elimina todos los hoteles de una comisión
	 * @param id
	 */
	public void deleteHotelFromComision(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(HOTEL_TABLE_NAME, HOTEL_COLUMN_COMISION_ID + " = " + id, null);
		db.close();
	}
	
	public void deleteHotel(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(HOTEL_TABLE_NAME,
				HOTEL_COLUMN_ID + " = " + id, null);
		db.close();
	}
	
	public HotelInfo insertHotel(HotelInfo hotel){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		if(hotel.getId() > 0) values.put(HOTEL_COLUMN_ID, hotel.getId());
		values.put(HOTEL_COLUMN_COMISION_ID, hotel.getComisionId());
		values.put(HOTEL_COLUMN_NAME, hotel.getName());
		values.put(HOTEL_COLUMN_COMMENTS, hotel.getComments());
		values.put(HOTEL_COLUMN_START_DATE, hotel.getStartDate());
		values.put(HOTEL_COLUMN_END_DATE, hotel.getEndDate());
		values.put(HOTEL_COLUMN_DAILY_EXPENSES, hotel.getDailyExpenses());
		values.put(HOTEL_COLUMN_LAUNDRY, hotel.getLaundry());
		values.put(HOTEL_COLUMN_MANUTENCION_EXPENSES, hotel.getManutencionExpenses());
		long insertId = db.insert(HOTEL_TABLE_NAME, null,
				values);
		Cursor cursor = db.query(HOTEL_TABLE_NAME,
				allColumnsHotel, HOTEL_COLUMN_ID + " = " + insertId,
				null, null, null, null);			

		cursor.moveToFirst();
		HotelInfo newHotel = cursorToHotel(cursor);
		//muy importante esto, primero se cierra el cursor y despues la bd
		cursor.close();
		db.close();
		return newHotel;		
	}
	
	public List<HotelInfo> getHotelsComision(long comision_id){
		SQLiteDatabase db = this.getReadableDatabase();
		List<HotelInfo> hotels = new ArrayList<HotelInfo>();

		Cursor cursor = db.query(HOTEL_TABLE_NAME,
				allColumnsHotel, HOTEL_COLUMN_COMISION_ID + " = " + comision_id
						, null, null, null, COMISION_COLUMN_START_DATE + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			HotelInfo hotel = cursorToHotel(cursor);
			hotels.add(hotel);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return hotels;
		
	}

	protected HotelInfo cursorToHotel(Cursor cursor) {
		HotelInfo hotel = new HotelInfo();

		hotel.setId(cursor.getLong(0));
		hotel.setComisionId(cursor.getLong(1));
		hotel.setName(cursor.getString(2));
		hotel.setComments(cursor.getString(3));
		hotel.setStartDate(cursor.getString(4));
		hotel.setEndDate(cursor.getString(5));
		hotel.setDailyExpenses(cursor.getString(6));
		hotel.setLaundry(cursor.getString(7));
		hotel.setManutencionExpenses(cursor.getString(8));

		return hotel;

	}

    /**
     * Obtiene todos los horarios del mes dado
     * @param year
     * @param month
     * @return
     */
    public HoursInfo getMonthHours(int year, int month){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(HOURS_TABLE_NAME,
                allColumnsHours,
                HOURS_COLUMN_YEAR + " = '" + year + "' AND "
                        + HOURS_COLUMN_MONTH + " = '" + month + "'"
                , null, null, null, HOURS_COLUMN_MONTH + " ASC");
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            HoursInfo hours = cursorToHours(cursor);
            db.close();
            return hours;
        } else {
            db.close();
            return new HoursInfo();
        }
    }
    /**
     * Obtiene todos los horarios del trimestre de un mes dado
     * @param year
     * @param month
     * @return
     */
    public SparseArray<HoursInfo> getQuarterHours(int year, int month){
        SQLiteDatabase db = this.getReadableDatabase();
        SparseArray<HoursInfo> hours = new SparseArray<HoursInfo>();

        List<Integer> months = CuadranteDates.getQuarterMonths(month);

        Cursor cursor = db.query(HOURS_TABLE_NAME,
                allColumnsHours,
                HOURS_COLUMN_YEAR + " = '" + year + "' AND ("
                        + HOURS_COLUMN_MONTH + " IN('"
                        + months.get(0) + "', '" + months.get(1) + "', '" + months.get(2) + "'))"
                , null, null, null, HOURS_COLUMN_MONTH + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HoursInfo hour = cursorToHours(cursor);
            hours.put(hour.getMonth(), hour);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        db.close();
        return hours;

    }

    /**
     * Obtiene los horarios del cuatrimestre de un mes dado
     * @param year
     * @param month
     * @return
     */
    public SparseArray<HoursInfo> getQuarter2Hours(int year, int month){
        SQLiteDatabase db = this.getReadableDatabase();
        SparseArray<HoursInfo> hours = new SparseArray<HoursInfo>();

        List<Integer> months = CuadranteDates.getQuarter2Months(month);

        Cursor cursor = db.query(HOURS_TABLE_NAME,
                allColumnsHours,
                HOURS_COLUMN_YEAR + " = '" + year + "' AND ("
                        + HOURS_COLUMN_MONTH + " IN('"
                        + months.get(0) + "', '" + months.get(1)
                        + "', '" + months.get(2) + "', '" + months.get(3) + "'))"
                , null, null, null, HOURS_COLUMN_MONTH + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HoursInfo hour = cursorToHours(cursor);
            hours.put(hour.getMonth(), hour);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        db.close();
        return hours;

    }
	
	/**
	 * Graba las horas de un mes
	 * @param hours
	 * @return
	 */
	public HoursInfo insertHours(HoursInfo hours){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(HOURS_COLUMN_YEAR, hours.getYear());
		values.put(HOURS_COLUMN_MONTH, hours.getMonth());
		values.put(HOURS_COLUMN_HOURS, hours.getHours());
		values.put(HOURS_COLUMN_REFERENCE, hours.getReference());
		values.put(HOURS_COLUMN_F2, hours.getF2());
		values.put(HOURS_COLUMN_GUARDIAS, hours.getGuardias());
		values.put(HOURS_COLUMN_F2_HOURS, hours.getF2Hours());
		long insertId = db.insert(HOURS_TABLE_NAME, null,
				values);
		Cursor cursor = db.query(HOURS_TABLE_NAME,
				allColumnsHours, HOURS_COLUMN_ID + " = " + insertId,
				null, null, null, null);			

		cursor.moveToFirst();
		HoursInfo newHoursInfo = cursorToHours(cursor);
		//muy importante esto, primero se cierra el cursor y despues la bd
		cursor.close();
		db.close();
		return newHoursInfo;
	}
	
	/**
	 * Elimina el computo de horas de un mes
	 * @param year
	 * @param month
	 */
	public void deleteHours(int year, int month){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(HOURS_TABLE_NAME, HOURS_COLUMN_YEAR + " = '" + year + "' AND month = '" + month + "'", null);
		db.close();		
	}

	protected HoursInfo cursorToHours(Cursor cursor) {
		HoursInfo hours = new HoursInfo();

		hours.setId(cursor.getInt(0));
		hours.setYear(cursor.getInt(1));
		hours.setMonth(cursor.getInt(2));
		hours.setHours(cursor.getDouble(3));
		hours.setReference(cursor.getDouble(4));
		hours.setF2(cursor.getDouble(5));
		hours.setGuardias(cursor.getInt(6));
		hours.setF2Hours(cursor.getDouble(7));

		return hours;

	}	
	
	/**
	 * Graba el turno
	 * @param turn
	 * @return
	 */
	public TurnInfo insertTurn(TurnInfo turn){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		if(turn.getId() > 0) values.put(TURN_COLUMN_ID, turn.getId());
		values.put(TURN_COLUMN_NAME, turn.getName());		
		long insertId = db.insert(TURN_TABLE_NAME, null,
				values);
		Cursor cursor = db.query(TURN_TABLE_NAME,
				allColumnsTurn, TURN_COLUMN_ID + " = " + insertId,
				null, null, null, null);		
		cursor.moveToFirst();
		turn = cursorToTurn(cursor);
		//muy importante esto, primero se cierra el cursor y despues la bd
		cursor.close();
		db.close();
		return turn;
	}
	
	public Integer updateTurn(TurnInfo turn) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TURN_COLUMN_ID, turn.getId());
		values.put(TURN_COLUMN_NAME, turn.getName());

		Integer update = db.update(
				TURN_TABLE_NAME, 
				values,
				TURN_COLUMN_ID + " = " + turn.getId(), 
				null);			
		db.close();
		return update;
	}		
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public TurnInfo getTurn(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] args = new String[] { String.valueOf(id) };
		Cursor cursor = db.rawQuery("SELECT * FROM "
				+ TURN_TABLE_NAME + " WHERE " + TURN_COLUMN_ID + " = ?", args);
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			TurnInfo turn = cursorToTurn(cursor);
			db.close();
			return turn;
		} else {
			db.close();
			return new TurnInfo();
		}
	}	
	
	public List<TurnInfo> getAllTurns() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<TurnInfo> turns = new ArrayList<TurnInfo>();

		Cursor cursor = db.query(TURN_TABLE_NAME,
				allColumnsTurn, null, null, null, null, TURN_COLUMN_NAME + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TurnInfo turn = cursorToTurn(cursor);
			turns.add(turn);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return turns;
	}	
	
	public void deleteTurn(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TURN_TABLE_NAME, TURN_COLUMN_ID + " = '" + id + "'", null);
		db.close();		
	}	
	
	protected TurnInfo cursorToTurn(Cursor cursor) {
		TurnInfo turn = new TurnInfo();

		turn.setId(cursor.getInt(0));
		turn.setName(cursor.getString(1));

		return turn;

	}	
	
	public long insertTurnType(TurnTypeInfo turnType){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(TURN_TYPE_COLUMN_TURN_ID, turnType.getTurnId());
		values.put(TURN_TYPE_COLUMN_TYPE_ID, turnType.getTypeId());
		values.put(TURN_TYPE_COLUMN_ORDEN, turnType.getOrden());
		values.put(TURN_TYPE_COLUMN_SALIENTE, turnType.getSaliente());

		long insertId = db.insert(TURN_TYPE_TABLE_NAME, null,
				values);
		db.close();
		return insertId;
	}
	
	/**
	 * Devuelve todos los tipos de un turno
	 * @param turnId
	 * @return
	 */
	public List<TurnTypeInfo> getTurnTypes(int turnId) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<TurnTypeInfo> turnTypes = new ArrayList<TurnTypeInfo>();
		
		String selectQuery = "SELECT tt.*, ts." + TYPE_SERVICE_COLUMN_NAME + " FROM " 
				+ TURN_TYPE_TABLE_NAME + " tt, " + TYPE_SERVICE_TABLE_NAME + " ts WHERE tt." 
				+ TURN_TYPE_COLUMN_TYPE_ID + " = ts." + TYPE_SERVICE_COLUMN_ID + " AND tt."
				+ TURN_TYPE_COLUMN_TURN_ID + " = '" + turnId + "' ORDER BY "
				+ TURN_TYPE_COLUMN_ORDEN + " ASC";
		//MyLog.d("getTurnTypes", selectQuery);
		/*
		Cursor cursor = db.query(TURN_TYPE_TABLE_NAME,
				allColumnsTurnType, null, null, null, null,
				TURN_TYPE_COLUMN_ORDEN + " ASC");
		*/
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				TurnTypeInfo turnType = cursorToTurnType(cursor);
				turnTypes.add(turnType);
				cursor.moveToNext();
			}
		}
		// Make sure to close the cursor
		cursor.close();
		db.close();
		return turnTypes;
	}
	
	public void deleteTurnTypes(int turnId){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TURN_TYPE_TABLE_NAME, TURN_TYPE_COLUMN_TURN_ID + " = '" + turnId + "'", null);
		db.close();		
	}	
	
	protected TurnTypeInfo cursorToTurnType(Cursor cursor) {
		TurnTypeInfo turnType = new TurnTypeInfo();

		turnType.setTurnId(cursor.getInt(0));
		turnType.setTypeId(cursor.getInt(1));
		turnType.setOrden(cursor.getInt(2));
		turnType.setSaliente(cursor.getInt(3));
		turnType.setName(cursor.getString(4));

		return turnType;
	}	
	
	public void updateSchedules() {
		/*
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE " + SERVICE_TABLE_NAME + " SET start_schedule = '', end_schedule = '' WHERE start_schedule = end_schedule");
		db.execSQL("UPDATE " + SERVICE_TABLE_NAME + " SET start_schedule2 = '', end_schedule2 = '' WHERE start_schedule2 = end_schedule2");
		db.execSQL("UPDATE " + SERVICE_TABLE_NAME + " SET start_schedule3 = '', end_schedule3 = '' WHERE start_schedule3 = end_schedule3");
		db.execSQL("UPDATE " + SERVICE_TABLE_NAME + " SET start_schedule4 = '', end_schedule4 = '' WHERE start_schedule4 = end_schedule4");

		db.execSQL("UPDATE " + TYPE_SERVICE_TABLE_NAME + " SET start_schedule = '', end_schedule = '' WHERE start_schedule = end_schedule");
		db.execSQL("UPDATE " + TYPE_SERVICE_TABLE_NAME + " SET start_schedule2 = '', end_schedule2 = '' WHERE start_schedule2 = end_schedule2");
		db.execSQL("UPDATE " + TYPE_SERVICE_TABLE_NAME + " SET start_schedule3 = '', end_schedule3 = '' WHERE start_schedule3 = end_schedule3");
		db.execSQL("UPDATE " + TYPE_SERVICE_TABLE_NAME + " SET start_schedule4 = '', end_schedule4 = '' WHERE start_schedule4 = end_schedule4");

		db.close();
		*/
	}		
	
	
}