package com.mylab.aagje.assortment;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;
import com.mylab.aagje.MainActivity;
import com.mylab.aagje.ObjectItem;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class AddActivity extends Activity {

	JSONObject jsonResponse;
	String resFromServer;
	boolean lookupfinished, found;
	Bitmap bitmap;
	int index = 0;
	int articlecount;
	private static final String TAG = "AddActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Add Assortment");
		found = addArticle("All");
		Intent mainintent = new Intent(this, MainActivity.class);
		startActivity(mainintent);
		finish();
	}

	private boolean addArticle(String barcode) {
		boolean found = false;
		JSONObject toSend = new JSONObject();
		try {
			toSend.put("msg", barcode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AddArticle transmitter = new AddArticle();
		transmitter.execute(new JSONObject[] { toSend });
		int count = 0;
		lookupfinished = false;
		while (!lookupfinished && (jsonResponse == null)) {
			try {
				Thread.sleep(1000);
				if (count == 10)
					continue;
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			if ((jsonResponse != null)
					&& (jsonResponse.getString("msg") != null)) {
				found = true;
				Log.v(TAG, "Response Received");
			} else
				Log.v(TAG, "No Response Received");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return found;
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	private class AddArticle extends
			AsyncTask<JSONObject, JSONObject, JSONObject> {

		String url = "http://192.168.1.31/BedAndBreakfast/php/aagje2.php";

		@Override
		protected JSONObject doInBackground(JSONObject... data) {
			JSONObject json = data[0];
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
			HttpPost post = new HttpPost(url);
			try {
				StringEntity se = new StringEntity("json=" + json.toString());
				post.addHeader("content-type",
						"application/x-www-form-urlencoded");
				post.setEntity(se);
				HttpResponse response;
				response = client.execute(post);
				String resFromServer = org.apache.http.util.EntityUtils
						.toString(response.getEntity());
				jsonResponse = new JSONObject(resFromServer);
				Log.i("JSON response from server",
						jsonResponse.getString("msg"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonResponse;
		}

		@Override
		protected void onPostExecute(JSONObject jsonResponse) {
			lookupfinished = true;
			String assortmentname;
			try {
				String s = jsonResponse.getString("msg");
				String[] ar = s.split(",");
				int count = ar.length;
				int index = 0;
				while (index < count) {
					bitmap = decodeBase64(ar[index + 3]);
					assortmentname = ar[index + 4];
					// ObjectItem(String itemId, String itemName, String
					// itemPrice, Bitmap image, String itemAssortment)
					ObjectItem objectitem = new ObjectItem(ar[index],
							ar[index + 1], ar[index + 2], bitmap,
							assortmentname, 0, true);
					MainActivity.Assortment.add(objectitem);
					boolean found = false;
					for (int i = 0; i < MainActivity.Assortments.size(); i++) {
						String ass = MainActivity.Assortments.get(i);
						if (ass.equals(assortmentname)) {
							found = true;
							continue;
						}
					}
					if (!found)
						MainActivity.Assortments.add(assortmentname);
					index = index + 5;
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}
}