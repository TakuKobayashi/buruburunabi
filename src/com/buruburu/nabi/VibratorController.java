package com.buruburu.nabi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

  public void init(Context context){
    super.init(context);
    mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
  }

  public void vibrate(){
    //TODO テキトー
    long[] pattern = {0, 1000, 2000, 5000, 3000, 1000}; // OFF/ON/OFF/ON...
    mVibrator.vibrate(pattern, -1);
  }
}
