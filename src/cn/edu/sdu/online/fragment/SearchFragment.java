package cn.edu.sdu.online.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class SearchFragment extends Fragment {

	LayoutInflater inflater;
	AutoCompleteTextView search;
	Button but;
	private List<Task> taskList = new ArrayList<Task>();// 解析得到的任务列表
	RefreshListView refreshListView;
	MylistviewAdapter myadapter;
	private Dialog progressDialog;
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private final static int GET_TASK_LIST = 0;
	protected static final String TAG = "SearchFragment";

	@Override
	public void onPause() {

		super.onPause();

		if (myadapter != null) {
			myadapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		if (myadapter != null) {
			myadapter.notifyDataSetChanged();
		}

		Log.v(TAG, "SearchFragmentOnresume");
		super.onResume();
	}

	private String getLocation() {
		SharedPreferences share = getActivity().getSharedPreferences(
				SHARE_LOGIN_TAG, Context.MODE_PRIVATE);
		String location = share.getString("location", "");
		return location;
	}

	private Handler myHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_TASK_LIST:
				if (myadapter != null) {
					myadapter.notifyDataSetChanged();
				}
				// myAdapter = new ImageAndTextListAdapter(getActivity(),
				// showTaskList, listView);
				// listView.setAdapter(myAdapter);
				// 设置显示列表

				break;

			default:
				break;
			}

		};

	};

	// private List<Task> listItem;//生成动态数组，加入数据
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("diyiceng third");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		Log.v(TAG, "SearchFragmentOnCreate");
		View view3 = inflater.inflate(R.layout.frag_third, null);
		refreshListView = (RefreshListView) view3.findViewById(R.id.listView);
		// listItem = new ArrayList<HashMap<String, String>>();
		this.inflater = inflater;
		List<Task> tempList = getListFromCache();
		if (tempList == null || tempList.size() == 0) {
			Log.v(TAG, "缓存为空，远程获取数据");

		} else {
			Log.v(TAG, "从缓存中获取列表,listSize:" + tempList.size());
			taskList = tempList;
		}

		myadapter = new MylistviewAdapter(getActivity(), taskList);

		refreshListView.setAdapter(myadapter);

		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("task", taskList.get(position - 1));
				// Log.v(TAG,"taskList.get(position):"+taskList.get(position).getContent()
				// );
				Intent intent = new Intent(getActivity(),
						DetailedTaskActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

		refreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 异步查询数据

				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(1000);
						// 做刷新的地方
						taskList.clear();
						String jsonData = new NetCore().GetTaskListByKey(search
								.getText().toString(), 0, getLocation());
						List<Task> taskListre = new ParseJson()
								.getTaskListFromJson(jsonData);
						for (int i = 0; i < taskListre.size(); i++) {
							taskList.add(taskListre.get(i));
						}
						FloatApplication.getApp().setStoreRelTask(
								StaticValues.STORE_SEARCHTASKLIST, taskList);
						return null;
					}

					protected void onPostExecute(Void result) {
						// myadapter.notifyDataSetChanged();
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
						String jsonData = new NetCore().GetTaskListByKey(search
								.getText().toString(), taskList.size(),
								getLocation());
						List<Task> taskListre = new ParseJson()
								.getTaskListFromJson(jsonData);
						for (int i = 0; i < taskListre.size(); i++) {
							taskList.add(taskListre.get(i));
						}
						FloatApplication.getApp().setStoreRelTask(
								StaticValues.STORE_SEARCHTASKLIST, taskList);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						// myadapter.notifyDataSetChanged();
						Message message = new Message();
						message.what = GET_TASK_LIST;
						myHandler.sendMessage(message);
						refreshListView.onRefreshFinish();
					}

				}.execute(new Void[] {});
			}
		});
		search = (AutoCompleteTextView) view3.findViewById(R.id.search);
		initAutoComplete("history", search);
		but = (Button) view3.findViewById(R.id.button1);
		but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击搜索
				saveHistory("history", search);// 保存搜索的记录
				searchList();

			}
		});

		return view3;
	}

	private List<Task> getListFromCache() {
		List<Task> cacheList;
		cacheList = FloatApplication.getApp().getStoreTaskList(
				StaticValues.STORE_SEARCHTASKLIST);
		return cacheList;
	}

	public void searchList() {
		progressDialog = DialogUtil
				.createLoadingDialog(getActivity(), "查询中...");
		progressDialog.show();
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				SystemClock.sleep(1000);
				// 做刷新的地方
				// 请求新数据并加入Tasklist

				String jsondata = new NetCore().GetTaskListByKey(search
						.getText().toString(), 0, getLocation());
				// Log.v(TAG, "jsonData:"+jsondata);
				List<Task> taskListre = new ParseJson()
						.getTaskListFromJson(jsondata);
				taskList.clear();
				for (int i = 0; i < taskListre.size(); i++) {
					taskList.add(taskListre.get(i));
				}
				FloatApplication.getApp().setStoreRelTask(
						StaticValues.STORE_SEARCHTASKLIST, taskList);
				Message message = new Message();
				message.what = GET_TASK_LIST;
				myHandler.sendMessage(message);
				return null;
			}

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				// myadapter.notifyDataSetChanged();
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

	/**
	 * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param autoCompleteTextView
	 *            要操作的AutoCompleteTextView
	 */
	private void initAutoComplete(String field, AutoCompleteTextView search) {
		SharedPreferences sp = getActivity().getSharedPreferences(
				"network_url", 0);
		String longhistory = sp.getString("history", "nothing");
		String[] histories = longhistory.split(",");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.array_item, histories);// 把前面的android.去掉！！！！
		// 只保留最近的50条的记录
		if (histories.length > 50) {
			String[] newHistories = new String[50];
			System.arraycopy(histories, 0, newHistories, 0, 50);
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.array_item, newHistories);
		}
		search.setAdapter(adapter);
		search.setThreshold(1);
		search.setCompletionHint("最近的5条记录");
		search.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				AutoCompleteTextView view = (AutoCompleteTextView) v;
				if (hasFocus) {
					// view.showDropDown();
				}
			}
		});
	}

	// SharedPreferences如果同一个key被设置了两次值，那么后来的值将会覆盖掉前面的值

	private void saveHistory(String field,
			AutoCompleteTextView autoCompleteTextView) {
		String text = autoCompleteTextView.getText().toString();
		SharedPreferences sp = getActivity().getSharedPreferences(
				"network_url", 0);
		String longhistory = sp.getString(field, "nothing");
		String[] histories = longhistory.split(",");
		if (histories.length > 100) {// 记录过多的话删除记录
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 30; i++) {

				sb.insert(i, histories[i] + ",");
			}
			sp.edit().putString("history", sb.toString()).commit();
		}
		if (!longhistory.contains(text + ",")) {
			StringBuilder sb = new StringBuilder(longhistory);
			sb.insert(0, text + ",");
			sp.edit().putString("history", sb.toString()).commit();
		}
	}

}
