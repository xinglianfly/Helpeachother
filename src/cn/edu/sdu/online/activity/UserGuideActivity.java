package cn.edu.sdu.online.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import cn.edu.sdu.online.R;

public class UserGuideActivity extends Activity {

	private ArrayList<ImageView> views;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//MobclickAgent.onError(this);
		views = new ArrayList<ImageView>();
		ImageView iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.lead01);
		views.add(iv);
		iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.lead02);
		views.add(iv);
		iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.lead03);
		views.add(iv);
		iv = new ImageView(this);
	
		iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UserGuideActivity.this.finish();
			}
		});
		//添加最后一个空图片，用于滑动到最后结束Activity
		iv = new ImageView(this);
		views.add(iv);
		
		ViewPager pager = new ViewPager(this);
		pager.setAdapter(new GuideAdapter(this, views));
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(pager);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				if(arg0 == views.size()-1)
					UserGuideActivity.this.finish();
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	
	class GuideAdapter extends PagerAdapter{

		List<ImageView> ivs;
		Context context;
		
		public GuideAdapter(Context context, List<ImageView> views) {
			ivs = views;
			this.context = context;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageView iv = ivs.get(position);
			LayoutParams params = new LayoutParams();
			params.height = LayoutParams.FILL_PARENT;
			params.width = LayoutParams.FILL_PARENT;
			((ViewPager)container).addView(iv,0,params);
			return views.get(position);
		}
	}
}
