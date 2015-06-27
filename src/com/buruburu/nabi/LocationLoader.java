package com.buruburu.nabi;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationLoader extends ContextSingletonBase<LocationLoader> implements LocationListener{

  private LocationManager _locationManager;
  private Location _location;
  private ArrayList<LocationUpdateListener> mListenerQueue;

  public void init(Context context){
    super.init(context);
    mListenerQueue = new ArrayList<LocationUpdateListener>();
    _locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    _location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
  }

  public void addUpdateListener(LocationUpdateListener listener){
    mListenerQueue.add(listener);
  }

  public void removeUpdateListener(LocationUpdateListener listener){
    mListenerQueue.remove(listener);
  }

  public void startRequestLocation(){
    _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); // 位置情報リスナー
    _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); // 位置情報リスナー
  }

  public void stopRequestLocation(){
    _locationManager.removeUpdates(this);
  }

  public Location getCurrentLocation(){
    return _location;
  }

  //デストラクタ
  @Override
  protected void finalize() throws Throwable {
    mListenerQueue.clear();
    super.finalize();
  }

  @Override
  public void onLocationChanged(Location location) {

	  Log.d("buruburunabi", "lon:"+ location.getLongitude() + "lat:"+ location.getLatitude());
    _location = location;
    for(LocationUpdateListener listener : mListenerQueue){
      listener.onUpdate(location);
    }
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

  @Override
  public void onProviderEnabled(String provider) {
  }

  @Override
  public void onProviderDisabled(String provider) {
  }

  public interface LocationUpdateListener{
    public void onUpdate(Location location);
  }
}
