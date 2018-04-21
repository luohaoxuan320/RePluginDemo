package com.lehow.comm.base;

import android.content.Context;
import com.qihoo360.replugin.RePlugin;

public class IContext {

  public static Context getLocalContext(Context context) {
    return RePlugin.getPluginContext();
  }
}
