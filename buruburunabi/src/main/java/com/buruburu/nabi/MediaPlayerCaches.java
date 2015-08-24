package com.buruburu.nabi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
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

//あらかじめ音声ファイルをすべてロードしておいた方が再生するときに遅延しない
//ただし、ファイルをすべてあらかじめ読み込んでおくためその分メモリを食うので注意
public class MediaPlayerCaches extends ContextSingletonBase{

  private HashMap<String, MediaPlayer> mSoundList;

  public void init(Context context){
    super.init(context);
    mSoundList = new HashMap<String, MediaPlayer>();
  }

  public boolean setMediaPlayersFromAssets(String... fileNames){
    mSoundList.clear();
    for(String fileName : fileNames){
      MediaPlayer sound = new MediaPlayer();
      try {
        AssetFileDescriptor afd = context.getAssets().openFd(fileName);
        sound.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        afd.close();
        sound.prepare();
        mSoundList.put(fileName, sound);
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }

  public MediaPlayer getMediaPlayer(String key){
    return mSoundList.get(key);
  }

  public void release(){
    for(MediaPlayer sound : mSoundList.values()){
      sound.stop();
      sound.release();
    }
    mSoundList.clear();
  }

  //デストラクタ
  @Override
  protected void finalize() throws Throwable {
    release();
    super.finalize();
  }
}
