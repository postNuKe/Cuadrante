package es.progmac.cuadrante;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.commonsware.cwac.colormixer.ColorMixer;
import com.commonsware.cwac.colormixer.ColorMixerDialogCopy;

import es.progmac.cuadrante.R;
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
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Pantalla de añadir/editar servicio diario
 * 
 * @author david4
 * 
 */
public class TipoServicioActivity extends SherlockListActivity implements
		//ColorPickerWithCopyDialog.OnColorChangedListener, 
		OnItemSelectedListener {

	private static final String TAG = "TipoServicioActivity";
	public DatabaseHandler db;

	public int TYPE_ID = 0;
	public String ACTIVITY = "";
	public String ACTION = "";
	public String DATE = "";

	/**
	 * Posición en el array de items
	 * 
	 * @see list_items
	 */
	static final int DIALOG_ID_TITLE = 0;
	static final int DIALOG_ID_NAME = 1;
	static final int DIALOG_ID_DATE_RANGE = 2;
	static final int DIALOG_ID_BG_COLOR = 3;
	static final int DIALOG_ID_TEXT_COLOR = 4;
	static final int DIALOG_ID_TYPE_DAY = 5;
	static final int DIALOG_ID_GUARDIA_COMBINADA = 6;
	static final int DIALOG_ID_SUCCESSION_COMMAND = 7;
	static final int DIALOG_ID_ASK_SCHEDULE = 8;
	static final int DIALOG_ID_HORARIO_INICIO = 9;
	static final int DIALOG_ID_HORARIO_FIN = 10;
	static final int DIALOG_ID_HORARIO_INICIO2 = 11;
	static final int DIALOG_ID_HORARIO_FIN2 = 12;
	static final int DIALOG_ID_HORARIO_INICIO3 = 13;
	static final int DIALOG_ID_HORARIO_FIN3 = 14;
	static final int DIALOG_ID_HORARIO_INICIO4 = 15;
	static final int DIALOG_ID_HORARIO_FIN4 = 16;
	
	/**
	 * Diálogo de actualizar los servicios del tipo con name, y colores
	 */
	static final int DIALOG_UPDATE_SERVICES = 100;
	/**
	 * Diálogo de información sobre los horarios y un tipo de dia seleccionado
	 */
	static final int DIALOG_TYPE_DATE_SCHEDULE_INFO = 101;
	
	/**
	 * Color por defecto de fondo para un servicio
	 */
	static final int DEFAULT_BG_COLOR = -1;// white
	/**
	 * Color por defecto del texto para un servicio
	 */
	static final int DEFAULT_TEXT_COLOR = -16777216;// black
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
	 * Array bidimensional con la lista de items a mostrar
	 */
	private String[][] list_items = {
			// [0] , [1] , [2]
			{ LIST_ITEM_TYPE_NORMAL, "Título", "" },
			{ LIST_ITEM_TYPE_NORMAL, "Abreviatura", "" },
			{ LIST_ITEM_TYPE_CHECKBOX, "Rango de fechas", "", "false" },
			{ LIST_ITEM_TYPE_SQUARE_COLOR, "Color de fondo",
					String.valueOf(DEFAULT_BG_COLOR) },
			{ LIST_ITEM_TYPE_SQUARE_COLOR, "Color del texto",
					String.valueOf(DEFAULT_TEXT_COLOR) },
			{ LIST_ITEM_TYPE_LIST, "Tipo de día", "0" },
			{ LIST_ITEM_TYPE_LIST, "Guardia combinada", "0" },
			{ LIST_ITEM_TYPE_LIST, "Sucesión de mando", "0" },
			{ LIST_ITEM_TYPE_CHECKBOX, "Preguntar para insertar horarios", "", "false" },
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
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tipo_servicio);
		
		db = new DatabaseHandler(this);
		mContext = this;
		
		list_items[DIALOG_ID_BG_COLOR][2] = Integer.toString(Sp.getServiceDefaultBgColor(mContext));
		list_items[DIALOG_ID_TEXT_COLOR][2] = Integer.toString(Sp.getServiceDefaultTextColor(mContext));
		
		// Get the message from the intent
		Intent intent = getIntent();

		TYPE_ID = intent.getIntExtra(Extra.TYPE_ID, 0);

		// miramos si se ha llamado esta pagina desde la creación directa de
		// un tipo en una celda del calendario, si es asi habra que crear el
		// tipo
		// y grabarlo al dia
		if(intent.hasExtra("ACTIVITY")){
			ACTIVITY = intent.getStringExtra("ACTIVITY");
			if (!ACTIVITY.equals("")) {
				ACTION = intent.getStringExtra("ACTION");
				DATE = intent.getStringExtra("DATE");
			}
		}

		TipoServicioInfo tipo_servicio = db.getTipoServicio(TYPE_ID);
		
		if (tipo_servicio.getId() > 0) {
			setTitle(getResources().getString(R.string.type_service_edit));
			/*
			Button btn_delete = (Button) findViewById(R.id.delete);
			btn_delete.setEnabled(true);
			*/
			list_items[DIALOG_ID_TITLE][2] = tipo_servicio.getTitle();
			list_items[DIALOG_ID_NAME][2] = tipo_servicio.getName();

			String is_date_range;
			if (tipo_servicio.getIsDateRange() == 1)
				is_date_range = "true";
			else
				is_date_range = "false";
			
			String ask_schedule = "false";
			if (tipo_servicio.getAskSchedule() == 1) ask_schedule = "true";
			
			list_items[DIALOG_ID_DATE_RANGE][3] = String.valueOf(is_date_range);
			list_items[DIALOG_ID_BG_COLOR][2] = tipo_servicio.getBgColor();
			list_items[DIALOG_ID_TEXT_COLOR][2] = tipo_servicio.getTextColor();
			
			list_items[DIALOG_ID_GUARDIA_COMBINADA][2] = String.valueOf(tipo_servicio.getGuardiaCombinada());
			list_items[DIALOG_ID_SUCCESSION_COMMAND][2] = String.valueOf(tipo_servicio.getSuccessionCommand());
			list_items[DIALOG_ID_TYPE_DAY][2] = String.valueOf(tipo_servicio.getTypeDay());
			list_items[DIALOG_ID_ASK_SCHEDULE][3] = String.valueOf(ask_schedule);

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
		}else{
			setTitle(getResources().getString(R.string.type_service_add));
		}

		listViewGenerateListAdapter();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_tipo_servicio, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		/*
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intentSettings = new Intent(this, SettingsActivity.class);
			startActivity(intentSettings);
			return true;
		case R.id.menu_add_schedule:
			onSetAddSchedule(getWindow().getDecorView().getRootView());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		*/
		View v = getWindow().getDecorView().getRootView();
		switch (item.getItemId()) {
		case R.id.bar_ready:
			onPromptDialogSetData(v);
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
		case DIALOG_ID_TITLE:
			showDialog(DIALOG_ID_TITLE);
			break;
		case DIALOG_ID_NAME:
			showDialog(DIALOG_ID_NAME);
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
					DEFAULT_BG_COLOR).show();
					*/
			new ColorMixerDialogCopy(this, Sp.getServiceDefaultBgColor(mContext),
					Integer.parseInt(list_items[DIALOG_ID_BG_COLOR][2]), 
					onSetColorBg).show();
			break;
		case DIALOG_ID_TEXT_COLOR:
			/*
			new ColorPickerWithCopyDialog(this, this, "text",
					Integer.parseInt(list_items[DIALOG_ID_TEXT_COLOR][2]),
					DEFAULT_TEXT_COLOR).show();
					*/
			new ColorMixerDialogCopy(this, Sp.getServiceDefaultTextColor(mContext),
					Integer.parseInt(list_items[DIALOG_ID_TEXT_COLOR][2]), 
					onSetColorText).show();
			
			break;
		case DIALOG_ID_DATE_RANGE:
			if (list_items[DIALOG_ID_DATE_RANGE][3] == "false")
				list_items[DIALOG_ID_DATE_RANGE][3] = "true";
			else
				list_items[DIALOG_ID_DATE_RANGE][3] = "false";
			listViewGenerateListAdapter();
			break;
		case DIALOG_ID_ASK_SCHEDULE:
			if (list_items[DIALOG_ID_ASK_SCHEDULE][3] == "false")
				list_items[DIALOG_ID_ASK_SCHEDULE][3] = "true";
			else
				list_items[DIALOG_ID_ASK_SCHEDULE][3] = "false";
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
		case DIALOG_ID_TITLE:
			PromptDialog dlg = new PromptDialog(this,
					R.string.type_service_title, 0, list_items[DIALOG_ID_TITLE][2], 30, null, 0) {
				@Override
				public boolean onOkClicked(String input) {
					// do something
					list_items[DIALOG_ID_TITLE][2] = input;
					listViewGenerateListAdapter();
					return true; // true = close dialog
				}
			};
			dlg.show();
			break;
		case DIALOG_ID_NAME:
			PromptDialog dlg2 = new PromptDialog(this, R.string.abbreviation,
					0, list_items[DIALOG_ID_NAME][2], Sp.getLengthServiceName(this), 
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
			dlg2.show();
			break;
		case DIALOG_UPDATE_SERVICES:
		    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    dialog.setMessage(getString(R.string.message_alert_update_services));
		    dialog.setCancelable(false);
		    dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {
		    		db.updateServiciosFromTipo(TYPE_ID, 
				    	list_items[DIALOG_ID_NAME][2], 
				    	list_items[DIALOG_ID_BG_COLOR][2], 
				    	list_items[DIALOG_ID_TEXT_COLOR][2]);
		    		db.updateHolidays(mContext);
		    		onSetData();
		       }
		    });
		    dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
				    onSetData();		        	
		        }
		    });
		    dialog.create();
		    dialog.show();
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
	 * Muestra una ventana emergente preguntando si quiere actualizar todos
	 * los servicios que tienen asignado este tipo de servicio, solo la abreviatura
	 * y colores
	 * @param view
	 */
	public void onPromptDialogSetData(View view){	
		TipoServicioInfo tipo_servicio = db.getTipoServicio(TYPE_ID);

		if (tipo_servicio.getId() > 0) {
			showDialog(DIALOG_UPDATE_SERVICES);
		}else{
			onSetData();
		}
	}
	
	public void onSetData() {
		if(list_items[DIALOG_ID_TITLE][2].length() == 0){
			Toast.makeText(
					getApplicationContext(), 
					R.string.error_title_required, 
					Toast.LENGTH_LONG).show();
			return;
		}
		if(list_items[DIALOG_ID_NAME][2].length() == 0){
			Toast.makeText(
					getApplicationContext(), 
					R.string.error_abreviatura_required, 
					Toast.LENGTH_LONG).show();
			return;
		}

		Integer is_date_range = 0;
		if (list_items[DIALOG_ID_DATE_RANGE][3] == "true")
			is_date_range = 1;
		
		Integer ask_schedule = 0;
		if (list_items[DIALOG_ID_ASK_SCHEDULE][3] == "true")
			ask_schedule = 1;
		MyLog.d(TAG, ask_schedule);
		
		//si tiene seleccionado un tipo de dia, los horarios tienen ke estar a 0, porque se supone
		//que son vacas, baja, etc... es decir, que no hay trabajo
		//pero el tipo 3 que es -1 dia natural del mes los grabamos
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

		TipoServicioInfo tipo_servicio = db.getTipoServicio(TYPE_ID);
		
		//si actualizamos un tipo
		if (tipo_servicio.getId() > 0) {
			db.updateTipoServicio(new TipoServicioInfo(TYPE_ID,
					list_items[DIALOG_ID_TITLE][2],
					list_items[DIALOG_ID_NAME][2],
					is_date_range,
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
					Integer.valueOf(list_items[DIALOG_ID_GUARDIA_COMBINADA][2]),
					Integer.valueOf(list_items[DIALOG_ID_TYPE_DAY][2]),
					Integer.valueOf(list_items[DIALOG_ID_SUCCESSION_COMMAND][2]),
					ask_schedule));
		} else {
			tipo_servicio = db.addTipoServicio(new TipoServicioInfo(
					list_items[DIALOG_ID_TITLE][2],
					list_items[DIALOG_ID_NAME][2], 
					is_date_range,
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
					Integer.valueOf(list_items[DIALOG_ID_GUARDIA_COMBINADA][2]),
					Integer.valueOf(list_items[DIALOG_ID_TYPE_DAY][2]),
					Integer.valueOf(list_items[DIALOG_ID_SUCCESSION_COMMAND][2]),
					ask_schedule));
		}
		// miramos si hay que añadir un tipo desde el calendario y asignarlo
		// a un dia en concreto
		if (!ACTIVITY.equals("")) {
			if (!ACTION.equals("") && !DATE.equals("")) {
				ServicioInfo servicio = db.getServicio(DATE);
				MyLog.d("onSetData", "se actualiza el servicio");
				// el dia ya existia en la bd, asi que se actualiza con el
				// nuevo tipo de servicio
				if (servicio.getId() > 0) {
					if(servicio.getIsHoliday() == 1){
						list_items[DIALOG_ID_BG_COLOR][2] = 
								String.valueOf(Sp.getHolidayBgColor(mContext));
						list_items[DIALOG_ID_TEXT_COLOR][2] = 
								String.valueOf(Sp.getHolidayBgColor(mContext));						
					}
					db.updateServicio(new ServicioInfo(DATE, tipo_servicio.getId(),
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
							"", 0,
							Integer.valueOf(list_items[DIALOG_ID_GUARDIA_COMBINADA][2]),
							Integer.valueOf(list_items[DIALOG_ID_TYPE_DAY][2]),
							0,
							Integer.valueOf(list_items[DIALOG_ID_SUCCESSION_COMMAND][2])));
				} else {
					servicio = db.addService(new ServicioInfo(DATE,
							tipo_servicio.getId(),
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
							"", 0,
							Integer.valueOf(list_items[DIALOG_ID_GUARDIA_COMBINADA][2]),
							Integer.valueOf(list_items[DIALOG_ID_TYPE_DAY][2]),
							0,
							Integer.valueOf(list_items[DIALOG_ID_SUCCESSION_COMMAND][2])));
				}
				Cuadrante.deleteHoursDate(mContext, DATE);
				//venimos de crear un tipo desde la pantalla del calendario
				//por lo que miramos si este nuevo tipo tiene rango de fechas
				//si es así al volver al calendario se debe de mostrar el 
				//datePicker para que el usuario seleccione hasta que día quiere
				if(ACTION.equals("createTipo") && tipo_servicio.getIsDateRange() == 1){
					Intent intent = new Intent();
					setResult(tipo_servicio.getId(), intent);
					this.finish();					
				}
			}
		}
		Cuadrante.refreshWidget(mContext);
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		this.finish();

	}

	public void onSetDelete(View view) {
		if (TYPE_ID > 0) {
		    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    //revisamos si el tipo es vacaciones o asuntos particulares, si es así mostramos
		    //ventana de que no puede eliminarlo
		    if(TYPE_ID == Sp.getHolidaysTypeService(this)){
			    dialog.setMessage(getString(R.string.message_alert_delete_type_service_holidays));
			    dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int id) {   				    		
			    	}
			    });
			    dialog.create();
			    dialog.show();	
		    }else if(TYPE_ID == Sp.getOwnAffairsTypeService(this)){
			    dialog.setMessage(getString(R.string.message_alert_delete_type_service_own_affairs));
			    dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int id) {   				    		
			    	}
			    });
			    dialog.create();
			    dialog.show();	
		    }else if(TYPE_ID == Sp.getTypeServiceMedicalLeave(this)){
			    dialog.setMessage(getString(R.string.message_alert_delete_type_service_medical_leave));
			    dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int id) {   				    		
			    	}
			    });
			    dialog.create();
			    dialog.show();		    	
			}else{
				//alertamos si está seguro de querer eliminar el servicio
			    dialog.setMessage(getString(R.string.message_alert_delete_tipo_servicio));
			    dialog.setCancelable(false);
			    dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int id) {  
			    		TipoServicioInfo type_service = db.getTipoServicio(TYPE_ID);
						db.deleteTipoServicio(TYPE_ID);
						db.updateServiciosto0(TYPE_ID, type_service.getTitle().concat("\n"));
						if(TYPE_ID == Sp.getLastUsedTypeService(mContext)){
							Sp.setLastUsedTypeServiceDV(mContext);
						}
						Cuadrante.refreshWidget(mContext);
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						TipoServicioActivity.this.finish();		    		
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
	}

	public void onSetCancel(View view) {
		this.finish();

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

		for (int i = list_items.length - 1; i > 6  && j <= 2; i--) {
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
	
	/**
	 * Función que se llama cuando se añade la hora inicio o fin
	 */
	private IntvlMinTimePickerDialog.OnTimeSetListener mTimeSetListener = 
			new IntvlMinTimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			switch (DIALOG_ID_HORARIO_WHICH) {
				case DIALOG_ID_HORARIO_INICIO:
					list_items[DIALOG_ID_HORARIO_INICIO][2] 
							= CuadranteDates.formatTime(hourOfDay, minuteOfHour);
					break;
				case DIALOG_ID_HORARIO_FIN:
					list_items[DIALOG_ID_HORARIO_FIN][2] 
							= CuadranteDates.formatTime(hourOfDay, minuteOfHour);
					break;
				case DIALOG_ID_HORARIO_INICIO2:
					list_items[DIALOG_ID_HORARIO_INICIO2][2] 
							= CuadranteDates.formatTime(hourOfDay, minuteOfHour);
					break;
				case DIALOG_ID_HORARIO_FIN2:
					list_items[DIALOG_ID_HORARIO_FIN2][2] 
							= CuadranteDates.formatTime(hourOfDay, minuteOfHour);
					break;
				case DIALOG_ID_HORARIO_INICIO3:
					list_items[DIALOG_ID_HORARIO_INICIO3][2] 
							= CuadranteDates.formatTime(hourOfDay, minuteOfHour);
					break;
				case DIALOG_ID_HORARIO_FIN3:
					list_items[DIALOG_ID_HORARIO_FIN3][2] 
							= CuadranteDates.formatTime(hourOfDay, minuteOfHour);
					break;
				case DIALOG_ID_HORARIO_INICIO4:
					list_items[DIALOG_ID_HORARIO_INICIO4][2] 
							= CuadranteDates.formatTime(hourOfDay, minuteOfHour);
					break;
				case DIALOG_ID_HORARIO_FIN4:
					list_items[DIALOG_ID_HORARIO_FIN4][2] 
							= CuadranteDates.formatTime(hourOfDay, minuteOfHour);
					break;			
			}
			listViewGenerateListAdapter();
		}
	};

	/**
	 * Regenera el listado de opciones del servicio
	 */
	public void listViewGenerateListAdapter() {
		/*
		 * ArrayList<HashMap<String,String>> list = new
		 * ArrayList<HashMap<String,String>>(); HashMap<String,String> item;
		 * for(int i=0; i < list_items.length;i++){ item = new
		 * HashMap<String,String>(); item.put( "line1", list_items[i][0]);
		 * item.put( "line2", list_items[i][1]); //item.put( "line3",
		 * list_items[i][2]); list.add( item ); }
		 * 
		 * sa = new SimpleAdapter(this, list, R.layout.servicio_list_item_1, new
		 * String[] { "line1", "line2", //"line3" }, new int[] { R.id.line_a,
		 * R.id.line_b, //R.id.line_c });
		 */

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

				if (item.get("type") == LIST_ITEM_TYPE_SQUARE_COLOR) {
					TextView square_color = (TextView) v
							.findViewById(R.id.square_color);
					layout_square_color.setVisibility(View.VISIBLE);
					square_color.setBackgroundColor(Integer.parseInt(item
							.get("line_b")));
				}else if (item.get("type") == LIST_ITEM_TYPE_CHECKBOX) {
					layout_checkbox.setVisibility(View.VISIBLE);
					CheckBox checkbox = (CheckBox) v
							.findViewById(R.id.checkBox);
					if (item.get("checked") == "true")
						checkbox.setChecked(true);
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cuadrante.checkSignIn(this);		
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
	
}