package com.lehow.plogin.biz;

import com.lehow.plogin.utils.Md5Utils;
import io.reactivex.Flowable;

public class LoginBiz {

  LoginApi loginApi;

  public LoginBiz(LoginApi loginApi) {
    this.loginApi = loginApi;
  }

  public Flowable<UserEntity> login(String userName, String pw) {
    return loginApi.login(userName, Md5Utils.toMd5(pw));
      /*  .map(new Function<HttpResult<UserEntity>, UserEntity>() {
          @Override public UserEntity apply(HttpResult<UserEntity> userEntityHttpResult)
              throws Exception {
            if (userEntityHttpResult.getCode() == 0) {
              throw new LoginInfoException(userEntityHttpResult.getMsg());
            }
            return userEntityHttpResult.getData();
          }
        });*/
  }

  public static class LoginInfoException extends Exception {

    public LoginInfoException(String message) {
      super(message);
    }
  }

}
