package com.lehow.plogin;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.lehow.plogin.biz.UserEntity;
import com.lehow.plogin.di.DaggerLoginComponent;
import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

  @BindView(R.id.et_name) EditText etName;
  @BindView(R.id.et_pw) EditText etPw;

  @Inject LoginPresenter loginPresenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    DaggerLoginComponent.create().inject(this);
    loginPresenter.takeView(this);
  }

  @OnClick(R.id.btnLogin) void btnLogin() {
    loginPresenter.login("13725333333", "123456");
  }

  @OnClick(R.id.btnRegiste) void btnRegister() {

  }

  @Override public void onLoginSuccess(UserEntity userEntity) {

  }

  @Override public void onLoginAcountError() {

  }

  @Override public void onLoginFailed() {

  }

  @Override public FragmentManager getSupportFragmentManager() {
    return super.getSupportFragmentManager();
  }
}
