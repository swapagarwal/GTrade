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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Item extends Activity{
	//TextView tv;
	TextView tvItemName,tvQuantity,tvCost,tvSeller,tvDescription;
	ImageView ivImage;
	Button btnBuy;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    String item_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);
		
		//tv=(TextView) findViewById(R.id.textView1);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    item_id = extras.getString("ITEM_ID");
		}
		else{
			finish();
			return;
		}
		
		tvCost=(TextView) findViewById(R.id.tvCost);
		tvDescription=(TextView) findViewById(R.id.tvDescription);
		tvItemName=(TextView) findViewById(R.id.tvItemName);
		tvQuantity=(TextView) findViewById(R.id.tvQuantity);
		tvSeller=(TextView) findViewById(R.id.tvSeller);
		ivImage=(ImageView) findViewById(R.id.ivImage);
		btnBuy=(Button) findViewById(R.id.btnBuy);
		
		@SuppressWarnings("static-access")
		final ProgressDialog p = new ProgressDialog(this).show(this,"Waiting for Server", "Accessing Server");
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                 try{
                     
                     httpclient=new DefaultHttpClient();
                     httppost= new HttpPost(getString(R.string.SERVER_IP)+"item.php"); // make sure the url is correct.
                     //add your data
                     nameValuePairs = new ArrayList<NameValuePair>(2);
                     // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                     nameValuePairs.add(new BasicNameValuePair("item_id",item_id));  // $Edittext_value = $_POST['Edittext_value'];
                     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                     //Execute HTTP Post Request
                     response=httpclient.execute(httppost);
                      
                     ResponseHandler<String> responseHandler = new BasicResponseHandler();
                     final String response = httpclient.execute(httppost, responseHandler);
                     if(response=="Item not found."){
                    	 //destroy xml and show item not found
                     }
                     System.out.println("Response : " + response);
                     runOnUiThread(new Runnable() {
                            public void run() {
                                p.dismiss();
                                 tvDescription.setText("Response from PHP : " + response);
                                 JSONObject object;
								try {
									
									object = new JSONObject(response);
									String item_nm = object.getString("item_nm");
									tvItemName.setText(item_nm);
									String quantity = object.getString("quantity");
									tvQuantity.setText("Quantity: "+quantity);
									String cost = object.getString("cost");
									tvCost.setText("Cost: "+cost);
									String seller = object.getString("user_nm");
									tvSeller.setText("Seller: "+seller);
									String description = object.getString("description");
	                                tvDescription.setText("Description:\n"+description);
	                                String img = object.getString("image");
	                                //getString(R.string.IMAGE_FOLDER)+pic_loc
	                                try{
	                                    byte [] encodeByte=Base64.decode(img,Base64.DEFAULT);
	                                    Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	                                    ivImage.setImageBitmap(bitmap);
	                                  }catch(Exception e){
	                                    e.getMessage();
	                                  }
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Toast.makeText(Item.this, "JSON exception.", Toast.LENGTH_SHORT).show();
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

}
