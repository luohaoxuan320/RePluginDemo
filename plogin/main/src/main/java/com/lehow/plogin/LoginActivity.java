package com.lehow.plogin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
  private MyDialogFragment myDialogFragment;

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

  @Override protected void onDestroy() {
    super.onDestroy();
    loginPresenter.onDestory();
  }

  @Override public void show() {
    if (myDialogFragment == null) myDialogFragment = new MyDialogFragment();
    myDialogFragment.show(getSupportFragmentManager(), "dialog");
  }

  @Override public void dismiss() {
    myDialogFragment.dismiss();
  }

  public static class MyDialogFragment extends DialogFragment {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.layout_dialog_fragment, container, false);
    }
  }
}
