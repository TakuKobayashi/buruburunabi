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
    MediaPlayerCaches caches = MediaPlayerCaches.getInstance(MediaPlayerCaches.class);
    caches.init(this);
    caches.setMediaPlayersFromAssets("1_up.wav",
                                     "2_where.wav",
                                     "3_setup.wav",
                                     "4_resetup.wav",
                                     "5_notfound.wav",
                                     "6_complete.wav",
                                     "left.wav",
                                     "little_left.wav",
                                     "little_right.wav",
                                     "right.wav"
                                    );
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
  }
}
