package cn.edu.sdu.online.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.adapter.ChatMessageListAdapter;
import cn.edu.sdu.online.chatservice.ChatwithService;
import cn.edu.sdu.online.chatservice.ChatwithService.ChatBinder;

public class MessageBox extends Activity {
	private ListView list;
	private ArrayList<String> users;
	private ChatMessageListAdapter adapter;
	private boolean bound = false;
	private ChatwithService chatservice;
	private String TAG = "MessageBox";
	private BoxReceiver broadcast;
	private int numMessages = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.messagebox);
	}

	class BoxReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.v(TAG, "收到了广播");
			String username = intent.getExtras().getString("user");
			Log.v(TAG, username+"username is");
			// Content content = (Content)
			// intent.getSerializableExtra("content");
			if (!users.contains(username)) {
				users.add(username);
			}
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}

	}

	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcast);
		unbindService(serviceConnection);
	};

	protected void onResume() {
		super.onResume();
		Intent intent = new Intent(this, ChatwithService.class);
		bindService(intent, serviceConnection, BIND_IMPORTANT);
		IntentFilter filter = new IntentFilter();
		filter.addAction("cn.edu.sdu.online.chatservice.receivemessage");
		if (broadcast == null) {
			broadcast = new BoxReceiver();
		}
		registerReceiver(broadcast, filter);
	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				Log.v(TAG, "是1");
				adapter.notifyDataSetChanged();
				
			}
		};
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
			users = chatservice.getChatUsers();
			list = (ListView) findViewById(R.id.messagelist);
			adapter = new ChatMessageListAdapter(MessageBox.this, users,chatservice);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			list.setOnItemClickListener(new listClickListener());
		}
	};

	protected void onDestroy() {
		super.onDestroy();

	};

	class listClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			System.out.println(position + "position is");
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MessageBox.this, ChatClient.class);
			String email = users.get(position);
			intent.putExtra("USERIDTO", email);
			startActivity(intent);
		}

	}
	// private void initialize() {
	// // TODO Auto-generated method stub
	// ArrayList<String> userslist =
	// PersistService.getInstance(this).getRecentUser(
	// ChatService.getInstance(this).getUsername());
	// for(int i=0;i<userslist.size();i++){
	// users.add(userslist.get(i));
	// }
	// adapter.notifyDataSetChanged();
	// }
	// void initialize() {
	// ArrayList<String> users = PersistService.getInstance(this).getRecentUser(
	// ChatService.getInstance(this).getUsername());//可以得到吗？？？？？？？？？？？？？？？？
	// userListView.getItems().addAll(users);

	// userListView.getSelectionModel().getSelectedItems()
	// .addListener(new ListChangeListener<String>() {
	// @Override
	// public void onChanged(
	// javafx.collections.ListChangeListener.Change<? extends String> c) {
	// String to = userListView.getSelectionModel()
	// .getSelectedItem();
	// UiManager.getInstance().openChat(to);
	// }
	//
	// });

	// }

}
