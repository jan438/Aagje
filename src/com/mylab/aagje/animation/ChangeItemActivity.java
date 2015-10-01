package com.mylab.aagje.animation;

import com.mylab.aagje.MainActivity;
import com.mylab.aagje.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ChangeItemActivity extends Activity {
	String itemname = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeitem);
		EditText search = (EditText) findViewById(R.id.searchfield);
		search.setTypeface(MainActivity.tf, Typeface.BOLD_ITALIC);
		search.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if ((s != null) && (s.length() > 0)) {
					itemname = s.toString();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.buttonSearchSubmit:
					boolean found = false;
					for (int i = 0; i < MainActivity.Assortment.size(); i++) {
						if (MainActivity.Assortment.get(i).getItemName()
								.equals(itemname)) {
							found = true;
							Intent intent = new Intent(getApplicationContext(),
									AnimationActivity.class);
							intent.putExtra("itemindex", i);
							startActivity(intent);
							finish();
						}
					}
					ImageView imageView = (ImageView) findViewById(R.id.resulticon);
					if (!found) {
						imageView.setImageResource(R.drawable.no);
					} else {
						imageView.setImageResource(R.drawable.ok);
					}
					break;
				case R.id.buttonExit:
					Intent intent = new Intent(getApplicationContext(),
							AnimationActivity.class);
					startActivity(intent);
					finish();
					break;
				}
			}
		};
		findViewById(R.id.buttonSearchSubmit).setOnClickListener(handler);
		findViewById(R.id.buttonExit).setOnClickListener(handler);
		ImageButton btn = (ImageButton) findViewById(R.id.buttonUndo);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText search = (EditText) findViewById(R.id.searchfield);
				search.setText("");
			}
		});
	}
}
