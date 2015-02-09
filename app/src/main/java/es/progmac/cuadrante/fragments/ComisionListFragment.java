package es.progmac.cuadrante.fragments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
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

import es.progmac.cuadrante.ComisionActivity;
import es.progmac.cuadrante.ComisionListActivity;
import es.progmac.cuadrante.MainActivity;
import es.progmac.cuadrante.R;
import es.progmac.cuadrante.TipoServicioActivity;
import es.progmac.cuadrante.TypeServiceHistoryActivity;
import es.progmac.cuadrante.info.ComisionInfo;
import es.progmac.cuadrante.info.HotelInfo;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.cuadrante.lib.Comission;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.Sp;

public class ComisionListFragment extends SherlockListFragment {
	private Context mContext;
	private int year = 0;
	private static final String KEY_YEAR = "ComisionList:Year";
	private static final String TAG = "ComisionListFragment";
	public DatabaseHandler db;
	private ListAdapter sa;
	List<ComisionInfo> comisions;
	private int mPosition = 0;
	int requestCode;
	
	public static ComisionListFragment newInstance(int year2) {
		ComisionListFragment fragment = new ComisionListFragment();
        
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(KEY_YEAR, year2);
        fragment.setArguments(args);
		//MyLog.d("ComisionListFragment", "newInstance");

        
        return fragment;
    }	
	
	/**
	 * Necesario un constructor vacío para que no pete cuando se refresca
	 * la pantalla o cuando se gira
	 */
	public ComisionListFragment(){
		//MyLog.d("ComisionListFragment", "constructor");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//MyLog.d("ComisionListFragment", "onCreate");
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_YEAR)) {
            year = savedInstanceState.getInt(KEY_YEAR);
        }else{
        	year = getArguments() != null ? getArguments().getInt(KEY_YEAR) : 0;
        }
        mContext = getActivity();
		db = new DatabaseHandler(mContext);
		comisions = db.getComisionInYear(year);
		listViewGenerateListAdapter();

	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//MyLog.d("ComisionListFragment", "oncreateView year:" + year);
		//comisions = db.getComisionInYear(year);
		
		View view = inflater.inflate(R.layout.activity_comision_list, container, false);        
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		TextView txtCount = (TextView) getView().findViewById(R.id.countCommissions);
		txtCount.setText(getString(R.string.number_comissions) + " " + String.valueOf(comisions.size()));
		LinearLayout line_length = (LinearLayout) getView().findViewById(R.id.resume_line_b);
		int daysBetween = 0, totalDays = 0, daysLarge = 0, daysShort = 0;
		for(ComisionInfo comision : comisions){
			daysBetween = CuadranteDates.getDaysBetweenDates(
					comision.getStartDate(), comision.getEndDate()) + 1;
			totalDays += daysBetween;
			if(daysBetween <= Sp.getCommissionsDaysLength(mContext)){
				daysShort += daysBetween;
			}else{
				daysLarge += daysBetween;
			}
		}
		TextView txtDays = (TextView) getView().findViewById(R.id.totalDays);
		txtDays.setText(getString(R.string.commission_days) + " " + totalDays);
		TextView txtDaysLarge = (TextView) getView().findViewById(R.id.largeDays);
		txtDaysLarge.setText(getString(R.string.commission_large_days) + " " + daysLarge);
		TextView txtDaysShort = (TextView) getView().findViewById(R.id.shortDays);
		txtDaysShort.setText(getString(R.string.commission_short_days) + " " + daysShort);
		if(Sp.getCommissionsDaysLengthActive(mContext)){
			line_length.setVisibility(View.VISIBLE);
		}else{
			line_length.setVisibility(View.GONE);			
		}
		
	}
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_YEAR, year);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }    
    /*
    public void onListItemClick(ListView parent, View v, int position, long id) {
    	Intent intent = new Intent(mContext, ComisionActivity.class);
    	intent.putExtra(Extra.COMISION_ID, comisions.get(position).getId());
    	startActivityForResult(intent, ComisionListActivity.REQUEST_CODE_EDIT_COMISION);
    }
    */
	public void onListItemClick(ListView parent, View v, int position, long id) {
		mPosition = position;
		getActivity().openContextMenu(v);
	}	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, 1, Menu.NONE, getResources().getString(R.string.edit));
		menu.add(Menu.NONE, 2, Menu.NONE, getResources().getString(R.string.jump_to_month));
	}	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == 1){
			Intent intent = new Intent(mContext, ComisionActivity.class);
			intent.putExtra(Extra.COMISION_ID, comisions.get(mPosition).getId());
			startActivityForResult(intent, ComisionListActivity.REQUEST_CODE_EDIT_COMISION);			
		}else if(item.getItemId() == 2){
			Intent intent = new Intent(mContext, MainActivity.class);	
			intent.putExtra(Extra.DAY, comisions.get(mPosition).getStartDay());
			intent.putExtra(Extra.MONTH, comisions.get(mPosition).getStartMonth() - 1);
			intent.putExtra(Extra.YEAR, comisions.get(mPosition).getStartYear());
			startActivity(intent);	
			getActivity().finish();
		}
		return true;
	}    
	public void listViewGenerateListAdapter() {
		// MyLog.d("onCreate", tipos_servicios.toString());
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		double total_expenses = 0, total_laundry = 0, eighty = 0, twenty = 0, 
				manutencion_expenses = 0, manutencionStart = 0, manutencionEnd = 0;
		int hotelDays = 0;
		for (ComisionInfo comision : comisions) {
			item = new HashMap<String, String>();
			total_expenses = 0;
			total_laundry = 0;
			eighty = 0;
			twenty = 0;
			hotelDays = 0;
			manutencion_expenses = 0;
			List<HotelInfo> hotels = db.getHotelsComision(comision.getId());
			for(HotelInfo hotel : hotels){
				hotelDays = CuadranteDates.getDaysBetweenDates(hotel.getStartDate(), hotel.getEndDate());
				total_expenses += hotelDays * Float.parseFloat(hotel.getDailyExpenses());
				total_laundry += Float.parseFloat(hotel.getLaundry());
				double tmp_manutencion = Float.parseFloat(hotel.getManutencionExpenses());
				if(tmp_manutencion > 0) manutencion_expenses = tmp_manutencion; 
			}
			//MyLog.d(TAG, "manutencion:" + manutencion_expenses);
			//servicios en esa comisión
			HashMap<String, Integer> ls_services = new HashMap<String, Integer>();
			List<ServicioInfo> services = db.getServicesFromInterval(
					comision.getStartDate(), comision.getEndDate());
			int i = 0;
			for(ServicioInfo service : services){
				Comission comission = new Comission(service);
				if(i == 0){//primer servicio de la comision
					switch (comission.hasManutencionStartDay()) {
					case 0://no tiene derecho
						//total_expenses -= manutencion_expenses;
						manutencionStart = manutencion_expenses;
						break;
					case 1://media
						//total_expenses -= manutencion_expenses/2;
						manutencionStart = manutencion_expenses/2;
						break;
					}
					//MyLog.d(TAG, "manutencion ida:" + comission.hasManutencionStartDay() + " " + manutencionStart);
				}else if(i > 0 && i == services.size() - 1){
					switch (comission.hasManutencionEndDay()) {
					case 1://media
					case 2://entera
						//total_expenses += manutencion_expenses/2;
						manutencionEnd = manutencion_expenses/2;
						break;
					}					
					//MyLog.d(TAG, "fin:" + comission.hasManutencionEndDay() + " " + manutencionEnd);
				}
				int qty = 1;
				if(ls_services.containsKey(service.getName())){
					qty = ls_services.get(service.getName()) + 1;
				}
				ls_services.put(service.getName(), qty);
				//MyLog.d("service in comision", service.getName() + ":" + qty);
				i++;
			}
			String services_str = "";
			for (Map.Entry<String, Integer> entry : ls_services.entrySet()) {
				services_str = services_str.concat(entry.getKey() + ": " + entry.getValue() + " | ");
				//MyLog.d("entries", "entries:" + entry.getKey() + ": " + entry.getValue());
			}
			//MyLog.d("listViewGenerateListAdapter", "services_str:" + services_str);
			
			eighty = total_expenses * 0.8 - manutencionStart;
			twenty = total_expenses * 0.2 + total_laundry + manutencionEnd;
			item.put("line_a", comision.getName());
			item.put("view_total_expenses", String.valueOf(comision.getViewTotalExpenses()));
			item.put("days", String.valueOf(CuadranteDates.getDaysBetweenDates(comision.getStartDate(), comision.getEndDate()) + 1));
			item.put("total", String.valueOf(new DecimalFormat("##.##").
					format(total_expenses + total_laundry - manutencionStart + manutencionEnd)));
			item.put("eighty", String.valueOf(new DecimalFormat("##.##").format(eighty)));
			item.put("twenty", String.valueOf(new DecimalFormat("##.##").format(twenty)));
			item.put("start_date", CuadranteDates.formatDateToHumans2(comision.getStartDate()));
			item.put("end_date", CuadranteDates.formatDateToHumans2(comision.getEndDate()));
			item.put("services", services_str);
			list.add(item);
		}
		sa = new ComisionItemAdapter(mContext,
				android.R.layout.simple_list_item_1, list);
		setListAdapter(sa);
	}
	
	public class ComisionItemAdapter extends
	ArrayAdapter<HashMap<String, String>> {
		private ArrayList<HashMap<String, String>> items;

		public ComisionItemAdapter(Context context, int textViewResourceId,
				ArrayList<HashMap<String, String>> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			
			// MyLog.d("servicioItemAdapter", "array: " + items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.comision_list_item_2, null);
			}
			int colorPos = position % 2;
			if(colorPos == 0){
				v.setBackgroundResource(R.color.commission_list_item_on);
			}else{
				v.setBackgroundResource(R.color.commission_list_item_off);	
			}

			HashMap<String, String> item = items.get(position);
			if (item != null) {
				TextView line_a = (TextView) v.findViewById(R.id.line_a);
				TextView line_b_a = (TextView) v.findViewById(R.id.line_b_a);
				TextView line_b_b = (TextView) v.findViewById(R.id.line_b_b);
				TextView line_c_a = (TextView) v.findViewById(R.id.line_c_a);
				TextView line_c_b = (TextView) v.findViewById(R.id.line_c_b);
				TextView line_d = (TextView) v.findViewById(R.id.line_d);

				line_a.setText(item.get("line_a"));
				line_b_a.setText(Html.fromHtml("<b>" + getString(R.string.start)
						+ "</b> " + item.get("start_date") + " <b>" + getString(R.string.end)
						+ "</b> " + item.get("end_date")));
				line_b_b.setText(Html.fromHtml("<b>" + getString(R.string.days)
						+ "</b> " + item.get("days")));
				if(item.get("view_total_expenses").equals("0")){//ver total
					line_c_b.setVisibility(View.GONE);
					line_c_a.setText(Html.fromHtml("<b>" + getString(R.string.commissions_total)
							+ "</b> " + item.get("total") + "€"));					
				}else{
					line_c_a.setText(Html.fromHtml("<b>" + getString(R.string.eighty_percent)
							+ "</b> " + item.get("eighty") + "€"));
					line_c_b.setVisibility(View.VISIBLE);
					line_c_b.setText(Html.fromHtml("<b>" + getString(R.string.twenty_percent)
							+ "</b> " + item.get("twenty") + "€"));
				}
				
				if(!item.get("services").equals("")){
					line_d.setVisibility(View.VISIBLE);
					line_d.setText(item.get("services"));
				}

			}
			return v;
		}
	} 
}