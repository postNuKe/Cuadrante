package es.progmac.cuadrante.fragments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

import es.progmac.cuadrante.ComisionListActivity;
import es.progmac.cuadrante.R;
import es.progmac.cuadrante.fragments.ComisionListFragment.ComisionItemAdapter;
import es.progmac.cuadrante.info.ComisionInfo;
import es.progmac.cuadrante.info.HotelInfo;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.Sp;

public class ComisionHistoryFragment extends SherlockListFragment {
	private Context mContext;
	private static final String KEY_YEAR = "ComisionHistory:Year";
	private DatabaseHandler db;
	private ListAdapter sa;
	public List<ComisionInfo> commissions;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public int year;
	
	public static ComisionHistoryFragment newInstance(int year2) {
		ComisionHistoryFragment fragment = new ComisionHistoryFragment();
        
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(KEY_YEAR, year2);
        fragment.setArguments(args);
		MyLog.d("ComisionHistoryFragment", "newInstance");

        
        return fragment;
    }	
	
	/**
	 * Necesario un constructor vacío para que no pete en algunos momentos
	 */	
	public ComisionHistoryFragment() {
		
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.d("ComisionHistoryFragment", "onCreate");
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_YEAR)) {
            year = savedInstanceState.getInt(KEY_YEAR);
        }else{
        	year = getArguments() != null ? getArguments().getInt(KEY_YEAR) : 0;
        }
        mContext = getActivity();
		db = new DatabaseHandler(mContext);
		commissions = db.getAllCommissions();
		listViewGenerateListAdapter();

	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//MyLog.d("onCreateView", "comisionHistoryFragment");
		//commissions = db.getAllCommissions();
		View view = inflater.inflate(R.layout.fragment_comision_history, container, false);        
		return view;	
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_YEAR, year);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}    

	public void onListItemClick(ListView parent, View v, int position, long id) {
		int yearClicked = Integer.valueOf(list.get(position).get("year"));
		if(yearClicked != year){
	    	Intent intent = new Intent(mContext, ComisionListActivity.class);
	    	intent.putExtra("year", yearClicked);
	    	startActivity(intent);
	    	getActivity().finish();
		}
	}

	public void listViewGenerateListAdapter() {
		HashMap<String, String> item;
		int totalDays = 0, year = 0, count = 0;
		int betweenDays = 0, largeDays = 0, shortDays = 0;
		ComisionInfo commission;
		for (int i = 0; i < commissions.size(); i++){
			item = new HashMap<String, String>();
			commission = commissions.get(i);
			if(year == 0){
				year = commission.getStartYear();
			}
						
			//significa que la comisión actual es del mismo año que la anterior
			if(year == commission.getStartYear()){
				count++;
				betweenDays = CuadranteDates.getDaysBetweenDates(
						commission.getStartDate(), commission.getEndDate()) + 1;
				totalDays += betweenDays;
				if(betweenDays <= Sp.getCommissionsDaysLength(mContext)){
					shortDays += betweenDays;
				}else{
					largeDays += betweenDays;
				}				
				//es el ultimo registro
				if(i == commissions.size() - 1){
					item.put("year", String.valueOf(year));
					item.put("count", String.valueOf(count));
					item.put("totalDays", String.valueOf(totalDays));
					item.put("largeDays", String.valueOf(largeDays));
					item.put("shortDays", String.valueOf(shortDays));
					list.add(item);					
				}
			}else{//hemos cambiado de año
				item.put("year", String.valueOf(year));
				item.put("count", String.valueOf(count));
				item.put("totalDays", String.valueOf(totalDays));
				item.put("largeDays", String.valueOf(largeDays));
				item.put("shortDays", String.valueOf(shortDays));
				list.add(item);
				
				year = commission.getStartYear();
				count = 1;
				largeDays = 0;
				shortDays = 0;
				betweenDays = CuadranteDates.getDaysBetweenDates(
						commission.getStartDate(), commission.getEndDate()) + 1;
				totalDays = betweenDays;
				if(betweenDays <= Sp.getCommissionsDaysLength(mContext)){
					shortDays = betweenDays;
				}else{
					largeDays = betweenDays;
				}				
				//es el ultimo registro
				if(i == commissions.size() - 1){
					item = new HashMap<String, String>();
					item.put("year", String.valueOf(year));
					item.put("count", String.valueOf(count));
					item.put("totalDays", String.valueOf(totalDays));
					item.put("largeDays", String.valueOf(largeDays));
					item.put("shortDays", String.valueOf(shortDays));
					list.add(item);					
				}
			}
		}
		sa = new ComisionHistoryItemAdapter(mContext,
				android.R.layout.simple_list_item_1, list);
		setListAdapter(sa);
	}
	

	public class ComisionHistoryItemAdapter extends
	ArrayAdapter<HashMap<String, String>> {
		private ArrayList<HashMap<String, String>> items;

		public ComisionHistoryItemAdapter(Context context, int textViewResourceId,
				ArrayList<HashMap<String, String>> items) {
			super(context, textViewResourceId, items);
			this.items = items;

			//MyLog.d("servicioItemAdapter", "array: " + items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.comision_history_list_item_1, null);
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
				TextView countCommissions = (TextView) v.findViewById(R.id.countCommissions);
				TextView totalDays = (TextView) v.findViewById(R.id.totalDays);
				TextView largeDays = (TextView) v.findViewById(R.id.largeDays);
				TextView shortDays = (TextView) v.findViewById(R.id.shortDays);

				line_a.setText(item.get("year"));
				countCommissions.setText(Html.fromHtml("<b>" + getString(R.string.number_comissions)
						+ "</b> " + item.get("count")));
				totalDays.setText(Html.fromHtml("<b>" + getString(R.string.commission_days)
						+ "</b> " + item.get("totalDays")));
				largeDays.setText(Html.fromHtml("<b>" + getString(R.string.commission_large_days)
						+ "</b> " + item.get("largeDays")));
				shortDays.setText(Html.fromHtml("<b>" + getString(R.string.commission_short_days)
						+ "</b> " + item.get("shortDays")));
				LinearLayout line_length = (LinearLayout) v.findViewById(R.id.resume_line_b);
				if(Sp.getCommissionsDaysLengthActive(mContext)){
					line_length.setVisibility(View.VISIBLE);
				}else{
					line_length.setVisibility(View.GONE);			
				}
			}
			return v;
		}
	} 


}
