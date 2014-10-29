package cn.edu.sdu.online.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.chatservice.ChatwithService;
import cn.edu.sdu.online.sqlite.PersistService;

public class ChatMessageListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<String> list;
	private ChatwithService chatservice;

	public ChatMessageListAdapter(Context context, ArrayList<String> list,
			ChatwithService chatservice) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		this.chatservice = chatservice;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.messagelistdetail, null);
		TextView time, from, message;
		time = (TextView) convertView.findViewById(R.id.messagetime);
		from = (TextView) convertView.findViewById(R.id.messagefrom);

		from.setText(list.get(position));
		String unread = chatservice.getUnreadMessages(list.get(position));
		if (!unread.equals("0")) {
			time.setText(chatservice.getUnreadMessages(list.get(position)));
		}
		message = (TextView) convertView.findViewById(R.id.messagelast);
		return convertView;
	}

}
