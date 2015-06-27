package com.buruburu.nabi;

import android.app.Application;

public class BuruburunabiApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    LocationLoader.getInstance(LocationLoader.class).init(this);
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    LocationLoader.getInstance(LocationLoader.class).stopRequestLocation();
  }
}