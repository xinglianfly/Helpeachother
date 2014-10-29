package cn.edu.sdu.online.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteUtil extends SQLiteOpenHelper{
	
	public static final String DBNAME="chatx.db";
	public static int VERSION=3;
    private static final String CreatemesSql = "CREATE TABLE IF NOT EXISTS chatmessages (messageid integer primary key autoincrement, t_from text, t_to text,t_content text,i_received_time text,i_checked text)";
    private static final String UpdatemesSql ="DROP TABLE IF EXISTS chatmessages";

	public SqliteUtil(Context context) {
		super(context, DBNAME, null, VERSION);
		System.out.println("执行了构造方法");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		 db.execSQL(CreatemesSql);
		 System.out.println("创建了表");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("执行了update");
		 db.execSQL(UpdatemesSql);
		 onCreate(db);
	}

}
