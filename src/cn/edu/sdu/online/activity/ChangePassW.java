package cn.edu.sdu.online.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.entity.User;
import cn.edu.sdu.online.net.NetCore;
import cn.edu.sdu.online.share.FloatApplication;
import cn.edu.sdu.online.util.DialogUtil;

public class ChangePassW extends Activity implements OnClickListener {
	private EditText text1;
	private EditText text2;
	private EditText text3;
	private Button quxiao;
	private Button baocun;
	Dialog dialog;
	User user = new User();
	String newpassword;
	String TAG = "ChangePassW";
	private final static int CHANGE_SUCCESS = 1;// 改变成功
	private final static int CHANGE_ERROR = 0;// 改变失败
	private final static int CHANGE_DENY = 2;// 与原密码不一致
	
	//返回按钮
	ImageView button_back_image;
	TextView button_back_text;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.change_pass_w);
		initialization();
	}

	private void initialization() {
		
		button_back_image=(ImageView)findViewById(R.id.changepw_button_back);
		button_back_text=(TextView)findViewById(R.id.changepw_button_back_text);
		button_back_image.setOnClickListener(new BackListener());
		button_back_text.setOnClickListener(new BackListener());
		
		text1 = (EditText) this.findViewById(R.id.editText1);
		text2 = (EditText) this.findViewById(R.id.editText2);
		text3 = (EditText) this.findViewById(R.id.editText3);
		quxiao = (Button) this.findViewById(R.id.quxiao);
		baocun = (Button) this.findViewById(R.id.baocun);
		quxiao.setOnClickListener(this);
		baocun.setOnClickListener(this);
	}
	
	class BackListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			ChangePassW.this.finish();
		}
	}

	@Override
	public void onClick(View v) {
		String oldPass = text1.getText().toString();
		String newPass = text2.getText().toString();
		String newconfirm = text3.getText().toString();
		switch (v.getId()) {
		case R.id.baocun:
			if (oldPass.trim().length() == 0 || newPass.trim().length() == 0
					|| newconfirm.trim().length() == 0) {
				Toast.makeText(this, "不能为空", 1000).show();
				return;
			}
			if (!newPass.equals(newconfirm)) {
				Toast.makeText(this, "前后密码不一致", 1000).show();
				return;
			}

			// else if (str1 != "           ...")// 原先的密码
			// Toast.makeText(this, "原密码输入不正确", 1000).show();

			// else {
			// Toast.makeText(this, "修改成功", 1000).show();
			// this.finish();
			// }
			// 打开对话框
			user.setId(FloatApplication.getApp()
					.getUser(getString(R.string.userFileName)).getId());
			user.setPassword(oldPass);
			newpassword = newPass;
			
			dialog = DialogUtil.createLoadingDialog(this,"修改中...");
			dialog.show ();
			Thread thread = new Thread(new ChangeThread());
			thread.start();
			break;
		case R.id.quxiao:
			this.finish();
			break;
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			if(dialog!=null){
				dialog.dismiss();
			}
			switch (message.what) {
			case CHANGE_SUCCESS:
				Toast.makeText(ChangePassW.this, "修改成功", Toast.LENGTH_SHORT)
						.show();
				break;
			case CHANGE_DENY:
				Toast.makeText(ChangePassW.this, "原密码输入错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case CHANGE_ERROR:
				Toast.makeText(ChangePassW.this, "修改失败", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
		}
	};

	private class ChangeThread implements Runnable {

		@Override
		public void run() {
			String jsonReSult = new NetCore().ChangePassword(user, newpassword);
			Log.v(TAG, "jsonReSult"+jsonReSult);
			Message message = new Message();
			message.what = 404;
			try {
				JSONObject jsonObject =new JSONObject(jsonReSult);
				
				message.what = jsonObject.getInt("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendMessage(message);
			
			
		}
	}
}
