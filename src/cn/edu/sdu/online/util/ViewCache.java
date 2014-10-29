package cn.edu.sdu.online.util;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.sdu.online.R;

public class ViewCache {

	private View baseView;
	private TextView userNameView;//用户名
	private ImageView imageView;//头像
	private TextView contentView;//内容
	private TextView awardView;//悬赏
	private TextView deadlineView;//截止日期

	public ViewCache(View baseView) {
		this.baseView = baseView;
	}

	public TextView getUserNameView() {
		if (userNameView == null) {
			userNameView = (TextView) baseView.findViewById(R.id.name);
		}
		return userNameView;
	}
	public TextView getDeadlineView() {
		if (deadlineView == null) {
			deadlineView = (TextView) baseView.findViewById(R.id.lefttime);
		}
		return deadlineView;
	}

	public ImageView getImageView() {
		if (imageView == null) {
			imageView = (ImageView) baseView.findViewById(R.id.faceimage);
		}
		return imageView;
	}

	public TextView getContentView() {
		if (contentView == null) {
			contentView = (TextView) baseView.findViewById(R.id.formaltext);
		}
		return contentView;
	}

	
	public TextView getAwardView() {
		if (awardView == null) {
			awardView = (TextView) baseView.findViewById(R.id.reward);
		}
		return awardView;
	}
//	public void setPriceview(TextView priceview) {
//		this.contentView = priceview;
//	}
//
//	public void setStateview(TextView stateview) {
//		this.awardView = stateview;
//	}

}