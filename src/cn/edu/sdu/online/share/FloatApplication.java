package cn.edu.sdu.online.share;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.WindowManager;
import cn.edu.sdu.online.chatservice.ChatwithService;
import cn.edu.sdu.online.entity.Task;
import cn.edu.sdu.online.entity.User;
import cn.edu.sdu.online.sqlite.PersistService;

public class FloatApplication extends Application {

	public static FloatApplication app;
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getWindowParams() {
		return windowParams;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		app = this;
		Intent intent = new Intent(this,ChatwithService.class);
		startService(intent);
	}
//	public static ArrayList<String>  getChatUsers(){
//		ArrayList<String> users = PersistService.getInstance(app).getRecentUser(
//				ChatService.getInstance(app).getUsername());
//		return users;
//	}
	
	public static PersistService getPersistInstence(){
	
		return PersistService.getInstance(app);
		
	}
	/*
	 * 存储广场任务列表
	 */
	public void setStoreTaskList(String filename, List<Task> tasklist) {
		try {
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream oj = new ObjectOutputStream(out);
			oj.writeObject(tasklist);
			oj.close();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static FloatApplication getApp() {
		return app;
	}

	/*
	 * 存储用户信息
	 */

	public void setStoreUser(String filename, User user) {
		try {
			System.out.println("diao yong le zheg fang fa");
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream oj = new ObjectOutputStream(out);
			oj.writeObject(user);
			oj.close();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.v("erroir1", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.v("erroir2", e.getMessage());
			e.printStackTrace();
		}

	}

	/*
	 * 存储已发出的任务列表
	 */
	public void setStoreRelTask(String filename, List<Task> releaseTask) {
		try {
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream oj = new ObjectOutputStream(out);
			oj.writeObject(releaseTask);
			oj.close();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 存储已收入的任务列表
	 */
	public void setStoreRecTask(String filename, List<Task> receiveTask) {

		try {
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream oj = new ObjectOutputStream(out);
			oj.writeObject(receiveTask);
			oj.close();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * 存储用户头像
	 */
	private void JPsetStoreheadphto(Bitmap bm, String path) {

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			FileOutputStream out = new FileOutputStream(new File(path));
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 得到广场任务列表
	 */
	public List<Task> getStoreTaskList(String filename) {
		List<Task> TaskList = null;
		try {
			FileInputStream in = openFileInput(filename);
			ObjectInputStream is = new ObjectInputStream(in);
			TaskList = (List<Task>) is.readObject();
			is.close();
			in.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TaskList;

	}

	/*
	 * 得到用户信息
	 */
	public  User getUser(String filename) {
		User user = null;
		try {
			FileInputStream in = openFileInput(filename);
			ObjectInputStream is = new ObjectInputStream(in);
			user = (User) is.readObject();
			is.close();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	/*
	 * 得到已接入的任务列表
	 */
	private List<Task> getHasRecTask(String filename) {
		List<Task> recTask = null;
		try {
			FileInputStream in = openFileInput(filename);
			ObjectInputStream is = new ObjectInputStream(in);
			recTask = (List<Task>) is.readObject();
			is.close();
			in.close();
			in = openFileInput(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recTask;

	}

	/*
	 * 得到已发出的任务列表
	 */
	private List<Task> getHasRelTask(String filename) {
		List<Task> relTask = null;
		try {
			FileInputStream in = openFileInput(filename);
			ObjectInputStream is = new ObjectInputStream(in);
			relTask = (List<Task>) is.readObject();
			is.close();
			in.close();
			in = openFileInput(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return relTask;

	}

	/*
	 * 得到用户头像
	 */
	public Bitmap getHeadphoto(String path) {
		Bitmap bit = null;
		try {
			FileInputStream fis = new FileInputStream(path);
			bit = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bit;
	}

}