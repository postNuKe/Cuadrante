package es.progmac.cuadrante;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.Days;
import org.joda.time.Interval;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import es.progmac.cuadrante.R;
import es.progmac.cuadrante.ComisionActivity.ComisionItemAdapter;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.PromptDialog;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HotelActivity extends SherlockListActivity {
	
	public DatabaseHandler db;
	private int _TMP_ID = 5;
	private ListAdapter sa;
	/**
	 * Datos pasados de un activity a este
	 */
	Bundle extras;
	/**
	 * Posición en el array de items
	 * 
	 * @see this.list_items
	 */
	static final int DIALOG_ID_NAME = 0;
	static final int DIALOG_ID_COMMENTS = 1;
	static final int DIALOG_ID_START_DATE = 2;
	static final int DIALOG_ID_END_DATE = 3;
	static final int DIALOG_ID_DAILY_EXPENSES = 4;
	static final int DIALOG_ID_MANUTENCION_EXPENSES = 5;
	static final int DIALOG_ID_LAUNDRY = 6;
	
	/**
	 * Variable que guardará qué DatePicker, si fecha inicio o fin, debe de
	 * mostrar
	 */
	static int DIALOG_ID_DATE_WHICH = DIALOG_ID_START_DATE;		
	/**
	 * Item de la lista es normal es decir, dos lineas de texto
	 */
	static final String LIST_ITEM_TYPE_NORMAL = "normal";	
	/**
	 * Para definir que el valor debe convertirse a formato de fecha
	 */
	static final String LIST_ITEM_TYPE_DATE = "date";	
	/**
	 * Para definir que la segunda linea debe añadirse el símbolo de euros
	 */
	static final String LIST_ITEM_TYPE_NORMAL_EUROS = "euros";	
	/**
	 * Array bidimensional con la lista de items a mostrar
	 */
	private String[][] list_items;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotel);
		db = new DatabaseHandler(this);
		
		Intent intent = getIntent();
		extras = intent.getExtras();
		if(intent.hasExtra(Extra.TMP_HOTEL_ID)){
			_TMP_ID = intent.getIntExtra(Extra.TMP_HOTEL_ID, 4);
			boolean add_hotel = intent.getBooleanExtra(Extra.HOTEL_ADD, true);
			//añadir hotel
			if(add_hotel){
				setTitle(R.string.title_activity_hotel_add);
			}else{//editamos hotel
				setTitle(R.string.title_activity_hotel_edit);
			}
			
			list_items = new String[][]{
				{ LIST_ITEM_TYPE_NORMAL, 
					this.getString(R.string.list_item_hotel_name), 
					intent.getStringExtra(Extra.NAME) },
				{ LIST_ITEM_TYPE_NORMAL, 
					this.getString(R.string.list_item_comments), 
					intent.getStringExtra(Extra.COMMENTS) },
				{ LIST_ITEM_TYPE_DATE, 
					this.getString(R.string.list_item_start_date), 
					intent.getStringExtra(Extra.START_DATE) },
				{ LIST_ITEM_TYPE_DATE, 
					this.getString(R.string.list_item_end_date), 
					intent.getStringExtra(Extra.END_DATE) },				
				{ LIST_ITEM_TYPE_NORMAL_EUROS, 
					this.getString(R.string.list_item_daily_expenses), 
					intent.getStringExtra(Extra.DAILY_EXPENSES) },	
				{ LIST_ITEM_TYPE_NORMAL_EUROS, 
					this.getString(R.string.list_item_manutencion_expenses), 
					intent.getStringExtra(Extra.MANUTENCION_EXPENSES) },						
				{ LIST_ITEM_TYPE_NORMAL_EUROS, 
					this.getString(R.string.list_item_laundry), 
					intent.getStringExtra(Extra.LAUNDRY) },							
			};
			
		}
		listViewGenerateListAdapter();

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_hotel, menu);

		return true;
	}    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		View v = getWindow().getDecorView().getRootView();
		switch (item.getItemId()) {
		case R.id.bar_ready:
			onSetData(v);
			return true;		
		case R.id.bar_cancel:
			onSetCancel(v);
			return true;		
		case R.id.bar_delete:
			onSetDelete(v);
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}	
	
	public void onSetCancel(View view) {
		this.finish();
	}	
	
	private void onSetData(View v) {
		//revisamos que se hayan introducido las fechas
		if(list_items[DIALOG_ID_START_DATE][2].length() > 0 && 
				list_items[DIALOG_ID_END_DATE][2].length() > 0){
			MyLog.d("onSetData", "comision dates:" + extras.getString(Extra.COMISION_START_DATE) + " " + extras.getString(Extra.COMISION_END_DATE));
			CuadranteDates comStartDate = new CuadranteDates(extras.getString(Extra.COMISION_START_DATE));			
			CuadranteDates comEndDate = new CuadranteDates(extras.getString(Extra.COMISION_END_DATE)).setTime(23, 59);
			Interval intervalComision = new Interval(comStartDate.getDateTime(), comEndDate.getDateTime());
			CuadranteDates hotelStartDate = new CuadranteDates(list_items[DIALOG_ID_START_DATE][2]).setTime(23, 59);			
			CuadranteDates hotelEndDate = new CuadranteDates(list_items[DIALOG_ID_END_DATE][2]);
			Interval intervalHotelStart = new Interval(hotelStartDate.getDateTime(), hotelStartDate.getDateTime());
			Interval intervalHotelEnd = new Interval(hotelEndDate.getDateTime(), hotelEndDate.getDateTime());
			/*
			MyLog.d("onSetData", "intervalHotelStart:" + intervalComision.overlaps(intervalHotelStart));
			MyLog.d("onSetData", "intervalHotelEnd:" + intervalComision.overlaps(intervalHotelEnd));
			MyLog.d("onSetData", "hotelStartDate.getYear():" + hotelStartDate.getYear() + 
					" hotelStartDate.getMonth():" + hotelStartDate.getMonth() + " hotelStartDate.getDay():" + hotelStartDate.getDay());
			MyLog.d("onSetData", "hotelEndDate.getYear():" + hotelEndDate.getYear() + 
					" hotelEndDate.getMonth():" + hotelEndDate.getMonth() + " hotelEndDate.getDay():" + hotelEndDate.getDay());
			MyLog.d("onSetData", "dias:" + Days.daysBetween(hotelStartDate.getDateTime(), hotelEndDate.getDateTime()).getDays());
			*/
			//el hotel está fuera del rango de la comisión
			if(!intervalComision.overlaps(intervalHotelStart) ||
				!intervalComision.overlaps(intervalHotelEnd) ||
				(hotelStartDate.getYear() == hotelEndDate.getYear() &&
				hotelStartDate.getMonth() == hotelEndDate.getMonth() &&
				hotelStartDate.getDay() == hotelEndDate.getDay()) ||
				(Days.daysBetween(hotelStartDate.getDateTime(), hotelEndDate.getDateTime()).getDays() < 0)
				){
				showErrorDialogIntervalComisionDate();
			}else{			
				Intent returnIntent = new Intent();
				returnIntent.putExtra(Extra.TMP_HOTEL_ID, _TMP_ID);
				returnIntent.putExtra(Extra.NAME, list_items[DIALOG_ID_NAME][2]);
				returnIntent.putExtra(Extra.COMMENTS, list_items[DIALOG_ID_COMMENTS][2]);
				returnIntent.putExtra(Extra.START_DATE, list_items[DIALOG_ID_START_DATE][2]);
				returnIntent.putExtra(Extra.END_DATE, list_items[DIALOG_ID_END_DATE][2]);
				returnIntent.putExtra(Extra.DAILY_EXPENSES, list_items[DIALOG_ID_DAILY_EXPENSES][2]);
				returnIntent.putExtra(Extra.MANUTENCION_EXPENSES, 
						list_items[DIALOG_ID_MANUTENCION_EXPENSES][2]);
				returnIntent.putExtra(Extra.LAUNDRY, list_items[DIALOG_ID_LAUNDRY][2]);
				setResult(RESULT_OK, returnIntent);     
				finish();
			}
		}else{
			Toast.makeText(this, R.string.error_no_start_and_end_date, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Si la fecha del hotel no está dentro de la fecha de la comisión muestra
	 * un diálogo de error.
	 */
	private void showErrorDialogIntervalComisionDate(){
	    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    dialog.setMessage(getString(R.string.message_alert_error_interval_hotel));
	    dialog.setCancelable(false);
	    dialog.setPositiveButton(getString(R.string.understood), new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {	    		
	       }
	    });
	    dialog.create();
	    dialog.show();		
	}
	
	private void onSetDelete(View v){
		//alertamos si está seguro de querer eliminar el hotel
	    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    dialog.setMessage(getString(R.string.message_alert_delete_hotel));
	    dialog.setCancelable(false);
	    dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent();
				intent.putExtra(Extra.TMP_HOTEL_ID, _TMP_ID);
				setResult(Cuadrante.RESULT_DELETE, intent);
				HotelActivity.this.finish();		    		
	       }
	    });
	    dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	
	        }
	    });
	    dialog.create();
	    dialog.show();		
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		Resources res = getResources();
		CuadranteDates date;
		PromptDialog dlg;
		final CuadranteDates comisionStartDate = 
				new CuadranteDates(extras.getString(Extra.COMISION_START_DATE));
		final CuadranteDates comisionEndDate = 
				new CuadranteDates(extras.getString(Extra.COMISION_END_DATE));
		switch (position) {
		case DIALOG_ID_NAME:
			dlg = new PromptDialog(this, 
				R.string.list_item_hotel_name, 0, list_items[DIALOG_ID_NAME][2], 
				res.getInteger(R.integer.MAX_LENGTH_INPUT_NAME), 
				String.format(this.getString(R.string.hint_max_characters), 
							res.getInteger(R.integer.MAX_LENGTH_INPUT_NAME)), 0) {
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
			dlg = new PromptDialog(this,
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
			dlg.show();
			break;		
		case DIALOG_ID_START_DATE:
			DIALOG_ID_DATE_WHICH = DIALOG_ID_START_DATE;
			date = new CuadranteDates(list_items[DIALOG_ID_START_DATE][2]);
			new DatePickerDialog(this, mDateSetListener, date.getYear(), date.getMonth() - 1, date.getDay()){
				/*
				public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
					//si se intenta cambiar la fecha a una menor que la fecha
					//inicio de la comisión, lo impide
					if(year < comisionStartDate.getYear() 
						|| (year == comisionStartDate.getYear() 
							&& monthOfYear < comisionStartDate.getMonth()-1)
						|| (year == comisionStartDate.getYear() 
								&& monthOfYear == comisionStartDate.getMonth()-1
								&& dayOfMonth < comisionStartDate.getDay())){
						view.updateDate(comisionStartDate.getYear(), comisionStartDate.getMonth()-1, comisionStartDate.getDay());
					}else if(year > comisionEndDate.getYear() 
						|| (year == comisionEndDate.getYear() 
							&& monthOfYear > comisionEndDate.getMonth()-1)
						|| (year == comisionEndDate.getYear() 
								&& monthOfYear == comisionEndDate.getMonth()-1
								&& dayOfMonth > comisionEndDate.getDay())){
						view.updateDate(comisionEndDate.getYear(), comisionEndDate.getMonth()-1, comisionEndDate.getDay());
					}
					
				}
				*/
			}.show();
			break;
		case DIALOG_ID_END_DATE:
			DIALOG_ID_DATE_WHICH = DIALOG_ID_END_DATE;
			//si no hay fecha fin, ponemos la fecha inicio
			if(list_items[DIALOG_ID_END_DATE][2].length() == 0){
				date = new CuadranteDates(list_items[DIALOG_ID_START_DATE][2]);
			}else{
				date = new CuadranteDates(list_items[DIALOG_ID_END_DATE][2]);
			}
			new DatePickerDialog(this, mDateSetListener, date.getYear(), date.getMonth() - 1, date.getDay()).show();
			break;
			
		case DIALOG_ID_DAILY_EXPENSES:			
			dlg = new PromptDialog(this, 
					R.string.list_item_daily_expenses, list_items[DIALOG_ID_DAILY_EXPENSES][2]) {
					@Override
					public boolean onOkClicked(String input) {
						// do something
						list_items[DIALOG_ID_DAILY_EXPENSES][2] = input;
						listViewGenerateListAdapter();
						return true; // true = close dialog
					}
			};
			dlg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			dlg.setHintText(R.string.dialog_message_number_decimal);
			dlg.show();
			break;
		case DIALOG_ID_MANUTENCION_EXPENSES:			
			dlg = new PromptDialog(this, 
					R.string.list_item_manutencion_expenses, list_items[DIALOG_ID_MANUTENCION_EXPENSES][2]) {
					@Override
					public boolean onOkClicked(String input) {
						// do something
						list_items[DIALOG_ID_MANUTENCION_EXPENSES][2] = input;
						listViewGenerateListAdapter();
						return true; // true = close dialog
					}
			};
			dlg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			dlg.setHintText(R.string.dialog_message_number_decimal);
			dlg.show();
			break;
		case DIALOG_ID_LAUNDRY:
			dlg = new PromptDialog(this, 
					R.string.list_item_laundry, 0, //R.string.dialog_message_laundry,
					list_items[DIALOG_ID_LAUNDRY][2]) {
				@Override
				public boolean onOkClicked(String input) {
					list_items[DIALOG_ID_LAUNDRY][2] = input;
					listViewGenerateListAdapter();
					return true; // true = close dialog
				}
			};
			dlg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			dlg.setHintText(R.string.dialog_message_number_decimal);
			dlg.show();
			break;			
		}
	}
	/**
	 * Regenera el listado de opciones de la comisión
	 */
	public void listViewGenerateListAdapter() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0; i < list_items.length; i++) {
			//if (list_items[i][0] != LIST_ITEM_TYPE_NORMAL_DISABLED) {
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
			//}
		}
		sa = new ComisionItemAdapter(this, android.R.layout.simple_list_item_1,
				list);
		setListAdapter(sa);
	}

	/**
	 * Maneja como se debe de mostrar cada item de la lista de opciones de la
	 * comisión
	 */
	public class ComisionItemAdapter extends
			ArrayAdapter<HashMap<String, String>> {
		private ArrayList<HashMap<String, String>> items;
		final ListView lv;

		public ComisionItemAdapter(Context context, int textViewResourceId,
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
				v = vi.inflate(R.layout.comision_list_item_1, null);
			}
			

			final HashMap<String, String> item = items.get(position);
			if (item != null) {
				TextView line_a = (TextView) v.findViewById(R.id.line_a);
				TextView line_b = (TextView) v.findViewById(R.id.line_b);
				
				line_a.setText(item.get("line_a"));
				
				if (item.get("type") == LIST_ITEM_TYPE_NORMAL_EUROS){
					if(item.get("line_b") == null) item.put("line_b", "0.0");
					line_b.setText(item.get("line_b") + "€");
				}else if(item.get("type") == LIST_ITEM_TYPE_DATE){
					line_b.setText(CuadranteDates.formatDateToHumans2(item.get("line_b")));
				}else{
					line_b.setText(item.get("line_b"));
				}
			
			}
			return v;
		}
	}

	/**
	 * Función que se llama cuando se añade la fecha de inicio o fin
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			String date = CuadranteDates.formatDate(selectedYear, selectedMonth + 1, selectedDay);
			switch (DIALOG_ID_DATE_WHICH) {
			case DIALOG_ID_START_DATE:
				list_items[DIALOG_ID_START_DATE][2] = date;
				break;
			case DIALOG_ID_END_DATE:
				list_items[DIALOG_ID_END_DATE][2] = date;
				break;
			}
			listViewGenerateListAdapter();
		}
	};	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cuadrante.checkSignIn(this);		
	}	
}