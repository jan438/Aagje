package com.mylab.aagje.video;

import com.mylab.aagje.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity {

	// private String path =
	// "http://192.168.1.31/videos/B&B%20La%20Normande%20(Netherlands).mp4";
	// private String path =
	// "http://192.168.1.31/videos/documentariesandyou.mp4";
	// private String path =
	// "http://192.168.1.31/videos/B&BLaNormande(Netherlands).mp4";
//	private String path = "http://192.168.1.31/videos/documentariesandyou.mp4";
	private String path = "http://192.168.1.31/videos/B&B%20La%20Normande%20(Netherlands).mp4";
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_videoplayer);
		mVideoView = (VideoView) findViewById(R.id.surface_view);

		if (path == "") {
			Toast.makeText(
					VideoPlayerActivity.this,
					"Please edit VideoViewDemo Activity, and set path"
							+ " variable to your media file URL/path",
					Toast.LENGTH_LONG).show();

		} else {
			mVideoView.setVideoPath(path);
			mVideoView.setMediaController(new MediaController(this));
			mVideoView.requestFocus();
		}
	}
}
