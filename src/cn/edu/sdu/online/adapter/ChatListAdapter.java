package cn.edu.sdu.online.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.entity.Content;

public class ChatListAdapter extends BaseAdapter {

	private List<Content> content;
	private LayoutInflater inflater;
	private String username;

	public ChatListAdapter(Context context, List<Content> content,
			String username) {
		this.content = content;
		inflater = LayoutInflater.from(context);
		this.username = username;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return content.size();
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
		if (username.equals(content.get(position).getFrom())) {
			convertView = inflater.inflate(R.layout.formclient_chat_out, null);
		} else {
			convertView = inflater.inflate(R.layout.formclient_chat_in, null);
		}
		TextView useridView = (TextView) convertView
				.findViewById(R.id.formclient_row_userid);
		TextView dateView = (TextView) convertView
				.findViewById(R.id.formclient_row_date);
		TextView msgView = (TextView) convertView
				.findViewById(R.id.formclient_row_msg);

		useridView.setText(content.get(position).getFrom());
		dateView.setText(content.get(position).getDatetime().toString());
		msgView.setText(content.get(position).getContent());
		return convertView;
	}

}
