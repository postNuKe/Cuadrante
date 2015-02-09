package es.progmac.cuadrante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import es.progmac.cuadrante.info.ComisionInfo;
import es.progmac.cuadrante.info.HotelInfo;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.PromptDialog;
import es.progmac.cuadrante.lib.Sp;

public class ComisionActivity extends SherlockListActivity {
	
	private static final String TAG = "ComisionActivity";
	
	public DatabaseHandler db;
	private int comisionYear;
	private long comision_id = 0;
	private ListAdapter sa;
	private Context mContext;
	/**
	 * identificador para la respuesta de la pantalla de añadir hotel
	 */
	public  int REQUEST_CODE_ADD_HOTEL = 1;
	public  int REQUEST_CODE_EDIT_HOTEL = 2;
	/**
	 * Posición en el array de items
	 * 
	 * @see list_items
	 */
	static final int DIALOG_ID_NAME = 0;
	static final int DIALOG_ID_COMMENTS = 1;
	static final int DIALOG_ID_START_DATE = 2;
	static final int DIALOG_ID_END_DATE = 3;
	static final int DIALOG_ID_VIEW_TOTAL_EXPENSES = 4;
	/**
	 * Número inicial de lineas del list_view, antes de que se carguen los hoteles
	 */
	private int numInitialListItems = 0;
	
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
	 * Item de la lista es normal es decir, dos lineas de texto
	 */
	static final String LIST_ITEM_TYPE_HOTEL = "hotel";	
	/** Item list */
	static final String LIST_ITEM_TYPE_LIST = "list";
	
	
	/**
	 * Array bidimensional con la lista de items a mostrar
	 */
	//private String[][] list_items;
	private ArrayList<String[]> list_items = new ArrayList<String[]>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comision);
		mContext = this;
		db = new DatabaseHandler(this);
		//Cuadrante.checkComisionDefaultValues(this);
		//list_items = new String[][]{
				//type, titulo, valor
		list_items.add(new String[]{ LIST_ITEM_TYPE_NORMAL, this.getString(R.string.list_item_comision_name), "" });
		list_items.add(new String[]{ LIST_ITEM_TYPE_NORMAL, this.getString(R.string.list_item_comments), "" });
		list_items.add(new String[]{ LIST_ITEM_TYPE_DATE, this.getString(R.string.list_item_start_date), "" });
		list_items.add(new String[]{ LIST_ITEM_TYPE_DATE, this.getString(R.string.list_item_end_date), "" });	
		list_items.add(new String[]{ 
				LIST_ITEM_TYPE_LIST, 
				this.getString(R.string.list_item_view_total_expenses), 
				Sp.getCommissionsTotalExpenses(mContext) });
		numInitialListItems = list_items.size();
		
		Intent intent = getIntent();
		
		DateTime today = new DateTime();
		
		comisionYear = intent.getIntExtra("year", today.getYear());
		//editar comision
		if(intent.hasExtra(Extra.COMISION_ID)){
			comision_id = intent.getLongExtra(Extra.COMISION_ID, 0);	
			ComisionInfo comision = db.getComision(comision_id);
			list_items.get(DIALOG_ID_NAME)[2] = comision.getName();
			list_items.get(DIALOG_ID_COMMENTS)[2] = comision.getComments();
			list_items.get(DIALOG_ID_START_DATE)[2] = comision.getStartDate();
			list_items.get(DIALOG_ID_END_DATE)[2] = comision.getEndDate();
			list_items.get(DIALOG_ID_VIEW_TOTAL_EXPENSES)[2] = String.valueOf(comision.getViewTotalExpenses());
			
			List<HotelInfo> hotels = db.getHotelsComision(comision_id);
			String[] tmpHotel;
			for(HotelInfo hotel : hotels){
				//MyLog.d(TAG, "hotel:" + hotel.getId());
				tmpHotel = new String[]{
						LIST_ITEM_TYPE_HOTEL, 			//0
						hotel.getName(), 				//1
						hotel.getComments(), 			//2
						hotel.getStartDate(), 			//3
						hotel.getEndDate(), 			//4
						hotel.getDailyExpenses(), 		//5
						hotel.getManutencionExpenses(),	//6
						hotel.getLaundry(),				//7
				};
				list_items.add(tmpHotel);
			}
		}else{//add comision
			//setTitle(this.getString(R.string.title_activity_comision_add2, comisionYear));
			setTitle(R.string.title_activity_comision_add);
		}

		listViewGenerateListAdapter();
	}



	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_comision, menu);

		return true;
	}    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		View v = getWindow().getDecorView().getRootView();
		switch (item.getItemId()) {
		case R.id.bar_ready:
			onSetData(v);
			return true;
		case R.id.bar_add_hotel:
			onSetAddHotel(v);
			return true;			
		case R.id.bar_delete:
			onSetDelete(v);
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}		
	
	public void onSetData(View v) {		
		if(!list_items.get(DIALOG_ID_NAME)[2].equals("")
				&& !list_items.get(DIALOG_ID_START_DATE)[2].equals("")
				&& !list_items.get(DIALOG_ID_END_DATE)[2].equals("")){
			if(CuadranteDates.getDaysBetweenDates(
					list_items.get(DIALOG_ID_START_DATE)[2], 
					list_items.get(DIALOG_ID_END_DATE)[2]) > 0){
				if(comision_id > 0){//edit
					db.updateComision(new ComisionInfo(
							comision_id, 
							list_items.get(DIALOG_ID_NAME)[2], 
							list_items.get(DIALOG_ID_COMMENTS)[2], 
							list_items.get(DIALOG_ID_START_DATE)[2], 
							list_items.get(DIALOG_ID_END_DATE)[2],
							Integer.parseInt(list_items.get(DIALOG_ID_VIEW_TOTAL_EXPENSES)[2])));
					db.deleteHotelFromComision(comision_id);			
				}else{//insert
					ComisionInfo comision = db.insertComision(new ComisionInfo(
							list_items.get(DIALOG_ID_NAME)[2], 
							list_items.get(DIALOG_ID_COMMENTS)[2], 
							list_items.get(DIALOG_ID_START_DATE)[2], 
							list_items.get(DIALOG_ID_END_DATE)[2],
							Integer.parseInt(list_items.get(DIALOG_ID_VIEW_TOTAL_EXPENSES)[2])));
					comision_id = comision.getId();	
				}
				//MyLog.d(TAG, "numInitialListItems:" + numInitialListItems + " i size:" + list_items.size());
				for(int i = numInitialListItems; i < list_items.size(); i++){
					//MyLog.d(TAG, "0:" + list_items.get(i)[0] + " LIST_ITEM_TYPE_HOTEL:" + LIST_ITEM_TYPE_HOTEL);
					if(list_items.get(i)[0] == LIST_ITEM_TYPE_HOTEL){
						//MyLog.d(TAG, list_items.get(i)[3] + " " + list_items.get(i)[4]);
						db.insertHotel(new HotelInfo(
								comision_id, 
								list_items.get(i)[1],	//name
								list_items.get(i)[2],	//comments
								list_items.get(i)[3],	//start_date
								list_items.get(i)[4],	//end_date
								list_items.get(i)[5],	//daily_expenses
								list_items.get(i)[6],	//manutencion_expenses
								list_items.get(i)[7]	//laundry
								));
					}
				}
				Cuadrante.refreshWidget(this);
				
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				this.finish();
			}else{
				Toast.makeText(this, getString(R.string.error_minus_end_date), Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(this, getString(R.string.error_no_some_data), Toast.LENGTH_LONG).show();
		}
	}



	public void onListItemClick(ListView parent, View v, int position, long id) {
		Resources res = getResources();
		CuadranteDates date;
		switch (position) {
		case DIALOG_ID_NAME:
			PromptDialog dlg = new PromptDialog(this, 
				R.string.list_item_comision_name, 0, list_items.get(DIALOG_ID_NAME)[2], 
				res.getInteger(R.integer.MAX_LENGTH_INPUT_NAME), 
				String.format(this.getString(R.string.hint_max_characters), 
							res.getInteger(R.integer.MAX_LENGTH_INPUT_NAME)), 0) {
				@Override
				public boolean onOkClicked(String input) {
					// do something
					list_items.get(DIALOG_ID_NAME)[2] = input;
					listViewGenerateListAdapter();
					return true; // true = close dialog
				}
			};
			dlg.show();
			break;
		case DIALOG_ID_COMMENTS:
			PromptDialog dlg2 = new PromptDialog(this,
					R.string.title_comments, 0, list_items.get(DIALOG_ID_COMMENTS)[2], 
					res.getInteger(R.integer.MAX_LENGTH_INPUT_COMMENTS), 
					String.format(this.getString(R.string.hint_max_characters),
							res.getInteger(R.integer.MAX_LENGTH_INPUT_COMMENTS)),
					5) {
				@Override
				public boolean onOkClicked(String input) {
					// do something
					list_items.get(DIALOG_ID_COMMENTS)[2] = input;
					listViewGenerateListAdapter();
					return true; // true = close dialog
				}
			};
			dlg2.show();
			break;		
		case DIALOG_ID_START_DATE:
			DIALOG_ID_DATE_WHICH = DIALOG_ID_START_DATE;
			date = new CuadranteDates(list_items.get(DIALOG_ID_START_DATE)[2]);
			new DatePickerDialog(this, mDateSetListener, date.getYear(), date.getMonth() - 1, date.getDay()).show();
			break;
		case DIALOG_ID_END_DATE:
			DIALOG_ID_DATE_WHICH = DIALOG_ID_END_DATE;
			//si no hay fecha fin, ponemos la fecha inicio
			if(list_items.get(DIALOG_ID_END_DATE)[2].length() == 0){
				CuadranteDates tmp_date = new CuadranteDates(list_items.get(DIALOG_ID_START_DATE)[2]);
				date = new CuadranteDates(tmp_date.getDateTime().plusDays(1));				
			}else{
				date = new CuadranteDates(list_items.get(DIALOG_ID_END_DATE)[2]);
			}
			new DatePickerDialog(this, mDateSetListener, date.getYear(), date.getMonth() - 1, date.getDay()).show();
			break;
		default://se supone que entonces son hoteles
			Intent intent = new Intent(this, HotelActivity.class);
			intent.putExtra(Extra.TMP_HOTEL_ID, position);
			intent.putExtra(Extra.HOTEL_ADD, false);
			intent.putExtra(Extra.COMISION_START_DATE, list_items.get(DIALOG_ID_START_DATE)[2]);
			intent.putExtra(Extra.COMISION_END_DATE, list_items.get(DIALOG_ID_END_DATE)[2]);
			intent.putExtra(Extra.NAME, list_items.get(position)[1]);
			intent.putExtra(Extra.COMMENTS, list_items.get(position)[2]);
			intent.putExtra(Extra.START_DATE, list_items.get(position)[3]);
			intent.putExtra(Extra.END_DATE, list_items.get(position)[4]);
			intent.putExtra(Extra.DAILY_EXPENSES, list_items.get(position)[5]);
			intent.putExtra(Extra.MANUTENCION_EXPENSES, list_items.get(position)[6]);
			intent.putExtra(Extra.LAUNDRY, list_items.get(position)[7]);
			startActivityForResult(intent, REQUEST_CODE_EDIT_HOTEL);
			break;
		}
	}
	
	public void onSetDelete(View view) {
		if (comision_id > 0) {
			//alertamos si está seguro de querer eliminar la comisión
		    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    dialog.setMessage(getString(R.string.message_alert_delete_comision));
		    dialog.setCancelable(false);
		    dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {	
		    		db.deleteComision(comision_id);
		    		Cuadrante.refreshWidget(mContext);
		    		//datasource.close();
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					ComisionActivity.this.finish();	
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
	
	public void onSetAddHotel(View view){
		//revisamos que se hayan introducido las fechas
		if(list_items.get(DIALOG_ID_START_DATE)[2].length() > 0 && 
				list_items.get(DIALOG_ID_END_DATE)[2].length() > 0){
			if(list_items.get(DIALOG_ID_START_DATE)[2].equals(
					list_items.get(DIALOG_ID_END_DATE)[2])){
				Toast.makeText(this, R.string.error_equals_start_and_end_date, Toast.LENGTH_LONG).show();
			}else{
				//revisamos si la fecha fin es menor que la fecha inicio
				CuadranteDates sd = new CuadranteDates(list_items.get(DIALOG_ID_START_DATE)[2]);
				CuadranteDates ed = new CuadranteDates(list_items.get(DIALOG_ID_END_DATE)[2]);
				Days d = Days.daysBetween(sd.getDateTime(), ed.getDateTime());
				if(d.getDays() < 0){
					Toast.makeText(this, R.string.error_minus_end_date, Toast.LENGTH_LONG).show();					
				}else{
					Intent intent = new Intent(this, HotelActivity.class);
					intent.putExtra(Extra.TMP_HOTEL_ID, list_items.size());
					intent.putExtra(Extra.HOTEL_ADD, true);
					intent.putExtra(Extra.COMISION_START_DATE, list_items.get(DIALOG_ID_START_DATE)[2]);
					intent.putExtra(Extra.COMISION_END_DATE, list_items.get(DIALOG_ID_END_DATE)[2]);
					intent.putExtra(Extra.NAME, "HOTEL");
					intent.putExtra(Extra.START_DATE, list_items.get(DIALOG_ID_START_DATE)[2]);
					intent.putExtra(Extra.END_DATE, list_items.get(DIALOG_ID_END_DATE)[2]);
					intent.putExtra(Extra.DAILY_EXPENSES, Sp.getDailyExpenses(this));
					intent.putExtra(Extra.MANUTENCION_EXPENSES, Sp.getDailyManutencionExpenses(this));
					intent.putExtra(Extra.LAUNDRY, "0.0");
					startActivityForResult(intent, REQUEST_CODE_ADD_HOTEL);
				}
			}
		}else{
			Toast.makeText(this, R.string.error_no_start_and_end_date, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Regenera el listado de opciones de la comisión
	 */
	public void listViewGenerateListAdapter() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0; i < list_items.size(); i++) {
			item = new HashMap<String, String>();
			item.put("position", String.valueOf(i));
			item.put("type", list_items.get(i)[0]);
			item.put("line_a", list_items.get(i)[1]);
			item.put("line_b", list_items.get(i)[2]);
			if(list_items.get(i)[0].equals(LIST_ITEM_TYPE_HOTEL)){
				item.put("start_date", list_items.get(i)[3]);
				item.put("end_date", list_items.get(i)[4]);
				item.put("daily_expenses", list_items.get(i)[5]);
				item.put("manutencion_expenses", list_items.get(i)[6]);
				item.put("laundry", list_items.get(i)[7]);
			}
			list.add(item);
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
				TextView line_c = (TextView) v.findViewById(R.id.line_c);
				TextView line_d = (TextView) v.findViewById(R.id.line_d);
				TextView line_e = (TextView) v.findViewById(R.id.line_e);
				TextView line_f = (TextView) v.findViewById(R.id.line_f);
				TextView line_g = (TextView) v.findViewById(R.id.line_g);
				TextView line_h = (TextView) v.findViewById(R.id.line_h);
				Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
				
				line_a.setText(item.get("line_a"));
				line_b.setText(item.get("line_b"));
				//hay que poner GONE porque si no la fila de Nombre de la
				//comisión aparecen añadidos los datos del hotel, algo muy raro
				line_c.setVisibility(View.GONE);
				line_d.setVisibility(View.GONE);
				line_e.setVisibility(View.GONE);
				line_f.setVisibility(View.GONE);
				line_g.setVisibility(View.GONE);
				line_h.setVisibility(View.GONE);
				spinner.setVisibility(View.GONE);
				
				if(item.get("type") == LIST_ITEM_TYPE_DATE){
					if(item.get("line_b").length() > 0)
						line_b.setText(CuadranteDates.formatDateToHumans3(item.get("line_b")));
				}else if(item.get("type") == LIST_ITEM_TYPE_LIST){
					line_b.setVisibility(View.GONE);
					spinner.setVisibility(View.VISIBLE);
					String[] spinnerEntries = getResources().getStringArray(R.array.commissions_total_expenses_entries);
				
					spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							list_items.get(DIALOG_ID_VIEW_TOTAL_EXPENSES)[2] = String.valueOf(position);
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});	
					ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(mContext, android.R.layout.simple_spinner_item, spinnerEntries);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
					spinner.setAdapter(adapter);
					spinner.setSelection(Integer.valueOf(item.get("line_b")));					
				}else if(item.get("type") == LIST_ITEM_TYPE_HOTEL){
					CuadranteDates start_date = new CuadranteDates(item.get("start_date"));
					CuadranteDates end_date = new CuadranteDates(item.get("end_date"));
					//Fecha inicio y fecha fin
					line_b.setText(Html.fromHtml("<b>" + getString(R.string.start) + "</b> " 
							+ start_date.formatDateToHumans2() + " "
							+ "<b>" + getString(R.string.end) + "</b> " 
							+ end_date.formatDateToHumans2()));
					
					//dieta diaria
					line_c.setVisibility(View.VISIBLE);
					line_c.setText(Html.fromHtml("<b>" + getString(R.string.daily_expenses2) + "</b> " 
							+ item.get("daily_expenses").concat("€")));
					
					//noches y precio noche
					Days d = Days.daysBetween(new DateTime(start_date.getYear(), 
							start_date.getMonth(), 
							start_date.getDay(), 0, 0), 
							new DateTime(end_date.getYear(), 
									end_date.getMonth(), 
									end_date.getDay(), 0, 0));
					line_d.setVisibility(View.VISIBLE);
					line_d.setText(Html.fromHtml("<b>" + getString(R.string.nights) + "</b> "
							+ d.getDays()));
					/*
					line_d.setText(Html.fromHtml("<b>" + getString(R.string.nights) + "</b> "
							+ d.getDays() + " "
							+ "<b>" + getString(R.string.night_price) + "</b> " + 
							item.get("night_price").concat("€")));
							*/
					
					//precio total
					line_e.setVisibility(View.VISIBLE);
					line_e.setText(Html.fromHtml("<b>" + getString(R.string.total_price) + "</b> " + d.getDays() * Float.parseFloat(item.get("daily_expenses")) + "€"));
					
					//pmanutención
					line_f.setVisibility(View.VISIBLE);
					line_f.setText(Html.fromHtml("<b>" + getString(R.string.manutencion_entera) + "</b> " + d.getDays() * Float.parseFloat(item.get("manutencion_expenses")) + "€"));
					
					//lavandería
					line_g.setVisibility(View.VISIBLE);
					line_g.setText(Html.fromHtml("<b>" + getString(R.string.laundry) + "</b> " + Float.parseFloat(item.get("laundry")) + "€"));					
					
					//comentarios
					if(item.get("line_b") != null){
						if(item.get("line_b").length() > 0){
							line_h.setVisibility(View.VISIBLE);
							line_h.setText(item.get("line_b"));		
						}
					}
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
				list_items.get(DIALOG_ID_START_DATE)[2] = date;
				break;
			case DIALOG_ID_END_DATE:
				list_items.get(DIALOG_ID_END_DATE)[2] = date;
				break;
			}
			listViewGenerateListAdapter();
		}
	};
	
	/**
	 * Cuando nos devuelven datos a esta pantalla
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_ADD_HOTEL || requestCode == REQUEST_CODE_EDIT_HOTEL){
			if(resultCode == RESULT_OK){				
				int tmp_hotel_id = data.getIntExtra(Extra.TMP_HOTEL_ID, list_items.size());
				String name = data.getStringExtra(Extra.NAME);
				String comments = data.getStringExtra(Extra.COMMENTS);
				String start_date = data.getStringExtra(Extra.START_DATE);
				String end_date = data.getStringExtra(Extra.END_DATE);
				String daily_expenses = data.getStringExtra(Extra.DAILY_EXPENSES);
				if(daily_expenses.length() == 0){
					daily_expenses = Sp.getDailyExpenses(this);
				}
				String manutencion_expenses = data.getStringExtra(Extra.MANUTENCION_EXPENSES);
				if(manutencion_expenses.length() == 0){
					manutencion_expenses = Sp.getDailyManutencionExpenses(this);
				}
				String laundry = data.getStringExtra(Extra.LAUNDRY);
				if(laundry.length() == 0) laundry = "0.0";
				String[] hotel = new String[]{
						LIST_ITEM_TYPE_HOTEL, 	//0
						name, 					//1
						comments, 				//2
						start_date, 			//3
						end_date, 				//4
						daily_expenses, 		//5
						manutencion_expenses, 	//6
						laundry,				//7
				};
				if(requestCode == REQUEST_CODE_ADD_HOTEL){
					list_items.add(tmp_hotel_id, hotel);	
				}else if(requestCode == REQUEST_CODE_EDIT_HOTEL){
					list_items.set(tmp_hotel_id, hotel);
				}
			}else if(resultCode == Cuadrante.RESULT_DELETE){
				int tmp_hotel_id = data.getIntExtra(Extra.TMP_HOTEL_ID, list_items.size());
				//existe el hotel ya añadido a la comisión, lo quitamos 
				//del arraylist
				try {
					list_items.remove(tmp_hotel_id);
					//tenemos que ordenar todos los index de los hoteles, para
					//que vayan correlativos
					ArrayList<String[]> copy_list_items = new ArrayList<String[]>();
					copy_list_items.addAll(list_items);
					list_items.clear();
					for(String[] item: copy_list_items){
						list_items.add(item);
					}
				//no existe el hotel, es decir, estaba creándolo nuevo
				} catch ( IndexOutOfBoundsException e ) {
					//list_items.add( index, new Object() );
					
				}
			}
		}
		listViewGenerateListAdapter();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cuadrante.checkSignIn(this);		
	}	
}