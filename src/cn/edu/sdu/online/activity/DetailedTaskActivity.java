package cn.edu.sdu.online.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.adapter.MessageListAdapter;
import cn.edu.sdu.online.entity.Task;
import cn.edu.sdu.online.util.ConvertString;

public class DetailedTaskActivity extends Activity {
	String TAG = "DetailedTaskActivity";
	private Button accept;
	private Button ignore;
	private Button message;
	private ImageView faceimage;
	private TextView name;
	private TextView squarescore;
	private TextView school;
	private TextView lefttime;
	private TextView reward;
	private TextView messageBox;
	private TextView formaltext, detail, change_tel, grade;
	private boolean areButtonsShowing;
	ListView refreshListView;
	MessageListAdapter myadapter;
	private ArrayList<HashMap<String, String>> listItem;// 生成动态数组，加入数据
	Task task;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.detailed_task_info);
		initialize();
		ignore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

	}

	class messageListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// sendEmptyMessage:发送一条消息

			Intent intent = new Intent();
			intent.setClass(DetailedTaskActivity.this, ChatClient.class);
			String email = task.getEmail();
			intent.putExtra("USERIDTO", ConvertString.convert(email));

			startActivity(intent);
			// DetailedTaskActivity.this.finish();
		}

	}

	private void initialize() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		task = (Task) bundle.getSerializable("task");
		Log.v(TAG, task.getContent());
		// accept = (Button) this.findViewById(R.id.accept);
		ignore = (Button) this.findViewById(R.id.ignore);
		message = (Button) this.findViewById(R.id.message);
		message.setOnClickListener(new messageListener());
		name = (TextView) this.findViewById(R.id.name);
		name.setText("昵称:" + task.getNickName());
		school = (TextView) this.findViewById(R.id.school);
		school.setText("学校:" + task.getSchool());
		lefttime = (TextView) this.findViewById(R.id.time);
		String str = task.getLimitTime().toString();
		if (str != null&&str.length()==8) {
			String str1 = str.substring(0, 4);
			String str2 = str.substring(4, 6);
			String str3 = str.substring(6, 8);
			lefttime.setText("有效期:" + str1 + "年" + str2 + "月" + str3 + "日");
		} else
			lefttime.setText("长期有效");
		// lefttime.setText(task.getLimitTime()+"");
		formaltext = (TextView) this.findViewById(R.id.task);
		formaltext.setText("任务:" + task.getContent());
		reward = (TextView) this.findViewById(R.id.reward);
		if (task.getAwardStatus() == 0) {
			reward.setText("奖励:" + task.getTipAward() + "元");
		}
		if (task.getAwardStatus() == 1) {
			reward.setText(task.getSpiritAward());
		}
		detail = (TextView) this.findViewById(R.id.detail);
		detail.setText("任务详情:" + task.getDetails());
		change_tel = (TextView) findViewById(R.id.change_tel);
		change_tel.setText(task.getPhoneNo());
		grade = (TextView) findViewById(R.id.grade);
		grade.setText("等级:" + task.getLevel() + "级");
		refreshListView = (ListView) this.findViewById(R.id.listView);
		messageBox = (TextView) findViewById(R.id.messagebox);
		messageBox.setOnClickListener(new messageBoxListener());
	}

	class messageBoxListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(DetailedTaskActivity.this, MessageBox.class);
			startActivity(intent);

		}

	}
}
