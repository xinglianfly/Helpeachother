package cn.edu.sdu.online.activity;

import java.io.File;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.adapter.MyArrayAdapter;
import cn.edu.sdu.online.chatservice.ChatwithService;
import cn.edu.sdu.online.chatservice.ChatwithService.ChatBinder;
import cn.edu.sdu.online.entity.User;
import cn.edu.sdu.online.net.NetCore;
import cn.edu.sdu.online.util.ConvertString;
import cn.edu.sdu.online.util.DialogUtil;

public class RegisterActivityNext extends Activity implements OnClickListener {
	String TAG = "RegisterActivityNext";
	// 声明信息
	int sex;
	String email, password, nick_name = "猜猜我是谁~", phone;
	double screenWigth, screenheight, screenDensity;
	Dialog dialog;
	private User registUser = new User();
	// 声明组件
	EditText edittext_nickname;
	Spinner spinner_name;
	ArrayAdapter<String> adapter;
	Button button_ok;
	Button button_skip;
	// 声明上传
	private static final int PHOTO_REQUEST_CAMERA = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���

	private Bitmap bitmap;
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	private boolean bound =false;
	private ChatwithService chatservice;

	// boolean isMenu=false;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.register_next_activity);

		getMessage();
		initialization();
		getSize();
		setSize();
		Intent intent = new Intent(this, ChatwithService.class);
		boolean isBound =  bindService(intent, connectionService, BIND_IMPORTANT | BIND_AUTO_CREATE);
		if (!isBound)
			Log.w("bound", "can't bound");
		else
			Log.i("bound", "bound success");
		// /////////////////////////////////////////////////////////
		// if (sex == 0)
		// edittext_nickname.setText("girl");
		// if (sex == 1)
		// edittext_nickname.setText("boy");
	}
	protected void onDestroy() {
		super.onDestroy();
		unbindService(connectionService);
	};
	
	

	private ServiceConnection connectionService = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			Log.v("bound", "disconnected");
			bound = false;

		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			Log.v("bound", "connected");
			ChatBinder binder = (ChatBinder) arg1;
			chatservice = binder.getService();
			bound = true;
		}
	};

	// 初始化组件
	private void initialization() {
		edittext_nickname = (EditText) findViewById(R.id.register_next_edittext_nickname);
		spinner_name = (Spinner) findViewById(R.id.register_spinner_name);
		// Spinner-----------------------------------------------------------------------
		// 下拉项
		String[] names = { " 先生", " 小姐", " 同学" };
		// 组件初始化
		adapter = new MyArrayAdapter(RegisterActivityNext.this, names);
		// 下拉样式
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_name.setAdapter(adapter);
		// 监听
		spinner_name.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sex = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		button_ok = (Button) findViewById(R.id.register_next_button_ok);
		button_ok.setOnClickListener(this);
		button_ok.setTag(2);

		button_skip = (Button) findViewById(R.id.register_next_button_jump);
		button_skip.setOnClickListener(this);
		button_skip.setTag(3);

	}

	// 得到上一步参数
	private void getMessage() {
		Intent intent = this.getIntent();
		email = intent.getStringExtra("email");
		password = intent.getStringExtra("password");
		sex = intent.getIntExtra("sex", 0);

	}

	// 组件尺寸------------------------------------------------------------------------------------------
	// 设置具体数值
	private void setSize() {
		setSize(R.id.register_next_image_nickname, -43, 0, -30, 0);
		setSize(R.id.register_next_edittext_nickname, 0, 0, -30, 0);
		// setSize(R.id.register_next_text_photo, -82, 0, -82, 0);
		setSize(R.id.register_next_button_ok, -180, 0, -40, 0);
		setSize(R.id.register_spinner_name, -100, 0, -30, 0);
	}

	// 得到屏幕数值
	private void getSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		screenWigth = metrics.widthPixels;
		screenheight = metrics.heightPixels;
		screenDensity = metrics.density;
	}

	// 设置组件尺寸的方法
	private void setSize(int id, double w_p, double w_m, double h_p, double h_m) {
		android.view.ViewGroup.LayoutParams lp = this.findViewById(id)
				.getLayoutParams();

		if (w_p != 0) {
			if (w_p > 0) {

				lp.width = (int) (w_p * screenWigth - w_m * screenDensity);
			}
			if (w_p < 0) {
				lp.width = (int) (w_p * -1 * screenDensity - w_m
						* screenDensity);
			}

		}

		if (h_p != 0) {
			if (h_p > 0) {
				lp.height = (int) (h_p * screenheight - h_m * screenDensity);
			}
			if (h_p < 0) {
				lp.height = (int) (h_p * -1 * screenDensity - h_m
						* screenDensity);
			}

		}
	}

	// 设置监听--------------------------------------------------------------------------------------
	@Override
	public void onClick(View v) {

		int tag = (Integer) v.getTag();
		// TODO Auto-generated method stub

		switch (tag) {
		case 1:// 上传图片
				// 打开菜单
			openOptionsMenu();
			break;

		case 2:// 完成
			nick_name = edittext_nickname.getText().toString();
			initUser();
			// 开启注册线程
			dialog = DialogUtil.createLoadingDialog(this, "注册中...");
			dialog.show();

			Thread thread = new Thread(new registerThread());
			thread.start();
			// 传送信息email,password,sex,nick_name,----------------------------------------------------------------------------
			// -----------------------------------------------------------------------------------
			// -----------------------------------------------------------------------------------

			break;

		case 3:// 跳过
			Intent intent = new Intent(RegisterActivityNext.this,
					LoginActivity.class);
			startActivity(intent);
			break;

		}

	}

	private void initUser() {
		registUser.setEmail(email);
		registUser.setPassword(password);
		registUser.setSex(sex);
		registUser.setNickName(nick_name);

	}

	// menu-----------------------------------------------------------------------------------------------
	// 上传选择的menu创建
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		String menu1 = getResources().getString(
				R.string.register_next_menu_photo);
		String menu2 = getResources()
				.getString(R.string.register_next_menu_cam);
		// 相册
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, menu1);// findViewById(R.string.register_next_menu_photo).toString());
		// 照相机
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, menu2);// findViewById(R.string.register_next_menu_cam).toString());

		return true;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			// 从相册返回数据
			if (data != null) {
				// 得到图片路径
				Uri uri = data.getData();
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(
						RegisterActivityNext.this,
						getResources().getString(
								R.string.register_toast_cardfull), 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
				// 设置尺寸
				int bw = bitmap.getWidth();
				int bh = bitmap.getHeight();
				float scaleW = ((float) 82.0) / bw;// 比例
				float scaleH = ((float) 82.0) / bh;
				Matrix newM = new Matrix();
				newM.postScale(scaleW, scaleH);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bw, bh, newM, true);
				boolean delete = tempFile.delete();
				System.out.println("delete = " + delete);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// 判断存储卡是否满
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	private void crop(Uri uri) {
		//
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		//
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		//
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private int startRegister() {
		Log.v(TAG, "startConnect...:");
		String jsonResult = new NetCore().Registe(registUser);
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

	private void registerOpenfire() {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {

			@SuppressWarnings("null")
			@Override
			public void run() {
					chatservice.register(ConvertString.convert(email), password);
					RegisterActivityNext.this.finish();
			}
		});
		t.start();

	}


	Handler openfireHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(RegisterActivityNext.this, "服务器没有返回结果",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(RegisterActivityNext.this, "这个账号已经存在",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:

				Toast.makeText(RegisterActivityNext.this, "注册失败",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(RegisterActivityNext.this, "恭喜你注册成功",
						Toast.LENGTH_SHORT).show();
				RegisterActivityNext.this.finish();
				break;

			default:
				break;
			}

		}
	};
	Handler registHandler = new Handler() {

		public void handleMessage(Message message) {
			if (dialog != null) {
				dialog.dismiss();
			}
			Log.v(TAG, "message.what" + message.what);
			switch (message.what) {

			case 1:
				// 注册成功
				registerOpenfire();
//				Toast.makeText(RegisterActivityNext.this,
//						getString(R.string.registerSuccess), Toast.LENGTH_SHORT)
//						.show();
//
//				RegisterActivityNext.this.finish();
				break;
			case 0:// 注册失败
				Toast.makeText(RegisterActivityNext.this,
						getString(R.string.registerFaild), Toast.LENGTH_SHORT)
						.show();
				RegisterActivityNext.this.finish();
				break;
			default:
				break;
			}
		}
	};

	class registerThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int result = startRegister();
			Message message = new Message();
			message.what = result;

			registHandler.sendMessage(message);
		}
	}
}
