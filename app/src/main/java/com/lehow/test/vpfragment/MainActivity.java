package com.lehow.test.vpfragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import com.qihoo360.replugin.RePlugin;

public class MainActivity extends AppCompatActivity {

  private ViewPager viewPager;
  private TabLayout tabLayout;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewPager = findViewById(R.id.viewPager);
    tabLayout = findViewById(R.id.tabLayout);

    tabLayout.setupWithViewPager(viewPager);
    viewPager.setAdapter(new MFragmentAdapter(getSupportFragmentManager()));

  }

  private class MFragmentAdapter extends FragmentPagerAdapter {
    Fragment[] fragments;
    public MFragmentAdapter(FragmentManager fm) {
      super(fm);
      String paPluginName = "pa";
      final String paClassName = "com.lehow.pa.PAFragment";

      final ClassLoader paClassLoader = RePlugin.fetchClassLoader(paPluginName);//这里并不会保证插件的application进程被初始化
      //注意这里的坑，如果插件的fragment里面getPluginContext==null,那么这里必须调用fetchContext,才能实例化插件的application进程
      //插件fragment里面getPluginContext 才有效
      Log.i("TAG", "MFragmentAdapter: fetchContext="+RePlugin.fetchContext("pa"));
      try {
        Fragment fragmentPA =
            paClassLoader.loadClass(paClassName).asSubclass(Fragment.class).newInstance();
        fragments = new Fragment[] {
            AFragment.newInstance(), fragmentPA, AFragment.newInstance()
        };
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

    }

    @Override public CharSequence getPageTitle(int position) {
      return "A"+position;
    }

    @Override public Fragment getItem(int position) {
      return fragments[position];
    }

    @Override public int getCount() {
      return 3;
    }
  }
}
