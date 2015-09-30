package com.mylab.aagje.audio;

import java.util.HashMap;
import com.mylab.aagje.MainActivity;
import com.mylab.aagje.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class AudioPlayerActivity extends Activity implements OnClickListener,
		OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {

	private ImageButton buttonPlayPause;
	private SeekBar seekBarProgress;
	public EditText editTextSongURL;
	private MediaPlayer mediaPlayer;
	private int mediaFileLengthInMilliseconds;
	private final Handler handler = new Handler();
	MediaMetadataRetriever metaRetriver;
	byte[] art;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audioplayer);
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.buttonExit:
					if (mediaPlayer != null) {
						mediaPlayer.stop();
					}
					Intent intent = new Intent(v.getContext(),
							MainActivity.class);
					startActivity(intent);
					finish();
					break;
				}
			}
		};
		findViewById(R.id.buttonExit).setOnClickListener(handler);
		initView();
	}

	private void initView() {
		buttonPlayPause = (ImageButton) findViewById(R.id.ButtonTestPlayPause);
		buttonPlayPause.setOnClickListener(this);

		seekBarProgress = (SeekBar) findViewById(R.id.SeekBarTestPlay);
		seekBarProgress.setMax(99);
		seekBarProgress.setOnTouchListener(this);
		editTextSongURL = (EditText) findViewById(R.id.EditTextSongURL);
		editTextSongURL.setText(R.string.testsong);
		Bitmap songImage = downloadBitmap("http://192.168.1.31/music/1050f49223064225a8b3a0fe9f38677f.mp3");
		ImageView albumart = (ImageView) findViewById(R.id.album_art);
		if (songImage != null) albumart.setImageBitmap(songImage);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
	}

	private Bitmap downloadBitmap(final String url) {
		final MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		Bitmap songImage = null;
		metaRetriever.setDataSource(url, new HashMap<String, String>());
		try {
			final byte[] art = metaRetriever.getEmbeddedPicture();
			songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
		} catch (Exception e) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "Album Art missing", Toast.LENGTH_LONG);
			toast.show();
		}
		return songImage;
	}

	private void primarySeekBarProgressUpdater() {
		seekBarProgress.setProgress((int) (((float) mediaPlayer
				.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));

		if (mediaPlayer.isPlaying()) {
			Runnable notification = new Runnable() {
				public void run() {
					primarySeekBarProgressUpdater();
				}
			};
			handler.postDelayed(notification, 1000);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ButtonTestPlayPause) {
			try {
				mediaPlayer.setDataSource(editTextSongURL.getText().toString());

				mediaPlayer.prepare();

			} catch (Exception e) {
				e.printStackTrace();
			}

			mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

			if (!mediaPlayer.isPlaying()) {
				mediaPlayer.start();
				buttonPlayPause.setImageResource(R.drawable.button_pause);
			} else {
				mediaPlayer.pause();
				buttonPlayPause.setImageResource(R.drawable.button_play);
			}

			primarySeekBarProgressUpdater();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.SeekBarTestPlay) {

			if (mediaPlayer.isPlaying()) {
				SeekBar sb = (SeekBar) v;
				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100)
						* sb.getProgress();
				mediaPlayer.seekTo(playPositionInMillisecconds);
			}
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {

		buttonPlayPause.setImageResource(R.drawable.button_play);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {

		seekBarProgress.setSecondaryProgress(percent);
	}
}