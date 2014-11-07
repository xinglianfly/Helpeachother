package cn.edu.sdu.online.activity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.entity.Task;
import cn.edu.sdu.online.net.NetCore;
import cn.edu.sdu.online.share.FloatApplication;

public class PublishTaskActivity extends Activity {

	String TAG = "PublishTaskActivity";
	String Now[];
	int intDate, setDate;// 当前日期
	int intTime, setTime;// 当前时间
	boolean ifClickMoney = false, ifClickSpirit = false;
	String limitTime;// 截止日期
	ImageView money_button, spirit_button, publish_button;// , score,
															// addDetailDesc;//,
															// addDescri;

	LinearLayout money_firstshow;
	LinearLayout money_secondshow;
	LinearLayout spirit_firstshow;
	LinearLayout spirit_secondshow;

	ViewStub money_first;
	ViewStub money_second;
	ViewStub spirit_first;
	ViewStub spirit_second;

	EditText input_money_award, input_spirit_award;
	EditText edittext_describe, edittext_detil;
	// LinearLayout addDesL;
	// ViewStub adddes;
	FloatApplication app;
	// 屏幕数据
	double screenWidth;
	double screenHight;
	double screenDensity;
	// Task
	Task task = new Task();
	// 信息变量
	int award_money;
	String award_spirit;
	String describe, describe_detil;
	Date sdf;
	boolean dataFired = false;// 确定timedialog只调用一次；
	boolean timeFired = false;
	// 时间选择
	TextView texeview_deadline, texeview_deadtime;
	Button button_selecttime;
	Button button_selectdate;
	String deadline, deadtime;

	// 返回按钮
	ImageView button_back_image;
	TextView button_back_text;

	TextView t;
	TextView s;

	int awardStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.publish_task_activity);
		app = FloatApplication.getApp();
		money_button = (ImageView) findViewById(R.id.pubilsh_image_money);
		spirit_button = (ImageView) findViewById(R.id.pubilsh_image_spirit);
		// score = (ImageView) findViewById(R.id.pubilsh_image_score);

		money_button.setOnClickListener(new moneyListener());

		money_first = (ViewStub) findViewById(R.id.money_firstshow);
		money_first.inflate();
		money_first.setVisibility(View.INVISIBLE);

		money_second = (ViewStub) findViewById(R.id.money_secondshow);
		money_second.inflate();
		money_second.setVisibility(View.INVISIBLE);

		input_money_award = (EditText) findViewById(R.id.input_money_award);

		spirit_button.setOnClickListener(new spiritListener());

		spirit_first = (ViewStub) findViewById(R.id.spirit_firstshow);
		spirit_first.inflate();
		spirit_first.setVisibility(View.INVISIBLE);

		spirit_second = (ViewStub) findViewById(R.id.spirit_secondshow);
		spirit_second.inflate();
		spirit_second.setVisibility(View.INVISIBLE);

		input_spirit_award = (EditText) findViewById(R.id.input_spirit_award);

		publish_button = (ImageView) this.findViewById(R.id.realease_image);
		publish_button.setOnClickListener(new publishListener());

		edittext_describe = (EditText) this.findViewById(R.id.describe);
		edittext_detil = (EditText) this.findViewById(R.id.describe_detil);

		texeview_deadline = (TextView) findViewById(R.id.public_textview_deadline);
		texeview_deadtime = (TextView) findViewById(R.id.public_textview_deadtime);

		button_selecttime = (Button) findViewById(R.id.public_button_select_time);
		View.OnClickListener timeBtnListener = new BtnOnClickListener(
				TIME_DIALOG);
		button_selecttime.setOnClickListener(timeBtnListener);

		button_selectdate = (Button) findViewById(R.id.public_button_select_date);
		View.OnClickListener dateBtnListener = new BtnOnClickListener(
				DATE_DIALOG);
		button_selectdate.setOnClickListener(dateBtnListener);

		t = (TextView) findViewById(R.id.first_second_text);
		s = (TextView) findViewById(R.id.spirit_second_text);

		button_back_image = (ImageView) findViewById(R.id.publish_button_back);
		button_back_text = (TextView) findViewById(R.id.publish_button_back_text);
		button_back_image.setOnClickListener(new BackListener());
		button_back_text.setOnClickListener(new BackListener());
		// addDesL = (LinearLayout) findViewById(R.id.adddesLayout);
		// addDescri = (ImageView) findViewById(R.id.adddesc_button);
		// addDescri.setOnClickListener(new addDescListener());
		// adddes = (ViewStub) findViewById(R.id.adddetail_stub);
		// adddes.inflate();
		// adddes.setVisibility(View.GONE);
		// addDetailDesc = (ImageView) findViewById(R.id.adddetail_button);
		// addDetailDesc.setOnClickListener(new addDetailedDesLis());

		getScreenSize();
		setSize();
		setTime();

		// firstshow = (LinearLayout)findViewById(R.id.firstshowtest);
		// firstshow.setVisibility(View.INVISIBLE);
		// secondshow =(LinearLayout)findViewById(R.id.secondshowtest);
		// secondshow.setVisibility(View.INVISIBLE);

	}

	// class addDetailedDesLis implements OnClickListener {
	//
	// @Override
	// public void onClick(View v) {
	//
	// //动画/////////////////////////////////////////////////////////////////////////////////
	// adddes.setVisibility(View.GONE);
	// ObjectAnimator.ofFloat(addDesL, "translationY", 50, 0)
	// .setDuration(100).start();
	// addDescri.setEnabled(true);
	// }
	//
	// }
	//
	// class addDescListener implements OnClickListener {
	//
	// @Override
	// public void onClick(View v) {
	// ObjectAnimator.ofFloat(addDesL, "translationY", 0, 20)
	// .setDuration(200).start();
	// addDescri.setEnabled(false);
	// new Handler().postDelayed(new Runnable() {
	// public void run() {
	// adddes.setVisibility(View.VISIBLE);
	//
	// }
	// }, public_textview_deadlinepublic_textview_deadlinew100);
	//
	// }
	//
	// }

	// award------------------------------------------------------------------------------
	class moneyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (ifClickMoney == false) {
				// secondshow.setVisibility(View.INVISIBLE);
				// firstshow.setVisibility(View.VISIBLE);
				money_first.setVisibility(View.VISIBLE);
				money_second.setVisibility(View.GONE);
				if (ifClickSpirit == true) {

					spirit_first.setVisibility(View.GONE);
					spirit_second.setVisibility(View.VISIBLE);
					s.setText("");
					ifClickSpirit = false;
				}
				ifClickMoney = true;
			} else {
				if (!input_money_award.getText().toString().equals("")) {
					// firstshow.setVisibility(View.INVISIBLE);
					// secondshow.setVisibility(View.VISIBLE);
					String inputmoney = input_money_award.getText().toString();
					Pattern pattern = Pattern.compile("[0-9]{1,}");
					Matcher matcher = pattern
							.matcher((CharSequence) inputmoney);
					if (matcher.matches()) {
						t.setText(input_money_award.getText().toString()
								+ "元小费");
					} else {
						new AlertDialog.Builder(PublishTaskActivity.this)
								.setTitle("请输入标准的数字格式").setPositiveButton("确定",

								new Dialog.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).create().show();
					}

					s.setText("");

					awardStatus = 0;
					// TODO　这里需要加是否是整数的判断
					try {
						award_money = Integer.parseInt(input_money_award
								.getText().toString());

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				money_first.setVisibility(View.GONE);
				money_second.setVisibility(View.VISIBLE);

				ifClickMoney = false;

			}

		}

	}

	class BackListener implements OnClickListener {
		public void onClick(View v) {
			PublishTaskActivity.this.finish();
		}
	}

	class spiritListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (ifClickSpirit == false) {
				// secondshow.setVisibility(View.INVISIBLE);
				// firstshow.setVisibility(View.VISIBLE);
				spirit_first.setVisibility(View.VISIBLE);
				spirit_second.setVisibility(View.GONE);
				if (ifClickMoney == true) {
					money_first.setVisibility(View.GONE);
					money_second.setVisibility(View.VISIBLE);

					t.setText("");
					ifClickMoney = false;
				}
				ifClickSpirit = true;
			} else {
				if (!input_spirit_award.getText().toString().equals("")) {
					// firstshow.setVisibility(View.INVISIBLE);
					// secondshow.setVisibility(View.VISIBLE);
					s.setText(input_spirit_award.getText().toString());
					t.setText("");

					awardStatus = 1;

					award_spirit = input_spirit_award.getText().toString();
				}
				spirit_first.setVisibility(View.GONE);
				spirit_second.setVisibility(View.VISIBLE);

				ifClickSpirit = false;

			}

		}

	}

	// time--------------------------------------------------------------------------------

	public void setTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd HH mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		Log.v(TAG, str);

		Now = str.split(" ");

		texeview_deadline.setText(Now[0] + "年" + Now[1] + "月" + Now[2] + "日");
		texeview_deadtime.setText(Now[3] + "时" + Now[4] + "分");
		String nowdate = Arrays.toString(Now);
		String str1 = Now[0] + Now[1] + Now[2];
		intDate = Integer.parseInt(str1);
		String str2 = Now[3] + Now[4];
		intTime = Integer.parseInt(str2);
		limitTime = Now[0] + "" + Now[1] + "" + Now[2];
	}

	private final int DATE_DIALOG = 1;

	private final int TIME_DIALOG = 2;

	protected Dialog onCreateDialog(int id) {
		// 用来获取日期和时间的
		Calendar calendar = Calendar.getInstance();

		Dialog dialog = null;
		switch (id) {
		case DATE_DIALOG:

			DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker datePicker, int year,
						int month, int dayOfMonth) {

					// Calendar月份是从0开始,所以month要加1
					String mmmm = (month + 1) + "", dddd = dayOfMonth + "";
					if (month < 9)
						mmmm = "0" + mmmm;
					if (dayOfMonth < 10)
						dddd = "0" + dddd;
					setDate = Integer.parseInt(year + mmmm + dddd);
					Log.v(TAG, "DATE_DIALOG");
					if (intDate > setDate) {
						if (dataFired == true) {
							return;
						} else {
							dataFired = true;
							new AlertDialog.Builder(PublishTaskActivity.this)
									.setTitle("错误").setMessage("请输入合适的时间")
									.setPositiveButton("确定",

									null).show();
						}
					}

					else {
						texeview_deadline.setText(year + "年" + mmmm + "月"
								+ dddd + "日");
						// ///////////////////////////////////////////////
						limitTime = year + "" + mmmm + "" + dddd;
					}

				}
			};
			dialog = new DatePickerDialog(this, dateListener,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			break;
		case TIME_DIALOG:

			TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker timerPicker, int hourOfDay,
						int minute) {

					// EditText editText =
					// (EditText) findViewById(R.id.editText);
					String hhhh = hourOfDay + "", mmmm = minute + "";
					if (hourOfDay < 10)
						hhhh = "0" + hhhh;
					if (minute < 10)
						mmmm = "0" + mmmm;
					setTime = Integer.parseInt(hhhh + mmmm);
					if ((intDate >= setDate) && setTime < intTime) {
						if (timeFired == true) {
							return;
						} else {
							timeFired = true;
							new AlertDialog.Builder(PublishTaskActivity.this)
									.setTitle("时间设置错误").setPositiveButton("确定",

									new Dialog.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();
						}

					} else
						texeview_deadtime.setText(hhhh + "时" + mmmm + "分");
				}
			};
			dialog = new TimePickerDialog(this, timeListener,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), true); // 是否为二十四制
			break;
		default:
			break;
		}
		return dialog;
	}

	/*
	 * 成员内部类,此处为提高可重用性，也可以换成匿名内部类
	 */
	private class BtnOnClickListener implements View.OnClickListener {

		private int dialogId = 0; // 默认为0则不显示对话框

		public BtnOnClickListener(int dialogId) {
			this.dialogId = dialogId;
		}

		@Override
		public void onClick(View view) {
			showDialog(dialogId);
		}

	}

	// public button
	// --------------------------------------------------------------------
	private class publishListener implements OnClickListener {

		// int awardStatus = 0;
		//
		// int tipAward =
		// Integer.parseInt(input_money_award.getText().toString());
		//
		// String inputSpirit = input_spirit_award.getText().toString();
		//
		// String Describe = edittext_describe.getText().toString();
		// String Detil = edittext_detil.getText().toString();

		// String limitTime =

		@Override
		public void onClick(View v) {
			// award_money;award_spirit;describe;describe_detil
			// 判断电话是否为空
			//
			String phone = app.getUser(getString(R.string.userFileName))
					.getPhoneNo();
			if (phone.length() == 0) {
				new AlertDialog.Builder(PublishTaskActivity.this)
						.setTitle("请完善用户电话等信息").setPositiveButton("确定",

						new Dialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										PublishTaskActivity.this,
										ChangePersonActivity.class);
								startActivity(intent);

							}
						}).create().show();
			} else {
				Log.v(TAG,
						FloatApplication.getApp()
								.getUser(getString(R.string.userFileName))
								.getId());
				task.setUserId(FloatApplication.getApp()
						.getUser(getString(R.string.userFileName)).getId());

				// String content=edittext_describe.getText().toString();

				task.setContent(edittext_describe.getText().toString());
				int aw = awardStatus;
				task.setAwardStatus(aw);
try {
	task.setTipAward(Integer.parseInt(input_money_award.getText()
			.toString()));
} catch (Exception e) {
	// TODO: handle exception
}
				

				task.setSpiritAward(input_spirit_award.getText().toString()
						+ "");

				Log.v(TAG, "limitTime" + limitTime);
				task.setLimitTime(limitTime);
				task.setDetails(edittext_detil.getText().toString());

				Thread thread = new Thread(new PublishThread());
				thread.start();

			}
		}
	}

	//

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				// 发布失败
				Toast.makeText(getApplicationContext(), "任务发布失败，请检查您的任务！",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				// 发布成功
				Toast.makeText(getApplicationContext(), "任务发布成功，帮您的人马上来！",
						Toast.LENGTH_SHORT).show();
				PublishTaskActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};

	class PublishThread implements Runnable {

		@Override
		public void run() {
			String jsonData = new NetCore().PublishTask(task);
			Log.v(TAG, "jsonData:" + jsonData);
			Message message = new Message();
			message.what = 404;
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				message.what = jsonObject.getInt("result");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			handler.sendMessage(message);

		}

	}

	// 屏幕操作
	public void getScreenSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHight = metrics.heightPixels;
		screenDensity = metrics.density;
	}

	private void setSize() {
		setSize(R.id.pubilsh_image_money, screenWidth / 2, 15, -1, 0);
		// setSize(R.id.pubilsh_image_score,screenWidth/3,8,-1,0);
		setSize(R.id.pubilsh_image_spirit, screenWidth / 2, 15, -1, 0);
		setSize(R.id.input_money_award, screenWidth / 4, 20, 30, 0);
		setSize(R.id.input_spirit_award, screenWidth / 4, 20, 30, 0);
		setSize(R.id.public_textview_deadline, 3 * screenWidth / 4, 20, 30, 0);
		setSize(R.id.public_textview_deadtime, 3 * screenWidth / 4, 20, 30, 0);
		setSize(R.id.public_button_select_time, 1 * screenWidth / 4, 0, 30, 0);
		setSize(R.id.public_button_select_date, 1 * screenWidth / 4, 0, 30, 0);

		setSize2(R.id.publish_area_left, -1, 0, screenHight / 6, 0);
		setSize2(R.id.publish_area_right, -1, 0, screenHight / 6, 0);
	}

	public void setSize(int id, double w_p, int w_m, double h_p, int h_m) {
		android.view.ViewGroup.LayoutParams lp = this.findViewById(id)
				.getLayoutParams();
		if (w_p != -1)
			lp.width = (int) (w_p - w_m * screenDensity);
		if (h_p != -1)
			lp.height = (int) (h_p * screenDensity - h_m * screenDensity);

	}

	public void setSize2(int id, double w_p, int w_m, double h_p, int h_m) {
		android.view.ViewGroup.LayoutParams lp = this.findViewById(id)
				.getLayoutParams();
		if (w_p != -1)
			lp.width = (int) (w_p * screenDensity - w_m * screenDensity);
		if (h_p != -1)
			lp.height = (int) (h_p - h_m * screenDensity);

	}

}
