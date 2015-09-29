package com.mylab.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.mylab.aagje.LookupActivity;
import com.mylab.stub.IntentResult;
import android.app.Activity;
import android.content.Intent;

public class IntentIntegrator {

	private final LookupActivity activity;
	static List<String> barcodes = new ArrayList<String>();
	static Random randomGenerator = new Random();
	
	public IntentIntegrator(Activity activity) {
		this.activity = (LookupActivity) activity;
		barcodes.add("8710966701041");
		barcodes.add("8718158201331");
		barcodes.add("8714789762135");
		barcodes.add("8713091021671");
		barcodes.add("7290103302580");
		barcodes.add("8717496473806");
		barcodes.add("8717496474674");
		barcodes.add("4005808638833");
		barcodes.add("5412359807479");
		barcodes.add("5099746546526");
	}
	
    public int getBarcodeCount() {
		return barcodes.size();    	
    }
    
    public String getBarcode(int index) {
		return barcodes.get(index);    	
    }
   
	public static IntentResult parseActivityResult(int requestCode,
			int resultCode, Intent intent) {
		return new IntentResult(barcodes.get(randomGenerator.nextInt(10)), "", null, 0, "");
	}

	public void initiateScan() {
		activity.onActivityResult(0, 0, null);
	}
}
