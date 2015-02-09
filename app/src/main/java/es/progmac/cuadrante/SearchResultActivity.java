package es.progmac.cuadrante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;


import es.progmac.cuadrante.R;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.MyLog;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultActivity extends ListActivity {
	private static final String TAG = "SearchResult";
	public DatabaseHandler db;
	private ListAdapter sa;
	int requestCode;
	private String mDateSelected;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        registerForContextMenu(getListView());
        db = new DatabaseHandler(this);
        listViewGenerateListAdapter();
    }
    
	public void listViewGenerateListAdapter() {
        //cogemos los datos pasados por la anterior pantalla
        Intent intent = getIntent();
        String searchString = intent.getStringExtra(Extra.SEARCH_STRING);
		List<ServicioInfo> servicios = db.getServicesFromSearchComments(
				searchString, 
				intent.getStringExtra(Extra.SEARCH_DATE_START),
				intent.getStringExtra(Extra.SEARCH_DATE_END), 
				intent.getIntArrayExtra(Extra.TYPES_SERVICES));     

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		
		for (ServicioInfo servicio : servicios) {
			item = new HashMap<String, String>();

			item.put("date", servicio.getDate());
			item.put("bg_color", servicio.getBgColor());
			item.put("text_color", servicio.getTextColor());
			DateTime dt = new DateTime(servicio.getYear(), servicio.getMonth(),
					servicio.getDay(), 0, 0);
			item.put("line_a", dt.toString("d 'de' MMMM 'de' y"));
			String str_line_b = String.format("%s |", servicio.getName());
			
			if (!servicio.getStartSchedule().equals(Cuadrante.SCHEDULE_NULL)
					|| !servicio.getEndSchedule().equals(Cuadrante.SCHEDULE_NULL)) {
				str_line_b = str_line_b
						+ String.format(" %s | %s |",
								servicio.getStartSchedule(),
								servicio.getEndSchedule());
				if (!servicio.getStartSchedule2().equals(Cuadrante.SCHEDULE_NULL)
						|| !servicio.getEndSchedule2().equals(Cuadrante.SCHEDULE_NULL)) {
					str_line_b = str_line_b
							+ String.format(" %s | %s |",
									servicio.getStartSchedule2(),
									servicio.getEndSchedule2());
					if (!servicio.getStartSchedule3().equals(Cuadrante.SCHEDULE_NULL)
							|| !servicio.getEndSchedule3().equals(Cuadrante.SCHEDULE_NULL)) {
						str_line_b = str_line_b
								+ String.format(" %s | %s |",
										servicio.getStartSchedule3(),
										servicio.getEndSchedule3());
						if (!servicio.getStartSchedule4().equals(Cuadrante.SCHEDULE_NULL)
								|| !servicio.getEndSchedule4().equals(Cuadrante.SCHEDULE_NULL)) {
							str_line_b = str_line_b
									+ String.format(" %s | %s ",
											servicio.getStartSchedule4(),
											servicio.getEndSchedule4());
						}

					}
				}
			}
			item.put("line_b", str_line_b);
			
			//mostramos la parte donde se muestra la cadena buscada en los 
			//comentarios del servicio con unos cuantos caracteres antes y 
			//otros despues con ...
			String str_line_c = "";
			int NUM_CHARACTERS_BEFORE = 30, NUM_CHARACTERS_AFTER = 30;
			int index = servicio.getComments().toLowerCase().indexOf(searchString.toLowerCase());
			//MyLog.d(TAG, "string:" + searchString + " " + index);
			int start = 0, end = index + searchString.length() + NUM_CHARACTERS_AFTER;
			if(index != -1){//si sale -1 es que no lo encontro
				if(index > NUM_CHARACTERS_BEFORE) {
					start = index - NUM_CHARACTERS_BEFORE;
				}				
				if(servicio.getComments().length() < 
						index + searchString.length() + NUM_CHARACTERS_AFTER){
					end = servicio.getComments().length();
				}
				str_line_c = servicio.getComments().substring(start, end);
			}
			item.put("line_c", str_line_c);
			list.add(item);
		}
		if(servicios.size() > 0){
			sa = new SearchServiceItemAdapter(this,
					android.R.layout.simple_list_item_1, list);
			setListAdapter(sa);
		}else{
			RelativeLayout count_results = (RelativeLayout) findViewById(R.id.count_results);
			count_results.setVisibility(View.VISIBLE);
		}
	}

	public class SearchServiceItemAdapter extends
			ArrayAdapter<HashMap<String, String>>{
		private ArrayList<HashMap<String, String>> items;

		public SearchServiceItemAdapter(Context context, int textViewResourceId,
				ArrayList<HashMap<String, String>> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			// MyLog.d("servicioItemAdapter", "array: " + items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.search_result_list_item_1, null);
			}
			HashMap<String, String> item = items.get(position);
			if (item != null) {
				TextView date = (TextView) v.findViewById(R.id.date);
				TextView line_a = (TextView) v.findViewById(R.id.line_a);
				TextView line_b = (TextView) v.findViewById(R.id.line_b);
				TextView line_c = (TextView) v.findViewById(R.id.line_c);
				LinearLayout layout_item = (LinearLayout) v
						.findViewById(R.id.listview_item);
				
				date.setText(item.get("date"));
				line_a.setText(item.get("line_a"));
				//line_a.setTextColor(Integer.parseInt(item.get("text_color")));
				line_b.setText(item.get("line_b"));
				//line_b.setTextColor(Integer.parseInt(item.get("text_color")));
				line_c.setText(item.get("line_c"));
				//line_c.setTextColor(Integer.parseInt(item.get("text_color")));
				//layout_item.setBackgroundColor(Integer.parseInt(item.get("bg_color")));

			}
			return v;
		}
			
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		/*
		Intent intent = new Intent(this, ServicioActivity.class);
		TextView date = (TextView) v.findViewById(R.id.date);
		intent.putExtra("date", date.getText().toString());
		startActivityForResult(intent, requestCode);
		*/
		TextView date = (TextView) v.findViewById(R.id.date);
		mDateSelected = date.getText().toString();
		openContextMenu(v);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, 1, Menu.NONE, getResources().getString(R.string.enter_the_service));
		menu.add(Menu.NONE, 2, Menu.NONE, getResources().getString(R.string.jump_to_month));
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == 1){
			Intent intent = new Intent(this, ServicioActivity.class);
			//TextView date = (TextView) v.findViewById(R.id.date);
			intent.putExtra("date", mDateSelected);
			startActivityForResult(intent, requestCode);			
		}else if(item.getItemId() == 2){
			Intent intent = new Intent(this, MainActivity.class);
			//TextView date = (TextView) v.findViewById(R.id.date);
			CuadranteDates date = new CuadranteDates(mDateSelected);
			
			intent.putExtra(Extra.DAY, date.getDay());
			intent.putExtra(Extra.MONTH, date.getMonth() - 1);
			intent.putExtra(Extra.YEAR, date.getYear());
			startActivityForResult(intent, requestCode);						
		}
		return true;
	}	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_result, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intentSettings = new Intent(this, SettingsActivity.class);
			startActivity(intentSettings);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}    
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cuadrante.checkSignIn(this);		
	}	
}