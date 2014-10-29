package cn.edu.sdu.online.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.subfragment.ReleaseTask;

public class SubContainer extends Fragment {

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.v(TAG, "SubContaineronResume");
		super.onResume();
	}

	TabHost mTabHost;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	List<View>views;
String TAG= "SubContainer";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.sub_fragment_tab_pager, null);
		Log.v(TAG, "SubContainertonCreateview");
		mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		
		mTabHost.setup();
		mViewPager = (ViewPager) view.findViewById(R.id.sub_pager);


		 mTabsAdapter = new TabsAdapter(SubContainer.this, mTabHost, mViewPager);
		 View release = inflater.inflate(R.layout.subtab_receive, null);
//		 View recieve = inflater.inflate(R.layout.subtab_release, null);
		// mTabsAdapter.addTab(
//		 mTabHost.newTabSpec("RECIEVE").setIndicator(recieve),
//		 RecieveTask.class, null);
		 mTabsAdapter.addTab(
		 mTabHost.newTabSpec("release").setIndicator(release),
		 ReleaseTask.class, null);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		return view;
	}

	
	
		
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */

	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(Fragment activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getChildFragmentManager());
			mContext = activity.getActivity();
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

}

//class myViewpager extends ViewPager
//{
//
//	private boolean isCanScroll=true;
//
//	public myViewpager(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		// TODO Auto-generated constructor stub
//	}
//
//	public myViewpager(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
//	
//	 public void setScanScroll(boolean isCanScroll) {  
//	        this.isCanScroll = isCanScroll;  
//	    }  
//	  
//	    @Override  
//	    public void scrollTo(int x, int y) {  
//	            super.scrollTo(x, y);  
//	    }  
//	  
//	    @Override  
//	    public boolean onTouchEvent(MotionEvent arg0) {  
//	        // TODO Auto-generated method stub  
//	        return false;//super.onTouchEvent(arg0);  
//	    }  
//	  
//	    @Override  
//	    public void setCurrentItem(int item, boolean smoothScroll) {  
//	        // TODO Auto-generated method stub  
//	        super.setCurrentItem(item, smoothScroll);  
//	    }  
//	  
//	    @Override  
//	    public void setCurrentItem(int item) {  
//	        // TODO Auto-generated method stub  
//	        super.setCurrentItem(item);  
//	    }  
//	
//}
