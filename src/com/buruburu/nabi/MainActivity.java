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
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private boolean mProcessing;
	private void startNavigation(){
		LocationLoader.getInstance(LocationLoader.class).addUpdateListener(new LocationLoader.LocationUpdateListener() {
			@Override
			public void onUpdate(Location location) {

			}
		});
		SensorStreamer.getInstance(SensorStreamer.class).addStraemListener(new SensorStreamListener() {
			@Override
			public void onOrientationParams(double pitch, double roll, double azimuth) {
				TextView t = (TextView) findViewById(R.id.DebugText);
				String s = "pitch:" + pitch + " roll:" + roll + " azimuth:" + azimuth;
				VibratorController vib = VibratorController.getInstance(VibratorController.class);
				if(vib.isVibrating) return;
				double diffDegree = azimuth - calcAzimuthAngleDegree();
				SoundController sound = SoundController.getInstance(SoundController.class);
				if(diffDegree < -25){
					vib.vibrate(VibratorController.Pattern.TurnRight);
					sound.changeCurrentSound("little_right.wav");
					sound.playCurrentSound();
				}else if(diffDegree > 25){
					vib.vibrate(VibratorController.Pattern.TurnLeft);
					sound.changeCurrentSound("little_left.wav");
					sound.playCurrentSound();
				}
				Log.d(Config.DEBUG_KEY,""+ diffDegree);

				//Log.d(Config.DEBUG_KEY, s);
				t.setText(s);
			}

			@Override
			public void onGyroscopeParams(double x, double y, double z) {
				String s = "x:" + x + " y:" + y + " z:" + z;
				//Log.d(Config.DEBUG_KEY, s);
				//TextView t = (TextView) findViewById(R.id.DebugText);
				//t.setText(s);
			}
		});
	}

	private double calcAzimuthAngleDegree(){
		//参考:http://kawae.dyndns.org/xt/modules/xpwiki/?%E6%96%B9%E4%BD%8D%E8%A7%92%E3%81%AE%E8%A8%88%E7%AE%97%E5%BC%8F
		//地点(B1,L1)から地点(B2,L2)を見た場合の方位角φと距離dの計算式
		//φ=atan2(cos(B2) * sin(L2 - L1) , cos(B1) * sin(B2) - sin(B1) * cos(B2) * cos(L2 - L1)
		//B(緯度)は北緯が正値, 南緯は負値
		//L(経度)は東経が正値, 西経は負値
		LocationLoader l = LocationLoader.getInstance(LocationLoader.class);
		Location lo = l.getCurrentLocation();
		double targetLatitudeRad = Math.toRadians(35.615596);
		double differentialLongitudeRad = Math.toRadians(139.777515 - lo.getLongitude());
		double deviceLatitudeRad = Math.toRadians(lo.getLatitude());

		double theta =Math.atan2(Math.cos(targetLatitudeRad) * Math.sin(differentialLongitudeRad), Math.cos(deviceLatitudeRad) * Math.sin(targetLatitudeRad) - Math.sin(deviceLatitudeRad) * Math.cos(targetLatitudeRad) * Math.cos(differentialLongitudeRad));
		return Math.toDegrees(theta);
	}


	private void setupSpeechRecognize(){
		mProcessing = false;
		SpeachRecognizerController sp = SpeachRecognizerController.getInstance(SpeachRecognizerController.class);
		sp.addResultCallback(new SpeachRecognizerController.SpeechRecognitionResultCallback() {
			@Override
			public void onResult(String word, float confidence) {
				mProcessing = true;
				SoundController s = SoundController.getInstance(SoundController.class);
				s.changeCurrentSound("3_setup.wav");
				s.addSoundFinishListener(new SoundController.SoundCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						SoundController s = SoundController.getInstance(SoundController.class);
						s.removeSoundFinishListener(this);
						startNavigation();
					}
				});
				s.playCurrentSound();
			}

			@Override
			public void onError(int error) {
				if (!mProcessing) {
					SpeachRecognizerController sp = SpeachRecognizerController.getInstance(SpeachRecognizerController.class);
					sp.removeUpdateListener(this);
					SoundController s = SoundController.getInstance(SoundController.class);
					s.changeCurrentSound("5_notfound.wav");
					s.addSoundFinishListener(new SoundController.SoundCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							SoundController s = SoundController.getInstance(SoundController.class);
							s.removeSoundFinishListener(this);
							setupSpeechRecognize();
						}
					});
					s.playCurrentSound();
				}
			}
		});
		sp.start();
	}

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
				s.removeSoundFinishListener(this);
				s.changeCurrentSound("2_where.wav");
				s.addSoundFinishListener(new SoundController.SoundCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						SoundController s = SoundController.getInstance(SoundController.class);
						s.removeSoundFinishListener(this);
						setupSpeechRecognize();
					}
				});
				s.playCurrentSound();
			}
		});
		s.changeCurrentSound("1_up.wav");
		s.playCurrentSound();

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
