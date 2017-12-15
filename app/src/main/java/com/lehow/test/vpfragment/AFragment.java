package com.lehow.test.vpfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class AFragment extends Fragment {

  public static AFragment newInstance() {
     Bundle args = new Bundle();
     AFragment fragment = new AFragment();
    fragment.setArguments(args);
    return fragment;
  }
  
  
  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_a,container,false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Toast.makeText(getContext(),"AAAA",Toast.LENGTH_SHORT).show();
      }
    });
  }
}
