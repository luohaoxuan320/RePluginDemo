package com.lehow.test.vpfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.qihoo360.replugin.RePlugin;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

  private ViewPager viewPager;
  private TabLayout tabLayout;
  String[] plugins = new String[] {
      "pa:com.lehow.pa.PAFragment", "plogin:com.lehow.plogin.LoginFragment"
  };
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewPager = findViewById(R.id.viewPager);
    tabLayout = findViewById(R.id.tabLayout);

    tabLayout.setupWithViewPager(viewPager);
    viewPager.setAdapter(new MFragmentAdapter(getSupportFragmentManager()));

  }

  private Fragment[] getPluginFragments() {
    Fragment[] fragments = new Fragment[plugins.length];
    for (int i = 0; i < plugins.length; i++) {
      String[] split = plugins[i].split(":");
      Context pluginContext = RePlugin.fetchContext(split[0]);
      if (pluginContext != null) {
        ClassLoader d1ClassLoader = RePlugin.fetchClassLoader(split[0]);//获取插件的ClassLoader
        try {
          Fragment fragment = d1ClassLoader.loadClass(split[1])
              .asSubclass(Fragment.class)
              .newInstance();                                      //使用插件的Classloader获取指定Fragment实例
          fragments[i] = fragment;
        } catch (InstantiationException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
    return fragments;
  }

  private class MFragmentAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();

    public MFragmentAdapter(FragmentManager fm) {
      super(fm);
      fragments.add(AFragment.newInstance());
      fragments.addAll(Arrays.asList(getPluginFragments()));
    }

    @Override public CharSequence getPageTitle(int position) {
      return "A"+position;
    }

    @Override public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override public int getCount() {
      return fragments.size();
    }
  }
}
