package com.lehow.pa;

import android.app.Application;
import android.util.Log;

public class MyApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Log.i("MyApp", "onCreate: Application=" + this);
  }
}
