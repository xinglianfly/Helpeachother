package cn.edu.sdu.online.subfragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.activity.DetailedTaskActivity;
import cn.edu.sdu.online.adapter.ReleaseTaskAdapter;
import cn.edu.sdu.online.entity.Task;
import cn.edu.sdu.online.net.NetCore;
import cn.edu.sdu.online.share.FloatApplication;
import cn.edu.sdu.online.util.DialogUtil;
import cn.edu.sdu.online.util.ParseJson;
import cn.edu.sdu.online.util.StaticValues;
import cn.edu.sdu.online.view.OnRefreshListener;
import cn.edu.sdu.online.view.RefreshListView;

public class ReleaseTask extends Fragment {
	
	String TAG = "ReleaseTask";
	LayoutInflater inflater;
	private List<Task> taskListFromNet = new ArrayList<Task>();// 解析得到的任务列表
	RefreshListView releaseList;
	ReleaseTaskAdapter adapter;
	private static final int FINISH_SUCCESS = 1;
	private static final int FINISH_FAILED = 0;
	private final static int GET_TASK_LIST = 2;
	View view;
	Dialog progressDialog;
	@Override
	public void onPause() {

		super.onPause();

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		Log.v(TAG, "ReleaseTaskonCreateView");
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	
		super.onResume();
	}
	private Handler myHandler = new Handler() {

		public void handleMessage(Message msg) {
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case GET_TASK_LIST:
				// taskListFromNet中已有数据，不信你可以打印一下
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}

				break;
			case FINISH_SUCCESS:
				// 刷新数据
				Toast.makeText(getActivity().getApplicationContext(), "设置成功",
						Toast.LENGTH_SHORT).show();
				break;
			case FINISH_FAILED:
				Toast.makeText(getActivity().getApplicationContext(), "设置失败",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

		};

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		view = inflater.inflate(R.layout.receivetask_fragment, null);
		Log.v(TAG, "ReleaseTaskonCreateView");
		List<Task> tempList = getListFromCache();
		if(tempList==null||tempList.size()==0){
			Log.v(TAG, "缓存为空，远程获取数据");
			getTask();
		}
		else {
			Log.v(TAG, "从缓存中获取列表,listSize:"+tempList.size());
			taskListFromNet = tempList;
		}
		initeView();
		RefreshList();
		
		return view;
	}

	private void getTask() {
		
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				SystemClock.sleep(1000);
				// 做刷新的地方
				// 请求新数据并加入Tasklist
				String jsonResult = new NetCore()
						.GetReleaseDealList(FloatApplication.getApp()
								.getUser(getString(R.string.userFileName))
								.getId());
				Log.v(TAG, " jsonResult" + jsonResult);
				List<Task> list = new ParseJson().getTaskListFromJson(jsonResult);

				for (int i = 0; i < list.size(); i++) {
					taskListFromNet.add(list.get(i));
				}
				FloatApplication.getApp().setStoreRelTask(
						new StaticValues().STORE_RELEASETASK, taskListFromNet);
				// Log.v("TAG", taskListFromNet.get(0).getContent() + "  " +
				// taskListFromNet.size());
				// 初始化showTaskList
				// initShowTaskList(taskList);
				Message message = new Message();
				message.what = GET_TASK_LIST;
				myHandler.sendMessage(message);
				return null;
			}

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				// 隐藏头布局
				releaseList.onRefreshFinish();
			}
		}.execute(new Void[] {});
	}

	private void initeView() {
		// TODO Auto-generated method stub
		releaseList = (RefreshListView) view
				.findViewById(R.id.storage_release_list);
		adapter = new ReleaseTaskAdapter(getActivity(), taskListFromNet);
		releaseList.setAdapter(adapter);
		setListener();
	}
	private List<Task> getListFromCache() {
		List<Task> cacheList;
		cacheList = FloatApplication.getApp().getStoreTaskList(
				StaticValues.STORE_RELEASETASK);
		return cacheList;
	}

	private void setListener() {
		releaseList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setBackgroundColor(Color.WHITE);
				Bundle bundle = new Bundle();
				 bundle.putSerializable("task", taskListFromNet.get(position-1));
				// Log.v(TAG,"taskList.get(position):"+taskList.get(position).getContent() );
				Intent intent = new Intent(getActivity(),
						DetailedTaskActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		releaseList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(getActivity()).setTitle("完成")
				.setMessage("将这项交易设置为已完成？")
				.setPositiveButton("完成！", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						Log.v(TAG, "arg2"+position);
						// 开启设置线程
						progressDialog = DialogUtil.createLoadingDialog(getActivity(),"连接中...");
						progressDialog.show ();
						Thread thread = new Thread(
								new setCompleteThread(taskListFromNet
										.get(position-1).getId()));
						thread.start();

					}
				}).setNegativeButton("取消！", null).show();

				return true;
			}
		});

	}

	class setCompleteThread implements Runnable {
		private String taskId;

		public setCompleteThread(String taskId) {
			super();
			this.taskId = taskId;

		}

		@Override
		public void run() {
			String jsonData = new NetCore().setFinishTask(taskId);
			Message message = new Message();
			message.what = 404;
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				message.what = jsonObject.getInt("result");
			} catch (JSONException e) {
				// 
				e.printStackTrace();
			}
			myHandler.sendMessage(message);

		}

	}

	

	class showMyTaskThread implements Runnable {
		public showMyTaskThread(String userId) {
			super();
			this.userId = userId;
		}

		String userId;

		@Override
		public void run() {

			// String jsonResult = new NetCore().GetRecieveDealList(userId);
			// taskListFromNet = new ParseJson().JPsetHasreceive(jsonResult);
			// Message message = new Message();
			// message.what = GET_TASK_LIST;
			// myHandler.sendMessage(message);

		}

	}

	private void RefreshList() {
		// TODO Auto-generated method stub
		releaseList.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 异步查询数据
				
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {

						SystemClock.sleep(1000);
						// 做刷新的地方
						// 请求新数据并加入Tasklist
						System.out.println("调用了方法");
						String jsonResult = new NetCore()
								.GetReleaseDealList(FloatApplication
										.getApp()
										.getUser(
												getString(R.string.userFileName))
										.getId());
						Log.v(TAG, "REFRESH" + jsonResult);
						// 解析jsonResult,初始化tasklist
						taskListFromNet.clear();
						List<Task> taskListre = new ParseJson()
								.getTaskListFromJson(jsonResult);
						
						for (int i = 0; i < taskListre.size(); i++) {
							taskListFromNet.add(taskListre.get(i));
						}
						FloatApplication.getApp().setStoreRelTask(
								new StaticValues().STORE_RELEASETASK, taskListFromNet);
						return null;
					}

					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
						// 隐藏头布局
						releaseList.onRefreshFinish();
					}
				}.execute(new Void[] {});
			}

			@Override
			public void onLoadMoring() {
				
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						// SystemClock.sleep(10);
						// 坐加载的地方
						// String jsonResult = new
						// NetCore().GetNewTask(taskListFromNet
						// .size());
						//
						// // 解析jsonResult,初始化tasklist
						// // taskList.clear();
						// List<Task> taskListre = new ParseJson()
						// .JPsquareTaskList(jsonResult);
						// for (int i = 0; i < taskListre.size(); i++) {
						// taskListFromNet.add(taskListre.get(i));
						// }

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);

						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
						// 隐藏头布局
						releaseList.onRefreshFinish();
					}

				}.execute(new Void[] {});
			}
		});
	}
}
