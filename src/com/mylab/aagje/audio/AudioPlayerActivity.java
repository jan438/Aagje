package com.mylab.aagje.audio;

import java.security.InvalidKeyException;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
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
	Encryption encryption;
	static byte[] header;
	static byte[] body;
	static byte[] encryptedheader;
	static byte[] encryptedbody;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		encryption = new Encryption();
		if ((Encryption.symKey == null) || (Encryption.c == null)) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Initialization encryption failed", Toast.LENGTH_LONG);
			toast.show();
		}
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
		if (songImage != null)
			albumart.setImageBitmap(songImage);
		TextView textView = (TextView) findViewById(R.id.songinfo);
		try {
			header = Encryption.sheader.getBytes();
			body = Encryption.sbody.getBytes();
			encryptedheader = Encryption.c.doFinal(header);
			encryptedbody = Encryption.c.doFinal(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String header = null;
		try {
			Encryption.c.init(Cipher.DECRYPT_MODE, Encryption.symKey);
			header = new String(Encryption.c.doFinal(encryptedheader));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		String description = null;
		try {
			Encryption.c.init(Cipher.DECRYPT_MODE, Encryption.symKey);
			description = new String(Encryption.c.doFinal(encryptedbody));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		Spannable styledText = new SpannableString(header + "\n" + description);
		TextAppearanceSpan span1 = new TextAppearanceSpan(this,
				R.style.textHeader);
		TextAppearanceSpan span2 = new TextAppearanceSpan(this,
				R.style.textbody);
		styledText.setSpan(span1, 0, header.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(span2, header.length() + 1, header.length() + 1
				+ description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(styledText);
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
			Toast toast = Toast.makeText(context, "Album Art missing",
					Toast.LENGTH_LONG);
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