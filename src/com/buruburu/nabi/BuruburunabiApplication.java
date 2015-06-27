package com.buruburu.nabi;

import android.app.Application;

public class BuruburunabiApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    LocationLoader locationLoader = LocationLoader.getInstance(LocationLoader.class);
    locationLoader.init(this);
    locationLoader.startRequestLocation();
    SensorStreamer sensorStreamer = SensorStreamer.getInstance(SensorStreamer.class);
    sensorStreamer.init(this);
    sensorStreamer.startSensor();
    SpeachRecognizerController.getInstance(SpeachRecognizerController.class).init(this);;
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    LocationLoader.getInstance(LocationLoader.class).stopRequestLocation();
    SensorStreamer.getInstance(SensorStreamer.class).stopSensor();
  }
}
