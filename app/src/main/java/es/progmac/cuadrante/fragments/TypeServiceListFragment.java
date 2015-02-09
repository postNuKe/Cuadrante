package es.progmac.cuadrante.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

import es.progmac.cuadrante.R;
import es.progmac.cuadrante.TipoServicioActivity;
import es.progmac.cuadrante.TypeServiceHistoryActivity;
import es.progmac.cuadrante.info.TipoServicioInfo;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Extra;

public class TypeServiceListFragment extends SherlockListFragment {
	private Context mContext;
	private static final String TAG = "TypeServiceListFragment";
	public DatabaseHandler db;
	private ListAdapter sa;
	int requestCode;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private int mTypeId = 0;

	public static TypeServiceListFragment newInstance() {
		TypeServiceListFragment fragment = new TypeServiceListFragment();        
        return fragment;
    }	
	
	/**
	 * Necesario un constructor vacío para que no pete cuando se refresca
	 * la pantalla o cuando se gira
	 */
	public TypeServiceListFragment(){
		//MyLog.d("ComisionListFragment", "constructor");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mContext = getActivity();
		db = new DatabaseHandler(mContext);
		

		listViewGenerateListAdapter();

	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View view = inflater.inflate(R.layout.activity_type_service_list, container, false);        
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
	}
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());//se pone aqui y no en onCreate porque si no peta
    }    
    
    public void onListItemClick(ListView parent, View v, int position, long id) {
    	TextView type_id = (TextView) v.findViewById(R.id.type_id);
		mTypeId = Integer.parseInt(type_id.getText().toString());
		getActivity().openContextMenu(v);
    }
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, 1, Menu.NONE, getResources().getString(R.string.edit));
		menu.add(Menu.NONE, 2, Menu.NONE, getResources().getString(R.string.view_history));
	} 
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == 1){
			Intent intent = new Intent(mContext, TipoServicioActivity.class);
			intent.putExtra(Extra.TYPE_ID, mTypeId);
			startActivityForResult(intent, requestCode);			
		}else if(item.getItemId() == 2){
			Intent intent = new Intent(mContext, TypeServiceHistoryActivity.class);			
			intent.putExtra(Extra.TYPE_ID, mTypeId);
			startActivityForResult(intent, requestCode);						
		}
		return true;
	}	
	public void listViewGenerateListAdapter() {
		List<TipoServicioInfo> tipos_servicios = db.getAllTipoServicios();
		// MyLog.d("onCreate", tipos_servicios.toString());
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;

		for (TipoServicioInfo servicio : tipos_servicios) {
			item = new HashMap<String, String>();

			item.put("type_id", String.valueOf(servicio.getId()));
			item.put("bg_color", servicio.getBgColor());
			item.put("text_color", servicio.getTextColor());
			item.put("line_a", servicio.getTitle());

			String str_line_b = String.format("%s |", servicio.getName());
			if(servicio.getIsDateRange() == 1){
				String dateRange = getResources().getString(R.string.date_range);		
				str_line_b = str_line_b + String.format(" %s |", dateRange);
			}	
			if(servicio.getTypeDay() > 0){
				switch (servicio.getTypeDay()) {
				case 1:
					str_line_b = str_line_b + String.format(" %s |", getString(R.string.resume_type_day_1));						
					break;
				case 2:
					str_line_b = str_line_b + String.format(" %s |", getString(R.string.resume_type_day_2));						
					break;
				case 3:
					str_line_b = str_line_b + String.format(" %s |", getString(R.string.resume_type_day_3));						
					break;					
				}
			}
			if(servicio.getGuardiaCombinada() > 0){
				str_line_b = str_line_b + 
						String.format(" %s |", Cuadrante.GUARDIAS_COMBINADAS.get(servicio.getGuardiaCombinada()));				
			}
			if(servicio.getAskSchedule() == 1){
				str_line_b = str_line_b + 
						String.format(" %s |", getString(R.string.ask_schedule));				
			}
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
									+ String.format(" %s | %s |",
											servicio.getStartSchedule4(),
											servicio.getEndSchedule4());
						}

					}
				}
			}
			item.put("line_b", str_line_b);
			list.add(item);
		}
		sa = new TypeServiceItemAdapter(mContext, android.R.layout.simple_list_item_1, list);
		setListAdapter(sa);
	}

	public class TypeServiceItemAdapter extends ArrayAdapter<HashMap<String, String>> {
		private ArrayList<HashMap<String, String>> items;

		public TypeServiceItemAdapter(Context context, int textViewResourceId,
				ArrayList<HashMap<String, String>> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			// MyLog.d("servicioItemAdapter", "array: " + items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) 
						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.tipo_servicio_list_item_1, null);
			}

			HashMap<String, String> item = items.get(position);
			if (item != null) {
				TextView type_id = (TextView) v.findViewById(R.id.type_id);
				TextView line_a = (TextView) v.findViewById(R.id.line_a);
				TextView line_b = (TextView) v.findViewById(R.id.line_b);
				LinearLayout layout_item = (LinearLayout) v
						.findViewById(R.id.listview_item);

				type_id.setText(item.get("type_id"));
				line_a.setText(item.get("line_a"));
				line_a.setTextColor(Integer.parseInt(item.get("text_color")));
				line_b.setText(item.get("line_b"));
				line_b.setTextColor(Integer.parseInt(item.get("text_color")));
				layout_item.setBackgroundColor(Integer.parseInt(item
						.get("bg_color")));

			}
			return v;
		}
	}

	
}