package es.progmac.cuadrante;

import org.joda.time.DateTime;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;
import es.progmac.cuadrante.R;
import es.progmac.cuadrante.fragments.ComisionHistoryFragment;
import es.progmac.cuadrante.fragments.ComisionListFragment;
import es.progmac.cuadrante.fragments.TurnListFragment;
import es.progmac.cuadrante.fragments.TypeServiceListFragment;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.MyLog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class TypeServiceListActivity extends SherlockFragmentActivity {
	
	private static final String TAG = "TypeServiceListActivity";

	private static String[] tabsName = new String[3];
	
	public static int REQUEST_CODE_EDIT_TYPE = 1;
	
	TypeServiceListFragmentAdapter mAdapter;
    ViewPager pager;
    TabPageIndicator indicator;
    int requestCode;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type_service_list_tabs);
		
		tabsName[0] = getString(R.string.types);
		tabsName[1] = getString(R.string.turns);
        
        mAdapter = new TypeServiceListFragmentAdapter(getSupportFragmentManager());

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(mAdapter);

        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);	
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_type_service_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.bar_add:
			switch (pager.getCurrentItem()) {
			case 0:
				intent = new Intent(this, TipoServicioActivity.class);
				// setResult(RESULT_OK, intent);
				// startActivity(intent);
				startActivityForResult(intent, requestCode);
				break;
			case 1:
				intent = new Intent(this, TurnActivity.class);
				// setResult(RESULT_OK, intent);
				// startActivity(intent);
				startActivityForResult(intent, requestCode);
				break;
			}
			/*
			//Toast.makeText(this, "current item:" + mAdapter.getPageTitle(mPager.getCurrentItem()), Toast.LENGTH_LONG).show();
			intent = new Intent(this, ComisionActivity.class);
			intent.putExtra("year", Integer.parseInt(mAdapter.getPageTitle(0).toString()));
			// setResult(RESULT_OK, intent);
			// startActivity(intent);
			startActivityForResult(intent, requestCode);
			*/
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class TypeServiceListFragmentAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {	    
	    private int mCount = 2;	    
	    
	    public TypeServiceListFragmentAdapter(FragmentManager fm) {
	        super(fm);
	    }
	    
	    @Override
	    public int getCount() {
	        return mCount;
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	    	return tabsName[position];
	    }

	    @Override
	    public Fragment getItem(int position) {
	    	//MyLog.d("Adapter", "getItem:" + position);
	    	switch (position) {
			case 0:	
				return TypeServiceListFragment.newInstance();
			case 1:
				return TurnListFragment.newInstance();
			}
			return null;
	    }

	    public int getItemPosition(Object object) {
	        return POSITION_NONE;
	    }

		@Override
		public int getIconResId(int index) {
			// TODO Auto-generated method stub
			return 0;
		}
	    
	}	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//MyLog.d("onActivityResult", "llama");
		mAdapter.notifyDataSetChanged();

		
	}	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cuadrante.checkSignIn(this);		
	}	
	
}