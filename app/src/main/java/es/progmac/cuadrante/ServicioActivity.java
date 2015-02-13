package es.progmac.cuadrante;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.commonsware.cwac.colormixer.ColorMixer;
import com.commonsware.cwac.colormixer.ColorMixerDialogCopy;

import es.progmac.cuadrante.R;
import es.progmac.cuadrante.info.ComisionInfo;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.cuadrante.info.TipoServicioInfo;
import es.progmac.cuadrante.lib.ColorPickerWithCopyDialog;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.IntvlMinTimePickerDialog;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.PromptDialog;
import es.progmac.cuadrante.lib.Sp;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Pantalla de añadir/editar servicio diario
 * 
 * @author david4
 * 
 */
public class ServicioActivity extends SherlockListActivity implements
		//ColorPickerWithCopyDialog.OnColorChangedListener, 
		OnItemSelectedListener {
	private String TAG = "ServicioActivity";
	private String[][] spin_items;
	public int TYPE_ID = 0;
	public String ACTIVITY;
	public String ACTION;
	public String DATE;
	public String SCHEDULE_START;
	public String SCHEDULE_END;
	public long _ID = 0;
	private Boolean INSIDE_ONCREATE = true;

	private String DATE_SELECTED = "";// formato YYYY-mm-dd

	/**
	 * Posición en el array de items
	 * 
	 * @see #list_items
	 */
	static final int DIALOG_ID_NAME = 0;
	static final int DIALOG_ID_COMMENTS = 1;
	static final int DIALOG_ID_IS_HOLIDAY = 2;
	static final int DIALOG_ID_IS_IMPORTANT = 3;
	static final int DIALOG_ID_BG_COLOR = 4;
	static final int DIALOG_ID_TEXT_COLOR = 5;
	static final int DIALOG_ID_TYPE_DAY = 6;
	static final int DIALOG_ID_GUARDIA_COMBINADA = 7;
	static final int DIALOG_ID_SUCCESSION_COMMAND = 8;
	static final int DIALOG_ID_HORARIO_INICIO = 9;
	static final int DIALOG_ID_HORARIO_FIN = 10;
	static final int DIALOG_ID_HORARIO_INICIO2 = 11;
	static final int DIALOG_ID_HORARIO_FIN2 = 12;
	static final int DIALOG_ID_HORARIO_INICIO3 = 13;
	static final int DIALOG_ID_HORARIO_FIN3 = 14;
	static final int DIALOG_ID_HORARIO_INICIO4 = 15;
	static final int DIALOG_ID_HORARIO_FIN4 = 16;

	/**
	 * Variable que guardará qué TimePicker, si horario inicio o fin, debe de
	 * mostrar
	 */
	static int DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_INICIO;
	/**
	 * Item de la lista es normal es decir, dos lineas de texto
	 */
	static final String LIST_ITEM_TYPE_NORMAL = "normal";
	/**
	 * Item de la lista con un cuadrado a la derecha con un color
	 */
	static final String LIST_ITEM_TYPE_SQUARE_COLOR = "square_color";
	/**
	 * Item de la lista con un cuadrado a la derecha con un color desactivado
	 */
	static final String LIST_ITEM_TYPE_SQUARE_COLOR_DISABLED = "square_color_disabled";
	/**
	 * Item normal que estará desactivado por defecto
	 */
	static final String LIST_ITEM_TYPE_NORMAL_DISABLED = "normal_disabled";
	/**
	 * Item normal pero con un checkbox a la derecha
	 */
	static final String LIST_ITEM_TYPE_CHECKBOX = "checkbox";
	/**
	 * Item list
	 */
	static final String LIST_ITEM_TYPE_LIST = "list";
	/**
	 * Variable que guardara el color de fondo del tipo de servicio si se
	 * activa el checkbox del dia festivo, para que cuando se deseleccione
	 * el festivo se pueda volver a cargar el color
	 */
	String TYPE_SERVICE_DEFAULT_BG_COLOR = "";
	/**
	 * Variable que guardara el color de texto del tipo de servicio si se
	 * activa el checkbox del dia festivo, para que cuando se deseleccione
	 * el festivo se pueda volver a cargar el color
	 */	
	String TYPE_SERVICE_DEFAULT_TEXT_COLOR = "";
	/**
	 * Array bidimensional con la lista de items a mostrar
	 */
	private String[][] list_items = {
			// [0] , [1] , [2]
			{ LIST_ITEM_TYPE_NORMAL, "Nombre del Servicio", "" },
			{ LIST_ITEM_TYPE_NORMAL, "Comentarios", "" },
			{ LIST_ITEM_TYPE_CHECKBOX, "Día festivo", "", "false" },// [3]
			{ LIST_ITEM_TYPE_CHECKBOX, "Día importante", "", "false" },// [3]
			{ LIST_ITEM_TYPE_SQUARE_COLOR, "Color de fondo",
					String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR) },
			{ LIST_ITEM_TYPE_SQUARE_COLOR, "Color del texto",
					String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR) },
			{ LIST_ITEM_TYPE_LIST, "Tipo de día", "0" },
			{ LIST_ITEM_TYPE_LIST, "Guardia combinada", "0" },
			{ LIST_ITEM_TYPE_LIST, "Sucesión de mando", "0" },
			{ LIST_ITEM_TYPE_NORMAL_DISABLED, "Horario Inicio", Cuadrante.SCHEDULE_NULL },
			{ LIST_ITEM_TYPE_NORMAL_DISABLED, "Horario Fin", Cuadrante.SCHEDULE_NULL },
			{ LIST_ITEM_TYPE_NORMAL_DISABLED, "Horario Inicio 2", Cuadrante.SCHEDULE_NULL },
			{ LIST_ITEM_TYPE_NORMAL_DISABLED, "Horario Fin 2", Cuadrante.SCHEDULE_NULL },
			{ LIST_ITEM_TYPE_NORMAL_DISABLED, "Horario Inicio 3", Cuadrante.SCHEDULE_NULL },
			{ LIST_ITEM_TYPE_NORMAL_DISABLED, "Horario Fin 3", Cuadrante.SCHEDULE_NULL },
			{ LIST_ITEM_TYPE_NORMAL_DISABLED, "Horario Inicio 4", Cuadrante.SCHEDULE_NULL },
			{ LIST_ITEM_TYPE_NORMAL_DISABLED, "Horario Fin 4", Cuadrante.SCHEDULE_NULL }, };

	private int year, month, day;
	// ArrayList holds the data (as HashMaps) to load into the ListView
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	// SimpleAdapter does the work to load the data in to the ListView
	private ListAdapter sa;
	
	public DatabaseHandler db;
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servicio);
		
		db = new DatabaseHandler(this);
		mContext = this;
		
		list_items[DIALOG_ID_BG_COLOR][2] = Integer.toString(Sp.getServiceDefaultBgColor(mContext));
		list_items[DIALOG_ID_TEXT_COLOR][2] = Integer.toString(Sp.getServiceDefaultTextColor(mContext));
		// Get the message from the intent
		Intent intent = getIntent();

		// obtenemos la fecha seleccionada en formato YYYY-mm-dd
		DATE_SELECTED = intent.getStringExtra(Extra.DATE);
		String title = CuadranteDates.formatDateToHumans4(DATE_SELECTED);
		List<ComisionInfo> comisions = db.getComisionFromInterval(DATE_SELECTED, DATE_SELECTED);
		if(comisions.size() > 0){
			title = title.concat(" | ").concat(comisions.get(0).getName());
    	}
		setTitle(title);
		// revisamos si estamos editando una fecha, cargamos la lista con los
		// datos	
		ServicioInfo servicio = db.getServicio(DATE_SELECTED);
		
		// revisamos si estamos llamando a guardar un servicio directamente
		// desde el desplegable de tipos de servicios al hacer click en un
		// item del calendario
		ACTIVITY = intent.getStringExtra("ACTIVITY");
		//MyLog.d("onCreate", DATE_SELECTED + " ACTIVITY:" + ACTIVITY);
		if (ACTIVITY != "") {
			ACTION = intent.getStringExtra("ACTION");
			TYPE_ID = intent.getIntExtra("TYPE_ID", 0);
			if (TYPE_ID > 0 && ACTION.equals("selectTipo")) {
				TipoServicioInfo tipo_servicio = db.getTipoServicio(TYPE_ID);
				list_items[DIALOG_ID_NAME][2] = tipo_servicio.getName();
				
				if(servicio.getId() > 0 && servicio.getComments().length() > 0){
					list_items[DIALOG_ID_COMMENTS][2] = servicio.getComments();
				}
				//el dia tiene servicio guardado con festivo incluido
				if(servicio.getId() > 0 && servicio.getIsHoliday() == 1){
					list_items[DIALOG_ID_IS_HOLIDAY][3] = "true";
					list_items[DIALOG_ID_BG_COLOR][2] = 
							Integer.toString(Sp.getHolidayBgColor(mContext));
							//String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY);
					list_items[DIALOG_ID_TEXT_COLOR][2] = 
							Integer.toString(Sp.getHolidayTextColor(mContext));
							//String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY);
				}else{
					list_items[DIALOG_ID_BG_COLOR][2] = tipo_servicio.getBgColor();
					list_items[DIALOG_ID_TEXT_COLOR][2] = tipo_servicio
							.getTextColor();				
				}

				list_items[DIALOG_ID_GUARDIA_COMBINADA][2] = String.valueOf(tipo_servicio.getGuardiaCombinada());
				list_items[DIALOG_ID_SUCCESSION_COMMAND][2] = String.valueOf(tipo_servicio.getSuccessionCommand());
				list_items[DIALOG_ID_TYPE_DAY][2] = String.valueOf(tipo_servicio.getTypeDay());
				
				Bundle extras = intent.getExtras();
				if(extras.containsKey("SCHEDULE_START") && extras.containsKey("SCHEDULE_END")){
					list_items[DIALOG_ID_HORARIO_INICIO][2] = intent.getStringExtra("SCHEDULE_START");
					list_items[DIALOG_ID_HORARIO_FIN][2] = intent.getStringExtra("SCHEDULE_END");					
				}else{
					list_items[DIALOG_ID_HORARIO_INICIO][2] = tipo_servicio.getStartSchedule();
					list_items[DIALOG_ID_HORARIO_FIN][2] = tipo_servicio.getEndSchedule();					
				}
				
				list_items[DIALOG_ID_HORARIO_INICIO2][2] = tipo_servicio.getStartSchedule2();
				list_items[DIALOG_ID_HORARIO_FIN2][2] = tipo_servicio.getEndSchedule2();
				list_items[DIALOG_ID_HORARIO_INICIO3][2] = tipo_servicio.getStartSchedule3();
				list_items[DIALOG_ID_HORARIO_FIN3][2] = tipo_servicio.getEndSchedule3();
				list_items[DIALOG_ID_HORARIO_INICIO4][2] = tipo_servicio.getStartSchedule4();
				list_items[DIALOG_ID_HORARIO_FIN4][2] = tipo_servicio.getEndSchedule4();

				onSetData(getWindow().getDecorView().getRootView());
			}
		}
		// si es igual a 0 es que no hay servicio puesto ese dia
		// cargamos el spinner con los tipos de servicios
		List<TipoServicioInfo> tipos_servicios = db.getAllTipoServicios();
		List<String> list = new ArrayList<String>();
		spin_items = new String[tipos_servicios.size() + 1][2];
		list.add(getString(R.string.no_type_service));
		spin_items[0][0] = getString(R.string.no_type_service);
		spin_items[0][1] = String.valueOf(0);
		int i = 1;
		for (TipoServicioInfo tipo_servicio : tipos_servicios) {
			list.add(tipo_servicio.getTitle());
			spin_items[i][0] = tipo_servicio.getTitle();
			spin_items[i][1] = String.valueOf(tipo_servicio.getId());
			i++;
		}
		Spinner spin = (Spinner) findViewById(R.id.spinner_type_services);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		//para que se vean más grandes cada item
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(dataAdapter);
		spin.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		if (servicio.getId() > 0) {
			/*
			Button btn_delete = (Button) getWindow().getDecorView()
					.getRootView().findViewById(R.id.delete);
			btn_delete.setEnabled(true);
			*/
			_ID = servicio.getId();
			//MyLog.d("onCreate", "_ID" + _ID);
			for (i = 0; i < spin_items.length; i++) {
				if (spin_items[i][1]
						.equals(String.valueOf(servicio.getTypeId()))) {
					// MyLog.d("onCreate", "dentro2");
					spin.setSelection(i);
					break;
				} else {
					spin.setSelection(0);
				}
			}
			list_items[DIALOG_ID_NAME][2] = servicio.getName();
			list_items[DIALOG_ID_COMMENTS][2] = servicio.getComments();

			String is_holiday;
			if (servicio.getIsHoliday() == 1){
				is_holiday = "true";
				list_items[DIALOG_ID_BG_COLOR][0] = LIST_ITEM_TYPE_SQUARE_COLOR_DISABLED;
				list_items[DIALOG_ID_TEXT_COLOR][0] = LIST_ITEM_TYPE_SQUARE_COLOR_DISABLED;
			}else{
				is_holiday = "false";
				list_items[DIALOG_ID_BG_COLOR][0] = LIST_ITEM_TYPE_SQUARE_COLOR;
				list_items[DIALOG_ID_TEXT_COLOR][0] = LIST_ITEM_TYPE_SQUARE_COLOR;
			}
			
			String is_important = "false";
			if (servicio.getIsImportant() == 1){
				is_important = "true";
			}

			list_items[DIALOG_ID_GUARDIA_COMBINADA][2] = String.valueOf(servicio.getGuardiaCombinada());
			list_items[DIALOG_ID_SUCCESSION_COMMAND][2] = String.valueOf(servicio.getSuccessionCommand());
			list_items[DIALOG_ID_TYPE_DAY][2] = String.valueOf(servicio.getTypeDay());
			list_items[DIALOG_ID_IS_HOLIDAY][3] = String.valueOf(is_holiday);
			list_items[DIALOG_ID_IS_IMPORTANT][3] = String.valueOf(is_important);
			list_items[DIALOG_ID_BG_COLOR][2] = servicio.getBgColor();
			list_items[DIALOG_ID_TEXT_COLOR][2] = servicio.getTextColor();
			
			list_items[DIALOG_ID_HORARIO_INICIO][2] = servicio.getStartSchedule();
			list_items[DIALOG_ID_HORARIO_FIN][2] = servicio.getEndSchedule();
			list_items[DIALOG_ID_HORARIO_INICIO2][2] = servicio.getStartSchedule2();
			list_items[DIALOG_ID_HORARIO_FIN2][2] = servicio.getEndSchedule2();
			list_items[DIALOG_ID_HORARIO_INICIO3][2] = servicio.getStartSchedule3();
			list_items[DIALOG_ID_HORARIO_FIN3][2] = servicio.getEndSchedule3();
			list_items[DIALOG_ID_HORARIO_INICIO4][2] = servicio.getStartSchedule4();
			list_items[DIALOG_ID_HORARIO_FIN4][2] = servicio.getEndSchedule4();

			// activamos el horario si el horario es diferente a 00:00
			if (!list_items[DIALOG_ID_HORARIO_INICIO][2].equals(Cuadrante.SCHEDULE_NULL)
					|| !list_items[DIALOG_ID_HORARIO_FIN][2].equals(Cuadrante.SCHEDULE_NULL)) {
				list_items[DIALOG_ID_HORARIO_INICIO][0] = LIST_ITEM_TYPE_NORMAL;
				list_items[DIALOG_ID_HORARIO_FIN][0] = LIST_ITEM_TYPE_NORMAL;	
			}
			// activamos el horario si el horario es diferente a 00:00
			if (!list_items[DIALOG_ID_HORARIO_INICIO2][2].equals(Cuadrante.SCHEDULE_NULL)
					|| !list_items[DIALOG_ID_HORARIO_FIN2][2].equals(Cuadrante.SCHEDULE_NULL)) {
				list_items[DIALOG_ID_HORARIO_INICIO2][0] = LIST_ITEM_TYPE_NORMAL;
				list_items[DIALOG_ID_HORARIO_FIN2][0] = LIST_ITEM_TYPE_NORMAL;	
			}
			// activamos el horario
			if (!list_items[DIALOG_ID_HORARIO_INICIO3][2].equals(Cuadrante.SCHEDULE_NULL)
					|| !list_items[DIALOG_ID_HORARIO_FIN3][2].equals(Cuadrante.SCHEDULE_NULL)) {
				list_items[DIALOG_ID_HORARIO_INICIO3][0] = LIST_ITEM_TYPE_NORMAL;
				list_items[DIALOG_ID_HORARIO_FIN3][0] = LIST_ITEM_TYPE_NORMAL;
			}
			// activamos el horario
			if (!list_items[DIALOG_ID_HORARIO_INICIO4][2].equals(Cuadrante.SCHEDULE_NULL)
					|| !list_items[DIALOG_ID_HORARIO_FIN4][2].equals(Cuadrante.SCHEDULE_NULL)) {
				list_items[DIALOG_ID_HORARIO_INICIO4][0] = LIST_ITEM_TYPE_NORMAL;
				list_items[DIALOG_ID_HORARIO_FIN4][0] = LIST_ITEM_TYPE_NORMAL;
			}
		}

		listViewGenerateListAdapter();
		/*
		Button btnAddSchedule = (Button)findViewById(R.id.add_shedule);
		final Button btnDelSchedule = (Button)findViewById(R.id.delete_schedule);
		
		btnAddSchedule.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
            	btnDelSchedule.setEnabled(true);            
            }
        });		
        */
	}

	public void onSetData(View view) {

		if(Cuadrante.canServiceSaves(this, TYPE_ID, DATE_SELECTED, DATE_SELECTED)){			
			//si tiene seleccionado un tipo de dia, los horarios tienen ke estar a 0, porque se supone
			//que son vacas, baja, etc... es decir, que no hay trabajo
			//pero queremos que guarde las horas cuando seleccionas el tipo 3, que es -1 dia
			//natural del mes
			int type_day = Integer.valueOf(list_items[DIALOG_ID_TYPE_DAY][2]);
			if(type_day > 0 && type_day < 3){
				list_items[DIALOG_ID_HORARIO_INICIO][2] = Cuadrante.SCHEDULE_NULL;
				list_items[DIALOG_ID_HORARIO_FIN][2] = Cuadrante.SCHEDULE_NULL;
				list_items[DIALOG_ID_HORARIO_INICIO2][2] = Cuadrante.SCHEDULE_NULL;
				list_items[DIALOG_ID_HORARIO_FIN2][2] = Cuadrante.SCHEDULE_NULL;
				list_items[DIALOG_ID_HORARIO_INICIO3][2] = Cuadrante.SCHEDULE_NULL;
				list_items[DIALOG_ID_HORARIO_FIN3][2] = Cuadrante.SCHEDULE_NULL;
				list_items[DIALOG_ID_HORARIO_INICIO4][2] = Cuadrante.SCHEDULE_NULL;
				list_items[DIALOG_ID_HORARIO_FIN4][2] = Cuadrante.SCHEDULE_NULL;
			}
			
			if(Sp.getAskService24Hours(mContext) &&
					(
					(!list_items[DIALOG_ID_HORARIO_INICIO][2].equals(Cuadrante.SCHEDULE_NULL)
					&& !list_items[DIALOG_ID_HORARIO_FIN][2].equals(Cuadrante.SCHEDULE_NULL) 
					&& list_items[DIALOG_ID_HORARIO_INICIO][2].equals(list_items[DIALOG_ID_HORARIO_FIN][2]))
					||
					(!list_items[DIALOG_ID_HORARIO_INICIO2][2].equals(Cuadrante.SCHEDULE_NULL)
					&& !list_items[DIALOG_ID_HORARIO_FIN2][2].equals(Cuadrante.SCHEDULE_NULL) 
					&& list_items[DIALOG_ID_HORARIO_INICIO2][2].equals(list_items[DIALOG_ID_HORARIO_FIN2][2]))
					||
					(!list_items[DIALOG_ID_HORARIO_INICIO3][2].equals(Cuadrante.SCHEDULE_NULL)
					&& !list_items[DIALOG_ID_HORARIO_FIN3][2].equals(Cuadrante.SCHEDULE_NULL) 
					&& list_items[DIALOG_ID_HORARIO_INICIO3][2].equals(list_items[DIALOG_ID_HORARIO_FIN3][2]))
					||
					(!list_items[DIALOG_ID_HORARIO_INICIO4][2].equals(Cuadrante.SCHEDULE_NULL)
					&& !list_items[DIALOG_ID_HORARIO_FIN4][2].equals(Cuadrante.SCHEDULE_NULL) 
					&& list_items[DIALOG_ID_HORARIO_INICIO4][2].equals(list_items[DIALOG_ID_HORARIO_FIN4][2]))
					)){
				
			    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			    dialog.setTitle(R.string.title_dialog_service_24_hours);
			    dialog.setMessage(R.string.message_alert_service_24_hours);
			    dialog.setCancelable(false);
			    dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int id) {	
			    		saveData();
			    	}
			    });
			    dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	
			        }
			    });
			    dialog.create();
			    dialog.show();			
			}else{
				saveData();
			}


		}else{
			Toast.makeText(
					getApplicationContext(), 
					R.string.error_cant_save_service_exceeded_limit, 
					Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Graba los datos del servicio
	 */
	public void saveData(){
		int is_holiday = 0;
		if (list_items[DIALOG_ID_IS_HOLIDAY][3] == "true")
			is_holiday = 1;
		else
			is_holiday = 0;
		
		int is_important = 0;
		if (list_items[DIALOG_ID_IS_IMPORTANT][3] == "true")
			is_important = 1;
		
						
		ServicioInfo servicio = db.getServicio(DATE_SELECTED);
		
		if (servicio.getId() > 0) {
			db.updateServicio(new ServicioInfo(
					DATE_SELECTED, 
					TYPE_ID,
					list_items[DIALOG_ID_NAME][2], 
					list_items[DIALOG_ID_BG_COLOR][2],
					list_items[DIALOG_ID_TEXT_COLOR][2],
					list_items[DIALOG_ID_HORARIO_INICIO][2],
					list_items[DIALOG_ID_HORARIO_FIN][2],
					list_items[DIALOG_ID_HORARIO_INICIO2][2],
					list_items[DIALOG_ID_HORARIO_FIN2][2],
					list_items[DIALOG_ID_HORARIO_INICIO3][2],
					list_items[DIALOG_ID_HORARIO_FIN3][2],
					list_items[DIALOG_ID_HORARIO_INICIO4][2],
					list_items[DIALOG_ID_HORARIO_FIN4][2],
					list_items[DIALOG_ID_COMMENTS][2],
					is_holiday,
					Integer.valueOf(list_items[DIALOG_ID_GUARDIA_COMBINADA][2]),
					Integer.valueOf(list_items[DIALOG_ID_TYPE_DAY][2]),
					is_important,
					Integer.valueOf(list_items[DIALOG_ID_SUCCESSION_COMMAND][2])));
		} else {
			servicio = db.addService(new ServicioInfo(
					DATE_SELECTED, 
					TYPE_ID,
					list_items[DIALOG_ID_NAME][2], 
					list_items[DIALOG_ID_BG_COLOR][2],
					list_items[DIALOG_ID_TEXT_COLOR][2],
					list_items[DIALOG_ID_HORARIO_INICIO][2],
					list_items[DIALOG_ID_HORARIO_FIN][2],
					list_items[DIALOG_ID_HORARIO_INICIO2][2],
					list_items[DIALOG_ID_HORARIO_FIN2][2],
					list_items[DIALOG_ID_HORARIO_INICIO3][2],
					list_items[DIALOG_ID_HORARIO_FIN3][2],
					list_items[DIALOG_ID_HORARIO_INICIO4][2],
					list_items[DIALOG_ID_HORARIO_FIN4][2],
					list_items[DIALOG_ID_COMMENTS][2],
					is_holiday,
					Integer.valueOf(list_items[DIALOG_ID_GUARDIA_COMBINADA][2]),
					Integer.valueOf(list_items[DIALOG_ID_TYPE_DAY][2]),
					is_important,
					Integer.valueOf(list_items[DIALOG_ID_SUCCESSION_COMMAND][2])));
		}
		//borramos los datos de la tabla hours del mes
		Cuadrante.deleteHoursDate(mContext, DATE_SELECTED);
		/*
		CuadranteDates tmpDate = new CuadranteDates(DATE_SELECTED);
		db.deleteHours(tmpDate.getYear(), tmpDate.getMonth());
		if(tmpDate.getDay() >= 28){
			CuadranteDates tmpDate2 = new CuadranteDates(tmpDate.getDateTime().plusMonths(1));
			db.deleteHours(tmpDate2.getYear(), tmpDate2.getMonth());
		}
		*/
		//si el horario fin es inferior al horario inicio significa que ha
		//terminado de trabajar el dia siguiente
		/*
		int start_schedule_hour = CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_INICIO][2]);
		int start_schedule_minutes = CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_INICIO][2]);
		int end_schedule_hour = CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_FIN][2]);
		int end_schedule_minutes = CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_FIN][2]);
		int start_schedule_hour2 = CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_INICIO2][2]);
		int start_schedule_minutes2 = CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_INICIO2][2]);
		int end_schedule_hour2 = CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_FIN2][2]);
		int end_schedule_minutes2 = CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_FIN2][2]);
		int start_schedule_hour3 = CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_INICIO3][2]);
		int start_schedule_minutes3 = CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_INICIO3][2]);
		int end_schedule_hour3 = CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_FIN3][2]);
		int end_schedule_minutes3 = CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_FIN3][2]);
		int start_schedule_hour4 = CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_INICIO4][2]);
		int start_schedule_minutes4 = CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_INICIO4][2]);
		int end_schedule_hour4 = CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_FIN4][2]);
		int end_schedule_minutes4 = CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_FIN4][2]);
		
		//03:30 03:00
		//22:00 06:00
		//MyLog.d("onSetData", "start: " + start_schedule_hour + " end:" +  end_schedule_hour);
		if((start_schedule_hour > end_schedule_hour || 
				start_schedule_hour2 > end_schedule_hour2 || 
				start_schedule_hour3 > end_schedule_hour3 || 
				start_schedule_hour4 > end_schedule_hour4) ||
				((start_schedule_hour == end_schedule_hour 
					&& !list_items[DIALOG_ID_HORARIO_INICIO][2].equals(Cuadrante.SCHEDULE_NULL)
					&& !list_items[DIALOG_ID_HORARIO_FIN][2].equals(Cuadrante.SCHEDULE_NULL)) || 
				(start_schedule_hour2 == end_schedule_hour2
					&& !list_items[DIALOG_ID_HORARIO_INICIO2][2].equals(Cuadrante.SCHEDULE_NULL)
					&& !list_items[DIALOG_ID_HORARIO_FIN2][2].equals(Cuadrante.SCHEDULE_NULL)) || 
				(start_schedule_hour3 == end_schedule_hour3
					&& !list_items[DIALOG_ID_HORARIO_INICIO3][2].equals(Cuadrante.SCHEDULE_NULL)
					&& !list_items[DIALOG_ID_HORARIO_FIN3][2].equals(Cuadrante.SCHEDULE_NULL)) || 
				(start_schedule_hour4 == end_schedule_hour4
					&& !list_items[DIALOG_ID_HORARIO_INICIO4][2].equals(Cuadrante.SCHEDULE_NULL)
					&& !list_items[DIALOG_ID_HORARIO_FIN4][2].equals(Cuadrante.SCHEDULE_NULL)))){
		*/
		//miramos si existe ya un servicio al dia siguiente, si lo tiene
		//puede ser que tenga Noche-Noche. Si no tiene servicio al dia
		//siguiente pues creamos un servicio de Saliente con los colores
		//de este servicio
		CuadranteDates temp = new CuadranteDates(DATE_SELECTED);
		DateTime dtTomorrow = new DateTime(temp.getYear(), temp.getMonth(), 
				temp.getDay(), 0, 0);
		dtTomorrow = dtTomorrow.plusDays(1);
		String tomorrow = dtTomorrow.toString("yyyy-MM-dd");
		ServicioInfo serTomorrow = db.getServicio(tomorrow);
		
		if(Cuadrante.isServiceEndNextDay(
				list_items[DIALOG_ID_HORARIO_INICIO][2], list_items[DIALOG_ID_HORARIO_FIN][2],
				list_items[DIALOG_ID_HORARIO_INICIO2][2], list_items[DIALOG_ID_HORARIO_FIN2][2],
				list_items[DIALOG_ID_HORARIO_INICIO3][2], list_items[DIALOG_ID_HORARIO_FIN3][2],
				list_items[DIALOG_ID_HORARIO_INICIO4][2], list_items[DIALOG_ID_HORARIO_FIN4][2])){

			//el dia siguiente no tiene servicio, asi que creamos el servicio
			//de SALIENTE
			if(serTomorrow.getId() == 0){
				String bg_color = list_items[DIALOG_ID_BG_COLOR][2];
				String text_color = list_items[DIALOG_ID_TEXT_COLOR][2];
				//si el servicio que hemos grabado es festivo no vamos a guardar el saliente
				//con el color del festivo, asi que cogemos los colores del tipo
				if(TYPE_ID > 0 && servicio.getIsHoliday() == 1){
					TipoServicioInfo type_service = db.getTipoServicio(TYPE_ID);
					bg_color = type_service.getBgColor();
					text_color = type_service.getTextColor();
				}else if(TYPE_ID == 0){//si no, pues los colores por defecto
					bg_color = String.valueOf(Sp.getServiceDefaultBgColor(mContext));
					text_color = String.valueOf(Sp.getServiceDefaultTextColor(mContext));
				}
				db.addService(new ServicioInfo(tomorrow, 0, 
						Cuadrante.getLengthServiceNameSubstring(
								this, Cuadrante.CALENDAR_SERVICE_SALIENTE), 
						bg_color, 
						text_color, 
						Cuadrante.SCHEDULE_NULL, Cuadrante.SCHEDULE_NULL, 
						Cuadrante.SCHEDULE_NULL, Cuadrante.SCHEDULE_NULL, 
						Cuadrante.SCHEDULE_NULL, Cuadrante.SCHEDULE_NULL, 
						Cuadrante.SCHEDULE_NULL, Cuadrante.SCHEDULE_NULL, "", 0, 0, 0, 0, 0));
			//si el servicio del día siguiente no tiene nada en la abreviatura pues ponemos
			//saliente
			}else if(serTomorrow.getName().equals("")){
				serTomorrow.setName(
						Cuadrante.getLengthServiceNameSubstring(this, Cuadrante.CALENDAR_SERVICE_SALIENTE));
				db.updateServicio(serTomorrow);
			//actualizamos el saliente con los colores por si el servicio de ese dia lo hemos
			//cambiado
			}else if(serTomorrow.getName().equals(Cuadrante.CALENDAR_SERVICE_SALIENTE)){
				String bgColor = list_items[DIALOG_ID_BG_COLOR][2];
				String textColor = list_items[DIALOG_ID_TEXT_COLOR][2];
				//si el servicio que hemos grabado es festivo no vamos a guardar el saliente
				//con el color del festivo, asi que cogemos los colores del tipo
				if(TYPE_ID > 0 && servicio.getIsHoliday() == 1){
					TipoServicioInfo type_service = db.getTipoServicio(TYPE_ID);
					bgColor = type_service.getBgColor();
					textColor = type_service.getTextColor();
				}else if(TYPE_ID == 0){//si no, pues los colores por defecto
					bgColor = String.valueOf(Sp.getServiceDefaultBgColor(mContext));
					textColor = String.valueOf(Sp.getServiceDefaultTextColor(mContext));
				}else if(serTomorrow.getIsHoliday() == 1){//si el saliente es festivo guardamos los
					//colores por defecto para el festivo
					bgColor = String.valueOf(Sp.getHolidayBgColor(mContext));
					textColor = String.valueOf(Sp.getHolidayTextColor(mContext));
				}
				serTomorrow.setBgColor(bgColor);
				serTomorrow.setTextColor(textColor);
				db.updateServicio(serTomorrow);
			}
		//puede que se modifique el servicio de una noche a un servicio de un dia por lo que
		//el saliente del dia siguiente habrá que eliminarlo si existe
		}else if(serTomorrow.getName().equals(Cuadrante.CALENDAR_SERVICE_SALIENTE) &&
				serTomorrow.getTypeId() == 0){
			db.deleteServicio(serTomorrow.getId());
		}
		Cuadrante.refreshWidget(mContext);
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		this.finish();		
	}

	public void onSetCancel(View view) {
		this.finish();
	}

	public void onSetDelete(View view) {
		if (_ID > 0) {
			//alertamos si está seguro de querer eliminar el servicio
		    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    dialog.setMessage(getString(R.string.message_alert_delete_servicio));
		    dialog.setCancelable(false);
		    dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {	
		    		ServicioInfo service = db.getServicio(_ID);
		    		//verificamos si el servicio termina al dia siguiente y por consiguiente hay
		    		//otro servicio llamado saliente para tambien eliminarlo
		    		String date_next_service = 
		    				new CuadranteDates(service.getDateTime().plusDays(1)).getDate();		    		
		    		ServicioInfo service_next_day = db.getServicio(date_next_service);
		    		if(service_next_day.getId() > 0){
		    			if(service_next_day.getName().equals(Cuadrante.CALENDAR_SERVICE_SALIENTE)
		    					&& service_next_day.getTypeId() == 0){
				    		db.deleteServicio(service_next_day.getId());		    				
		    			}
		    		}
		    		
		    		db.deleteServicio(_ID);
					//borramos los datos de la tabla hours del mes
					CuadranteDates tmpDate = new CuadranteDates(service.getDateTime());
					db.deleteHours(tmpDate.getYear(), tmpDate.getMonth());
					if(tmpDate.getDay() >= 28){
						CuadranteDates tmpDate2 = new CuadranteDates(tmpDate.getDateTime().plusMonths(1));
						db.deleteHours(tmpDate2.getYear(), tmpDate2.getMonth());
					}		    		
		    		//datasource.close();
		    		Cuadrante.refreshWidget(mContext);
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					ServicioActivity.this.finish();		    		
		       }
		    });
		    dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	
		        }
		    });
		    dialog.create();
		    dialog.show();			
		}
	}

	/**
	 * Cuando se quiere añadir un nuevo horario
	 * 
	 * @param view
	 */
	public void onSetAddSchedule(View view) {
		int j = 1, i;
		for (i = 6; i < list_items.length && j <= 2; i++) {
			if (list_items[i][0] == LIST_ITEM_TYPE_NORMAL_DISABLED) {
				list_items[i][0] = LIST_ITEM_TYPE_NORMAL;
				list_items[i][2] = Cuadrante.SCHEDULE_DV;
				j++;
			}
		}
		//int text = 0;
		
		if(j > 2){
			//text = R.string.schedule_added;
			ListView lv = (ListView) findViewById(android.R.id.list);
			lv.setStackFromBottom(true);
		}else{
			Toast.makeText(this, R.string.schedule_no_more_added, Toast.LENGTH_SHORT).show();
		}
		
		listViewGenerateListAdapter();
	}	
	
	public void onSetDeleteSchedule(View view) {
		int j = 1;

		for (int i = list_items.length - 1; i > 7  && j <= 2; i--) {
			if (list_items[i][0] == LIST_ITEM_TYPE_NORMAL) {
				list_items[i][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
				list_items[i][2] = Cuadrante.SCHEDULE_NULL;
				j++;
			}
		}
		
		int text = 0;
		if(j > 2){
			text = R.string.schedule_deleted;
			ListView lv = (ListView) findViewById(android.R.id.list);
			lv.setStackFromBottom(false);			
		}else
			text = R.string.schedule_no_more_deleted;
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		
		listViewGenerateListAdapter();
	}	
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_servicio, menu);

		return true;
	}    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		View v = getWindow().getDecorView().getRootView();
		switch (item.getItemId()) {
		case R.id.bar_ready:
			onSetData(v);
			return true;
			/*
		case R.id.bar_cancel:
			onSetCancel(v);
			return true;
			*/
		case R.id.bar_add_schedule:
			onSetAddSchedule(v);
			return true;
		case R.id.bar_delete_schedule:
			onSetDeleteSchedule(v);
			return true;			
		case R.id.bar_delete:
			onSetDelete(v);
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		/*
		 * Toast.makeText(this, "id: " + id + " position: " + position,
		 * Toast.LENGTH_SHORT).show();
		 */
		switch (position) {
		case DIALOG_ID_NAME:
			showDialog(DIALOG_ID_NAME);
			break;
		case DIALOG_ID_COMMENTS:
			showDialog(DIALOG_ID_COMMENTS);
			break;
		case DIALOG_ID_HORARIO_INICIO:
			DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_INICIO;
			showDialog(DIALOG_ID_HORARIO_INICIO);
			break;
		case DIALOG_ID_HORARIO_FIN:
			DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_FIN;
			showDialog(DIALOG_ID_HORARIO_FIN);
			break;
		case DIALOG_ID_HORARIO_INICIO2:
			DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_INICIO2;
			showDialog(DIALOG_ID_HORARIO_INICIO2);
			break;
		case DIALOG_ID_HORARIO_FIN2:
			DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_FIN2;
			showDialog(DIALOG_ID_HORARIO_FIN2);
			break;
		case DIALOG_ID_HORARIO_INICIO3:
			DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_INICIO3;
			showDialog(DIALOG_ID_HORARIO_INICIO3);
			break;
		case DIALOG_ID_HORARIO_FIN3:
			DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_FIN3;
			showDialog(DIALOG_ID_HORARIO_FIN3);
			break;
		case DIALOG_ID_HORARIO_INICIO4:
			DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_INICIO4;
			showDialog(DIALOG_ID_HORARIO_INICIO4);
			break;
		case DIALOG_ID_HORARIO_FIN4:
			DIALOG_ID_HORARIO_WHICH = DIALOG_ID_HORARIO_FIN4;
			showDialog(DIALOG_ID_HORARIO_FIN4);
			break;
		case DIALOG_ID_BG_COLOR:
			/*
			new ColorPickerWithCopyDialog(this, this, "bg",
					Integer.parseInt(list_items[DIALOG_ID_BG_COLOR][2]),
					Cuadrante.SERVICE_DEFAULT_BG_COLOR).show();
					*/
			new ColorMixerDialogCopy(this, Sp.getServiceDefaultBgColor(mContext), 
					Integer.parseInt(list_items[DIALOG_ID_BG_COLOR][2]), 
					onSetColorBg).show();
			
			break;
		case DIALOG_ID_TEXT_COLOR:
			/*
			new ColorPickerWithCopyDialog(this, this, "text",
					Integer.parseInt(list_items[DIALOG_ID_TEXT_COLOR][2]),
					Cuadrante.SERVICE_DEFAULT_TEXT_COLOR).show();
					*/
			new ColorMixerDialogCopy(this, Sp.getServiceDefaultTextColor(mContext), 
					Integer.parseInt(list_items[DIALOG_ID_TEXT_COLOR][2]), 
					onSetColorText).show();
			
			break;
		case DIALOG_ID_IS_HOLIDAY:
			if (list_items[DIALOG_ID_IS_HOLIDAY][3] == "false"){
				list_items[DIALOG_ID_IS_HOLIDAY][3] = "true";
				//list_items[DIALOG_ID_BG_COLOR][2] = String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY);
				//list_items[DIALOG_ID_TEXT_COLOR][2] = String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY);
				list_items[DIALOG_ID_BG_COLOR][2] = 
						Integer.toString(Sp.getHolidayBgColor(mContext));
						//String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY);
				list_items[DIALOG_ID_TEXT_COLOR][2] = 
						Integer.toString(Sp.getHolidayTextColor(mContext));
						//String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY);				
				list_items[DIALOG_ID_BG_COLOR][0] = LIST_ITEM_TYPE_SQUARE_COLOR_DISABLED;
				list_items[DIALOG_ID_TEXT_COLOR][0] = LIST_ITEM_TYPE_SQUARE_COLOR_DISABLED;
			}else{
				list_items[DIALOG_ID_IS_HOLIDAY][3] = "false";
				if(TYPE_ID > 0){
					TipoServicioInfo typeService = db.getTipoServicio(TYPE_ID);
					list_items[DIALOG_ID_BG_COLOR][2] = typeService.getBgColor();
					list_items[DIALOG_ID_TEXT_COLOR][2] = typeService.getTextColor();					
				}else{
					list_items[DIALOG_ID_BG_COLOR][2] = 
							String.valueOf(Sp.getServiceDefaultBgColor(mContext));
					list_items[DIALOG_ID_TEXT_COLOR][2] = 
							String.valueOf(Sp.getServiceDefaultTextColor(mContext));
				}
				list_items[DIALOG_ID_BG_COLOR][0] = LIST_ITEM_TYPE_SQUARE_COLOR;
				list_items[DIALOG_ID_TEXT_COLOR][0] = LIST_ITEM_TYPE_SQUARE_COLOR;
			}					
			
			listViewGenerateListAdapter();
			break;
		case DIALOG_ID_IS_IMPORTANT:
			if (list_items[DIALOG_ID_IS_IMPORTANT][3] == "false"){
				list_items[DIALOG_ID_IS_IMPORTANT][3] = "true";
			}else{
				list_items[DIALOG_ID_IS_IMPORTANT][3] = "false";
			}					
			
			listViewGenerateListAdapter();
			break;
		}

	}

	/**
	 * Generación de miniventanas (dialogos) dependiendo del id que se le pasa
	 * 
	 * @param int id número que representa un tipo de diálogo a mostrar
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ID_NAME:
			MyLog.d("onCreateDialog", "length:" + Sp.getLengthServiceName(this));
			PromptDialog dlg = new PromptDialog(this, R.string.abbreviation, 0,
					list_items[DIALOG_ID_NAME][2], Sp.getLengthServiceName(this), 
					String.format(
							this.getString(R.string.hint_max_characters), 
							Sp.getLengthServiceName(this)), 0) {
				@Override
				public boolean onOkClicked(String input) {
					// do something
					list_items[DIALOG_ID_NAME][2] = input;
					listViewGenerateListAdapter();
					return true; // true = close dialog
				}
			};
			dlg.show();
			break;
		case DIALOG_ID_COMMENTS:
			Resources res = getResources();
			PromptDialog dlg2 = new PromptDialog(this,
					R.string.title_comments, 0, list_items[DIALOG_ID_COMMENTS][2], 
					res.getInteger(R.integer.MAX_LENGTH_INPUT_COMMENTS), 
					String.format(this.getString(R.string.hint_max_characters),
							res.getInteger(R.integer.MAX_LENGTH_INPUT_COMMENTS)),
					5) {
				@Override
				public boolean onOkClicked(String input) {
					// do something
					list_items[DIALOG_ID_COMMENTS][2] = input;
					listViewGenerateListAdapter();
					return true; // true = close dialog
				}
			};
			dlg2.show();
			break;			
		case DIALOG_ID_HORARIO_INICIO:
			return new IntvlMinTimePickerDialog(
					this, mTimeSetListener, 
					CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_INICIO][2]), 
					IntvlMinTimePickerDialog.getRoundedMinute(
							CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_INICIO][2]), this), true);
		case DIALOG_ID_HORARIO_FIN:
			return new IntvlMinTimePickerDialog(this, mTimeSetListener,
					CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_FIN][2]), 
					IntvlMinTimePickerDialog.getRoundedMinute(
							CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_FIN][2]), this), true);
		case DIALOG_ID_HORARIO_INICIO2:
			return new IntvlMinTimePickerDialog(this, mTimeSetListener,
					CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_INICIO2][2]), 
					IntvlMinTimePickerDialog.getRoundedMinute(
							CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_INICIO2][2]), this), true);
		case DIALOG_ID_HORARIO_FIN2:
			return new IntvlMinTimePickerDialog(this, mTimeSetListener,
					CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_FIN2][2]), 
					IntvlMinTimePickerDialog.getRoundedMinute(
							CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_FIN2][2]), this), true);
		case DIALOG_ID_HORARIO_INICIO3:
			return new IntvlMinTimePickerDialog(this, mTimeSetListener,
					CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_INICIO3][2]), 
					IntvlMinTimePickerDialog.getRoundedMinute(
							CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_INICIO3][2]), this), true);
		case DIALOG_ID_HORARIO_FIN3:
			return new IntvlMinTimePickerDialog(this, mTimeSetListener,
					CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_FIN3][2]), 
					IntvlMinTimePickerDialog.getRoundedMinute(
							CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_FIN3][2]), this), true);
		case DIALOG_ID_HORARIO_INICIO4:
			return new IntvlMinTimePickerDialog(this, mTimeSetListener,
					CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_INICIO4][2]), 
					IntvlMinTimePickerDialog.getRoundedMinute(
							CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_INICIO4][2]), this), true);
		case DIALOG_ID_HORARIO_FIN4:
			return new IntvlMinTimePickerDialog(this, mTimeSetListener,
					CuadranteDates.getHour(list_items[DIALOG_ID_HORARIO_FIN4][2]), 
					IntvlMinTimePickerDialog.getRoundedMinute(
							CuadranteDates.getMinutes(list_items[DIALOG_ID_HORARIO_FIN4][2]), this), true);
		}
		return null;
	}



	/**
	 * Función que se llama cuando se añade la hora inicio o fin
	 */
	private IntvlMinTimePickerDialog.OnTimeSetListener mTimeSetListener = 
			new IntvlMinTimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			/*
			 * Toast.makeText(getBaseContext(), "witch:" +
			 * DIALOG_ID_HORARIO_WHICH, Toast.LENGTH_SHORT).show();
			 */
			switch (DIALOG_ID_HORARIO_WHICH) {
			case DIALOG_ID_HORARIO_INICIO:
				list_items[DIALOG_ID_HORARIO_INICIO][2]  = CuadranteDates.formatTime(hourOfDay, minuteOfHour);
				break;
			case DIALOG_ID_HORARIO_FIN:
				list_items[DIALOG_ID_HORARIO_FIN][2] = CuadranteDates.formatTime(hourOfDay, minuteOfHour);
				break;
			case DIALOG_ID_HORARIO_INICIO2:
				list_items[DIALOG_ID_HORARIO_INICIO2][2] = CuadranteDates.formatTime(hourOfDay, minuteOfHour);
				break;
			case DIALOG_ID_HORARIO_FIN2:
				list_items[DIALOG_ID_HORARIO_FIN2][2] = CuadranteDates.formatTime(hourOfDay, minuteOfHour);
				break;
			case DIALOG_ID_HORARIO_INICIO3:
				list_items[DIALOG_ID_HORARIO_INICIO3][2] = CuadranteDates.formatTime(hourOfDay, minuteOfHour);
				break;
			case DIALOG_ID_HORARIO_FIN3:
				list_items[DIALOG_ID_HORARIO_FIN3][2] = CuadranteDates.formatTime(hourOfDay, minuteOfHour);
				break;
			case DIALOG_ID_HORARIO_INICIO4:
				list_items[DIALOG_ID_HORARIO_INICIO4][2] = CuadranteDates.formatTime(hourOfDay, minuteOfHour);
				break;
			case DIALOG_ID_HORARIO_FIN4:
				list_items[DIALOG_ID_HORARIO_FIN4][2] = CuadranteDates.formatTime(hourOfDay, minuteOfHour);
				break;
			}
			listViewGenerateListAdapter();
		}
	};

	/**
	 * Regenera el listado de opciones del servicio
	 */
	public void listViewGenerateListAdapter() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0; i < list_items.length; i++) {
			if (list_items[i][0] != LIST_ITEM_TYPE_NORMAL_DISABLED) {
				item = new HashMap<String, String>();
				item.put("position", String.valueOf(i));
				item.put("type", list_items[i][0]);
				item.put("line_a", list_items[i][1]);
				item.put("line_b", list_items[i][2]);
				// MyLog.d("list", list_items[i][1] + " length: " +
				// list_items[i].length);
				if (list_items[i].length == 4) {
					// MyLog.d("listViewGenerateListAdapter", "checked: " +
					// list_items[i][3]);
					item.put("checked", list_items[i][3]);
				}
				// item.put( "line3", list_items[i][2]);
				list.add(item);
			}
		}
		sa = new ServicioItemAdapter(this, android.R.layout.simple_list_item_1,
				list);
		setListAdapter(sa);
	}

	/**
	 * Maneja como se debe de mostrar cada item de la lista de opciones del
	 * servicio
	 * 
	 * @author david4
	 * 
	 */
	public class ServicioItemAdapter extends
			ArrayAdapter<HashMap<String, String>> {
		private ArrayList<HashMap<String, String>> items;
		final ListView lv;

		public ServicioItemAdapter(Context context, int textViewResourceId,
				ArrayList<HashMap<String, String>> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			lv = getListView();
			// MyLog.d("servicioItemAdapter", "array: " + items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.servicio_list_item_1, null);
			}
			

			final HashMap<String, String> item = items.get(position);
			if (item != null) {
				TextView line_a = (TextView) v.findViewById(R.id.line_a);
				TextView line_b = (TextView) v.findViewById(R.id.line_b);
				LinearLayout layout_square_color = (LinearLayout) v
						.findViewById(R.id.layout_square_color);
				LinearLayout layout_checkbox = (LinearLayout) v
						.findViewById(R.id.layout_checkbox);
				// tengo que ponerlos invisibles porque al actualizar la lista
				// se mostraban en otros items checkbos o los caudrados
				layout_square_color.setVisibility(View.INVISIBLE);
				layout_checkbox.setVisibility(View.INVISIBLE);
				Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
				spinner.setVisibility(View.GONE);
				
				if (item.get("type") == LIST_ITEM_TYPE_SQUARE_COLOR 
						|| item.get("type") == LIST_ITEM_TYPE_SQUARE_COLOR_DISABLED) {
					TextView square_color = (TextView) v
							.findViewById(R.id.square_color);
					layout_square_color.setVisibility(View.VISIBLE);
					square_color.setBackgroundColor(Integer.parseInt(item
							.get("line_b")));
				}
				//si es un item que hay que desactivar el click parece ser 
				//que funciona con true
				if (item.get("type") == LIST_ITEM_TYPE_SQUARE_COLOR_DISABLED) {
					//MyLog.d("getView", "type:" + item.get("type"));
					v.setClickable(true);
					//no funciona?
					//v.setEnabled(false);
				}
				if (item.get("type") == LIST_ITEM_TYPE_CHECKBOX) {
					layout_checkbox.setVisibility(View.VISIBLE);
					CheckBox checkbox = (CheckBox) v
							.findViewById(R.id.checkBox);
					checkbox.setTag(item);

					if (item.get("checked") == "true"){
						checkbox.setChecked(true);
					}	
					//esto es para que al hacer click en los checkbox sea como
					//al hacer click en el listitem, si no, no se guardan los
					//datos después por los checks de los checkbox
					checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
						@Override 
						public void onCheckedChanged(CompoundButton buttonView, 
								boolean isChecked) { 
							onListItemClick(lv, lv, Integer.parseInt(item.get("position")), Integer.parseInt(item.get("position")));
							if (buttonView.isChecked()) { 
								//onListItemClick(lv, lv, Integer.parseInt(item.get("position")), Integer.parseInt(item.get("position")));
								//Toast.makeText(getBaseContext(), "" + item.get("position"), Toast.LENGTH_SHORT).show();
							} 
							else 
							{ 
								//Toast.makeText(getBaseContext(), "UnChecked", Toast.LENGTH_SHORT).show(); 
							} 
						} 
					}); 				
				}else if(item.get("type") == LIST_ITEM_TYPE_LIST){
					line_b.setVisibility(View.GONE);
					spinner.setVisibility(View.VISIBLE);
					String[] spinnerEntries = null;
					if(item.get("line_a").equals("Guardia combinada")){
						spinnerEntries = getResources().getStringArray(R.array.guardias_combinadas_entries);
						spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								list_items[DIALOG_ID_GUARDIA_COMBINADA][2] = String.valueOf(position);	
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// TODO Auto-generated method stub
								
							}
						});						
					}else if(item.get("line_a").equals("Tipo de día")){
						spinnerEntries = getResources().getStringArray(R.array.type_day_entries);
						spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								list_items[DIALOG_ID_TYPE_DAY][2] = String.valueOf(position);								
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// TODO Auto-generated method stub
								
							}
						});						
					}else if(item.get("line_a").equals("Sucesión de mando")){
						spinnerEntries = getResources().getStringArray(R.array.succession_command_entries);
						spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								list_items[DIALOG_ID_SUCCESSION_COMMAND][2] = String.valueOf(position);								
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// TODO Auto-generated method stub
								
							}
						});						
					}
					ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(mContext, android.R.layout.simple_spinner_item, spinnerEntries);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
					spinner.setAdapter(adapter);
					spinner.setSelection(Integer.valueOf(item.get("line_b")));
				}
				
				line_a.setText(item.get("line_a"));
				line_b.setText(item.get("line_b"));
				
			
			}
			return v;
		}
	}

	/*
	@Override
	public void colorChanged(String key, int color) {
		if (key == "bg") {
			list_items[DIALOG_ID_BG_COLOR][2] = String.valueOf(color);
		} else if (key == "text") {
			list_items[DIALOG_ID_TEXT_COLOR][2] = String.valueOf(color);
		}

		listViewGenerateListAdapter();
	}
	*/
	/**
	 * Cuando se selecciona el color de fondo del tipo de servicio
	 */
	public ColorMixer.OnColorChangedListener onSetColorBg=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			list_items[DIALOG_ID_BG_COLOR][2] = String.valueOf(color);
			listViewGenerateListAdapter();
		}
	};

	/**
	 * Cuando se selecciona el color del texto del tipo de servicio
	 */
	public ColorMixer.OnColorChangedListener onSetColorText=
			new ColorMixer.OnColorChangedListener() {
		public void onColorChange(int color) {
			list_items[DIALOG_ID_TEXT_COLOR][2] = String.valueOf(color);
			listViewGenerateListAdapter();
		}
	};
	
	
	/*
	public void colorCopyChanged(int color) {
		ClipboardManager clipboard = (ClipboardManager)
		        getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(String.valueOf(color));
		Toast.makeText(this.getApplicationContext(), String.valueOf(color), Toast.LENGTH_SHORT).show();
	}
	*/

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			/*
			  Toast.makeText(parent.getContext(), "OnItemSelectedListener : " +
			  parent.getItemAtPosition(pos).toString() + " post:" + pos +
			  " id:" + id + " type_id: " + spin_items[pos][1],
			  Toast.LENGTH_SHORT).show();
			*/
			TYPE_ID = Integer.parseInt(spin_items[pos][1]);
			if (TYPE_ID > 0 && !INSIDE_ONCREATE) {				
				TipoServicioInfo tipo_servicio = db.getTipoServicio(TYPE_ID);
				list_items[DIALOG_ID_NAME][2] = tipo_servicio.getName();
				//list_items[DIALOG_ID_COMMENTS][2] = "";
				
				//MyLog.d("spinnerItemSelected", "is_holiday:" + list_items[DIALOG_ID_IS_HOLIDAY][3]);
				if(list_items[DIALOG_ID_IS_HOLIDAY][3] == "true"){
					list_items[DIALOG_ID_BG_COLOR][2] = 
							String.valueOf(Sp.getHolidayBgColor(mContext));
					list_items[DIALOG_ID_TEXT_COLOR][2] = 
							String.valueOf(Sp.getHolidayTextColor(mContext));					
				}else{
					list_items[DIALOG_ID_BG_COLOR][2] = tipo_servicio.getBgColor();
					list_items[DIALOG_ID_TEXT_COLOR][2] = tipo_servicio.getTextColor();
				}
				
				list_items[DIALOG_ID_GUARDIA_COMBINADA][2] = String.valueOf(tipo_servicio.getGuardiaCombinada());
				list_items[DIALOG_ID_SUCCESSION_COMMAND][2] = String.valueOf(tipo_servicio.getSuccessionCommand());
				list_items[DIALOG_ID_TYPE_DAY][2] = String.valueOf(tipo_servicio.getTypeDay());

				list_items[DIALOG_ID_HORARIO_INICIO][2] = tipo_servicio.getStartSchedule();
				list_items[DIALOG_ID_HORARIO_FIN][2] = tipo_servicio.getEndSchedule();
				list_items[DIALOG_ID_HORARIO_INICIO2][2] = tipo_servicio.getStartSchedule2();
				list_items[DIALOG_ID_HORARIO_FIN2][2] = tipo_servicio.getEndSchedule2();
				list_items[DIALOG_ID_HORARIO_INICIO3][2] = tipo_servicio.getStartSchedule3();
				list_items[DIALOG_ID_HORARIO_FIN3][2] = tipo_servicio.getEndSchedule3();
				list_items[DIALOG_ID_HORARIO_INICIO4][2] = tipo_servicio.getStartSchedule4();
				list_items[DIALOG_ID_HORARIO_FIN4][2] = tipo_servicio.getEndSchedule4();

				// activamos el horario si el horario es diferente a 00:00
				if (!list_items[DIALOG_ID_HORARIO_INICIO][2].equals(Cuadrante.SCHEDULE_NULL)
						|| !list_items[DIALOG_ID_HORARIO_FIN][2].equals(Cuadrante.SCHEDULE_NULL)) {
					list_items[DIALOG_ID_HORARIO_INICIO][0] = LIST_ITEM_TYPE_NORMAL;
					list_items[DIALOG_ID_HORARIO_FIN][0] = LIST_ITEM_TYPE_NORMAL;
				}
				// activamos el horario si el horario es diferente a 00:00
				if (!list_items[DIALOG_ID_HORARIO_INICIO2][2].equals(Cuadrante.SCHEDULE_NULL)
						|| !list_items[DIALOG_ID_HORARIO_FIN2][2].equals(Cuadrante.SCHEDULE_NULL)) {
					list_items[DIALOG_ID_HORARIO_INICIO2][0] = LIST_ITEM_TYPE_NORMAL;
					list_items[DIALOG_ID_HORARIO_FIN2][0] = LIST_ITEM_TYPE_NORMAL;
				}
				// activamos el horario
				if (!list_items[DIALOG_ID_HORARIO_INICIO3][2].equals(Cuadrante.SCHEDULE_NULL)
						|| !list_items[DIALOG_ID_HORARIO_FIN3][2].equals(Cuadrante.SCHEDULE_NULL)) {
					list_items[DIALOG_ID_HORARIO_INICIO3][0] = LIST_ITEM_TYPE_NORMAL;
					list_items[DIALOG_ID_HORARIO_FIN3][0] = LIST_ITEM_TYPE_NORMAL;
				}
				// activamos el horario
				if (!list_items[DIALOG_ID_HORARIO_INICIO4][2].equals(Cuadrante.SCHEDULE_NULL)
						|| !list_items[DIALOG_ID_HORARIO_FIN4][2].equals(Cuadrante.SCHEDULE_NULL)) {
					list_items[DIALOG_ID_HORARIO_INICIO4][0] = LIST_ITEM_TYPE_NORMAL;
					list_items[DIALOG_ID_HORARIO_FIN4][0] = LIST_ITEM_TYPE_NORMAL;
				}
			} else {
				if (!INSIDE_ONCREATE) {
					// MyLog.d("onItemSelected", "onItemSelected");
					list_items[DIALOG_ID_NAME][2] = "";
					list_items[DIALOG_ID_IS_HOLIDAY][3] = "false";
					list_items[DIALOG_ID_IS_IMPORTANT][3] = "false";
					list_items[DIALOG_ID_BG_COLOR][2] = 
							String.valueOf(Sp.getServiceDefaultBgColor(mContext));
					list_items[DIALOG_ID_TEXT_COLOR][2] = 
							String.valueOf(Sp.getServiceDefaultTextColor(mContext));

					list_items[DIALOG_ID_GUARDIA_COMBINADA][2] = "0";
					list_items[DIALOG_ID_SUCCESSION_COMMAND][2] = "0";
					list_items[DIALOG_ID_TYPE_DAY][2] = "0";
					
					list_items[DIALOG_ID_HORARIO_INICIO][2] = Cuadrante.SCHEDULE_NULL;
					list_items[DIALOG_ID_HORARIO_FIN][2] = Cuadrante.SCHEDULE_NULL;
					list_items[DIALOG_ID_HORARIO_INICIO2][2] = Cuadrante.SCHEDULE_NULL;
					list_items[DIALOG_ID_HORARIO_FIN2][2] = Cuadrante.SCHEDULE_NULL;
					list_items[DIALOG_ID_HORARIO_INICIO3][2] = Cuadrante.SCHEDULE_NULL;
					list_items[DIALOG_ID_HORARIO_FIN3][2] = Cuadrante.SCHEDULE_NULL;
					list_items[DIALOG_ID_HORARIO_INICIO4][2] = Cuadrante.SCHEDULE_NULL;
					list_items[DIALOG_ID_HORARIO_FIN4][2] = Cuadrante.SCHEDULE_NULL;

					// activamos el horario si el horario es diferente a 00:00
					if (!list_items[DIALOG_ID_HORARIO_INICIO][2].equals(Cuadrante.SCHEDULE_NULL)
							|| !list_items[DIALOG_ID_HORARIO_FIN][2].equals(Cuadrante.SCHEDULE_NULL)) {
						list_items[DIALOG_ID_HORARIO_INICIO][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
						list_items[DIALOG_ID_HORARIO_FIN][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
					}
					// activamos el horario si el horario es diferente a 00:00
					if (!list_items[DIALOG_ID_HORARIO_INICIO2][2].equals(Cuadrante.SCHEDULE_NULL)
							|| !list_items[DIALOG_ID_HORARIO_FIN2][2].equals(Cuadrante.SCHEDULE_NULL)) {
						list_items[DIALOG_ID_HORARIO_INICIO2][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
						list_items[DIALOG_ID_HORARIO_FIN2][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
					}
					// activamos el horario
					if (!list_items[DIALOG_ID_HORARIO_INICIO3][2].equals(Cuadrante.SCHEDULE_NULL)
							|| !list_items[DIALOG_ID_HORARIO_FIN3][2].equals(Cuadrante.SCHEDULE_NULL)) {
						list_items[DIALOG_ID_HORARIO_INICIO3][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
						list_items[DIALOG_ID_HORARIO_FIN3][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
					}
					// activamos el horario
					if (!list_items[DIALOG_ID_HORARIO_INICIO4][2].equals(Cuadrante.SCHEDULE_NULL)
							|| !list_items[DIALOG_ID_HORARIO_FIN4][2].equals(Cuadrante.SCHEDULE_NULL)) {
						list_items[DIALOG_ID_HORARIO_INICIO4][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
						list_items[DIALOG_ID_HORARIO_FIN4][0] = LIST_ITEM_TYPE_NORMAL_DISABLED;
					}
				}
				INSIDE_ONCREATE = false;

			}
			listViewGenerateListAdapter();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cuadrante.checkSignIn(this);		
	}	
}