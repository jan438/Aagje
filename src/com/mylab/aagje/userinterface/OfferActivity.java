package com.mylab.aagje.userinterface;

import java.util.Calendar;
import java.util.Random;
import com.mylab.aagje.MainActivity;
import com.mylab.aagje.ObjectItem;
import com.mylab.aagje.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class OfferActivity extends Activity {
	private ImageButton refreshButton;
	private ImageButton touchButton;
	private Handler handler = new Handler();
	static Random randomGenerator;
	int week_random;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar calender = Calendar.getInstance();
		int week = calender.get(Calendar.WEEK_OF_YEAR);
		randomGenerator = new Random(week);
		week_random = randomGenerator.nextInt(10);
		setContentView(R.layout.activity_offer);
		touchButton = (ImageButton) findViewById(R.id.toucharea);
		View parent = findViewById(R.id.layout);
		parent.post(new Runnable() {
			public void run() {
				Rect delegateArea = new Rect();
				ImageButton delegate = touchButton;
				delegate.getHitRect(delegateArea);
				TouchDelegate expandedArea = new TouchDelegate(delegateArea,
						delegate);
				if (View.class.isInstance(delegate.getParent())) {
					((View) delegate.getParent())
							.setTouchDelegate(expandedArea);
				}
			};
		});
		View view_instance = (View) findViewById(R.id.editablePhoto1);
		LayoutParams params = view_instance.getLayoutParams();
		params.width = 300;
		params.height = 300;
		view_instance.setLayoutParams(params);
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.buttonOffertoMain:
					Intent intent = new Intent(v.getContext(),
							MainActivity.class);
					startActivity(intent);
					finish();
					break;
				}
			}
		};
		findViewById(R.id.buttonOffertoMain).setOnClickListener(handler);
		refreshButton = (ImageButton) findViewById(R.id.refresh);
		refreshButton
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}

	public void onClick(View view) {
		final View photo = findViewById(R.id.editablePhoto1);
		final View progress = findViewById(R.id.progressBar1);
		progress.setVisibility(View.VISIBLE);
		refreshButton.setVisibility(View.GONE);
		ObjectItem offeritem = MainActivity.Assortment.get(week_random);
		touchButton.setImageBitmap(offeritem.getImage());
		TextView textViewItem = (TextView) findViewById(R.id.offerdescription);
		textViewItem.setTextColor(Color.BLUE);
		double offerprice = Double.parseDouble(offeritem.getItemPrice());
		double offerdiscount = offerprice * 10/100;
		offerprice = offerprice - offerdiscount;
		textViewItem.setText(Html.fromHtml("<h7>"
				+ offeritem.getItemName() + " " + String.format( "%.2f", offerprice) + "</h7>"));
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							if (photo != null
									&& photo instanceof RoundImageView) {
								RoundImageView photo2 = (RoundImageView) photo;
								photo2.setImageBitmap(BitmapFactory
										.decodeStream(getAssets().open(
												"offer.png")));
							}
							progress.setVisibility(View.GONE);
							refreshButton.setVisibility(View.VISIBLE);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}
		};
		new Thread(runnable).start();
		Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
	}
}