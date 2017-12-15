package com.lehow.test.vpfragment;

import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.RePluginConfig;

public class MApplication extends RePluginApplication {

  @Override protected RePluginConfig createConfig() {
    RePluginConfig rePluginConfig = new RePluginConfig();
    //插件里面的fragment只参与编译，运行时找不到，此处配置，fragment用宿主的
    rePluginConfig.setUseHostClassIfNotFound(true);
    return rePluginConfig;
  }
}
