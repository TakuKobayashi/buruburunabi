package com.buruburu.nabi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VibratorController extends ContextSingletonBase<VibratorController>{

  private Vibrator mVibrator;
  public boolean isVibrating;
  private Timer mVibratorTimer;

  public enum Pattern{
    Alert,
    TurnLeft,
    TurnRight,
    Worn
  }

  public void init(Context context){
    super.init(context);
    mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    mVibratorTimer = new Timer(true);
    isVibrating = false;
  }

  public void vibrate(Pattern pattern){
    Log.d(Config.DEBUG_KEY, "vibrating:"+ isVibrating);
    if(isVibrating) return;
    long[] patterns = {0, 1000, 2000, 5000, 3000, 1000}; // OFF/ON/OFF/ON...
    if(pattern == Pattern.Alert) {
      // その１　交差点に接近などの警告ゾーンは　点滅ぶる
      // 0.5秒ブル＋0.4休止の5回繰り返し
      patterns = new long[]{0, 500, 400, 500, 400, 500, 400, 500, 400, 500}; // OFF/ON/OFF/ON...
    }else if(pattern == Pattern.TurnLeft) {
      // その2　左折指示　で　１ぶる
      // 1秒ブル＋2秒休止の３回繰り返
      patterns = new long[]{0, 1000, 2000, 1000, 2000, 1000, 2000}; // OFF/ON/OFF/ON...
    }else if(pattern == Pattern.TurnRight) {
      //その3　右折指示　で　２ぶる
      //0.5秒ブル＋0.5秒休止+1.5秒ブル+2秒休止の３回繰り返し
      patterns = new long[]{0, 500, 500, 1500, 2000, 500, 500, 1500, 2000, 500, 500, 1500}; // OFF/ON/OFF/ON...
    }else if(pattern == Pattern.Worn){
        //その4　間違え指示　で　連続ぶる
        //5秒ブル＋1秒休止の2回繰り返し
        patterns = new long[]{0,5000,1000,5000}; // OFF/ON/OFF/ON...
    }
    isVibrating = true;
    long sum = 0;
    for(int i = 0;i < patterns.length;++i) {
      sum += patterns[i];
    }
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        isVibrating = false;
      }
    };
    mVibratorTimer.schedule(task, sum);
    mVibrator.vibrate(patterns, -1);
  }
}
