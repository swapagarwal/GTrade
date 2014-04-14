package com.swap.gtrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Item extends Activity{
	//TextView tv;
	TextView tvItemName,tvQuantity,tvCost,tvSeller,tvDescription;
	ImageView ivImage;
	Button btnBuy,btnWatch;
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
		btnWatch=(Button) findViewById(R.id.btnWatch);
		
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
                     nameValuePairs = new ArrayList<NameValuePair>(1);
                     // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                     nameValuePairs.add(new BasicNameValuePair("item_id",item_id));  // $Edittext_value = $_POST['Edittext_value'];
                     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                     //Execute HTTP Post Request
                     response=httpclient.execute(httppost);
                      
                     ResponseHandler<String> responseHandler = new BasicResponseHandler();
                     final String response = httpclient.execute(httppost, responseHandler);
                     if(response.equals("Item not found.")){
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
        
        btnBuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Item.this)
				.setTitle("Confirm action")
				.setMessage("Are you sure you want to add this item to your cart?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						HttpClient httpclient = new DefaultHttpClient();
						HttpGet httpget = new HttpGet(getString(R.string.SERVER_IP)+"cart.php?item_id="+item_id+"&user_nm="+PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_nm", ""));
						try {
						    HttpResponse response = httpclient.execute(httpget);
						    if(response != null) {
						        String line = "";
						        InputStream inputstream = response.getEntity().getContent();
						        line = convertStreamToString(inputstream);
						        Toast.makeText(Item.this, line, Toast.LENGTH_SHORT).show();
						    } else {
						        Toast.makeText(Item.this, "Unable to complete your request", Toast.LENGTH_LONG).show();
						    }
						} catch (ClientProtocolException e) {
						    Toast.makeText(Item.this, "Caught ClientProtocolException", Toast.LENGTH_SHORT).show();
						} catch (IOException e) {
						    Toast.makeText(Item.this, "Caught IOException", Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
						    Toast.makeText(Item.this, "Caught Exception", Toast.LENGTH_SHORT).show();
						}
					}

					private String convertStreamToString(InputStream is) {
					    String line = "";
					    StringBuilder total = new StringBuilder();
					    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
					    try {
					        while ((line = rd.readLine()) != null) {
					            total.append(line);
					        }
					    } catch (Exception e) {
					        Toast.makeText(Item.this, "Stream Exception", Toast.LENGTH_SHORT).show();
					    }
					    return total.toString();
					}
				})
				.setNegativeButton("No", null)
				.show();
			}
		});
        
        btnWatch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Item.this)
				.setTitle("Confirm action")
				.setMessage("Are you sure you want to add this item to your watchlist?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						HttpClient httpclient = new DefaultHttpClient();
						HttpGet httpget = new HttpGet(getString(R.string.SERVER_IP)+"watch.php?item_id="+item_id+"&user_nm="+PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_nm", ""));
						try {
						    HttpResponse response = httpclient.execute(httpget);
						    if(response != null) {
						        String line = "";
						        InputStream inputstream = response.getEntity().getContent();
						        line = convertStreamToString(inputstream);
						        Toast.makeText(Item.this, line, Toast.LENGTH_SHORT).show();
						    } else {
						        Toast.makeText(Item.this, "Unable to complete your request", Toast.LENGTH_LONG).show();
						    }
						} catch (ClientProtocolException e) {
						    Toast.makeText(Item.this, "Caught ClientProtocolException", Toast.LENGTH_SHORT).show();
						} catch (IOException e) {
						    Toast.makeText(Item.this, "Caught IOException", Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
						    Toast.makeText(Item.this, "Caught Exception", Toast.LENGTH_SHORT).show();
						}
					}

					private String convertStreamToString(InputStream is) {
					    String line = "";
					    StringBuilder total = new StringBuilder();
					    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
					    try {
					        while ((line = rd.readLine()) != null) {
					            total.append(line);
					        }
					    } catch (Exception e) {
					        Toast.makeText(Item.this, "Stream Exception", Toast.LENGTH_SHORT).show();
					    }
					    return total.toString();
					}
				})
				.setNegativeButton("No", null)
				.show();
			}
		});
	}

}
