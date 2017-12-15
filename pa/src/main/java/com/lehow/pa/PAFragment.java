package com.lehow.pa;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PAFragment extends BaseFragment {

  @Override protected int getLayoutRes() {
    return R.layout.fragment_pa;
  }

  @Override public void onViewCreated(View view,  Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Toast.makeText(getActivity(), "PA PA PA", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
