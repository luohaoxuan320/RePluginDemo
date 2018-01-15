package com.lehow.plogin;

import android.text.TextUtils;
import com.lehow.plogin.biz.LoginBiz;
import javax.inject.Inject;

public class LoginPresenter implements LoginContract.Presenter {

  @Inject LoginBiz loginBiz;

  @Override public void login(String userName, String pw) {
    //TODO 基本的本地校验逻辑
    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pw)) {
      // 不能为空
      return;
    }

    loginBiz.login(userName, pw);
  }

  @Override public void register(String userName, String pw) {

  }
}
