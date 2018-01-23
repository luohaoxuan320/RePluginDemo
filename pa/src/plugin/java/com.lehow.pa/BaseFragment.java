package com.lehow.pa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.qihoo360.replugin.RePlugin;

public abstract class BaseFragment extends Fragment {

  @Nullable @Override
  final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = LayoutInflater.from(RePlugin.getPluginContext()).inflate(getLayoutRes(), container, false);
    ButterKnife.bind(this,view);
    return view;
  }

  protected abstract int getLayoutRes();
}
