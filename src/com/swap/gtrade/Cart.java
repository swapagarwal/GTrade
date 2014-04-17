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

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Cart extends ListActivity{
	TextView tvlist;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    String item_nm[];
    String item_id[];
    String item_type[];
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(item_type[position].equalsIgnoreCase("auction")){
			Intent iAuction = new Intent(Cart.this,Auction.class);
			iAuction.putExtra("ITEM_ID", item_id[position].toString());
			startActivity(iAuction);
		}
		else{
			Intent iItem = new Intent(Cart.this,Item.class);
			iItem.putExtra("ITEM_ID", item_id[position].toString());
			startActivity(iItem);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart);
		
		tvlist=(TextView) findViewById(R.id.tvList);
		
         try{
             
             httpclient=new DefaultHttpClient();
             httppost= new HttpPost(getString(R.string.SERVER_IP)+"viewcart.php"); // make sure the url is correct.
             //add your data
             nameValuePairs = new ArrayList<NameValuePair>(1);
             // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
             nameValuePairs.add(new BasicNameValuePair("user_nm",PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_nm", "")));  // $Edittext_value = $_POST['Edittext_value'];
             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             //Execute HTTP Post Request
             response=httpclient.execute(httppost);
              
             ResponseHandler<String> responseHandler = new BasicResponseHandler();
             final String response = httpclient.execute(httppost, responseHandler);
             System.out.println("Response : " + response);
             runOnUiThread(new Runnable() {
                    public void run() {
                    	JSONArray jArray;
                    	JSONObject jObject;
						try {
							jArray = new JSONArray(response);
							if(jArray.length()==0){
								tvlist.setText("Your cart is empty.");
							}
							else{
								tvlist.setText("");
							}
							item_nm=new String[jArray.length()];
							item_id=new String[jArray.length()];
							item_type=new String[jArray.length()];
							for(int i=0;i<jArray.length();i++){
								jObject = jArray.getJSONObject(i);
								item_nm[i]=jObject.getString("item_nm");
								item_id[i]=jObject.getString("item_id");
								item_type[i]=jObject.getString("type");
							}
							setListAdapter(new ArrayAdapter<String>(Cart.this, android.R.layout.simple_list_item_1, item_nm));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(Cart.this, "JSON exception.", Toast.LENGTH_SHORT).show();
						}
                     }
             	});
         }catch(Exception e){
             System.out.println("Exception : " + e.getMessage());
         }
	}

}
