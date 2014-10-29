package cn.edu.sdu.online.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.sdu.online.entity.Task;
import cn.edu.sdu.online.entity.User;
import cn.edu.sdu.online.share.FloatApplication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParseJson {

	// 解析发布任务状态码
	public int JPaddTask(String jsonData) {
		int result = 2;
		JSONObject jo;
		// String s ="{\"result\"=0}";
		try {
			jo = new JSONObject(jsonData);
			result = jo.getInt("result");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}

	// /*
	// * 解析任务详情
	// */
	// private void JPtaskDetail(String jsonData) {
	// Gson gson = new Gson();
	//
	// List<Task> tasklist = new ArrayList<Task>(); // 读取任务列表对象
	// tasklist = gson.fromJson(jsonData, new TypeToken<List<Task>>() {
	// }.getType());
	// }

	/*
	 * 解析任务详情
	 */
	public Task JPtaskDetail(String jsonData) {
		Gson gson = new Gson();
		Task task = new Task();
		task = gson.fromJson(jsonData, Task.class);
		return task;

	}

	// 解析得到广场任务列表并放入缓存
	public List<Task> getTaskListFromJson(String jsonData) {
		Gson gson = new Gson();
		List<Task> tasklist = new ArrayList<Task>(); // 读取任务列表对象
		tasklist = gson.fromJson(jsonData, new TypeToken<List<Task>>() {
		}.getType());

//		FloatApplication.getApp().setStoreTaskList(
//				StaticValues.STORE_SQUARETASKLIST, tasklist);
		return tasklist;
	}

	/*
	 * 解析用户信息
	 */
	public void JPuser(String jsonData) {
		Gson gson = new Gson();
		User user = new User();
		user = gson.fromJson(jsonData, User.class);
		FloatApplication.getApp().setStoreUser(StaticValues.STORE_USER,
				user);
	}

//	/*
//	 * 解析已经发出的任务列表
//	 */
//	public List<Task> JPsetHasrelease(String jsonData) {
//		Gson gson = new Gson();
//		List<Task> releaseTask = new ArrayList<Task>();
//		releaseTask = gson.fromJson(jsonData, new TypeToken<List<Task>>() {
//		}.getType());
//		
//		return releaseTask;
//	}

//	/*
//	 * 解析已经接受的任务列表
//	 */
//	public List<Task> JPsetHasreceive(String jsonData) {
//		Gson gson = new Gson();
//		List<Task> receiveTask = new ArrayList<Task>();
//		receiveTask = gson.fromJson(jsonData, new TypeToken<List<Task>>() {
//		}.getType());
//		FloatApplication.getApp().setStoreRelTask(
//				StaticValues.STORE_RECIEVETASK, receiveTask);
//		return receiveTask;
//	}

//	// 解析搜索的任务列表
//	public List<Task> JPSearch(String jsonData) {
//		Gson gson = new Gson();
//		List<Task> searchTask = new ArrayList<Task>();
//		searchTask = gson.fromJson(jsonData, new TypeToken<List<Task>>() {
//		}.getType());
//		return searchTask;
//
//	}

//	/*
//	 * 摇一摇的任务列表
//	 */
//	public List<Task> JPShake(String jsonData) {
//		Gson gson = new Gson();
//		List<Task> shakeTask = new ArrayList<Task>();
//		shakeTask = gson.fromJson(jsonData, new TypeToken<List<Task>>() {
//		}.getType());
//		return shakeTask;
//	}

}
