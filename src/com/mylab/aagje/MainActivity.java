package com.mylab.aagje;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import com.mylab.aagje.animation.AnimationActivity;
import com.mylab.aagje.assortment.AddActivity;
import com.mylab.aagje.audio.AudioPlayerActivity;
import com.mylab.aagje.info.WeatherActivity;
import com.mylab.aagje.inventory.InventoryListActivity;
import com.mylab.aagje.jms.JMSDemoActivity;
import com.mylab.aagje.listview.ListViewActivity;
import com.mylab.aagje.tabs.TabsActivity;
import com.mylab.aagje.userinterface.OfferActivity;
import com.mylab.aagje.video.VideoPlayerActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;

public class MainActivity extends Activity {
	AlertDialog alertDialogArticles;
	final public static List<ObjectItem> Assortment = new ArrayList<ObjectItem>();
	public static List<String> Assortments = new ArrayList<String>();
	final public static List<ObjectItem> Cart = new ArrayList<ObjectItem>();
	boolean landscapemode = false;
	Display display;
	public static int width;
	public static int height;
	public static int notificationid;
	private static final String TAG = "MainActivity";
	public static Typeface tf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Configuration config = getResources().getConfiguration();
		int density = config.densityDpi;
		InetAddress inetAddress = null;
		String sinetAddress = "";
		Context context = getApplicationContext();
		tf = null;
		try {
			tf = Typeface.createFromAsset(getAssets(),
					"Montserrat-Regular.ttf");
		} catch (RuntimeException e) {
			Toast toast = Toast.makeText(context, "Font missing",
					Toast.LENGTH_LONG);
			toast.show();
		}
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						sinetAddress = inetAddress.toString();
						sinetAddress = sinetAddress.substring(1);
						Log.i(TAG, sinetAddress);
					}
				}
			}
		} catch (Exception e) {
			Log.e("------------", e.toString());
		}
		display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		if (width > height)
			landscapemode = true;
		CharSequence text = "Aagje version 1.0o " + density + " "
				+ landscapemode + " " + sinetAddress;
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
		if (Assortment.isEmpty()) {
			Intent intent = new Intent(context, AddActivity.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.activity_main);
		ColorAnimationDrawable mActionBarBackground = new ColorAnimationDrawable();
		getActionBar().setBackgroundDrawable(mActionBarBackground);
		mActionBarBackground.start();
		CheckBox checkCart = (CheckBox) findViewById(R.id.clearCart);
		checkCart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					int count = Cart.size();
					for (int i = count - 1; i >= 0; i--) {
						Cart.remove(i);
					}
				}
			}
		});
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.buttonShowList:
					showPopUp();
					break;
				case R.id.buttonScanArticle:
					Intent intent = new Intent(v.getContext(),
							LookupActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.buttonSendList:
					intent = new Intent(v.getContext(), JMSDemoActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.buttonVariousInformation:
					Intent subActivity = new Intent(MainActivity.this,
							WeatherActivity.class);
					Bundle scaleBundle = ActivityOptions.makeScaleUpAnimation(
							v, 0, 0, v.getWidth(), v.getHeight()).toBundle();
					startActivity(subActivity, scaleBundle);
					finish();
					break;
				case R.id.buttonTabs:
					if (Assortment.size() > 9) {
						subActivity = new Intent(MainActivity.this,
								TabsActivity.class);
						Bundle translateBundle = ActivityOptions
								.makeCustomAnimation(MainActivity.this,
										R.anim.slide_in_left,
										R.anim.slide_out_left).toBundle();
						startActivity(subActivity, translateBundle);
						finish();
					}
					break;
				case R.id.buttonUserInterface:
					ActivityOptions options = ActivityOptions
							.makeScaleUpAnimation(v, 0, 0, v.getWidth(),
									v.getHeight());
					intent = new Intent(v.getContext(), OfferActivity.class);
					startActivity(intent, options.toBundle());
					finish();
					break;
				case R.id.buttonInventoryList:
					intent = new Intent(v.getContext(),
							InventoryListActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.buttonAnimation:
					intent = new Intent(v.getContext(), AnimationActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.buttonListView:
					intent = new Intent(v.getContext(), ListViewActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.buttonPlayer:
					intent = new Intent(v.getContext(),
							AudioPlayerActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.buttonVideoPlayer:
					intent = new Intent(v.getContext(),
							VideoPlayerActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.buttonExitMain:
					finish();
					break;
				}
			}
		};
		findViewById(R.id.buttonShowList).setOnClickListener(handler);
		findViewById(R.id.buttonScanArticle).setOnClickListener(handler);
		findViewById(R.id.buttonSendList).setOnClickListener(handler);
		findViewById(R.id.buttonVariousInformation).setOnClickListener(handler);
		findViewById(R.id.buttonTabs).setOnClickListener(handler);
		findViewById(R.id.buttonUserInterface).setOnClickListener(handler);
		findViewById(R.id.buttonInventoryList).setOnClickListener(handler);
		findViewById(R.id.buttonAnimation).setOnClickListener(handler);
		findViewById(R.id.buttonListView).setOnClickListener(handler);
		findViewById(R.id.buttonPlayer).setOnClickListener(handler);
		findViewById(R.id.buttonVideoPlayer).setOnClickListener(handler);
		findViewById(R.id.buttonExitMain).setOnClickListener(handler);
	}

	public void showPopUp() {
		ArrayAdapterItem adapter = new ArrayAdapterItem(this,
				R.layout.list_view_row_item, Cart);
		ListView listViewItems = new ListView(this);
		listViewItems.setAdapter(adapter);
		alertDialogArticles = new AlertDialog.Builder(MainActivity.this)
				.setView(listViewItems).setTitle("Articles").show();
	}
}