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
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SpeachRecognizerController extends ContextSingletonBase implements RecognitionListener{

  private SpeechRecognizer _speechRecognizer;
  private ArrayList<SpeechRecognitionResultCallback> mListenerQueue;

  public void init(Context context){
    super.init(context);
    _speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
    _speechRecognizer.setRecognitionListener(this);
    mListenerQueue = new ArrayList<SpeechRecognitionResultCallback>();
  }

  public void addResultCallback(SpeechRecognitionResultCallback callback){
    mListenerQueue.add(callback);
  }

  public void removeUpdateListener(SpeechRecognitionResultCallback callback){
    mListenerQueue.remove(callback);
  }

  private void resultAction(Bundle results){
    float[] confidence = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
    ArrayList<String> recData = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    Log.d(Config.DEBUG_KEY, "" + recData);
    float prev = -1;
    int index = 0;
    for(int i = 0;i < confidence.length;++i){
      if(confidence[i] > prev){
        index = i;
      }
    }
    for(SpeechRecognitionResultCallback callback : mListenerQueue){
      callback.onResult(recData.get(index), confidence[index]);
    }
  }

  public void start(){
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
    _speechRecognizer.startListening(intent);
  }

  public void stop(){
    _speechRecognizer.stopListening();
  }

  public void finish(){
    _speechRecognizer.destroy();
  }

  public interface SpeechRecognitionResultCallback{
    public void onResult(String word, float confidence);
    public void onError(int error);
  }

  @Override
  public void onReadyForSpeech(Bundle params) {
    Log.d(Config.DEBUG_KEY, "onReadyForSpeech:" + params);
  }

  @Override
  public void onBeginningOfSpeech() {
    Log.d(Config.DEBUG_KEY, "onBeginningOfSpeech");
  }

  @Override
  public void onRmsChanged(float rmsdB) {
    Log.d(Config.DEBUG_KEY, "onRmsChanged:" + rmsdB);
  }

  @Override
  public void onBufferReceived(byte[] buffer) {
    for(int i = 0; i < buffer.length; i++){
      Log.d(Config.DEBUG_KEY, "onEndonBufferReceived:"+buffer[i]);
    }
  }

  @Override
  public void onEndOfSpeech() {
    Log.d(Config.DEBUG_KEY, "onEndOfSpeech");
  }

  @Override
  public void onError(int error) {
    Log.d(Config.DEBUG_KEY, "onError:" + error);
    for(SpeechRecognitionResultCallback callback : mListenerQueue){
      callback.onError(error);
    }
  }

  @Override
  public void onResults(Bundle results) {
    Log.d(Config.DEBUG_KEY, "onResults:" + results);
    resultAction(results);
    stop();
  }

  @Override
  public void onPartialResults(Bundle partialResults) {
    Log.d(Config.DEBUG_KEY, "onPartialResults:" + partialResults);
  }

  @Override
  public void onEvent(int eventType, Bundle params) {
    Log.d(Config.DEBUG_KEY, "onEvent:" + eventType);
    Log.d(Config.DEBUG_KEY, "onEvent:" + params);
  }
}
