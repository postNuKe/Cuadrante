package es.progmac.cuadrante.lib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.progmac.cuadrante.R;
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
import android.database.sqlite.SQLiteDatabase;
import android.media.ExifInterface;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class DatabaseAssistant
{
	private static final String TAG = "DatabaseAssistant";
	public static final String EXPORT_FILE_NAME = Environment.getExternalStorageDirectory().getPath() + "/backup_db_cuadrante.xml";

	private Context _ctx;
	private SQLiteDatabase _db;
	private DatabaseHandler _db2;
	private Exporter _exporter;
	private Importer _importer;

	public DatabaseAssistant( Context ctx, SQLiteDatabase db )
	{
		_ctx = ctx;
		_db = db;

	}
	
	public String importData(DatabaseHandler db, String file){
		log("Importing Data");
		_db2 = db;
		_db2.destroy();
		Importer _importer = new Importer(file);
		return _importer.getFileAttrName();
	}

	public void exportData( )
	{
		log( "Exporting Data" );
		try
		{
			// create a file on the sdcard to export the
			// database contents to
			File myFile = new File( EXPORT_FILE_NAME );
			myFile.createNewFile();

			FileOutputStream fOut =  new FileOutputStream(myFile);
			BufferedOutputStream bos = new BufferedOutputStream( fOut );

			_exporter = new Exporter( bos );
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			_exporter.startDbExport(DatabaseHandler.DB_NAME, DatabaseHandler.DB_VERSION );

			// get the tables out of the given sqlite database
			String sql = "SELECT * FROM sqlite_master where type='table'";

			Cursor cur = _db.rawQuery( sql, new String[0] );
			//MyLog.d("db", "show tables, cur size " + cur.getCount() );
			cur.moveToFirst();

			String tableName;
			while ( cur.getPosition() < cur.getCount() )
			{
				tableName = cur.getString( cur.getColumnIndex( "name" ) );
				log( "table name " + tableName );

				// don't process these two tables since they are used
				// for metadata
				if ( ! tableName.equals( "android_metadata" ) &&
						! tableName.equals( "sqlite_sequence" ) )
				{
					exportTable( tableName );
				}

				cur.moveToNext();
			}
			
			//grabamos los shared preferences
			Map<String,?> keys = PreferenceManager.getDefaultSharedPreferences(_ctx).getAll();
			_exporter.startTableSharedPreferencesExport();
			_exporter.startRow();
			for(Map.Entry<String,?> entry : keys.entrySet()){
				_exporter.addColumn(entry.getKey(), entry.getValue().toString());
				MyLog.d("map values",entry.getKey() + ": " + entry.getValue().toString());            
			}
			_exporter.endRow();
			_exporter.endTable();
			
			
			_exporter.endDbExport();
			_exporter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void exportTable( String tableName ) throws IOException
	{
		_exporter.startTable(tableName);

		// get everything from the table
		String sql = "select * from " + tableName;
		Cursor cur = _db.rawQuery( sql, new String[0] );
		int numcols = cur.getColumnCount();

		MyLog.d(TAG, "Start exporting table " + tableName );

		//		// logging
		//		for( int idx = 0; idx < numcols; idx++ )
		//		{
		//			log( "column " + cur.getColumnName(idx) );
		//		}

		cur.moveToFirst();

		// move through the table, creating rows
		// and adding each column with name and value
		// to the row
		while( cur.getPosition() < cur.getCount() )
		{
			_exporter.startRow();
			String name;
			String val;
			for( int idx = 0; idx < numcols; idx++ )
			{
				name = cur.getColumnName(idx);
				val = cur.getString( idx );
				//log( "col '" + name + "' -- val '" + val + "'" );

				_exporter.addColumn( name, val );
			}

			_exporter.endRow();
			cur.moveToNext();
		}

		cur.close();

		_exporter.endTable();
	}

	private void log( String msg )
	{
		MyLog.d( "DatabaseAssistant", msg );
	}
	
	/**
	 * This method ensures that the output String has only
	 * valid XML unicode characters as specified by the
	 * XML 1.0 standard. For reference, please see
	 * <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
	 * standard</a>. This method will return an empty
	 * String if the input is null or empty.
	 *
	 * @param in The String whose non-valid characters we want to remove.
	 * @return The in String, stripped of non-valid characters.
	 */
	public static String stripNonValidXMLCharacters(String in) {
		StringBuffer out = new StringBuffer(); // Used to hold the output.
		char current; // Used to reference the current character.

		if (in == null || ("".equals(in))) return ""; // vacancy test.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
			if ((current == 0x9) ||
					(current == 0xA) ||
					(current == 0xD) ||
					((current >= 0x20) && (current <= 0xD7FF)) ||
					((current >= 0xE000) && (current <= 0xFFFD)) ||
					((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}	
	
	class Exporter
	{
		private static final String CLOSING_WITH_TICK = "'>";
		private static final String START_DB = "<export-database name='";
		private static final String END_DB = "\r\n" + "</export-database>";
		private static final String START_TABLE =  "\r\n" + "<table name='";
		private static final String END_TABLE = "</table>";
		private static final String START_ROW = "\r\n" + "<row>";
		private static final String END_ROW = "</row>";
		private static final String START_COL = "<col name='";
		private static final String END_COL = "</col>";

		private BufferedOutputStream _bos;

		public Exporter() throws FileNotFoundException
		{
			this( new BufferedOutputStream(
					_ctx.openFileOutput( EXPORT_FILE_NAME,
							Context.MODE_WORLD_READABLE ) ) );
		}

		public Exporter( BufferedOutputStream bos )
		{
			_bos = bos;
		}

		public void close() throws IOException
		{
			if ( _bos != null )
			{
				_bos.close();
			}
		}

		public void startDbExport( String dbName, int version) throws IOException
		{
			DateTime dt = new DateTime();
			String date = CuadranteDates.formatDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
			String stg = 
					"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>" 
					+ "\r\n" 
					+ START_DB + dbName + "' appversion='" 
					+ _ctx.getResources().getString(R.string.app_versionName) 
					+ "' date='" + date + "' dbversion='" 
					+ version + CLOSING_WITH_TICK;
			_bos.write( stg.getBytes() );
		}
		
		public void startTableSharedPreferencesExport() throws IOException{
			String stg = START_TABLE + Cuadrante.BACKUP_DB_TABLE_NAME_SP + CLOSING_WITH_TICK;
			_bos.write( stg.getBytes() ); 
		}

		public void endDbExport() throws IOException
		{
			_bos.write( END_DB.getBytes() );
		}

		public void startTable( String tableName ) throws IOException
		{
			String stg = START_TABLE + tableName + CLOSING_WITH_TICK;
			_bos.write( stg.getBytes() );
		}

		public void endTable() throws IOException
		{
			_bos.write( END_TABLE.getBytes() );
		}

		public void startRow() throws IOException
		{
			_bos.write( START_ROW.getBytes() );
		}

		public void endRow() throws IOException
		{
			_bos.write( END_ROW.getBytes() );
		}

		public void addColumn( String name, String val ) throws IOException
		{
			val = stripNonValidXMLCharacters(val);
			String stg = START_COL + name + CLOSING_WITH_TICK + val + END_COL;
			_bos.write( stg.getBytes() );
		} 		
	}

	class Importer
	{
		private String file_attr_name = "", file_attr_dbversion = "", 
				file_attr_appversion = "", file_attr_date = "";
		
		/**
		 * Nombre del atributo del archivo normalmente, grs o cuadrante nombre de la bd
		 * @return
		 */
		public String getFileAttrName(){
			return file_attr_name;
		}
		
		public Importer(String file){
			DocumentBuilderFactory builderFactory =
			        DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			try {
			    builder = builderFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
			    e.printStackTrace();  
			}
			
			try {
			    Document document = builder.parse(
			            new FileInputStream(file));
	        	
	        	//tablas
	        	Element rootElement = document.getDocumentElement();
	        	file_attr_name = rootElement.getAttribute("name");
	        	file_attr_appversion = rootElement.getAttribute("appversion");
	        	file_attr_date = rootElement.getAttribute("date");
	        	file_attr_dbversion = rootElement.getAttribute("dbversion");
	        	
	        	NodeList tables = rootElement.getChildNodes();
	        	for(int i=0; i < tables.getLength(); i++){
	        		Node table = tables.item(i);

	        		if(table instanceof Element){
	        			//a child element to process
	        			Element child = (Element) table;
	        			String table_name = child.getAttribute("name");
	        			MyLog.d(TAG, "table:" + table_name);
	        			//registros
	        			NodeList rows = table.getChildNodes();
	        			for(int j=0; j < rows.getLength(); j++){
	    	        		Node row = rows.item(j);

	    	        		if(row instanceof Element){
		    	        		int guardia_combinada = 0;
		    	        		int type_day = 0;
		    	        		int is_important = 0;
		    	        		int succession_command = 0;
		    	        		int ask_schedule = 0;
	    	        			//si la tabla no es la de shared_preferences
	    	        			//MyLog.d("importer", "tabla:" + table_name);
		    	        		if(!table_name.equals(Cuadrante.BACKUP_DB_TABLE_NAME_SP)){
		    	        			//columnas
		    	        			NodeList cols = row.getChildNodes();
		    	        			HashMap<String, String> record = new HashMap<String, String>();
		    	        			for(int k=0; k < cols.getLength(); k++){
		    	        				Element col = (Element) cols.item(k);
		    	        				record.put(col.getAttribute("name"), col.getTextContent());
		    	        			}
		    	        			if(table_name.equals(DatabaseHandler.SERVICE_TABLE_NAME)){
		    	        				if(getFileAttrName().equals("grs")){
		    	        					//si importamos una copia de la app Cuadrante, ponemos todos los servicios
		    	        					//de alerta en guardia combinada tipo D
		    	        					if(record.get("is_alert").equals("1")) 
		    	        						guardia_combinada = 4;
		    	        					
		    	        					//debemos de poner todos los horarios que sean iguales
		    	        					//a nada
		    	        					if(record.get(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE)
		    	        							.equals(record.get(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE))){
			    	        					record.put(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE, "");
			    	        					record.put(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE, "");
		    	        					}
		    	        					if(record.get(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE2)
		    	        							.equals(record.get(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE2))){
			    	        					record.put(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE2, "");
			    	        					record.put(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE2, "");
		    	        					}
		    	        					if(record.get(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE3)
		    	        							.equals(record.get(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE3))){
			    	        					record.put(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE3, "");
			    	        					record.put(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE3, "");
		    	        					}
		    	        					if(record.get(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE4)
		    	        							.equals(record.get(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE4))){
			    	        					record.put(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE4, "");
			    	        					record.put(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE4, "");
		    	        					}
		    	        				}else{
		    	        					guardia_combinada = Integer.parseInt(record.get(DatabaseHandler.SERVICE_COLUMN_GUARDIA_COMBINADA));
		    	        					if(record.get(DatabaseHandler.SERVICE_COLUMN_TYPE_DAY) != null)
		    	        						type_day = Integer.parseInt(record.get(DatabaseHandler.SERVICE_COLUMN_TYPE_DAY));
		    	        					else
		    	        						type_day = 0;
		    	        					if(record.get(DatabaseHandler.SERVICE_COLUMN_TYPE_DAY) != null)
		    	        						is_important = Integer.valueOf(record.get(DatabaseHandler.SERVICE_COLUMN_IS_IMPORTANT));
		    	        					else 
		    	        						is_important = 0;
		    	        					if(record.get(DatabaseHandler.SERVICE_COLUMN_SUCCESSION_COMMAND) != null)
		    	        						succession_command = Integer.valueOf(record.get(DatabaseHandler.SERVICE_COLUMN_SUCCESSION_COMMAND));
		    	        					else 
		    	        						succession_command = 0;
		    	        				}
		    	        				
		    	        				ServicioInfo service = new ServicioInfo(
		    	        						Integer.valueOf(record.get(DatabaseHandler.SERVICE_COLUMN_ID)), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_DATE), 
		    	        						Integer.valueOf(record.get(DatabaseHandler.SERVICE_COLUMN_TYPE_ID)), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_NAME), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_BG_COLOR), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_TEXT_COLOR), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE2), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE2), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE3), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE3), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_START_SCHEDULE4), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_END_SCHEDULE4), 
		    	        						record.get(DatabaseHandler.SERVICE_COLUMN_COMMENTS), 
		    	        						Integer.valueOf(record.get(DatabaseHandler.SERVICE_COLUMN_IS_HOLIDAY)),
		    	        						guardia_combinada,
		    	        						type_day,
		    	        						is_important,
		    	        						succession_command);
		    	        				_db2.addService(service);
		    	        			}else if(table_name.equals(DatabaseHandler.TYPE_SERVICE_TABLE_NAME)){
		    	        				if(getFileAttrName().equals("grs")){
		    	        					//si importamos una copia de la app Cuadrante, ponemos todos los tipos
		    	        					//de alerta en guardia combinada tipo D
		    	        					if(record.get("is_alert").equals("1")) 
		    	        						guardia_combinada = 4;
		    	        					//debemos de poner todos los horarios que sean iguales
		    	        					//a nada
		    	        					if(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE)
		    	        							.equals(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE))){
			    	        					record.put(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE, "");
			    	        					record.put(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE, "");
		    	        					}
		    	        					if(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE2)
		    	        							.equals(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE2))){
			    	        					record.put(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE2, "");
			    	        					record.put(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE2, "");
		    	        					}
		    	        					if(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE3)
		    	        							.equals(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE3))){
			    	        					record.put(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE3, "");
			    	        					record.put(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE3, "");
		    	        					}
		    	        					if(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE4)
		    	        							.equals(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE4))){
			    	        					record.put(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE4, "");
			    	        					record.put(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE4, "");
		    	        					}		    	        					
		    	        				}else{
		    	        					guardia_combinada = Integer.parseInt(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_GUARDIA_COMBINADA));
		    	        					if(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_TYPE_DAY) != null)
		    	        						type_day = Integer.parseInt(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_TYPE_DAY));
		    	        					else
		    	        						type_day = 0;
		    	        					if(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_SUCCESSION_COMMAND) != null)
		    	        						succession_command = Integer.parseInt(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_SUCCESSION_COMMAND));
		    	        					else
		    	        						succession_command = 0;
		    	        					if(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_ASK_SCHEDULE) != null)
		    	        						ask_schedule = Integer.parseInt(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_ASK_SCHEDULE));
		    	        					else
		    	        						ask_schedule = 0;
		    	        				}
		    	        				TipoServicioInfo type_service = new TipoServicioInfo(
		    	        						Integer.valueOf(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_ID)), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_TITLE), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_NAME), 
		    	        						Integer.valueOf(record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_IS_DATE_RANGE)), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_BG_COLOR), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_TEXT_COLOR), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE2), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE2), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE3), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE3), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_START_SCHEDULE4), 
		    	        						record.get(DatabaseHandler.TYPE_SERVICE_COLUMN_END_SCHEDULE4),
		    	        						guardia_combinada,
		    	        						type_day,
		    	        						succession_command,
		    	        						ask_schedule);
		    	        				_db2.addTipoServicio(type_service);	    	        				
		    	        			}else if(table_name.equals(DatabaseHandler.COMISION_TABLE_NAME)){
		    	        				ComisionInfo commission = new ComisionInfo(
		    	        						Integer.valueOf(record.get(DatabaseHandler.COMISION_COLUMN_ID)), 
		    	        						record.get(DatabaseHandler.COMISION_COLUMN_NAME), 
		    	        						record.get(DatabaseHandler.COMISION_COLUMN_COMMENTS), 
		    	        						record.get(DatabaseHandler.COMISION_COLUMN_START_DATE), 
		    	        						record.get(DatabaseHandler.COMISION_COLUMN_END_DATE),
		    	        						Integer.valueOf(record.get(DatabaseHandler.COMISION_COLUMN_VIEW_TOTAL_EXPENSES)));
		    	        				_db2.insertComision(commission);	    	        				
		    	        			}else if(table_name.equals(DatabaseHandler.HOTEL_TABLE_NAME)){
		    	        				HotelInfo hotel = new HotelInfo(
		    	        						Integer.valueOf(record.get(DatabaseHandler.HOTEL_COLUMN_ID)), 
		    	        						Integer.valueOf(record.get(DatabaseHandler.HOTEL_COLUMN_COMISION_ID)), 
		    	        						record.get(DatabaseHandler.HOTEL_COLUMN_NAME), 
		    	        						record.get(DatabaseHandler.HOTEL_COLUMN_COMMENTS), 
		    	        						record.get(DatabaseHandler.HOTEL_COLUMN_START_DATE), 
		    	        						record.get(DatabaseHandler.HOTEL_COLUMN_END_DATE),
		    	        						record.get(DatabaseHandler.HOTEL_COLUMN_DAILY_EXPENSES), 
		    	        						record.get(DatabaseHandler.HOTEL_COLUMN_MANUTENCION_EXPENSES), 
		    	        						record.get(DatabaseHandler.HOTEL_COLUMN_LAUNDRY));
		    	        				_db2.insertHotel(hotel);	    	        				
		    	        			}else if(table_name.equals(DatabaseHandler.HOURS_TABLE_NAME)){
		    	        				HoursInfo hours = new HoursInfo(
		    	        						Integer.valueOf(record.get(DatabaseHandler.HOURS_COLUMN_ID)), 
		    	        						Integer.valueOf(record.get(DatabaseHandler.HOURS_COLUMN_YEAR)), 
		    	        						Integer.valueOf(record.get(DatabaseHandler.HOURS_COLUMN_MONTH)), 
		    	        						Double.valueOf(record.get(DatabaseHandler.HOURS_COLUMN_HOURS)), 
		    	        						Double.valueOf(record.get(DatabaseHandler.HOURS_COLUMN_REFERENCE)),
		    	        						Double.valueOf(record.get(DatabaseHandler.HOURS_COLUMN_F2)), 
		    	        						Integer.valueOf(record.get(DatabaseHandler.HOURS_COLUMN_GUARDIAS)),
		    	        						Double.valueOf(record.get(DatabaseHandler.HOURS_COLUMN_F2_HOURS)));
		    	        				_db2.insertHours(hours);	    	        				
		    	        			}else if(table_name.equals(DatabaseHandler.TURN_TABLE_NAME)){
		    	        				TurnInfo turn = new TurnInfo(
		    	        						Integer.valueOf(record.get(DatabaseHandler.TURN_COLUMN_ID)), 
		    	        						record.get(DatabaseHandler.TURN_COLUMN_NAME));
		    	        				_db2.insertTurn(turn);	    	        				
		    	        			}else if(table_name.equals(DatabaseHandler.TURN_TYPE_TABLE_NAME)){
		    	        				TurnTypeInfo turnType = new TurnTypeInfo(
		    	        						Integer.valueOf(record.get(DatabaseHandler.TURN_TYPE_COLUMN_TURN_ID)), 
		    	        						Integer.valueOf(record.get(DatabaseHandler.TURN_TYPE_COLUMN_TYPE_ID)),
		    	        						Integer.valueOf(record.get(DatabaseHandler.TURN_TYPE_COLUMN_ORDEN)),
		    	        						Integer.valueOf(record.get(DatabaseHandler.TURN_TYPE_COLUMN_SALIENTE)));
		    	        				_db2.insertTurnType(turnType);	    	        				
		    	        			}
		    	        		//shared preferences
		    	        		}else{
		    	        			MyLog.d("importer", "shared_preferences");
		    	        			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(_ctx).edit();
		    	        			NodeList cols = row.getChildNodes();
		    	        			HashMap<String, String> record = new HashMap<String, String>();
		    	        			editor.clear().commit();
		    	        			
		    	        			for(int k=0; k < cols.getLength(); k++){
		    	        				Element col = (Element) cols.item(k);
		    	        				record.put(col.getAttribute("name"), col.getTextContent());
			    	        			MyLog.d("importer", col.getAttribute("name"));
			    	        			String type = "";
			    	        			//si existe la variable se guarda, si
			    	        			//no ponemos esto da error al importar
			    	        			//y cierra la app
			    	        			if(Sp.SHARED_PREFERENCES.containsKey(col.getAttribute("name"))){
				    	        			type = Sp.SHARED_PREFERENCES.get(col.getAttribute("name"));
			    	        				if(type.equals("boolean")){
			    	        					boolean value;
			    	        					if(col.getTextContent().equals("true")){
			    	        						value = true;
			    	        					}else{
			    	        						value = false;
			    	        					}
			    	        					editor.putBoolean(col.getAttribute("name"), value);
			    	        				}else if(type.equals("int")){
			    	        					int value = Integer.valueOf(col.getTextContent());
			    	        					editor.putInt(col.getAttribute("name"), value);
			    	        				}else if(type.equals("string")){
			    	        					editor.putString(col.getAttribute("name"), col.getTextContent());
			    	        				}
			    	        			}
		    	        			}	
		    	        			editor.commit();
		    	        		}
	    	        		}	        				
	        			}
	        		}
	        	}	       
			} catch (SAXException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			        
		}
	}

}