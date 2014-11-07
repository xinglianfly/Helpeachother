package cn.edu.sdu.online.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.chatservice.ChatwithService;
import cn.edu.sdu.online.chatservice.ChatwithService.ChatBinder;
import cn.edu.sdu.online.entity.User;
import cn.edu.sdu.online.net.NetCore;
import cn.edu.sdu.online.share.FloatApplication;
import cn.edu.sdu.online.util.ConvertString;
import cn.edu.sdu.online.util.DialogUtil;

import com.tencent.tauth.Tencent;

public class LoginActivity extends Activity implements View.OnClickListener {
	private String TAG = "LoginActivity";
	private User loginUser = new User();
	private double screenWidth, screenHight, density;
	/** 用来操作SharePreferences的标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	/** 如果登录成功后,用于保存用户名到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_EMAIL = "MAP_LOGIN_EMAIL";

	/** 如果登录成功后,用于保存PASSWORD到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";
	private String SHARE_LOGIN_SUCCESS = "SHARE_LOGIN_SUCCESS";

	/** 如果登陆失败,这个可以给用户确切的消息显示,true是网络连接失败,false是用户名和密码错误 */
	private boolean isNetError;
	Dialog dialog;
	ImageView user, pass;
	Button buttonEnter, register;
	EditText editUser, editPass;
	CheckBox checkRemember;

	Tencent mTencent;
	Handler handler;
	String SCOPE = "get_simple_userinfo,add_topic";
	String openid;
	SharedPreferences sp;
	boolean isRemember = true;
	private boolean bound = false;
	private ChatwithService chatservice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		if(sp.getBoolean("first_run", true)){
			Intent intent = new Intent(this,UserGuideActivity.class);
			startActivity(intent);
			Editor editor = sp.edit();
			editor.putBoolean("first_run", false);
			editor.commit();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.login_activity);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		findView();
		getSize();
		setSize();
		getRememberMe(isRemember);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent sIntent = new Intent(LoginActivity.this, ChatwithService.class);
		bindService(sIntent, serviceConnection, BIND_IMPORTANT
				| BIND_AUTO_CREATE);

	};

	ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			bound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			ChatBinder binder = (ChatBinder) service;
			chatservice = binder.getService();
			Log.v(TAG, "BIND chatservice" + chatservice);
			bound = true;
		}
	};

	protected void onPause() {
		super.onPause();
		if (bound)
			unbindService(serviceConnection);
	};

	// 设置控件绝对大小
	public void setSize() {
		setSize(1, R.id.login_image_user, 43, 0, 30, 0);
		setSize(1, R.id.login_image_pass, 43, 0, 30, 0);
		setSize(1, R.id.login_edittext_user, -1, 0, 30, 0);
		setSize(1, R.id.login_edittext_password, -1, 0, 30, 0);
		setSize(1, R.id.login_button_renrne, 180, 0, 40, 0);
		setSize(1, R.id.login_button_register, 180, 0, 40, 0);
	}

	/**
	 * 控件大小设置 style=1时 w_p,h_p为控件尺寸（px），若为-1则不设置 style=2时
	 * w_p,h_p为控件在屏幕%，w_m,h_m,为差值
	 * */

	public void setSize(int style, int id, int w_p, int w_m, int h_p, int h_m) {
		if (style == 1) {
			android.view.ViewGroup.LayoutParams lp = this.findViewById(id)
					.getLayoutParams();
			if (w_p != -1)
				lp.width = (int) (w_p * density);
			if (h_p != -1)
				lp.height = (int) (h_p * density);
		}
		if (style == 2) {
			android.view.ViewGroup.LayoutParams lp = this.findViewById(id)
					.getLayoutParams();
			if (w_p != -1)
				lp.width = (int) (w_p * screenWidth - w_m * density);
			if (h_p != -1)
				lp.height = (int) (h_p * screenHight - h_m * density);
		}
	}

	// 得到屏幕长宽密度
	private void getSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHight = metrics.heightPixels;
		density = metrics.density;
	}

	// 初始化控件
	private void findView() {
		buttonEnter = (Button) findViewById(R.id.login_button_renrne);
		buttonEnter.setOnClickListener(this);
		buttonEnter.setTag(1);

		register = (Button) findViewById(R.id.login_button_register);
		register.setOnClickListener(this);
		register.setTag(2);

		user = (ImageView) findViewById(R.id.login_image_user);
		pass = (ImageView) findViewById(R.id.login_image_pass);
		editUser = (EditText) findViewById(R.id.login_edittext_user);
		editPass = (EditText) findViewById(R.id.login_edittext_password);

		checkRemember = (CheckBox) findViewById(R.id.login_checkbox_remember);
		checkRemember.setTag(3);

	}

	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	private void initUser() {
		saveSharePreferences(isRemember);
		loginUser.setEmail(editUser.getText().toString());
		loginUser.setPassword(editPass.getText().toString());

	}

	// 监听
	public void onClick(View v) {
		int tag = (Integer) v.getTag();

		switch (tag) {
		case 1:
			initUser();// 从输入框得到数据
			dialog = DialogUtil.createLoadingDialog(this, "请稍后...");
			dialog.show();
			Thread loginTh = new Thread(new loginThread());
			loginTh.start();
			break;
		case 2:
			Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(it);
			break;
		case 3:
			isRemember = checkRemember.isChecked();
			break;
		}

	}

	/** 读取记住的用户名和密码 */
	private void getRememberMe(boolean isRemember) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		String email = share.getString(SHARE_LOGIN_EMAIL, "");
		String password = share.getString(SHARE_LOGIN_PASSWORD, "");
		if (!"".equals(email)) {
			editUser.setText(email);
		}
		if (!"".equals(password)) {
			editPass.setText(password);
			checkRemember.setChecked(true);
		}

		share = null;

	}

	/** 清除密码 */
	private void clearSharePassword() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_PASSWORD, "").commit();
		share = null;
	}

	/**
	 * 如果登录成功过,则将登陆用户名和密码记录在SharePreferences
	 * 
	 * @param saveUserName
	 *            是否将用户名保存到SharePreferences
	 * @param savePassword
	 *            是否将密码保存到SharePreferences
	 * */

	private void saveSharePreferences(boolean saveUser) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		if (saveUser) {
			share.edit()
					.putString(SHARE_LOGIN_PASSWORD,
							editPass.getText().toString()).commit();
			share.edit()
					.putString(SHARE_LOGIN_EMAIL, editUser.getText().toString())
					.commit();
		}

		share = null;
	}

	// //

	private int startLogin() {
		Log.v(TAG, "startConnect...:");
		String jsonResult = new NetCore().Login(loginUser);

		Log.v(TAG, "jsonResult:" + jsonResult);
		int result = 10;
		try {
			result = new JSONObject(jsonResult).getInt("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v(TAG, "loginresult:" + result);
		return result;
	}

	Handler loginHandler = new Handler() {
		public void handleMessage(Message message) {
			SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
			Log.v(TAG, "message.what" + message.what);
			switch (message.what) {

			case 1:
				// 登录成功

				share.edit()
						.putString(SHARE_LOGIN_PASSWORD,
								editPass.getText().toString()).commit();
				share.edit()
						.putString(SHARE_LOGIN_EMAIL,
								editUser.getText().toString()).commit();
				share.edit().putBoolean(SHARE_LOGIN_SUCCESS, true).commit();
				Toast.makeText(LoginActivity.this,
						getString(R.string.loginSuccess), Toast.LENGTH_SHORT)
						.show();
				// 得出用户信息并跳转
				// 开启线程

				Thread thread = new Thread(new initUserThread());
				thread.start();

				break;
			case 0:
				// 登录失败
				if (dialog != null) {
					dialog.dismiss();
				}
				share.edit().putBoolean(SHARE_LOGIN_SUCCESS, false).commit();
				Toast.makeText(LoginActivity.this,
						getString(R.string.loginFail), Toast.LENGTH_SHORT)
						.show();
				clearSharePassword();
				break;
			case 3:
				// 登录成功后成功获取用户资料
				if (dialog != null) {
					dialog.dismiss();
				}

				Thread openfire = new Thread(new logOpenfire());
				openfire.start();

				Intent intent1 = new Intent();
				intent1.setClass(LoginActivity.this, FragmentTabsPager.class);
				startActivity(intent1);
				LoginActivity.this.finish();
				break;
			default:
				break;
			}

		}
	};

	class loginThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int result = startLogin();
			Message message = new Message();
			message.what = result;

			loginHandler.sendMessage(message);
		}
	}

	private void startGetUser() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);

		String email = share.getString(SHARE_LOGIN_EMAIL, "");
		String jsonResult = new NetCore().GetUserInfo(email);

		User user = parseJson(jsonResult);
		FloatApplication.getApp().setStoreUser(
				getString(R.string.userFileName), user);
		Log.v(TAG, jsonResult);
		Log.v(TAG, user.getEmail());

	}

	private User parseJson(String jsonReslut) {
		// TODO Auto-generated method stub
		User user = new User();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonReslut);
			user.setId(jsonObject.getString("id"));
			user.setEmail(jsonObject.getString("email"));
			user.setNickName(jsonObject.getString("nickName"));
			user.setSex(jsonObject.getInt("sex"));
			user.setPhoneNo(jsonObject.getString("phoneNo"));
			user.setRegisterTime(jsonObject.getString("registerTime"));
			user.setLevel(jsonObject.getInt("level"));
			user.setScore(jsonObject.getInt("score"));
			user.setLastLogin(jsonObject.getString("lastLogin"));
			user.setLastMsg(jsonObject.getString("lastMsg"));
			user.setHeadPhoto(jsonObject.getString("headPhoto"));
			user.setSchool(jsonObject.getString("school"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	class initUserThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			startGetUser();
			Message message = new Message();
			message.what = 3;
			loginHandler.sendMessage(message);
		}

	}

	class logOpenfire implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String username = loginUser.getEmail();
			String password = loginUser.getPassword();
			Log.v(TAG, username + "username");
			Log.v(TAG, password + "password");
			Log.v(TAG, chatservice + "CHATSERVICE");

			if (bound) {
				Log.v(TAG,
						"ChatService connection" + chatservice.getConnection());
				if (!chatservice.getConnection().isConnected()) {
					chatservice.connect();
				}
				chatservice.login(ConvertString.convert(username), password);

			}
		}

	}

}
