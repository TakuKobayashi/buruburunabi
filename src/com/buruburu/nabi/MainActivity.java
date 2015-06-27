package com.buruburu.nabi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buruburu.nabi.SensorStreamer.SensorStreamListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(Config.DEBUG_KEY, "aaaaaaaaaaaaa");
		SensorStreamer.getInstance(SensorStreamer.class).addStraemListener(new SensorStreamListener() {
			@Override
			public void onOrientationParams(double pitch, double roll, double azimuth) {
				TextView t = (TextView) findViewById(R.id.DebugText);
				String s = "pitch:" + pitch + " roll:" + roll + " azimuth:" + azimuth;
				Log.d(Config.DEBUG_KEY, s);
				//t.setText(s);
			}
			
			@Override
			public void onGyroscopeParams(double x, double y, double z) {
				String s = "x:" + x + " y:" + y + " z:" + z;
				Log.d(Config.DEBUG_KEY, s);
				TextView t = (TextView) findViewById(R.id.DebugText);
				t.setText(s);
			}
		});
		//とりあえず入れておく
		VibratorController.getInstance(VibratorController.class).vibrate();
		SoundController s = SoundController.getInstance(SoundController.class);
		s.addSound("sample.wav");
		s.playCurrentSound();
	}
}
