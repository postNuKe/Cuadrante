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
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.Extra;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public class ComisionListActivity extends SherlockFragmentActivity {
	
	private static String[] tabsName = new String[3];
	
	private int requestCode;

	public static int REQUEST_CODE_EDIT_COMISION = 1;
	public static int REQUEST_CODE_JUMP_MONTH_COMISION = 2;
	
	ComisionListFragmentAdapter mAdapter;
    ViewPager pager;
    TabPageIndicator indicator;
    public int year;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comision_list_tabs);

		//Cuadrante.checkComisionDefaultValues(this);
		DateTime today = new DateTime();
        Intent intent = getIntent();
		year = intent.getIntExtra("year", today.getYear());        
		
		tabsName[0] = String.valueOf(year);
		tabsName[1] = getString(R.string.record);
        
        mAdapter = new ComisionListFragmentAdapter(getSupportFragmentManager());

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(mAdapter);

        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_comision_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.bar_add_comision:
			//Toast.makeText(this, "current item:" + mAdapter.getPageTitle(mPager.getCurrentItem()), Toast.LENGTH_LONG).show();
			intent = new Intent(this, ComisionActivity.class);
			intent.putExtra("year", Integer.parseInt(mAdapter.getPageTitle(0).toString()));
			// setResult(RESULT_OK, intent);
			// startActivity(intent);
			startActivityForResult(intent, requestCode);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public class ComisionListFragmentAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {	    
	    private int mCount = 2;	    
	    
	    public ComisionListFragmentAdapter(FragmentManager fm) {
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
				return ComisionListFragment.newInstance(year);
			case 1:
				return ComisionHistoryFragment.newInstance(year);
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