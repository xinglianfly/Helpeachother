package cn.edu.sdu.online.activity;

import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.activity.MessageBox.BoxReceiver;
import cn.edu.sdu.online.adapter.ChatListAdapter;
import cn.edu.sdu.online.chatservice.ChatwithService;
import cn.edu.sdu.online.chatservice.ChatwithService.ChatBinder;
import cn.edu.sdu.online.entity.Content;
import cn.edu.sdu.online.util.ConvertString;

public class ChatClient extends Activity {
	private String pUSERID;
	private EditText msgText;
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String toUser = "";
	private List<Content> list;
	private ChatListAdapter adapter;
	private Button sendButton;
	private ListView listview;
	private ChatwithService chatservice;
	private boolean bound = false;
	private String TAG = "ChatClient";
	private receiveBroadcast broadcast ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formclient);
		initView();
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		pUSERID = ConvertString.convert(share.getString("MAP_LOGIN_EMAIL", ""));
		System.out.println(pUSERID + "pUser");
		toUser = getIntent().getStringExtra("USERIDTO");
		System.out.println("toUser" + toUser);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unbindService(serviceConnection);
		unregisterReceiver(broadcast);
		chatservice.setIfCurrentActivity(false);
	}
	protected void onResume() {
		super.onResume();
		Intent intentservice = new Intent(this, ChatwithService.class);
		bindService(intentservice, serviceConnection,
				BIND_IMPORTANT);
		IntentFilter filter = new IntentFilter();
		filter.addAction("cn.edu.sdu.online.chatservice.receivemessage");
		if (broadcast == null) {
			broadcast = new receiveBroadcast();
		}
		registerReceiver(broadcast, filter);
	};
	ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.v(TAG, "CHATSERVICE YOULDIS");
			bound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v(TAG, "CHATSERVICE YOUL");
			ChatBinder binder = (ChatBinder) service;
			chatservice = binder.getService();
			list = chatservice.initChat(pUSERID, toUser);
			chatservice.setIfCurrentActivity(true);
			chatservice.setRead(toUser);
			adapter = new ChatListAdapter(ChatClient.this, list, pUSERID);
			listview.setAdapter(adapter);
			Log.v(TAG, chatservice + "CHATSERVICE");
			bound = true;
		}
	};

	private void initView() {
		listview = (ListView) findViewById(R.id.formclient_listview);
		listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		msgText = (EditText) findViewById(R.id.formclient_text);
		sendButton = (Button) findViewById(R.id.formclient_sendmessagebt);
		sendButton.setOnClickListener(new sendMessageListener());
	}

	class sendMessageListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sendMessage();
		}

	}

	void sendMessage() {
		// try {
		String c = msgText.getText().toString();
		Content content = new Content(pUSERID, toUser, c, new Date(), true);
		list.add(content);
		adapter.notifyDataSetChanged();
		chatservice.sendMessage(content, c);
		msgText.setText("");
	}

	public class receiveBroadcast extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.v(TAG, "receive a broadcast");
			Content content = (Content) intent.getSerializableExtra("content");
			Log.v(TAG, content.getContent());
			list.add(content);
			
			Message message = new Message();
			message.what = 1;
			noticeHander.sendMessage(message);
		}
		
	}
	Handler noticeHander = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}

		}
	};

}
