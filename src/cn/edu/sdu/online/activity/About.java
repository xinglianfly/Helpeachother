package cn.edu.sdu.online.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(webView);
		webView.loadUrl("file:///android_asset/about.html");
	}

}
