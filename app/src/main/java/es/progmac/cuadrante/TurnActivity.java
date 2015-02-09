package es.progmac.cuadrante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import es.progmac.cuadrante.info.TipoServicioInfo;
import es.progmac.cuadrante.info.TurnInfo;
import es.progmac.cuadrante.info.TurnTypeInfo;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.PromptDialog;
import es.progmac.cuadrante.lib.Sp;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TurnActivity extends SherlockListActivity implements OnItemSelectedListener {
	private int sTurnId = 0;
	private int sSelectedOrder = 0;
	private String sTurnName = "";
	private boolean sAddType = false;
	private Context mContext;
	private static final String TAG = "TurnActivity";
	public DatabaseHandler db;
	private ListAdapter sa;
	int requestCode;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
	List<TurnTypeInfo> sTurnTypes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_turn);
		db = new DatabaseHandler(this);
		Intent intent = getIntent();
		sTurnId = intent.getIntExtra(Extra.TURN_ID, 0);
		TurnInfo turn = db.getTurn(sTurnId);
		sTurnName = turn.getName();		
		sTurnTypes = db.getTurnTypes(sTurnId);
		if(sTurnId > 0){
			setTitle(String.format(getString(R.string.edit_turn), sTurnName));
		}else{
			setTitle(R.string.add_turn);			
		}
		
		registerForContextMenu(getListView());
		//se usa un textview invisible para poder mostrar el contextmenu al hacer click en un 
		//item del actionbar, si no no se puede hacer
		registerForContextMenu(findViewById(R.id.contextmenu));
		listViewGenerateListAdapter();		
	}
	
	public void listViewGenerateListAdapter() {
		sAddType = false;
		//MyLog.d("onCreate", "tipos:" + sTurnTypes.size());
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = new HashMap<String, String>();;
		item.put("type_id", "0");
		item.put("line_a", getString(R.string.name));
		item.put("line_b", sTurnName);
		item.put("bg_color", String.valueOf(Cuadrante.SERVICE_DEFAULT_BG_COLOR));
		item.put("text_color", String.valueOf(Cuadrante.SERVICE_DEFAULT_TEXT_COLOR));
		list.add(item);

		for (TurnTypeInfo turnType : sTurnTypes) {
			TipoServicioInfo typeService = db.getTipoServicio(turnType.getTypeId());
			item = new HashMap<String, String>();
			item.put("type_id", String.valueOf(turnType.getTypeId()));
			item.put("bg_color", typeService.getBgColor());
			item.put("text_color", typeService.getTextColor());
			item.put("line_a", typeService.getTitle());
			
			String str_line_b = "";
			if(turnType.getSaliente() == 1) str_line_b = getString(R.string.add_saliente);
			item.put("line_b", str_line_b);
			list.add(item);
		}
		sa = new TurnTypeItemAdapter(this,
				android.R.layout.simple_list_item_1, list);
		setListAdapter(sa);
	}

	public class TurnTypeItemAdapter extends
			ArrayAdapter<HashMap<String, String>> {
		private ArrayList<HashMap<String, String>> items;

		public TurnTypeItemAdapter(Context context, int textViewResourceId,
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
				v = vi.inflate(R.layout.turn_type_list_item_1, null);
			}

			HashMap<String, String> item = items.get(position);
			if (item != null) {
				//TextView type_id = (TextView) v.findViewById(R.id.type_id);
				TextView line_a = (TextView) v.findViewById(R.id.line_a);
				TextView line_b = (TextView) v.findViewById(R.id.line_b);
				LinearLayout layout_item = (LinearLayout) v
						.findViewById(R.id.listview_item);

				//type_id.setText(item.get("type_id"));
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
	
	/**
	 * Al hacer click en un item de la lista
	 */
	public void onListItemClick(ListView parent, View v, int position, long id) {
		//TextView type_id = (TextView) v.findViewById(R.id.type_id);
		sSelectedOrder = position;
		//MyLog.d(TAG, "position:" + position + " id:" + id);
		if(sSelectedOrder > 0) openContextMenu(v);
		else showDialog(0);
	}	
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			PromptDialog dlg = new PromptDialog(this, R.string.name, 0,
					sTurnName, 30, 
					String.format(
							this.getString(R.string.hint_max_characters), 
							30), 0) {
				@Override
				public boolean onOkClicked(String input) {
					// do something
					sTurnName = input;
					listViewGenerateListAdapter();
					return true; // true = close dialog
				}
			};
			dlg.show();
			break;
		}
		return null;
	}
	
	@Override
	/**
	 * menus desplegables
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if(sAddType){
			List<TipoServicioInfo> typeServices = db.getAllTipoServicios();//db.getAllTipoServiciosForAddTurn();
			for (TipoServicioInfo typeService : typeServices) {
				menu.add(1, typeService.getId(), Menu.NONE, typeService.getTitle());
			}
		}else if(sSelectedOrder > 0){
			menu.add(Menu.NONE, 1, Menu.NONE, getResources().getString(R.string.up));
			menu.add(Menu.NONE, 2, Menu.NONE, getResources().getString(R.string.down));
			menu.add(Menu.NONE, 3, Menu.NONE, getResources().getString(R.string.add_remove_saliente));
			menu.add(Menu.NONE, 4, Menu.NONE, getResources().getString(R.string.delete));
		}
	}
	
	/**
	 * Que pasa al hacer click en una opción del menu desplegable
	 * @para Se debe de especificar android.view.MenuItem porque si no no funciona al hacer clic
	 * en un item del desplegable
	 */
	public boolean onContextItemSelected(android.view.MenuItem item) {
		//MyLog.d(TAG, "group:" + item.getGroupId() +  " item:" + item.getItemId());
		int typeOrder = sSelectedOrder - 1;
		TurnTypeInfo type = new TurnTypeInfo();
		TurnTypeInfo typeToUp = new TurnTypeInfo();
		TurnTypeInfo typeToDown = new TurnTypeInfo();
		if(item.getGroupId() == Menu.NONE){//editar un tipo
			switch (item.getItemId()) {
			case 1://up
				if(typeOrder > 0){
					typeToUp = sTurnTypes.get(typeOrder);
					typeToDown = sTurnTypes.get(typeOrder - 1);
					sTurnTypes.set(typeOrder - 1, typeToUp);
					sTurnTypes.set(typeOrder, typeToDown);
				}
				break;
			case 2://down
				if(typeOrder < sTurnTypes.size() - 1){
					typeToDown = sTurnTypes.get(typeOrder);
					typeToUp = sTurnTypes.get(typeOrder + 1);
					sTurnTypes.set(typeOrder + 1, typeToDown);
					sTurnTypes.set(typeOrder, typeToUp);
				}
				break;
			case 3://saliente
				type = sTurnTypes.get(typeOrder);
				type.switchSaliente();
				sTurnTypes.set(typeOrder, type);
				break;
			case 4://delete
				sTurnTypes.remove(typeOrder);
				break;
			}
		}else{//añadir tipo
			TipoServicioInfo typeService = db.getTipoServicio(item.getItemId());
			
			sTurnTypes.add(
					new TurnTypeInfo(sTurnId, item.getItemId(), 
							sTurnTypes.size(), typeService.isServiceEndNextDay()));
		}
		listViewGenerateListAdapter();
		return true;
	}	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_turn, menu);
		return true;
	}    

	public boolean onOptionsItemSelected(MenuItem item) {
		View v = getWindow().getDecorView().getRootView();
		switch (item.getItemId()) {
		case R.id.bar_ready:
			onSetData(v);
			return true;
		case R.id.bar_cancel:
			onSetCancel(v);
			return true;
		case R.id.bar_add_type:
			sAddType = true;
			openContextMenu(findViewById(R.id.contextmenu));
			return true;		
		case R.id.bar_delete:
			onSetDelete(v);
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}	
	
	public void onSetData(View view){
		if(sTurnName != null){
			if(sTurnName.length() > 0){
				if(sTurnTypes.size() > 0){
					TurnInfo turn = new TurnInfo(sTurnId, sTurnName);
					MyLog.d(TAG, "turnId:" + sTurnId + " name:" + sTurnName);
					if(sTurnId > 0) db.updateTurn(turn);
					else turn = db.insertTurn(turn);
					db.deleteTurnTypes(turn.getId());
					int i = 0;
					for (TurnTypeInfo turnType : sTurnTypes) {
						TipoServicioInfo type = db.getTipoServicio(turnType.getTypeId());
						MyLog.d(TAG, "turnId:" + turn.getId() + "typeName:" + type.getName() + " type:" + turnType.getTypeId() + " orden:" + i + " saliente:" + turnType.getSaliente());
						db.insertTurnType(
								new TurnTypeInfo(
										turn.getId(), 
										turnType.getTypeId(), 
										i, 
										turnType.getSaliente()));
						i++;
					}
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					this.finish();		
				}else{
					Toast.makeText(mContext, getString(R.string.error_must_add_types), 
							Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(mContext, getString(R.string.error_name_required), Toast.LENGTH_LONG).show();			
			}
		}else{
			Toast.makeText(mContext, getString(R.string.error_name_required), Toast.LENGTH_LONG).show();			
		}
	}
	
	public void onSetCancel(View view) {
		this.finish();
	}	
	
	public void onSetDelete(View view){
		if(sTurnId > 0){
			final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			//alertamos si está seguro de querer eliminar el turno
		    dialog.setMessage(getString(R.string.message_alert_delete_turn));
		    dialog.setCancelable(false);
		    dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {  
		    		db.deleteTurn(sTurnId);
		    		db.deleteTurnTypes(sTurnId);
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					TurnActivity.this.finish();		    		
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
		
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


}
