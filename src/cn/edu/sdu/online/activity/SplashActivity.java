package cn.edu.sdu.online.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import cn.edu.sdu.online.R;

public class SplashActivity extends Activity {
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	
	private String SHARE_LOGIN_SUCCESS = "SHARE_LOGIN_SUCCESS";
	private SharedPreferences share;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		share = getSharedPreferences(SHARE_LOGIN_TAG, MODE_PRIVATE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.splash_activity);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean iflogin = share.getBoolean(SHARE_LOGIN_SUCCESS, false);
				if (iflogin) {
					Intent intentF = new Intent();
					intentF.setClass(SplashActivity.this,
							FragmentTabsPager.class);
					startActivity(intentF);
					finish();
				} else {
					Intent intent = new Intent();
					intent.setClass(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}
		}, 5000);

	}

}
