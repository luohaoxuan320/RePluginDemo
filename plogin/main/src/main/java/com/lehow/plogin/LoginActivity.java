package com.lehow.plogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

  @BindView(R.id.container) FrameLayout container;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    getSupportFragmentManager().beginTransaction()
        .add(R.id.container, new LoginFragment(), "login")
        .commit();
  }

}
