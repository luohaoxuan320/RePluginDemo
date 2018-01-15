package com.lehow.plogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.et_name) EditText etName;
  @BindView(R.id.et_pw) EditText etPw;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @OnClick(R.id.btnLogin) void btnLogin() {

  }

  @OnClick(R.id.btnRegiste) void btnRegister() {

  }
}
