package com.mylab.aagje.info;

import com.mylab.aagje.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutBox {
	  static String VersionName(Context context) {
	    try {
	      return context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
	    } catch (NameNotFoundException e) {
	      return "Unknown";
	    }
	  }
	  public static void Show(Activity callingActivity) {
	    SpannableString aboutText = new SpannableString("Version " + VersionName(callingActivity)+ "\n\n"
	+ callingActivity.getString(R.string.about));
	    View about;
	    TextView tvAbout;
	    try {
	      LayoutInflater inflater = callingActivity.getLayoutInflater();
	      about = inflater.inflate(R.layout.aboutbox, (ViewGroup) callingActivity.findViewById(R.id.aboutView));
	      tvAbout = (TextView) about.findViewById(R.id.aboutText);
	    } catch(InflateException e) {
	      about = tvAbout = new TextView(callingActivity);
	    }
	    tvAbout.setText(aboutText);
	    Linkify.addLinks(tvAbout, Linkify.ALL);
	    new AlertDialog.Builder(callingActivity)
	      .setTitle("About " + callingActivity.getString(R.string.botercartoons))
	      .setCancelable(true)
	      .setIcon(R.drawable.botercartoons)
	      .setPositiveButton("OK", null)
	      .setView(about)
	      .show();
	  }
	}