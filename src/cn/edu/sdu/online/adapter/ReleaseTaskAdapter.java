package cn.edu.sdu.online.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.entity.Task;

public class ReleaseTaskAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<Task> taskListFromNet;
	public ReleaseTaskAdapter(Context context, List<Task> taskListFromNet) {
		inflater = LayoutInflater.from(context);
		this.taskListFromNet = taskListFromNet;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return taskListFromNet.size();
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
		TextView formal_text, release_jiangli, release_lefttime, release_reward, release_state;
		convertView = inflater.inflate(R.layout.storage_release_listview, null);
		release_state = (TextView) convertView.findViewById(R.id.release_state);
		int state = taskListFromNet.get(position).getState();
		switch (state) {
		case 0:
			release_state.setText("已发布");
			break;
		case 2:
			release_state.setText("已完成");
			break;

		default:
			break;
		}
		
		release_jiangli = (TextView) convertView.findViewById(R.id.release_jiangli);
		if(taskListFromNet.get(position).getAwardStatus()==0){
			release_jiangli.setText("金钱奖励:"+taskListFromNet.get(position).getTipAward());
		}else{
			release_jiangli.setText("精神奖励:"+taskListFromNet.get(position).getSpiritAward());
		}
		
		release_lefttime = (TextView) convertView
				.findViewById(R.id.release_lefttime);
		release_lefttime.setText("剩余时间:"+taskListFromNet.get(position).getLimitTime());
//		release_reward = (TextView) convertView
//				.findViewById(R.id.release_reward);
//		release_reward.setText(taskListFromNet.get(position).get);
		formal_text = (TextView) convertView
				.findViewById(R.id.release_formaltext);
		formal_text.setText(taskListFromNet.get(position).getContent());
		return convertView;
	}

}
