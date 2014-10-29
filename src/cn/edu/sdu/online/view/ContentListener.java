package cn.edu.sdu.online.view;

import cn.edu.sdu.online.entity.Content;

public interface ContentListener {
	public void messageReceived(Content content);
	public String getUsername();
}
