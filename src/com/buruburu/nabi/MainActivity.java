package com.buruburu.nabi;

import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buruburu.nabi.SensorStreamer.SensorStreamListener;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.media.MediaPlayer;
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
		SoundController s = SoundController.getInstance(SoundController.class);
		s.addSoundFinishListener(new SoundController.SoundCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				SoundController s = SoundController.getInstance(SoundController.class);
				s.changeCurrentSound("2_where.wav");
				s.playCurrentSound();
				s.removeSoundFinishListener(this);
			}
		});
		s.changeCurrentSound("1_up.wav");
		s.playCurrentSound();
		
		SensorStreamer.getInstance(SensorStreamer.class).addStraemListener(new SensorStreamListener() {
			@Override
			public void onOrientationParams(double pitch, double roll, double azimuth) {
				TextView t = (TextView) findViewById(R.id.DebugText);
				String s = "pitch:" + pitch + " roll:" + roll + " azimuth:" + azimuth;
				//Log.d(Config.DEBUG_KEY, s);
				//t.setText(s);
			}
			
			@Override
			public void onGyroscopeParams(double x, double y, double z) {
				String s = "x:" + x + " y:" + y + " z:" + z;
				//Log.d(Config.DEBUG_KEY, s);
				TextView t = (TextView) findViewById(R.id.DebugText);
				t.setText(s);
			}
		});
		//とりあえず入れておく
		/*
		VibratorController.getInstance(VibratorController.class).vibrate();
		SoundController s = SoundController.getInstance(SoundController.class);
		s.addSound("sample.wav");
		s.playCurrentSound();
		*/
		//isServiceRunnig("MainService");
		//startService(new Intent(MainActivity.this, MainService.class));
	}

    /**
     * サービスが実行中か
     * @param className サービスのクラス名
     * @return true: 実行中です
     */
    private boolean isServiceRunnig(String className) {
        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List<RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
         
        Log.i(Config.DEBUG_KEY, "Search Start: " + className);
        for (RunningServiceInfo curr : listServiceInfo) {
            Log.i(Config.DEBUG_KEY, "Check: " + curr.service.getClassName());
            if (curr.service.getClassName().contains(className)) {
                Log.i(Config.DEBUG_KEY + "isServiceRunnig", ">>>>>>FOUND!");
                return true;
            }
        }
        return false;
    }
}
