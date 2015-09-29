package com.mylab.aagje.info;

import com.mylab.aagje.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BoterCartoonsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        findViewById(R.id.buttonBoterCartoons).setOnClickListener(new OnClickListener(){
            public void onClick(View arg0) {
            	AboutBox.Show(BoterCartoonsActivity.this);
    	    }
        });
    }
}