package com.mylab.aagje;

/*import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;*/
import com.mylab.stub.IntentResult;
import com.mylab.stub.IntentIntegrator;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

public class LookupActivity extends Activity {
	JSONObject jsonResponse;
	String resFromServer;
	boolean lookupfinished;
	Bitmap bitmap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanningResult != null) {
			String barcode = scanningResult.getContents();
			Toast toast = Toast.makeText(getApplicationContext(), barcode,
					Toast.LENGTH_LONG);
			toast.show();
			boolean found;
			found = false;
			for (int i = 0; i < MainActivity.Cart.size(); i++) {
				if (MainActivity.Cart.get(i).getItemId().equals(barcode)) {
					MainActivity.Cart.get(i).count++;
					found = true;
					continue;
				}
			}
			if (!found) {
				for (int i = 0; i < MainActivity.Assortment.size(); i++) {
					if (MainActivity.Assortment.get(i).getItemId()
							.equals(barcode)) {
						ObjectItem asitem = MainActivity.Assortment.get(i);
						if (asitem.getOnoff()) {
							ObjectItem cartitem = new ObjectItem(barcode,
									asitem.getItemName(),
									asitem.getItemPrice(), asitem.image,
									asitem.getItemAssortment(), 1, true);
							MainActivity.Cart.add(cartitem);
						}
						found = true;
						continue;
					}
				}
			}
			Intent mainintent = new Intent(this, MainActivity.class);
			startActivity(mainintent);
			finish();
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data received!", Toast.LENGTH_LONG);
			toast.show();
		}
	}

}
