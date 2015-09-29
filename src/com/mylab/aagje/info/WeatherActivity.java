package com.mylab.aagje.info;

import com.mylab.aagje.R;
import org.json.JSONException;
import com.mylab.aagje.MainActivity;
import com.mylab.aagje.weather.model.Weather;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class WeatherActivity extends Activity {

	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	private TextView hum;
	private ImageView imgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.buttonWeathertoMain:
					Intent intent = new Intent(v.getContext(),
							MainActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.buttonBoterCartoons:
					intent = new Intent(v.getContext(),
							BoterCartoonsActivity.class);
					startActivity(intent);
					break;
				}
			}
		};

		String city = "Amsterdam,NL";

		cityText = (TextView) findViewById(R.id.cityText);
		condDescr = (TextView) findViewById(R.id.condDescr);
		temp = (TextView) findViewById(R.id.temp);
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView) findViewById(R.id.windSpeed);
		windDeg = (TextView) findViewById(R.id.windDeg);
		imgView = (ImageView) findViewById(R.id.condIcon);
		findViewById(R.id.buttonWeathertoMain).setOnClickListener(handler);
		findViewById(R.id.buttonBoterCartoons).setOnClickListener(
				new OnClickListener() {
					public void onClick(View arg0) {
						AboutBox.Show(WeatherActivity.this);
					}
				});

		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[] { city });
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

			try {
				weather = JSONWeatherParser.getWeather(data);
				weather.iconData = ((new WeatherHttpClient())
						.getImage(weather.currentCondition.getIcon()));

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return weather;

		}

		@Override
		protected void onPostExecute(Weather weather) {
			super.onPostExecute(weather);

			if (weather.iconData != null) {
				imgView.setImageBitmap(weather.iconData);
			}

			cityText.setText("Weather in: " + weather.location.getCity() + ","
					+ weather.location.getCountry());
			condDescr.setText(weather.currentCondition.getCondition() + "("
					+ weather.currentCondition.getDescr() + ")");
			temp.setText(""
					+ Math.round((weather.temperature.getTemp() - 273.15))
					+ "°C");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windDeg.setText("" + weather.wind.getDeg() + "°");

		}

	}
}
