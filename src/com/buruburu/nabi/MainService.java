package com.buruburu.nabi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MainService extends Service {
  // スレッドを停止するために必要
  private boolean mThreadActive = true;

  // スレッド処理
  private Runnable mTask = new Runnable() {
    @Override
    public void run() {
      // 永遠に動き続けてくれる
      while (mThreadActive) {
        Log.d(Config.DEBUG_KEY, "run");
      }
    }
  };
  private Thread mThread;

	@Override
    public void onCreate() {
        Log.d(Config.DEBUG_KEY, "onCreate");
        Toast.makeText(this, "MyService#onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
        Log.i(Config.DEBUG_KEY, "onDestroy");
     // スレッド停止
        this.mThread.interrupt();
        this.mThreadActive = false;
        Toast.makeText(this, "MyService#onDestroy", Toast.LENGTH_SHORT).show();
    }

	//サービスに接続するためのBinder
    public class MainServiceBinder extends Binder {
      //サービスの取得
      MainService getService() {
        return MainService.this;
      }
    }
    //Binderの生成
    private final IBinder mBinder = new MainServiceBinder();
    
	@Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "MyService#onBind"+ ": " + intent, Toast.LENGTH_SHORT).show();
        Log.d(Config.DEBUG_KEY, "onBind" + ": " + intent);
        return mBinder;
    }
 
    @Override
    public void onRebind(Intent intent){
        Toast.makeText(this, "MyService#onRebind"+ ": " + intent, Toast.LENGTH_SHORT).show();
        Log.d(Config.DEBUG_KEY, "onRebind" + ": " + intent);
    }
 
    @Override
    public boolean onUnbind(Intent intent){
        Toast.makeText(this, "MyService#onUnbind"+ ": " + intent, Toast.LENGTH_SHORT).show();
        Log.d(Config.DEBUG_KEY, "onUnbind" + ": " + intent);
 
        //onUnbindをreturn trueでoverrideすると次回バインド時にonRebildが呼ばれる
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Config.DEBUG_KEY, "onStartCommand Received start id " + startId + ": " + intent);
        Toast.makeText(this, "MyService#onStartCommand", Toast.LENGTH_SHORT).show();
        //コメントアウト外すと永遠に動き出すので注意
        //this.mThread = new Thread(null, mTask, "NortifyingService");
        //this.mThread.start(); 
        //明示的にサービスの起動、停止が決められる場合の返り値
        // START_NOT_STICKY,START_REDELIVER_INTENT,START_STICKY_COMPATIBILITY
        return START_STICKY;
    }
}
