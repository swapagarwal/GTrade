package com.swap.gtrade;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity{
	Button btnLogin;
	EditText username,password;
	TextView tv;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		username=(EditText) findViewById(R.id.etusername);
		password=(EditText) findViewById(R.id.etpassword);
		tv=(TextView) findViewById(R.id.textView1);
		btnLogin=(Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				@SuppressWarnings("static-access")
				final ProgressDialog p = new ProgressDialog(arg0.getContext()).show(arg0.getContext(),"Waiting for Server", "Accessing Server");
                Thread thread = new Thread()
                {
                    @Override
                    public void run() {
                         try{
                             
                             httpclient=new DefaultHttpClient();
                             httppost= new HttpPost("http://"+getString(R.string.SERVER_IP)+"/android/login.php"); // make sure the url is correct.
                             //add your data
                             nameValuePairs = new ArrayList<NameValuePair>(2);
                             // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                             nameValuePairs.add(new BasicNameValuePair("username",username.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
                             nameValuePairs.add(new BasicNameValuePair("password",password.getText().toString().trim()));
                             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                             //Execute HTTP Post Request
                             response=httpclient.execute(httppost);
                              
                             ResponseHandler<String> responseHandler = new BasicResponseHandler();
                             final String response = httpclient.execute(httppost, responseHandler);
                             System.out.println("Response : " + response);
                             runOnUiThread(new Runnable() {
                                    public void run() {
                                        p.dismiss();
                                         tv.setText("Response from PHP : " + response);
                                    }
                                });
                             
                         }catch(Exception e){
                              
                             runOnUiThread(new Runnable() {
                                public void run() {
                                    p.dismiss();
                                }
                            });
                             System.out.println("Exception : " + e.getMessage());
                         }
                    }
                };
 
                thread.start();
			}
		});
	}

}
