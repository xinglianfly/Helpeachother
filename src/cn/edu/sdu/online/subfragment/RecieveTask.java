package cn.edu.sdu.online.subfragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.adapter.ReceiveListAdapter;
import cn.edu.sdu.online.entity.Task;

public class RecieveTask extends Fragment {

	LayoutInflater inflater;
	private List<Task> taskListFromNet = new ArrayList<Task>();// 解析得到的任务列表

	private final static int GET_TASK_LIST = 0;
	private Handler myHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_TASK_LIST:
				// taskListFromNet中已有数据，不信你可以打印一下
				// 设置显示列表
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
		System.out.println("zhi cin");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		System.out.println("receive task");
		View view = inflater.inflate(R.layout.releasetask_fragment, null);
		ListView recieveList = (ListView) view
				.findViewById(R.id.storage_receive_list);
		recieveList.setAdapter(new ReceiveListAdapter(getActivity()));
		return view;
	}

	class showMyTaskThread implements Runnable {
		public showMyTaskThread(String userId) {
			super();
			this.userId = userId;
		}

		String userId;

		@Override
		public void run() {
			
//			
//			String jsonResult = new NetCore().GetRecieveDealList(userId);
//			taskListFromNet = new ParseJson().JPsetHasreceive(jsonResult);
//			Message message = new Message();
//			message.what = GET_TASK_LIST;
//			myHandler.sendMessage(message);
		
		}

	}
	// class listRecieveAdapter extends BaseAdapter{
	//
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// return 6;
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // TODO Auto-generated method stub
	// TextView store_name,store_score,store_lefttime,store_formal,store_reward;
	// convertView = inflater.inflate(R.layout.storage_receive_listview, null);
	// store_name = (TextView)convertView.findViewById(R.id.store_name);
	// store_name.setText("名字");
	// store_score =(TextView)convertView.findViewById(R.id.store_score);
	// store_score.setText("5分");
	// store_lefttime = (TextView)convertView.findViewById(R.id.store_lefttime);
	// store_lefttime.setText("2h");
	// store_formal =(TextView)convertView.findViewById(R.id.store_formaltext);
	// store_formal.setText("我想要一只大大的灰机~~~");
	// store_reward = (TextView)convertView.findViewById(R.id.store_reward);
	// store_reward.setText("￥5+8分");
	// return convertView;
	// }
	//
	// }
}
