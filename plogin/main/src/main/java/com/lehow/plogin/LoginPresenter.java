package com.lehow.plogin;

import android.text.TextUtils;
import android.util.Log;
import com.lehow.net.NetApiException;
import com.lehow.plogin.base.RxLifeCycleHelper;
import com.lehow.plogin.base.RxTransformers;
import com.lehow.plogin.biz.LoginBiz;
import com.lehow.plogin.biz.UserEntity;
import io.reactivex.functions.Consumer;

public class LoginPresenter implements LoginContract.Presenter {

  private static final String TAG = "LoginPresenter";
  private LoginContract.View loginView;
  private LoginBiz loginBiz;

  private RxLifeCycleHelper rxLifeCycleHelper = new RxLifeCycleHelper();

  public LoginPresenter(LoginBiz loginBiz) {
    this.loginBiz = loginBiz;
  }

  @Override public void login(String userName, String pw) {

    //TODO 基本的本地校验逻辑
    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pw)) {
      // 不能为空
      return;
    }

    loginBiz.login(userName, pw)
        .compose(RxTransformers.<UserEntity>io_main())
        .compose(RxTransformers.waitLoadingTransformer(loginView))
        .subscribe(new Consumer<UserEntity>() {
          @Override public void accept(UserEntity userEntity) throws Exception {
            Log.i(TAG, "accept: userEntity=" + userEntity.getUserName());
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) throws Exception {
            if (throwable instanceof NetApiException) {
              Log.i(TAG, "accept:登录信息错误 " + throwable.getMessage());
            } else {
              Log.i(TAG, "accept: " + throwable.getMessage());
            }
          }
        });

  }

  @Override public void register(String userName, String pw) {

  }

  @Override public void onDestory() {
    rxLifeCycleHelper.unsubscribe(RxLifeCycleHelper.ActivityEvent.DESTROY);
  }

  @Override public void takeView(LoginContract.View view) {
    this.loginView = view;
  }

  @Override public void dropView() {
    this.loginView = null;
  }


}
