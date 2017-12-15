package com.lehow.pa;

import android.app.Application;
import android.util.Log;
import com.qihoo360.replugin.RePlugin;

public class MyApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Log.i("MyApp", "onCreate: getHostContext="+ RePlugin.getHostContext());
    Log.i("MyApp", "onCreate: getPluginContext="+RePlugin.getPluginContext());
    Log.i("MyApp", "onCreate: Application="+this);
  }
}
