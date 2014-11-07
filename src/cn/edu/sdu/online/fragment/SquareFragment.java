package cn.edu.sdu.online.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.activity.DetailedTaskActivity;
import cn.edu.sdu.online.adapter.MylistviewAdapter;
import cn.edu.sdu.online.entity.Task;
import cn.edu.sdu.online.net.NetCore;
import cn.edu.sdu.online.share.FloatApplication;
import cn.edu.sdu.online.util.DialogUtil;
import cn.edu.sdu.online.util.ParseJson;
import cn.edu.sdu.online.util.StaticValues;
import cn.edu.sdu.online.view.OnRefreshListener;
import cn.edu.sdu.online.view.RefreshListView;

public class SquareFragment extends Fragment implements OnClickListener {

	@Override
	public void onPause() {
		// taskList.clear();
		if (mylistviewAdapter != null) {
			mylistviewAdapter.notifyDataSetChanged();
		}

		super.onPause();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// startThread = true;
		// taskList.clear();
		Log.v(TAG, "squareFragmentonResume");
		if (mylistviewAdapter != null) {
			mylistviewAdapter.notifyDataSetChanged();
		}
		// showListByCatogryId(StaticValues.MULTIPLE);

		super.onResume();

	}

	LayoutInflater inflater;
	RefreshListView refreshListView;// 刷新列表
	// private List<ImageAndText> showTaskList = new
	// ArrayList<ImageAndText>();// 用于显示的任务列表
	private List<Task> taskList = new ArrayList<Task>();// 解析得到的任务列表
	private MylistviewAdapter mylistviewAdapter;
	private String TAG = "SquareFragment";
	private Dialog progressDialog;
	private View view;
	private Button button1, button2, button3, button4;
	private Boolean bu1 = true, bu2 = true, bu3 = true, bu4 = true;

	private final static int GET_TASK_LIST = 0;
	private Handler myHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_TASK_LIST:
				if (mylistviewAdapter != null) {
					mylistviewAdapter.notifyDataSetChanged();
				}
				// 设置显示列表

				break;

			default:
				break;
			}

		};

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.square_fragment, container, false);
		button1 = (Button) view.findViewById(R.id.button1);
		button2 = (Button) view.findViewById(R.id.button2);
		button3 = (Button) view.findViewById(R.id.button3);
		button4 = (Button) view.findViewById(R.id.button4);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button1.setOnClickListener(this);
		Log.v(TAG, "squareFragmentonCreate");
		List<Task> tempList = getListFromCache();
		if(tempList==null||tempList.size()==0){
			Log.v(TAG, "缓存为空，远程获取数据");
			new Thread(new SearchThread()).start();
		}
		else {
			Log.v(TAG, "从缓存中获取列表,listSize:"+tempList.size());
			taskList = tempList;
		}
		initSquareLayout();
		return view;

	}

	private List<Task> getListFromCache() {
		List<Task> cacheList;
		cacheList = FloatApplication.getApp().getStoreTaskList(
				StaticValues.STORE_SQUARETASKLIST);
		return cacheList;
	}

	private void initSquareLayout() {
		refreshListView = (RefreshListView) view
				.findViewById(R.id.square_listview);
		refreshListView.setDivider(null);
		initListView();

	}

	private void initListView() {
		mylistviewAdapter = new MylistviewAdapter(getActivity(), taskList);
		refreshListView.setAdapter(mylistviewAdapter);
		refreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 异步查询数据

				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {

						SystemClock.sleep(1000);
						// 做刷新的地方
						// 请求新数据并加入Tasklist

						String jsonResult = new NetCore().GetNewTask(0);

						// 解析jsonResult,初始化tasklist
						taskList.clear();
						List<Task> taskListre = new ParseJson()
								.getTaskListFromJson(jsonResult);
						for (int i = 0; i < taskListre.size(); i++) {
							taskList.add(taskListre.get(i));
						}
						FloatApplication.getApp().setStoreRelTask(
								StaticValues.STORE_SQUARETASKLIST, taskList);
						return null;
					}

					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						// mylistviewAdapter.notifyDataSetChanged();
						Message message = new Message();
						message.what = GET_TASK_LIST;
						myHandler.sendMessage(message);
						// 隐藏头布局
						refreshListView.onRefreshFinish();
					}
				}.execute(new Void[] {});
			}

			@Override
			public void onLoadMoring() {

				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(2000);
						// 坐加载的地方
						String jsonResult = new NetCore().GetNewTask(taskList
								.size());

						// 解析jsonResult,初始化tasklist
						
						List<Task> taskListre = new ParseJson()
								.getTaskListFromJson(jsonResult);
						for (int i = 0; i < taskListre.size(); i++) {
							taskList.add(taskListre.get(i));
						}
						FloatApplication.getApp().setStoreRelTask(
								StaticValues.STORE_SQUARETASKLIST, taskList);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);

						// mylistviewAdapter.notifyDataSetChanged();
						Message message = new Message();
						message.what = GET_TASK_LIST;
						myHandler.sendMessage(message);
						refreshListView.onRefreshFinish();
					}

				}.execute(new Void[] {});
			}
		});
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v(TAG, "arg2" + arg2 + "arg3" + arg3);

				// String taskId = taskListFromNet.get(arg2).getTaskId();
				// // 向服务器发送商品名称和信息，请求具体信息
				// Log.v(TAG, "" + goodsName + "" + goodsPrice + " " + "  "
				// + goodsId);
				Bundle bundle = new Bundle();
				bundle.putSerializable("task", taskList.get(arg2 - 1));

				Intent intent = new Intent(getActivity(),
						DetailedTaskActivity.class);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);

			}
		});

	}

	private void getTaskByCon(final int id, final String school) {

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				SystemClock.sleep(1000);
				// 做刷新的地方
				// 请求新数据并加入Tasklist

				String jsonResult;
				if (id == R.id.button1) {
					jsonResult = new NetCore().GetNewTask(0);
				} else if (id == R.id.button2) {
					jsonResult = new NetCore().GetSchoolTask(0, school);
				} else if (id == R.id.button3) {
					jsonResult = new NetCore().GetUrgeTask(0);
				} else {
					jsonResult = new NetCore().GetTipTask(0);
				}
				Log.v(TAG, jsonResult);
				taskList.clear();
				List<Task> taskListre = new ParseJson()
						.getTaskListFromJson(jsonResult);
				for (int i = 0; i < taskListre.size(); i++) {
					taskList.add(taskListre.get(i));
				}
				FloatApplication.getApp().setStoreRelTask(
						StaticValues.STORE_SQUARETASKLIST, taskList);
				return null;
			}

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				// mylistviewAdapter.notifyDataSetChanged();
				Message message = new Message();
				message.what = GET_TASK_LIST;
				myHandler.sendMessage(message);
				// 隐藏头布局
				if (progressDialog != null) {
					Log.v(TAG, "对话框快消失！");
					progressDialog.dismiss();
				}
				refreshListView.onRefreshFinish();
			}
		}.execute(new Void[] {});
	}

	// private void initShowTaskList(List<Task> taskList) {
	// for (int i = 0; i < taskList.size(); i++) {
	//
	// String content = taskList.get(i).getContent();
	//
	// // int awardCatgory = taskList.get(i).getAwardCatogry();
	// String award = taskList.get(i).getSpiritAward();
	//
	// String deadLine = taskList.get(i).getLimitTime();
	// // String imageName = taskList.get(i).getHeadPhoto();
	// // String imageUrl = NetCore.DownloadPictruesAddr + "/" + userId +
	// // "/"
	// // + imageName;
	// // showTaskList.add(new ImageAndText("", "", content,
	// // award, deadLine));
	// }
	//
	// }

	private class SearchThread implements Runnable {

		@Override
		public void run() {
			String jsonResult = new NetCore().GetNewTask(0);
			Log.v(TAG, jsonResult);
			// 解析jsonResult,初始化tasklist
			List<Task> list = new ParseJson().getTaskListFromJson(jsonResult);
			if(list==null);
			else{
			for (int i = 0; i < list.size(); i++) {
				taskList.add(list.get(i));
			}}
			FloatApplication.getApp().setStoreRelTask(
					StaticValues.STORE_SQUARETASKLIST, taskList);
try {
	Log.v(TAG, taskList.get(0).getContent() + "  " + taskList.size());
} catch (Exception e) {
	// TODO: handle exception
}
			
			// 初始化showTaskList
			// initShowTaskList(taskList);
			Message message = new Message();
			message.what = GET_TASK_LIST;
			myHandler.sendMessage(message);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		progressDialog =DialogUtil.createLoadingDialog(getActivity(),"加载中...");
		progressDialog.show ();
		switch (v.getId()) {
		case R.id.button1:
			if (bu1 == true) {
				button1.setTextColor(Color.parseColor("#ffb652"));
				bu1 = false;
			} else {
				button1.setTextColor(Color.parseColor("#00C5CD"));
				bu1 = true;
			}
			// 得到最近
			getTaskByCon(R.id.button1, "");
			break;
		case R.id.button2:
			if (bu2 == true) {
				button2.setTextColor(Color.parseColor("#ffb652"));
				bu2 = false;
			} else {
				button2.setTextColor(Color.parseColor("#00C5CD"));
				bu2 = true;
			}
			// 得到本校
			getTaskByCon(
					R.id.button2,
					FloatApplication.getApp()
							.getUser(getString(R.string.userFileName))
							.getSchool());
			break;
		case R.id.button3:
			if (bu3 == true) {
				button3.setTextColor(Color.parseColor("#ffb652"));
				bu3 = false;
			} else {
				button3.setTextColor(Color.parseColor("#00C5CD"));
				bu3 = true;
			}
			// 得到最紧急
			getTaskByCon(R.id.button3, "");
			break;
		case R.id.button4:
			if (bu4 == true) {
				button4.setTextColor(Color.parseColor("#ffb652"));
				bu4 = false;
			} else {
				button4.setTextColor(Color.parseColor("#00C5CD"));
				bu4 = true;
			}
			// 得到有金钱悬赏的
			getTaskByCon(R.id.button4, "");
			break;
		default:
			break;
		}

	}

}
