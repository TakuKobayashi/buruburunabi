package com.buruburu.nabi;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class SensorStreamer extends ContextSingletonBase<SensorStreamer> implements SensorEventListener{

  private SensorManager _sensorManager;
  private ArrayList<SensorStreamListener> mListenerQueue;
  //地磁気
  private float[] _geomagnetic = new float[3];
  //加速度(重力)
  private float[] _accelerometer = new float[3];
  //ジャイロ(重力加速度)
  private float[] mGyroscope = new float[3];

  public void init(Context context){
    super.init(context);
    mListenerQueue = new ArrayList<SensorStreamListener>();
    _sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    Log.d(Config.DEBUG_KEY, "aksdgfhahgalkg");
  }

  public void addStraemListener(SensorStreamListener listener){
    mListenerQueue.add(listener);
  }

  public void removeStraemListener(SensorStreamListener listener){
    mListenerQueue.remove(listener);
  }

  public void startSensor(){
    //加速度センサー
    _sensorManager.registerListener(this, _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    //地磁気センサー
    _sensorManager.registerListener(this, _sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
    //ジャイロセンサー
    _sensorManager.registerListener(this, _sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
  }

  public void stopSensor(){
    _sensorManager.unregisterListener(this);
  }

  //デストラクタ
  @Override
  protected void finalize() throws Throwable {
    stopSensor();
    mListenerQueue.clear();
    super.finalize();
  }

  public interface SensorStreamListener{
    public void onOrientationParams(double pitch, double roll, double azimuth);
    public void onGyroscopeParams(double x, double y, double z);
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    switch(event.sensor.getType()) {
      case Sensor.TYPE_ACCELEROMETER:
        _accelerometer = event.values.clone();
        break;
      case Sensor.TYPE_MAGNETIC_FIELD:
        _geomagnetic = event.values.clone();
        break;
      case Sensor.TYPE_GYROSCOPE:
        mGyroscope = event.values.clone();
        break;
      }
      if(_geomagnetic != null && _accelerometer != null){
        /* 回転行列 */
        float[]  inR = new float[16];
        float[] outR = new float[16];
        SensorManager.getRotationMatrix(inR, null, _accelerometer, _geomagnetic);
        //Activityの表示が縦固定の場合。横向きになる場合、修正が必要です
        SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
        float[] orientation   = new float[3];
        SensorManager.getOrientation(outR, orientation);
        //_orientation[0]:Z軸 _orientation[1]:X軸 _orientation[2]:Y軸
        //Log.d(TAG, "pitch(X軸):"+Math.toDegrees(_orientation[1])+" roll(Y軸):"+Math.toDegrees(_orientation[2])+" azimuth(Z軸):"+Math.toDegrees(_orientation[0]));
        for(SensorStreamListener listener : mListenerQueue){
          listener.onOrientationParams(Math.toDegrees(orientation[1]), Math.toDegrees(orientation[2]), Math.toDegrees(orientation[0]));
        }
        _accelerometer = null;
        _geomagnetic = null;
      }else if(mGyroscope != null){
        for(SensorStreamListener listener : mListenerQueue){
          listener.onGyroscopeParams(mGyroscope[0], mGyroscope[1], mGyroscope[2]);
        }
        mGyroscope = null;
      }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    Log.d(Config.DEBUG_KEY, "sensorChanged");
  }
}
