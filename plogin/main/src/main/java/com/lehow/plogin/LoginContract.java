package com.lehow.plogin;

import com.lehow.plogin.base.BasePresenter;
import com.lehow.plogin.base.BaseView;
import com.lehow.plogin.base.ILoadingView;
import com.lehow.plogin.biz.UserEntity;

public interface LoginContract {

  interface View extends BaseView<Presenter>, ILoadingView {
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

  interface Presenter extends BasePresenter<View> {
    void login(String userName, String pw);

    void register(String userName, String pw);

    void onDestory();

  }
}
