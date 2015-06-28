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
    SpeachRecognizerController.getInstance(SpeachRecognizerController.class).init(this);
    VibratorController.getInstance(VibratorController.class).init(this);
    SoundController soundController = SoundController.getInstance(SoundController.class);
    soundController.init(this);
    soundController.addSound("1_up.wav");
    soundController.addSound("2_where.wav");
    soundController.addSound("3_setup.wav");
    soundController.addSound("4_resetup.wav");
    soundController.addSound("5_notfound.wav");
    soundController.addSound("6_complete.wav");
    soundController.addSound("left.wav");
    soundController.addSound("little_left.wav");
    soundController.addSound("little_right.wav");
    soundController.addSound("right.wav");
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    LocationLoader.getInstance(LocationLoader.class).stopRequestLocation();
    SensorStreamer.getInstance(SensorStreamer.class).stopSensor();
  }
}
