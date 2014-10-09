package com.myemr.mobileemr;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class DisplayActivity extends Activity{
	   
	private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		actionBar=getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		
		ActionBar.Tab tab = actionBar.newTab().setText(R.string.left).setTabListener(new MyTabListener<BasicFragment>(this,
				"BasicInfo", BasicFragment.class));
		actionBar.addTab(tab);
		
		tab=actionBar.newTab().setText(R.string.right).setTabListener(new MyTabListener<MedicFragment>(this,
				"MedicInfo", MedicFragment.class));
		actionBar.addTab(tab);
		
	}
	
	
	class MyTabListener<T extends Fragment> implements ActionBar.TabListener{
		private Fragment mfragment;
		private Activity mactivity;
		private String mTag;
		private Class<T> mclass;
		
		MyTabListener(Activity activity, String tag, Class<T> c){
			mactivity=activity;
			mTag=tag;
			mclass=c;
		}
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(mfragment==null){
				mfragment=Fragment.instantiate(mactivity, mclass.getName());
				ft.add(android.R.id.content, mfragment, mTag);
			}else
				ft.attach(mfragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(mfragment!=null)
				ft.detach(mfragment);
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}
	}
}
