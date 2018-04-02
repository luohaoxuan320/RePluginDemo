package com.lehow.plogin.di;

import android.util.Log;
import com.lehow.plogin.LoginPresenter;
import com.lehow.plogin.biz.LoginApi;
import com.lehow.plogin.biz.LoginBiz;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module public class LoginModule {

  @Provides public LoginPresenter provideLoginPresenter(LoginBiz loginBiz) {
    return new LoginPresenter(loginBiz);
  }

  @Provides public LoginBiz provideLoginBiz(LoginApi loginApi) {
    return new LoginBiz(loginApi);
  }

  @Provides public LoginApi provideLoginApi(Retrofit retrofit) {
    Log.i(getClass().getSimpleName(), "provideLoginApi: retrofit=" + retrofit);
    return retrofit.create(LoginApi.class);
  }
}
