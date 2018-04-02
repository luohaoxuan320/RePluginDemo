package com.lehow.plogin;

import android.text.TextUtils;
import android.util.Log;
import com.lehow.loading.LoadingSubscriber;
import com.lehow.plogin.biz.LoginBiz;
import com.lehow.plogin.biz.UserEntity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

  private static final String TAG = "LoginPresenter";
  private LoginContract.View loginView;
  private LoginBiz loginBiz;

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
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new LoadingSubscriber<UserEntity>(loginView, new Consumer<UserEntity>() {
          @Override public void accept(UserEntity userEntity) throws Exception {
            Log.i(TAG, "accept: userEntity=" + userEntity.getUserName());
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) throws Exception {
            if (throwable instanceof LoginBiz.LoginInfoException) {
              Log.i(TAG, "accept:登录信息错误 " + throwable.getMessage());
            } else {
              Log.i(TAG, "accept: " + throwable.getMessage());
            }
          }
        }));



    /*loginBiz.login(userName, pw).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<UserEntity>() {
      @Override public void accept(UserEntity userEntity) throws Exception {
        Log.i(TAG, "accept: userEntity="+userEntity.getUserName());
      }
    }, new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        if (throwable instanceof LoginBiz.LoginInfoException) {
          Log.i(TAG, "accept:登录信息错误 "+throwable.getMessage());
        }else{
          Log.i(TAG, "accept: "+throwable.getMessage());
        }
      }
    });*/
  }

  @Override public void register(String userName, String pw) {

  }

  @Override public void takeView(LoginContract.View view) {
    this.loginView = view;
  }

  @Override public void dropView() {
    this.loginView = null;
  }
}
