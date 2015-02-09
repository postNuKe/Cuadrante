package es.progmac.cuadrante;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
import es.progmac.cuadrante.info.TipoServicioInfo;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.PromptDialog;
import es.progmac.cuadrante.lib.Sp;

public class SearchActivity extends SherlockListActivity {
	
	private static final String TAG = "SearchActivity";
	
	public DatabaseHandler db;
	private ListAdapter sa;
	private Context mContext;
	/**
	 * Posición en el array de items
	 * 
	 * @see list_items
	 */
	static final int DIALOG_ID_TEXT = 0;
	static final int DIALOG_ID_DATE_START = 1;
	static final int DIALOG_ID_DATE_END = 2;
	static final int DIALOG_ID_LIST_TYPE = 3;
	
	/**
	 * Variable que guardará qué DatePicker, si fecha inicio o fin, debe de
	 * mostrar
	 */
	static int DIALOG_ID_DATE_WHICH = DIALOG_ID_DATE_START;		
	/**
	 * Item de la lista es normal es decir, dos lineas de texto
	 */
	static final String LIST_ITEM_TYPE_NORMAL = "normal";	
	/**
	 * Para definir que el valor debe convertirse a formato de fecha
	 */
	static final String LIST_ITEM_TYPE_DATE = "date";	
	/** Item list */
	static final String LIST_ITEM_TYPE_LIST = "list";
	
	List<TipoServicioInfo> types = new ArrayList<TipoServicioInfo>();
	protected CharSequence[] _options;
	protected boolean[] _selections;
	
	
	/**
	 * Array bidimensional con la lista de items a mostrar
	 */
	//private String[][] list_items;
	private ArrayList<String[]> list_items = new ArrayList<String[]>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		mContext = this;
		db = new DatabaseHandler(this);
		types = db.getAllTipoServicios();
		resetForm();
	}
	
	public void resetForm(){
		list_items = new ArrayList<String[]>();
		//list_items = new String[][]{
		//type, titulo, valor
		list_items.add(new String[]{ 
				LIST_ITEM_TYPE_NORMAL, this.getString(R.string.list_item_text), "" });
		list_items.add(new String[]{ 
				LIST_ITEM_TYPE_DATE, this.getString(R.string.list_item_start_date), "" });
		list_items.add(new String[]{ 
				LIST_ITEM_TYPE_DATE, this.getString(R.string.list_item_end_date), "" });	
		list_items.add(new String[]{ 
				LIST_ITEM_TYPE_LIST, 
				this.getString(R.string.list_item_types_service), getString(R.string.all) });
		int i=0;
		_options = new String[types.size()];
		for(TipoServicioInfo type : types){
			_options[i] = type.getTitle();
			i++;
		}
		_selections = new boolean[ _options.length ];		
		listViewGenerateListAdapter();		
	}



	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.search, menu);

		return true;
	}    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		View v = getWindow().getDecorView().getRootView();
		switch (item.getItemId()) {
		case R.id.bar_search:
			if(list_items.get(DIALOG_ID_TEXT)[2].length() > 0){
				Intent intent = new Intent(mContext, SearchResultActivity.class);
				intent.putExtra(Extra.SEARCH_STRING, list_items.get(DIALOG_ID_TEXT)[2]);
				intent.putExtra(Extra.SEARCH_DATE_START, list_items.get(DIALOG_ID_DATE_START)[2]);
				intent.putExtra(Extra.SEARCH_DATE_END, list_items.get(DIALOG_ID_DATE_END)[2]);
				int[] typesExtraTmp = new int[types.size()];
				int j = 0;
				for(int i = 0; i < _options.length; i++ ){
					//Log.i( "reset", _options[i] + " selected: " + _selections[i] );
					if(_selections[i]){
						//MyLog.d(TAG, "id:" + types.get(i).getId());
						typesExtraTmp[i] = types.get(i).getId();
						j++;
					}					
				}
				int[] typesExtra = new int[j];
				j = 0;
				for(int i = 0; i < typesExtraTmp.length; i++){
					if(typesExtraTmp[i] > 0){
						//MyLog.d(TAG, "final id:" + types.get(i).getId());
						typesExtra[j] = typesExtraTmp[i];
						j++;
					}
				}
				intent.putExtra(Extra.TYPES_SERVICES, typesExtra);
				startActivity(intent);
			}else{//no ha introducido texto.
				Toast.makeText(mContext, R.string.error_no_input_search, Toast.LENGTH_LONG).show();
			}
			return true;
		case R.id.bar_clear:
			resetForm();
			return true;						
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		Resources res = getResources();
		CuadranteDates date;
		switch (position) {
		case DIALOG_ID_TEXT:
			PromptDialog dlg = new PromptDialog(this, 
				R.string.list_item_text, 0, list_items.get(DIALOG_ID_TEXT)[2], 
				res.getInteger(R.integer.MAX_LENGTH_INPUT_NAME), 
				String.format(this.getString(R.string.hint_max_characters), 
							res.getInteger(R.integer.MAX_LENGTH_INPUT_NAME)), 0) {
				@Override
				public boolean onOkClicked(String input) {
					// do something
					list_items.get(DIALOG_ID_TEXT)[2] = input;
					listViewGenerateListAdapter();
					return true; // true = close dialog
				}
			};
			dlg.show();
			break;	
		case DIALOG_ID_DATE_START:
			DIALOG_ID_DATE_WHICH = DIALOG_ID_DATE_START;
			date = new CuadranteDates(list_items.get(DIALOG_ID_DATE_START)[2]);
			new DatePickerDialog(
					mContext, mDateSetListener, date.getYear(), date.getMonth() - 1, date.getDay()).show();
			break;
		case DIALOG_ID_DATE_END:
			DIALOG_ID_DATE_WHICH = DIALOG_ID_DATE_END;
			//si no hay fecha fin, ponemos la fecha inicio
			if(list_items.get(DIALOG_ID_DATE_END)[2].length() == 0){
				CuadranteDates tmp_date = new CuadranteDates(list_items.get(DIALOG_ID_DATE_END)[2]);
				date = new CuadranteDates(tmp_date.getDateTime().plusDays(1));				
			}else{
				date = new CuadranteDates(list_items.get(DIALOG_ID_DATE_END)[2]);
			}
			new DatePickerDialog(
					mContext, mDateSetListener, date.getYear(), date.getMonth() - 1, date.getDay()).show();
			break;
		case DIALOG_ID_LIST_TYPE:
			showDialog(DIALOG_ID_LIST_TYPE);
			break;
		}
	}
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ID_LIST_TYPE:
			return new AlertDialog.Builder(mContext)
	        	.setTitle(R.string.dialog_title_types_services)
	        	.setMultiChoiceItems( _options, _selections, new DialogSelectionClickHandler() )
	        	.setPositiveButton(R.string.ok, new DialogButtonClickHandler() )
	        	.create();
		}
		return null;
	}
	
	public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener{
		public void onClick( DialogInterface dialog, int clicked, boolean selected ){
			//Log.i( "SelectionClick", _options[ clicked ] + " selected: " + selected );
		}
	}
	

	public class DialogButtonClickHandler implements DialogInterface.OnClickListener{
		public void onClick( DialogInterface dialog, int clicked ){
			switch( clicked ){
				case DialogInterface.BUTTON_POSITIVE:
					list_items.get(DIALOG_ID_LIST_TYPE)[2] = "";
					for( int i = 0; i < _options.length; i++ ){
						Log.i( "print", _options[ i ] + " selected: " + _selections[i] );
						if(_selections[i]){
							list_items.get(DIALOG_ID_LIST_TYPE)[2] += _options[i] + ", ";
						}
					}	
					if(list_items.get(DIALOG_ID_LIST_TYPE)[2].length() == 0){
						list_items.get(DIALOG_ID_LIST_TYPE)[2] = getString(R.string.all);
					}
					//para que se vuelva a generar la ventana de nuevo con los valores
					//si no, entonces si reseteamos el formulario y volver a abrir el dialog
					//vuelven a salir las opciones chekeadas de la anterior vez
					removeDialog(DIALOG_ID_LIST_TYPE);
					listViewGenerateListAdapter();
					break;
			}
		}
	}

	/**
	 * Regenera el listado
	 */
	public void listViewGenerateListAdapter() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0; i < list_items.size(); i++) {
			item = new HashMap<String, String>();
			item.put("type", list_items.get(i)[0]);
			item.put("line_a", list_items.get(i)[1]);
			item.put("line_b", list_items.get(i)[2]);
			list.add(item);
		}
		sa = new SearchItemAdapter(this, android.R.layout.simple_list_item_1,
				list);
		setListAdapter(sa);
	}

	/**
	 * Maneja como se debe de mostrar cada item de la lista de opciones
	 */
	public class SearchItemAdapter extends
			ArrayAdapter<HashMap<String, String>> {
		private ArrayList<HashMap<String, String>> items;
		final ListView lv;

		public SearchItemAdapter(Context context, int textViewResourceId,
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
				v = vi.inflate(R.layout.search_list_item_1, null);
			}
			

			final HashMap<String, String> item = items.get(position);
			if (item != null) {
				TextView line_a = (TextView) v.findViewById(R.id.line_a);
				TextView line_b = (TextView) v.findViewById(R.id.line_b);
				
				line_a.setText(item.get("line_a"));
				line_b.setText(item.get("line_b"));
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
			case DIALOG_ID_DATE_START:
				list_items.get(DIALOG_ID_DATE_START)[2] = date;
				break;
			case DIALOG_ID_DATE_END:
				list_items.get(DIALOG_ID_DATE_END)[2] = date;
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