package cn.edu.sdu.online.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.entity.Task;

public class MylistviewAdapter extends BaseAdapter {
	private Activity activity;
	private List<Task> listDate;
	private static LayoutInflater inflater = null;

	public MylistviewAdapter(Context context, List<Task> listDate) {
		this.listDate = listDate;
		inflater = LayoutInflater.from(context);
		// this.inflater = (LayoutInflater) activity
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDate.size();// listDate.size();
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
		// ImageView faceImage;
		TextView leftTime, reward, formalText, name;
		ImageView im_sex;
		convertView = inflater
				.inflate(R.layout.square_frag_listview_item, null);
		name = (TextView) convertView.findViewById(R.id.name);
		name.setText(listDate.get(position).getNickName());
		im_sex = (ImageView)convertView.findViewById(R.id.imageView_sex);
		int sex = listDate.get(position).getSex();
		if(sex==0){
			im_sex.setBackgroundResource(R.drawable.woman);
		}
		if(sex==1){
			im_sex.setBackgroundResource(R.drawable.man);
		}
		leftTime = (TextView) convertView.findViewById(R.id.lefttime);
		String str = listDate.get(position).getLimitTime();
		if (str!=null&&str.length()==8) {
			String str1 = str.substring(0, 4);
			String str2 = str.substring(4, 6);
			String str3 = str.substring(6, 8);
			leftTime.setText(str1 + "年" + str2 + "月" + str3 + "日");
		} else
			leftTime.setText("长期有效");
		reward = (TextView) convertView.findViewById(R.id.reward);
		switch (listDate.get(position).getAwardStatus()) {
		case 0:
			try {
				reward.setText(listDate.get(position).getTipAward() + "元");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			break;
		case 1:
			try {
				reward.setText(listDate.get(position).getSpiritAward() + "");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			break;

		default:
			break;
		}

		// String html =
		// "我想要一只大大的灰机~~~我想要一只大大的灰机~~~我想要一只大大的灰机~~~我想要一只大大的灰机~~~我想要一只大大的灰机~~~"
		// + "我想要一只大大的灰机~~~我想要一只大大的灰机~~~我想要一只大大的灰机~~~";

		formalText = (TextView) convertView.findViewById(R.id.formaltext);

		formalText.setText(listDate.get(position).getContent());
		return convertView;
	}

}