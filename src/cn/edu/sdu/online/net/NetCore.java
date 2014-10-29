package cn.edu.sdu.online.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import cn.edu.sdu.online.entity.Task;
import cn.edu.sdu.online.entity.User;

public class NetCore {
	//private final static String ServerAddr = "http://211.87.225.164:8080/Dajiabang/";
	 private final static String ServerAddr ="http://202.194.14.195:8080/Dajiabang/";

	private final String LoginAddr = ServerAddr + "user/login";
	private final String RegisteAddr = ServerAddr + "user/register";
	// SJQ
	private final String newListAddr = ServerAddr
			+ "action/newList";
	private final String newListOfSchoolAddr = ServerAddr
			+ "action/newListOfSchool";
	private final String urgencyListAddr = ServerAddr
			+ "action/urgencyList";
	private final String tipListAddr = ServerAddr
			+ "action/tipList";
	private final String SearchAddr = ServerAddr + "action/searchList";
	//private final String ShakeAddr = ServerAddr + "action/shake";
	private final String TaskDetailAddr = ServerAddr + "action/taskDetail";
	//private final String ShowRecieveAddr = ServerAddr
//			+ "action/showRecieveListByUser";
	private final String ShowReleaseAddr = ServerAddr
			+ "action/showListByUser";
	private final String FinishAddr = ServerAddr + "action/finishTask";
	//private final String ActivateAddr = ServerAddr + "action/activateTask";
	private final String PublishAddr = ServerAddr + "action/addTask";
	//private final String UploadAddr = ServerAddr + "action/uploadPic";
	private final String UserinfoAddr = ServerAddr + "action/userinfo";
	private final String ChangeUserInfoAddr = ServerAddr + "action/changeUserInfo";
	private final String ChangePasswordAddr = ServerAddr + "action/changePassword";
	public final static String DownloadPictruesAddr = ServerAddr + "uploads";// 全图
	public final static String DownloadSmallPictruesAddr = ServerAddr + "small";// 缩略图

	
	
	// http://202.194.14.195:8080/MyEnet/uploads/94e92c09-7233-4ba2-9fee-107bdcbee2ac/1400424861395.jpg

	public final static int ERROR = -1;
	public final static int LOGIN_SUCCESS = 0;
	public final static int LOGIN_ERROR = 1;

	public final static int REGISTER_ALREADY = 0;
	public final static int REGISTER_SUCCESS = 1;
	public final static int REGISTER_ERROR = 2;
	public final static int REGISTER_INVALID = 3;

	public final static int SEND_MESSAGE_SUCCESS = 0;
	public final static int SEND_MESSAGE_ERROR = 1;

	public final static int READ_MESSAGE_SUCCESS = 0;

	public final static int FINISH_DEAL_SUCCESS = 0;
	public final static int FINISH_DEAL_ERROR = 1;

	public final static int ADD_PRODUCT_SUCCESS = 0;
	public final static int ADD_PRODUCT_ERROR = 1;

	public final static int UPLOAD_PIC_SUCCESS = 0;
	public final static int UPLOAD_PIC_ERROR = 1;

	//
	// user的具体信息
	//
	/**
	 * 登陆
	 * 
	 * 必填字段// user.email // user.password
	 * 
	 * @param user
	 *            用户
	 */
	public String Login(User user) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user.email", user.getEmail()));
		params.add(new BasicNameValuePair("user.password", user.getPassword()));
		String jsonData = "";
		try {
			jsonData = GetResultFromNet(LoginAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// int result = getJsonResult(jsonData);
		// JsonObject jo=new JsonObject();
		// jo.get
		return jsonData;
		// gson.
	}

	/**
	 * 注册 必填字段// user.email // user.password // user.phone // user.sex 选填字段 //
	 * user.userName // user.headphoto
	 * 
	 * @param user
	 *            用户
	 */
	public String Registe(User user) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 必填
		params.add(new BasicNameValuePair("user.email", user.getEmail()));
		params.add(new BasicNameValuePair("user.password", user.getPassword()));
		
		params.add(new BasicNameValuePair("user.sex", user.getSex()+""));
		// 选填
		
		params.add(new BasicNameValuePair("user.nickName", user.getNickName()));
		//params.add(new BasicNameValuePair("user.headphoto", user.getHeadphone()));

		String result = "";
		try {
			result = GetResultFromNet(RegisteAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Gson gson = new Gson();
		// Map<String, Integer> map = new HashMap<String, Integer>();
		// map = gson.fromJson(result, Map.class);
		// double resultNum = map.get("result");
		// System.out.println(resultNum);
		return result;
	}
	/**
	 * 登陆
	 * 
	 * 必填字段// user.email // user.password
	 * 
	 * @param user
	 *            用户
	 */
	public String PublishTask(Task task) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("task.userId", task.getUserId()));
		params.add(new BasicNameValuePair("task.content", task.getContent()));
		params.add(new BasicNameValuePair("task.awardStatus", task.getAwardStatus()+""));
		params.add(new BasicNameValuePair("task.tipAward", task.getTipAward()+""));
		params.add(new BasicNameValuePair("task.spiritAward", task.getSpiritAward()+""));
		params.add(new BasicNameValuePair("task.limitTime", task.getLimitTime()+""));
		params.add(new BasicNameValuePair("task.details", task.getDetails()+""));
		
		
	
		String jsonData = "";
		try {
			jsonData = GetResultFromNet(PublishAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// int result = getJsonResult(jsonData);
		// JsonObject jo=new JsonObject();
		// jo.get
		return jsonData;
		// gson.
	}
	/**
	 * 获取任务列表（广场） *
	 * 
	 * @param stuNo
	 *            用户stuNo
	 */
	public String GetNewTask(int numCursor) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("numCursor", numCursor + ""));
		String result = "";
		try {
			result = GetResultFromNet(newListAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String GetSchoolTask(int numCursor,String school) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("numCursor", numCursor + ""));
		params.add(new BasicNameValuePair("school", school + ""));
		String result = "";
		try {
			result = GetResultFromNet(newListOfSchoolAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String GetUrgeTask(int numCursor) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("numCursor", numCursor + ""));
		String result = "";
		try {
			result = GetResultFromNet(urgencyListAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String GetTipTask(int numCursor) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("numCursor", numCursor + ""));
		String result = "";
		try {
			result = GetResultFromNet(tipListAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获取任务列表（搜索） *
	 * 
	 */
	public String GetTaskListByKey(String key,int numCursor) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key", key + ""));
		params.add(new BasicNameValuePair("numCursor", numCursor + ""));
//		params.add(new BasicNameValuePair("xLocation", xLocation + ""));
//		params.add(new BasicNameValuePair("yLocation", yLocation + ""));
//		params.add(new BasicNameValuePair("location", location + ""));
		String result = "";
		try {
			result = GetResultFromNet(SearchAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
//	/**
//	 * 获取任务列表（摇） *
//	 * 
//	 */
//	public String GetTaskListByLocation(float xLocation,
//			float yLocation, String location) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		
//		params.add(new BasicNameValuePair("xLocation", xLocation + ""));
//		params.add(new BasicNameValuePair("yLocation", yLocation + ""));
//		params.add(new BasicNameValuePair("location", location + ""));
//		String result = "";
//		try {
//			result = GetResultFromNet(ShakeAddr, params);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
	
	/**
	 * 获取任务的所有信息
	 * 
	 * @param productId
	 */
	public String GetProductAllMes(String taskId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", taskId));
		String result = "";
		try {
			result = GetResultFromNet(TaskDetailAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	/**
	 * 获取已发布任务列表
	 * 
	 * @param userId
	 *            用户id
	 */
	public String GetReleaseDealList(String userId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", userId));
		String result = "";
		try {
			result = GetResultFromNet(ShowReleaseAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
//	/**
//	 * 获取已接受任务列表
//	 * 
//	 * @param userId
//	 *            用户id
//	 */
//	public String GetRecieveDealList(String userId) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id", userId));
//		String result = "";
//		try {
//			result = GetResultFromNet(ShowRecieveAddr, params);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * 设置已完成
	 * 
	 * @param taskId
	 *            任务id
	 */
	public String setFinishTask(String taskId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", taskId));
		String result = "";
		try {
			result = GetResultFromNet(FinishAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
//	/**
//	 * 重新激活
//	 * 
//	 * @param taskId
//	 *            任务id
//	 */
//	public String setActivateTask(String taskId) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id", taskId));
//		String result = "";
//		try {
//			result = GetResultFromNet(ActivateAddr, params);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * 发布任务 
	 * 
	 * @param product
	 *            商品信息
	 */
//	public String AddTask(Task task) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("task.userName", task.getPublisherName()
//				+ ""));
//		params.add(new BasicNameValuePair("task.taskContent", task
//				.getTaskContent() + ""));
//		params.add(new BasicNameValuePair("task.userId", task.getPublisherId() + ""));
//		params.add(new BasicNameValuePair("task.awardCatogry", task
//				.getAwardCatogry() + ""));
//		params.add(new BasicNameValuePair("task.awardContent", task
//				.getAwardContent() + ""));
//		params.add(new BasicNameValuePair("task.telephone", task.getTelephone()
//				+ ""));
//		params.add(new BasicNameValuePair("task.deadLine", task.getDeadLine()
//				+ ""));
//		params.add(new BasicNameValuePair("task.taskDetail", task
//				.getTaskDetail() + ""));
//
//		String jsonData = "";
//		try {
//			jsonData = GetResultFromNet(ReleaseAddr, params);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return jsonData;
//	}


	
	
	
//	/**
//	 * 上传图片
//	 */
//	public String UpLoadPicture(String path, int pictureChoice, String userId) {
//		String result = "";
//		Map<String, String> params = new HashMap<String, String>();
//		// params.put("method", "save");
//		params.put("picturechoice", pictureChoice + "");
//		params.put("userId", userId);
//
//		File uploadFile = new File(path);
//		FormFile formfile = new FormFile(uploadFile.getName(), uploadFile,
//				"myFile", null);
//		try {
//			result = SocketHttpRequester.post(UploadAddr, params,
//					formfile);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * 获取用户信息
	 * 
	 * @param email
	 *            用户email
	 */
	public String GetUserInfo(String email) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		String result = "";
		try {
			result = GetResultFromNet(UserinfoAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String Change_person_information(User user) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("user.stuNo", user.stuNo));
		// params.add(new BasicNameValuePair("user.realName", user.realName));

		params.add(new BasicNameValuePair("user.id", user.getId()));
		params.add(new BasicNameValuePair("user.nickName", user.getNickName()));
		params.add(new BasicNameValuePair("user.sex", user.getSex()+""));
		params.add(new BasicNameValuePair("user.phoneNo", user.getPhoneNo()));
		params.add(new BasicNameValuePair("user.school", user.getSchool()));

		// System.out.println(user.userName+user.password+user.address+user.phone+user.email+user.dormNo+user.dormPhone);
		String result = "";
		try {
			result = GetResultFromNet(ChangeUserInfoAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		
		 return result;
	}
	//修改密码
	public String ChangePassword(User user,String newpassword) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("user.stuNo", user.stuNo));
		// params.add(new BasicNameValuePair("user.realName", user.realName));

		params.add(new BasicNameValuePair("user.id", user.getId()));
		params.add(new BasicNameValuePair("user.password", user.getPassword()));
		
		params.add(new BasicNameValuePair("newpassword", newpassword));
		//Log.v("ChangePassW", "oldPass:"+oldPass+"newpass"+newPass);
		// System.out.println(user.userName+user.password+user.address+user.phone+user.email+user.dormNo+user.dormPhone);
		String result = "";
		try {
			result = GetResultFromNet(ChangePasswordAddr, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		
		 return result;
	}
	// //
	// // message
	// //
	// /**
	// * 得到信息的数量
	// */
	// public int GetMesNum(String userId) {
	// int num = 0;
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("id", userId));
	// String result = "";
	// try {
	// result = GetResultFromNet(GetMesCount, params);
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// JSONObject jsonObject;
	// try {
	// jsonObject = new JSONObject(result);
	// num = jsonObject.getInt("result");
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return num;
	//
	// }
	//
	// /**
	// * 设置短信已读
	// *
	// * @param mesId
	// * 消息id
	// */
	// public String MakeMesRead(String mesId) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("id", mesId));
	// String result = "";
	// try {
	// result = GetResultFromNet(MakeMesReadAddr, params);
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	// /**
	// * 获取用户所有信息
	// *
	// * @param userId
	// * 用户id
	// */
	// public String GetUserAllMes(String userId) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("id", userId));
	// String result = "";
	// try {
	// result = GetResultFromNet(GetUserAllMesAddr, params);
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }
	//
	// /**
	// * 获取未读信息
	// *
	// * @param userId
	// * 用户id
	// */
	// public String GetUnreadMes(String userId) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("id", userId));
	// String result = "";
	// try {
	// result = GetResultFromNet(GetUnreadMesAddr, params);
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	// /**
	// * 发送信息 //message.sourceId // message.targetId // message.productId //
	// * message.content
	// *
	// * @param msg
	// * 信息
	// */
	// public String SendMes(Messages msg) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("message.sourceId", msg.sourceId));
	// params.add(new BasicNameValuePair("message.targetId", msg.targetId));
	// params.add(new BasicNameValuePair("message.productId", msg.productId));
	// params.add(new BasicNameValuePair("message.content", msg.content));
	// String result = "";
	// try {
	// result = GetResultFromNet(SendMesAddr, params);
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	

	

	

	// /**
	// * 下载图片
	// *
	// */
	// public Bitmap DownloadPicture(String userId, String fileName) {
	// String path = DownloadPictruesAddr + "/" + userId + "/" + fileName;
	// Log.v("ShouYeFragment", "图片路径："+path);
	// // String path =
	// //
	// "http://202.194.14.195:8080/MyEnet/uploads/47ca7fb0-cb96-4caf-a209-b98910d5902b/1397786350057.jpg";
	// Bitmap bitmap = null;
	// try {
	// byte[] data = ImageService.getImage(path);// 获取图片数据
	// if (data != null) {
	// // 构建位图对象
	// Log.v("ShouYeFragment", "data.length" + data.length);
	// bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	// if (bitmap == null) {
	// Log.v("ShouYeFragment", "获取图片失败");
	// }
	// else {
	// Log.v("ShouYeFragment", "获取图片成功");
	// }
	//
	// }
	// } catch (Exception e) {
	//
	// }
	// return bitmap;
	// }

	

	
	//
	/**
	 * 
	 * @param url
	 *            地址
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws Exception
	 */
	private String GetResultFromNet(String url, List<NameValuePair> params)
			throws ClientProtocolException, IOException {
		HttpPost httpRequest = new HttpPost(url);
		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse httpResponse = new DefaultHttpClient()
				.execute(httpRequest);
		String jsonData = "";
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = br.readLine()) != null) {
				jsonData += line + "\r\n";
			}
		}
		System.out.println(jsonData);
		return jsonData;
	}

	/**
	 * 发送信息 //message.sourceId // message.targetId // message.productId //
	 * message.content
	 * 
	 * @param msg
	 *            信息
	 */
	

}
