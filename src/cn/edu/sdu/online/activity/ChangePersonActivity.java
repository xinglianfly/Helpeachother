package cn.edu.sdu.online.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.adapter.MyArrayAdapter;
import cn.edu.sdu.online.entity.User;
import cn.edu.sdu.online.net.NetCore;
import cn.edu.sdu.online.share.FloatApplication;
import cn.edu.sdu.online.util.DialogUtil;

public class ChangePersonActivity extends Activity implements OnClickListener {
	private Button baocun;
	private Button quxiao;
	private EditText change_tel;
	private EditText name2;
	private EditText school;
	private ImageView image;
	Spinner spinner_sex1;
	//Spinner spinner_name;
	
	ArrayAdapter<String> adapter1;
	ArrayAdapter<String> adapter2;
	String TAG = "ChangePersonActivity";
	private final static int CHANGE_SUCCESS = 1;// 改变成功
	private final static int CHANGE_ERROR = 0;// 改变失败
	int sex = 0;// "0" is girl; "1" is boy;
	int namenum = 0;
	String[] sexs1 = { "我是美女", "我是帅哥" };
	//String[] namespinner2 = { "  先生", "  小姐", "  同学" };
	Dialog dialog;
	FloatApplication app=FloatApplication.getApp();
	User user ;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.change_information);
		initialization();
	}

	private void initialization() {
		user=app.getUser(getString(R.string.userFileName));
		baocun = (Button) findViewById(R.id.baocun);
		quxiao = (Button) findViewById(R.id.quxiao);
		change_tel = (EditText) findViewById(R.id.change_tel);
		name2 = (EditText) findViewById(R.id.name2);
		school = (EditText) findViewById(R.id.changeschool);
		image=(ImageView)this.findViewById(R.id.userface);
		if(user.getSex()==1)
			image.setImageResource(R.drawable.boy);
		else
		image.setImageResource(R.drawable.girl);
			
		baocun.setOnClickListener(this);
		quxiao.setOnClickListener(this);
//		Intent intent = getIntent();
//		Bundle bun = intent.getExtras();
		// sex=intent.getIntExtra("sex1", 0);
		// change_tel.setText(intent.getStringExtra("tel"));
		// name2.setText(intent.getStringExtra("name"));
//		sex = bun.getInt("sex1");
//		change_tel.setText(bun.getString("tel"));
//		name2.setText(bun.getString("name"));
//		school.setText(bun.getString("school"));
		change_tel.setText(app.getUser(getString(R.string.userFileName)).getPhoneNo());
		name2.setText(app.getUser(getString(R.string.userFileName)).getNickName());
		school.setText(app.getUser(getString(R.string.userFileName)).getSchool());
		
		
		
		// 组件初始化
		spinner_sex1 = (Spinner) findViewById(R.id.register_spinner_sex1);
		//spinner_name = (Spinner) findViewById(R.id.register_spinner_name);
		adapter1 = new MyArrayAdapter(ChangePersonActivity.this, sexs1);
		//adapter2 = new MyArrayAdapter(ChangePersonActivity.this, namespinner2);
		// 下拉样式
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// add
		spinner_sex1.setAdapter(adapter1);
		//spinner_name.setAdapter(adapter2);
		// 监听
		spinner_sex1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sex = arg2;
			app.getUser(getString(R.string.userFileName)).setSex(sex);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
//		spinner_name.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				namenum = arg2;
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.baocun) {
//			Intent intent = getIntent();
//			Bundle bun = new Bundle();
//			bun.putString("name", name2.getText().toString());
//			bun.putString("tel", change_tel.getText().toString());
//
//			bun.putString("school", school.getText().toString());
//			bun.putInt("sex1", sex);
//			bun.putInt("nickname", namenum);
//			// intent.putExtra("name", name2.getText().toString());
//			// intent.putExtra("sex1",sex);
//			// intent.putExtra("tel", change_tel.getText().toString());
//			intent.putExtras(bun);
//			ChangePersonActivity.this.setResult(0, intent);
			

			user.setNickName(name2.getText().toString());
			user.setPhoneNo(change_tel.getText().toString());
			user.setSchool(school.getText().toString());
			user.setSex(sex);
			user.setId(FloatApplication.getApp()
					.getUser(getString(R.string.userFileName)).getId());
			Log.v(TAG, "nickName:"+user.getNickName()+"phoneNo:"+user.getPhoneNo()+"school:"+user.getSchool()+"sex:"+user.getSex()+"id:"+user.getId());
			FloatApplication.getApp().setStoreUser(
					getString(R.string.userFileName), user);
			
			// 开启对话框
			dialog = DialogUtil.createLoadingDialog(ChangePersonActivity.this,"修改中...");
			// 开启线程
			dialog.show ();
			Thread thread = new Thread(new ChangeThread());
			thread.start();
			ChangePersonActivity.this.finish();
		} else {
			// Intent intent=new
			// Intent(this,PersonFragment.getActivity().class);
			// startActivity(intent);
			ChangePersonActivity.this.finish();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			if (dialog != null) {
				dialog.dismiss();
			}
			switch (message.what) {

			case CHANGE_SUCCESS:
				Toast.makeText(getApplicationContext(), "更改成功",
						Toast.LENGTH_SHORT).show();
				break;
			case CHANGE_ERROR:
				Toast.makeText(getApplicationContext(), "更改失败",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	private class ChangeThread implements Runnable {

		@Override
		public void run() {
			//
			String jsonData = new NetCore().Change_person_information(user);
			Message message = new Message();
			message.what = 404;
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				message.what = jsonObject.getInt("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}