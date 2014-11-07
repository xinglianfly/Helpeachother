package cn.edu.sdu.online.sqlite;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.sdu.online.entity.Content;
import cn.edu.sdu.online.util.ConvertString;

public class PersistService {
	private Context context;
	private static PersistService instance = null;
	private SqliteUtil util;
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String SHARE_LOGIN_EMAIL = "MAP_LOGIN_EMAIL";

	private PersistService(Context context) {
		System.out.println("执行了构造方法 persistService");
		util = new SqliteUtil(context);
		this.context = context;
	}

	public static PersistService getInstance(Context context) {
		if (instance == null) {
			instance = new PersistService(context);
		}
		System.out.println("构造方法的context" + context);
		return instance;
	}

	// 将收到的消息存到数据库
	public void appendMessage(Content content) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("t_from", content.getFrom());
		contentvalues.put("t_to", content.getTo());
		contentvalues.put("t_content", content.getContent());
		contentvalues.put("i_received_time", content.getDatetime().getTime());
		contentvalues.put("i_checked", content.isChecked() ? 1 : 0);
		SQLiteDatabase db = util.getWritableDatabase();
		System.out.println("添加到数据库");
		db.insert("chatmessages", null, contentvalues);
		db.close();

	}

	// 从数据库中读出两者的历史消息，，？？？？？？？check呢
	public ArrayList<Content> getContents(String from, String to) {
		ArrayList<Content> contents = new ArrayList<Content>();
		SQLiteDatabase db = util.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from chatmessages where (t_from = ? and t_to = ? )or(t_from = ? and t_to = ?)",
						new String[] { from, to, to, from });
		while (cursor.moveToNext()) {
			Content c = new Content(cursor.getString(1), cursor.getString(2),
					cursor.getString(3), new Date(cursor.getInt(4)), true);
			contents.add(c);
		}
		db.close();
		return contents;
	}

	public int getUnreadMessages(String from) {
		// SharedPreferences share =
		// context.getSharedPreferences(SHARE_LOGIN_TAG,0);
		// String email =
		// ConvertString.convert(share.getString(SHARE_LOGIN_EMAIL,
		// ""));
		int count = 0;
		SQLiteDatabase db = util.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from chatmessages where (t_from = ? and t_to = ? and i_checked = 0)or(t_from = ? and t_to = ? and i_checked = 0)",
						new String[] { from, getEmail(), getEmail(), from });
		while (cursor.moveToNext()) {
			// db.update("chatxmessage", "1", "i_checked = ?", new
			// String[]{"0"});
			count++;
		}
		db.close();
		return count;
	}

	public void setRead(String from) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = util.getReadableDatabase();
		ContentValues args = new ContentValues();
		args.put("i_checked", "1");
		db.update(
				"chatmessages",
				args,
				"(t_from = ? and t_to = ? and i_checked = 0)or(t_from = ? and t_to = ? and i_checked = 0)",
				new String[] { from, getEmail(), getEmail(), from });

	}

	private String getEmail() {
		SharedPreferences share = context.getSharedPreferences(SHARE_LOGIN_TAG,
				0);
		String email = ConvertString.convert(share.getString(SHARE_LOGIN_EMAIL,
				""));
		return email;
	}

	// 得到最近的联系人的聊天记录
	public ArrayList<String> getRecentUser(String to) {
		SharedPreferences share = context.getSharedPreferences(SHARE_LOGIN_TAG,
				0);
		String email = ConvertString.convert(share.getString(SHARE_LOGIN_EMAIL,
				""));
		System.out.println("email is " + email + "to is" + to);
		ArrayList<String> users = new ArrayList<String>();
		SQLiteDatabase db = util.getReadableDatabase();
		if (to != null) {
			Cursor cursor = db
					.rawQuery(
							"select distinct t_to ,t_from from chatmessages where t_from = ? or t_to =?",
							new String[] { to, to });

			while (cursor.moveToNext()) {
				String toorfrom = cursor.getString(0);
				String fromorto = cursor.getString(1);
				if (toorfrom.equals(email)) {
					if (!users.contains(fromorto)) {
						users.add(fromorto);
					}
				} else {
					if (!users.contains(toorfrom)) {
						users.add(toorfrom);
					}
				}
			}
		}
		return users;

	}

}
