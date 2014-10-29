package cn.edu.sdu.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.sdu.online.R;

public class ReceiveListAdapter extends BaseAdapter{
	LayoutInflater inflater;
	
	public ReceiveListAdapter(Context context){
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6;
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
		TextView store_name,store_score,store_lefttime,store_formal,store_reward;
		convertView = inflater.inflate(R.layout.storage_receive_listview, null);
		store_name = (TextView)convertView.findViewById(R.id.store_name);
		store_name.setText("名字");
		store_formal =(TextView)convertView.findViewById(R.id.store_formaltext);
		store_formal.setText("我想要一只大大的灰机~~~");
		store_reward = (TextView)convertView.findViewById(R.id.store_reward);
		store_reward.setText("￥5+8分");
		return convertView;
	}

}
