package es.progmac.cuadrante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.progmac.cuadrante.R;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.cuadrante.info.TipoServicioInfo;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.MyLog;
import android.os.Bundle;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class TypeServiceHistoryActivity extends ExpandableListActivity implements OnChildClickListener {
	public DatabaseHandler db;
	
	private static final String NAME = "NAME";
	private static final String IS_EVEN = "IS_EVEN";

	private static final String TAG = "TypeServiceHistoryActivity";
	 
	private ExpandableListAdapter mAdapter;
	private ArrayList<ArrayList<Long>> lista = new ArrayList<ArrayList<Long>>();
	private Long serviceSelectedId;
	
	int requestCode;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type_service_history);
		registerForContextMenu(getExpandableListView());
		db = new DatabaseHandler(this);
		Intent intent = getIntent();
        int type_id = intent.getIntExtra(Extra.TYPE_ID, 0);
        
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        
        TipoServicioInfo type_service = db.getTipoServicio(type_id);        
        setTitle(String.format(getString(R.string.title_activity_type_service_history), type_service.getTitle()));
        
        Cursor c = db.getTypeServiceHistory(type_id);
        
		String[] values = new String[c.getCount()];
		//MyLog.d("oncreate", "c.getcount:" + c.getCount());
		int i = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
        	//Recorremos el cursor hasta que no haya más registros
        	do {
        		//values[i] = c.getString(0).concat(" : ").concat(c.getString(1));
        		Map<String, String> curGroupMap = new HashMap<String, String>();
                groupData.add(curGroupMap);
                curGroupMap.put(NAME, c.getString(0).concat(" : ").concat(c.getString(1)));
                curGroupMap.put(IS_EVEN, (i % 2 == 0) ? "This group is even" : "This group is odd");
                
                List<ServicioInfo> services = 
                		db.getServicesFromTypeService(
                				type_id, 
                				CuadranteDates.formatDate(c.getInt(0), 1, 1), 
                				CuadranteDates.formatDate(c.getInt(0), 12, 31));
                List<Map<String, String>> children = new ArrayList<Map<String, String>>();
                lista.add(new ArrayList<Long>());
                for (ServicioInfo service : services) {
                	Map<String, String> curChildMap = new HashMap<String, String>();
                	children.add(curChildMap);
                	curChildMap.put(NAME, CuadranteDates.formatDateToHumans2(service.getDate()));
                	curChildMap.put(IS_EVEN, service.getComments());
                	lista.get(i).add(service.getId());
                }
                childData.add(children);        		
        		//MyLog.d("oncreate", "i:" + i + " " + values[i]);
        		i++;
        	} while(c.moveToNext());
        }                
        
        if(values.length > 0){
        	/*
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        			android.R.layout.simple_list_item_1, android.R.id.text1, values);
        	*/
        	// Set up our adapter
            mAdapter = new SimpleExpandableListAdapter(
                    this,
                    groupData,
                    android.R.layout.simple_expandable_list_item_1,
                    new String[] { NAME, IS_EVEN },
                    new int[] { android.R.id.text1, android.R.id.text2 },
                    childData,
                    android.R.layout.simple_expandable_list_item_2,
                    new String[] { NAME, IS_EVEN },
                    new int[] { android.R.id.text1, android.R.id.text2 }
                    );        	
        	setListAdapter(mAdapter);			
        }else{
        	RelativeLayout count_results = (RelativeLayout) findViewById(R.id.count_results);
        	count_results.setVisibility(View.VISIBLE);
        }
        
        c.close();
        db.close();
	}

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
        //MyLog.d(TAG, "group:" + groupPosition + " child:" + childPosition + " id:" + id);
        MyLog.d(TAG,lista.get(groupPosition).get(childPosition));
        serviceSelectedId = lista.get(groupPosition).get(childPosition);
        openContextMenu(v);
        return true;
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
		ServicioInfo service = db.getServicio(serviceSelectedId);
		if(item.getItemId() == 1){
			Intent intent = new Intent(this, ServicioActivity.class);
			intent.putExtra("date", service.getDate());
			startActivityForResult(intent, requestCode);			
		}else if(item.getItemId() == 2){
			Intent intent = new Intent(this, MainActivity.class);			
			intent.putExtra(Extra.DAY, service.getDay());
			intent.putExtra(Extra.MONTH, service.getMonth() - 1);
			intent.putExtra(Extra.YEAR, service.getYear());
			startActivityForResult(intent, requestCode);						
		}
		return true;
	}    
}
