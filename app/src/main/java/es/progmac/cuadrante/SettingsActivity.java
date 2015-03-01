package es.progmac.cuadrante;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import com.commonsware.cwac.colormixer.ColorMixer;
import com.commonsware.cwac.colormixer.ColorMixerDialogCopy;


import es.progmac.cuadrante.R;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.cuadrante.info.TipoServicioInfo;
import es.progmac.cuadrante.lib.DatabaseAssistant;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.DialogSpinner;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.Pas;
import es.progmac.cuadrante.lib.Sp;
import es.progmac.android.others.ListPreferenceMultiSelect;
import es.progmac.android.fileexplorer.FileDialog;
import es.progmac.android.fileexplorer.SelectionMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private static final String TAG = "SettingsActivity";
	private static final String ZIPFILE_BACKUP_DB = Environment.getExternalStorageDirectory().getPath() + "/gc_backup.zip";
	public DatabaseHandler db;
	Context mContext;
	private DateTime mToday = new DateTime();
	/**
	 * Nombre de la base de datos del fichero a importar, grs o cuadrante lo más normal
	 */
	String importedFileDbName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(this);
		mContext = this;

		//setContentView(R.layout.activity_settings);
		addPreferencesFromResource(R.xml.preferences);

		//añadimos los tipos de servicios a los listados de vacaciones y asuntos
		//propios
		List<TipoServicioInfo> typeServices = (List<TipoServicioInfo>) db.getAllTipoServicios();
		CharSequence[] entries = new CharSequence[typeServices.size()];
		CharSequence[] entryValues = new CharSequence[typeServices.size()];

		int i = 0;
		for (TipoServicioInfo typeService : typeServices) {
			entries[i] = typeService.getTitle();
			entryValues[i] = String.valueOf(typeService.getId());
			i++;
		} 

		ListPreference listHolidays = (ListPreference) findPreference(Sp.SP_TYPE_SERVICE_HOLIDAYS);
		listHolidays.setEntries(entries);
		listHolidays.setEntryValues(entryValues);

		ListPreference listOwnAffairs = (ListPreference) findPreference(Sp.SP_TYPE_SERVICE_OWN_AFFAIRS);
		listOwnAffairs.setEntries(entries);
		listOwnAffairs.setEntryValues(entryValues);
        /*
		ListPreference listMedicalLeave = (ListPreference) findPreference(Sp.SP_TYPE_SERVICE_MEDICAL_LEAVE);
		listMedicalLeave.setEntries(entries);
		listMedicalLeave.setEntryValues(entryValues);
		*/

		//tipos servicios para comisiones
		/*
	        ListPreferenceMultiSelect listTypesComision = (ListPreferenceMultiSelect) findPreference("types_services_comision");
	        listTypesComision.setEntries(entries);
	        listTypesComision.setEntryValues(entryValues);  
		 */       

		//Pas
		//DateTime today = new DateTime();
		ListPreference listPas = (ListPreference) findPreference(Sp.SP_PAS);
		listPas.setSummary(String.format(this.getString(R.string.pas_summary)
				, mToday.getWeekOfWeekyear()
				, mToday.withDayOfWeek(DateTimeConstants.MONDAY).toString("dd/MM") 
				, mToday.withDayOfWeek(DateTimeConstants.SUNDAY).toString("dd/MM")));


		setPreference();
		//setPreference2();

		//gestionamos el valor introducido en la preferencia del email
		EditTextPreference spEmail = (EditTextPreference) getPreferenceScreen().findPreference(Sp.SP_EMAIL);
		spEmail.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Boolean rtnval = true;
				String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
				if (!Pattern.matches(pattern, newValue.toString())) {
					Toast.makeText(getApplicationContext(), R.string.email_error, Toast.LENGTH_LONG).show();
					rtnval = false;
				}  
				return rtnval;
			}
		});        

		//si se llama con un parametro de backup true, pues se hace
		Intent intent = getIntent();
		boolean AUTOBACKUP = intent.getBooleanExtra(Extra.AUTO_BACKUP, false);
		if(AUTOBACKUP){
			generateBackup();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Set up a listener whenever a key changes            
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		Cuadrante.checkSignIn(this);		
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes            
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
	}    

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// Let's do something a preference value changes
		if (key.equals(Sp.SP_DAYS_HOLIDAYS) || key.equals(Sp.SP_TYPE_SERVICE_HOLIDAYS)
				|| key.equals(Sp.SP_DAYS_OWN_AFFAIRS) || key.equals(Sp.SP_TYPE_SERVICE_OWN_AFFAIRS)) {
			setPreference();
		}else if(key.equals(Sp.SP_PAS) || key.equals(Sp.SP_PAS_ACTIVE)){
			//DateTime dt = new DateTime();
			Sp.setPasYear(mContext, mToday.getYear());
			Sp.setPasWeek(mContext, mToday.getWeekOfWeekyear());
		}else if(key.equals(Sp.SP_WIDGET_WEEK_FIRST_DAY)){
			Cuadrante.refreshWidget(mContext);
		}else if(key.equals(Sp.SP_SUNDAY_COLOR_ACTIVE)){
			Cuadrante.refreshWidget(mContext);
		}else if(key.equals(Sp.SP_WORKDAY_WEEK_HOURS) || key.equals(Sp.SP_WORKDAY_COMPUTING_HOURS)){//vaciamos la tabla de horas
            db.destroyHoursTable();
        }
	}    

	@Override
	/**
	 * Al hacer click en una preferencia
	 */
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {   	
		//exportamos la base de datos
		if(preference.getKey().equals("backup_db_export")){
			generateBackup();

		}else if(preference.getKey().equals("backup_db_import")){
			Intent intent = new Intent(getBaseContext(), FileDialog.class);
			intent.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory().getPath());

			//can user select directories or not
			intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
			//solo se pueden seleccionar archivos no crear nuevos
			intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);

			//alternatively you can set file filter
			intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "xml" });

			startActivityForResult(intent, Cuadrante.REQUEST_CODE_SAVE);    
			//actualizamos siempre el listado de tipos de servicios por defecto 
			//para las comisiones
		}else if(preference.getKey().equals("login")){//ventana de login
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(Extra.MODE, "edit");
			startActivityForResult(intent, Cuadrante.REQUEST_CODE_LOGIN_SAVE);			 
		}else if(preference.getKey().equals(Sp.SP_SUNDAY_BG_COLOR)){
			new ColorMixerDialogCopy(
					this, Sp.SP_SUNDAY_BG_COLOR_DV, 
					Sp.getSundayBgColor(mContext), onSetSundayBgColor).show();
		}else if(preference.getKey().equals(Sp.SP_SUNDAY_TEXT_COLOR)){
			new ColorMixerDialogCopy(
					this, Sp.SP_SUNDAY_TEXT_COLOR_DV, 
					Sp.getSundayTextColor(mContext), onSetSundayTextColor).show();
		}else if(preference.getKey().equals(Sp.SP_TODAY_BG_COLOR)){
			new ColorMixerDialogCopy(
					this, Sp.SP_TODAY_BG_COLOR_DV, 
					Sp.getTodayBgColor(mContext), onSetTodayBgColor).show();
		}else if(preference.getKey().equals(Sp.SP_TODAY_TEXT_COLOR)){
			new ColorMixerDialogCopy(
					this, Sp.SP_TODAY_TEXT_COLOR_DV, 
					Sp.getTodayTextColor(mContext), onSetTodayTextColor).show();
		}else if(preference.getKey().equals(Sp.SP_HOLIDAY_BG_COLOR)){
			new ColorMixerDialogCopy(
					this, Sp.SP_HOLIDAY_BG_COLOR_DV, 
					Sp.getHolidayBgColor(mContext), onSetHolidayBgColor).show();
		}else if(preference.getKey().equals(Sp.SP_HOLIDAY_TEXT_COLOR)){
			new ColorMixerDialogCopy(
					this, Sp.SP_HOLIDAY_TEXT_COLOR_DV, 
					Sp.getHolidayTextColor(mContext), onSetHolidayTextColor).show();
		}else if(preference.getKey().equals(Sp.SP_SERVICE_DEFAULT_BG_COLOR)){
			new ColorMixerDialogCopy(
					this, Sp.SP_SERVICE_DEFAULT_BG_COLOR_DV, 
					Sp.getServiceDefaultBgColor(mContext), onSetServiceDefaultBgColor).show();
		}else if(preference.getKey().equals(Sp.SP_SERVICE_DEFAULT_TEXT_COLOR)){
			new ColorMixerDialogCopy(
					this, Sp.SP_SERVICE_DEFAULT_TEXT_COLOR_DV, 
					Sp.getServiceDefaultTextColor(mContext), onSetServiceDefaultTextColor).show();
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);

	}
	
	/** Cuando se selecciona el color de fondo para los domingos */
	public ColorMixer.OnColorChangedListener onSetSundayBgColor=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			Sp.setSundayBgColor(mContext, color);
		}
	};
	/** Cuando se selecciona el color de texto para los domingos */
	public ColorMixer.OnColorChangedListener onSetSundayTextColor=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			Sp.setSundayTextColor(mContext, color);
		}
	};
	/** Cuando se selecciona el color de fondo para el día actual */
	public ColorMixer.OnColorChangedListener onSetTodayBgColor=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			Sp.setTodayBgColor(mContext, color);
		}
	};
	/** Cuando se selecciona el color de texto para el día actual */
	public ColorMixer.OnColorChangedListener onSetTodayTextColor=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			Sp.setTodayTextColor(mContext, color);
		}
	};
	/** Cuando se selecciona el color de fondo para los festivos */
	public ColorMixer.OnColorChangedListener onSetHolidayBgColor=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			Sp.setHolidayBgColor(mContext, color);
			db.updateHolidays(mContext);
		}
	};
	/** Cuando se selecciona el color de texto para los festivos */
	public ColorMixer.OnColorChangedListener onSetHolidayTextColor=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			Sp.setHolidayTextColor(mContext, color);
			db.updateHolidays(mContext);
		}
	};
	/** Cuando se selecciona el color de fondo para los servicios por defecto */
	public ColorMixer.OnColorChangedListener onSetServiceDefaultBgColor=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			Sp.setServiceDefaultBgColor(mContext, color);
		}
	};
	/** Cuando se selecciona el color de texto para los servicios por defecto */
	public ColorMixer.OnColorChangedListener onSetServiceDefaultTextColor=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			Sp.setServiceDefaultTextColor(mContext, color);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public synchronized void onActivityResult(final int requestCode,
			int resultCode, final Intent data) {
		switch (requestCode) {
		case Cuadrante.REQUEST_CODE_SAVE:
		case Cuadrante.REQUEST_CODE_LOAD:
			if (resultCode == Activity.RESULT_OK) {

				if (requestCode == Cuadrante.REQUEST_CODE_SAVE) {
					//System.out.println("Saving...");
				} else if (requestCode == Cuadrante.REQUEST_CODE_LOAD) {
					//System.out.println("Loading...");
				}
				//importamos la base de datos
				final String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
				//MyLog.d("onActivityResult", filePath);
				final ProgressDialog pd = ProgressDialog.show(this,
						getResources().getString(R.string.dialog_title_import_backup),
						getResources().getString(R.string.please_wait),
						true, false);
				pd.setOnCancelListener(new OnCancelListener() {
					String[] items;
					@Override
					public void onCancel(DialogInterface dialog) {
						//MyLog.d("onCancel", "dentro del metodo onCancel");
						//el archivo a importar es una copia de la app grs
						if(importedFileDbName.equals("grs")){		

							int holidays_id = Sp.getHolidaysTypeService(mContext);
							TipoServicioInfo type_service = db.getTipoServicio(holidays_id);
							type_service.setTypeDay(Cuadrante.TYPE_DAY_ORDINARY);
							db.updateTipoServicio(type_service);
							db.updateServicesFromTypeTypeDay(holidays_id, Cuadrante.TYPE_DAY_ORDINARY);

							int own_affairs_id = Sp.getOwnAffairsTypeService(mContext);
							type_service = db.getTipoServicio(own_affairs_id);
							//si tiene el nombre asuntos propios cambiarlo por asuntos particulares
							if(type_service.getTitle().contains("PROPIOS")){
								type_service.setTitle(
										type_service.getTitle().replace("PROPIOS", "PARTICULARES"));
							}
							type_service.setTypeDay(Cuadrante.TYPE_DAY_ESPECIAL);
							db.updateTipoServicio(type_service);
							db.updateServicesFromTypeTypeDay(own_affairs_id, Cuadrante.TYPE_DAY_ESPECIAL);							 

							final List<TipoServicioInfo> type_services = db.getAllTipoServicios();
							items = new String[type_services.size() + 1];
							items[0] = getString(R.string.no_type_service);
							int i = 1;
							for (TipoServicioInfo ts : type_services) {
								items[i] = ts.getTitle();
								i++;
							}
							/**
							 * Bajas
							 */
							final DialogSpinner ds = new DialogSpinner(
									mContext, 
									items, 
									R.string.title_dialog_medical_leave, 
									R.string.message_dialog_select_type_service_for_drop_medical_leave) {
								@Override
								public boolean onOkClicked(int position) {
									// do something
									//MyLog.d("onOkClicked Bajas", "posicion:" + position);
									if(position > 0){
										TipoServicioInfo ts = type_services.get(position - 1);
										ts.setTypeDay(Cuadrante.TYPE_DAY_ORDINARY);
										db.updateTipoServicio(ts);		
										db.updateServicesFromTypeTypeDay(ts.getId(), Cuadrante.TYPE_DAY_ORDINARY);
										Sp.setTypeServiceMedicalLeave(mContext, ts.getId());
									}
									/**
									 * Semana Santa
									 */
									final DialogSpinner ds2 = new DialogSpinner(
											mContext, 
											items, 
											R.string.title_dialog_permission_semana_santa, 
											R.string.message_dialog_select_type_service_for_drop_permission_semana_santa) {
										@Override
										public boolean onOkClicked(int position) {
											// do something
											//MyLog.d("onOkClicked Semana santa", "posicion:" + position);
											if(position > 0){
												TipoServicioInfo ts = type_services.get(position - 1);
												ts.setTypeDay(Cuadrante.TYPE_DAY_ESPECIAL);
												db.updateTipoServicio(ts);	
												db.updateServicesFromTypeTypeDay(ts.getId(), Cuadrante.TYPE_DAY_ESPECIAL);
											}

											/**
											 * Navidad
											 */
											final DialogSpinner ds3 = new DialogSpinner(
													mContext, 
													items, 
													R.string.title_dialog_permission_christmas, 
													R.string.message_dialog_select_type_service_for_drop_permission_christmas) {
												@Override
												public boolean onOkClicked(int position) {
													// do something
													//MyLog.d("onOkClicked Semana santa", "posicion:" + position);
													if(position > 0){
														TipoServicioInfo ts = type_services.get(position - 1);
														ts.setTypeDay(Cuadrante.TYPE_DAY_ESPECIAL);
														db.updateTipoServicio(ts);
														db.updateServicesFromTypeTypeDay(ts.getId(), Cuadrante.TYPE_DAY_ESPECIAL);
													}

													/**
													 * Permiso urgente
													 */
													final DialogSpinner ds4 = new DialogSpinner(
															mContext, 
															items, 
															R.string.title_dialog_permission_urgent, 
															R.string.message_dialog_select_type_service_for_drop_permission_urgent) {
														@Override
														public boolean onOkClicked(int position) {
															if(position > 0){
																TipoServicioInfo ts = type_services.get(position - 1);
																ts.setTypeDay(Cuadrante.TYPE_DAY_ESPECIAL);
																db.updateTipoServicio(ts);
																db.updateServicesFromTypeTypeDay(ts.getId(), Cuadrante.TYPE_DAY_ESPECIAL);
															}

															/**
															 * Permiso incorporación
															 */
															final DialogSpinner ds5 = new DialogSpinner(
																	mContext, 
																	items, 
																	R.string.title_dialog_permission_incorporation, 
																	R.string.message_dialog_select_type_service_for_drop_permission_incorporation) {
																@Override
																public boolean onOkClicked(int position) {
																	if(position > 0){
																		TipoServicioInfo ts = type_services.get(position - 1);
																		ts.setTypeDay(Cuadrante.TYPE_DAY_ORDINARY);
																		db.updateTipoServicio(ts);
																		db.updateServicesFromTypeTypeDay(ts.getId(), Cuadrante.TYPE_DAY_ORDINARY);
																	}
																	Toast.makeText(mContext, 
																			R.string.backup_db_import_successfully,
																			Toast.LENGTH_LONG).show();
																	return true;
																}
															};
															ds5.show();													 							 
															return true;
														}
													};
													ds4.show();														 
													return true;
												}
											};
											ds3.show();											 
											return true;
										}
									};
									ds2.show();										 

									return true; // true = close dialog
								}
							};
							ds.show();		 
						}
					}
				});
				new Thread(new Runnable(){
					public void run(){
						//Decompress d = new Decompress(filePath, Environment.getExternalStorageDirectory().getPath() + "/");
						//d.unzip();
						DatabaseAssistant dba = 
								new DatabaseAssistant(getApplicationContext(), 
										db.getReadableDatabase());
						importedFileDbName = dba.importData(db, filePath);	

						boolean success = (new File(filePath)).delete();
						if (success) {
							//Toast.makeText(context, "SE HA BORRADO", Toast.LENGTH_LONG).show();
						}					 
						pd.cancel();
						//finish();
						//startActivity(getIntent());	 

					}
				}).start();		 
			} else if (resultCode == Activity.RESULT_CANCELED) {
				MyLog.d("onActivityResult", "cancelado por el usuario");
			}			
			break;

		case Cuadrante.REQUEST_CODE_EMAIL:
			//eliminamos el backup por seguridad para que no quede en la tarjeta
			//y pueda ser sacada la información por alguien si roba el movil
			AlarmDeleteFileReceiver alarm = new AlarmDeleteFileReceiver(
					this.getApplicationContext(), 
					System.currentTimeMillis() + 1000 * 60, //1 minuto
					DatabaseAssistant.EXPORT_FILE_NAME);

			break;
		case Cuadrante.REQUEST_CODE_LOGIN_SAVE:
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(this, R.string.saved_preferences_ok, Toast.LENGTH_LONG).show();
			}else if(resultCode == Cuadrante.RESULT_ERROR){
				Toast.makeText(this, R.string.saved_preferences_error, Toast.LENGTH_LONG).show();				 
			}
			break;
		}

	}    

	public void setPreference(){
		//indicamos los dias de vacaciones gastados
		//DateTime date = new DateTime();
		String dateStart, dateEnd;
		//si el mes actual es enero entonces tenemos que poner el intervalo
		//del año pasado al actual
		if(mToday.getMonthOfYear() == 1){
			dateStart = CuadranteDates.formatDate(mToday.minusYears(1).getYear(), 02, 01);
			dateEnd = CuadranteDates.formatDate(mToday.getYear(), 01, 31);
		}else{
			dateStart = CuadranteDates.formatDate(mToday.getYear(), 02, 01);
			dateEnd = CuadranteDates.formatDate(mToday.plusYears(1).getYear(), 01, 31);        	
		}
		List<ServicioInfo> services = db.getServicesFromTypeService(
				Sp.getHolidaysTypeService(this), 
				dateStart,
				dateEnd, true, false);
		int numServicesSpent = Cuadrante.getDaysHolidaysSpent(services);

		Preference prefHolidays = (Preference) findPreference("num_days_holidays");
		//para que no de error cuando se entra por primera vez a esta pantalla
		//después de la instalación
		if(prefHolidays != null){
			int daysHolidays = Sp.getHolidaysDays(this);
			prefHolidays.setSummary(
					String.format(this.getString(R.string.holidays_resume)
							, daysHolidays
							, numServicesSpent 
							, daysHolidays - numServicesSpent));   
		}
		services = db.getServicesFromTypeService(
				Sp.getOwnAffairsTypeService(this), 
				dateStart,
				dateEnd);
		Preference prefOwnAffairs = (Preference) findPreference("num_own_affairs");
		if(prefOwnAffairs != null){
			int daysOwnAffairs = Sp.getOwnAffairsDays(this);
			prefOwnAffairs.setSummary(
					String.format(this.getString(R.string.own_affairs_resume)
							, daysOwnAffairs
							, services.size()
							, daysOwnAffairs - services.size()));
		}        
	}

	public void generateBackup(){
		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if(isSDPresent){
			//creamos un dialogo de espera mientras se ejecuta el backup
			final ProgressDialog pd = ProgressDialog.show(this,
					getResources().getString(R.string.dialog_title_backup_make),
					getResources().getString(R.string.please_wait),
					true, false);
			new Thread(new Runnable(){
				public void run(){
					DatabaseAssistant dba = new DatabaseAssistant(
							getApplicationContext(), db.getReadableDatabase());
					dba.exportData();
					db.close();
					/**TODO 22-10-2012 
					 * No se puede comprimir el xml xk al descomprimirlo para
					 * importarlo da error, y creo que es porque no lo reconoce
					 * como fichero xml, está mal formateado o algo.
					 * Si se envia por email tal cual el xml, al descargarlo sí
					 * lo reconoce perfectamente y lo importa.
					 */
					//comprimimos el xml xk he probado que en 5 años, el xml puede
					//llegar a pesar 1 mega y medio, si la aplicación se usa
					//durante muchos años podrían no poder enviar por email
					//tantos megas ya que ahora gmail no permite enviar más de 3mb
					//Compress compress = new Compress(new String[] {DatabaseAssistant.EXPORT_FILE_NAME}, ZIPFILE_BACKUP_DB);
					//compress.zip();
					//se elimina el xml por seguridad
					/*
			    		boolean success = (new File(DatabaseAssistant.EXPORT_FILE_NAME)).delete();
			    		if (!success) {
			    		    // Deletion failed
			    		} 
					 */   	 

					pd.dismiss();//quita el dialogo

					//creamos la pantalla de envio de email
					Intent email = new Intent(Intent.ACTION_SEND);
					//email.putExtra(Intent.EXTRA_EMAIL, new String[]{UserEmailFetcher.getEmail(getApplicationContext())});		  
					email.putExtra(Intent.EXTRA_EMAIL, new String[]{Sp.getEmail(mContext)});
					email.putExtra(Intent.EXTRA_SUBJECT, 
							getResources().getString(R.string.backup_db_email_subject));
					email.putExtra(Intent.EXTRA_TEXT, 
							getResources().getString(R.string.backup_db_email_message, CuadranteDates.formatDateToHumans(mToday)));
					//email.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ ZIPFILE_BACKUP_DB));
					email.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ DatabaseAssistant.EXPORT_FILE_NAME));
					email.setType("message/rfc822");
					startActivityForResult(
							Intent.createChooser(
									email, 
									getResources().getString(R.string.choose_email_client)), 
									Cuadrante.REQUEST_CODE_EMAIL);
				}
			}).start();    	
		}else{
			Toast.makeText(this, getString(R.string.error_no_sd_card), Toast.LENGTH_LONG).show();
		}
	}
}