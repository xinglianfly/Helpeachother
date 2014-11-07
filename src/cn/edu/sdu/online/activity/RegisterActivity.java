package cn.edu.sdu.online.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.adapter.MyArrayAdapter;
import cn.edu.sdu.online.share.FloatApplication;

public class RegisterActivity extends Activity 
{
	String TAG ="REGISTER_ACTIVITY";
	//初始化组件
	ImageView image_email,image_password,image_password_again;
	EditText editText_email,editText_password,editText_password_again;
	Button button_next;
	Spinner spinner_sex;
	ArrayAdapter<String> adapter;
	
	//初始化变量
	double screenWigth,screenheight,screenDensity;
	//初始化数值
	String email="";
	String password="";
	String password_again="";
	int sex=0;// "0" is girl; "1" is boy;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.register_activity);
		
		initialization ();
		getSize();
		setSize();
	}
	
	//初始化方法
	private void initialization ()
	{
		//email-----------------------------------------------------------------------------
		image_email=(ImageView)findViewById(R.id.register_image_email);		
		editText_email=(EditText)findViewById(R.id.register_edittext_email);
		//password--------------------------------------------------------------------------
		image_password=(ImageView) findViewById(R.id.register_image_password);
		editText_password=(EditText)findViewById(R.id.register_edittext_password);
		//password_again--------------------------------------------------------------------
		image_password_again=(ImageView)findViewById(R.id.register_image_password_again);
		editText_password_again=(EditText)findViewById(R.id.register_edittext_password_again);
		
		/////////////////////////////////////////////////////////////////////
		
		//Sex Spinner-----------------------------------------------------------------------
		//下拉项
		String[] sexs={" 我是美女"," 我是帅哥"};
		//组件初始化
		spinner_sex=(Spinner)findViewById(R.id.register_spinner_sex);
		adapter=new MyArrayAdapter(RegisterActivity.this,sexs);
		//下拉样式
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//add
		spinner_sex.setAdapter(adapter);
		//监听
		spinner_sex.setOnItemSelectedListener(new OnItemSelectedListener()
		{
		    @Override
    	     public void onItemSelected(AdapterView<?> arg0, View arg1,
			    int arg2, long arg3) 
		    {
				// TODO Auto-generated method stub
				sex=arg2;
		    }

		     @Override
		    public void onNothingSelected(AdapterView<?> arg0)
            {
			    // TODO Auto-generated method stub
				
		    }
		});
		  
		//next button------------------------------------------------------------------------------------
		button_next=(Button)findViewById(R.id.register_next);
		button_next.setOnClickListener(new OnClickListener() //按钮监听
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				email=editText_email.getText().toString();
				password=editText_password.getText().toString();
				password_again=editText_password_again.getText().toString();
				
				//判断输入是否为空
				if(email.equals("")||password.equals("")||password_again.equals(""))
				{
					//为空
					Toast.makeText(RegisterActivity.this, R.string.register_toast_empty, Toast.LENGTH_LONG).show();
				}
				else//不为空
				{
					//判断email是否有效
					Pattern pattern=Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
					Matcher matcher = pattern.matcher(email);
					//email无效
					if(!matcher.matches())
					{
						Toast.makeText(RegisterActivity.this, R.string.register_toast_emailerror, Toast.LENGTH_LONG).show();
					}
					//email有效
					else
					{
						//判断密码是否一致
						//不一致
						if(!password.equals(password_again))
						{
							Toast.makeText(RegisterActivity.this, R.string.register_toast_error, Toast.LENGTH_LONG).show();
//							editText_password.setText("");
//							editText_password_again.setText("");
						}
						//密码一致
						else
						{
							Intent intent=new Intent(RegisterActivity.this,RegisterActivityNext.class);


							Bundle bundle =new Bundle();
							bundle.putString("email", email);
							bundle.putString("password", password);
							bundle.putInt("sex", sex);
							intent.putExtras(bundle);

							startActivity(intent);
							RegisterActivity.this.finish();
						}
						
					}		
					
				}
			}
		});
	}
	
	//组件尺寸的具体设置
	private void setSize()
	{
		
		setSize(R.id.register_image_email, -43, 0, -30, 0);
		setSize(R.id.register_edittext_email, 0, 0,-30, 0);
		
		setSize(R.id.register_image_password, -43, 0, -30, 0);
		setSize(R.id.register_edittext_password, 0, 0,-30, 0);
		
		setSize(R.id.register_image_password_again, -43, 0, -30, 0);
		setSize(R.id.register_edittext_password_again, 0, 0,-30, 0);
		
		setSize(R.id.register_next, -180, 0, -40, 0);
		
		setSize(R.id.register_spinner_sex, -180, 0, -40, 0);
	}
	
	//得到屏幕数值
	private void getSize()
	{
		DisplayMetrics metrics=new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		screenWigth=metrics.widthPixels;
		screenheight=metrics.heightPixels;
		screenDensity=metrics.density;
	}
	
	//设置组件尺寸的方法
	private void setSize(int id,double w_p,double w_m,double h_p,double h_m)
	{
		android.view.ViewGroup.LayoutParams lp =this.findViewById(id).getLayoutParams();
		
		if(w_p!=0)
		{
			if(w_p>0)
			{

				lp.width=(int)(w_p*screenWigth-w_m*screenDensity);
			}
			if(w_p<0)
			{
				lp.width=(int)(w_p*-1*screenDensity-w_m*screenDensity);
			}
			
		}
		
		if(h_p!=0)
		{
			if(h_p>0)
			{
				lp.height=(int)(h_p*screenheight-h_m*screenDensity);
			}
			if(h_p<0)
			{
				lp.height=(int)(h_p*-1*screenDensity-h_m*screenDensity);
			}
			
		}
	}
	


	
	

	
}
