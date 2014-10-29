/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.sdu.online.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.fragment.PersonFragment;
import cn.edu.sdu.online.fragment.SearchFragment;
import cn.edu.sdu.online.fragment.SquareFragment;
import cn.edu.sdu.online.fragment.SubContainer;
import cn.edu.sdu.online.share.FloatApplication;
import cn.edu.sdu.online.view.FloatView;


/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI that
 * switches between tabs and also allows the user to perform horizontal flicks
 * to move between the tabs.
 */
public class FragmentTabsPager extends FragmentActivity {
	TabHost mTabHost;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	static TextView view1;
	//static ActionBar actionbar;
	static TextView titlea;
	View viewTitleBar;

	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerParams = null;
	private FloatView floatView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setTheme(R.style.MainTheme);
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(FragmentTabsPager.this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.fragment_tabs_pager);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);//布局
		mTabHost.setup();
		View storage = inflater.inflate(R.layout.tab_storage, null);//tab bar 布局
		
		View square = inflater.inflate(R.layout.tab_square, null);
		View shake = inflater.inflate(R.layout.tab_shake, null);
		View person = inflater.inflate(R.layout.tab_personal, null);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		titlea = (TextView) findViewById(R.id.title);//题目
		
		
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);//解析器

		mTabsAdapter.addTab(mTabHost.newTabSpec("square")//添加f
				.setIndicator(square), SquareFragment.class, null);
		mTabsAdapter.addTab(mTabHost.newTabSpec("shake")
				.setIndicator(shake), SearchFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("storage").setIndicator(storage),
				SubContainer.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("person").setIndicator(person),
				PersonFragment.class, null);
		 
		 new Handler().postDelayed(new Runnable() {
	            public void run() {
	            	createView();
	            }
	        }, 200);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

	}
    //悬浮按钮
	private void createView() {
		floatView = new FloatView(getApplicationContext());
		floatView.setOnClickListener(new imageviewListener());
		floatView.setImageResource(R.drawable.releasetask);
		windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		windowManagerParams = ((FloatApplication) getApplication())
				.getWindowParams();
		windowManagerParams.type = LayoutParams.TYPE_PHONE;
		windowManagerParams.format = PixelFormat.RGBA_8888;
		windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
		windowManagerParams.x = 800;
		windowManagerParams.y = 900;
		windowManagerParams.width = LayoutParams.WRAP_CONTENT;
		windowManagerParams.height = LayoutParams.WRAP_CONTENT;
		windowManager.addView(floatView, windowManagerParams);
	}

	class imageviewListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(FragmentTabsPager.this, PublishTaskActivity.class);
			startActivity(intent);
		}
	}



	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		new Handler().postDelayed(new Runnable() {
            public void run() {
            	windowManager.addView(floatView, windowManagerParams);//此时再加悬浮button
            }
        }, 500);
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			windowManager.removeView(floatView);//去除悬浮button
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
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
		String TAG = "FragmentTabsPager";
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		String[] title = { "广场", "搜索", "清单", "信息" };

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
		
		
      //再说
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

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
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
			Log.v(TAG, position+"position");
			mViewPager.setCurrentItem(position);
			titlea.setText(title[position]);
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
			titlea.setText(title[position]);
			Log.v(TAG, position+"position");
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// System.out.println(state+"{{{{{{{{");
		}

	}
	/** 
	 * 菜单、返回键响应 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    // TODO Auto-generated method stub  
	    if(keyCode == KeyEvent.KEYCODE_BACK)  
	       {    
	           exitBy2Click();      //调用双击退出函数  
	       }  
	    return false;  
	}  
	/** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  
	  
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true; // 准备退出  
	        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false; // 取消退出  
	            }  
	        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
	  
	    } else {  
	        finish();  
	    }  
	} 


}
