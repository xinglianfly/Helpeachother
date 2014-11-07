package cn.edu.sdu.online.activity;


import cn.edu.sdu.online.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity {
	
	//返回按钮
	ImageView button_back_image;
	TextView button_back_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.about_activity_layout);
		WebView webView = (WebView)findViewById(R.id.about_webview);
		webView.loadUrl("file:///android_asset/about.html");
		
		
		//返回按钮
		button_back_image=(ImageView)findViewById(R.id.about_button_back);
		button_back_text=(TextView)findViewById(R.id.about_button_back_text);
		button_back_image.setOnClickListener(new BackListener());
		button_back_text.setOnClickListener(new BackListener());
		
//		WebView webView = new WebView(this);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
//		setContentView(webView);
//		webView.loadUrl("file:///android_asset/about.html");
	}

	class BackListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			About.this.finish();
		}
	}	
}
