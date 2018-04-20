package com.lehow.plogin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.lehow.comm.base.BaseFragment;
import com.lehow.plogin.biz.UserEntity;
import com.lehow.plogin.di.DaggerLoginComponent;
import javax.inject.Inject;

public class LoginFragment extends BaseFragment implements LoginContract.View {

  @BindView(R.id.et_name) EditText etName;
  @BindView(R.id.et_pw) EditText etPw;

  @Inject LoginPresenter loginPresenter;
  private MyDialogFragment myDialogFragment;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DaggerLoginComponent.create().inject(this);
    loginPresenter.takeView(this);
  }

  @Override protected int getLayoutRes() {
    return R.layout.fragment_login;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    loginPresenter.onDestory();
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

  @Override public void show() {
    if (myDialogFragment == null) myDialogFragment = new MyDialogFragment();
    myDialogFragment.show(getFragmentManager(), "dialog");
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
