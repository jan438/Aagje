package com.mylab.aagje.inventory;

import com.mylab.aagje.MainActivity;
import com.mylab.aagje.ObjectItem;
import com.mylab.aagje.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class InventoryListActivity extends Activity {
	String pageData[] = new String[MainActivity.Assortment.size()];
	LayoutInflater inflater;
	ViewPager vp;
	Context context;
	ObjectItem item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.activity_inventorylist);
		for (int i = 0; i < MainActivity.Assortment.size(); i++) {
			pageData[i] = MainActivity.Assortment.get(i).getItemName();
		}
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vp = (ViewPager) findViewById(R.id.viewPager);
		vp.setAdapter(new MyPagesAdapter());
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.buttonShoppingListtoMain:
					Intent intent = new Intent(v.getContext(),
							MainActivity.class);
					startActivity(intent);
					finish();
					break;
				}
			}
		};
		findViewById(R.id.buttonShoppingListtoMain).setOnClickListener(handler);
	}

	class MyPagesAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return pageData.length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View page = inflater.inflate(R.layout.inventorylist_page, null);
			((TextView) page.findViewById(R.id.textPager))
					.setText(pageData[position]);
			((ImageView) page.findViewById(R.id.imagePager))
					.setImageBitmap(MainActivity.Assortment.get(position)
							.getImage());
			final EditText et = ((EditText) page.findViewById(R.id.quantityPager));
			Switch sw = ((Switch) page.findViewById(R.id.switch1));
			sw.setChecked(MainActivity.Assortment.get(position).getOnoff());
			sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					boolean bsw = MainActivity.Assortment.get(position)
							.getOnoff();
					if (bsw == true)
						MainActivity.Assortment.get(position).setOnoff(false);
					else
						MainActivity.Assortment.get(position).setOnoff(true);
				}
			});
			et.setText(Integer.toString(MainActivity.Assortment.get(position)
					.getCount()));
			et.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
					int duration = Toast.LENGTH_LONG;
					String scount = s.toString();
					Toast toast = Toast.makeText(context, scount, duration);
					toast.show();
					if ((scount != null) && (scount.length() > 0))
						MainActivity.Assortment.get(position).setCount(
								Integer.parseInt(scount));
					else
						MainActivity.Assortment.get(position).setCount(0);
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}
			});
			ImageButton btn = (ImageButton) page.findViewById(R.id.buttonUndo);
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					et.setText("");
				}
			});
			((ViewPager) container).addView(page, 0);
			return page;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (View) arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
			object = null;
		}
	}
}
