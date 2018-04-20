package com.lehow.pa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.lehow.comm.base.IContext;

public class PAFragment extends Fragment {

  @Nullable @Override
  final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = LayoutInflater.from(IContext.getRealContext(getContext()))
        .inflate(R.layout.fragment_pa, container, false);
    ButterKnife.bind(this, view);
    return view;
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
