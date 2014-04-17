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
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button b,btnLogin,btnSearch,btnItem,btnAuction,btnCart,btnList;
    EditText et,etItem,etAuction;
    TextView tv;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button)findViewById(R.id.Button01);
        et= (EditText)findViewById(R.id.EditText01);
        tv= (TextView)findViewById(R.id.tv);
  
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 
                @SuppressWarnings("static-access")
				final ProgressDialog p = new ProgressDialog(v.getContext()).show(v.getContext(),"Waiting for Server", "Accessing Server");
                Thread thread = new Thread()
                {
                    @Override
                    public void run() {
                         try{
                             
                             httpclient=new DefaultHttpClient();
                             httppost= new HttpPost(getString(R.string.SERVER_IP)+"connection.php"); // make sure the url is correct.
                             //add your data
                             nameValuePairs = new ArrayList<NameValuePair>(1);
                             // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                             nameValuePairs.add(new BasicNameValuePair("Edittext_value",et.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
                             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                             //Execute HTTP Post Request
                             response=httpclient.execute(httppost);
                              
                             ResponseHandler<String> responseHandler = new BasicResponseHandler();
                             final String response = httpclient.execute(httppost, responseHandler);
                             System.out.println("Response : " + response);
                             runOnUiThread(new Runnable() {
                                    public void run() {
                                        p.dismiss();
                                         if(et.getText().toString().equals(response)){
                                        	 tv.setText("Response from PHP : " + response+" (Connected!)");
                                         }
                                         else{
                                        	 tv.setText("Response from PHP : " + response+" (Not connected!)");
                                         }
                                         
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
        btnLogin = (Button)findViewById(R.id.button2);
        btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iLogin = new Intent(MainActivity.this,Login.class);
				startActivity(iLogin);
			}
		});
        btnSearch = (Button)findViewById(R.id.button3);
        btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iSearch = new Intent(MainActivity.this,Search.class);
				startActivity(iSearch);
			}
		});
        etItem=(EditText) findViewById(R.id.editText1);
        btnItem = (Button)findViewById(R.id.button4);
        btnItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(etItem.getText().toString() != null && !etItem.getText().toString().equals("")){
					Intent iItem = new Intent(MainActivity.this,Item.class);
					iItem.putExtra("ITEM_ID", etItem.getText().toString());
					startActivity(iItem);
				}
				else{
					Toast.makeText(getApplicationContext(), "Please enter a item id.", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        etAuction=(EditText) findViewById(R.id.editText2);
        btnAuction = (Button)findViewById(R.id.button5);
        btnAuction.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(etAuction.getText().toString() != null && !etAuction.getText().toString().equals("")){
					Intent iAuction = new Intent(MainActivity.this,Auction.class);
					iAuction.putExtra("ITEM_ID", etAuction.getText().toString());
					startActivity(iAuction);
				}
				else{
					Toast.makeText(getApplicationContext(), "Please enter a item id.", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        btnCart = (Button)findViewById(R.id.button1);
        btnCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_nm", "").equals("")){
					Toast.makeText(MainActivity.this, "Please login first.", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent iCart = new Intent(MainActivity.this,Cart.class);
					startActivity(iCart);
				}
				
			}
		});
        btnList = (Button)findViewById(R.id.button6);
        btnList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_nm", "").equals("")){
					Toast.makeText(MainActivity.this, "Please login first.", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent iList = new Intent(MainActivity.this,WatchList.class);
					startActivity(iList);
				}
				
			}
		});
        

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
        startActivity(new Intent(this, Login.class));
        return true;
        default:
        return super.onOptionsItemSelected(item);
        }
    }
}
