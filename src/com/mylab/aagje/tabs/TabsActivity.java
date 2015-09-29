package com.mylab.aagje.tabs;

import java.util.Random;
import com.mylab.aagje.MainActivity;
import com.mylab.aagje.ObjectItem;
import com.mylab.aagje.R;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TabsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static final String TAG = "TabsActivity";
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	static View rootView;
	static Random randomGenerator = new Random();
	static String[] assortments = { "Potatoes vegetables and fruits",
			"Bread and cake", "Meats" };
	static int assortment_index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section3)
				.setTabListener(this));
	}
	
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
				tab.getPosition() + 1);
		fragment.setArguments(args);
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class DummySectionFragment extends Fragment implements
			OnTouchListener, OnDragListener {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.activity_drag, container,
					false);
			int index1 = 0, index2 = 0, index3 = 0;
			assortment_index = getArguments().getInt(ARG_SECTION_NUMBER) - 1;
			ObjectItem article1, article2, article3;
			boolean found = false;
			while (!found) {
				index1 = randomGenerator.nextInt(10);
				article1 = MainActivity.Assortment.get(index1);
				if (article1.getItemAssortment().equals(
						assortments[assortment_index])) {
					found = true;
				}
			}
			found = false;
			while (!found) {
				index2 = randomGenerator.nextInt(10);
				article2 = MainActivity.Assortment.get(index2);
				if (article2.getItemAssortment().equals(
						assortments[assortment_index])) {
					found = true;
				}
			}
			found = false;
			while (!found) {
				index3 = randomGenerator.nextInt(10);
				article3 = MainActivity.Assortment.get(index3);
				if (article3.getItemAssortment().equals(
						assortments[assortment_index])) {
					found = true;
				}
			}
			ImageView imageView1 = (ImageView) rootView
					.findViewById(R.id.article1);
			imageView1
					.setImageBitmap(MainActivity.Assortment.get(index1).image);
			ImageView imageView2 = (ImageView) rootView
					.findViewById(R.id.article2);
			imageView2
					.setImageBitmap(MainActivity.Assortment.get(index2).image);
			ImageView imageView3 = (ImageView) rootView
					.findViewById(R.id.article3);
			imageView3
					.setImageBitmap(MainActivity.Assortment.get(index3).image);
			imageView1.setOnTouchListener(this);
			imageView2.setOnTouchListener(this);
			imageView3.setOnTouchListener(this);
			View.OnClickListener handler = new View.OnClickListener() {
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.buttonDragtoMain:
						Intent intent = new Intent(v.getContext(),
								MainActivity.class);
						startActivity(intent);
						getActivity().finish();
						break;
					}
				}
			};
			rootView.findViewById(R.id.top_container).setOnDragListener(this);
			rootView.findViewById(R.id.bottom_container)
					.setOnDragListener(this);
			rootView.findViewById(R.id.buttonDragtoMain).setOnClickListener(
					handler);
			return rootView;
		}

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				Log.i(TAG,
						"ACTION_DOWN Description: " + v.getContentDescription());
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				v.startDrag(null, shadowBuilder, v, 0);
				v.setVisibility(View.INVISIBLE);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean onDrag(View v, DragEvent e) {
			int aindex = 0;
			if (e.getAction() == DragEvent.ACTION_DROP) {
				Log.i(TAG,
						"ACTION_DROP Description: " + v.getContentDescription());
				View view = (View) e.getLocalState();
				ViewGroup from = (ViewGroup) view.getParent();
				from.removeView(view);
				LinearLayout to = (LinearLayout) v;
				to.addView(view);
				view.setVisibility(View.VISIBLE);
				int count = from.getChildCount();
				Log.i(TAG, "Child count from " + count);
				if (v.getContentDescription().equals("Bottom Container")
						&& (count > 0) && (MainActivity.Assortment.size() > 4)) {
					boolean found = false;
					while (!found) {
						aindex = randomGenerator.nextInt(10);
						ObjectItem article = MainActivity.Assortment
								.get(aindex);
						if (article.getItemAssortment().equals(
								assortments[assortment_index])) {
							found = true;
						}
					}
					ImageView tochange = (ImageView) from.getChildAt(0);
					tochange.setImageBitmap(MainActivity.Assortment.get(aindex).image);
				}
			}
			return true;
		}
	}
}
