package com.mylab.aagje.listview;

import com.mylab.aagje.MainActivity;
import com.mylab.aagje.R;
import com.mylab.aagje.listview.MyExpandableListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ExpandableListView;

public class ListViewActivity extends Activity {

	SparseArray<Group> groups = new SparseArray<Group>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		createData();
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
		MyExpandableListAdapter adapter = new MyExpandableListAdapter(this,
				groups);
		listView.setAdapter(adapter);
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.buttonExitMain:
					Intent intent = new Intent(v.getContext(),
							MainActivity.class);
					startActivity(intent);
					finish();
					break;
				}
			}
		};
		findViewById(R.id.buttonExitMain).setOnClickListener(handler);
	}

	public void createData() {
		for (int i = 0; i < MainActivity.Assortments.size(); i++) {
			String assortname = MainActivity.Assortments.get(i);
			Group group = new Group(assortname);
			for (int j = 0; j < MainActivity.Assortment.size(); j++) {
				if (MainActivity.Assortment.get(j).getItemAssortment()
						.equals(assortname))
					group.children.add(MainActivity.Assortment.get(j));
				continue;
			}
			groups.append(i, group);
		}
	}

}
