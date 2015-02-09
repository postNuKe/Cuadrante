package es.progmac.cuadrante.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListFragment;

import es.progmac.cuadrante.R;
import es.progmac.cuadrante.TurnActivity;
import es.progmac.cuadrante.info.TurnInfo;
import es.progmac.cuadrante.info.TurnTypeInfo;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.Request;

public class TurnListFragment extends SherlockListFragment {
	private static final String TAG = null;
	private Context mContext;
	private DatabaseHandler db;
	public List<TurnInfo> turns;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	
	public static TurnListFragment newInstance() {
		TurnListFragment fragment = new TurnListFragment();        
        return fragment;
    }	
	
	/**
	 * Necesario un constructor vacío para que no pete en algunos momentos
	 */	
	public TurnListFragment() {
		
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
		View view = inflater.inflate(R.layout.fragment_turn_list, container, false);        
		
		return view;	
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
    	Intent intent = new Intent(mContext, TurnActivity.class);
    	intent.putExtra(Extra.TURN_ID, turns.get(position).getId());
    	startActivityForResult(intent, Request.TURN_EDIT);
	}

	public void listViewGenerateListAdapter() {
		// MyLog.d("onCreate", tipos_servicios.toString());
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		turns = db.getAllTurns();
		for (TurnInfo turn : turns) {
			int days = 0;
			List<TurnTypeInfo> types = db.getTurnTypes(turn.getId());
			String typesLine = "";
			for(TurnTypeInfo type : types){
				typesLine = typesLine.concat(type.getName() + " | ");
				if(type.getSaliente() == 1){
					typesLine = typesLine.concat("SAL | ");
					days++;
				}
				days++;
			}
			Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("First Line", 
            		String.format(getString(R.string.turn_title), turn.getName(), days));
            datum.put("Second Line", typesLine);
            data.add(datum);
		}
		SimpleAdapter adapter = new SimpleAdapter(
				mContext, 
				data,
                android.R.layout.simple_list_item_2, 
                new String[] {"First Line", "Second Line" }, 
                new int[] {android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}
}
