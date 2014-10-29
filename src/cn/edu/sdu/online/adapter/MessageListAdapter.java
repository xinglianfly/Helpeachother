package cn.edu.sdu.online.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.sdu.online.R;

public class MessageListAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> listDate;
	private static LayoutInflater inflater = null;

	public MessageListAdapter(Activity a,ArrayList<HashMap<String, String>> listDate) {
		this.activity = a;
		this.listDate =listDate;
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		TextView store_name, store_formal;
		ImageView faceImage;
		convertView = inflater.inflate(R.layout.message, null);
		faceImage = (ImageView) convertView.findViewById(R.id.faceimage);
		faceImage.setImageResource(R.drawable.faceimage);
		store_name = (TextView) convertView.findViewById(R.id.name);
		store_name.setText("名字");
		store_formal = (TextView) convertView
				.findViewById(R.id.formaltext);
		store_formal.setText("我想要一只大大的灰机~~~");
		return convertView;
	}

}
