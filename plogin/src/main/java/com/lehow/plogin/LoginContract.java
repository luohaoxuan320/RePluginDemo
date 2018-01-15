package com.lehow.plogin;

import com.lehow.plogin.biz.UserEntity;

public interface LoginContract {

  interface View {
    /**
     * 登录成功
     */
    void onLoginSuccess(UserEntity userEntity);

    /**
     * 登录账号或者密码错误
     */
    void onLoginAcountError();

    /**
     * 登录失败，网络异常
     */
    void onLoginFailed();
  }

  interface Presenter {
    void login(String userName, String pw);

    void register(String userName, String pw);
  }
}
