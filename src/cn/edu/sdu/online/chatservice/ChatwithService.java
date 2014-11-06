package cn.edu.sdu.online.chatservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.activity.ChatClient;
import cn.edu.sdu.online.entity.Content;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ChatMessageListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.sasl.SASLErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;

import cn.edu.sdu.online.sqlite.PersistService;
import cn.edu.sdu.online.view.ContentListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ChatwithService extends Service {

	private AbstractXMPPConnection connection;
	private ChatManager chatManager;
	private Set<ContentListener> listeners;
	private String username;
	private String TAG = "ChatwithService";
	private Chat chat;
	private PersistService persistService;
	private int numMessages = 0;
	private Content content;
	private boolean ifCurrentActivity = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return new ChatBinder();
	}

	Thread thread;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		persistService = PersistService.getInstance(this);
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				connect();
			}
		});
		thread.start();
		Log.v(TAG, "service is running");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// stopSelf();
	}

	// 连接openfire的服务器
	public void connect() {
		// TODO Auto-generated method stub
		ConnectionConfiguration configuration = new ConnectionConfiguration(
				"202.194.14.195", 5222);
		configuration.setSecurityMode(SecurityMode.disabled);
		connection = new XMPPTCPConnection(configuration);

		try {
			connection.connect();
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v(TAG, "error is" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v(TAG, "error is" + e.getMessage());
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v(TAG, "error is" + e.getMessage());
		}

		Log.v("ChatService",
				"是否存在连接：" + Boolean.toString(connection.isConnected()));
	}

	public void setRead(String user) {
		persistService.setRead(user);
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		try {
			connection.disconnect();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// Log.v(TAG, "service is running");

//		Timer timer = new Timer();
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				Log.v(TAG, "service is running");
//			}
//		}, 1, 1000);
		return super.onStartCommand(intent, flags, startId);
	}

	public class ChatBinder extends Binder {
		public ChatwithService getService() {
			return ChatwithService.this;
		}
	}

	// 注册
	public void register(String email, String password) {
		Message message = new Message();
		try {
			AccountManager.getInstance(getConnection()).createAccount(email,
					password);
			message.what = 3;
			openfireHandler.sendMessage(message);
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			message.what = 0;
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			message.what = 1;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			message.what = 2;
			e.printStackTrace();
		}
	}

	Handler openfireHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ChatwithService.this, "服务器没有返回结果",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(ChatwithService.this, "这个账号已经存在",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:

				Toast.makeText(ChatwithService.this, "注册失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				Toast.makeText(ChatwithService.this, "恭喜你注册成功",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

		}
	};

	// 得到最近联系人列表
	public ArrayList<String> getChatUsers() {
		ArrayList<String> users = persistService.getRecentUser(getUsername());
		return users;
	}

	// 登陆smack的服务器
	public boolean login(String username, String password) {
		Log.v(TAG, "执行了login");
		Log.v(TAG, "username"+username);
		try {
			connection.login(username, password);
			this.username = username;
			initChatManager();

			Log.v(TAG, "connecgt");
			return true;
		} catch (SASLErrorException e) {
			Log.v("ChatService", e.getMessage());
			return false;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// 接收到消息的时候做的处理，一将收的的消息存到数据库，然后从数据库中读出未读的消息
	public void initChatManager() {
		Log.v(TAG, "initeManager 执行了");
		listeners = new HashSet<ContentListener>();
		chatManager = ChatManager.getInstanceFor(connection);
		Log.v(TAG, "CHATMANAGER"+chatManager);
		chatManager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				if (createdLocally) {
					Log.v("ChatService", "本地建立的聊天");
				} else {
					Log.v("ChatService", "远程建立的聊天");
				}

				chat.addMessageListener(new ChatMessageListener() {

					@Override
					public void processMessage(Chat arg0,
							org.jivesoftware.smack.packet.Message message) {
						// TODO Auto-generated method stub
						String from = message.getFrom().split("@")[0];
						String to = message.getTo().split("@")[0];
						String c = message.getBody();
						Date datetime = new Date();
						System.out.println("from是" + from + "to是" + to
								+ "content是" + c);
						content = new Content(from, to, c, datetime, false);
						Intent intent = new Intent();
						intent.setAction("cn.edu.sdu.online.chatservice.receivemessage");
						intent.putExtra("content", content);
						intent.putExtra("user", content.getFrom());
						Log.v(TAG, "收到消息");
						if (!ifCurrentActivity) {
							Notifications(content.getContent());
						}
						persistService.appendMessage(content);
						sendBroadcast(intent);
					}
				});

			}
		});
	}

	// 显示通知
	private void Notifications(String currentText) {
		// TODO Auto-generated method stub
		Log.v(TAG, "进入了notification");
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated
		int notifyID = 1;
		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
				this).setContentTitle("新消息").setContentText(currentText)
				.setSmallIcon(R.drawable.ic_launcher);
		mNotifyBuilder.setDefaults(Notification.DEFAULT_ALL);
		mNotifyBuilder.setContentText(currentText).setAutoCancel(true);
		Intent intent = new Intent(this, ChatClient.class);
		intent.putExtra("USERIDTO", content.getFrom());
		Log.v(TAG, content.getFrom()+"content from");
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				ChatwithService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		mNotifyBuilder.setContentIntent(pendingIntent);
		mNotificationManager.notify(notifyID, mNotifyBuilder.build());

	}

	public List<Content> initChat(String from, String to) {
		Log.v(TAG, chatManager + "chatmanager");
		chat = chatManager.createChat(to + "@127.0.0.1", null);
		ArrayList<Content> contents = persistService.getContents(from, to);
		return contents;
	}

	// 发送消息
	public void sendMessage(Content content, String c) {
		persistService.appendMessage(content);
		try {
			chat.sendMessage(c);
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AbstractXMPPConnection getConnection() {
		Log.v(TAG, "getconnection");
		return connection;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}

	public void addContentListner(ContentListener listener) {
		listeners.add(listener);
	}

	public void removeContentListner(ContentListener listener) {
		listeners.remove(listener);
	}

	public String getUsername() {
		return username;
	}

	public boolean isIfCurrentActivity() {
		return ifCurrentActivity;
	}

	public void setIfCurrentActivity(boolean ifCurrentActivity) {
		this.ifCurrentActivity = ifCurrentActivity;
	}

	public String getUnreadMessages(String string) {
		// TODO Auto-generated method stub
		int num = persistService.getUnreadMessages(string);
		return String.valueOf(num);
	}
}
