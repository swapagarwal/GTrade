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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus.NmeaListener;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends ListActivity{
	Button btnSearch;
	EditText etSearch;
	TextView tv;
	ListView result;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    String item_nm[];//={"First Item","Second Item","Third Item"};
    String item_id[];//={"1","2","3"};
    String item_type[];
    
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(item_type[position].equalsIgnoreCase("auction")){
			Intent iAuction = new Intent(Search.this,Auction.class);
			iAuction.putExtra("ITEM_ID", item_id[position].toString());
			startActivity(iAuction);
		}
		else{
			Intent iItem = new Intent(Search.this,Item.class);
			iItem.putExtra("ITEM_ID", item_id[position].toString());
			startActivity(iItem);
		}
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		etSearch=(EditText) findViewById(R.id.etSearch);
		tv=(TextView) findViewById(R.id.tvSearch);
		result=getListView();
		//setListAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, item_nm));
		btnSearch=(Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
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
                             httppost= new HttpPost(getString(R.string.SERVER_IP)+"search.php"); // make sure the url is correct.
                             //add your data
                             nameValuePairs = new ArrayList<NameValuePair>(2);
                             // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                             nameValuePairs.add(new BasicNameValuePair("search",etSearch.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
                             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                             //Execute HTTP Post Request
                             response=httpclient.execute(httppost);
                              
                             ResponseHandler<String> responseHandler = new BasicResponseHandler();
                             final String response = httpclient.execute(httppost, responseHandler);
                             System.out.println("Response : " + response);
                             runOnUiThread(new Runnable() {
                                    public void run() {
                                        p.dismiss();
                                         if(response.contains("No results found.")){
                                        	 tv.setText("Response from PHP : " + response);
                                        	 item_nm=new String[0];
          									 item_id=new String[0];
          									 item_type=new String[0];
          									setListAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, item_nm));
                                         }
                                         else{
                                        	 tv.setText("");
                                        	 JSONArray jArray;
                                        	 JSONObject jObject;
             								try {
             									jArray = new JSONArray(response);
             									item_nm=new String[jArray.length()];
             									item_id=new String[jArray.length()];
             									item_type=new String[jArray.length()];
             									for(int i=0;i<jArray.length();i++){
             										jObject = jArray.getJSONObject(i);
             										item_nm[i]=jObject.getString("item_nm");
             										item_id[i]=jObject.getString("item_id");
             										item_type[i]=jObject.getString("type");
             									}
             									setListAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, item_nm));
             								} catch (JSONException e) {
             									// TODO Auto-generated catch block
             									e.printStackTrace();
             									Toast.makeText(Search.this, "JSON exception.", Toast.LENGTH_SHORT).show();
             								}
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
	}

}
