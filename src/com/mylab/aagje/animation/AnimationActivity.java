package com.mylab.aagje.animation;

import java.util.Random;

import com.mylab.aagje.MainActivity;
import com.mylab.aagje.R;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationActivity extends Activity {
	int itemindex = -1, animation_item_index;
	Bitmap choosenitem;
	NotificationManager nm;
	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MainActivity.notificationid = 1;
		Bundle extras = getIntent().getExtras();
		context = getApplicationContext();
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (extras != null) {
			itemindex = extras.getInt("itemindex");
		}
		setContentView(R.layout.activity_animation);
		if (itemindex < 0) {
			Random randomGenerator = new Random();
			animation_item_index = randomGenerator.nextInt(10);
		} else {
			animation_item_index = itemindex;
		}
		((TextView) findViewById(R.id.textView1))
				.setText(MainActivity.Assortment.get(animation_item_index)
						.getItemName());
		if (choosenitem != null)
			choosenitem.recycle();
		choosenitem = getResizedBitmap(
				MainActivity.Assortment.get(animation_item_index).getImage(),
				144, 144);
		((ImageView) findViewById(R.id.imageView1)).setImageBitmap(choosenitem);
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	public void startAnimation(View v) {
		final ImageView aniView = (ImageView) findViewById(R.id.imageView1);

		switch (v.getId()) {
		case R.id.ButtonStartAnimation:
			nm.cancel(MainActivity.notificationid);
			ObjectAnimator fadeOut = ObjectAnimator.ofFloat(aniView, "alpha",
					0f);
			fadeOut.setDuration(2000);
			ObjectAnimator mover = ObjectAnimator.ofFloat(aniView,
					"translationX", -500f, 0f);
			mover.setDuration(2000);
			ObjectAnimator fadeIn = ObjectAnimator.ofFloat(aniView, "alpha",
					0f, 1f);
			fadeIn.setDuration(2000);
			AnimatorSet animatorSet = new AnimatorSet();
			mover.addListener(new AnimatorListener() {
			    @Override 
			    public void onAnimationEnd(Animator animation) {
					Intent notificationIntent = new Intent(context, MainActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(context, 88,
							notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
					Resources res = context.getResources();
					Notification.Builder builder = new Notification.Builder(context);
					builder.setContentIntent(contentIntent)
							.setSmallIcon(R.drawable.barcode)
							.setLargeIcon(
									BitmapFactory.decodeResource(res, R.drawable.barcode))
							.setTicker(res.getString(R.string.articlesdelivered))
							.setWhen(System.currentTimeMillis()).setAutoCancel(true)
							.setContentTitle(res.getString(R.string.animation))
							.setContentText(res.getString(R.string.articlesdelivered));
					Notification n = builder.build();
					nm.notify(MainActivity.notificationid, n);
			    }

				@Override
				public void onAnimationCancel(Animator animation) {	
				}

				@Override
				public void onAnimationRepeat(Animator animation) {		
				}

				@Override
				public void onAnimationStart(Animator animation) {			
				}
			});
			animatorSet.play(mover).with(fadeIn).after(fadeOut);		
			animatorSet.start();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.itemmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_search:
			Intent intent = new Intent(this, ChangeItemActivity.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.action_back:
			nm.cancel(MainActivity.notificationid);
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}