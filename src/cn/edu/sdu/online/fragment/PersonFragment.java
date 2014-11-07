package cn.edu.sdu.online.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.activity.About;
import cn.edu.sdu.online.activity.ChangePassW;
import cn.edu.sdu.online.activity.ChangePersonActivity;
import cn.edu.sdu.online.activity.LoginActivity;
import cn.edu.sdu.online.adapter.MessageListAdapter;
import cn.edu.sdu.online.chatservice.ChatwithService;
import cn.edu.sdu.online.chatservice.ChatwithService.ChatBinder;
import cn.edu.sdu.online.entity.User;
import cn.edu.sdu.online.share.FloatApplication;
import cn.edu.sdu.online.view.MyAnimations;
import cn.edu.sdu.online.view.RefreshListView;

import com.umeng.fb.FeedbackAgent;

public class PersonFragment extends Fragment {
	

	private TextView change_tel;
	private TextView name2;
	private TextView sex_edit;
	private TextView school;
	private TextView schoolcontent;
	private TextView nickname;
	private RefreshListView message;
	private RelativeLayout composerButtonsShowHideButton;
	private ImageView composerButtonsShowHideButtonIcon,userface;
	private RelativeLayout composerButtonsWrapper;
	private boolean areButtonsShowing;
	MessageListAdapter myadapter;
	private String TAG = "PersonFragment";
	private ArrayList<HashMap<String, String>> listItem;// 生成动态数组，加入数据
	int sex = 0;// "0" is girl; "1" is boy;
	String[] sexs = { "   我是美女", "   我是帅哥" };
	private ChatwithService chatservice;
	private boolean bound=false;
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String SHARE_LOGIN_SUCCESS = "SHARE_LOGIN_SUCCESS";
	FloatApplication app=FloatApplication.getApp();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		


	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		name2.setText(app.getUser(getString(R.string.userFileName)).getNickName());
		change_tel.setText(app.getUser(getString(R.string.userFileName)).getPhoneNo());
		schoolcontent.setText(app.getUser(getString(R.string.userFileName)).getSchool());
		if(app.getUser(getString(R.string.userFileName)).getSex()==1)
			userface.setImageResource(R.drawable.boy);
		else 
			userface.setImageResource(R.drawable.girl);
		if(app.getUser(getString(R.string.userFileName)).getSex()==0){
			sex_edit.setText("我是美女");
		}
		if(app.getUser(getString(R.string.userFileName)).getSex()==1){
			sex_edit.setText("我是帅哥");
		}
		
		Intent intent = new Intent(getActivity(), ChatwithService.class);
		getActivity().bindService(intent, serviceConnection, Context.BIND_IMPORTANT);
		Log.v(TAG, "PersonFragmentonResume");
	}
	
	public void onPause() {
		super.onPause();
		getActivity().unbindService(serviceConnection);
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
			bound = true;
		}
	};

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);
		View view4 = inflater.inflate(R.layout.fourth_frag, null);
		Log.v(TAG, "PersonFragmentonCreate");

		userface=(ImageView)view4.findViewById(R.id.userface);
		if(app.getUser(getString(R.string.userFileName)).getSex()==1)
			userface.setImageResource(R.drawable.boy);
		else 
			userface.setImageResource(R.drawable.girl);
		composerButtonsWrapper = (RelativeLayout) view4
				.findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) view4
				.findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) view4
				.findViewById(R.id.composer_buttons_show_hide_button_icon);
		setListener();

		change_tel = (TextView) view4.findViewById(R.id.change_tel);
		change_tel.setText(app.getUser(getString(R.string.userFileName)).getPhoneNo());
		name2 = (TextView) view4.findViewById(R.id.name2);
		name2.setText(app.getUser(getString(R.string.userFileName)).getNickName());
		school = (TextView) view4.findViewById(R.id.school);
		//学校的名称
		schoolcontent = (TextView) view4.findViewById(R.id.schoolcontent);
		schoolcontent.setText(app.getUser(getString(R.string.userFileName)).getSchool());
		sex_edit = (TextView) view4.findViewById(R.id.sex);
		if(app.getUser(getString(R.string.userFileName)).getSex()==0){
			sex_edit.setText("我是美女");
		}
		if(app.getUser(getString(R.string.userFileName)).getSex()==1){
			sex_edit.setText("我是帅哥");
		}
		
		

		return view4;
	}

	private void setListener() {
		// 给大按钮设置点击事件
		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!areButtonsShowing) {
					// 图标的动画
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
					// 加号的动画
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(0,
									-225, 300));
				} else {
					// 图标的动画
					MyAnimations
							.startAnimationsOut(composerButtonsWrapper, 300);
					// 加号的动画
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(
									-225, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});

		// 给小图标设置点击事件
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			final ImageView smallIcon = (ImageView) composerButtonsWrapper
					.getChildAt(i);
			final int position = i;
			smallIcon.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					// 这里写各个item的点击事件
					// 1.加号按钮缩小后消失 缩小的animation
					// 2.其他按钮缩小后消失 缩小的animation
					// 3.被点击按钮放大后消失 透明度渐变 放大渐变的animation
					// composerButtonsShowHideButton.startAnimation(MyAnimations.getMiniAnimation(300));

					if (areButtonsShowing) {
						composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-225, 0, 300));
						smallIcon.startAnimation(MyAnimations
								.getMaxAnimation(400));
						for (int j = 0; j < composerButtonsWrapper
								.getChildCount(); j++) {
							if (j != position) {
								final ImageView smallIcon = (ImageView) composerButtonsWrapper
										.getChildAt(j);
								smallIcon.startAnimation(MyAnimations
										.getMiniAnimation(300));
								// MyAnimations.getMiniAnimation(300).setFillAfter(true);
							} else {
								Setting(j);
							}
						}
						areButtonsShowing = !areButtonsShowing;
					}

				}
			});
		}
	}

	private void Setting(int i) {
		// 按钮操作
		if (i == 0) {
			Intent intent = new Intent(getActivity(), About.class);
			startActivity(intent);
		}
		if (i == 1) {
			// 反馈
			FeedbackAgent agent = new FeedbackAgent(getActivity());
			agent.sync();
			agent.startFeedbackActivity();
		}
		if (i == 2) {
			Intent intent = new Intent(getActivity(), ChangePassW.class);
			startActivityForResult(intent, 0);

		}
		if (i == 3) {
			// 修改资料
			Intent intent = new Intent(getActivity(),
					ChangePersonActivity.class);
			startActivityForResult(intent, 0);

		}
		if (i == 4) {



			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT,
					"PRE团队推出了一款移动互帮助手啦！很棒，只要你想，世界都会帮你哦~~亲们快去下吧~~!");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);

		}
		if (i == 5) {



			// 退出
			chatservice.disconnect();
			SharedPreferences share = getActivity().getSharedPreferences(SHARE_LOGIN_TAG, Context.MODE_PRIVATE);
			share.edit().putBoolean(SHARE_LOGIN_SUCCESS, false).commit();
//			getActivity().unbindService(serviceConnection);
			getActivity().finish();
		}

	}
	
	// @Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == 0 && resultCode == 0 && null != data) {
//			Bundle bun = data.getExtras();
//			change_tel.setText(bun.getString("tel"));
//			name2.setText(bun.getString("name"));
//			if(FloatApplication.getApp().getUser(getString(R.string.userFileName)).getSex()==0){
//				sex_edit.setText("我是美女");userface.setImageResource(R.drawable.girl);
//			}
//			if(FloatApplication.getApp().getUser(getString(R.string.userFileName)).getSex()==1){
//				sex_edit.setText("我是帅哥");userface.setImageResource(R.drawable.boy);
//			}
//			schoolcontent.setText( bun.getString("school"));
//
//		}
//	}

}
